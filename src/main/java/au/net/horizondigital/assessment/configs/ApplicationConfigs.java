package au.net.horizondigital.assessment.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
@PropertySource(value = "file:/opt/conf/app-configs.properties")
public class ApplicationConfigs {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("au.net.horizondigital.assessment.controllers")).
                paths(PathSelectors.any()).build()
                .apiInfo(new ApiInfo("Mubeen's Assessment Task",
                        "Some basic APIs for the coffee shop assessment task",
                        "v1.0", null, new Contact("Mubeen Ali", null, "mubeenalisindhu@gmail.com"), null, null, Collections.emptyList()));

    }
}
