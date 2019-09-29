package bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 李杰 on 2019/9/7.
 */

public class SerachPublishVideo implements Serializable{


    /**
     * code : 0
     * message : 成功
     * data : {"path":"/api/video/list/user_publish","pageSize":10,"indexList":[{"vid":"1568544879116343","uid":"6749242527","nickName":"啦啦啦啦","avatar":"201909151568535152612.png","level":1,"audioName":"还有我","videoPath":"深圳","videoWidth":360,"videoHeight":640,"videoDesc":"我OK咯莫写作业","videoDuration":13375,"coverPath":"201909151568544879326.jpg","followStatus":false,"likeStatus":false,"likeCounts":1,"commentCounts":1,"shareCounts":0,"createTime":1568544879000,"videoUrl":"201909151568544877081.mp4","goodsId":"5","goodsName":"植物三宝洗发乳500ml/瓶"},{"vid":"1568454420641214","uid":"6749242527","nickName":"啦啦啦啦","avatar":"201909151568535152612.png","level":1,"audioName":"还好有你在","videoPath":"深圳","videoWidth":360,"videoHeight":640,"videoDesc":"我弄呀我","videoDuration":5898,"coverPath":"201909141568454419921.jpg","followStatus":false,"likeStatus":false,"likeCounts":1,"commentCounts":2,"shareCounts":0,"createTime":1568454420000,"videoUrl":"201909141568454418194.mp4","goodsId":"1","goodsName":"植物三宝辣木精华360"},{"vid":"1568453293956186","uid":"6749242527","nickName":"啦啦啦啦","avatar":"201909151568535152612.png","level":1,"audioName":"还好有你在","videoPath":"深圳","videoWidth":360,"videoHeight":640,"videoDesc":"5566","videoDuration":3855,"coverPath":"201909141568453293162.jpg","followStatus":false,"likeStatus":false,"likeCounts":0,"commentCounts":0,"shareCounts":0,"createTime":1568453293000,"videoUrl":"201909141568453291727.mp4","goodsId":"9","goodsName":"植物三宝洗衣液（普通）2.5KG/瓶"},{"vid":"1568453004409251","uid":"6749242527","nickName":"啦啦啦啦","avatar":"201909151568535152612.png","level":1,"audioName":"还有我","videoPath":"深圳","videoWidth":360,"videoHeight":640,"videoDesc":"957855","videoDuration":6223,"coverPath":"201909141568453004119.jpg","followStatus":false,"likeStatus":false,"likeCounts":0,"commentCounts":0,"shareCounts":0,"createTime":1568453004000,"videoUrl":"201909141568453001990.mp4","goodsId":"1","goodsName":"植物三宝辣木精华360"},{"vid":"1568451943781434","uid":"6749242527","nickName":"啦啦啦啦","avatar":"201909151568535152612.png","level":1,"audioName":"还有我","videoPath":"深圳","videoWidth":360,"videoHeight":640,"videoDesc":"556555988","videoDuration":4551,"coverPath":"201909141568451943186.jpg","followStatus":false,"likeStatus":false,"likeCounts":0,"commentCounts":0,"shareCounts":0,"createTime":1568451943000,"videoUrl":"201909141568451942185.mp4","goodsId":"5","goodsName":"植物三宝洗发乳500ml/瓶"},{"vid":"1568451562224010","uid":"6749242527","nickName":"啦啦啦啦","avatar":"201909151568535152612.png","level":1,"audioName":"还好有你在","videoPath":"深圳","videoWidth":360,"videoHeight":640,"videoDesc":"5565985665","videoDuration":8615,"coverPath":"201909141568451562048.jpg","followStatus":false,"likeStatus":false,"likeCounts":0,"commentCounts":0,"shareCounts":0,"createTime":1568451562000,"videoUrl":"201909141568451560143.mp4","goodsId":"1","goodsName":"植物三宝辣木精华360"},{"vid":"1568451415547837","uid":"6749242527","nickName":"啦啦啦啦","avatar":"201909151568535152612.png","level":1,"audioName":"还有我","videoPath":"深圳","videoWidth":360,"videoHeight":640,"videoDesc":"偷看你们瞳孔","videoDuration":3646,"coverPath":"201909141568451415738.jpg","followStatus":false,"likeStatus":false,"likeCounts":1,"commentCounts":0,"shareCounts":0,"createTime":1568451416000,"videoUrl":"201909141568451415231.mp4","goodsId":"10","goodsName":"植物三宝果蔬餐具净（浓缩）1KG/瓶"},{"vid":"1568451239931699","uid":"6749242527","nickName":"啦啦啦啦","avatar":"201909151568535152612.png","level":1,"audioName":"还好有你在","videoPath":"深圳","videoWidth":360,"videoHeight":640,"videoDesc":"58885","videoDuration":3646,"coverPath":"201909141568451239338.jpg","followStatus":false,"likeStatus":false,"likeCounts":0,"commentCounts":0,"shareCounts":0,"createTime":1568451239000,"videoUrl":"201909141568451238501.mp4","goodsId":"2","goodsName":"家用水龙头净水器"}],"page":1}
     * time : 2019-09-17 01:34:00
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
         * path : /api/video/list/user_publish
         * pageSize : 10
         * indexList : [{"vid":"1568544879116343","uid":"6749242527","nickName":"啦啦啦啦","avatar":"201909151568535152612.png","level":1,"audioName":"还有我","videoPath":"深圳","videoWidth":360,"videoHeight":640,"videoDesc":"我OK咯莫写作业","videoDuration":13375,"coverPath":"201909151568544879326.jpg","followStatus":false,"likeStatus":false,"likeCounts":1,"commentCounts":1,"shareCounts":0,"createTime":1568544879000,"videoUrl":"201909151568544877081.mp4","goodsId":"5","goodsName":"植物三宝洗发乳500ml/瓶"},{"vid":"1568454420641214","uid":"6749242527","nickName":"啦啦啦啦","avatar":"201909151568535152612.png","level":1,"audioName":"还好有你在","videoPath":"深圳","videoWidth":360,"videoHeight":640,"videoDesc":"我弄呀我","videoDuration":5898,"coverPath":"201909141568454419921.jpg","followStatus":false,"likeStatus":false,"likeCounts":1,"commentCounts":2,"shareCounts":0,"createTime":1568454420000,"videoUrl":"201909141568454418194.mp4","goodsId":"1","goodsName":"植物三宝辣木精华360"},{"vid":"1568453293956186","uid":"6749242527","nickName":"啦啦啦啦","avatar":"201909151568535152612.png","level":1,"audioName":"还好有你在","videoPath":"深圳","videoWidth":360,"videoHeight":640,"videoDesc":"5566","videoDuration":3855,"coverPath":"201909141568453293162.jpg","followStatus":false,"likeStatus":false,"likeCounts":0,"commentCounts":0,"shareCounts":0,"createTime":1568453293000,"videoUrl":"201909141568453291727.mp4","goodsId":"9","goodsName":"植物三宝洗衣液（普通）2.5KG/瓶"},{"vid":"1568453004409251","uid":"6749242527","nickName":"啦啦啦啦","avatar":"201909151568535152612.png","level":1,"audioName":"还有我","videoPath":"深圳","videoWidth":360,"videoHeight":640,"videoDesc":"957855","videoDuration":6223,"coverPath":"201909141568453004119.jpg","followStatus":false,"likeStatus":false,"likeCounts":0,"commentCounts":0,"shareCounts":0,"createTime":1568453004000,"videoUrl":"201909141568453001990.mp4","goodsId":"1","goodsName":"植物三宝辣木精华360"},{"vid":"1568451943781434","uid":"6749242527","nickName":"啦啦啦啦","avatar":"201909151568535152612.png","level":1,"audioName":"还有我","videoPath":"深圳","videoWidth":360,"videoHeight":640,"videoDesc":"556555988","videoDuration":4551,"coverPath":"201909141568451943186.jpg","followStatus":false,"likeStatus":false,"likeCounts":0,"commentCounts":0,"shareCounts":0,"createTime":1568451943000,"videoUrl":"201909141568451942185.mp4","goodsId":"5","goodsName":"植物三宝洗发乳500ml/瓶"},{"vid":"1568451562224010","uid":"6749242527","nickName":"啦啦啦啦","avatar":"201909151568535152612.png","level":1,"audioName":"还好有你在","videoPath":"深圳","videoWidth":360,"videoHeight":640,"videoDesc":"5565985665","videoDuration":8615,"coverPath":"201909141568451562048.jpg","followStatus":false,"likeStatus":false,"likeCounts":0,"commentCounts":0,"shareCounts":0,"createTime":1568451562000,"videoUrl":"201909141568451560143.mp4","goodsId":"1","goodsName":"植物三宝辣木精华360"},{"vid":"1568451415547837","uid":"6749242527","nickName":"啦啦啦啦","avatar":"201909151568535152612.png","level":1,"audioName":"还有我","videoPath":"深圳","videoWidth":360,"videoHeight":640,"videoDesc":"偷看你们瞳孔","videoDuration":3646,"coverPath":"201909141568451415738.jpg","followStatus":false,"likeStatus":false,"likeCounts":1,"commentCounts":0,"shareCounts":0,"createTime":1568451416000,"videoUrl":"201909141568451415231.mp4","goodsId":"10","goodsName":"植物三宝果蔬餐具净（浓缩）1KG/瓶"},{"vid":"1568451239931699","uid":"6749242527","nickName":"啦啦啦啦","avatar":"201909151568535152612.png","level":1,"audioName":"还好有你在","videoPath":"深圳","videoWidth":360,"videoHeight":640,"videoDesc":"58885","videoDuration":3646,"coverPath":"201909141568451239338.jpg","followStatus":false,"likeStatus":false,"likeCounts":0,"commentCounts":0,"shareCounts":0,"createTime":1568451239000,"videoUrl":"201909141568451238501.mp4","goodsId":"2","goodsName":"家用水龙头净水器"}]
         * page : 1
         */

        private String path;
        private int pageSize;
        private int page;
        private List<IndexListBean> indexList;

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

        public List<IndexListBean> getIndexList() {
            return indexList;
        }

        public void setIndexList(List<IndexListBean> indexList) {
            this.indexList = indexList;
        }

        public static class IndexListBean implements Serializable{
            /**
             * vid : 1568544879116343
             * uid : 6749242527
             * nickName : 啦啦啦啦
             * avatar : 201909151568535152612.png
             * level : 1
             * audioName : 还有我
             * videoPath : 深圳
             * videoWidth : 360
             * videoHeight : 640
             * videoDesc : 我OK咯莫写作业
             * videoDuration : 13375
             * coverPath : 201909151568544879326.jpg
             * followStatus : false
             * likeStatus : false
             * likeCounts : 1
             * commentCounts : 1
             * shareCounts : 0
             * createTime : 1568544879000
             * videoUrl : 201909151568544877081.mp4
             * goodsId : 5
             * goodsName : 植物三宝洗发乳500ml/瓶
             */

            private String vid;
            private String uid;
            private String nickName;
            private String avatar;
            private int level;
            private String audioName;
            private String videoPath;
            private int videoWidth;
            private int videoHeight;
            private String videoDesc;
            private int videoDuration;
            private String coverPath;
            private boolean followStatus;
            private boolean likeStatus;
            private int likeCounts;
            private int commentCounts;
            private int shareCounts;
            private long createTime;
            private String videoUrl;
            private String goodsId;
            private String goodsName;

            public String getVid() {
                return vid;
            }

            public void setVid(String vid) {
                this.vid = vid;
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

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public String getAudioName() {
                return audioName;
            }

            public void setAudioName(String audioName) {
                this.audioName = audioName;
            }

            public String getVideoPath() {
                return videoPath;
            }

            public void setVideoPath(String videoPath) {
                this.videoPath = videoPath;
            }

            public int getVideoWidth() {
                return videoWidth;
            }

            public void setVideoWidth(int videoWidth) {
                this.videoWidth = videoWidth;
            }

            public int getVideoHeight() {
                return videoHeight;
            }

            public void setVideoHeight(int videoHeight) {
                this.videoHeight = videoHeight;
            }

            public String getVideoDesc() {
                return videoDesc;
            }

            public void setVideoDesc(String videoDesc) {
                this.videoDesc = videoDesc;
            }

            public int getVideoDuration() {
                return videoDuration;
            }

            public void setVideoDuration(int videoDuration) {
                this.videoDuration = videoDuration;
            }

            public String getCoverPath() {
                return coverPath;
            }

            public void setCoverPath(String coverPath) {
                this.coverPath = coverPath;
            }

            public boolean isFollowStatus() {
                return followStatus;
            }

            public void setFollowStatus(boolean followStatus) {
                this.followStatus = followStatus;
            }

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

            public int getCommentCounts() {
                return commentCounts;
            }

            public void setCommentCounts(int commentCounts) {
                this.commentCounts = commentCounts;
            }

            public int getShareCounts() {
                return shareCounts;
            }

            public void setShareCounts(int shareCounts) {
                this.shareCounts = shareCounts;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public String getVideoUrl() {
                return videoUrl;
            }

            public void setVideoUrl(String videoUrl) {
                this.videoUrl = videoUrl;
            }

            public String getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(String goodsId) {
                this.goodsId = goodsId;
            }

            public String getGoodsName() {
                return goodsName;
            }

            public void setGoodsName(String goodsName) {
                this.goodsName = goodsName;
            }
        }
    }
}
