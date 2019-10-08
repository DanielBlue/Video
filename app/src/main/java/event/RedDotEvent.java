package event;

/**
 * Created by maoqi on 2019/10/8.
 */
public class RedDotEvent extends MessageEvent {
    private int pushType;

    public RedDotEvent(int pushType) {
        this.pushType = pushType;
    }

    public int getPushType() {
        return pushType;
    }
}
