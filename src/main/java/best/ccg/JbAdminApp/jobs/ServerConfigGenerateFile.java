package best.ccg.JbAdminApp.jobs;

import best.ccg.JbAdminApp.config.AppSettings;
import best.ccg.JbAdminApp.entity.TransientConfigFile;
import best.ccg.JbAdminApp.entity.TransientConfigFileProduct;
import best.ccg.JbAdminApp.service.JBServerBlacklistComputerService;
import best.ccg.JbAdminApp.service.JBServerBlacklistEmployeeService;
import best.ccg.JbAdminApp.service.JBServerProductBlacklistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServerConfigGenerateFile {

    @Autowired
    JBServerBlacklistEmployeeService jbServerBlacklistEmployeeService;
    @Autowired
    JBServerBlacklistComputerService jbServerBlacklistComputerService;
    @Autowired
    JBServerProductBlacklistService jbServerProductBlacklistService;

    private static final Logger logger = LoggerFactory.getLogger(ServerConfigGenerateFile.class);

    @Scheduled(cron = "0 0/5 * * * *")
    public void makeConfigFile() {

        logger.info("start");
        List<TransientConfigFile> conffile = new ArrayList<>();
        List<TransientConfigFileProduct> products = new ArrayList<>();
        jbServerProductBlacklistService.findIdValue().forEach(p -> {
            List<String> userNames = jbServerBlacklistEmployeeService.getEmployeeNameByProductid(p.getId());
            List<String> hostNames = jbServerBlacklistComputerService.getComputerNameByProductid(p.getId());
            if (userNames.size() > 0 || hostNames.size() > 0) {
                TransientConfigFileProduct transientConfigFileProduct = new TransientConfigFileProduct();
                transientConfigFileProduct.setProduct(p.getValueP());

                transientConfigFileProduct.setUserName(userNames);

                if (hostNames != null) {
                    transientConfigFileProduct.setHostName(hostNames);
                }

                products.add(transientConfigFileProduct);
            }
        });

        if (products.size() > 0) {
            TransientConfigFile blacklist = new TransientConfigFile("blacklist", products);
            conffile.add(blacklist);
            String fileValue = conffile.stream().map(TransientConfigFile::generateString).collect(Collectors.joining());

            if (!fileValue.equals("")) {
                try {
                    PrintWriter out = new PrintWriter(AppSettings.appSettingsConfigFilepath);
                    out.println(fileValue);
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }
}


