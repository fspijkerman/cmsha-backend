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
import nl.sonicity.sha2017.cms.cmshabackend.api.models.*;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/flamer")
@CrossOrigin(origins = "*")
public class FlamerController {
    @RequestMapping(path="/claim", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasAuthority('ANONYMOUS') or hasRole('ROLE_ADMIN')")
    @ApiResponses({
            @ApiResponse(code = 403, message = "Invalid claim_ticket", response = ErrorDetail.class),
    })
    public FlamerClaimResponse claim(@RequestBody FlamerClaimRequest flamerClaimRequest) {
        return new FlamerClaimResponse(LocalDateTime.now().plus(Duration.ofSeconds(60)));
    }

    @RequestMapping(path="/fire", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasAuthority('ANONYMOUS') or hasRole('ROLE_ADMIN')")
    @ApiResponses({
            @ApiResponse(code = 403, message = "Invalid claim_ticket", response = ErrorDetail.class),
    })
    public FlamerFireResponse claim(@RequestBody FlamerFireRequest flamerFireRequest) {
        return new FlamerFireResponse(LocalDateTime.now().plus(Duration.ofSeconds(30)));
    }

}
