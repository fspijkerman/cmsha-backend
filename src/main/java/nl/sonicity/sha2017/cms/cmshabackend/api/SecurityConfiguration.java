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
package nl.sonicity.sha2017.cms.cmshabackend.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * Created by hugo on 02/07/2017.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    public final static String ROLE_ADMIN = "ADMIN";
    public final static String ROLE_ANONYMOUS = "ANONYMOUS";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                csrf()
                    .disable().
                sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and().
                authorizeRequests().
                    antMatchers("/health/**")
                        .hasRole(ROLE_ADMIN).
                    anyRequest()
                        .permitAll().
                and().
                    anonymous()
                        .authorities(ROLE_ANONYMOUS);

        http
                .addFilterBefore(new AuthenticationFilter(authenticationManager()), BasicAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new ApiKeyAuthenticationProvider());
    }
}