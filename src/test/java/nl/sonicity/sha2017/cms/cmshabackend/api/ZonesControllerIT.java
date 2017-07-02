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
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.apis.CatchExceptionHamcrestMatchers.hasMessage;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.IsEqual.equalTo;

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

    @Test
    public void testZonesListAnonymous() throws Exception {
        Zone[] zones = restTemplate.getForObject("http://localhost:{port}/zones/", Zone[].class, localServerPort);
        assertThat(zones.length, equalTo(0));
    }

    @Test
    public void testZonesListWithAdminApiKey() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set(AuthenticationFilter.APIKEY_HEADER, "myadmintesttoken");

        HttpEntity<String> entity = new HttpEntity<String>(null,headers);
        ResponseEntity<Zone[]> zones = restTemplate.exchange("http://localhost:{port}/zones/", HttpMethod.GET, entity, Zone[].class, localServerPort);
        assertThat(zones.getBody().length, equalTo(0));
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
        Zone zone = new Zone("TestZone");
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
        HttpHeaders headers = new HttpHeaders();
        headers.set(AuthenticationFilter.APIKEY_HEADER, "myadmintesttoken");

        Zone zone = new Zone("TestZone");
        HttpEntity<Zone> zoneHttpEntity = new HttpEntity<>(zone, headers);
        ResponseEntity<Zone> createdZoneEntity = restTemplate.exchange("http://localhost:{port}/zones/", HttpMethod.POST, zoneHttpEntity, Zone.class, localServerPort);

        assertThat(createdZoneEntity.getBody().getName(), equalTo(zone.getName()));

        HttpEntity<Void> voidHttpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<Zone[]> zones = restTemplate.exchange("http://localhost:{port}/zones/", HttpMethod.GET, voidHttpEntity, Zone[].class, localServerPort);
        assertThat(zones.getBody().length, equalTo(1));
    }

    @Test
    public void testClaimZone() throws Exception {
        // Create the zone as admin fro now
        HttpHeaders headers = new HttpHeaders();
        headers.set(AuthenticationFilter.APIKEY_HEADER, "myadmintesttoken");

        Zone zone = new Zone("TestZone");
        HttpEntity<Zone> zoneHttpEntity = new HttpEntity<>(zone, headers);
        ResponseEntity<Zone> createdZoneEntity = restTemplate.exchange("http://localhost:{port}/zones/", HttpMethod.POST, zoneHttpEntity, Zone.class, localServerPort);

        assertThat(createdZoneEntity.getBody().getName(), equalTo(zone.getName()));

        Claim claim = new Claim(1,1, 1);
        restTemplate.put("http://localhost:{port}/zones/{zonename}/claim", claim, localServerPort, zone.getName());
    }
}
