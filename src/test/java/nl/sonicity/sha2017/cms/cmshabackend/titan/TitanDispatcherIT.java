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

import nl.sonicity.sha2017.cms.cmshabackend.titan.models.FixtureControlId;
import nl.sonicity.sha2017.cms.cmshabackend.titan.models.Handle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by hugo on 24/06/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:integrationtest.properties")
public class TitanDispatcherIT {
    @Autowired
    private TitanDispatcher titanDispatcher;

    @Test
    public void selectAndLocateFixture() {
        String showname = titanDispatcher.getShowName();
        assertThat("This test depends a matching show loaded in the titan", showname, equalTo("Test Show Hugo"));

        Optional<Handle> fixtureHandle = titanDispatcher.listFixtures()
                .stream()
                .filter(fixture -> fixture.getHandleLocation().getIndex() == 1 &&
                    fixture.getHandleLocation().getPage() == 0)
                .findFirst();

        assertThat(fixtureHandle.isPresent(), equalTo(true));

        titanDispatcher.playbacksSelectionClear();
        titanDispatcher.programmerEditorClearAll();
        titanDispatcher.selectionContextSelectFixture(fixtureHandle.get().getTitanId());
        titanDispatcher.programmerEditorFixturesLocateSelectedFixtures(false);

        // Visually inspect, first lamp should be "located"
    }

    @Test
    public void selectAndLocateGroup() {
        String showname = titanDispatcher.getShowName();
        assertThat("This test depends a matching show loaded in the titan", showname, equalTo("Test Show Hugo"));

        titanDispatcher.listGroups()
                .stream()
                .forEach(handle->{
                    System.out.println(handle.getLegend() + ": " + handle.getTitanId());
                });

        Optional<Handle> handle = titanDispatcher.listGroups()
                .stream()
                .filter(group -> group.getLegend().equals("Dim 1"))
                .findFirst();

        assertThat(handle.isPresent(), equalTo(true));

        titanDispatcher.programmerEditorClearAll();
        titanDispatcher.playbacksSelectionClear();
        titanDispatcher.groupRecallGroupById(handle.get().getTitanId());
        titanDispatcher.programmerEditorFixturesLocateSelectedFixtures(false);

        // Visually inspect, first lamp should be "located"
    }

    @Test
    public void clearProgrammer() {
        String showname = titanDispatcher.getShowName();
        assertThat("This test depends a matching show loaded in the titan", showname, equalTo("Test Show Hugo"));

        titanDispatcher.playbacksSelectionClear();
        titanDispatcher.programmerEditorClearAll();
    }

    @Test
    public void createHighPrioColorCue() throws Exception {
        String showname = titanDispatcher.getShowName();
        assertThat("This test depends a matching show loaded in the titan", showname, equalTo("Test Show Hugo"));

        titanDispatcher.playbacksSelectionClear();
        titanDispatcher.programmerEditorClearAll();

        // Select group
        Handle handle = titanDispatcher.listGroups()
                .stream()
                .filter(group -> group.getLegend().equals("Dim 1"))
                .findFirst().orElseThrow(() -> new Exception("No handle Dim 1 Found"));

        titanDispatcher.groupRecallGroupById(handle.getTitanId());

        titanDispatcher.programmerSetBlindMode(false, 0);

        //assertThat(titanDispatcher.programmerIsBlindActive(), equalTo(true));

        // Set Dimmer 100%
        titanDispatcher.programmerEditorFixturesSetControlValue(FixtureControlId.Dimmer, 1, 1, true, false);

        // Set Red 0%
        titanDispatcher.programmerEditorFixturesSetControlValue(FixtureControlId.Red, 1, 0, true, false);

        // Set Green 8%
        titanDispatcher.programmerEditorFixturesSetControlValue(FixtureControlId.Green, 1, 0.08f, true, false);

        // Set Blue 100%
        titanDispatcher.programmerEditorFixturesSetControlValue(FixtureControlId.Blue, 1, 1, true, false);

        // Set Macro "Safe"
        // Set Mode "Dimmer"
        // Record
        //   -> Cue
        titanDispatcher.playbacksStoreCue("PlaybackWindow", 5, true);
        // Clear All\
        // Playback Options
        // -> Select Cue
        // -> Playback
        //    --> Priority High
        //    Exit
        //  Exit
        // Clear All
        titanDispatcher.playbacksSelectionClear();
        titanDispatcher.programmerEditorClearAll();

        titanDispatcher.programmerSetBlindMode(false, 0);
    }

}
