package customeview;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.am.shortVideo.R;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by JC on 2019/9/7.
 */

public class EditInfoPopupwindow extends PopupWindow implements View.OnClickListener{
    private Context context;
    private int editType;
    private EditText editorinfo;
    private Button bt_editorconfirm;
    private Button bt_editorcancel;
    private EditTextInfo editTextinfo;
    public EditInfoPopupwindow(Context context,int editType) {
        super(context);
        this.editType=editType;
        this.context=context;
        initView();
    }

    private void initView() {
        setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
        setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        View view= LayoutInflater.from(context).inflate(R.layout.editinfo_popupwindow,null,false);
        setContentView(view);
        setAnimationStyle(R.style.Mypopupwindow);
        setOutsideTouchable(true);
         setFocusable(true);
          showAtLocation(view, Gravity.CENTER,0,0);
       editorinfo=(EditText)view.findViewById(R.id.et_editinfo);
        bt_editorconfirm=(Button)view.findViewById(R.id.bt_editorconfirm);
        bt_editorcancel=(Button)view.findViewById(R.id.bt_editorcancel);
        editorinfo.setOnClickListener(this);
        bt_editorcancel.setOnClickListener(this);
        bt_editorconfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.et_editinfo:
                openKeybord(editorinfo,context);
                break;
            case R.id.bt_editorcancel:
                closeKeybord(editorinfo,context);
                dismiss();
                break;
            case R.id.bt_editorconfirm:
                closeKeybord(editorinfo,context);
                String name=editorinfo.getText().toString().trim();
                editTextinfo.getTextString(v,editType,name);
                dismiss();
                break;
            default:

        }

    }
    public void setOnTextStringLinstener(EditTextInfo editTextinfo){
            this.editTextinfo=editTextinfo;
    }
    public interface EditTextInfo{
        void getTextString(View v,int editType,String info);
    }
    //打开软键盘
    public  void openKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);

    }

    //关闭软键盘
    public static void closeKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }
}
