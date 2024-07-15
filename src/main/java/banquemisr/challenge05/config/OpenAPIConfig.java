package banquemisr.challenge05.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Optional.ofNullable;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI customOpenAPI(WebFluxProperties properties) {
        String name = "bearer-key";
        return new OpenAPI().addServersItem(new Server().url(ofNullable(properties.getBasePath()).orElse("/"))).components(new Components().addSecuritySchemes(name, new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"))).addSecurityItem(new SecurityRequirement().addList(name)).info(new Info().title(" BankMiser API ").version("1.0"));
    }

    @Bean
    public GroupedOpenApi internalApi()
    {
        return GroupedOpenApi.builder()
                .group("internal").pathsToExclude("/partner/api/**")
                .packagesToScan("banquemisr.challenge05.web.rest")
                .build();
    }
}
