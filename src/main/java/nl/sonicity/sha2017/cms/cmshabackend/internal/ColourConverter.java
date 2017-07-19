package nl.sonicity.sha2017.cms.cmshabackend.internal;

import nl.sonicity.sha2017.cms.cmshabackend.api.validation.ValidationHelpers;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.entities.Colour;

/**
 * Created by htrippaers on 18/07/2017.
 */
public class ColourConverter {
//    public String colourAsHex(Colour colour) {
//        float decimalValue = 255 * colour.getBlue();
//
//    }

    public static byte valueAsByte(float value) {
        ValidationHelpers.between(0, 1).test(value).orThrow();
        float decimalValue = 255 * value;
        return (byte)Math.abs(decimalValue);
    }

}
