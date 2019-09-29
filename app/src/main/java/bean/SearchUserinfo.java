package bean;

import java.util.List;

/**
 * Created by JC on 2019/9/9.
 */

public class SearchUserinfo {

    /**
     * code : 0
     * message : 成功
     * data : {"path":"/api/user/search","pageSize":10,"pageList":[{"uid":"84485553","nickName":"方煜","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908281566976781790.png"}],"page":1}
     * time : 2019-09-04 00:25:45
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
         * path : /api/user/search
         * pageSize : 10
         * pageList : [{"uid":"84485553","nickName":"方煜","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908281566976781790.png"}]
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
             * uid : 84485553
             * nickName : 方煜
             * avatar : http://pwe4y2ao3.bkt.clouddn.com/201908281566976781790.png
             */

            private String uid;
            private String nickName;
            private String avatar;

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
        }
    }
}
