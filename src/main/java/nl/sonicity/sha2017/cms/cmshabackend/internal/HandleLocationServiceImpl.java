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

import nl.sonicity.sha2017.cms.cmshabackend.persistence.entities.CueLocation;
import nl.sonicity.sha2017.cms.cmshabackend.titan.models.HandleLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class HandleLocationServiceImpl implements HandleLocationService {
    private static final Logger LOG = LoggerFactory.getLogger(HandleLocationServiceImpl.class);

    private JdbcTemplate jdbcTemplate;

    public HandleLocationServiceImpl(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Reserves location by setting the reserved flag to true, attempts to prevent concurrency issues
     * by using pessimistic locking on the row.
     * Tried doing this with hibernate but hibernate caching is preventing this way of working or
     * so it seems.
     *
     * @param handleLocation
     * @return
     */
    @Override
    @Transactional
    public Optional<HandleLocation> reserveHandleLocation(HandleLocation handleLocation) {
        LOG.debug("SELECT ... FOR UPDATE on location {}", handleLocation);
        List<CueLocation> locations = jdbcTemplate.query("SELECT groupname, page, page_index, reserved FROM cue_location WHERE groupname=? AND page=? and page_index=? FOR UPDATE",
                new Object[]{ handleLocation.getGroup(), handleLocation.getPage(), handleLocation.getIndex()},
                        (rs, rowNum) -> new CueLocation(
                                rs.getString("groupname"),
                                rs.getInt("page"),
                                rs.getInt("page_index"),
                                rs.getBoolean("reserved"),
                                null)
        );

        if (locations.size() != 1) {
            LOG.warn("SELECT ... FOR UPDATE should have returned one row");
            return Optional.empty();
        }

        CueLocation location = locations.get(0);
        LOG.debug("Successfully row locked location {}", location);

        if (!location.getReserved()) {
            jdbcTemplate.update("UPDATE cue_location SET reserved = 1 WHERE groupname=? AND page=? and page_index=?",
                    handleLocation.getGroup(), handleLocation.getPage(), handleLocation.getIndex());
            LOG.debug("Successfully set reserved flag for location {}", location);
            return Optional.of(handleLocation);
        } else {
            LOG.debug("Locked location {} is already reserved", location);
            return Optional.empty();
        }
    }
}
