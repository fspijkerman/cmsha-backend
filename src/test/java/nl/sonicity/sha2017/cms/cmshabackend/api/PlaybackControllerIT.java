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

import nl.sonicity.sha2017.cms.cmshabackend.api.models.Playback;
import nl.sonicity.sha2017.cms.cmshabackend.titan.TitanService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.apis.CatchExceptionHamcrestMatchers.hasMessage;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.IsEqual.equalTo;

public class PlaybackControllerIT extends AbstractRestControllerIT {

    @Before
    public void setUp() throws Exception {
        cueLocationRepository.deleteAll();
        zoneMappingRepository.deleteAll();
        activeClaimRepository.deleteAll();
    }

    @Test
    public void testListPlaybacks() throws Exception {
        prepareDatabase();

        HttpHeaders headers = new HttpHeaders();
        headers.set(AuthenticationFilter.APIKEY_HEADER, MYADMINTESTTOKEN);

        HttpEntity<String> entity = new HttpEntity<String>(null,headers);
        ResponseEntity<Playback[]> responseEntity = restTemplate.exchange("http://localhost:{port}/playback/", HttpMethod.GET, entity, Playback[].class, localServerPort);
        assertThat(responseEntity.getBody().length, equalTo(2));
    }

    @Test
    public void testCreateEmptyPlayback() throws Exception {
        prepareDatabase();

        HttpHeaders headers = new HttpHeaders();
        headers.set(AuthenticationFilter.APIKEY_HEADER, MYADMINTESTTOKEN);

        Playback createPlayback = new Playback("PlaybackWindow", 0, 10, null, null);
        HttpEntity<Playback> entity = new HttpEntity<>(createPlayback, headers);

        ResponseEntity<Void> responseEntity = restTemplate.exchange("http://localhost:{port}/playback/", HttpMethod.POST, entity, Void.class, localServerPort);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
    }

    @Test
    public void testCreateDuplicatePlayback() throws Exception {
        prepareDatabase();

        HttpHeaders headers = new HttpHeaders();
        headers.set(AuthenticationFilter.APIKEY_HEADER, MYADMINTESTTOKEN);

        Playback createPlayback = new Playback("PlaybackWindow", 0, 1, null, null);
        HttpEntity<Playback> entity = new HttpEntity<>(createPlayback, headers);

        catchException(restTemplate).exchange("http://localhost:{port}/playback/", HttpMethod.POST, entity, Void.class, localServerPort);

        Assert.assertThat(caughtException(),
                allOf(
                        instanceOf(HttpClientErrorException.class),
                        hasMessage("409 null")
                )
        );
    }

}