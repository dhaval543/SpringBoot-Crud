package backend.java.config;

import java.util.Arrays;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
            	registry.addMapping("/api/**")
                .allowedOrigins("http://http://34.28.9.29/")
                .allowedMethods("GET", "POST", "PATCH", "DELETE")
                .allowedHeaders("*")
                .exposedHeaders("Location")
                .allowCredentials(true);

            }
        };
    }
    @Bean
    public ApplicationRunner applicationRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("### Listing all registered Beans ###");
            Arrays.stream(ctx.getBeanDefinitionNames())
                  .sorted()
                  .forEach(System.out::println);
        };
    }
}
