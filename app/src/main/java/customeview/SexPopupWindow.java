package customeview;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.am.shortVideo.R;

import okhttp3.Callback;

/**
 * Created by JC on 2019/9/6.
 */

public class SexPopupWindow extends PopupWindow {
    private SexCallBack callback;
    private Context context;
    private Button bt_man;
    private Button bt_woman;

    public SexPopupWindow(Context context) {
        super(context);
        this.context=context;
        initView();
    }

    private void initView() {
        View view= LayoutInflater.from(context).inflate(R.layout.sex_popupwindow,null,false);
        setContentView(view);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        setAnimationStyle(R.style.Mypopupwindow);
        setFocusable(true);
        showAtLocation(view, Gravity.BOTTOM,0,0);
       bt_man=(Button)view.findViewById(R.id.select_man);
        bt_woman=(Button)view.findViewById(R.id.select_woman);
        bt_man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onviewclick(R.id.select_man);
                dismiss();
            }
        });
        bt_woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onviewclick(R.id.select_woman);
                dismiss();
            }
        });

    }
    public void setOnClickLinstenr(SexCallBack callBack){
        this.callback=callBack;
    }
    public interface SexCallBack{
        void onviewclick(int id);
    }
}
