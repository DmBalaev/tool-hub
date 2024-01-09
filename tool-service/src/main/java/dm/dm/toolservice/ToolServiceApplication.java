package dm.dm.toolservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(title = "Tool hub", version = "1.0"),
		tags ={@Tag(name = "Registration and authentication", description = "Endpoints for registration and authentication"),
				@Tag(name = "Account management", description = "Endpoints for accounts"),
				@Tag(name = "Tool management", description = "Endpoints for tool"),
				@Tag(name = "Rental management", description = "Endpoints for comments")
		},
		servers = @Server(description = "Local", url = "http://localhost:8080")
)
@SecurityScheme(
		name = "BearerAuth",
		type = SecuritySchemeType.HTTP,
		scheme = "bearer",
		bearerFormat = "JWT",
		description = "Bearer token",
		in = SecuritySchemeIn.HEADER
)
public class ToolServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToolServiceApplication.class, args);
	}

}