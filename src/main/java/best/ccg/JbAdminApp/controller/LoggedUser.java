package best.ccg.JbAdminApp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;


@Component
public class LoggedUser implements HttpSessionBindingListener {

    static final Logger log =
            LoggerFactory.getLogger(LoggedUser.class);

    private String username;


    public LoggedUser(String username) {
        this.username = username;

    }

    public LoggedUser() {
    }

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        LoggedUser user = (LoggedUser) event.getValue();
        log.warn("User logged in : {}", user.getUsername());

    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        LoggedUser user = (LoggedUser) event.getValue();
        log.warn("User loged out : {}", user.getUsername());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}