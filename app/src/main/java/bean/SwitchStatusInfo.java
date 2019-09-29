package bean;

import com.google.gson.annotations.SerializedName;

public class SwitchStatusInfo {

    /**
     * code : 0
     * message : 成功
     * data : {"path":"/api/msg/push/switch","switch":"关注","state":false}
     * time : 2019-09-09 17:43:56
     */

    public int code;
    public String message;
    public DataBean data;
    public String time;


    public static class DataBean {
        /**
         * path : /api/msg/push/switch
         * switch : 关注
         * state : false
         */

        public String path;
        @SerializedName("switch")
        public String switchX;
        public boolean state;

    }
}
