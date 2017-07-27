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

import nl.sonicity.sha2017.cms.cmshabackend.persistence.ActiveClaimRepository;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.CueLocationRepository;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.ZoneMappingRepository;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.entities.ActiveClaim;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.entities.Colour;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.entities.CueLocation;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.entities.ZoneCoordinates;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.entities.ZoneMapping;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:integrationtest.properties")
@ActiveProfiles({"mock-titan", "test"})
public abstract class AbstractRestControllerIT {
    public static final String MYADMINTESTTOKEN =
            "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJDb2xvck15U0hBMjAxNyIsInN1YiI6Imh1Z29AdHJpcHBhZXJzLm5sIiwi" +
                    "bmJmIjoxNDk5NjE2ODI3LCJleHAiOjE1MzExNTI4MjcsImlhdCI6MTQ5OTYxNjgyNywianRpIjoiaWQ0MiIsInR5cCI6Imh0d" +
                    "HBzOi8va3ViZXJuZXRlcy5zdHJvY2FtcC5uZXQifQ.vAFytnvw-T-7E2OKsbclpki2ZmCwAm_uJq3Q2AgVzj9HBd7_Lw_S1Wt" +
                    "id_MoKMwBWNCEN0vne-oqHZgJ0krN5rQHNEoOO7BAjaiPKEzyBQ6l6iWvuavimrpWML0g1Cj2npwZbbcclAHNnCtwDQLKWQnQ" +
                    "gGlR1qtEB3M4pzTkqJEerqC4ZrQXdKx3qchyDoRN4D6lbsuX1N5jgAuqiULcVwF_0y8No_HkpURWWzPY0wPVN7iOi6PAJwIer" +
                    "A-adue6N-zqlIyxNkNoA5ybjaAw01BU5cMPv3Yi_0EqeyDY8Etk4y8kMjKsBdRLPom2smiDpNwYinIoy5qNhiuArq1szdKPwK" +
                    "9IfQ9ByxcMC3mgOadv0nLkViAEEsBRQLDpoyKo6uCaEiaG2Lwhy8VXY2K1XOMlmWKPGwv4DMKR6hmum8e9gCyX_xiWzR1CHMy" +
                    "-Ey632-7-A_2MDxKnUF5KzygmN35L1N6OsYUHARlM0Mcw4gD1v85lJz7AvanMxDx5YAUYhSDsB1KJTaQ-WT5RTmhOLdrBdIDE" +
                    "9SHmZPoUFCTgsmX4NLSeab8yYLm3j4OMj2coA49-C8RgPprp69ClNsKnNHKfXSKqMMemGRwAg4_JlGlgHOXQP8CzdTk-HDc8k" +
                    "n-C-xdWfakPsWvGDyT_rtrNxFCs4ZNrT6zRvIg38WyGZNs";

    @LocalServerPort
    protected int localServerPort;

    @Autowired
    protected RestTemplate restTemplate;

    @Autowired
    protected ZoneMappingRepository zoneMappingRepository;

    @Autowired
    protected ActiveClaimRepository activeClaimRepository;

    @Autowired
    protected CueLocationRepository cueLocationRepository;

    protected void prepareDatabase() {
        ZoneMapping zoneMapping = new ZoneMapping("Zone1", "Group 1", 1111);
        List<ZoneCoordinates> zoneCoordinates = new ArrayList<>();
        zoneCoordinates.add(new ZoneCoordinates(52.033199d, 5.155046d));
        zoneCoordinates.add(new ZoneCoordinates(52.033172d, 5.154831d));
        zoneCoordinates.add(new ZoneCoordinates(52.033117d, 5.154590d));
        zoneMapping.setCoordinatesList(zoneCoordinates);
        zoneMappingRepository.save(zoneMapping);

        ActiveClaim activeClaim = new ActiveClaim(LocalDateTime.now(), Duration.ofSeconds(60), 1113, new Colour(1,0,0));
        zoneMapping = new ZoneMapping("Zone2", "Group 2", 1112);
        zoneMapping.setActiveClaim(activeClaim);
        activeClaimRepository.save(activeClaim);
        zoneMappingRepository.save(zoneMapping);

        CueLocation cueLocation = new CueLocation("PlaybackWindow", 0, 0, false, null);
        cueLocationRepository.save(cueLocation);
        cueLocation = new CueLocation("PlaybackWindow", 0, 1, false, null);
        cueLocationRepository.save(cueLocation);
    }
}
