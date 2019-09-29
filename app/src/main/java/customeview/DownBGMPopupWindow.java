package customeview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.am.shortVideo.R;
import com.am.shortVideo.activity.LoginActivity;
import com.tiktokdemo.lky.tiktokdemo.Constant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import bean.SelectMusicBean;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.HttpUri;

/**
 * Created by 李杰 on 2019/9/13.
 */

public class DownBGMPopupWindow extends PopupWindow implements View.OnClickListener {
    private final OktHttpUtil okHttpUtil;
    private Context context;
    private Button bt_logincancel;
    private Button bt_loginconfirm;
    private SelectMusicBean.DataBean.IndexListBean value;
    private DownBGMIsSucess downBGMIsSucess;
    private TextView tv_content;
    private boolean isSuccess=false;
    public DownBGMPopupWindow(Context context, SelectMusicBean.DataBean.IndexListBean value) {
        super(context);
        this.context=context;
        this.value=value;
        okHttpUtil= OktHttpUtil.getInstance();
        initView();
    }
    private void initView() {
        setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
        setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        View view= LayoutInflater.from(context).inflate(R.layout.downbgm_popupwindow,null,false);
        setContentView(view);
        setAnimationStyle(R.style.Mypopupwindow);
        setOutsideTouchable(true);
        setFocusable(true);
        showAtLocation(view, Gravity.CENTER,0,0);
        tv_content=(TextView)view.findViewById(R.id.tv_content);
        bt_logincancel=(Button)view.findViewById(R.id.bt_logincancel);
        bt_loginconfirm=(Button)view.findViewById(R.id.bt_loginconfirm);
        bt_logincancel.setOnClickListener(this);
        bt_loginconfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_logincancel:
                downBGMIsSucess.downisSucess(false);
                dismiss();
                break;
            case R.id.bt_loginconfirm:
                if(isSuccess){
                    isSuccess=false;
                    dismiss();
                    return;
                }
                tv_content.setText("下载中...");
                bt_logincancel.setVisibility(View.GONE);
                bt_loginconfirm.setVisibility(View.GONE);
                okHttpUtil.downMusicFile(HttpUri.BASE_DOMAIN+value.getAudioUrl(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                isSuccess=false;
                                tv_content.setText("下载失败，请重试！！！");
                                bt_logincancel.setVisibility(View.VISIBLE);
                                bt_loginconfirm.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        writeFile(response);
                    }
                });
                break;
        }
    }
    private void writeFile(Response response) {
        InputStream is = null;
        FileOutputStream fos = null;
        is = response.body().byteStream();
        File file = new File(Constant.DOWNBGM, value.getName()+".mp3");
        try {
            fos = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            int len = 0;
            //获取下载的文件的大小
            long fileSize = response.body().contentLength();
            long sum = 0;
            int porSize = 0;
            while ((len = is.read(bytes)) != -1) {
                fos.write(bytes);
                sum += len;
                porSize = (int) ((sum * 1.0f / fileSize) * 100);
                Log.d("myTag", "writeFile: "+porSize);
                if(porSize==100){
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isSuccess=true;
                            tv_content.setText("下载完成！！！");
                            bt_loginconfirm.setVisibility(View.VISIBLE);
                            downBGMIsSucess.downisSucess(true);
                        }
                    });
                }
            }
        } catch (Exception e) {
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isSuccess=false;
                    tv_content.setText("下载失败，请重试！！！");
                    bt_logincancel.setVisibility(View.VISIBLE);
                    bt_loginconfirm.setVisibility(View.VISIBLE);
                }
            });
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.i("myTag", "下载成功");
    }

    public void setDownStatusLinstener(DownBGMIsSucess downBGMIsSucess){
        this.downBGMIsSucess=downBGMIsSucess;
    }
    public interface DownBGMIsSucess{
        void downisSucess(boolean isSucess);
    }
}
