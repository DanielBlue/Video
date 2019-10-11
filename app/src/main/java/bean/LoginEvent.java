package bean;

import event.MessageEvent;

/**
 * Created by maoqi on 2019/10/11.
 */
public class LoginEvent extends MessageEvent {
    public static final int LOGIN = 1;
    public static final int LOGINOUT = 2;

    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public LoginEvent(int code) {
        this.code = code;
    }
}
