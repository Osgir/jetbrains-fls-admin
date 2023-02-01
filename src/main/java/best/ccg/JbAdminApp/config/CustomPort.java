package best.ccg.JbAdminApp.config;

import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.stereotype.Component;

import static best.ccg.JbAdminApp.config.AppContext.loadConfiguration;

@Component
public class CustomPort implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
    @Override
    public void customize(ConfigurableServletWebServerFactory server) {
        loadConfiguration();

        server.setPort(AppSettings.appSettingsConfigPort);

    }
}
