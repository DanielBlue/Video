package bean;

/**
 * Created by 李杰 on 2019/8/25.
 */

public class FindOtherUserInfo {

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

        public static class UserInfoBean {
            /**
             * uid : 59957169
             * phone : 13022220000
             * gender : 1
             * nickname : 丁阳
             * birthday : 1991-1-2
             * address : 湖北武汉
             * avatar : https://user-gold-cdn.xitu.io/2018/12/3/1676feab8d523b8d?imageView2/1/w/180/h/180/q/85/format/webp/interlace/1
             * signature : 暂无个性签名
             * level : 1
             * regTime : 1565499955000
             * fansCount : 0
             * videoCount : 5
             * followCount : 5
             * getLikeCount : 11
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
