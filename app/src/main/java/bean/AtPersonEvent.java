package bean;

import event.MessageEvent;

/**
 * Created by maoqi on 2019/10/9.
 */
public class AtPersonEvent extends MessageEvent {
    private String uid;
    private String nickname;

    public AtPersonEvent(String uid, String nickname) {
        this.uid = uid;
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public String getUid() {
        return uid;
    }
}
