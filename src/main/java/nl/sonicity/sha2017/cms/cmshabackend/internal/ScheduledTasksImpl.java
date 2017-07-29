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

import nl.sonicity.sha2017.cms.cmshabackend.persistence.ActiveClaimRepository;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.CueLocationRepository;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.SpecialZoneClaimRepository;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.ZoneMappingRepository;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.entities.ActiveClaim;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.entities.CueLocation;
import nl.sonicity.sha2017.cms.cmshabackend.titan.TitanService;
import nl.sonicity.sha2017.cms.cmshabackend.titan.models.HandleLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Created by hugo on 05/07/2017.
 */
@Component
@Profile("!test")
public class ScheduledTasksImpl implements ScheduledTasks {
    private static final Logger LOG = LoggerFactory.getLogger(ScheduledTasksImpl.class);

    private ZoneMappingRepository zoneMappingRepository;
    private CueLocationRepository cueLocationRepository;
    private ActiveClaimRepository activeClaimRepository;
    private SpecialZoneClaimRepository specialZoneClaimRepository;
    private TitanService titanService;
    private FireLotteryService fireLotteryService;

    private HandleLocation flameSafetyHandleLocation;
    private HandleLocation emergencyHandleLocation;

    public ScheduledTasksImpl(ZoneMappingRepository zoneMappingRepository, CueLocationRepository cueLocationRepository, ActiveClaimRepository activeClaimRepository,
                          SpecialZoneClaimRepository specialZoneClaimRepository, TitanService titanService, FireLotteryService fireLotteryService) {
        this.zoneMappingRepository = zoneMappingRepository;
        this.cueLocationRepository = cueLocationRepository;
        this.activeClaimRepository = activeClaimRepository;
        this.specialZoneClaimRepository = specialZoneClaimRepository;
        this.titanService = titanService;

        flameSafetyHandleLocation = titanService.getHandleLocationFromProperties("flamesafety");
        emergencyHandleLocation = titanService.getHandleLocationFromProperties("emergency");
        this.fireLotteryService = fireLotteryService;

        LOG.info("Configured flame safety cue to {}", flameSafetyHandleLocation);
        LOG.info("Configured emergency cue to {}", emergencyHandleLocation);
    }

    @Override
    @Scheduled(fixedRate = 5000)
    public void expireClaims() {
        zoneMappingRepository.findByActiveClaimIsNotNull().forEach(zoneMapping -> {
            ActiveClaim activeClaim = zoneMapping.getActiveClaim();
            LOG.debug("Claim {} -> created at {} and expires at {}", activeClaim.getId(), activeClaim.getCreated(), activeClaim.getCreated().plus(activeClaim.getExpiration()));
            if (activeClaim.getCreated().plus(activeClaim.getExpiration()).isBefore(LocalDateTime.now())) {
                LOG.info("Zone {} has an expiring claim, stopping playback on titanId {}", zoneMapping.getZoneName(), activeClaim.getPlaybackTitanId());
                titanService.deactivateCue(activeClaim.getPlaybackTitanId());

                Optional<CueLocation> reservation = cueLocationRepository.findOneByActiveClaim(activeClaim);
                if (!reservation.isPresent()) {
                    LOG.warn("Disabling cue without a cue location");
                } else {
                    CueLocation location = reservation.get();
                    location.setActiveClaim(null);
                    location.setReserved(false);
                    cueLocationRepository.save(location);
                }

                zoneMapping.setActiveClaim(null);
                zoneMappingRepository.save(zoneMapping);
                activeClaimRepository.delete(activeClaim.getId());
            }
        });
    }

    @Override
    @Scheduled(fixedRate = 5000)
    public void expireSpecialZoneClaims() {
        specialZoneClaimRepository.findAll().forEach(specialZoneClaim -> {
            if (specialZoneClaim.getClaimTag() != null &&
                    specialZoneClaim.getClaimed().plus(specialZoneClaim.getClaimExpiration()).isBefore(LocalDateTime.now())) {
                LOG.info("SpecialZoneClaim for zone {} has expired", specialZoneClaim.getZoneName());

                specialZoneClaim.setClaimTag(null);
                specialZoneClaim.setClaimed(null);
                specialZoneClaim.setClaimExpiration(null);
                specialZoneClaimRepository.save(specialZoneClaim);
            }
        });
    }

    @Override
    @Scheduled(fixedRate = 5000)
    public void updateActivationStatus() {
        fireLotteryService.setFireSystemAvailable(titanService.isHandleActive(flameSafetyHandleLocation));
    }
}
