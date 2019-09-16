/**
 * Copyright 2017 ChenHao Dendi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.andy.meetquickly.view.indexbar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.andy.meetquickly.R;

import java.util.Map;

public class FloatingBarItemDecoration extends RecyclerView.ItemDecoration {

    private final String TAG = FloatingBarItemDecoration.class.getSimpleName();
    private DecorationCallback callback;
    private Context mContext;
    private int mTitleHeight;
    private Paint mBackgroundPaint;
    private Paint mTextPaint;
    private int mTextHeight;
    private int mTextBaselineOffset;
    private int mTextStartMargin;
    /**
     * Integer means the related position of the Recyclerview#getViewAdapterPosition()
     * (the position of the view in original adapter's list)
     * String means the title to be drawn
     */
//    private Map<Integer, String> mList;

    public FloatingBarItemDecoration(Context context, DecorationCallback decorationCallback) {
        this.mContext = context;
        Resources resources = mContext.getResources();
        this.callback = decorationCallback;
//        this.mList = list;
        this.mTitleHeight = resources.getDimensionPixelSize(R.dimen.item_decoration_title_height);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(ContextCompat.getColor(mContext, R.color.item_decoration_title_background));

        mTextPaint = new Paint();
        mTextPaint.setColor(ContextCompat.getColor(mContext, R.color.item_decoration_title_fontcolor));
        mTextPaint.setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.item_decoration_title_fontsize));

        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        mTextHeight = (int) (fm.bottom - fm.top);
        mTextBaselineOffset = (int) fm.bottom;
        mTextStartMargin = resources.getDimensionPixelOffset(R.dimen.item_decoration_title_start_margin);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
//        outRect.set(0, mList.containsKey(position) ? mTitleHeight : 0, 0, 0);
        int pos = parent.getChildAdapterPosition(view);
        long groupId = callback.getGroupId(pos);
        if (groupId < 0) return;
        if (pos == 0 || isFirstInGroup(pos)) {
            if (callback.getGroupFirstLine(pos).toUpperCase().equals("###")){
                outRect.top = 0;
            }else {
                outRect.top = mTitleHeight;
            }
        } else {
            outRect.top = 0;
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

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int position = params.getViewAdapterPosition();
            if (position == 0 || isFirstInGroup(position)) {
                drawTitleArea(c, left, right, child, params, position);
            }
        }
    }

    private void drawTitleArea(Canvas c, int left, int right, View child,
                               RecyclerView.LayoutParams params, int position) {
        final int rectBottom = child.getTop() - params.topMargin;
        if (callback.getGroupFirstLine(position).toUpperCase().equals("###")){
            c.drawRect(0, 0, 0,
                    0, mBackgroundPaint);
            c.drawText("", child.getPaddingLeft() + mTextStartMargin,
                    rectBottom - (mTitleHeight - mTextHeight) / 2 - mTextBaselineOffset, mTextPaint);
        }else {
            c.drawRect(left, rectBottom - mTitleHeight, right,
                    rectBottom, mBackgroundPaint);
            c.drawText(callback.getGroupFirstLine(position).toUpperCase(), child.getPaddingLeft() + mTextStartMargin,
                    rectBottom - (mTitleHeight - mTextHeight) / 2 - mTextBaselineOffset, mTextPaint);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        final int position = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();
        if (position == RecyclerView.NO_POSITION) {
            return;
        }

        View child = parent.findViewHolderForAdapterPosition(position).itemView;
        String initial = getTag(position);
        if (initial == null) {
            return;
        }
        boolean flag = false;

        if (getTag(position + 1) != null && !initial.equals(getTag(position + 1))) {
            if (child.getHeight() + child.getTop() < mTitleHeight) {
                c.save();
                flag = true;
                c.translate(0, child.getHeight() + child.getTop() - mTitleHeight);
            }
        }
        if (!initial.equals("###")) {
            c.drawRect(parent.getPaddingLeft(), parent.getPaddingTop(),
                    parent.getRight() - parent.getPaddingRight(), parent.getPaddingTop() + mTitleHeight, mBackgroundPaint);
            c.drawText(initial, child.getPaddingLeft() + mTextStartMargin,
                    parent.getPaddingTop() + mTitleHeight - (mTitleHeight - mTextHeight) / 2 - mTextBaselineOffset, mTextPaint);
        }else {
            c.drawRect(0, 0,0, 0, mBackgroundPaint);
            c.drawText("", child.getPaddingLeft() + mTextStartMargin,
                    parent.getPaddingTop() + mTitleHeight - (mTitleHeight - mTextHeight) / 2 - mTextBaselineOffset, mTextPaint);
        }
        if (flag) {
            c.restore();
        }
    }

    private String getTag(int position) {
        while (position >= 0) {
            if ((position == 0 || isFirstInGroup(position))) {
                return callback.getGroupFirstLine(position).toUpperCase();
            }
            position--;
        }
        return null;
    }

    public interface DecorationCallback {

        long getGroupId(int position);

        String getGroupFirstLine(int position);
    }
}

