package bean;

import java.util.List;

/**
 * Created by JC on 2019/9/18.
 */

public class CommentAtBean {


    /**
     * code : 0
     * message : 成功
     * data : {"path":"/api/msg/list/comment","pageSize":10,"pageList":[{"id":174,"commentUid":"58656517","content":"123","commentTime":1568603316000,"likeStatus":false,"atType":2,"atContent":null,"replyState":false,"authorReply":null,"coverPath":"201909141568454419921.jpg","videoUrl":"201909141568454418194.mp4","nickName":"测试123456","avatar":"201909151568535152612.png"},{"id":175,"commentUid":"58656517","content":"456","commentTime":1568603327000,"likeStatus":false,"atType":2,"atContent":null,"replyState":false,"authorReply":null,"coverPath":"201909141568454419921.jpg","videoUrl":"201909141568454418194.mp4","nickName":"测试123456","avatar":"201909151568535152612.png"},{"id":176,"commentUid":"58656517","content":"789","commentTime":1568603361000,"likeStatus":false,"atType":2,"atContent":null,"replyState":false,"authorReply":null,"coverPath":"201909151568544879326.jpg","videoUrl":"201909151568544877081.mp4","nickName":"测试123456","avatar":"201909151568535152612.png"},{"id":179,"commentUid":"58656517","content":"哈哈哈哈","commentTime":1568603434000,"likeStatus":false,"atType":1,"atContent":{"atUid":"6749242527","atNickName":"啦啦啦啦"},"replyState":false,"authorReply":null,"coverPath":"201909121568270304273.jpg","videoUrl":"201909121568270302058.mp4","nickName":"测试123456","avatar":"201909151568535152612.png"},{"id":182,"commentUid":"58656517","content":"123","commentTime":1568642218000,"likeStatus":false,"atType":1,"atContent":{"atUid":"6749242527","atNickName":"啦啦啦啦"},"replyState":false,"authorReply":null,"coverPath":"201909161568601709009.jpg","videoUrl":"201909161568601708088.mp4","nickName":"测试123456","avatar":"201909151568535152612.png"},{"id":184,"commentUid":"58656517","content":"123","commentTime":1568642242000,"likeStatus":false,"atType":1,"atContent":{"atUid":"6749242527","atNickName":"啦啦啦啦"},"replyState":false,"authorReply":null,"coverPath":"201909121568271513816.png","videoUrl":"201909121568271512241.mp4","nickName":"测试123456","avatar":"201909151568535152612.png"},{"id":186,"commentUid":"6749242527","content":"回复测试123456:我弄","commentTime":1568655703000,"likeStatus":false,"atType":2,"atContent":null,"replyState":false,"authorReply":null,"coverPath":"201909151568544879326.jpg","videoUrl":"201909151568544877081.mp4","nickName":"啦啦啦啦","avatar":"201909151568535152612.png"}],"page":1}
     * time : 2019-09-18 21:03:50
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
         * path : /api/msg/list/comment
         * pageSize : 10
         * pageList : [{"id":174,"commentUid":"58656517","content":"123","commentTime":1568603316000,"likeStatus":false,"atType":2,"atContent":null,"replyState":false,"authorReply":null,"coverPath":"201909141568454419921.jpg","videoUrl":"201909141568454418194.mp4","nickName":"测试123456","avatar":"201909151568535152612.png"},{"id":175,"commentUid":"58656517","content":"456","commentTime":1568603327000,"likeStatus":false,"atType":2,"atContent":null,"replyState":false,"authorReply":null,"coverPath":"201909141568454419921.jpg","videoUrl":"201909141568454418194.mp4","nickName":"测试123456","avatar":"201909151568535152612.png"},{"id":176,"commentUid":"58656517","content":"789","commentTime":1568603361000,"likeStatus":false,"atType":2,"atContent":null,"replyState":false,"authorReply":null,"coverPath":"201909151568544879326.jpg","videoUrl":"201909151568544877081.mp4","nickName":"测试123456","avatar":"201909151568535152612.png"},{"id":179,"commentUid":"58656517","content":"哈哈哈哈","commentTime":1568603434000,"likeStatus":false,"atType":1,"atContent":{"atUid":"6749242527","atNickName":"啦啦啦啦"},"replyState":false,"authorReply":null,"coverPath":"201909121568270304273.jpg","videoUrl":"201909121568270302058.mp4","nickName":"测试123456","avatar":"201909151568535152612.png"},{"id":182,"commentUid":"58656517","content":"123","commentTime":1568642218000,"likeStatus":false,"atType":1,"atContent":{"atUid":"6749242527","atNickName":"啦啦啦啦"},"replyState":false,"authorReply":null,"coverPath":"201909161568601709009.jpg","videoUrl":"201909161568601708088.mp4","nickName":"测试123456","avatar":"201909151568535152612.png"},{"id":184,"commentUid":"58656517","content":"123","commentTime":1568642242000,"likeStatus":false,"atType":1,"atContent":{"atUid":"6749242527","atNickName":"啦啦啦啦"},"replyState":false,"authorReply":null,"coverPath":"201909121568271513816.png","videoUrl":"201909121568271512241.mp4","nickName":"测试123456","avatar":"201909151568535152612.png"},{"id":186,"commentUid":"6749242527","content":"回复测试123456:我弄","commentTime":1568655703000,"likeStatus":false,"atType":2,"atContent":null,"replyState":false,"authorReply":null,"coverPath":"201909151568544879326.jpg","videoUrl":"201909151568544877081.mp4","nickName":"啦啦啦啦","avatar":"201909151568535152612.png"}]
         * page : 1
         */

        private String path;
        private int pageSize;
        private int page;
        private List<PageListBean> pageList;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public List<PageListBean> getPageList() {
            return pageList;
        }

        public void setPageList(List<PageListBean> pageList) {
            this.pageList = pageList;
        }

        public static class PageListBean {
            /**
             * id : 174
             * commentUid : 58656517
             * content : 123
             * commentTime : 1568603316000
             * likeStatus : false
             * atType : 2
             * atContent : null
             * replyState : false
             * authorReply : null
             * coverPath : 201909141568454419921.jpg
             * videoUrl : 201909141568454418194.mp4
             * nickName : 测试123456
             * avatar : 201909151568535152612.png
             */

            private int id;
            private String commentUid;
            private String content;
            private long commentTime;
            private boolean likeStatus;
            private int atType;
            private Object atContent;
            private boolean replyState;
            private Object authorReply;
            private String coverPath;
            private String videoUrl;
            private String nickName;
            private String avatar;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCommentUid() {
                return commentUid;
            }

            public void setCommentUid(String commentUid) {
                this.commentUid = commentUid;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public long getCommentTime() {
                return commentTime;
            }

            public void setCommentTime(long commentTime) {
                this.commentTime = commentTime;
            }

            public boolean isLikeStatus() {
                return likeStatus;
            }

            public void setLikeStatus(boolean likeStatus) {
                this.likeStatus = likeStatus;
            }

            public int getAtType() {
                return atType;
            }

            public void setAtType(int atType) {
                this.atType = atType;
            }

            public Object getAtContent() {
                return atContent;
            }

            public void setAtContent(Object atContent) {
                this.atContent = atContent;
            }

            public boolean isReplyState() {
                return replyState;
            }

            public void setReplyState(boolean replyState) {
                this.replyState = replyState;
            }

            public Object getAuthorReply() {
                return authorReply;
            }

            public void setAuthorReply(Object authorReply) {
                this.authorReply = authorReply;
            }

            public String getCoverPath() {
                return coverPath;
            }

            public void setCoverPath(String coverPath) {
                this.coverPath = coverPath;
            }

            public String getVideoUrl() {
                return videoUrl;
            }

            public void setVideoUrl(String videoUrl) {
                this.videoUrl = videoUrl;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }
        }
    }
}
