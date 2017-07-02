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
import nl.sonicity.sha2017.cms.cmshabackend.api.models.Zone;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hugo on 02/07/2017.
 */
@RestController
@RequestMapping("/zones")
public class ZonesController {
    List<Zone> zones = new ArrayList<>();

    @RequestMapping(path="/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("permitAll")
    public List<Zone> listZones() {
        return Collections.unmodifiableList(zones);
    }

    @RequestMapping(path="/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Zone newZone(@RequestBody Zone zone) {
        zones.add(zone);
        return zone;
    }

    @RequestMapping(path="/{zoneName}/claim", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasAuthority('ANONYMOUS') or hasRole('ROLE_ADMIN')")
    public Zone claimZone(@PathVariable("zoneName") String zoneName, @RequestBody Claim claim) throws Exception {
        return zones
                .stream()
                .filter(z -> z.getName().equals(zoneName))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Zone \"%s\"not found", zoneName)));
    }
}
