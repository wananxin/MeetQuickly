package com.andy.meetquickly.view.indexbar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

import com.andy.meetquickly.R;

/**
 * @Author: created by Andy
 * @CreateDate: 2019/5/15 14:53
 * 描述：
 */
public class PinnedSectionDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "PinnedSectionDecoration";

    private DecorationCallback callback;
    private TextPaint textPaint;
    private Paint paint;
    private int topGap;
    private Paint.FontMetrics fontMetrics;

    private int mTextHeight;
    private int mTextBaselineOffset;
    private int mTextStartMargin;


    public PinnedSectionDecoration(Context context, DecorationCallback decorationCallback) {
        Resources res = context.getResources();
        this.callback = decorationCallback;

        paint = new Paint();
        paint.setColor(res.getColor(R.color.item_decoration_title_background));

        textPaint = new TextPaint();
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.item_decoration_title_fontsize));
        textPaint.setColor(ContextCompat.getColor(context, R.color.item_decoration_title_fontcolor));
        textPaint.getFontMetrics(fontMetrics);
        textPaint.setTextAlign(Paint.Align.LEFT);
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        mTextHeight = (int) (fm.bottom - fm.top);
        mTextBaselineOffset = (int) fm.bottom;
        mTextStartMargin = context.getResources().getDimensionPixelOffset(R.dimen.item_decoration_title_start_margin);
        fontMetrics = new Paint.FontMetrics();
        topGap = res.getDimensionPixelSize(R.dimen.sectioned_top);


    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        long groupId = callback.getGroupId(pos);
        if (groupId < 0) return;
        if (pos == 0 || isFirstInGroup(pos)) {
            outRect.top = topGap;
        } else {
            outRect.top = 0;
        }
    }


    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int itemCount = state.getItemCount();
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        float lineHeight = textPaint.getTextSize() + fontMetrics.descent;

        long preGroupId, groupId = -1;
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);

            preGroupId = groupId;
            groupId = callback.getGroupId(position);
            if (groupId < 0 || groupId == preGroupId) continue;

            String textLine = callback.getGroupFirstLine(position).toUpperCase();
            if (TextUtils.isEmpty(textLine)) continue;

            int viewBottom = view.getBottom();
            float textY = Math.max(topGap, view.getTop());
            if (position + 1 < itemCount) { //下一个和当前不一样移动当前
                long nextGroupId = callback.getGroupId(position + 1);
                if (nextGroupId != groupId && viewBottom < textY ) {//组内最后一个view进入了header
                    textY = viewBottom;
                }
            }
            c.drawRect(left, textY - topGap, right, textY, paint);
            c.drawText(textLine, left, textY, textPaint);
        }

    }

    private boolean isFirstInGroup(int pos) {
        if (pos == 0) {
            return true;
        } else {
            long prevGroupId = callback.getGroupId(pos - 1);
            long groupId = callback.getGroupId(pos);
            return prevGroupId != groupId;
        }
    }

    public interface DecorationCallback {

        long getGroupId(int position);

        String getGroupFirstLine(int position);
    }

}