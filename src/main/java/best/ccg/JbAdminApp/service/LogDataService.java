package best.ccg.JbAdminApp.service;

import best.ccg.JbAdminApp.entity.JBServerComputer;
import best.ccg.JbAdminApp.entity.JBServerEmployee;
import best.ccg.JbAdminApp.entity.LogData;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LogDataService {

    private static final Logger logger = LoggerFactory.getLogger(LogDataService.class);

    public static List<LogData> getListLogData(String pathObject) {
        logger.debug("start ");
        try {
            URL url = new URL(pathObject);
            URLConnection request = url.openConnection();
            request.connect();
            InputStream inputStream = (InputStream) request.getContent();
            String serverData = IOUtils.toString(inputStream, String.valueOf(StandardCharsets.UTF_8));
            IOUtils.closeQuietly(inputStream);
            //TODO: check if serverData is valid json
            JSONArray jsonarray = new JSONArray(serverData);
            List<LogData> logData = new ArrayList<>();
            for (int i = 0; i < jsonarray.length(); i++) {
                if (jsonarray.getJSONObject(i).length() != 0) {
                    String userId = jsonarray.getJSONObject(i).getString("userId");
                    String userName = JBServerEmployee.getEmployeeName(userId);
                    String hostName = JBServerComputer.getCompName(userId);

                    String license = jsonarray.getJSONObject(i).getString("license");
                    String product = jsonarray.getJSONObject(i).getString("product");
                    String version = product.substring(product.indexOf(":") + 1);
                    String productBL = product.substring(0, product.indexOf(":")).toUpperCase();
                    logData.add(new LogData(userName, hostName, license, version, productBL, product));
                }
            }
            return logData;
        } catch (Exception e) {
            logger.debug("getListLogData error ");
            logger.error(e.getMessage());
        }
        return null;
    }
}
