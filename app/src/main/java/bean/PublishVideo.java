package bean;

/**
 * Created by 李杰 on 2019/8/25.
 */

public class PublishVideo {

    /**
     * code : 0
     * message : 成功上传
     * data : {"videoInfo":{"id":"1566550233758441","userId":"84485553","audioId":"1022112299991432","videoPath":"西安","videoDesc":"哈哈哈哈哈","videoDuration":60,"videoWidth":473,"videoHeight":896,"coverPath":"http://pwe4y2ao3.bkt.clouddn.com/201908231566550233129.jpg","likeCounts":0,"commentCounts":0,"shareCounts":0,"status":1,"createTime":1566550233911,"videoUrl":"http://pwe4y2ao3.bkt.clouddn.com/201908231566550231260.mp4"}}
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
         * videoInfo : {"id":"1566550233758441","userId":"84485553","audioId":"1022112299991432","videoPath":"西安","videoDesc":"哈哈哈哈哈","videoDuration":60,"videoWidth":473,"videoHeight":896,"coverPath":"http://pwe4y2ao3.bkt.clouddn.com/201908231566550233129.jpg","likeCounts":0,"commentCounts":0,"shareCounts":0,"status":1,"createTime":1566550233911,"videoUrl":"http://pwe4y2ao3.bkt.clouddn.com/201908231566550231260.mp4"}
         */

        private VideoInfoBean videoInfo;

        public VideoInfoBean getVideoInfo() {
            return videoInfo;
        }

        public void setVideoInfo(VideoInfoBean videoInfo) {
            this.videoInfo = videoInfo;
        }

        public static class VideoInfoBean {
            /**
             * id : 1566550233758441
             * userId : 84485553
             * audioId : 1022112299991432
             * videoPath : 西安
             * videoDesc : 哈哈哈哈哈
             * videoDuration : 60
             * videoWidth : 473
             * videoHeight : 896
             * coverPath : http://pwe4y2ao3.bkt.clouddn.com/201908231566550233129.jpg
             * likeCounts : 0
             * commentCounts : 0
             * shareCounts : 0
             * status : 1
             * createTime : 1566550233911
             * videoUrl : http://pwe4y2ao3.bkt.clouddn.com/201908231566550231260.mp4
             */

            private String id;
            private String userId;
            private String audioId;
            private String videoPath;
            private String videoDesc;
            private int videoDuration;
            private int videoWidth;
            private int videoHeight;
            private String coverPath;
            private int likeCounts;
            private int commentCounts;
            private int shareCounts;
            private int status;
            private long createTime;
            private String videoUrl;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getAudioId() {
                return audioId;
            }

            public void setAudioId(String audioId) {
                this.audioId = audioId;
            }

            public String getVideoPath() {
                return videoPath;
            }

            public void setVideoPath(String videoPath) {
                this.videoPath = videoPath;
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

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
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
