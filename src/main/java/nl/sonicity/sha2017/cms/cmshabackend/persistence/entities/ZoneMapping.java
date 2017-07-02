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

/**
 * Created by hugo on 02/07/2017.
 */
@Entity
@Table(indexes = { @Index(name="idx_zoneName", columnList = "zoneName", unique = true) })
public class ZoneMapping implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String zoneName;

    @Column(nullable = false)
    private String titanGroupName;

    @Column
    private Integer titanGroupId;

    protected ZoneMapping() {}

    public ZoneMapping(String zoneName, String titanGroupName, Integer titanGroupId) {
        this.zoneName = zoneName;
        this.titanGroupName = titanGroupName;
        this.titanGroupId = titanGroupId;
    }

    public long getId() {
        return id;
    }

    public String getZoneName() {
        return zoneName;
    }

    public String getTitanGroupName() {
        return titanGroupName;
    }

    public Integer getTitanGroupId() {
        return titanGroupId;
    }
}
