package nl.sonicity.sha2017.cms.cmshabackend.internal;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by htrippaers on 18/07/2017.
 */
public class ColourConverterTest {
    @Test
    public void testPercentageToByte() throws Exception {
        for (float f = 0 ; f <= 1.00; f += 0.001) {
            System.out.println(String.format("%f => %d", f, Byte.toUnsignedInt(ColourConverter.valueAsByte(f))));
        }
    }
}