package com.am.shortVideo.EventBean;

import event.MessageEvent;

public class AttentionEvent extends MessageEvent {
    public String uid;
    public boolean isAttent;

    public AttentionEvent() {
    }

    public AttentionEvent(String uid, boolean isAttent) {
        this.uid = uid;
        this.isAttent = isAttent;
    }
}
