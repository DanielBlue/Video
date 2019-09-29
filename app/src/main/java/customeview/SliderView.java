package customeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.am.shortVideo.R;

/**
 * Created by JC on 2019/8/24.
 */

public class SliderView extends View {
    private Paint paint;
    private int choose=-1;
    private ChangeLetter changeLetter;
    private String[] b=new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O",
            "P","Q","R","S","T","U","V","W","X","Y","Z","#"};
    public SliderView(Context context) {
        this(context,null);
    }

    public SliderView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public SliderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        paint=new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
      int width=getWidth();
        int height=getHeight();
        int countheight=height/b.length;
        for(int i=0;i<b.length;i++){
            if(choose==i){
                paint.setTextSize(50f);
                paint.setTypeface(Typeface.DEFAULT_BOLD);
                paint.setColor(getResources().getColor(R.color.color_33));
                changeLetter.getchangeLetter(b[i]);
            }else{
                paint.setTextSize(50f);
                paint.setColor(getResources().getColor(R.color.color_68));
            }
            int xpos= (int) (width/2-paint.measureText(b[i])/2);
            canvas.drawText(b[i],xpos,i*countheight+countheight,paint);
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float touchY=event.getY();
        choose= (int) (touchY/(getHeight()/b.length));
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(choose>=0&&choose<b.length){
                    invalidate();
                }
        }
        return true;
    }

    public void setChangeLetterLinstener(ChangeLetter changeLetter){
        this.changeLetter=changeLetter;
    }
    public interface  ChangeLetter{
        void getchangeLetter(String letter);
    }
}
