package bean;

/**
 * Created by 李杰 on 2019/8/25.
 */

public class PublishComment {

    /**
     * code : 0
     * message : 已成功评论本条内容!
     * data : {"path":"/api/comment/publish","comment":{"id":105,"content":"蛇皮走位看不见","userId":"6749242527","videoId":"5995716959957168","atUserId":"11111","atType":1,"createTime":1567648865884,"likeCounts":0,"replyContent":null,"replyTime":null,"replyAtType":null,"replyAtUid":null,"authorId":null}}
     * time : 2019-09-05 10:01:08
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
         * path : /api/comment/publish
         * comment : {"id":105,"content":"蛇皮走位看不见","userId":"6749242527","videoId":"5995716959957168","atUserId":"11111","atType":1,"createTime":1567648865884,"likeCounts":0,"replyContent":null,"replyTime":null,"replyAtType":null,"replyAtUid":null,"authorId":null}
         */

        private String path;
        private CommentBean comment;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public CommentBean getComment() {
            return comment;
        }

        public void setComment(CommentBean comment) {
            this.comment = comment;
        }

        public static class CommentBean {
            /**
             * id : 105
             * content : 蛇皮走位看不见
             * userId : 6749242527
             * videoId : 5995716959957168
             * atUserId : 11111
             * atType : 1
             * createTime : 1567648865884
             * likeCounts : 0
             * replyContent : null
             * replyTime : null
             * replyAtType : null
             * replyAtUid : null
             * authorId : null
             */

            private int id;
            private String content;
            private String userId;
            private String videoId;
            private String atUserId;
            private int atType;
            private long createTime;
            private int likeCounts;
            private Object replyContent;
            private Object replyTime;
            private Object replyAtType;
            private Object replyAtUid;
            private Object authorId;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getVideoId() {
                return videoId;
            }

            public void setVideoId(String videoId) {
                this.videoId = videoId;
            }

            public String getAtUserId() {
                return atUserId;
            }

            public void setAtUserId(String atUserId) {
                this.atUserId = atUserId;
            }

            public int getAtType() {
                return atType;
            }

            public void setAtType(int atType) {
                this.atType = atType;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getLikeCounts() {
                return likeCounts;
            }

            public void setLikeCounts(int likeCounts) {
                this.likeCounts = likeCounts;
            }

            public Object getReplyContent() {
                return replyContent;
            }

            public void setReplyContent(Object replyContent) {
                this.replyContent = replyContent;
            }

            public Object getReplyTime() {
                return replyTime;
            }

            public void setReplyTime(Object replyTime) {
                this.replyTime = replyTime;
            }

            public Object getReplyAtType() {
                return replyAtType;
            }

            public void setReplyAtType(Object replyAtType) {
                this.replyAtType = replyAtType;
            }

            public Object getReplyAtUid() {
                return replyAtUid;
            }

            public void setReplyAtUid(Object replyAtUid) {
                this.replyAtUid = replyAtUid;
            }

            public Object getAuthorId() {
                return authorId;
            }

            public void setAuthorId(Object authorId) {
                this.authorId = authorId;
            }
        }
    }
}
