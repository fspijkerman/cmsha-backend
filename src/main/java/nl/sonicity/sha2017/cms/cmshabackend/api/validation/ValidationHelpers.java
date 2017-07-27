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
package nl.sonicity.sha2017.cms.cmshabackend.api.validation;

import com.google.common.base.Strings;

import java.util.Objects;

import static java.lang.String.format;

/**
 * Created by hugo on 09/07/2017.
 */
public class ValidationHelpers {
    private ValidationHelpers() {
    }

    public static Validation<Float> between(float lower, float upper){
        return SimpleValidation.from(s -> s >=lower && s <= upper, format("value must be between %f and %f (inclusive).", lower, upper));
    }

    public static Validation<Integer> between(int lower, int upper) {
        return SimpleValidation.from(s -> s >= lower && s <= upper, format("value must be between %d and %d (inclusive).", lower, upper));
    }

    public static Validation<Integer> notNull() {
        return SimpleValidation.from(Objects::nonNull, "Object should not be null");
    }

    public static Validation<String> notEmpty() {
        return SimpleValidation.from(s -> !Strings.isNullOrEmpty(s), "string should not be null or empty");
    }
}
