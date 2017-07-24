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
import nl.sonicity.sha2017.cms.cmshabackend.api.models.Coordinate;
import nl.sonicity.sha2017.cms.cmshabackend.api.models.ExtendedZone;
import nl.sonicity.sha2017.cms.cmshabackend.api.models.Zone;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.ActiveClaimRepository;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.CueLocationRepository;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.ZoneMappingRepository;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.entities.*;
import nl.sonicity.sha2017.cms.cmshabackend.titan.TitanService;
import nl.sonicity.sha2017.cms.cmshabackend.titan.models.CreateRgbCueResult;
import nl.sonicity.sha2017.cms.cmshabackend.titan.models.HandleLocation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.apis.CatchExceptionHamcrestMatchers.hasMessage;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Mockito.when;

/**
 * Created by hugo on 02/07/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:integrationtest.properties")
@ActiveProfiles({"mock-titan", "test"})
public class ZonesControllerIT {
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
    private int localServerPort;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplate restTemplateB;

    @Autowired
    private ZoneMappingRepository zoneMappingRepository;

    @Autowired
    private ActiveClaimRepository activeClaimRepository;

    @Autowired
    private CueLocationRepository cueLocationRepository;

    @MockBean
    private TitanService titanService;

    @Before
    public void setUp() throws Exception {
        cueLocationRepository.deleteAll();
        zoneMappingRepository.deleteAll();
        activeClaimRepository.deleteAll();
    }

    @Test
    public void testZonesListAnonymous() throws Exception {
        prepareDatabase();

        Zone[] zones = restTemplate.getForObject("http://localhost:{port}/zones/", Zone[].class, localServerPort);
        assertThat(zones.length, equalTo(3));

        Zone availableZone = Stream.of(zones)
                .filter(z -> "Zone1".equals(z.getName()))
                .findAny()
                .orElseThrow(() -> new Exception("There should be a Zone1"));

        Assert.assertThat(availableZone.getAvailable(), equalTo(true));
        Assert.assertThat(availableZone.getColour(), is(nullValue()));

        Zone unavailableZone = Stream.of(zones)
                .filter(z -> "Zone2".equals(z.getName()))
                .findAny()
                .orElseThrow(() -> new Exception("There should be a Zone2"));

        Assert.assertThat(unavailableZone.getAvailable(), equalTo(false));
        Assert.assertThat(unavailableZone.getColour(), equalTo("ff0000"));

        Zone flameThrowerZone =  Stream.of(zones)
                .filter(z -> "FlameThrowers".equals(z.getName()))
                .findAny()
                .orElseThrow(() -> new Exception("There should be a FlameThrowers"));

        Assert.assertThat(flameThrowerZone.getAvailable(), equalTo(false));
        Assert.assertThat(flameThrowerZone.getColour(), is(nullValue()));

    }

    @Test
    public void testZonesListWithAdminApiKey() throws Exception {
        prepareDatabase();

        HttpHeaders headers = new HttpHeaders();
        headers.set(AuthenticationFilter.APIKEY_HEADER, MYADMINTESTTOKEN);

        HttpEntity<String> entity = new HttpEntity<String>(null,headers);
        ResponseEntity<Zone[]> zones = restTemplate.exchange("http://localhost:{port}/zones/", HttpMethod.GET, entity, Zone[].class, localServerPort);
        assertThat(zones.getBody().length, equalTo(3));
    }

    @Test
    public void testZonesListWithInvalidAdminApiKey() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set(AuthenticationFilter.APIKEY_HEADER, "notavalidtoken");

        HttpEntity<String> entity = new HttpEntity<String>(null,headers);

        catchException(restTemplate).exchange("http://localhost:{port}/zones/", HttpMethod.GET, entity, Zone[].class, localServerPort);

        Assert.assertThat(caughtException(),
                allOf(
                        instanceOf(HttpClientErrorException.class),
                        hasMessage("401 null")
                )
        );
    }

    @Test
    public void testAddZoneAnonymous() throws Exception {
        Zone zone = new Zone("TestZone", true, null, Collections.emptyList(), null);
        catchException(restTemplate).postForObject("http://localhost:{port}/zones/", zone, Zone.class, localServerPort);

        Assert.assertThat(caughtException(),
                allOf(
                        instanceOf(HttpClientErrorException.class),
                        hasMessage("403 null")
                )
        );
    }

    @Test
    public void testAddZoneWithApiKey() throws Exception {
        when(titanService.groupExists(any())).thenReturn(true);

        HttpHeaders headers = new HttpHeaders();
        headers.set(AuthenticationFilter.APIKEY_HEADER, MYADMINTESTTOKEN);

        ExtendedZone zone = new ExtendedZone("TestZone1", "Dim 1", Collections.emptyList());
        HttpEntity<Zone> zoneHttpEntity = new HttpEntity<>(zone, headers);
        ResponseEntity<Zone> createdZoneEntity = restTemplate.exchange("http://localhost:{port}/zones/", HttpMethod.POST, zoneHttpEntity, Zone.class, localServerPort);

        assertThat(createdZoneEntity.getBody().getName(), equalTo(zone.getName()));
        assertThat(createdZoneEntity.getBody().getAvailable(), equalTo(true));

        assertThat(zoneMappingRepository.findOneByZoneName("TestZone1").isPresent(), equalTo(true));
    }

    @Test
    public void testAddZoneWithApiKeyAndCoordinates() throws Exception {
        when(titanService.groupExists(any())).thenReturn(true);

        HttpHeaders headers = new HttpHeaders();
        headers.set(AuthenticationFilter.APIKEY_HEADER, MYADMINTESTTOKEN);

        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(52.033199d, 5.155046d));
        coordinates.add(new Coordinate(52.033172d, 5.154831d));
        coordinates.add(new Coordinate(52.033117d, 5.154590d));

        ExtendedZone zone = new ExtendedZone("TestZone1", "Dim 1", coordinates);
        HttpEntity<Zone> zoneHttpEntity = new HttpEntity<>(zone, headers);
        ResponseEntity<Zone> createdZoneEntity = restTemplate.exchange("http://localhost:{port}/zones/", HttpMethod.POST, zoneHttpEntity, Zone.class, localServerPort);

        assertThat(createdZoneEntity.getBody().getName(), equalTo(zone.getName()));
        assertThat(createdZoneEntity.getBody().getAvailable(), equalTo(true));

        assertThat(zoneMappingRepository.findOneByZoneName("TestZone1").isPresent(), equalTo(true));
        ZoneMapping zoneMapping = zoneMappingRepository.findOneByZoneName("TestZone1").get();
        assertThat(zoneMapping.getCoordinatesList().size(), equalTo(3));
    }

    @Test
    public void testAddExistingZoneWithApiKey() throws Exception {
        prepareDatabase();

        when(titanService.groupExists(any())).thenReturn(true);

        HttpHeaders headers = new HttpHeaders();
        headers.set(AuthenticationFilter.APIKEY_HEADER, MYADMINTESTTOKEN);

        ExtendedZone zone = new ExtendedZone("Zone1", "Dim 1", Collections.emptyList());
        HttpEntity<Zone> zoneHttpEntity = new HttpEntity<>(zone, headers);
        catchException(restTemplate).exchange("http://localhost:{port}/zones/", HttpMethod.POST, zoneHttpEntity, Zone.class, localServerPort);

        Assert.assertThat(caughtException(),
                allOf(
                        instanceOf(HttpClientErrorException.class),
                        hasMessage("400 null")
                )
        );

    }

    @Test
    public void testClaimZone() throws Exception {
        prepareDatabase();

        CreateRgbCueResult cueResult = new CreateRgbCueResult(new HandleLocation("Testpage", 0, 0), 1111);
        when(titanService.createRgbCue(any(), any(), anyFloat(), anyFloat(), anyFloat())).thenReturn(cueResult);
        when(titanService.groupExists(any())).thenReturn(true);

        Claim claim = new Claim(1,1, 1);
        restTemplate.put("http://localhost:{port}/zones/{zonename}/claim", claim, localServerPort, "Zone1");

        assertThat(activeClaimRepository.findAll().iterator().hasNext(), equalTo(true));

        ZoneMapping persistedZone = zoneMappingRepository.findOneByZoneName("Zone1")
                .orElseThrow(() -> new Exception("Persisted zone not found"));

        assertThat(persistedZone.getActiveClaim(), not(nullValue()));
    }

    @Test
    @SuppressWarnings("squid:S2925")
    public void testClaimZoneConcurrency() throws Exception {
        prepareDatabase();

        ZoneMapping zoneMapping = new ZoneMapping("Zone3", "Group 1", 1111);
        zoneMappingRepository.save(zoneMapping);

        zoneMapping = new ZoneMapping("Zone4", "Group 1", 1111);
        zoneMappingRepository.save(zoneMapping);


        CreateRgbCueResult cueResult = new CreateRgbCueResult(new HandleLocation("Testpage", 0, 0), 1111);
        when(titanService.createRgbCue(any(), any(), anyFloat(), anyFloat(), anyFloat())).then((c) -> {
            Thread.sleep(5000);
            return cueResult;
        }).thenReturn(cueResult);
        when(titanService.groupExists(any())).thenReturn(true);

        Thread t1 = new Thread(() -> {

            Claim claim = new Claim(1,1, 1);
            restTemplate.put("http://localhost:{port}/zones/{zonename}/claim", claim, localServerPort, "Zone3");

            assertThat(activeClaimRepository.findAll().iterator().hasNext(), equalTo(true));

            ZoneMapping persistedZone = zoneMappingRepository.findOneByZoneName("Zone3")
                    .orElseThrow(() -> new RuntimeException("Persisted zone not found"));

            assertThat(persistedZone.getActiveClaim(), not(nullValue()));
        });

        Thread t2 = new Thread(() -> {
            Claim claim = new Claim(1,1, 1);
            restTemplateB.put("http://localhost:{port}/zones/{zonename}/claim", claim, localServerPort, "Zone4");

            assertThat(activeClaimRepository.findAll().iterator().hasNext(), equalTo(true));

            ZoneMapping persistedZone = zoneMappingRepository.findOneByZoneName("Zone4")
                    .orElseThrow(() -> new RuntimeException("Persisted zone not found"));

            assertThat(persistedZone.getActiveClaim(), not(nullValue()));
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        // If this went well both locations should have been claimed
        assertThat(cueLocationRepository.findAllByActiveClaimIsNull().size(), equalTo(0));
    }

    @Test
    public void testClaimZoneWithInvalidValue() throws Exception {
        prepareDatabase();

        CreateRgbCueResult cueResult = new CreateRgbCueResult(new HandleLocation("Testpage", 0, 0), 1111);
        when(titanService.createRgbCue(any(), any(), anyFloat(), anyFloat(), anyFloat())).thenReturn(cueResult);
        when(titanService.groupExists(any())).thenReturn(true);

        Claim claim = new Claim(1,1, 9);
        catchException(restTemplate).put("http://localhost:{port}/zones/{zonename}/claim", claim, localServerPort, "Zone1");

        Assert.assertThat(caughtException(),
                allOf(
                        instanceOf(HttpClientErrorException.class),
                        hasMessage("400 null")
                )
        );
    }

    @Test
    public void testClaimZoneAlreadyClaimed() throws Exception {
        prepareDatabase();

        CreateRgbCueResult cueResult = new CreateRgbCueResult(new HandleLocation("Testpage", 0, 0), 1111);
        when(titanService.createRgbCue(any(), any(), anyFloat(), anyFloat(), anyFloat())).thenReturn(cueResult);
        when(titanService.groupExists(any())).thenReturn(true);

        // Create the zone as admin fro now
        HttpHeaders headers = new HttpHeaders();
        headers.set(AuthenticationFilter.APIKEY_HEADER, MYADMINTESTTOKEN);

        Claim claim = new Claim(1,1, 1);
        catchException(restTemplate).put("http://localhost:{port}/zones/{zonename}/claim", claim, localServerPort, "Zone2");

        Assert.assertThat(caughtException(),
                allOf(
                        instanceOf(HttpClientErrorException.class),
                        hasMessage("409 null")
                )
        );

    }

    @Test
    public void testUnhandledException() throws Exception {
        prepareDatabase();

        when(titanService.createRgbCue(any(), any(), anyFloat(), anyFloat(), anyFloat())).thenThrow(new ResourceAccessException("Simulated connection error"));

        Claim claim = new Claim(1,1, 1);
        catchException(restTemplate).put("http://localhost:{port}/zones/{zonename}/claim", claim, localServerPort, "Zone1");

        Assert.assertThat(caughtException(),
                allOf(
                        instanceOf(HttpServerErrorException.class),
                        hasMessage("500 null")
                )
        );

        HttpServerErrorException e = caughtException();
        assertThat(e.getResponseBodyAsString(), equalTo("{\"message\":\"Unhandled exception, please contact operator\"}"));
    }

    private void prepareDatabase() {
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
