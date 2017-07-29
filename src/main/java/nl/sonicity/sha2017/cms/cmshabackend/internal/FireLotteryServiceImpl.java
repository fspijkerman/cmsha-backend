/**
 * Copyright © 2017 Sonicity (info@sonicity.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.sonicity.sha2017.cms.cmshabackend.internal;

import nl.sonicity.sha2017.cms.cmshabackend.api.exceptions.ResourceNotFoundException;
import nl.sonicity.sha2017.cms.cmshabackend.internal.exceptions.InvalidSpecialZoneClaimTicket;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.SpecialZoneClaimRepository;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.entities.SpecialZoneClaim;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class FireLotteryServiceImpl implements FireLotteryService {
    private static final Logger LOG = LoggerFactory.getLogger(FireLotteryServiceImpl.class);
    public static final String ZONE_FLAME_THROWERS = "FlameThrowers";

    private SpecialZoneClaimRepository specialZoneClaimRepository;

    private AtomicBoolean fireSystemAvailable = new AtomicBoolean(false);

    public FireLotteryServiceImpl(SpecialZoneClaimRepository specialZoneClaimRepository) {
        this.specialZoneClaimRepository = specialZoneClaimRepository;
    }

    @Override
    public void setFireSystemAvailable(boolean status) {
        boolean oldStatus = fireSystemAvailable.getAndSet(status);
        if (oldStatus != status) {
            LOG.info("Setting FlameSystem availability to {}", status);
        }
    }

    @Override
    public boolean getFireSystemAvailable() {
        return fireSystemAvailable.get();
    }

    /**
     * Enter the draw for the fire lottery
     * if the optional string is present it
     * contains a ticket code to claim the
     * fire system.
     *
     * @return
     */
    @Override
    @Transactional
    public Optional<String> enterDraw() {
        if (!fireSystemAvailable.get()) {
            // Nobody wins if the system is off
            return Optional.empty();
        }

        Optional<SpecialZoneClaim> flameZone = specialZoneClaimRepository.findOneByZoneName(ZONE_FLAME_THROWERS);
        if (!flameZone.isPresent()) {
            SpecialZoneClaim specialZoneClaim = new SpecialZoneClaim(ZONE_FLAME_THROWERS, null, null, null);
            flameZone = Optional.of(specialZoneClaimRepository.save(specialZoneClaim));
        }

        SpecialZoneClaim flamer = flameZone.orElseThrow(() -> new ResourceNotFoundException("Very very wrong..."));

        if (flamer.getClaimTag() != null) {
            // Not your day, already claimed
            return Optional.empty();
        }

        Random random = new Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        int randomValue = random.nextInt(10); // bound should be configurable
        if (!(randomValue == 1)) {
            // Also not your day
            return Optional.empty();
        }

        String claimCode = RandomStringUtils.randomAlphanumeric(8);
        flamer.setClaimTag(claimCode);
        flamer.setClaimed(LocalDateTime.now());
        flamer.setClaimExpiration(Duration.ofMinutes(5));

        specialZoneClaimRepository.save(flamer);
        LOG.info("Generated claimTicket for the {} zone, expires at {}", ZONE_FLAME_THROWERS, flamer.getClaimed().plus(flamer.getClaimExpiration()));

        return Optional.of(claimCode);
    }

    @Override
    public LocalDateTime claim(String claimTicket) {
        SpecialZoneClaim flameZone = specialZoneClaimRepository.findOneByZoneName(ZONE_FLAME_THROWERS)
                .orElseThrow(() -> new ResourceNotFoundException("Zone \"" + ZONE_FLAME_THROWERS + "\" missing"));

        if (flameZone.getClaimTag() == null || !flameZone.getClaimTag().equals(claimTicket)) {
            throw new InvalidSpecialZoneClaimTicket("claim_ticket no longer valid");
        }

        // Add 30 minutes so people can walk to the flamer
        flameZone.setClaimExpiration(flameZone.getClaimExpiration().plus(Duration.ofMinutes(30)));

        flameZone = specialZoneClaimRepository.save(flameZone);
        return flameZone.getClaimed().plus(flameZone.getClaimExpiration());
    }

    @Override
    public LocalDateTime fire(String claimTicket, int sequence) {
        SpecialZoneClaim flameZone = specialZoneClaimRepository.findOneByZoneName(ZONE_FLAME_THROWERS)
                .orElseThrow(() -> new ResourceNotFoundException("Zone \"" + ZONE_FLAME_THROWERS + "\" missing"));

        if (flameZone.getClaimTag() == null || !flameZone.getClaimTag().equals(claimTicket)) {
            throw new InvalidSpecialZoneClaimTicket("claim_ticket no longer valid");
        }

        LocalDateTime currentExpiry = flameZone.getClaimed().plus(flameZone.getClaimExpiration());
        if (currentExpiry.isAfter(LocalDateTime.now().plus(Duration.ofMinutes(2)))) {
            LOG.debug("Resetting expiration to now + 2 minutes");
            long delta = ChronoUnit.SECONDS.between(flameZone.getClaimed(), LocalDateTime.now().plus(Duration.ofMinutes(2)));
            flameZone.setClaimExpiration(Duration.ofSeconds(delta));
            flameZone = specialZoneClaimRepository.save(flameZone);
        }

        //FIXME trigger something on the titan here

        return flameZone.getClaimed().plus(flameZone.getClaimExpiration());
    }

}
