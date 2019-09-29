package bean;

/**
 * Created by JC on 2019/9/18.
 */

public class OtherUserInfo {

    /**
     * code : 0
     * message : 成功
     * data : {"userInfo":{"uid":"58656517","phone":"18888888888","gender":2,"nickname":"测试123456","birthday":"2016-03-13","address":"河北省石家庄市","avatar":"201909151568535152612.png","signature":"123456789","level":4,"regTime":1565546755000,"followState":true,"fansCount":4,"videoCount":29,"followCount":1,"getLikeCount":3},"path":"/api/user/info"}
     * time : 2019-09-18 09:11:32
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
         * userInfo : {"uid":"58656517","phone":"18888888888","gender":2,"nickname":"测试123456","birthday":"2016-03-13","address":"河北省石家庄市","avatar":"201909151568535152612.png","signature":"123456789","level":4,"regTime":1565546755000,"followState":true,"fansCount":4,"videoCount":29,"followCount":1,"getLikeCount":3}
         * path : /api/user/info
         */

        private UserInfoBean userInfo;
        private String path;

        public UserInfoBean getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfoBean userInfo) {
            this.userInfo = userInfo;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public static class UserInfoBean {
            /**
             * uid : 58656517
             * phone : 18888888888
             * gender : 2
             * nickname : 测试123456
             * birthday : 2016-03-13
             * address : 河北省石家庄市
             * avatar : 201909151568535152612.png
             * signature : 123456789
             * level : 4
             * regTime : 1565546755000
             * followState : true
             * fansCount : 4
             * videoCount : 29
             * followCount : 1
             * getLikeCount : 3
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
            private boolean followState;
            private int fansCount;
            private int videoCount;
            private int followCount;
            private int getLikeCount;

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

            public boolean isFollowState() {
                return followState;
            }

            public void setFollowState(boolean followState) {
                this.followState = followState;
            }

            public int getFansCount() {
                return fansCount;
            }

            public void setFansCount(int fansCount) {
                this.fansCount = fansCount;
            }

            public int getVideoCount() {
                return videoCount;
            }

            public void setVideoCount(int videoCount) {
                this.videoCount = videoCount;
            }

            public int getFollowCount() {
                return followCount;
            }

            public void setFollowCount(int followCount) {
                this.followCount = followCount;
            }

            public int getGetLikeCount() {
                return getLikeCount;
            }

            public void setGetLikeCount(int getLikeCount) {
                this.getLikeCount = getLikeCount;
            }
        }
    }
}
