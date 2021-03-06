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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by hugo on 02/07/2017.
 */
public class ActuatorsIT extends AbstractRestControllerIT {
    @Test
    public void testHealthAnonymous() throws Exception {
        // Should only give the consolidated status
        String health = restTemplate.getForObject("http://localhost:{port}/health/", String.class, localServerPort);
        assertThat(health, equalTo("{\"status\":\"UP\"}"));
    }

    @Test
    public void testHealthWithAdminApiKey() throws Exception {
        // Should give the complete status
        HttpHeaders headers = new HttpHeaders();
        headers.set(AuthenticationFilter.APIKEY_HEADER, MYADMINTESTTOKEN);

        HttpEntity<String> entity = new HttpEntity<String>(null,headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:{port}/health/", HttpMethod.GET, entity,String.class, localServerPort);
        assertThat(responseEntity.getBody(), containsString("{\"status\":\"UP\",\"titanDispatcher\""));
    }

}
