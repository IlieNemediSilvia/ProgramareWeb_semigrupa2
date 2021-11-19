package demo;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskConfiguration {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("Tasks API")
                .version("1.0")
                .description("This is a sample CRUD Tasks API"));
    }
}
