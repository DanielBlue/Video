package bean;

/**
 * Created by JC on 2019/9/11.
 */

public class ChangUserpassword {

    /**
     * code : 0
     * message : 密码修改成功!
     * data : {"currentTime":"2019-08-28 00:20:48","path":"/api/user/modify_pwd"}
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
        /**
         * currentTime : 2019-08-28 00:20:48
         * path : /api/user/modify_pwd
         */

        private String currentTime;
        private String path;

        public String getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime(String currentTime) {
            this.currentTime = currentTime;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
