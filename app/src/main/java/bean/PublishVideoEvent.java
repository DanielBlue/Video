package bean;

import event.MessageEvent;

/**
 * Created by maoqi on 2019/10/18.
 */
public class PublishVideoEvent extends MessageEvent {
    private String filePath;

    public PublishVideoEvent(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
