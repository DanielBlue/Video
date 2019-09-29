package bean;

import java.util.List;

/**
 * Created by 李杰 on 2019/8/25.
 */

public class VideoComment {


    /**
     * code : 0
     * message : 成功
     * data : {"commentList":[{"id":86,"commentUid":"84485553","content":"你以后炒菜必粘锅","commentTime":1567143185000,"likeStatus":false,"atType":2,"atContent":{"atUid":"59957169","atNickName":"丁阳"},"replyState":true,"authorReply":{"authorNickName":"丁阳","authorAvatar":"https://user-gold-cdn.xitu.io/2018/12/3/1676feab8d523b8d?imageView2/1/w/180/h/180/q/85/format/webp/interlace/1","replyContent":"你爸斗地主3456没有7","replyTime":1567245343000,"atType":1,"atContent":{"atUid":"59957169","atNickName":"丁阳"}},"nickName":"方煜","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908281566976781790.png"},{"id":87,"commentUid":"84485553","content":"你以后炒菜必粘锅","commentTime":1567143185000,"likeStatus":false,"atType":2,"atContent":{"atUid":null,"atNickName":null},"replyState":false,"authorReply":null,"nickName":"方煜","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908281566976781790.png"},{"id":84,"commentUid":"84485553","content":"你以后炒菜必粘锅","commentTime":1567143184000,"likeStatus":false,"atType":2,"atContent":{"atUid":null,"atNickName":null},"replyState":false,"authorReply":null,"nickName":"方煜","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908281566976781790.png"},{"id":85,"commentUid":"84485553","content":"你以后炒菜必粘锅","commentTime":1567143184000,"likeStatus":false,"atType":2,"atContent":{"atUid":null,"atNickName":null},"replyState":false,"authorReply":null,"nickName":"方煜","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908281566976781790.png"},{"id":83,"commentUid":"84485553","content":"你以后炒菜必粘锅","commentTime":1567143183000,"likeStatus":false,"atType":2,"atContent":{"atUid":null,"atNickName":null},"replyState":false,"authorReply":null,"nickName":"方煜","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908281566976781790.png"},{"id":82,"commentUid":"84485553","content":"你以后炒菜必粘锅","commentTime":1567143183000,"likeStatus":false,"atType":2,"atContent":{"atUid":null,"atNickName":null},"replyState":false,"authorReply":null,"nickName":"方煜","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908281566976781790.png"},{"id":81,"commentUid":"84485553","content":"你以后炒菜必粘锅","commentTime":1567143182000,"likeStatus":false,"atType":2,"atContent":{"atUid":null,"atNickName":null},"replyState":false,"authorReply":null,"nickName":"方煜","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908281566976781790.png"},{"id":79,"commentUid":"84485553","content":"你以后炒菜必粘锅","commentTime":1567143181000,"likeStatus":false,"atType":2,"atContent":{"atUid":null,"atNickName":null},"replyState":false,"authorReply":null,"nickName":"方煜","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908281566976781790.png"},{"id":80,"commentUid":"84485553","content":"你以后炒菜必粘锅","commentTime":1567143181000,"likeStatus":false,"atType":2,"atContent":{"atUid":null,"atNickName":null},"replyState":false,"authorReply":null,"nickName":"方煜","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908281566976781790.png"},{"id":78,"commentUid":"84485553","content":"你以后炒菜必粘锅","commentTime":1567143180000,"likeStatus":false,"atType":2,"atContent":{"atUid":null,"atNickName":null},"replyState":false,"authorReply":null,"nickName":"方煜","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908281566976781790.png"}],"path":"/api/video/list/comment","pageSize":10,"page":1}
     * time : 2019-09-02 11:32:27
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
         * commentList : [{"id":86,"commentUid":"84485553","content":"你以后炒菜必粘锅","commentTime":1567143185000,"likeStatus":false,"atType":2,"atContent":{"atUid":"59957169","atNickName":"丁阳"},"replyState":true,"authorReply":{"authorNickName":"丁阳","authorAvatar":"https://user-gold-cdn.xitu.io/2018/12/3/1676feab8d523b8d?imageView2/1/w/180/h/180/q/85/format/webp/interlace/1","replyContent":"你爸斗地主3456没有7","replyTime":1567245343000,"atType":1,"atContent":{"atUid":"59957169","atNickName":"丁阳"}},"nickName":"方煜","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908281566976781790.png"},{"id":87,"commentUid":"84485553","content":"你以后炒菜必粘锅","commentTime":1567143185000,"likeStatus":false,"atType":2,"atContent":{"atUid":null,"atNickName":null},"replyState":false,"authorReply":null,"nickName":"方煜","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908281566976781790.png"},{"id":84,"commentUid":"84485553","content":"你以后炒菜必粘锅","commentTime":1567143184000,"likeStatus":false,"atType":2,"atContent":{"atUid":null,"atNickName":null},"replyState":false,"authorReply":null,"nickName":"方煜","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908281566976781790.png"},{"id":85,"commentUid":"84485553","content":"你以后炒菜必粘锅","commentTime":1567143184000,"likeStatus":false,"atType":2,"atContent":{"atUid":null,"atNickName":null},"replyState":false,"authorReply":null,"nickName":"方煜","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908281566976781790.png"},{"id":83,"commentUid":"84485553","content":"你以后炒菜必粘锅","commentTime":1567143183000,"likeStatus":false,"atType":2,"atContent":{"atUid":null,"atNickName":null},"replyState":false,"authorReply":null,"nickName":"方煜","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908281566976781790.png"},{"id":82,"commentUid":"84485553","content":"你以后炒菜必粘锅","commentTime":1567143183000,"likeStatus":false,"atType":2,"atContent":{"atUid":null,"atNickName":null},"replyState":false,"authorReply":null,"nickName":"方煜","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908281566976781790.png"},{"id":81,"commentUid":"84485553","content":"你以后炒菜必粘锅","commentTime":1567143182000,"likeStatus":false,"atType":2,"atContent":{"atUid":null,"atNickName":null},"replyState":false,"authorReply":null,"nickName":"方煜","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908281566976781790.png"},{"id":79,"commentUid":"84485553","content":"你以后炒菜必粘锅","commentTime":1567143181000,"likeStatus":false,"atType":2,"atContent":{"atUid":null,"atNickName":null},"replyState":false,"authorReply":null,"nickName":"方煜","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908281566976781790.png"},{"id":80,"commentUid":"84485553","content":"你以后炒菜必粘锅","commentTime":1567143181000,"likeStatus":false,"atType":2,"atContent":{"atUid":null,"atNickName":null},"replyState":false,"authorReply":null,"nickName":"方煜","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908281566976781790.png"},{"id":78,"commentUid":"84485553","content":"你以后炒菜必粘锅","commentTime":1567143180000,"likeStatus":false,"atType":2,"atContent":{"atUid":null,"atNickName":null},"replyState":false,"authorReply":null,"nickName":"方煜","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908281566976781790.png"}]
         * path : /api/video/list/comment
         * pageSize : 10
         * page : 1
         */

        private String path;
        private int pageSize;
        private int page;
        private List<CommentListBean> commentList;

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

        public List<CommentListBean> getCommentList() {
            return commentList;
        }

        public void setCommentList(List<CommentListBean> commentList) {
            this.commentList = commentList;
        }

        public static class CommentListBean {
            /**
             * id : 86
             * commentUid : 84485553
             * content : 你以后炒菜必粘锅
             * commentTime : 1567143185000
             * likeStatus : false
             * atType : 2
             * atContent : {"atUid":"59957169","atNickName":"丁阳"}
             * replyState : true
             * authorReply : {"authorNickName":"丁阳","authorAvatar":"https://user-gold-cdn.xitu.io/2018/12/3/1676feab8d523b8d?imageView2/1/w/180/h/180/q/85/format/webp/interlace/1","replyContent":"你爸斗地主3456没有7","replyTime":1567245343000,"atType":1,"atContent":{"atUid":"59957169","atNickName":"丁阳"}}
             * nickName : 方煜
             * avatar : http://pwe4y2ao3.bkt.clouddn.com/201908281566976781790.png
             */

            private int id;
            private String commentUid;
            private String content;
            private long commentTime;
            private boolean likeStatus;
            private int atType;
            private AtContentBean atContent;
            private boolean replyState;
            private AuthorReplyBean authorReply;
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

            public AtContentBean getAtContent() {
                return atContent;
            }

            public void setAtContent(AtContentBean atContent) {
                this.atContent = atContent;
            }

            public boolean isReplyState() {
                return replyState;
            }

            public void setReplyState(boolean replyState) {
                this.replyState = replyState;
            }

            public AuthorReplyBean getAuthorReply() {
                return authorReply;
            }

            public void setAuthorReply(AuthorReplyBean authorReply) {
                this.authorReply = authorReply;
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

            public static class AtContentBean {
                /**
                 * atUid : 59957169
                 * atNickName : 丁阳
                 */

                private String atUid;
                private String atNickName;

                public String getAtUid() {
                    return atUid;
                }

                public void setAtUid(String atUid) {
                    this.atUid = atUid;
                }

                public String getAtNickName() {
                    return atNickName;
                }

                public void setAtNickName(String atNickName) {
                    this.atNickName = atNickName;
                }
            }

            public static class AuthorReplyBean {
                /**
                 * authorNickName : 丁阳
                 * authorAvatar : https://user-gold-cdn.xitu.io/2018/12/3/1676feab8d523b8d?imageView2/1/w/180/h/180/q/85/format/webp/interlace/1
                 * replyContent : 你爸斗地主3456没有7
                 * replyTime : 1567245343000
                 * atType : 1
                 * atContent : {"atUid":"59957169","atNickName":"丁阳"}
                 */

                private String authorNickName;
                private String authorAvatar;
                private String replyContent;
                private long replyTime;
                private int atType;
                private AtContentBeanX atContent;

                public String getAuthorNickName() {
                    return authorNickName;
                }

                public void setAuthorNickName(String authorNickName) {
                    this.authorNickName = authorNickName;
                }

                public String getAuthorAvatar() {
                    return authorAvatar;
                }

                public void setAuthorAvatar(String authorAvatar) {
                    this.authorAvatar = authorAvatar;
                }

                public String getReplyContent() {
                    return replyContent;
                }

                public void setReplyContent(String replyContent) {
                    this.replyContent = replyContent;
                }

                public long getReplyTime() {
                    return replyTime;
                }

                public void setReplyTime(long replyTime) {
                    this.replyTime = replyTime;
                }

                public int getAtType() {
                    return atType;
                }

                public void setAtType(int atType) {
                    this.atType = atType;
                }

                public AtContentBeanX getAtContent() {
                    return atContent;
                }

                public void setAtContent(AtContentBeanX atContent) {
                    this.atContent = atContent;
                }

                public static class AtContentBeanX {
                    /**
                     * atUid : 59957169
                     * atNickName : 丁阳
                     */

                    private String atUid;
                    private String atNickName;

                    public String getAtUid() {
                        return atUid;
                    }

                    public void setAtUid(String atUid) {
                        this.atUid = atUid;
                    }

                    public String getAtNickName() {
                        return atNickName;
                    }

                    public void setAtNickName(String atNickName) {
                        this.atNickName = atNickName;
                    }
                }
            }
        }
    }
}
