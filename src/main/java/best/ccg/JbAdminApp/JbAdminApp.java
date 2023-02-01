package best.ccg.JbAdminApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
public class JbAdminApp {

    public static void main(String[] args) {
        SpringApplication.run(JbAdminApp.class, args);
    }

}