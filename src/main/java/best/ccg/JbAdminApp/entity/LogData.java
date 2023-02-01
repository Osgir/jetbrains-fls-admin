package best.ccg.JbAdminApp.entity;

public class LogData {

    String userName;
    String hostName;
    String license;
    String version;
    String productBL;
    String product;

    public LogData(String userName, String hostName, String license, String version, String productBL, String product) {
        this.userName = userName;
        this.hostName = hostName;
        this.license = license;
        this.version = version;
        this.productBL = productBL;
        this.product = product;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getProductBL() {
        return productBL;
    }

    public void setProductBL(String productBL) {
        this.productBL = productBL;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public LogData() {
    }

    @Override
    public String toString() {
        return "LogData{" +
                "userName='" + userName + '\'' +
                ", hostName='" + hostName + '\'' +
                ", license='" + license + '\'' +
                ", version='" + version + '\'' +
                ", productBL='" + productBL + '\'' +
                ", product='" + product + '\'' +
                '}';
    }
}
