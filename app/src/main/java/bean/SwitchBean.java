package bean;

import java.util.List;

public class SwitchBean {
    public int code;
    public String message;
    public DataBean data;
    public String time;

    public class DataBean {
        public SwitchInfo push_list;

        public class SwitchInfo {
            public String uid;
            public boolean zan;
            public boolean follow;
            public boolean comment;

        }
    }
}
