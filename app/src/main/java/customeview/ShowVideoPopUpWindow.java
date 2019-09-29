package customeview;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.am.shortVideo.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.AtUserNickAdapter;
import adapter.CommentAdapter;
import application.MyApplication;
import bean.AttentionPerson;
import bean.AttentionPersonVideo;
import bean.PublishComment;
import bean.SerachPublishVideo;
import bean.UserInfo;
import bean.VideoComment;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.HttpUri;
import util.RecycleViewDivider;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by JC on 2019/9/18.
 */

public class ShowVideoPopUpWindow extends PopupWindow implements View.OnClickListener, AtUserNickAdapter.AtNickCallBack
        , CommentAdapter.AuthorityReplayCallBack {
    Context context;
    private static final String TAG = "ShowVideoPopUpWindow";
    private View view;
    private RecyclerView comment_recycleview;
    private EditText ed_comment;
    private boolean isopen = false;
    private CommentAdapter commentAdapter;
    private Button bt_hidepop;
    private Button bt_sendcomment;
    private List<VideoComment.DataBean.CommentListBean> comentdatas;
    private TextView allCommentcount;
    private List<AttentionPersonVideo.DataBean.IndexListBean> homevideodatas;
    private OktHttpUtil oktHttpUtil;
    private int curposition;
    private Button bt_at;
    private List<AttentionPerson.DataBean.PageListBean> newCommentDatas = new ArrayList<>();
    private RecyclerView comment_recycleview1;
    private boolean isAtClick = false;
    private String At_id;
    private int authorytReplayPosition;
    private int currentStatus = 0;//0代表默认评论 //1代表@ //2代表作者回复
    private boolean isAuthority = false;

    public ShowVideoPopUpWindow(Context context, List<AttentionPersonVideo.DataBean.IndexListBean> homevideodatas, int curposition) {
        super(context);
        this.context = context;
        this.homevideodatas = homevideodatas;
        this.curposition = curposition;
        oktHttpUtil = ((MyApplication) context.getApplicationContext()).getOkHttpUtil();
        initView();
    }

    private void initView() {
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        view = LayoutInflater.from(context).inflate(R.layout.comment_popupwindow, null, false);
        setContentView(view);
        setFocusable(true);
        setAnimationStyle(R.style.Mypopupwindow);
        setOutsideTouchable(true);
        showAtLocation(view, Gravity.BOTTOM, 0, 0);
        comment_recycleview = (RecyclerView) view.findViewById(R.id.comment_recyleview);
        comment_recycleview1 = (RecyclerView) view.findViewById(R.id.comment_recyleview1);
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
                            oktHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.PersonInfo.REQUEST_HEADER_PERSONINFO, ((MyApplication) context.getApplicationContext()).getMaps(), new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    e.printStackTrace();
                                    Log.e(TAG, "onFailure: ");
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String userinfoResult = response.body().string();
                                    Log.d(TAG, "onResponse: userinfoCallback\n" + userinfoResult);
                                    Gson gson = new Gson();
                                    UserInfo userinfo = gson.fromJson(userinfoResult, UserInfo.class);
                                    if (userinfo.getCode() == 0 && userinfo.getData() != null) {
                                        if (userinfo.getData().getUserInfo().getUid().equals(homevideodatas.get(curposition).getUid())) {
                                            isAuthority = true;
                                            ((Activity) context).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    allCommentcount.setText(context.getResources().getString(R.string.tv_allcomment) + "(" + homevideodatas.get(curposition).getCommentCounts() + ")");
                                                    if (comentdatas != null) {
                                                        commentAdapter = new CommentAdapter(comentdatas, context, isAuthority);
                                                        commentAdapter.setOnAuthorityReplayLinstener(ShowVideoPopUpWindow.this);
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
                                                        commentAdapter.setOnAuthorityReplayLinstener(ShowVideoPopUpWindow.this);
                                                        comment_recycleview.setAdapter(commentAdapter);
                                                    }
                                                }
                                            });
                                        }
                                    } else if (userinfo.getCode() == 1005) {
                                        ((Activity) context).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                new LoginPopupwindow(context);
                                            }
                                        });
                                    }
                                }
                            });


                        } else if (videoComment.getCode() == 1005) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new LoginPopupwindow(context);
                                }
                            });
                        }

                    }
                });

    }

    private List<AttentionPerson.DataBean.PageListBean> loadAtData() {
        oktHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.PersonInfo.REQUEST_HEADER_ATTENTIONPERSONQUERY,
                ((MyApplication) context.getApplicationContext()).getMaps(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String attentionperson = response.body().string();
                        Log.d(TAG, "attentionlist-->onResponse: \n" + attentionperson);
                        Gson gson = new Gson();
                        final AttentionPerson attentionPersonVideo = gson.fromJson(attentionperson, AttentionPerson.class);
                        if (attentionPersonVideo.getData() != null && attentionPersonVideo.getData().getPageList() != null) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    newCommentDatas = attentionPersonVideo.getData().getPageList();
                                    AtUserNickAdapter atUsernickAdatpter = new AtUserNickAdapter(newCommentDatas, context);
                                    comment_recycleview1.setAdapter(atUsernickAdatpter);
                                    atUsernickAdatpter.setOnNickItemLinstener(ShowVideoPopUpWindow.this);
                                }
                            });

                        }
                    }
                });

        return newCommentDatas;

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
                    currentStatus = 0;
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
                    currentStatus = 0;
                    String commentcontents = ed_comment.getText().toString().trim();
                    String contents = commentcontents.substring(commentcontents.indexOf(":") + 1, commentcontents.length());
                    if (contents.isEmpty()) {
                        Toast.makeText(context, "输入内容不能为空", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    maps.put("cid", "" + comentdatas.get(curposition).getId()); //;//"5995716959957168"
                    maps.put("content", contents);
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
                                if (publishComment.getCode() == 0 && publishComment.getMessage().contains("成功评论")) {
                                    Log.d(TAG, "onResponse-->comment:sucess ");
//                                    VideoComment.DataBean.CommentListBean  commentListBean=new VideoComment.DataBean.CommentListBean();
//                                    //commentListBean.setAvatar(publishComment.getData().getComment().getContent());
//                                    commentListBean.setContent(publishComment.getData().getComment().getContent());
//                                    commentListBean.setCommentTime(publishComment.getData().getComment().getCreateTime());
//                                   comentdatas.add(commentListBean);
//                                    ((Activity)context).runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            ed_comment.setText("");
//                                            commentAdapter.notifyDataSetChanged();
//                                            comment_recycleview.scrollToPosition(comentdatas.size()-1);
//                                        }
//                                    });
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
                                                        ((Activity) context).runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                ed_comment.setText("");
                                                                commentAdapter = new CommentAdapter(comentdatas, context, isAuthority);
                                                                commentAdapter.setOnAuthorityReplayLinstener(ShowVideoPopUpWindow.this);
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
                                            new LoginPopupwindow(context);
                                        }
                                    });
                                } else if (publishComment.getCode() == 400) {
                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dismiss();
                                            Toast.makeText(context, "发送失败", Toast.LENGTH_SHORT).show();
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
                if (!isAtClick) {
                    isAtClick = true;
                    if (!comentdatas.isEmpty()) {
                        comment_recycleview1.setVisibility(View.VISIBLE);
                        loadAtData();

                    }
                } else {
                    isAtClick = false;
                    comment_recycleview1.setVisibility(View.GONE);
                }
                break;
            default:
        }
    }

    @Override
    public void getAtName(String nicknama, String id) {
        currentStatus = 1;
        isAtClick = false;
        Log.d(TAG, "nicknama: " + nicknama + "id\t" + id);
        At_id = id;
        ed_comment.setText("@" + nicknama + ":");
        ed_comment.setSelection(ed_comment.getText().length());
        openKeybord(ed_comment, context);
        comment_recycleview1.setVisibility(View.GONE);

    }

    @Override
    public void authorityReplayData(int position) {
        currentStatus = 2;
        isAtClick = false;
        authorytReplayPosition = position;
        ed_comment.setText("回复" + comentdatas.get(position).getNickName() + ":");
        ed_comment.setSelection(ed_comment.getText().length());
        openKeybord(ed_comment, context);
    }
}
