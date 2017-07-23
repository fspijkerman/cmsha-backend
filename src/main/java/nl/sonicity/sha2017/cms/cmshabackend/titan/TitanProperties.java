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
import nl.sonicity.sha2017.cms.cmshabackend.titan.models.HandleLocation;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties("titan")
public class TitanProperties {
    private Map<String, Object> webapi = new HashMap<String, Object>();
    private Map<String, Object> rest = new HashMap<String, Object>();
    private Map<String, Object> handles = new HashMap<String, Object>();

    public Map<String, Object> getWebapi() {
        return webapi;
    }

    public void setWebapi(Map<String, Object> webapi) {
        this.webapi = webapi;
    }

    public Map<String, Object> getRest() {
        return rest;
    }

    public void setRest(Map<String, Object> rest) {
        this.rest = rest;
    }

    public Map<String, Object> getHandles() {
        return handles;
    }

    public void setHandles(Map<String, Object> handles) {
        this.handles = handles;
    }

    /**
     * Depends on the following properties being present
     * titan.handles.{handleName}.group=XXX
     * titan.handles.{handleName}.page=XXX
     * titan.handles.{handleName}.index=XXX
     *
     * @param handleName
     * @return
     */
    public HandleLocation getHandleLocation(String handleName) {
        if (!handles.containsKey(handleName)) {
            throw new PropertyNotFoundException("No handle with name " + handleName);
        }

        Map<String, String> locationMap = (Map<String, String>) handles.get(handleName);
        if (!locationMap.containsKey("group") || !locationMap.containsKey("page") || !locationMap.containsKey("index")) {
            throw new PropertyNotFoundException("Invalid configuration for handle " + handleName);
        }

        return new HandleLocation(locationMap.get("group"), Integer.parseInt(locationMap.get("page")), Integer.parseInt(locationMap.get("index")));
    }
}
