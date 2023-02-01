package best.ccg.JbAdminApp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;

public class AppSettings {
    public static Boolean appSettingsHasIdentifiers;
    public static String appSettingsErrorLogJobFilepath;
    public static String appSettingsConfigFilepath;
    public static String appSettingsReportlink;

    public static String appSettingsdriverClassName;
    public static String appSettingsDialect;

    public static int appSettingsConfigPort;

    private HashMap fHashMap;

    private static AppSettings appSettings;

    private static final Logger logger = LoggerFactory.getLogger(AppSettings.class);

    public static void loadSettings() {
        try {
            File file = new File("settings.xml");
            AppSettings.load(file);

            appSettingsHasIdentifiers = AppSettings.getBoolean("JBServer.isHasIdentifiers");
            appSettingsErrorLogJobFilepath = AppSettings.getString("JBServerErrorLogJob.filepath");
            appSettingsReportlink = AppSettings.getString("JBServer.reportlink");
            appSettingsConfigFilepath = AppSettings.getString("JBServerConfigFile.filepath");

            appSettingsdriverClassName = AppSettings.getString("jdbc.driverClassName");
            appSettingsDialect = AppSettings.getString("hibernate.dialect");

            appSettingsConfigPort = AppSettings.getInt("JBServerConfigFile.serverport",81);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private AppSettings() {
        fHashMap = new HashMap();
    }

    static {
        appSettings = new AppSettings();
    }

    public static Object get(String key) {
        return appSettings.fHashMap.get(key);
    }

    public static Object get(String key, Object deflt) {
        Object obj = appSettings.fHashMap.get(key);
        if (obj == null) {
            return deflt;
        } else {
            return obj;
        }
    }

    public static String getString(String key) {
        return appSettings.fHashMap.get(key).toString();
    }

    public static Boolean getBoolean(String key) {
        return !appSettings.fHashMap.get(key).toString().equals("0");
    }

    public static int getInt(String key, int deflt) {
        Object obj = appSettings.fHashMap.get(key);
        if (obj == null) {
            return deflt;
        } else {
            return Integer.valueOf((String) obj);
        }
    }

    public static void put(String key, Object data) {

        if (data == null) {
            logger.error("data is null");
        } else {
            appSettings.fHashMap.put(key, data);
        }
    }


    public static boolean load(File file) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        if (doc == null) {
            throw new NullPointerException();
        }
        NodeList propertiesNL = doc.getDocumentElement().getChildNodes();
        for (int i = 0; (i < propertiesNL.getLength()); i++) {
            if (propertiesNL.item(i).getNodeName().equals("properties")) {
                NodeList propertyList = propertiesNL.item(i).getChildNodes();
                for (int j = 0; j < propertyList.getLength(); j++) {
                    NamedNodeMap attributes = propertyList.item(j).getAttributes();
                    Node n = attributes.getNamedItem("key");
                    NodeList childs = propertyList.item(j).getChildNodes();
                    for (int k = 0; k < childs.getLength(); k++) {
                        if (childs.item(k).getNodeType() == Node.TEXT_NODE) {
                            put(n.getNodeValue(), childs.item(k).getNodeValue());
                        }
                    }
                }
            }
        }
        return true;
    }

}
