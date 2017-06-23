package nl.sonicity.sha2017.cms.cmshabackend.titan;

import nl.sonicity.sha2017.cms.cmshabackend.titan.models.Handle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
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

        String actual = titanDispatcher.getVersion().getValue();

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

        String actual = titanDispatcher.getShowName().getValue();

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

}