package customeview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.am.shortVideo.R;
import com.google.gson.Gson;
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
 * Created by maoqi on 2019/10/30.
 */
public class DownBgmDialog extends DialogFragment implements View.OnClickListener {
    private Window mWindow;
    private Context mContext;
    private SelectMusicBean.DataBean.IndexListBean mDataBean;
    private TextView tv_content;
    private Button bt_logincancel;
    private Button bt_loginconfirm;
    private boolean isSuccess = false;

    public static DownBgmDialog newInstance(SelectMusicBean.DataBean.IndexListBean value) {
        Bundle args = new Bundle();
        DownBgmDialog fragment = new DownBgmDialog();
        args.putString("value", new Gson().toJson(value));
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        
        View rootView = inflater.inflate(R.layout.downbgm_popupwindow, null);
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(rootView);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        mWindow = dialog.getWindow();
        mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mWindow.setWindowAnimations(R.style.comment_detail_anim);
        mWindow.setGravity(Gravity.BOTTOM);
        mContext = getActivity();
        String value = getArguments().getString("value");
        mDataBean = new Gson().fromJson(value, SelectMusicBean.DataBean.IndexListBean.class);
        initView(rootView);
        return dialog;
    }

    private void initView(View rootView) {
        tv_content = (TextView) rootView.findViewById(R.id.tv_content);
        bt_logincancel = (Button) rootView.findViewById(R.id.bt_logincancel);
        bt_loginconfirm = (Button) rootView.findViewById(R.id.bt_loginconfirm);
        bt_logincancel.setOnClickListener(this);
        bt_loginconfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_logincancel:
                downBGMIsSucess.downisSucess(false);
                dismiss();
                break;
            case R.id.bt_loginconfirm:
                if (isSuccess) {
                    isSuccess = false;
                    dismiss();
                    return;
                }
                tv_content.setText("下载中...");
                bt_logincancel.setVisibility(View.GONE);
                bt_loginconfirm.setVisibility(View.GONE);
                OktHttpUtil.getInstance().downMusicFile(HttpUri.BASE_DOMAIN + mDataBean.getAudioUrl(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                isSuccess = false;
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
        File file = new File(Constant.DOWNBGM, mDataBean.getName() + ".mp3");
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
                Log.d("myTag", "writeFile: " + porSize);
                if (porSize == 100) {
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isSuccess = true;
                            tv_content.setText("下载完成！！！");
                            bt_loginconfirm.setVisibility(View.VISIBLE);
                            downBGMIsSucess.downisSucess(true);
                        }
                    });
                }
            }
        } catch (Exception e) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isSuccess = false;
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

    private DownBGMIsSucess downBGMIsSucess;

    public void setDownStatusLinstener(DownBGMIsSucess downBGMIsSucess) {
        this.downBGMIsSucess = downBGMIsSucess;
    }

    public interface DownBGMIsSucess {
        void downisSucess(boolean isSucess);
    }
}
