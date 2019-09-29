package base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by JC on 2019/8/19.
 */

public abstract class MyAllBaseAdapter<K, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {
    private List<K> dates;
    private Context context;
    public static final int FOOT_VIEW = 1;
    public static final int NORMAL_VIEW = 0;

    public MyAllBaseAdapter(List<K> dates, Context context) {
        this.dates = dates;
        this.context = context;
    }

    @Override
    public V onCreateViewHolder(ViewGroup viewGroup, int i) {
        return onAdapterCreaetViewHolder(viewGroup, i);
    }

    @Override
    public void onBindViewHolder(V v, int i) {
        getBindViewHolder(v, i);
    }

    @Override
    public int getItemCount() {
        return dates.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return FOOT_VIEW;
        }
        return NORMAL_VIEW;
    }

    public abstract V onAdapterCreaetViewHolder(ViewGroup viewGroup, int viewType);

    public abstract void getBindViewHolder(V viewHolder, int position);
}
