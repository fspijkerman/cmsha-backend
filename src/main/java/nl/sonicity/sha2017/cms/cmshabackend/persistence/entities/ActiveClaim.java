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
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Created by hugo on 05/07/2017.
 */
@Entity
public class ActiveClaim implements Serializable {
    @Id
    @GeneratedValue
    private long id;

    @OneToOne(mappedBy = "activeClaim")
    private ZoneMapping zoneMapping;

    @Column(nullable = false)
    private LocalDateTime created;

    @Column(nullable = false)
    private Duration expiration;

    @Column(nullable = false)
    private Integer playbackTitanId;

    @Embedded
    Colour colour;

    protected ActiveClaim() {}

    public ActiveClaim(LocalDateTime created, Duration expiration, Integer playbackTitanId, Colour colour) {
        this.created = created;
        this.expiration = expiration;
        this.playbackTitanId = playbackTitanId;
        this.colour = colour;
    }

    public long getId() {
        return id;
    }

    public ZoneMapping getZoneMapping() {
        return zoneMapping;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public Duration getExpiration() {
        return expiration;
    }

    public Integer getPlaybackTitanId() {
        return playbackTitanId;
    }

    public Colour getColour() {
        return colour;
    }
}
