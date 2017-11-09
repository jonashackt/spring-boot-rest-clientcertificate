package de.jonashackt.restexamples.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.ant("/restexamples/*"))
				.build();
	}
	
	private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Restexamples Demo API")
                .description("A really interesting API, that you should know!")
                .termsOfServiceUrl("http://springfox.io")
                .license("Apache License Version 2.0")
                .licenseUrl("https://github.com/jonashackt/restexamples/blob/master/LICENSE")
                .version("2.0")
                .build();
    }
}
