package event;

/**
 * Created by maoqi on 2019/10/8.
 */
public class RedDotEvent extends MessageEvent {
    private int pushType;
    private boolean isShow;

    public boolean isShow() {
        return isShow;
    }

    public RedDotEvent(int pushType,boolean isShow) {
        this.pushType = pushType;
        this.isShow = isShow;
    }

    public int getPushType() {
        return pushType;
    }
}
