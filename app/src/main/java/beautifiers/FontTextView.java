package beautifiers;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by moshe on 01/05/2017.
 */

public class FontTextView extends androidx.appcompat.widget.AppCompatTextView {
    public FontTextView(Context context) {

        super(context);
        this.setTypeface(Typeface.createFromAsset(getResources().getAssets(),"fonts/LotteMedium.ttf"));
    }

    public FontTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(getResources().getAssets(),"fonts/LotteMedium.ttf"));

    }

    public FontTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setTypeface(Typeface.createFromAsset(getResources().getAssets(),"fonts/LotteMedium.ttf"));

    }

}
