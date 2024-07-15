package banquemisr.challenge05.config;


import banquemisr.challenge05.security.jwt.TokenProvider;
import banquemisr.challenge05.service.impl.ReactiveUserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Bishoy samir
 *  Security configration
 */
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Slf4j
public class SecurityConfig {

    private final TokenProvider tokenProvider;


    public SecurityConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }


    @Order(1)
    @Bean
    SecurityWebFilterChain apiHttpSecurity(ServerHttpSecurity http) {
        http
                .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/webjars/**"))
                .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/v3/api-docs/swagger-config"))
                .authorizeExchange((exchanges) -> exchanges

                        .anyExchange().permitAll()
                );
        return http.build();
    }

    @Order(2)
    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authenticationManager());
        authenticationWebFilter.setServerAuthenticationConverter(new JwtServerAuthenticationConverter());

        return http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(HttpMethod.POST, "/authenticate/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/user/**").permitAll()
                        .pathMatchers("/v3/api-docs/**").permitAll()
                        .pathMatchers("/webjars/**").permitAll()
                        .pathMatchers("/swagger-ui/**").permitAll()
                        .pathMatchers("/swagger-ui.html").permitAll()
                        .pathMatchers("/v3/api-docs/swagger-config").permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Qualifier("auth")
    public ReactiveAuthenticationManager authenticationManager() {
        return authentication -> {
            String jwt = authentication.getCredentials().toString();
            boolean b = tokenProvider.validateToken(jwt);
            if (b) {
                return Mono.just(authentication);
            } else {
                return Mono.empty();
            }
        };
    }

    private class JwtServerAuthenticationConverter implements ServerAuthenticationConverter {
        private final String bearer = "Bearer ";

        @Override
        public Mono<Authentication> convert(ServerWebExchange exchange) {

            List<String> authorization = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
            if (authorization != null && authorization.size() > 0) {
                String authValue = new String(authorization.get(0).getBytes());
                if (authValue.startsWith(bearer) && authValue.trim().length() > bearer.length()) {
                    String jwt = new String(authorization.get(0).getBytes()).substring(bearer.length());
                    if (!jwt.equals("null")) {
                        return tokenProvider.getAuthentication(jwt);
                    } else {
                        return Mono.empty();
                    }
                } else
                    return Mono.empty();
            } else
                return Mono.empty();
        }
    }

    @Bean
    public UserDetailsRepositoryReactiveAuthenticationManager userDetailsRepositoryReactiveAuthenticationManager(
            PasswordEncoder passwordEncoder,
            ReactiveUserDetailsServiceImpl detailsService) {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(detailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }



}



