package farmconnect.farmconnectbackend.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Filter;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class AppConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Configuration
    public class QueryDslConfig {

        @PersistenceContext
        private EntityManager entityManager;

        @Bean
        public JPAQueryFactory jpaQueryFactory() {
            return new JPAQueryFactory(entityManager);
        }
    }
}