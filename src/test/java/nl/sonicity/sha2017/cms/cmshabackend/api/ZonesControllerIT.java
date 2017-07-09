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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.apis.CatchExceptionHamcrestMatchers.hasMessage;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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
public class ZonesControllerIT {
    @LocalServerPort
    private int localServerPort;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ZoneMappingRepository zoneMappingRepository;

    @Autowired
    private ActiveClaimRepository activeClaimRepository;

    @MockBean
    private TitanService titanService;

    @Before
    public void setUp() throws Exception {
        zoneMappingRepository.deleteAll();
        activeClaimRepository.deleteAll();
    }

    @Test
    public void testZonesListAnonymous() throws Exception {
        prepareDatabase();

        Zone[] zones = restTemplate.getForObject("http://localhost:{port}/zones/", Zone[].class, localServerPort);
        assertThat(zones.length, equalTo(2));
        assertThat(zones[0].getName(), equalTo("Zone1"));
        assertThat(zones[0].getAvailable(), equalTo(true));

        assertThat(zones[1].getName(), equalTo("Zone2"));
        assertThat(zones[1].getAvailable(), equalTo(false));
    }

    @Test
    public void testZonesListWithAdminApiKey() throws Exception {
        prepareDatabase();

        HttpHeaders headers = new HttpHeaders();
        headers.set(AuthenticationFilter.APIKEY_HEADER, "myadmintesttoken");

        HttpEntity<String> entity = new HttpEntity<String>(null,headers);
        ResponseEntity<Zone[]> zones = restTemplate.exchange("http://localhost:{port}/zones/", HttpMethod.GET, entity, Zone[].class, localServerPort);
        assertThat(zones.getBody().length, equalTo(2));
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
        Zone zone = new Zone("TestZone", true);
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
        headers.set(AuthenticationFilter.APIKEY_HEADER, "myadmintesttoken");

        ExtendedZone zone = new ExtendedZone("TestZone1", true, "Dim 1");
        HttpEntity<Zone> zoneHttpEntity = new HttpEntity<>(zone, headers);
        ResponseEntity<Zone> createdZoneEntity = restTemplate.exchange("http://localhost:{port}/zones/", HttpMethod.POST, zoneHttpEntity, Zone.class, localServerPort);

        assertThat(createdZoneEntity.getBody().getName(), equalTo(zone.getName()));
        assertThat(createdZoneEntity.getBody().getAvailable(), equalTo(true));

        assertThat(zoneMappingRepository.findOneByZoneName("TestZone1").isPresent(), equalTo(true));
    }

    @Test
    public void testAddExistingZoneWithApiKey() throws Exception {
        prepareDatabase();

        when(titanService.groupExists(any())).thenReturn(true);

        HttpHeaders headers = new HttpHeaders();
        headers.set(AuthenticationFilter.APIKEY_HEADER, "myadmintesttoken");

        ExtendedZone zone = new ExtendedZone("Zone1", true, "Dim 1");
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

        when(titanService.createRgbCue(any(), anyFloat(), anyFloat(), anyFloat())).thenReturn(1500);
        when(titanService.groupExists(any())).thenReturn(true);

        Claim claim = new Claim(1,1, 1);
        restTemplate.put("http://localhost:{port}/zones/{zonename}/claim", claim, localServerPort, "Zone1");

        assertThat(activeClaimRepository.findAll().iterator().hasNext(), equalTo(true));

        ZoneMapping persistedZone = zoneMappingRepository.findOneByZoneName("Zone1")
                .orElseThrow(() -> new Exception("Persisted zone not found"));

        assertThat(persistedZone.getActiveClaim(), not(nullValue()));
    }

    @Test
    public void testClaimZoneWithInvalidValue() throws Exception {
        prepareDatabase();

        when(titanService.createRgbCue(any(), anyFloat(), anyFloat(), anyFloat())).thenReturn(1500);
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

        when(titanService.createRgbCue(any(), anyFloat(), anyFloat(), anyFloat())).thenReturn(1500);
        when(titanService.groupExists(any())).thenReturn(true);

        // Create the zone as admin fro now
        HttpHeaders headers = new HttpHeaders();
        headers.set(AuthenticationFilter.APIKEY_HEADER, "myadmintesttoken");

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

        when(titanService.createRgbCue(any(), anyFloat(), anyFloat(), anyFloat())).thenThrow(new ResourceAccessException("Simulated connection error"));

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
        zoneMappingRepository.save(zoneMapping);

        ActiveClaim activeClaim = new ActiveClaim(LocalDateTime.now(), Duration.ofSeconds(60), 1113);
        zoneMapping = new ZoneMapping("Zone2", "Group 2", 1112);
        zoneMapping.setActiveClaim(activeClaim);
        activeClaimRepository.save(activeClaim);
        zoneMappingRepository.save(zoneMapping);
    }
}
