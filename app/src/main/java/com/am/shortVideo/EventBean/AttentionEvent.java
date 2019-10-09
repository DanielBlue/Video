package com.am.shortVideo.EventBean;

public class AttentionEvent {
    public String uid;
    public boolean isAttent;

    public AttentionEvent() {
    }

    public AttentionEvent(String uid, boolean isAttent) {
        this.uid = uid;
        this.isAttent = isAttent;
    }
}
