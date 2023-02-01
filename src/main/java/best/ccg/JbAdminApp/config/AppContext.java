package best.ccg.JbAdminApp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

import static best.ccg.JbAdminApp.config.AppSettings.appSettingsDialect;
import static best.ccg.JbAdminApp.config.AppSettings.appSettingsdriverClassName;

@Configuration
@PropertySource("classpath:database.properties")
public class AppContext {
    public static String db_user;
    public static String db_pass;

    @Autowired
    private Environment environment;

    private static final Logger logger
            = LoggerFactory.getLogger(AppContext.class);

    @Bean
    @Qualifier(value = "transactionManager")
    public PlatformTransactionManager jpaTransactionManager(EntityManagerFactory emf) {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(emf);
        tm.setDataSource(dataSource());
        return tm;
    }

    protected static void loadConfiguration() {
        AppSettings.loadSettings();
        if ((System.getenv("JBADMIN_DB_USERNAME") != null &&
                System.getenv("JBADMIN_DB_PASSWORD") != null)) {
            db_user = System.getenv("JBADMIN_DB_USERNAME");
            db_pass = System.getenv("JBADMIN_DB_PASSWORD");
        } else {
            logger.error("No database credential found. Add JBADMIN_DB_USERNAME and JBADMIN_DB_PASSWORD.");
            System.exit(139);
        }
    }

    @Bean
    public DataSource dataSource() {
        loadConfiguration();

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(appSettingsdriverClassName);
        dataSource.setUrl(AppSettings.getString("jdbc.url"));

        dataSource.setUsername(db_user);
        dataSource.setPassword(db_pass);

        return dataSource;
    }

    @Bean
    protected Properties hibernateProperties() {
        Properties properties = new Properties();

        properties.put("hibernate.dialect", appSettingsDialect);
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
        properties.put("hibernate.hbm2ddl.auto", environment.getRequiredProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.cache.use_second_level_cache", "false");
        return properties;
    }
}