package best.ccg.JbAdminApp.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class TransientConfigFile {
    @Getter
    @Setter
    String name;
    @Getter
    @Setter
    List<TransientConfigFileProduct> products;

    public TransientConfigFile(String name, List<TransientConfigFileProduct> products) {
        this.name = name;
        this.products = products;
    }


    public String generateString() {
        int i = 1;
        String s =
                "{" +
                        "\"" + name + "\" : [";
        for (TransientConfigFileProduct p : products) {
            s += p.generateString();
            if (i < products.size())
                s += ",";
            i++;
        }
        s += "]}";
        return s;
    }
}
