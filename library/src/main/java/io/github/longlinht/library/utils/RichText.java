package io.github.longlinht.library.utils;

/**
 * Created by Tao He on 18-8-2.
 * hetaoof@gmail.com
 */

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;

import java.util.Arrays;

/**
 * 使用SpannableString 实现的富文本操作工具类
 *
 * Created by Tao He on 18-4-27.
 * hetaoof@gmail.com
 *
 */
public final class RichText {

    /**
     * 组合多种不同的效果
     */
    public static CharSequence compose(@NonNull CharSequence... fonts) {
        return compose(Arrays.asList(fonts));
    }

    /**
     * 组合多种不同的效果
     */
    public static CharSequence compose(Iterable<? extends CharSequence> fontIterable) {
        final SpannableStringBuilder builder = new SpannableStringBuilder();
        for (CharSequence font : fontIterable) {
            builder.append(SpannableString.valueOf(font));
        }

        return builder;
    }

    private final SpannableString mSpanText;

    private RichText(SpannableString mSpanText) {
        this.mSpanText = mSpanText;
    }

    /**
     * 装饰一段文本
     */
    public static RichText decorate(@NonNull CharSequence source) {
        return new RichText(SpannableString.valueOf(source));
    }

    /**
     * 粗体
     */
    public RichText bold() {
        applySpan(new StyleSpan(Typeface.BOLD));
        return this;
    }

    /**
     * 粗 斜体
     */
    public RichText boldItalic() {
        applySpan(new StyleSpan(Typeface.BOLD_ITALIC));
        return this;
    }

    /**
     * 斜体
     */
    public RichText italic() {
        applySpan(new StyleSpan(Typeface.ITALIC));
        return this;
    }

    /**
     * 设置颜色
     */
    public RichText color(int color) {
        applySpan(new ForegroundColorSpan(color));
        return this;
    }

    /**
     * 显示为图片
     */
    public RichText image(Drawable drawable) {
        applySpan(new ImageSpan(drawable));
        return this;
    }

    /**
     * 设置相对大小
     */
    public RichText relativeSize(float size) {
        applySpan(new RelativeSizeSpan(size));
        return this;
    }

    /**
     * 设置绝对大小
     *
     * @param sizeInPx 以像素为单位的大小
     */
    public RichText absoluteSize(int sizeInPx) {
        applySpan(new AbsoluteSizeSpan(sizeInPx));
        return this;
    }

    /**
     * 横穿样式
     */
    public RichText strikethrough() {
        applySpan(new StrikethroughSpan());
        return this;
    }

    /**
     * 下划线样式
     */
    public RichText underline() {
        applySpan(new UnderlineSpan());
        return this;
    }

    /**
     * 可点击的
     */
    public RichText clickableWithUnderline(final View.OnClickListener onClickListener, final int linkColor) {
        applySpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                onClickListener.onClick(widget);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(linkColor);
                ds.setUnderlineText(true);
            }
        });
        return this;
    }

    /**
     * 可点击的
     */
    public RichText clickable(final View.OnClickListener onClickListener) {
        applySpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                onClickListener.onClick(widget);
            }
        });
        return this;
    }

    public CharSequence asCharsequence() {
        return mSpanText;
    }

    public SpannableString asSpannableString() {
        return mSpanText;
    }

    public Spannable asSpannable() {
        return mSpanText;
    }

    private void applySpan(CharacterStyle style) {
        mSpanText.setSpan(style, 0, mSpanText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
}

