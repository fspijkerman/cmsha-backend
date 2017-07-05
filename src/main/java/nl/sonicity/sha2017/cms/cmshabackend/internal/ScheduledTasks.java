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

import nl.sonicity.sha2017.cms.cmshabackend.persistence.ZoneMappingRepository;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.entities.ActiveClaim;
import nl.sonicity.sha2017.cms.cmshabackend.titan.TitanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by hugo on 05/07/2017.
 */
@Component
public class ScheduledTasks {
    private static final Logger LOG = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private ZoneMappingRepository zoneMappingRepository;

    @Autowired
    private TitanService titanService;

    @Scheduled(fixedRate = 5000)
    public void expireClaims() {
        LOG.info("The time is now {}", LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE));

        zoneMappingRepository.findByActiveClaimIsNotNull().forEach(zoneMapping -> {
            ActiveClaim activeClaim = zoneMapping.getActiveClaim();
            if (activeClaim.getCreated().plus(activeClaim.getExpiration()).isBefore(LocalDateTime.now())) {
                LOG.info("Zone {} has an expiring claim, stopping playback on titanId {}", zoneMapping.getZoneName(), activeClaim.getPlaybackTitanId());
                titanService.deactivateCue(activeClaim.getPlaybackTitanId());
                zoneMapping.setActiveClaim(null);
                zoneMappingRepository.save(zoneMapping);
            }
        });
    }
}
