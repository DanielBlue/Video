package bean;

import java.util.List;

/**
 * Created by 李杰 on 2019/8/25.
 */

public class FindOtherUserVideo {

    /**
     * code : 0
     * message : 成功
     * data : {"pageSize":1,"indexList":[{"vid":"1022112294134532","uid":"6554675480","nickName":"测试","avatar":"https://mirror-gold-cdn.xitu.io/168e08a02f4dda88289?imageView2/1/w/180/h/180/q/85/format/webp/interlace/1","level":2,"audioName":"雨一直下","videoPath":"武汉","videoWidth":720,"videoHeight":480,"videoDesc":"这是一个测试视频","videoDuration":30,"coverPath":"https://p9.pstatp.com/video1609/tos-cn-i-0000/454d76e0785311e9b7537cd30adcc96a","likeCounts":0,"commentCounts":0,"shareCounts":0,"createTime":1565622355000,"videoUrl":"https://blz-videos.nosdn.127.net/1/OverWatch/OVR-S03_E03_McCree_REUNION_zhCN_1080P_mb78.mp4"}],"page":2}
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
         * pageSize : 1
         * indexList : [{"vid":"1022112294134532","uid":"6554675480","nickName":"测试","avatar":"https://mirror-gold-cdn.xitu.io/168e08a02f4dda88289?imageView2/1/w/180/h/180/q/85/format/webp/interlace/1","level":2,"audioName":"雨一直下","videoPath":"武汉","videoWidth":720,"videoHeight":480,"videoDesc":"这是一个测试视频","videoDuration":30,"coverPath":"https://p9.pstatp.com/video1609/tos-cn-i-0000/454d76e0785311e9b7537cd30adcc96a","likeCounts":0,"commentCounts":0,"shareCounts":0,"createTime":1565622355000,"videoUrl":"https://blz-videos.nosdn.127.net/1/OverWatch/OVR-S03_E03_McCree_REUNION_zhCN_1080P_mb78.mp4"}]
         * page : 2
         */

        private int pageSize;
        private int page;
        private List<IndexListBean> indexList;

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

        public static class IndexListBean {
            /**
             * vid : 1022112294134532
             * uid : 6554675480
             * nickName : 测试
             * avatar : https://mirror-gold-cdn.xitu.io/168e08a02f4dda88289?imageView2/1/w/180/h/180/q/85/format/webp/interlace/1
             * level : 2
             * audioName : 雨一直下
             * videoPath : 武汉
             * videoWidth : 720
             * videoHeight : 480
             * videoDesc : 这是一个测试视频
             * videoDuration : 30
             * coverPath : https://p9.pstatp.com/video1609/tos-cn-i-0000/454d76e0785311e9b7537cd30adcc96a
             * likeCounts : 0
             * commentCounts : 0
             * shareCounts : 0
             * createTime : 1565622355000
             * videoUrl : https://blz-videos.nosdn.127.net/1/OverWatch/OVR-S03_E03_McCree_REUNION_zhCN_1080P_mb78.mp4
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
            private int likeCounts;
            private int commentCounts;
            private int shareCounts;
            private long createTime;
            private String videoUrl;

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
        }
    }
}
