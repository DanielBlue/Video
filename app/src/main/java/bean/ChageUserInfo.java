package bean;

/**
 * Created by 李杰 on 2019/8/25.
 */

public class ChageUserInfo {


    /**
     * code : 0
     * message : 修改成功！
     * data : {"userInfo":{"uid":"84485553","phone":"18120440747","gender":2,"nickname":"23333","birthday":"1994-12-12","address":"武汉","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908271566836423382.jpg","signature":"你是魔鬼吗","level":3,"regTime":1565499955000,"deviceId":null,"deviceType":null}}
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
         * userInfo : {"uid":"84485553","phone":"18120440747","gender":2,"nickname":"23333","birthday":"1994-12-12","address":"武汉","avatar":"http://pwe4y2ao3.bkt.clouddn.com/201908271566836423382.jpg","signature":"你是魔鬼吗","level":3,"regTime":1565499955000,"deviceId":null,"deviceType":null}
         */

        private UserInfoBean userInfo;

        public UserInfoBean getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfoBean userInfo) {
            this.userInfo = userInfo;
        }

        public static class UserInfoBean {
            /**
             * uid : 84485553
             * phone : 18120440747
             * gender : 2
             * nickname : 23333
             * birthday : 1994-12-12
             * address : 武汉
             * avatar : http://pwe4y2ao3.bkt.clouddn.com/201908271566836423382.jpg
             * signature : 你是魔鬼吗
             * level : 3
             * regTime : 1565499955000
             * deviceId : null
             * deviceType : null
             */

            private String uid;
            private String phone;
            private int gender;
            private String nickname;
            private String birthday;
            private String address;
            private String avatar;
            private String signature;
            private int level;
            private long regTime;
            private Object deviceId;
            private Object deviceType;

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getSignature() {
                return signature;
            }

            public void setSignature(String signature) {
                this.signature = signature;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public long getRegTime() {
                return regTime;
            }

            public void setRegTime(long regTime) {
                this.regTime = regTime;
            }

            public Object getDeviceId() {
                return deviceId;
            }

            public void setDeviceId(Object deviceId) {
                this.deviceId = deviceId;
            }

            public Object getDeviceType() {
                return deviceType;
            }

            public void setDeviceType(Object deviceType) {
                this.deviceType = deviceType;
            }
        }
    }
}
