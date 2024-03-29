package in.simplygeek.retrospective;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ScrumRetrospectiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScrumRetrospectiveApplication.class, args);
	}
	
	@GetMapping
    public ResponseEntity<String> healthCheck(){
    	return ResponseEntity.status(HttpStatus.OK).body("health check");
    }

}
