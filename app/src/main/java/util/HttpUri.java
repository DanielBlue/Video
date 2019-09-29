package util;

/**
 * Created by JC on 2019/8/24.
 */

public class HttpUri {

    //    public static String BASE_URL = "http://47.104.15.124:8090";//
//    public static String BASE_DOMAIN = "http://cdn.health.healthplatform.xyz/";
    public static String BASE_URL = "http://shortvideo.jdecology.com";//
    public static String BASE_DOMAIN = "http://shortvideo.healthplatform.xyz/";

    public interface LoginOrRegister {
        String REQUEST_HEADER_LOGIN = "/api/login";
        String REQUEST_HEADER_SMS = "/api/sms";
        String REQUEST_HEADER_REGISTER = "/api/register";
        String REQUEST_HEADER_LOGOUT = "/api/logout";
    }

    public interface PersonInfo {
        //个人信息资料
        String REQUEST_HEADER_PERSONINFO = "/api/user/person_info";
        String REQUEST_HEADER_USERIMG = "/api/user/upload_avatar";
        String REQUEST_HEADER_CHANGEUSERINFO = "/api/user/modify_info";
        String REQUEST_HEADER_ATTENTIONUSERSTATUS = "/api/user/follow";
        String REQUEST_HEADER_USEROTHERINFO = "/api/user/info";
        String REQUEST_HEADER_CHANGEPASSWORD = "/api/user/modify_pwd";
        String REQUEST_HEADER_ATTENTIONPERSONQUERY = "/api/user/list/follow";
        String REQUEST_HEADER_MYSELF_FANS_QUERY = "/api/user/list/fans";
        String REQUEST_HEADER_SEARCH = "/api/user/search";
        String REQUEST_HEADER_SWITCH_LIST = "/api/msg/push/switch/list";
    }

    public interface VIDEO {
        //视频相关
        String REQUEST_HEADER_SEARCHUSERVIDEO = "/api/video/list/user_publish";
        String REQUEST_HEADER_HOMEVIDEO = "/api/video/list/index";
        String REQUEST_HEADER_ATTENTIONUSERVIDEO = "/api/video/list/follow";
        String REQUEST_HEADER_SEARCHVIDEO = "/api/video/search";
        String REQUEST_HEADER_PUBLISHVIDEO = "/api/video/publish";
        String REQUEST_HEADER_VIDEOLIKE = "/api/video/zan";
        //    String REQUEST_HEADER_VIDEOCOMMENT="/api/video/list/comment";
        String REQUEST_HEADER_VIDEOCOMMENT = "/api/video/list/all_comment";
        String REQUEST_HEADER_PUBLISHCOMMENT = "/api/comment/publish";
        String REQUEST_HEADER_REPLYCOMMENT = "/api/comment/reply";
        String REQUEST_HEADER_COMMENTLIKE = "/api/comment/zan";
        String REQUEST_HEADER_SHAREVIDEO = "/api/video/share";
        String REQUEST_HEADER_CHANNEl = "/api/video/list/channel";
    }

    public interface BGM {
        //背景音乐
        String REQUEST_HEADER_BGM = "/api/audio/list/all";
    }

    public interface MESSAGE {
        String REQUEST_HEADER_VIDEOAPPECT = "/api/msg/list/comment";
        String REQUEST_HEADER_ATTENTIONFANS = "/api/msg/list/fans";
        String REQUEST_HEADER_VIDEOLIKELIST = "/api/msg/list/zan";
        String REQUEST_HEADER_SWITCH_LIST = "/api/msg/push/switch/list";
        String REQUEST_HEADER_CHANGEPUSHMESSAGE = "/api/msg/push/switch";
        String REQUEST_HEADER_PUSHHISTORY = "/api/msg/push/history";
    }
}
