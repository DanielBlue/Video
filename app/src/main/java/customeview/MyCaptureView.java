package customeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;

import android.widget.LinearLayout;
import com.am.shortVideo.R;

/**
 * Created by JC on 2019/8/27.
 */

public class MyCaptureView extends LinearLayout {
    private Paint paint;
    private Path path;
    private static final String TAG = "MyCaptureView";
    public MyCaptureView(Context context) {
        this(context,null);
    }

    public MyCaptureView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyCaptureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        paint=new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3);
        paint.setColor(getResources().getColor(R.color.colorWhite));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw: ");
        path=new Path();
        path.quadTo(45,-48,80,0);
        canvas.drawPath(path,paint);
    }
}
