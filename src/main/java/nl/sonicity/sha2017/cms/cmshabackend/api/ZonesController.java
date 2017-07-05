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
package nl.sonicity.sha2017.cms.cmshabackend.api;

import nl.sonicity.sha2017.cms.cmshabackend.api.models.Claim;
import nl.sonicity.sha2017.cms.cmshabackend.api.models.ExtendedZone;
import nl.sonicity.sha2017.cms.cmshabackend.api.models.Zone;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.ActiveClaimRepository;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.ZoneMappingRepository;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.entities.ActiveClaim;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.entities.ZoneMapping;
import nl.sonicity.sha2017.cms.cmshabackend.titan.TitanService;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by hugo on 02/07/2017.
 */
@RestController
@RequestMapping("/zones")
public class ZonesController {
    private ZoneMappingRepository zoneMappingRepository;
    private ActiveClaimRepository activeClaimRepository;
    private TitanService titanService;

    public ZonesController(ZoneMappingRepository zoneMappingRepository, ActiveClaimRepository activeClaimRepository, TitanService titanService) {
        this.zoneMappingRepository = zoneMappingRepository;
        this.activeClaimRepository = activeClaimRepository;
        this.titanService = titanService;
    }

    @RequestMapping(path="/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("permitAll")
    public List<Zone> listZones() {
        Spliterator<ZoneMapping> spliterator = zoneMappingRepository.findAll().spliterator();
        return StreamSupport.stream(spliterator, false).map(m -> new Zone(m.getZoneName())).collect(Collectors.toList());
    }

    @RequestMapping(path="/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Zone newZone(@RequestBody ExtendedZone extendedZone) {
        if (!titanService.groupExists(extendedZone.getGroupName())) {
            throw new ResourceNotFoundException(String.format("No group with name \"%s\" is configured on the console", extendedZone.getGroupName()));
        }

        ZoneMapping zoneMapping = new ZoneMapping(extendedZone.getName(), extendedZone.getGroupName(), null);
        zoneMappingRepository.save(zoneMapping);
        return extendedZone;
    }

    @RequestMapping(path="/{zoneName}/claim", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasAuthority('ANONYMOUS') or hasRole('ROLE_ADMIN')")
    public Zone claimZone(@PathVariable("zoneName") String zoneName, @RequestBody Claim claim) throws Exception {
        ZoneMapping zoneMapping = zoneMappingRepository.findOneByZoneName(zoneName)
                .orElseThrow(() -> new ResourceNotFoundException("No mapping found for zone " + zoneName));

        int playbackId = titanService.createRgbCue(zoneMapping.getTitanGroupName(), claim.getRed(), claim.getGreen(), claim.getBlue());
        titanService.activateCue(playbackId);

        ActiveClaim activeClaim = new ActiveClaim(LocalDateTime.now(), Duration.ofMinutes(10), playbackId);
        activeClaimRepository.save(activeClaim);

        zoneMapping.setActiveClaim(activeClaim);
        zoneMappingRepository.save(zoneMapping);

        return new Zone(zoneMapping.getZoneName());
    }
}
