package guru.sfg.brewery.config;

import guru.sfg.brewery.BreweryConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//       //  ((HttpSecurity)((HttpSecurity)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)
//                http
//                        .authorizeRequests(authorize -> {
//                            authorize.antMatchers("/", "/webjars/**", "login", "/resources/**").permitAll();
//                        })
//                    .authorizeRequests()
//                  .anyRequest().authenticated().and()
//                  .formLogin().and()
//                  .httpBasic();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorize -> {
            authorize
                    .requestMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll()
                    .requestMatchers("/beers/find", "/beers*", "/**").permitAll()
                    .requestMatchers(HttpMethod.GET,  BreweryConstants.BEER_API_PREFIX + BreweryConstants.BEER_API_SUFFIX + "*").permitAll()
                    .requestMatchers(HttpMethod.POST, BreweryConstants.BEER_API_PREFIX + BreweryConstants.BEER_API_SUFFIX + "*").permitAll()
                    .anyRequest().authenticated();
                   //  .requestMatchers("/**").authenticated().permitAll();
        })
        .httpBasic(Customizer.withDefaults())
        .formLogin(Customizer.withDefaults());
    //    .formLogin(form -> form.loginPage("/login").permitAll());

        return http.build();
    }

}
