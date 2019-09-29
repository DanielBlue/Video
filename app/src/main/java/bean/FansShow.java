package bean;

import java.util.List;

/**
 * Created by JC on 2019/9/4.
 */

public class FansShow {

    /**
     * code : 0
     * message : 成功
     * data : {"currentTime":"2019-08-29 22:37:22","pageSize":10,"path":"/api/user/list/fans","pageList":[{"uid":"78937422","nickName":"233332","avatar":"https://mirror-gold-cdn.xitu.io/169b998eca61f8f8ae4?imageView2/1/w/180/h/180/q/85/format/webp/interlace/1","followStatus":false,"followDate":1567089442560},{"uid":"58656517","nickName":"233332\n","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908281566973054931.png","followStatus":true,"followDate":1567089442646},{"uid":"59957169","nickName":"丁阳","avatar":"https://user-gold-cdn.xitu.io/2018/12/3/1676feab8d523b8d?imageView2/1/w/180/h/180/q/85/format/webp/interlace/1","followStatus":false,"followDate":1567089442733},{"uid":"6696029845","nickName":"测试","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908281566982161196.jpg","followStatus":false,"followDate":1567089442817}],"page":1}
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
         * currentTime : 2019-08-29 22:37:22
         * pageSize : 10
         * path : /api/user/list/fans
         * pageList : [{"uid":"78937422","nickName":"233332","avatar":"https://mirror-gold-cdn.xitu.io/169b998eca61f8f8ae4?imageView2/1/w/180/h/180/q/85/format/webp/interlace/1","followStatus":false,"followDate":1567089442560},{"uid":"58656517","nickName":"233332\n","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908281566973054931.png","followStatus":true,"followDate":1567089442646},{"uid":"59957169","nickName":"丁阳","avatar":"https://user-gold-cdn.xitu.io/2018/12/3/1676feab8d523b8d?imageView2/1/w/180/h/180/q/85/format/webp/interlace/1","followStatus":false,"followDate":1567089442733},{"uid":"6696029845","nickName":"测试","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908281566982161196.jpg","followStatus":false,"followDate":1567089442817}]
         * page : 1
         */

        private String currentTime;
        private int pageSize;
        private String path;
        private int page;
        private List<PageListBean> pageList;

        public String getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime(String currentTime) {
            this.currentTime = currentTime;
        }

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
             * uid : 78937422
             * nickName : 233332
             * avatar : https://mirror-gold-cdn.xitu.io/169b998eca61f8f8ae4?imageView2/1/w/180/h/180/q/85/format/webp/interlace/1
             * followStatus : false
             * followDate : 1567089442560
             */

            private String uid;
            private String nickName;
            private String avatar;
            private boolean followStatus;
            private long followDate;

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

            public long getFollowDate() {
                return followDate;
            }

            public void setFollowDate(long followDate) {
                this.followDate = followDate;
            }
        }
    }
}
