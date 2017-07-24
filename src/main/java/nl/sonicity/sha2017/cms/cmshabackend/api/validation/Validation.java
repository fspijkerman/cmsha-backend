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

/**
 * Created by hugo on 09/07/2017.
 * Based on https://medium.com/@jplanes/lambda-validations-with-java-8-86aa8143bd9f
 */
@FunctionalInterface
public interface Validation<K> {

    ValidationResult test(K param);

    default Validation<K> and(Validation<K> other) {
        return param -> {
            ValidationResult firstResult = this.test(param);
            return !firstResult.isValid() ? firstResult : other.test(param);
        };
    }

    default Validation<K> or(Validation<K> other) {
        return param -> {
            ValidationResult firstResult = this.test(param);
            return firstResult.isValid() ? firstResult : other.test(param);
        };
    }

}
