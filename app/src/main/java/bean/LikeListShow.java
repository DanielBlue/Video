package bean;

import java.util.List;

/**
 * Created by JC on 2019/9/7.
 */

public class LikeListShow {
    /**
     * code : 0
     * message : 成功
     * data : {"path":"/api/msg/list/zan","pageSize":10,"pageList":[{"uid":"58656517","nickName":"测试123456","avatar":"201909151568535152612.png","vid":"1568544879116343","videoUrl":"201909151568544877081.mp4","cover":"201909151568544879326.jpg","createTime":1568603355000},{"uid":"58656517","nickName":"测试123456","avatar":"201909151568535152612.png","vid":"1568454420641214","videoUrl":"201909141568454418194.mp4","cover":"201909141568454419921.jpg","createTime":1568603309000},{"uid":"52910447","nickName":"丁阳11","avatar":"201909151568533277271.jpg","vid":"1568451415547837","videoUrl":"201909141568451415231.mp4","cover":"201909141568451415738.jpg","createTime":1568529502000}],"page":1}
     * time : 2019-09-18 21:44:26
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
         * path : /api/msg/list/zan
         * pageSize : 10
         * pageList : [{"uid":"58656517","nickName":"测试123456","avatar":"201909151568535152612.png","vid":"1568544879116343","videoUrl":"201909151568544877081.mp4","cover":"201909151568544879326.jpg","createTime":1568603355000},{"uid":"58656517","nickName":"测试123456","avatar":"201909151568535152612.png","vid":"1568454420641214","videoUrl":"201909141568454418194.mp4","cover":"201909141568454419921.jpg","createTime":1568603309000},{"uid":"52910447","nickName":"丁阳11","avatar":"201909151568533277271.jpg","vid":"1568451415547837","videoUrl":"201909141568451415231.mp4","cover":"201909141568451415738.jpg","createTime":1568529502000}]
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
             * uid : 58656517
             * nickName : 测试123456
             * avatar : 201909151568535152612.png
             * vid : 1568544879116343
             * videoUrl : 201909151568544877081.mp4
             * cover : 201909151568544879326.jpg
             * createTime : 1568603355000
             */

            private String uid;
            private String nickName;
            private String avatar;
            private String vid;
            private String videoUrl;
            private String cover;
            private long createTime;

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

            public String getVid() {
                return vid;
            }

            public void setVid(String vid) {
                this.vid = vid;
            }

            public String getVideoUrl() {
                return videoUrl;
            }

            public void setVideoUrl(String videoUrl) {
                this.videoUrl = videoUrl;
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
