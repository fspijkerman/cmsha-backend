package nl.sonicity.sha2017.cms.cmshabackend.titan.models;

/**
 * Created by hugo on 25/06/2017.
 */
public enum HandleType {
    Fixtures("Fixtures"),
    Groups("Groups"),
    Macros("Macros"),
    Playbacks("Playbacks"),
    StaticPlaybacks("StaticPlaybacks"),
    Colours("Colours"),
    Workspaces("Workspaces"),
    Effects("Effects"),
    RollerA("RollerA"),
    PlaybackWindow("PlaybackWindow");

    private String titanGroupName;

    HandleType(String titanGroupName) {
        this.titanGroupName = titanGroupName;
    }

    public String getTitanGroupName() {
        return titanGroupName;
    }
}
