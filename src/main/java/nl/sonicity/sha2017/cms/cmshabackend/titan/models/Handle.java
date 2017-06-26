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
package nl.sonicity.sha2017.cms.cmshabackend.titan.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by hugo on 23/06/2017.
 */
public class Handle {
    private boolean active;
    private String legend;
    private HandleLocation handleLocation;
    private List<KeyValue> properties;
    private int titanId;
    private String type;

    @JsonCreator
    public Handle(@JsonProperty("Active") boolean active, @JsonProperty("Legend") String legend,
                  @JsonProperty("handleLocation") HandleLocation handleLocation,
                  @JsonProperty("properties") List<KeyValue> properties,
                  @JsonProperty("titanId") int titanId,
                  @JsonProperty("type") String type) {
        this.active = active;
        this.legend = legend;
        this.handleLocation = handleLocation;
        this.properties = properties;
        this.titanId = titanId;
        this.type = type;
    }

    public boolean isActive() {
        return active;
    }

    public String getLegend() {
        return legend;
    }

    public HandleLocation getHandleLocation() {
        return handleLocation;
    }

    public List<KeyValue> getProperties() {
        return properties;
    }

    public int getTitanId() {
        return titanId;
    }

    public String getType() {
        return type;
    }
}

/*
   {
        "Active": false,
        "Legend": "",
        "handleLocation": {
            "group": "Fixtures",
            "index": 80,
            "page": 3
        },
        "properties": [
            {
                "Key": "lockState",
                "Value": "Unlocked"
            }
        ],
        "titanId": 3514,
        "type": "fixtureHandle"
    }
 */