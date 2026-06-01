package org.example.auramesh;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

public class GradientTextView extends AppCompatTextView {

    public GradientTextView(Context context) { super(context); }
    public GradientTextView(Context context, AttributeSet attrs) { super(context, attrs); }
    public GradientTextView(Context context, AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int startColor = ContextCompat.getColor(getContext(), R.color.gradient_start);
        int endColor = ContextCompat.getColor(getContext(), R.color.gradient_end);
        Shader shader = new LinearGradient(0, 0, w, 0,
                new int[]{startColor, endColor}, null, Shader.TileMode.CLAMP);
        getPaint().setShader(shader);
    }
}
