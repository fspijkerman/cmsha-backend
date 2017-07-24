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
package nl.sonicity.sha2017.cms.cmshabackend.persistence.entities;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
public class SpecialZoneClaim {
    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true, nullable = false)
    private String zoneName;

    private String claimTag;
    private LocalDateTime claimed;
    private Duration claimExpiration;

    @Version
    private long optLock = 0L;

    protected SpecialZoneClaim() {
    }

    public SpecialZoneClaim(String zoneName, String claimTag, LocalDateTime claimed, Duration claimExpiration) {
        this.zoneName = zoneName;
        this.claimTag = claimTag;
        this.claimed = claimed;
        this.claimExpiration = claimExpiration;
    }

    public long getId() {
        return id;
    }

    public String getZoneName() {
        return zoneName;
    }

    public String getClaimTag() {
        return claimTag;
    }

    public void setClaimTag(String claimTag) {
        this.claimTag = claimTag;
    }

    public LocalDateTime getClaimed() {
        return claimed;
    }

    public void setClaimed(LocalDateTime claimed) {
        this.claimed = claimed;
    }

    public Duration getClaimExpiration() {
        return claimExpiration;
    }

    public void setClaimExpiration(Duration claimExpiration) {
        this.claimExpiration = claimExpiration;
    }
}
