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
package nl.sonicity.sha2017.cms.cmshabackend.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by hugo on 02/07/2017.
 */
public class ExtendedZone extends Zone {
    private String groupName;

    @JsonCreator
    public ExtendedZone(@JsonProperty("name") String name, @JsonProperty("groupName") String groupName,
                        @JsonProperty("coordinates") List<Coordinate> coordinates) {
        super(name, false, null, coordinates, null);
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }
}
