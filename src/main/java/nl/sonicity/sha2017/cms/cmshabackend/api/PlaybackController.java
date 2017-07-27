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
package nl.sonicity.sha2017.cms.cmshabackend.api;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nl.sonicity.sha2017.cms.cmshabackend.api.models.ErrorDetail;
import nl.sonicity.sha2017.cms.cmshabackend.api.models.Playback;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.CueLocationRepository;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.entities.CueLocation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static nl.sonicity.sha2017.cms.cmshabackend.api.validation.ValidationHelpers.between;
import static nl.sonicity.sha2017.cms.cmshabackend.api.validation.ValidationHelpers.notEmpty;
import static nl.sonicity.sha2017.cms.cmshabackend.api.validation.ValidationHelpers.notNull;

@RestController
@RequestMapping("/playback")
@CrossOrigin(origins = "*")
public class PlaybackController {
    private CueLocationRepository cueLocationRepository;

    public PlaybackController(CueLocationRepository cueLocationRepository) {
        this.cueLocationRepository = cueLocationRepository;
    }

    @RequestMapping(path="/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiResponses({
            @ApiResponse(code = 403, message = "Forbidden", response = ErrorDetail.class),
    })
    List<Playback> listPlaybacks() {
        return StreamSupport.stream(cueLocationRepository.findAll().spliterator(), false)
                .map(this::cueLocationToPlaybackConverter)
                .collect(Collectors.toList());
    }

    @RequestMapping(path="/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiResponses({
            @ApiResponse(code = 403, message = "Forbidden", response = ErrorDetail.class),
    })
    @ResponseStatus(HttpStatus.CREATED)
    void newPlayback(@RequestBody Playback playback) {
        notEmpty().test(playback.getGroup()).orThrow();
        notNull().and(between(0,100)).test(playback.getPage()).orThrow();
        notNull().and(between(0,100)).test(playback.getIndex()).orThrow();

        CueLocation cueLocation = playbackToCueLocationConverter(playback);
        cueLocation.setNew(true);
        cueLocationRepository.save(cueLocation);
    }

    private Playback cueLocationToPlaybackConverter(CueLocation cueLocation) {
        return new Playback(cueLocation.getGroup(), cueLocation.getPage(), cueLocation.getPageIndex(), cueLocation.getActiveClaim() != null ? cueLocation.getActiveClaim().getPlaybackTitanId() : null, null);
    }

    private CueLocation playbackToCueLocationConverter(Playback playback) {
        return new CueLocation(playback.getGroup(), playback.getPage(), playback.getIndex(), false, null);
    }
}
