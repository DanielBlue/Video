package bean;

/**
 * Created by 李杰 on 2019/8/25.
 */

public class UserLogin {

    /**
     * code : 0
     * message : 登录成功!
     * data : {}
     */

    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        public UserInfoBean user;
    }
}
