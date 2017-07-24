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

import java.util.function.Predicate;

/**
 * Created by hugo on 09/07/2017.
 * Based on https://medium.com/@jplanes/lambda-validations-with-java-8-86aa8143bd9f
 */
public class SimpleValidation<K> implements Validation<K> {

    private Predicate<K> predicate;
    private String onErrorMessage;

    private SimpleValidation(Predicate<K> predicate, String onErrorMessage) {
        this.predicate = predicate;
        this.onErrorMessage = onErrorMessage;
    }

    public static <K> SimpleValidation<K> from(Predicate<K> predicate, String onErrorMessage) {
        return new SimpleValidation<>(predicate, onErrorMessage);
    }

    @Override
    public ValidationResult test(K param) {
        return predicate.test(param) ? ValidationResult.ok() : ValidationResult.fail(onErrorMessage);
    }
}