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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class FireLotteryServiceImpl implements FireLotteryService {
    private static final Logger LOG = LoggerFactory.getLogger(FireLotteryServiceImpl.class);

    private AtomicBoolean fireSystemAvailable = new AtomicBoolean(false);

    @Override
    public void setFireSystemAvailable(boolean status) {
        boolean oldStatus = fireSystemAvailable.getAndSet(status);
        if (oldStatus != status) {
            LOG.info("Setting FlameSystem availability to {}", status);
        }
    }

    /**
     * Enter the draw for the fire lottery
     * if the optional string is present it
     * contains a ticket code to claim the
     * fire system.
     *
     * @return
     */
    public Optional<String> enterDraw() {
       if (!fireSystemAvailable.get()) {
           // Nobody wins if the system is off
           return Optional.empty();
       }

       // Check if there already is valid claim ticket
       // exit if there is

       // If not generate a new ticket and try to persist it
       // if that works return the ticket

       return Optional.empty(); // FIXME
    }
}
