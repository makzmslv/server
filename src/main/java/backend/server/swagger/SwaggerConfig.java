package backend.server.swagger;

import static springfox.documentation.builders.PathSelectors.regex;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;

import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig
{

    @Value("${swagger.documentation.version}")
    private String apiVersion;

    /**
     * Every SwaggerSpringMvcPlugin bean is picked up by the swagger-mvc framework - allowing for multiple swagger groups i.e. same code base multiple swagger resource listings.
     */
    @Bean
    public Docket restApi()
    {
        // @formatter:off
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(new ApiInfo("Backend", "REST services", apiVersion, null, null, null, null))
            .directModelSubstitute(org.joda.time.LocalDate.class, LocalDate.class)
            .directModelSubstitute(org.joda.time.LocalTime.class, LocalTime.class)
            .directModelSubstitute(org.joda.time.LocalDateTime.class, LocalDateTime.class)
            .directModelSubstitute(org.joda.time.DateTime.class, DateTime.class)
            .select()
            .paths(sandppPaths())
            .build();
        // @formatter:on
    }

    private Predicate<String> sandppPaths()
    {
        return regex("/(?!(mvc-error|actuator)).*");
    }

    @Bean
    public Docket actuatorApi()
    {
        // @formatter:off
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(new ApiInfo("Spring Boot", "Actuator REST services", apiVersion, null, null, null, null ))
            .directModelSubstitute(org.joda.time.LocalDate.class, LocalDate.class)
            .directModelSubstitute(org.joda.time.LocalTime.class, LocalTime.class)
            .directModelSubstitute(org.joda.time.LocalDateTime.class, LocalDateTime.class)
            .directModelSubstitute(org.joda.time.DateTime.class, DateTime.class)
            .groupName("springBoot")
            .select()
            .paths(actuatorPaths())
            .build();
        // @formatter:on
    }

    private Predicate<String> actuatorPaths()
    {
        return regex("/actuator/.*");
    }

}
