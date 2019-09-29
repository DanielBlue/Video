package customeview;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.am.shortVideo.R;

/**
 * Created by JC on 2019/8/1.
 */

public class MyPopUpWindow extends PopupWindow implements View.OnClickListener {
    private static final String TAG = "MyPopUpWindow";
    private PopupCallback popupCallback;
    private View view;
    private Context context;
    private Button bt_capture;
    private Button bt_album;
    private Button bt_cancel;
    public MyPopUpWindow(Context context) {
        super(context);
        this.context=context;
        initView();
    }

    private void initView() {
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        view= LayoutInflater.from(context).inflate(R.layout.popupwindow_layout,null,false);
        setContentView(view);
        setFocusable(true);
//        setAnimationStyle(R.style.Mypopupwindow);
        setOutsideTouchable(true);
        showAtLocation(view, Gravity.BOTTOM,0,0);
         bt_capture=(Button)view.findViewById(R.id.bt_capture);
         bt_album=(Button)view.findViewById(R.id.bt_album);
         bt_cancel=(Button)view.findViewById(R.id.bt_cancel);
            bt_capture.setOnClickListener(this);
            bt_album.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_capture:
                dismiss();
                popupCallback.popOnClick(R.id.bt_capture);
                break;
            case R.id.bt_album:
                dismiss();
                popupCallback.popOnClick(R.id.bt_album);
                break;
            case R.id.bt_cancel:
                dismiss();
                popupCallback.popOnClick(R.id.bt_cancel);
                break;
            default:
                Log.d(TAG, "onClick: error");
        }
    }
    public void setPopOnClickLinstener(PopupCallback popupCallback){
        this.popupCallback=popupCallback;
    }
    public interface PopupCallback{
        void popOnClick(int id);
    }
}
