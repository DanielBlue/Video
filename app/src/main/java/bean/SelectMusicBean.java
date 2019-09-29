package bean;

import java.util.List;

/**
 * Created by 李杰 on 2019/9/13.
 */

public class SelectMusicBean {

    /**
     * code : 0
     * message : 成功
     * data : {"path":"/api/audio/list/all","pageSize":10,"indexList":[{"id":"1022112249991419","name":"还好有你在","audioUrl":"http://pwe4y2ao3.bkt.clouddn.com/%E8%BF%98%E5%A5%BD%E6%9C%89%E4%BD%A0%E5%9C%A8.mp3","duration":59,"createTime":1565633155000},{"id":"1022112299291413","name":"还有我","audioUrl":"http://pwe4y2ao3.bkt.clouddn.com/%E8%BF%98%E6%9C%89%E6%88%91.mp3","duration":60,"createTime":1565633155000},{"id":"1022112299991432","name":"背对背拥抱","audioUrl":"http://pwe4y2ao3.bkt.clouddn.com/%E8%83%8C%E5%AF%B9%E8%83%8C%E6%8B%A5%E6%8A%B1.mp3","duration":60,"createTime":1565633155000},{"id":"1022212321121125","name":"我站在山的尽头","audioUrl":"http://pwe4y2ao3.bkt.clouddn.com/%E6%88%91%E7%AB%99%E5%9C%A8%E5%B1%B1%E7%9A%84%E5%B0%BD%E5%A4%B4..mp3","duration":60,"createTime":1565633155000},{"id":"1022212321321111","name":"Only when l met you did l rea","audioUrl":"http://pwe4y2ao3.bkt.clouddn.com/Only%20when%20l%20met%20you%20did%20l%20rea.mp3","duration":60,"createTime":1565633155000},{"id":"1022212321343411","name":"空蝉变奏曲","audioUrl":"http://pwe4y2ao3.bkt.clouddn.com/20190910153213443434.mp3","duration":214,"createTime":1565633155000},{"id":"1022212321543214","name":"最长的电影","audioUrl":"http://pwe4y2ao3.bkt.clouddn.com/Fire.mp3","duration":0,"createTime":1565633155000},{"id":"1022212321643217","name":"夜的第七章","audioUrl":"http://pwe4y2ao3.bkt.clouddn.com/Fire.mp3","duration":0,"createTime":1565633155000},{"id":"1022212331821128","name":"夏天","audioUrl":"http://pwe4y2ao3.bkt.clouddn.com/Fire.mp3","duration":0,"createTime":1565633155000},{"id":"1022214321343212","name":"能不能给我一首歌的时间","audioUrl":"http://pwe4y2ao3.bkt.clouddn.com/Fire.mp3","duration":0,"createTime":1565633155000}],"page":1}
     * time : 2019-09-13 00:10:37
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
         * path : /api/audio/list/all
         * pageSize : 10
         * indexList : [{"id":"1022112249991419","name":"还好有你在","audioUrl":"http://pwe4y2ao3.bkt.clouddn.com/%E8%BF%98%E5%A5%BD%E6%9C%89%E4%BD%A0%E5%9C%A8.mp3","duration":59,"createTime":1565633155000},{"id":"1022112299291413","name":"还有我","audioUrl":"http://pwe4y2ao3.bkt.clouddn.com/%E8%BF%98%E6%9C%89%E6%88%91.mp3","duration":60,"createTime":1565633155000},{"id":"1022112299991432","name":"背对背拥抱","audioUrl":"http://pwe4y2ao3.bkt.clouddn.com/%E8%83%8C%E5%AF%B9%E8%83%8C%E6%8B%A5%E6%8A%B1.mp3","duration":60,"createTime":1565633155000},{"id":"1022212321121125","name":"我站在山的尽头","audioUrl":"http://pwe4y2ao3.bkt.clouddn.com/%E6%88%91%E7%AB%99%E5%9C%A8%E5%B1%B1%E7%9A%84%E5%B0%BD%E5%A4%B4..mp3","duration":60,"createTime":1565633155000},{"id":"1022212321321111","name":"Only when l met you did l rea","audioUrl":"http://pwe4y2ao3.bkt.clouddn.com/Only%20when%20l%20met%20you%20did%20l%20rea.mp3","duration":60,"createTime":1565633155000},{"id":"1022212321343411","name":"空蝉变奏曲","audioUrl":"http://pwe4y2ao3.bkt.clouddn.com/20190910153213443434.mp3","duration":214,"createTime":1565633155000},{"id":"1022212321543214","name":"最长的电影","audioUrl":"http://pwe4y2ao3.bkt.clouddn.com/Fire.mp3","duration":0,"createTime":1565633155000},{"id":"1022212321643217","name":"夜的第七章","audioUrl":"http://pwe4y2ao3.bkt.clouddn.com/Fire.mp3","duration":0,"createTime":1565633155000},{"id":"1022212331821128","name":"夏天","audioUrl":"http://pwe4y2ao3.bkt.clouddn.com/Fire.mp3","duration":0,"createTime":1565633155000},{"id":"1022214321343212","name":"能不能给我一首歌的时间","audioUrl":"http://pwe4y2ao3.bkt.clouddn.com/Fire.mp3","duration":0,"createTime":1565633155000}]
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

        public static class IndexListBean {
            /**
             * id : 1022112249991419
             * name : 还好有你在
             * audioUrl : http://pwe4y2ao3.bkt.clouddn.com/%E8%BF%98%E5%A5%BD%E6%9C%89%E4%BD%A0%E5%9C%A8.mp3
             * duration : 59
             * createTime : 1565633155000
             */

            private String id;
            private String name;
            private String audioUrl;
            private int duration;
            private long createTime;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAudioUrl() {
                return audioUrl;
            }

            public void setAudioUrl(String audioUrl) {
                this.audioUrl = audioUrl;
            }

            public int getDuration() {
                return duration;
            }

            public void setDuration(int duration) {
                this.duration = duration;
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
