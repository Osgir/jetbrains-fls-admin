package best.ccg.JbAdminApp.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class TransientConfigFileProduct {

    @Getter
    @Setter
    String product;
    @Getter
    @Setter
    List<String> userName;
    @Getter
    @Setter
    List<String> hostName;

    public String generateString() {
        String users = "";
        String hosts = "";
        if (userName != null) {
            int i = 1;
            for (String u : userName) {
                users += u;
                if (i < userName.size()) users += "|";
                i++;
            }
        }

        if (hostName != null) {
            int i = 1;
            for (String u : hostName) {
                hosts += u;
                if (i < hostName.size()) hosts += "|";
                i++;
            }
        }
        String s = "";
        if (!users.equals("")) {
            if (!product.equals("ALL")) s += "{\"product\" : \"" + product + "\" ,";
            else
                s += "{";
            s += " \"userName\" : \"(" + users + ")\"";
            s += "} ";
        }

        if (!users.equals("") && !hosts.equals("")) s += ",";
        if (!hosts.equals("")) {

            if (!product.equals("ALL")) s += "{\"product\" : \"" + product + "\" ,";
            else
                s += " {";
            s += " \"hostName\" : \"(" + hosts + ")\"";
            s += "} ";
        }
        return s;
    }
}
