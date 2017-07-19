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

import nl.sonicity.sha2017.cms.cmshabackend.persistence.entities.Colour;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by htrippaers on 18/07/2017.
 */
public class ColourConverterTest {
    @Test
    public void testFloatPercentageToByte() throws Exception {
        assertThat(ColourConverter.floatPercentageToInt(0.873f), equalTo(222));
        assertThat(ColourConverter.floatPercentageToInt(0f), equalTo(0));
        assertThat(ColourConverter.floatPercentageToInt(1.00f), equalTo(255));
    }

    @Test
    public void testRGBHex() throws Exception {
        Colour colour = new Colour(0.5f, 0f, 1f);
        assertThat(ColourConverter.colourAsRGBHex(colour), equalTo("7f00ff"));
    }
}