package bean;

/**
 * Created by 李杰 on 2019/8/25.
 */

public class AttentionOrCancelPerson {

    /**
     * code : 0
     * message : 已关注
     * data : {"path":"/api/user/follow","followStatus":true}
     * time : 2019-09-03 16:48:39
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
         * path : /api/user/follow
         * followStatus : true
         */

        private String path;
        private boolean followStatus;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public boolean isFollowStatus() {
            return followStatus;
        }

        public void setFollowStatus(boolean followStatus) {
            this.followStatus = followStatus;
        }
    }
}
