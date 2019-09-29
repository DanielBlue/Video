package bean;

/**
 * Created by 李杰 on 2019/8/25.
 */

public class UserIMG {

    /**
     * code : 0
     * message : 成功修改头像！
     * data : {"path":"/api/user/upload_avatar","avatarUrl":"http://pwe4y2ao3.bkt.clouddn.com/201909061567733742462..jpg"}
     * time : 2019-09-06 09:35:42
     */

    private int code;
    private String message;
    private DataBean data;
    private String time;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static class DataBean {
        /**
         * path : /api/user/upload_avatar
         * avatarUrl : http://pwe4y2ao3.bkt.clouddn.com/201909061567733742462..jpg
         */

        private String path;
        private String avatarUrl;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }
    }
}
