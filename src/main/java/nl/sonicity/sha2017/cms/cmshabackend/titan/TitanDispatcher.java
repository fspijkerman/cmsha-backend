package nl.sonicity.sha2017.cms.cmshabackend.titan;

import com.fasterxml.jackson.core.io.SerializedString;
import nl.sonicity.sha2017.cms.cmshabackend.titan.models.Handle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

@Component
public class TitanDispatcher {

    private String baseUrl;
    private RestTemplate restTemplate;

    @Value("${titan.wepapi.url}")
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        restTemplate = new RestTemplate();
    }

    public SerializedString getVersion() {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/titan/get/System/SoftwareVersion");

        return restTemplate
                .getForObject(builder.build().encode().toString(), SerializedString.class);
    }

    public SerializedString getShowName() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/titan/get/Show/ShowName");

        return restTemplate
                .getForObject(builder.build().encode().toString(), SerializedString.class);

    }

    public List<Handle> listFixtures() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/titan/handles/Fixtures");

        Handle[] fixtures = restTemplate
                .getForObject(builder.build().encode().toString(), Handle[].class);

        return Arrays.asList(fixtures);
    }

    public List<Handle> listGroups() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/titan/handles/Groups");

        Handle[] groups = restTemplate
                .getForObject(builder.build().encode().toString(), Handle[].class);

        return Arrays.asList(groups);
    }

    public List<Handle> listColours() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/titan/handles/Colours");

        Handle[] colours = restTemplate
                .getForObject(builder.build().encode().toString(), Handle[].class);

        return Arrays.asList(colours);
    }

    public void firePlayback(int userNumber, float level, boolean alwaysRefire) throws Exception {
        if (level < 0 || level > 1) {
            throw new Exception("Invalid value for level");
        }
        DecimalFormat formatter = new DecimalFormat("#.###");

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/titan/script/Playbacks/FirePlaybackAtLevel")
                .queryParam("userNumber", userNumber)
                .queryParam("level", formatter.format(level))
                .queryParam("bool", alwaysRefire);

        restTemplate.exchange(builder.build().encode().toString(), HttpMethod.GET, null, Void.class);
    }
}
