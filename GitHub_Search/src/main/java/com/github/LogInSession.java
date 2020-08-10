package com.github;

import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * LogInSession Class for session operations
 */
@Component
public class LogInSession implements Serializable {

    public int loginStatus;

    /**
     * init loginStatus
     */
    public LogInSession(){ this.loginStatus = 0;}

    /**
     * getLoginStatus function
     * @return the loginStatus
     */
    public int getLoginStatus(){return loginStatus;}

    /**
     * setLoginStatus function
     * @param loginStatus update the loginStatus for session
     */
    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }
}
