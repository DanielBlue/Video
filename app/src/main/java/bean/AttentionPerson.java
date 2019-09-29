package bean;

import java.util.List;

/**
 * Created by JC on 2019/9/3.
 */

public class AttentionPerson {

    /**
     * code : 0
     * message : 成功
     * data : {"pageSize":10,"path":"/api/user/list/follow","pageList":[{"uid":"6554675480","nickName":"李杰","avatar":"https://mirror-gold-cdn.xitu.io/168e08a02f4dda88289?imageView2/1/w/180/h/180/q/85/format/webp/interlace/1","followStatus":false},{"uid":"58656517","nickName":"戢飞","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908281566973054931.png","followStatus":false},{"uid":"84485553","nickName":"方煜","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908281566976781790.png","followStatus":false}],"page":1}
     * time : 2019-09-03 18:01:35
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
         * pageSize : 10
         * path : /api/user/list/follow
         * pageList : [{"uid":"6554675480","nickName":"李杰","avatar":"https://mirror-gold-cdn.xitu.io/168e08a02f4dda88289?imageView2/1/w/180/h/180/q/85/format/webp/interlace/1","followStatus":false},{"uid":"58656517","nickName":"戢飞","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908281566973054931.png","followStatus":false},{"uid":"84485553","nickName":"方煜","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908281566976781790.png","followStatus":false}]
         * page : 1
         */

        private int pageSize;
        private String path;
        private int page;
        private List<PageListBean> pageList;

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
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
             * uid : 6554675480
             * nickName : 李杰
             * avatar : https://mirror-gold-cdn.xitu.io/168e08a02f4dda88289?imageView2/1/w/180/h/180/q/85/format/webp/interlace/1
             * followStatus : false
             */

            private String uid;
            private String nickName;
            private String avatar;
            private boolean followStatus;

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

            public boolean isFollowStatus() {
                return followStatus;
            }

            public void setFollowStatus(boolean followStatus) {
                this.followStatus = followStatus;
            }
        }
    }
}
