package customeview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.am.shortVideo.EventBean.CommentCountEvent;
import com.am.shortVideo.R;
import com.am.shortVideo.activity.AtPersonActivity;
import com.am.shortVideo.activity.LoginActivity;
import com.am.shortVideo.view.BubbleLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.CommentAdapter;
import application.MyApplication;
import bean.AtPersonEvent;
import bean.AttentionPerson;
import bean.IndexListBean;
import bean.PublishComment;
import bean.UserInfoBean;
import bean.VideoComment;
import event.MessageEvent;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.BaseUtils;
import util.HttpUri;
import util.RecycleViewDivider;
import util.SizeUtils;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by JC on 2019/8/19.
 */

public class CommentDialog extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "CommentPopupWindow";
    Context context;
    private RecyclerView comment_recycleview;
    private EditText ed_comment;
    private boolean isopen = false;
    private CommentAdapter commentAdapter;
    private Button bt_hidepop;
    private Button bt_sendcomment;
    private List<VideoComment.DataBean.CommentListBean> comentdatas;
    private TextView allCommentcount;
    private List<IndexListBean> homevideodatas;
    private OktHttpUtil oktHttpUtil;
    private int curposition;
    private Button bt_at;
    private List<AttentionPerson.DataBean.PageListBean> newCommentDatas = new ArrayList<>();
    private RecyclerView comment_recycleview1;
    private BubbleLayout bl_at;
    private boolean isAtClick = false;
    private String At_id;
    private int authorytReplayPosition;
    private int currentStatus = 0;//0代表默认评论 //1代表@ //2代表作者回复
    private boolean isAuthority = false;
    private Window mWindow;

    public static CommentDialog newInstance(List<IndexListBean> homevideodatas, int curposition) {
        Bundle args = new Bundle();
        CommentDialog fragment = new CommentDialog();
        args.putString("homevideodatas", new Gson().toJson(homevideodatas));
        args.putInt("curposition", curposition);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.comment_popupwindow, null);
        EventBus.getDefault().register(this);
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(rootView);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        mWindow = dialog.getWindow();
        mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mWindow.setWindowAnimations(R.style.comment_detail_anim);
        mWindow.setGravity(Gravity.BOTTOM);
        context = getActivity();
        this.homevideodatas = new Gson().fromJson(getArguments().getString("homevideodatas"), new TypeToken<List<IndexListBean>>() {
        }.getType());
        this.curposition = getArguments().getInt("curposition");
        initView(rootView);
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        closeKeybord(ed_comment,getActivity());
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(400));
    }

    private void initView(View view) {
        oktHttpUtil = MyApplication.getOkHttpUtil();
        comment_recycleview = (RecyclerView) view.findViewById(R.id.comment_recyleview);
        comment_recycleview1 = (RecyclerView) view.findViewById(R.id.comment_recyleview1);
        bl_at = (BubbleLayout) view.findViewById(R.id.bl_at);
        ed_comment = (EditText) view.findViewById(R.id.et_comment);
        bt_hidepop = (Button) view.findViewById(R.id.bt_commentcancel);
        allCommentcount = (TextView) view.findViewById(R.id.tv_allcommentaccount);
        bt_sendcomment = (Button) view.findViewById(R.id.bt_sendcomment);
        bt_at = (Button) view.findViewById(R.id.bt_at);

        LinearLayoutManager linerlayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        comment_recycleview.setLayoutManager(linerlayoutManager);
        LinearLayoutManager linerlayoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        comment_recycleview1.setLayoutManager(linerlayoutManager1);
        comment_recycleview.addItemDecoration(new RecycleViewDivider(context, LinearLayout.VERTICAL, 2, 0, 0, context.getResources().getColor(R.color.colorDarkGray)));

        bt_sendcomment.setOnClickListener(this);
        ed_comment.setOnClickListener(this);
        bt_hidepop.setOnClickListener(this);
        bt_at.setOnClickListener(this);

        HashMap<String, String> commentmaps = new HashMap<>();
        commentmaps.put("vid", homevideodatas.get(curposition).getVid());// "5995716959957168"
        oktHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_VIDEOCOMMENT,
                ((MyApplication) context.getApplicationContext()).getMaps(), commentmaps, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "usercommentonFailure: ");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        Log.d(TAG, "usercommentonResponse: \n" + result);
                        Gson gson = new Gson();
                        VideoComment videoComment = gson.fromJson(result, VideoComment.class);
                        if (videoComment.getCode() == 0) {
                            comentdatas = videoComment.getData().getCommentList();
                            UserInfoBean userInfo = MyApplication.getInstance().getUserInfo();
                            if (userInfo != null && userInfo.getUid().equals(homevideodatas.get(curposition).getUid())) {
                                isAuthority = true;
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        allCommentcount.setText(context.getResources().getString(R.string.tv_allcomment) + homevideodatas.get(curposition).getCommentCounts());
                                        if (comentdatas != null) {
                                            commentAdapter = new CommentAdapter(comentdatas, context, isAuthority);
                                            commentAdapter.setOnAuthorityReplayLinstener(new CommentAdapter.AuthorityReplayCallBack() {
                                                @Override
                                                public void authorityReplayData(int position) {
                                                    currentStatus = 2;
                                                    isAtClick = false;
                                                    authorytReplayPosition = position;
                                                    ed_comment.setText("回复" + comentdatas.get(position).getNickName() + ":");
                                                    ed_comment.setSelection(ed_comment.getText().length());
                                                    openKeybord(ed_comment, context);
                                                }
                                            });
                                            comment_recycleview.setAdapter(commentAdapter);
                                        }
                                    }
                                });
                            } else {
                                isAuthority = false;
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        allCommentcount.setText(context.getResources().getString(R.string.tv_allcomment) + "(" + homevideodatas.get(curposition).getCommentCounts() + ")");
                                        if (comentdatas != null) {
                                            commentAdapter = new CommentAdapter(comentdatas, context, isAuthority);
                                            commentAdapter.setOnAuthorityReplayLinstener(new CommentAdapter.AuthorityReplayCallBack() {
                                                @Override
                                                public void authorityReplayData(int position) {
                                                    currentStatus = 2;
                                                    isAtClick = false;
                                                    authorytReplayPosition = position;
                                                    ed_comment.setText("回复" + comentdatas.get(position).getNickName() + ":");
                                                    ed_comment.setSelection(ed_comment.getText().length());
                                                    openKeybord(ed_comment, context);
                                                }
                                            });
                                            comment_recycleview.setAdapter(commentAdapter);
                                        }
                                    }
                                });
                            }

//                            oktHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.PersonInfo.REQUEST_HEADER_PERSONINFO, ((MyApplication) context.getApplicationContext()).getMaps(), new Callback() {
//                                @Override
//                                public void onFailure(Call call, IOException e) {
//                                    e.printStackTrace();
//                                    Log.e(TAG, "onFailure: ");
//                                }
//
//                                @Override
//                                public void onResponse(Call call, Response response) throws IOException {
//                                    String userinfoResult = response.body().string();
//                                    Log.d(TAG, "onResponse: userinfoCallback\n" + userinfoResult);
//                                    Gson gson = new Gson();
//                                    UserInfo userinfo = gson.fromJson(userinfoResult, UserInfo.class);
//
//
//                                    if (userinfo.getCode() == 0 && userinfo.getData() != null) {
//
//                                    } else if (userinfo.getCode() == 1005) {
//                                        ((Activity) context).runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                BaseUtils.getLoginDialog(context).show();
//                                            }
//                                        });
//                                    }
//                                }
//                            });


                        } else if (videoComment.getCode() == 1005) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    BaseUtils.getLoginDialog(context).show();
                                }
                            });
                        }

                    }
                });

    }

    //打开软键盘
    public void openKeybord(EditText mEditText, Context mContext) {
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

    @Override
    public void onClick(View v) {
        if (MyApplication.getInstance().getUserInfo() != null) {
            switch (v.getId()) {
                case R.id.bt_sendcomment:
                    closeKeybord(ed_comment, context);
                    //String addData=ed_comment.getText().toString();
                    //comentdatas.add(addData);
                    //commentAdapter.notifyDataSetChanged();
                    String url = "";
                    HashMap<String, String> maps = new HashMap<>();
                    if (currentStatus == 0) {
                        String contents = ed_comment.getText().toString().trim();
                        if (contents.isEmpty()) {
                            Toast.makeText(context, "输入内容不能为空", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        maps.put("vid", homevideodatas.get(curposition).getVid()); //;//"5995716959957168"
                        maps.put("content", contents);
                        maps.put("at_uid", "null");
                        url = HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_PUBLISHCOMMENT;
                    } else if (currentStatus == 1) {
                        String commentcontents = ed_comment.getText().toString().trim();
                        String contents = commentcontents.substring(commentcontents.indexOf(":") + 1, commentcontents.length());
                        if (contents.isEmpty()) {
                            Toast.makeText(context, "输入内容不能为空", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        maps.put("vid", homevideodatas.get(curposition).getVid()); //;//"5995716959957168"
                        maps.put("content", contents);
                        maps.put("at_uid", At_id);
                        url = HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_PUBLISHCOMMENT;
                    } else if (currentStatus == 2) {
                        String commentcontents = ed_comment.getText().toString().trim();
                        String contents = commentcontents.substring(commentcontents.indexOf(":") + 1, commentcontents.length());
                        if (contents.isEmpty()) {
                            Toast.makeText(context, "输入内容不能为空", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        maps.put("cid", "" + comentdatas.get(curposition).getId()); //;//"5995716959957168"
                        maps.put("reply_content", contents);
                        //maps.put("at_uid","");
                        url = HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_REPLYCOMMENT;
                    }


                    oktHttpUtil.setPostRequest(url,
                            ((MyApplication) context.getApplicationContext()).getMaps(), maps, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String commentResult = response.body().string();
                                    Log.d(TAG, "onResponse: \n" + commentResult);
                                    Gson gson = new Gson();
                                    PublishComment publishComment = gson.fromJson(commentResult, PublishComment.class);
                                    if (publishComment.getCode() == 0) {
                                        currentStatus = 0;
                                        final HashMap<String, String> commentmaps = new HashMap<>();
                                        commentmaps.put("vid", homevideodatas.get(curposition).getVid());//"5995716959957168"
                                        oktHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_VIDEOCOMMENT,
                                                ((MyApplication) context.getApplicationContext()).getMaps(), commentmaps, new Callback() {
                                                    @Override
                                                    public void onFailure(Call call, IOException e) {

                                                    }

                                                    @Override
                                                    public void onResponse(Call call, Response response) throws IOException {
                                                        String result = response.body().string();
                                                        Log.d(TAG, "usercommentonResponse: \n" + result);
                                                        Gson gson = new Gson();
                                                        final VideoComment videoComment = gson.fromJson(result, VideoComment.class);
                                                        if (videoComment.getCode() == 0) {
                                                            if (!comentdatas.isEmpty()) {
                                                                comentdatas.clear();
                                                            }
                                                            comentdatas = videoComment.getData().getCommentList();
                                                            CommentCountEvent commentCountEvent = new CommentCountEvent();
                                                            commentCountEvent.count = comentdatas.size();
                                                            commentCountEvent.vid = homevideodatas.get(curposition).getVid();
                                                            EventBus.getDefault().post(commentCountEvent);
                                                            ((Activity) context).runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    allCommentcount.setText(context.getResources().getString(R.string.tv_allcomment) + "(" + comentdatas.size() + ")");
                                                                    ed_comment.setText("");
                                                                    commentAdapter = new CommentAdapter(comentdatas, context, isAuthority);
                                                                    commentAdapter.setOnAuthorityReplayLinstener(new CommentAdapter.AuthorityReplayCallBack() {
                                                                        @Override
                                                                        public void authorityReplayData(int position) {
                                                                            currentStatus = 2;
                                                                            isAtClick = false;
                                                                            authorytReplayPosition = position;
                                                                            ed_comment.setText("回复" + comentdatas.get(position).getNickName() + ":");
                                                                            ed_comment.setSelection(ed_comment.getText().length());
                                                                            openKeybord(ed_comment, context);
                                                                        }
                                                                    });
                                                                    comment_recycleview.setAdapter(commentAdapter);
                                                                }
                                                            });

                                                        }
                                                    }
                                                });

                                    } else if (publishComment.getCode() == 1005) {
                                        ((Activity) context).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                dismiss();
                                                BaseUtils.getLoginDialog(context).show();
                                            }
                                        });
                                    }
                                }
                            });
                    break;
                case R.id.et_comment:
                    openKeybord(ed_comment, context);

                    break;
                case R.id.bt_commentcancel:
                    dismiss();
                    break;
                case R.id.bt_at:
                    AtPersonActivity.start(getActivity());
                    break;
                default:
            }
        } else {
            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
        }
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent event) {
        if (event != null) {
            if (event instanceof AtPersonEvent) {
                String nickname = ((AtPersonEvent) event).getNickname();
                String uid = ((AtPersonEvent) event).getUid();
                currentStatus = 1;
                isAtClick = false;
                At_id = uid;
                ed_comment.setText("@" + nickname + ":");
                ed_comment.setSelection(ed_comment.getText().length());
                openKeybord(ed_comment, context);
            }
        }
    }
}
