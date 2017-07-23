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

import nl.sonicity.sha2017.cms.cmshabackend.api.exceptions.ValidationFailedException;
import nl.sonicity.sha2017.cms.cmshabackend.api.exceptions.ZoneAlreadyClaimedException;
import nl.sonicity.sha2017.cms.cmshabackend.api.models.Claim;
import nl.sonicity.sha2017.cms.cmshabackend.api.models.ExtendedZone;
import nl.sonicity.sha2017.cms.cmshabackend.api.models.Zone;
import nl.sonicity.sha2017.cms.cmshabackend.internal.Core;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.ActiveClaimRepository;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.ZoneMappingRepository;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.entities.ActiveClaim;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.entities.Colour;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.entities.ZoneCoordinates;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.entities.ZoneMapping;
import nl.sonicity.sha2017.cms.cmshabackend.titan.TitanService;
import nl.sonicity.sha2017.cms.cmshabackend.titan.models.CreateRgbCueResult;
import nl.sonicity.sha2017.cms.cmshabackend.titan.models.HandleLocation;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.apis.CatchExceptionHamcrestMatchers.hasMessage;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by hugo on 09/07/2017.
 */
public class ZonesControllerTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    TitanService titanService;
    @Mock
    ZoneMappingRepository zoneMappingRepository;
    @Mock
    Core core;
    @Mock
    ActiveClaimRepository activeClaimRepository;
    private ZonesController zonesController;
    private static final Colour RED = new Colour(1, 0, 0);

    @Before
    public void setUp() throws Exception {
        zonesController = new ZonesController(zoneMappingRepository, titanService, core);
    }

    @Test
    public void testListZones() throws Exception {
        List<ZoneMapping> zones = getZoneMappings();

        when(zoneMappingRepository.findAll()).thenReturn(zones);

        List<Zone> result = zonesController.listZones(false);
        assertThat(result.size(), equalTo(3));

        Zone unavailableZone = result.stream()
                .filter(z -> "Zone1".equals(z.getName()))
                .findAny()
                .orElseThrow(() -> new Exception("There should be a Zone1"));

        assertThat(unavailableZone.getAvailable(), equalTo(false));
        assertThat(unavailableZone.getColour(), equalTo("ff0000"));
        assertThat(unavailableZone.getCoordinates().size(), equalTo(1));

        Zone availableZone = result.stream()
                .filter(z -> "Zone2".equals(z.getName()))
                .findAny()
                .orElseThrow(() -> new Exception("There should be a Zone2"));

        assertThat(availableZone.getAvailable(), equalTo(true));
        assertThat(availableZone.getColour(), is(nullValue()));
        assertThat(availableZone.getCoordinates().size(), equalTo(1));

        Zone flameThrowerZone = result.stream()
                .filter(z -> "FlameThrowers".equals(z.getName()))
                .findAny()
                .orElseThrow(() -> new Exception("There should be a FlameThrowers"));

        assertThat(flameThrowerZone.getAvailable(), equalTo(false));
        assertThat(flameThrowerZone.getColour(), is(nullValue()));
        assertThat(flameThrowerZone.getCoordinates().size(), equalTo(0));
    }

    @Test
    public void testListZonesEmpty() throws Exception {
        List<ZoneMapping> zones = new ArrayList<>();

        when(zoneMappingRepository.findAll()).thenReturn(zones);

        List<Zone> result = zonesController.listZones(false);
        assertThat(result.size(), equalTo(1));
    }

    @Test
    public void testListZonesFilterAvailable() throws Exception {
        List<ZoneMapping> zones = getZoneMappings();

        when(zoneMappingRepository.findAll()).thenReturn(zones);

        List<Zone> result = zonesController.listZones(true);
        assertThat(result.size(), equalTo(2));

        Zone availableZone = result.stream()
                .filter(z -> "Zone2".equals(z.getName()))
                .findAny()
                .orElseThrow(() -> new Exception("There should be a Zone2"));

        assertThat(availableZone.getAvailable(), equalTo(true));
        assertThat(availableZone.getColour(), is(nullValue()));
    }

    @Test
    public void testNewZone() throws Exception {
        ExtendedZone extendedZone = new ExtendedZone("Zone1", true, "Group 1", null, null);

        when(titanService.groupExists(any())).thenReturn(true);
        when(zoneMappingRepository.findOneByZoneName(any())).thenReturn(Optional.empty());

        Zone createdZone = zonesController.newZone(extendedZone);

        assertThat(createdZone.getName(), equalTo("Zone1"));
        assertThat(createdZone.getAvailable(), equalTo(true));

        verify(titanService, times(1)).groupExists(eq("Group 1"));

        ArgumentCaptor<ZoneMapping> captor = ArgumentCaptor.forClass(ZoneMapping.class);
        verify(zoneMappingRepository, times(1)).save(captor.capture());

        assertThat(captor.getValue().getZoneName(), equalTo("Zone1"));
        assertThat(captor.getValue().getActiveClaim(), equalTo(null));
        assertThat(captor.getValue().getTitanGroupName(), equalTo("Group 1"));
        assertThat(captor.getValue().getTitanGroupId(), is(nullValue()));
    }

    @Test
    public void testDuplicateZone() throws Exception {
        ExtendedZone extendedZone = new ExtendedZone("Zone1", true, "Group 1", null, null);

        when(titanService.groupExists(any())).thenReturn(true);
        when(zoneMappingRepository.findOneByZoneName(any())).thenReturn(Optional.of(new ZoneMapping("Zone1", "Group 1", 1111)));

        catchException(zonesController).newZone(extendedZone);

        assertThat(caughtException(),
                allOf(
                        instanceOf(ValidationFailedException.class),
                        hasMessage("Zone with name \"Zone1\" already exists.")
                )
        );

    }

    @Test
    public void testNewClaim() throws Exception {
        ZoneMapping zone1 = new ZoneMapping("Zone1", "Group 1", null);
        zone1.setActiveClaim(null);
        when(zoneMappingRepository.findOneByZoneName("Zone1")).thenReturn(Optional.of(zone1));
        CreateRgbCueResult cueResult = new CreateRgbCueResult(new HandleLocation("Testpage", 0, 0), 1111);
        when(titanService.createRgbCue(any(), any(), anyFloat(), anyFloat(), anyFloat())).thenReturn(cueResult);

        ZoneMapping claimed = new ZoneMapping("Zone1", "Group 1", null);
        claimed.setActiveClaim(new ActiveClaim(LocalDateTime.now(), Duration.ofSeconds(5), 1111, new Colour(1,1,1)));
        claimed.setCoordinatesList(Collections.emptyList());
        when(core.processClaim(any(), any())).thenReturn(claimed);

        Claim claim = new Claim(1, 0.5f, 0.1f);
        Zone claimedZone = zonesController.claimZone("Zone1", claim);

        assertThat(claimedZone.getName(), equalTo("Zone1"));
        assertThat(claimedZone.getAvailable(), equalTo(false));
    }

    @Test
    public void testNewClaimOnClaimedGroup() throws Exception {
        ZoneMapping zone1 = new ZoneMapping("Zone1", "Group 1", null);
        zone1.setActiveClaim(new ActiveClaim(LocalDateTime.now(), Duration.ofSeconds(60), null, RED));

        when(zoneMappingRepository.findOneByZoneName("Zone1")).thenReturn(Optional.of(zone1));
        CreateRgbCueResult cueResult = new CreateRgbCueResult(new HandleLocation("Testpage", 0, 0), 1111);
        when(titanService.createRgbCue(any(), any(), anyFloat(), anyFloat(), anyFloat())).thenReturn(cueResult);
        when(core.processClaim(any(), any())).thenThrow(new ZoneAlreadyClaimedException("This zone is already claimed"));

        Claim claim = new Claim(1, 0.5f, 0.1f);
        catchException(zonesController).claimZone("Zone1", claim);

        assertThat(caughtException(),
                allOf(
                        instanceOf(ZoneAlreadyClaimedException.class),
                        hasMessage("This zone is already claimed")
                )
        );
    }

    private List<ZoneMapping> getZoneMappings() {
        List<ZoneMapping> zones = new ArrayList<>();

        ZoneMapping unavailable = new ZoneMapping("Zone1", "Group1", null);
        unavailable.setActiveClaim(new ActiveClaim(LocalDateTime.now(), Duration.ofSeconds(60), 1111, RED));
        unavailable.setCoordinatesList(Collections.singletonList(new ZoneCoordinates(52.033199d, 5.155046)));
        zones.add(unavailable);

        ZoneMapping available = new ZoneMapping("Zone2", "Group2", null);
        available.setCoordinatesList(Collections.singletonList(new ZoneCoordinates(52.033172d, 5.154831)));
        zones.add(available);
        return zones;
    }
}