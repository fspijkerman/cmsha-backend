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
import nl.sonicity.sha2017.cms.cmshabackend.persistence.entities.*;
import nl.sonicity.sha2017.cms.cmshabackend.titan.TitanServiceMockImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:integrationtest.properties")
@ActiveProfiles({"mock-titan", "test"})
public class ScheduledTasksIT {
    @Autowired
    private ZoneMappingRepository zoneMappingRepository;

    @Autowired
    private ActiveClaimRepository activeClaimRepository;

    @Autowired
    private CueLocationRepository cueLocationRepository;

    @Autowired
    private SpecialZoneClaimRepository specialZoneClaimRepository;

    @MockBean
    private FireLotteryService fireLotteryService;

    private ScheduledTasks scheduledTasks;

    @Before
    public void setUp() throws Exception {
        scheduledTasks = new ScheduledTasks(zoneMappingRepository, cueLocationRepository, activeClaimRepository, specialZoneClaimRepository, new TitanServiceMockImpl(), fireLotteryService);
    }

    @Test
    public void testExpiration() throws Exception {
        prepareDatabase();

        scheduledTasks.expireClaims();

        ZoneMapping zoneMapping = zoneMappingRepository.findOneByZoneName("Zone1")
                .orElseThrow(() -> new Exception("Zone1 should still be there"));
        assertThat(zoneMapping.getActiveClaim(), is(nullValue()));

        assertThat(activeClaimRepository.count(), equalTo(0l));

        CueLocation location = cueLocationRepository.findOne(new CueLocation.CueLocationPk("PlaybackWindow", 0, 0));
        assertThat(location.getActiveClaim(), is(nullValue()));
        assertThat(location.getReserved(), equalTo(Boolean.FALSE));
    }

    private void prepareDatabase() {
        ZoneMapping zoneMapping = new ZoneMapping("Zone1", "Group 1", 1111);
        List<ZoneCoordinates> zoneCoordinates = new ArrayList<>();
        zoneCoordinates.add(new ZoneCoordinates(52.033199d, 5.155046d));
        zoneCoordinates.add(new ZoneCoordinates(52.033172d, 5.154831d));
        zoneCoordinates.add(new ZoneCoordinates(52.033117d, 5.154590d));
        zoneMapping.setCoordinatesList(zoneCoordinates);
        zoneMappingRepository.save(zoneMapping);

        ActiveClaim activeClaim = new ActiveClaim(LocalDateTime.of(2017,1,1,13,00, 0), Duration.ofSeconds(60), 1113, new Colour(1,0,0));
        zoneMapping = new ZoneMapping("Zone2", "Group 2", 1112);
        zoneMapping.setActiveClaim(activeClaim);
        activeClaimRepository.save(activeClaim);
        zoneMappingRepository.save(zoneMapping);

        CueLocation cueLocation = new CueLocation("PlaybackWindow", 0, 0, true, activeClaim);
        cueLocationRepository.save(cueLocation);
        cueLocation = new CueLocation("PlaybackWindow", 0, 1, false, null);
        cueLocationRepository.save(cueLocation);
    }

}