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

import nl.sonicity.sha2017.cms.cmshabackend.titan.exceptions.RequestFailedException;
import nl.sonicity.sha2017.cms.cmshabackend.titan.models.FixtureControlId;
import nl.sonicity.sha2017.cms.cmshabackend.titan.models.Handle;
import nl.sonicity.sha2017.cms.cmshabackend.titan.models.HandleLocation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.HttpStatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.apis.CatchExceptionHamcrestMatchers.hasMessage;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
@ActiveProfiles({"test"})
public class TitanDispatcherTest {

    @Autowired
    private TitanDispatcher titanDispatcher;
    private ClientAndServer clientAndServer;

    @Before
    public void setUp() throws Exception {
        clientAndServer = ClientAndServer.startClientAndServer(4430);
    }

    @After
    public void tearDown() throws Exception {
        clientAndServer.stop(true);
    }

    @Test
    public void getVersion() throws Exception {
        clientAndServer
                .when(HttpRequest.request("/titan/get/System/SoftwareVersion"))
                .respond(HttpResponse.response()
                        .updateHeader("Content-Length","6")
                        .updateHeader("Content-Type","application/json")
                        .withBody("\"10.1\""));

        String actual = titanDispatcher.getVersion();

        assertEquals("10.1", actual);
    }

    @Test
    public void getShowName() throws Exception {
        clientAndServer
                .when(HttpRequest.request("/titan/get/Show/ShowName"))
                .respond(HttpResponse.response()
                        .updateHeader("Content-Length","31")
                        .updateHeader("Content-Type","application/json")
                        .withBody("\"Dna room basic white 05.01.2016\""));

        String actual = titanDispatcher.getShowName();

        assertEquals("Dna room basic white 05.01.2016", actual);

    }

    @Test
    public void listFixtures() throws Exception {
        clientAndServer
                .when(HttpRequest.request("/titan/handles/Fixtures"))
                .respond(HttpResponse.response()
                        .updateHeader("Content-Length","31")
                        .updateHeader("Content-Type","application/json")
                        .withBody("[{\"handleLocation\":{\"group\":\"Fixtures\",\"index\":0,\"page\":0},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":3075,\"type\":\"fixtureHandle\",\"Active\":false,\"Legend\":\"\"},{\"handleLocation\":{\"group\":\"Fixtures\",\"index\":1,\"page\":0},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":3077,\"type\":\"fixtureHandle\",\"Active\":false,\"Legend\":\"\"},{\"handleLocation\":{\"group\":\"Fixtures\",\"index\":20,\"page\":0},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":8405,\"type\":\"fixtureHandle\",\"Active\":false,\"Legend\":\"\"},{\"handleLocation\":{\"group\":\"Fixtures\",\"index\":0,\"page\":1},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":1700,\"type\":\"fixtureHandle\",\"Active\":false,\"Legend\":\"\"},{\"handleLocation\":{\"group\":\"Fixtures\",\"index\":1,\"page\":1},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":1705,\"type\":\"fixtureHandle\",\"Active\":false,\"Legend\":\"\"},{\"handleLocation\":{\"group\":\"Fixtures\",\"index\":2,\"page\":1},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":1706,\"type\":\"fixtureHandle\",\"Active\":false,\"Legend\":\"\"},{\"handleLocation\":{\"group\":\"Fixtures\",\"index\":3,\"page\":1},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":1708,\"type\":\"fixtureHandle\",\"Active\":false,\"Legend\":\"\"},{\"handleLocation\":{\"group\":\"Fixtures\",\"index\":4,\"page\":1},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":1709,\"type\":\"fixtureHandle\",\"Active\":false,\"Legend\":\"\"},{\"handleLocation\":{\"group\":\"Fixtures\",\"index\":5,\"page\":1},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":1710,\"type\":\"fixtureHandle\",\"Active\":false,\"Legend\":\"\"},{\"handleLocation\":{\"group\":\"Fixtures\",\"index\":6,\"page\":1},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":1711,\"type\":\"fixtureHandle\",\"Active\":false,\"Legend\":\"\"},{\"handleLocation\":{\"group\":\"Fixtures\",\"index\":7,\"page\":1},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":1712,\"type\":\"fixtureHandle\",\"Active\":false,\"Legend\":\"\"},{\"handleLocation\":{\"group\":\"Fixtures\",\"index\":20,\"page\":1},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":1713,\"type\":\"fixtureHandle\",\"Active\":false,\"Legend\":\"\"},{\"handleLocation\":{\"group\":\"Fixtures\",\"index\":21,\"page\":1},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":1714,\"type\":\"fixtureHandle\",\"Active\":false,\"Legend\":\"\"},{\"handleLocation\":{\"group\":\"Fixtures\",\"index\":22,\"page\":1},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":1715,\"type\":\"fixtureHandle\",\"Active\":false,\"Legend\":\"\"},{\"handleLocation\":{\"group\":\"Fixtures\",\"index\":23,\"page\":1},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":1716,\"type\":\"fixtureHandle\",\"Active\":false,\"Legend\":\"\"},{\"handleLocation\":{\"group\":\"Fixtures\",\"index\":24,\"page\":1},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":1717,\"type\":\"fixtureHandle\",\"Active\":false,\"Legend\":\"\"},{\"handleLocation\":{\"group\":\"Fixtures\",\"index\":25,\"page\":1},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":1718,\"type\":\"fixtureHandle\",\"Active\":false,\"Legend\":\"\"},{\"handleLocation\":{\"group\":\"Fixtures\",\"index\":26,\"page\":1},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":1719,\"type\":\"fixtureHandle\",\"Active\":false,\"Legend\":\"\"},{\"handleLocation\":{\"group\":\"Fixtures\",\"index\":27,\"page\":1},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":1720,\"type\":\"fixtureHandle\",\"Active\":false,\"Legend\":\"\"}]"));

        List<Handle> actual = titanDispatcher.listFixtures();

        assertEquals(19, actual.size());
        Handle fixture = actual.get(0);
        assertThat(fixture.getTitanId(), equalTo(3075));
        assertThat(fixture.getType(), equalTo("fixtureHandle"));
    }

    @Test
    public void listGroups() throws Exception {
        clientAndServer
                .when(HttpRequest.request("/titan/handles/Groups"))
                .respond(HttpResponse.response()
                        .updateHeader("Content-Length","31")
                        .updateHeader("Content-Type","application/json")
                        .withBody("[{\"handleLocation\":{\"group\":\"Groups\",\"index\":0,\"page\":0},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":1707,\"type\":\"groupHandle\",\"Active\":false,\"Legend\":\"All Rush Par 1 RGBW\"},{\"handleLocation\":{\"group\":\"Groups\",\"index\":1,\"page\":0},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":1721,\"type\":\"groupHandle\",\"Active\":false,\"Legend\":\"[16x Rush Par 1 RGBW]\"},{\"handleLocation\":{\"group\":\"Groups\",\"index\":10,\"page\":0},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":3041,\"type\":\"groupHandle\",\"Active\":false,\"Legend\":\"All Reflection LEDKO VariWhite\"},{\"handleLocation\":{\"group\":\"Groups\",\"index\":11,\"page\":0},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":3078,\"type\":\"groupHandle\",\"Active\":false,\"Legend\":\"[2x Reflection LEDKO VariWhite]\"},{\"handleLocation\":{\"group\":\"Groups\",\"index\":20,\"page\":0},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":3203,\"type\":\"groupHandle\",\"Active\":false,\"Legend\":\"All LEDWash CW-WW\"},{\"handleLocation\":{\"group\":\"Groups\",\"index\":21,\"page\":0},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":3275,\"type\":\"groupHandle\",\"Active\":false,\"Legend\":\"[30x LEDWash CW-WW]\"},{\"handleLocation\":{\"group\":\"Groups\",\"index\":22,\"page\":0},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":1729,\"type\":\"groupHandle\",\"Active\":false,\"Legend\":\"All LEDWash RGBW\"},{\"handleLocation\":{\"group\":\"Groups\",\"index\":24,\"page\":0},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":3515,\"type\":\"groupHandle\",\"Active\":false,\"Legend\":\"[32x LEDWash RGBW]\"},{\"handleLocation\":{\"group\":\"Groups\",\"index\":25,\"page\":0},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":8408,\"type\":\"groupHandle\",\"Active\":false,\"Legend\":\"All P5\"},{\"handleLocation\":{\"group\":\"Groups\",\"index\":30,\"page\":0},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":10317,\"type\":\"groupHandle\",\"Active\":false,\"Legend\":\"RushP1RGBW window\"},{\"handleLocation\":{\"group\":\"Groups\",\"index\":31,\"page\":0},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":10334,\"type\":\"groupHandle\",\"Active\":false,\"Legend\":\"RushP1RGBW office\"},{\"handleLocation\":{\"group\":\"Groups\",\"index\":40,\"page\":0},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":10011,\"type\":\"groupHandle\",\"Active\":false,\"Legend\":\"Wash RGBW window\"},{\"handleLocation\":{\"group\":\"Groups\",\"index\":41,\"page\":0},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":10056,\"type\":\"groupHandle\",\"Active\":false,\"Legend\":\"Wash RGBW office\"},{\"handleLocation\":{\"group\":\"Groups\",\"index\":50,\"page\":0},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":10531,\"type\":\"groupHandle\",\"Active\":false,\"Legend\":\"Wash CW-WW window\"},{\"handleLocation\":{\"group\":\"Groups\",\"index\":51,\"page\":0},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":10655,\"type\":\"groupHandle\",\"Active\":false,\"Legend\":\"Wash CW-WW office\"},{\"handleLocation\":{\"group\":\"Groups\",\"index\":52,\"page\":0},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":11298,\"type\":\"groupHandle\",\"Active\":false,\"Legend\":\"entree\"}]"));

        List<Handle> actual = titanDispatcher.listGroups();

        assertEquals(16, actual.size());
        Handle group = actual.get(0);
        assertThat(group.getTitanId(), equalTo(1707));
        assertThat(group.getType(), equalTo("groupHandle"));
        assertThat(group.getLegend(), equalTo("All Rush Par 1 RGBW"));
    }

    @Test
    public void listColours() throws Exception {
        clientAndServer
                .when(HttpRequest.request("/titan/handles/Colours"))
                .respond(HttpResponse.response()
                        .updateHeader("Content-Length","31")
                        .updateHeader("Content-Type","application/json")
                        .withBody("[{\"handleLocation\":{\"group\":\"Colours\",\"index\":0,\"page\":0},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":3810,\"type\":\"paletteHandle\",\"Active\":false,\"Legend\":\"\"},{\"handleLocation\":{\"group\":\"Colours\",\"index\":1,\"page\":0},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":4549,\"type\":\"paletteHandle\",\"Active\":false,\"Legend\":\"\"},{\"handleLocation\":{\"group\":\"Colours\",\"index\":4,\"page\":0},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":6240,\"type\":\"paletteHandle\",\"Active\":false,\"Legend\":\"\"},{\"handleLocation\":{\"group\":\"Colours\",\"index\":5,\"page\":0},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],\"titanId\":5473,\"type\":\"paletteHandle\",\"Active\":false,\"Legend\":\"\"}]"));

        List<Handle> actual = titanDispatcher.listColours();

        assertEquals(4, actual.size());
        Handle group = actual.get(0);
        assertThat(group.getTitanId(), equalTo(3810));
        assertThat(group.getType(), equalTo("paletteHandle"));
    }

    @Test
    public void programmerEditorClearAll() throws Exception {
        clientAndServer
                .when(HttpRequest.request("/titan/script/2/Programmer/Editor/ClearAll"))
                .respond(HttpResponse.response()
                        .updateHeader("Content-Length","0")
                        .withBody(""));

        titanDispatcher.programmerEditorClearAll();

        clientAndServer.verify(HttpRequest.request("/titan/script/2/Programmer/Editor/ClearAll"));
    }

    @Test
    public void playbacksSelectionClear() throws Exception {
        clientAndServer
                .when(HttpRequest.request("/titan/script/2/Playbacks/Selection/Clear"))
                .respond(HttpResponse.response()
                        .updateHeader("Content-Length","0")
                        .withBody(""));

        titanDispatcher.playbacksSelectionClear();

        clientAndServer.verify(HttpRequest.request("/titan/script/2/Playbacks/Selection/Clear"));
    }

    @Test
    public void programmerEditorFixturesSetControlValue() throws Exception {
        clientAndServer
                .when(HttpRequest.request("/titan/script/2/Programmer/Editor/Fixtures/SetControlValueById"))
                .respond(HttpResponse.response()
                        .updateHeader("Content-Length","0")
                        .withBody(""));

        titanDispatcher.programmerEditorFixturesSetControlValue(FixtureControlId.DIMMER, 1, 0.5f, true, true);

        clientAndServer.verify(HttpRequest
                .request("/titan/script/2/Programmer/Editor/Fixtures/SetControlValueById")
                .withQueryStringParameter("controlId", "16")
                .withQueryStringParameter("functionId", "1")
                .withQueryStringParameter("value", "0.5")
                .withQueryStringParameter("programmer", "true")
                .withQueryStringParameter("createRestorePoint", "true"));
    }

    @Test
    public void selectionContextSelectFixture() throws Exception {
        clientAndServer
                .when(HttpRequest.request("/titan/script/2/Selection/Context/Global/SelectFixture"))
                .respond(HttpResponse.response()
                        .updateHeader("Content-Length","0")
                        .withBody(""));

        titanDispatcher.selectionContextSelectFixture(1836);

        clientAndServer.verify(HttpRequest
                .request("/titan/script/2/Selection/Context/Global/SelectFixture")
                .withQueryStringParameter("handle_titanId", "1836"));
    }

    @Test
    public void groupRecallGroup() throws Exception {
        clientAndServer
                .when(HttpRequest.request("/titan/script/2/Group/RecallGroup"))
                .respond(HttpResponse.response()
                        .updateHeader("Content-Length","0")
                        .withBody(""));

        titanDispatcher.groupRecallGroup(1876);

        clientAndServer.verify(HttpRequest
                .request("/titan/script/2/Group/RecallGroup")
                .withQueryStringParameter("handle_titanId", "1876"));
    }

    @Test
    public void groupRecallGroupById() throws Exception {
        clientAndServer
                .when(HttpRequest.request("/titan/script/2/Group/RecallGroupById"))
                .respond(HttpResponse.response()
                        .updateHeader("Content-Length","0")
                        .withBody(""));

        titanDispatcher.groupRecallGroupById(1876);

        clientAndServer.verify(HttpRequest
                .request("/titan/script/2/Group/RecallGroupById")
                .withQueryStringParameter("groupId", "1876"));
    }

    @Test
    public void failedGroupRecallGroup() throws Exception {
        clientAndServer
                .when(HttpRequest.request("/titan/script/2/Group/RecallGroup"))
                .respond(HttpResponse.response()
                        .withStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR_500.code())
                        .withBody("Some error message from the Titan"));

        catchException(titanDispatcher).groupRecallGroup(1876);

        assertThat(caughtException(),
                allOf(
                        instanceOf(RequestFailedException.class),
                        hasMessage("Some error message from the Titan")
                )
        );

        clientAndServer.verify(HttpRequest
                .request("/titan/script/2/Group/RecallGroup")
                .withQueryStringParameter("handle_titanId", "1876"));
    }

    @Test
    public void programmerEditorFixturesLocateSelectedFixtures() throws Exception {
        clientAndServer
                .when(HttpRequest.request("/titan/script/2/Programmer/Editor/Fixtures/LocateSelectedFixtures"))
                .respond(HttpResponse.response()
                        .updateHeader("Content-Length","0")
                        .withBody(""));

        titanDispatcher.programmerEditorFixturesLocateSelectedFixtures(true);

        clientAndServer.verify(HttpRequest
                .request("/titan/script/2/Programmer/Editor/Fixtures/LocateSelectedFixtures")
                .withQueryStringParameter("allAttributes", "true"));
    }

    @Test
    public void getHandleByLocation() throws Exception {
        clientAndServer
                .when(HttpRequest.request("/titan/script/2/Handles/GetHandle"))
                .respond(HttpResponse.response()
                        .updateHeader("Content-Length","178")
                        .updateHeader("Content-Type","application/json")
                        .withBody("{\"handleLocation\":{\"group\":\"Fixtures\",\"index\":80,\"page\":3},\"properties\":[{\"Key\":\"lockState\",\"Value\":\"Unlocked\"}],"
                                + "\"titanId\":3514,\"type\":\"fixtureHandle\",\"Active\":false,\"Legend\":\"\"}"));

        Optional<Handle> handle = titanDispatcher.getHandleByLocation(new HandleLocation("fixtures", 80, 3));

        assertThat(handle.isPresent(), equalTo(true));
        assertThat(3514, equalTo(handle.get().getTitanId()));

        clientAndServer.verify(HttpRequest
                .request("/titan/script/2/Handles/GetHandle")
                .withQueryStringParameter("group", "fixtures")
                .withQueryStringParameter("index", "80")
                .withQueryStringParameter("page", "3"));
    }

    @Test
    public void getHandleByLocationEmptyLocation() throws Exception {
        clientAndServer
                .when(HttpRequest.request("/titan/script/2/Handles/GetHandle"))
                .respond(HttpResponse.response()
                        .updateHeader("Content-Length","0")
                        .withBody(""));

        Optional<Handle> handle = titanDispatcher.getHandleByLocation(new HandleLocation("fixtures", 3, 1));

        assertThat(false, equalTo(handle.isPresent()));

        clientAndServer.verify(HttpRequest
                .request("/titan/script/2/Handles/GetHandle")
                .withQueryStringParameter("group", "fixtures")
                .withQueryStringParameter("index", "3")
                .withQueryStringParameter("page", "1"));
    }

    @Test
    public void playbacksPlayCue() throws Exception {
        clientAndServer
                .when(HttpRequest.request("/titan/script/2/Playbacks/PlayCue"))
                .respond(HttpResponse.response()
                        .updateHeader("Content-Length","0")
                        .withBody(""));

        titanDispatcher.playbacksPlayCue("playbackwindow", 1, 0.5f, 1);

        clientAndServer.verify(HttpRequest
                .request("/titan/script/2/Playbacks/PlayCue")
                .withQueryStringParameter("group", "playbackwindow")
                .withQueryStringParameter("index", "1")
                .withQueryStringParameter("level", "0.5")
                .withQueryStringParameter("accuracy", "1"));
    }

    @Test
    public void playbacksStoreCue() throws Exception {
        clientAndServer
                .when(HttpRequest.request("/titan/script/2/Playbacks/StoreCue"))
                .respond(HttpResponse.response()
                        .updateHeader("Content-Length","0")
                        .withBody(""));

        titanDispatcher.playbacksStoreCue("playbackwindow", 3, false);

        clientAndServer.verify(HttpRequest
                .request("/titan/script/2/Playbacks/StoreCue")
                .withQueryStringParameter("group", "playbackwindow")
                .withQueryStringParameter("index", "3")
                .withQueryStringParameter("updateOnly", "false"));
    }

    @Test
    public void playbacksReplacePlaybackCue() throws Exception {
        clientAndServer
                .when(HttpRequest.request("/titan/script/2/Playbacks/ReplacePlaybackCue"))
                .respond(HttpResponse.response()
                        .updateHeader("Content-Length","0")
                        .withBody(""));

        titanDispatcher.playbacksReplacePlaybackCue(1111, true);

        clientAndServer.verify(HttpRequest
                .request("/titan/script/2/Playbacks/ReplacePlaybackCue")
                .withQueryStringParameter("handle_titanId", "1111")
                .withQueryStringParameter("updateOnly", "true"));
    }

    @Test
    public void programmerSetBlindMode() throws Exception {
        clientAndServer
                .when(HttpRequest.request("/titan/script/2/Programmer/SetBlindMode"))
                .respond(HttpResponse.response()
                        .updateHeader("Content-Length","0")
                        .withBody(""));

        titanDispatcher.programmerSetBlindMode(false, 0.3f);

        clientAndServer.verify(HttpRequest
                .request("/titan/script/2/Programmer/SetBlindMode")
                .withQueryStringParameter("setChangesLive", "false")
                .withQueryStringParameter("fadeTime", "0.3"));
    }

    @Test
    public void programmerIsBlindActive() throws Exception {
        clientAndServer
                .when(HttpRequest.request("/titan/get/2/Programmer/BlindActive"))
                .respond(HttpResponse.response()
                        .updateHeader("Content-Length","4")
                        .withBody("true"));

        boolean result = titanDispatcher.programmerIsBlindActive();

        assertThat(result, equalTo(true));
    }

    @Test
    public void playbacksSelectEditHandle() throws Exception {
        clientAndServer
                .when(HttpRequest.request("/titan/script/2/Playbacks/Select/EditHandle"))
                .respond(HttpResponse.response()
                        .updateHeader("Content-Length","0")
                        .withBody(""));

        titanDispatcher.playbacksSelectEditHandle(1111);

        clientAndServer.verify(HttpRequest
                .request("/titan/script/2/Playbacks/Select/EditHandle")
                .withQueryStringParameter("handle_titanId", "1111"));
    }

    @Test
    public void setPlaybacksPlaybackOptionsPriority() throws Exception {
        clientAndServer
                .when(HttpRequest.request("/titan/set/2/Playbacks/PlaybackOptions/Priority"))
                .respond(HttpResponse.response()
                        .updateHeader("Content-Length","0")
                        .withBody(""));

        titanDispatcher.setPlaybacksPlaybackOptionsPriority(75);

        clientAndServer.verify(HttpRequest
                .request("/titan/set/2/Playbacks/PlaybackOptions/Priority")
                .withMethod("POST")
                .withBody("75"));
    }

    @Test
    public void playbacksPlaybackEditExit() throws Exception {
        clientAndServer
                .when(HttpRequest.request("/titan/script/2/Playbacks/PlaybackEdit/Exit"))
                .respond(HttpResponse.response()
                        .updateHeader("Content-Length","0")
                        .withBody(""));

        titanDispatcher.playbacksPlaybackEditExit();

        clientAndServer.verify(HttpRequest
                .request("/titan/script/2/Playbacks/PlaybackEdit/Exit")
                .withBody(""));
    }

    @Test
    public void setProgrammerBlindActive() throws Exception {
        clientAndServer
                .when(HttpRequest.request("/titan/set/2/Programmer/BlindActive"))
                .respond(HttpResponse.response()
                        .updateHeader("Content-Length","0")
                        .withBody(""));

        titanDispatcher.setProgrammerBlindActive(true);

        clientAndServer.verify(HttpRequest
                .request("/titan/set/2/Programmer/BlindActive")
                .withBody("true"));
    }


}