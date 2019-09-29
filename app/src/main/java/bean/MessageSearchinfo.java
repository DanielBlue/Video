package bean;

/**
 * Created by 李杰 on 2019/9/9.
 */

public class MessageSearchinfo {
    public final String message;

    public static MessageSearchinfo getInstance(String message) {
        return new MessageSearchinfo(message);
    }

    private MessageSearchinfo(String message) {
        this.message = message;
    }
    public String getMessage(){
        return message;
    }
}
