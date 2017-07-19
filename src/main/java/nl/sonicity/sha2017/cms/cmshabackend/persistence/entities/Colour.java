package nl.sonicity.sha2017.cms.cmshabackend.persistence.entities;

import javax.persistence.Column;

/**
 * Created by htrippaers on 17/07/2017.
 */
public class Colour {
    @Column(nullable = false)
    private float red;

    @Column(nullable = false)
    private float green;

    @Column(nullable = false)
    private float blue;

    protected Colour() {}

    public Colour(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }
}
