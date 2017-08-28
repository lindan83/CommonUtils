package com.lance.common.util.spannable;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public abstract class SpannableClickable extends ClickableSpan implements View.OnClickListener {
    /**
     * text颜色
     */
    private final int textColor;

    public SpannableClickable(int textColor) {
        this.textColor = textColor;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);

        ds.setColor(textColor);
        ds.setUnderlineText(false);
        ds.clearShadowLayer();
    }
}
