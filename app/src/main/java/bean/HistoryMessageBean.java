package bean;

import java.util.List;

/**
 * Created by JC on 2019/9/12.
 */

public class HistoryMessageBean {

    /**
     * code : 0
     * message : 成功
     * data : {"path":"/api/msg/push/history","pushHistory":[{"id":1,"msgType":1,"msgContent":"测试","userId":"84485553","createTime":1568099508000}]}
     * time : 2019-09-10 17:59:03
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
         * path : /api/msg/push/history
         * pushHistory : [{"id":1,"msgType":1,"msgContent":"测试","userId":"84485553","createTime":1568099508000}]
         */

        private String path;
        private List<PushHistoryBean> pushHistory;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public List<PushHistoryBean> getPushHistory() {
            return pushHistory;
        }

        public void setPushHistory(List<PushHistoryBean> pushHistory) {
            this.pushHistory = pushHistory;
        }

        public static class PushHistoryBean {
            /**
             * id : 1
             * msgType : 1
             * msgContent : 测试
             * userId : 84485553
             * createTime : 1568099508000
             */

            private int id;
            private int msgType;
            private String msgContent;
            private String userId;
            private long createTime;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getMsgType() {
                return msgType;
            }

            public void setMsgType(int msgType) {
                this.msgType = msgType;
            }

            public String getMsgContent() {
                return msgContent;
            }

            public void setMsgContent(String msgContent) {
                this.msgContent = msgContent;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
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
