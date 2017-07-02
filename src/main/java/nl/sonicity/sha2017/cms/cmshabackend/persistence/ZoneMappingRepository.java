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
package nl.sonicity.sha2017.cms.cmshabackend.persistence;

import nl.sonicity.sha2017.cms.cmshabackend.persistence.entities.ZoneMapping;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by hugo on 02/07/2017.
 */
public interface ZoneMappingRepository extends CrudRepository<ZoneMapping, Long> {

    Optional<ZoneMapping> findOneByZoneName(String zoneName);
}
