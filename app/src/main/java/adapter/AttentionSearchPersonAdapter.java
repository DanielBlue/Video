package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.am.shortVideo.R;

import java.util.List;

import base.MyAllBaseAdapter;
import base.MyBaseViewHolder;
import util.FootViewHolder;
import util.PinYinUtil;

/**
 * Created by JC on 2019/8/19.
 */

public class AttentionSearchPersonAdapter extends MyAllBaseAdapter<String, MyBaseViewHolder> {
    private Context context;
    private List<String> dataes;
    private PinYinUtil pinYinUtil;
    public AttentionSearchPersonAdapter(List<String> dates, Context context, PinYinUtil pinYinUtil) {
        super(dates, context);
        this.context=context;
        this.dataes=dates;
        this.pinYinUtil=pinYinUtil;
    }

    @Override
    public MyBaseViewHolder onAdapterCreaetViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType==0){
            View view= LayoutInflater.from(context).inflate(R.layout.attentionperson_item,null);
            return new AttentionPersonViewHolder(view);
        }else if(viewType==1){
            View view= LayoutInflater.from(context).inflate(R.layout.foot_item,null);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void getBindViewHolder(MyBaseViewHolder viewHolder, int position) {
           if(getItemViewType(position)==0){
               char letter=getfontLetter(position);
              AttentionPersonViewHolder attentionPersonViewHolder=(AttentionPersonViewHolder)viewHolder;
               if(isFirstShowLetter(letter)==position){
                   attentionPersonViewHolder.tv_tag.setVisibility(View.VISIBLE);
                    attentionPersonViewHolder.tv_tag.setText(""+letter);
               }else{
                   attentionPersonViewHolder.tv_tag.setVisibility(View.GONE);
               }
              attentionPersonViewHolder.textView.setText(dataes.get(position));
           } else if(getItemViewType(position)==1){
              FootViewHolder footViewhHolderHolder=(FootViewHolder)viewHolder;
           }
    }

    public class AttentionPersonViewHolder extends MyBaseViewHolder{
        private ImageView imageView;
        private TextView textView;
        private TextView tv_tag;

        public AttentionPersonViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            tv_tag=(TextView)itemView.findViewById(R.id.tv_tag);
            textView=(TextView)itemView.findViewById(R.id.tv_attenionperson_name);
        }

    }
    public char getfontLetter(int position){
       char letter=pinYinUtil.getPinYin(dataes.get(position)).charAt(0);
        return letter;
    }

    public int  isFirstShowLetter(char position){
        for(int i=0;i<getItemCount()-1;i++){
            String letter=pinYinUtil.getPinYin(dataes.get(i));
            if(letter.charAt(0)==(position)){
                return i;
            }
        }
        return 0;
    }
}
