package nl.sonicity.sha2017.cms.cmshabackend.titan.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by hugo on 23/06/2017.
 */
public class KeyValue {
    private String key;
    private String value;

    @JsonCreator
    public KeyValue(@JsonProperty("Key") String key, @JsonProperty("Value") String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
