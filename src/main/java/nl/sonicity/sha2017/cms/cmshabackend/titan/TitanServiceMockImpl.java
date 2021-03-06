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

import nl.sonicity.sha2017.cms.cmshabackend.titan.exceptions.PropertyNotFoundException;
import nl.sonicity.sha2017.cms.cmshabackend.titan.models.CreateRgbCueResult;
import nl.sonicity.sha2017.cms.cmshabackend.titan.models.HandleLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by hugo on 05/07/2017.
 */
@Component
@Profile("mock-titan")
public class TitanServiceMockImpl implements TitanService {
    private static final Logger LOG = LoggerFactory.getLogger(TitanServiceMockImpl.class);
    private AtomicInteger atomicInteger = new AtomicInteger(1000);

    @Override
    public boolean groupExists(String groupName) {
        LOG.info("TitanServiceMock: groupExists({}) -> true", groupName);
        return true;
    }

    @Override
    public CreateRgbCueResult createRgbCue(HandleLocation handleLocation, String groupName, float red, float green, float blue) {
        int titanId = atomicInteger.incrementAndGet();
        LOG.info("TitanServiceMock: createRgbCue({}, {}, {}, {}, {}) -> {}", handleLocation, groupName, red, green, blue, titanId);
        return new CreateRgbCueResult(handleLocation, titanId);
    }

    @Override
    public void activateCue(int cueId) {
        LOG.info("TitanServiceMock: activateCue({})", cueId);
    }

    @Override
    public void deactivateCue(int cueId) {
        LOG.info("TitanServiceMock: deactivateCue({})", cueId);
    }

    @Override
    public TitanStatus getStatus() {
        return new TitanStatus("TitanMock", "10.1");
    }

    @Override
    public String getTitanUrl() {
        return "local://mock";
    }

    @Override
    public boolean isHandleActive(HandleLocation handleLocation) {
        LOG.info("TitanServiceMock: isHandleActive({}) -> true", handleLocation);
        return true;
    }

    @Override
    public HandleLocation getHandleLocationFromProperties(String handleName) {
        HandleLocation location;
        switch (handleName) {
            case "flamesafety":
                location = new HandleLocation("PlaybackWindow", 0,0 );
                break;
            case "emergency":
                location = new HandleLocation("PlaybackWindow", 1, 0);
                break;
            default:
                throw new PropertyNotFoundException("Mock not configured");
        }

        LOG.info("TitanServiceMock: getHandleLocationFromProperties({}) -> {}", handleName, location);
        return location;
    }


}
