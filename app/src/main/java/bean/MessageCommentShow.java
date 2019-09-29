package bean;

import java.util.List;

/**
 * Created by JC on 2019/9/7.
 */

public class MessageCommentShow {

    /**
     * code : 0
     * message : 成功
     * data : {"path":"/api/msg/list/comment","pageSize":10,"pageList":[{"type":1,"uid":"59957169","nickName":"丁阳","avatar":"https://user-gold-cdn.xitu.io/2018/12/3/1676feab8d523b8d?imageView2/1/w/180/h/180/q/85/format/webp/interlace/1","comment":"你妈买菜必涨价超级加倍","vid":"5995716959957168","cover":"https://p9.pstatp.com/video1609/tos-cn-i-0000/454d76e0785311e9b7537cd30adcc96a","createTime":1566480878000},{"type":1,"uid":"78937422","nickName":"233332","avatar":"https://mirror-gold-cdn.xitu.io/169b998eca61f8f8ae4?imageView2/1/w/180/h/180/q/85/format/webp/interlace/1","comment":"你斗地主3456没有7","vid":"5995716959957168","cover":"https://p9.pstatp.com/video1609/tos-cn-i-0000/454d76e0785311e9b7537cd30adcc96a","createTime":1566484186000}],"page":1}
     * time : 2019-09-06 15:59:37
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
         * pageList : [{"type":1,"uid":"59957169","nickName":"丁阳","avatar":"https://user-gold-cdn.xitu.io/2018/12/3/1676feab8d523b8d?imageView2/1/w/180/h/180/q/85/format/webp/interlace/1","comment":"你妈买菜必涨价超级加倍","vid":"5995716959957168","cover":"https://p9.pstatp.com/video1609/tos-cn-i-0000/454d76e0785311e9b7537cd30adcc96a","createTime":1566480878000},{"type":1,"uid":"78937422","nickName":"233332","avatar":"https://mirror-gold-cdn.xitu.io/169b998eca61f8f8ae4?imageView2/1/w/180/h/180/q/85/format/webp/interlace/1","comment":"你斗地主3456没有7","vid":"5995716959957168","cover":"https://p9.pstatp.com/video1609/tos-cn-i-0000/454d76e0785311e9b7537cd30adcc96a","createTime":1566484186000}]
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
             * type : 1
             * uid : 59957169
             * nickName : 丁阳
             * avatar : https://user-gold-cdn.xitu.io/2018/12/3/1676feab8d523b8d?imageView2/1/w/180/h/180/q/85/format/webp/interlace/1
             * comment : 你妈买菜必涨价超级加倍
             * vid : 5995716959957168
             * cover : https://p9.pstatp.com/video1609/tos-cn-i-0000/454d76e0785311e9b7537cd30adcc96a
             * createTime : 1566480878000
             */

            private int type;
            private String uid;
            private String nickName;
            private String avatar;
            private String comment;
            private String vid;
            private String cover;
            private long createTime;

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
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

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }

            public String getVid() {
                return vid;
            }

            public void setVid(String vid) {
                this.vid = vid;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }
        }
    }
}
