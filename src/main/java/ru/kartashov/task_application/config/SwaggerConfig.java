package ru.kartashov.task_application.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@AllArgsConstructor
public class SwaggerConfig {
    private ServerProperties serverProperties;
    @Bean
    public OpenAPI init() {
        Integer port = serverProperties.getPort();
        Server taskServer = new Server();
        taskServer.setUrl("http://localhost:%s".formatted(port));

        Info info = new Info();
        info.title("Сервис задач");
        info.version("1.0");
        info.description("Система управления задачами");

        return new OpenAPI().info(info).servers(List.of(taskServer));
    }
}
