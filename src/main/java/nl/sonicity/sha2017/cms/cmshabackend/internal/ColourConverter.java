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
package nl.sonicity.sha2017.cms.cmshabackend.internal;

import nl.sonicity.sha2017.cms.cmshabackend.api.validation.ValidationHelpers;
import nl.sonicity.sha2017.cms.cmshabackend.persistence.entities.Colour;

/**
 * Created by htrippaers on 18/07/2017.
 */
public class ColourConverter {

    private ColourConverter() {
    }

    public static String colourAsRGBHex(Colour colour) {
        return String.format("%02x%02x%02x",
                floatPercentageToInt(colour.getRed()),
                floatPercentageToInt(colour.getGreen()),
                floatPercentageToInt(colour.getBlue()));
    }

    /**
     * Converts a value between 0 and 1 to the corresponding int value between 0 and 255
     * @param value
     * @return
     */
    public static int floatPercentageToInt(float value) {
        ValidationHelpers.between(0, 1).test(value).orThrow();
        float decimalValue = 255 * value;
        return (int)Math.abs(decimalValue);
    }

}
