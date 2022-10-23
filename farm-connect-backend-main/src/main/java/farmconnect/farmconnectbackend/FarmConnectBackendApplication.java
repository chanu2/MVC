package farmconnect.farmconnectbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;


@SpringBootApplication
@EnableScheduling
public class FarmConnectBackendApplication {

	@PostConstruct
	public void started() {
		// timezone UTC 셋팅
		//TimeZone.setDefault(TimeZone.getTimeZone("KST"));
	}

	public static void main(String[] args) {

		SpringApplication.run(FarmConnectBackendApplication.class, args);
	}

}
