package base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 李杰 on 2019/8/5.
 */

public abstract class MyBaseAdapter<T, K extends MyBaseViewHolder> extends RecyclerView.Adapter<K> {
    private static final String TAG = "MyBaseAdapter";
    private List<T> datas;
    private Context context;
    public static final int FOOT_VIEW = 1;
    public static final int NORMAL_VIEW = 0;

    public MyBaseAdapter(List<T> datas, Context context) {
        Log.d(TAG, "MyBaseAdapter: ");
        this.datas = datas;
        this.context = context;
    }

    @Override
    public K onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        K holdler = onCreateHolder(parent, viewType);
        return holdler;
    }

    @Override
    public void onBindViewHolder(K holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + position);
//        if(position>2){
//            onHolder(holder,datas.get(position-1),position-1);
//        }else{
        onHolder(holder, position);
        //}

    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return FOOT_VIEW;
        }
        return NORMAL_VIEW;
    }

    @Override
    public int getItemCount() {
        return datas.size() + 1;
    }

    public abstract K onCreateHolder(ViewGroup parent, int viewType);

    public abstract void onHolder(K holder, int position);


}
