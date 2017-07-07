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
package nl.sonicity.sha2017.cms.cmshabackend.titan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Created by hugo on 02/07/2017.
 */
@Component
public class TitanDispatcherHealthIndicator implements HealthIndicator {
    @Autowired
    private TitanService titanService;

    @Override
    public Health health() {
        try {
            TitanStatus titanStatus = titanService.getStatus();
            return Health.up()
                    .withDetail("showName", titanStatus.getShowName())
                    .withDetail("titanVersion", titanStatus.getTitanVersion())
                    .withDetail("titanBaseUrl", titanService.getTitanUrl())
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("titanBaseUrl", titanService.getTitanUrl())
                    .withException(e)
                    .build();
        }
    }
}
