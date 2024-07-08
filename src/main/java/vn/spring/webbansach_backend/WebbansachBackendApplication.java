package vn.spring.webbansach_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = "vn.spring.webbansach_backend")
public class WebbansachBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(WebbansachBackendApplication.class, args);
	}
}

