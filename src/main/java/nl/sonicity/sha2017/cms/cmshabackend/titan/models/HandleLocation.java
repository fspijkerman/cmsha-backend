package nl.sonicity.sha2017.cms.cmshabackend.titan.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by hugo on 23/06/2017.
 */
public class HandleLocation {
    private String group;
    private int index;
    private int page;

    @JsonCreator
    public HandleLocation(@JsonProperty("group") String group, @JsonProperty("index") int index, @JsonProperty("page") int page) {
        this.group = group;
        this.index = index;
        this.page = page;
    }

    public String getGroup() {
        return group;
    }

    public int getIndex() {
        return index;
    }

    public int getPage() {
        return page;
    }
}
