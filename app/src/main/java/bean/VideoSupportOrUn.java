package bean;

/**
 * Created by 李杰 on 2019/8/25.
 */

public class VideoSupportOrUn {

    /**
     * code : 0
     * message : 已点赞
     * data : {"likeStatus":true,"likeCounts":2}
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
         * likeStatus : true
         * likeCounts : 2
         */

        private boolean likeStatus;
        private int likeCounts;

        public boolean isLikeStatus() {
            return likeStatus;
        }

        public void setLikeStatus(boolean likeStatus) {
            this.likeStatus = likeStatus;
        }

        public int getLikeCounts() {
            return likeCounts;
        }

        public void setLikeCounts(int likeCounts) {
            this.likeCounts = likeCounts;
        }
    }
}
