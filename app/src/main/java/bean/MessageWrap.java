package bean;

/**
 * Created by 李杰 on 2019/9/9.
 */

public class MessageWrap {
    public final String message;

    public static MessageWrap getInstance(String message) {
        return new MessageWrap(message);
    }

    private MessageWrap(String message) {
        this.message = message;
    }
    public String getMessage(){
        return message;
    }
}
