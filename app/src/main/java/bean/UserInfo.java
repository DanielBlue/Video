package bean;

/**
 * Created by 李杰 on 2019/8/25.
 */

public class UserInfo {

    /**
     * code : 0
     * message : 成功
     * data : {"userInfo":{"uid":"59957169","phone":"13022220000","gender":1,"nickname":"丁阳","birthday":"1991-1-2","address":"湖北武汉","avatar":"https://user-gold-cdn.xitu.io/2018/12/3/1676feab8d523b8d?imageView2/1/w/180/h/180/q/85/format/webp/interlace/1","signature":"暂无个性签名","level":1,"regTime":1565499955000,"fansCount":0,"videoCount":5,"followCount":5,"getLikeCount":11}}
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
         * userInfo : {"uid":"59957169","phone":"13022220000","gender":1,"nickname":"丁阳","birthday":"1991-1-2","address":"湖北武汉","avatar":"https://user-gold-cdn.xitu.io/2018/12/3/1676feab8d523b8d?imageView2/1/w/180/h/180/q/85/format/webp/interlace/1","signature":"暂无个性签名","level":1,"regTime":1565499955000,"fansCount":0,"videoCount":5,"followCount":5,"getLikeCount":11}
         */

        private UserInfoBean userInfo;

        public UserInfoBean getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfoBean userInfo) {
            this.userInfo = userInfo;
        }
    }
}
