package farmconnect.farmconnectbackend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class FirebaseInitialize {
    @Value("${firebase.config.path}")
    String firebaseConfigPath;

    @PostConstruct
    public void initialize() {
        FirebaseOptions options = null;
        try {
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream()))
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FirebaseApp.initializeApp(options);
    }

}
