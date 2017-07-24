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
import nl.sonicity.sha2017.cms.cmshabackend.api.exceptions.ResourceNotFoundException;
import nl.sonicity.sha2017.cms.cmshabackend.api.exceptions.ValidationFailedException;
import nl.sonicity.sha2017.cms.cmshabackend.api.models.*;
import nl.sonicity.sha2017.cms.cmshabackend.api.validation.ValidationHelpers;
import nl.sonicity.sha2017.cms.cmshabackend.internal.ColourConverter;
import nl.sonicity.sha2017.cms.cmshabackend.internal.Core;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.ZoneMappingRepository;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.entities.ZoneCoordinates;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.entities.ZoneMapping;
import nl.sonicity.sha2017.cms.cmshabackend.titan.TitanService;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by hugo on 02/07/2017.
 */
@RestController
@RequestMapping("/zones")
@CrossOrigin(origins = "*")
public class ZonesController {
    private ZoneMappingRepository zoneMappingRepository;
    private TitanService titanService;
    private Core core;

    public ZonesController(ZoneMappingRepository zoneMappingRepository, TitanService titanService, Core core) {
        this.zoneMappingRepository = zoneMappingRepository;
        this.core = core;
        this.titanService = titanService;
    }

    @RequestMapping(path="/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasAuthority('ANONYMOUS') or hasRole('ROLE_ADMIN')")
    @ApiResponses({
            @ApiResponse(code = 403, message = "Access is Denied", response = ErrorDetail.class),
            @ApiResponse(code = 500, message = "Internal Error", response = ErrorDetail.class)
    })
    public List<Zone> listZones(@RequestParam(name="showAvailableOnly", required = false, defaultValue = "false") boolean showAvailableOnly) {
        Spliterator<ZoneMapping> spliterator = zoneMappingRepository.findAll().spliterator();
        List<Zone> regularZones = StreamSupport
                .stream(spliterator, false)
                .filter(m -> !showAvailableOnly || m.getActiveClaim() == null)
                .map(this::convertToZone)
                .collect(Collectors.toList());

        List<Zone> result = new ArrayList<>();
        result.addAll(regularZones);
        result.addAll(core.getSpecialZones());

        return result;
    }

    @RequestMapping(path="/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Required resource not found", response = ErrorDetail.class),
            @ApiResponse(code = 400, message = "Invalid input", response = ErrorDetail.class),
            @ApiResponse(code = 403, message = "Access is Denied", response = ErrorDetail.class),
            @ApiResponse(code = 500, message = "Internal Error", response = ErrorDetail.class)
    })
    public Zone newZone(@RequestBody ExtendedZone extendedZone) {
        ValidationHelpers.notEmpty().test(extendedZone.getGroupName()).orThrow();
        ValidationHelpers.notEmpty().test(extendedZone.getName()).orThrow();

        if (!titanService.groupExists(extendedZone.getGroupName())) {
            throw new ResourceNotFoundException(String.format("No group with name \"%s\" is configured on the console", extendedZone.getGroupName()));
        }

        if (zoneMappingRepository.findOneByZoneName(extendedZone.getName()).isPresent()) {
            throw new ValidationFailedException("Zone with name \"" + extendedZone.getName() + "\" already exists.");
        }

        List<ZoneCoordinates> coordinates = new ArrayList<>();
        if (extendedZone.getCoordinates() != null) {
            extendedZone.getCoordinates()
                    .stream()
                    .map(this::convertToZoneCoordinate)
                    .forEach(coordinates::add);
        }

        ZoneMapping zoneMapping = new ZoneMapping(extendedZone.getName(), extendedZone.getGroupName(), null);
        zoneMapping.setCoordinatesList(coordinates);

        ZoneMapping createdZone = zoneMappingRepository.save(zoneMapping);
        return convertToZone(createdZone);
    }

    @RequestMapping(path="/{zoneName}/claim", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasAuthority('ANONYMOUS') or hasRole('ROLE_ADMIN')")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Required resource not found", response = ErrorDetail.class),
            @ApiResponse(code = 409, message = "Zone is already claimed", response = ErrorDetail.class),
            @ApiResponse(code = 400, message = "Invalid input", response = ErrorDetail.class),
            @ApiResponse(code = 403, message = "Access is Denied", response = ErrorDetail.class),
            @ApiResponse(code = 500, message = "Internal Error", response = ErrorDetail.class)
    })
    public Zone claimZone(@PathVariable("zoneName") String zoneName, @RequestBody Claim claim) {
        ValidationHelpers.between(0, 1).test(claim.getBlue()).orThrow();
        ValidationHelpers.between(0, 1).test(claim.getGreen()).orThrow();
        ValidationHelpers.between(0, 1).test(claim.getRed()).orThrow();

        ZoneMapping zoneMapping = core.processClaim(zoneName, claim);
        return convertToZone(zoneMapping);
    }

    private Zone convertToZone(ZoneMapping m) {
        String colour = m.getActiveClaim() != null ? ColourConverter.colourAsRGBHex(m.getActiveClaim().getColour()) : null;
        List<Coordinate> coordinates = m.getCoordinatesList().stream().map(this::convertToCoordinate).collect(Collectors.toList());
        return new Zone(m.getZoneName(), m.getActiveClaim() == null, colour, coordinates, null);
    }

    private Coordinate convertToCoordinate(ZoneCoordinates z) {
        return new Coordinate(z.getLongitude(), z.getLatitude());
    }

    private ZoneCoordinates convertToZoneCoordinate(Coordinate z) {
        return new ZoneCoordinates(z.getLongitude(), z.getLatitude());
    }

}
