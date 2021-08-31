package br.com.santander.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfigutarion {

        @Bean
        public Docket API() {
            return new Docket(DocumentationType.SWAGGER_2)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("br.com.santander.spring"))
                    .paths(PathSelectors.ant("/**"))
                    .build()
                    .apiInfo(metaDataAppInfo());
        }

        private ApiInfo metaDataAppInfo() {
            return new ApiInfoBuilder().title("[APURAÇÃO MICROSERVICE] - Spring Boot REST API for Swagger.")
                    .description("\"Documentação da API de acesso aos endpoints com Swagger - [APIREST MICROSERVICE]")
                    .version("1.0")
                    .license("Apache License Version 2.0")
                    .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0\"")
                    .build();
        }
    }

