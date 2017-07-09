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
import nl.sonicity.sha2017.cms.cmshabackend.persistence.ActiveClaimRepository;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.ZoneMappingRepository;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.entities.ActiveClaim;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.entities.ZoneMapping;
import nl.sonicity.sha2017.cms.cmshabackend.titan.TitanService;
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
    ActiveClaimRepository activeClaimRepository;
    private ZonesController zonesController;

    @Before
    public void setUp() throws Exception {

        zonesController = new ZonesController(zoneMappingRepository, activeClaimRepository, titanService);
    }

    @Test
    public void testListZones() throws Exception {
        List<ZoneMapping> zones = getZoneMappings();

        when(zoneMappingRepository.findAll()).thenReturn(zones);

        List<Zone> result = zonesController.listZones(false);
        assertThat(result.size(), equalTo(2));
    }

    @Test
    public void testListZonesEmpty() throws Exception {
        List<ZoneMapping> zones = new ArrayList<>();

        when(zoneMappingRepository.findAll()).thenReturn(zones);

        List<Zone> result = zonesController.listZones(false);
        assertThat(result.size(), equalTo(0));
    }

    @Test
    public void testListZonesFilterAvailable() throws Exception {
        List<ZoneMapping> zones = getZoneMappings();

        when(zoneMappingRepository.findAll()).thenReturn(zones);

        List<Zone> result = zonesController.listZones(true);
        assertThat(result.size(), equalTo(1));
        assertThat(result.get(0).getName(), equalTo("Zone2"));
    }

    @Test
    public void testNewZone() throws Exception {
        ExtendedZone extendedZone = new ExtendedZone("Zone1", true, "Group 1");

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
        ExtendedZone extendedZone = new ExtendedZone("Zone1", true, "Group 1");

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
        when(titanService.createRgbCue(any(), anyFloat(), anyFloat(), anyFloat())).thenReturn(1111);

        Claim claim = new Claim(1, 0.5f, 0.1f);
        Zone claimedZone = zonesController.claimZone("Zone1", claim);

        assertThat(claimedZone.getName(), equalTo("Zone1"));
        assertThat(claimedZone.getAvailable(), equalTo(false));

        verify(titanService).createRgbCue(eq("Group 1"), eq(1.0f), eq(0.5f), eq(0.1f));
        verify(titanService).activateCue(eq(1111));

        ArgumentCaptor<ActiveClaim> captor = ArgumentCaptor.forClass(ActiveClaim.class);
        verify(activeClaimRepository).save(captor.capture());
        assertThat(captor.getValue().getZoneMapping(), is(nullValue())); // Will be filled on retrieval
        assertThat(captor.getValue().getPlaybackTitanId(), equalTo(1111));
    }

    @Test
    public void testNewClaimOnClaimedGroup() throws Exception {
        ZoneMapping zone1 = new ZoneMapping("Zone1", "Group 1", null);
        zone1.setActiveClaim(new ActiveClaim(LocalDateTime.now(), Duration.ofSeconds(60), null));

        when(zoneMappingRepository.findOneByZoneName("Zone1")).thenReturn(Optional.of(zone1));
        when(titanService.createRgbCue(any(), anyFloat(), anyFloat(), anyFloat())).thenReturn(1111);

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
        unavailable.setActiveClaim(new ActiveClaim(LocalDateTime.now(), Duration.ofSeconds(60), 1111));
        zones.add(unavailable);

        ZoneMapping available = new ZoneMapping("Zone2", "Group2", null);
        zones.add(available);
        return zones;
    }
}