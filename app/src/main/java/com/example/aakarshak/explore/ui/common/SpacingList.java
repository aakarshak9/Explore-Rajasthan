package com.example.aakarshak.explore.ui.common;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class SpacingList extends RecyclerView.ItemDecoration {
    //Stores the spacing to be applied between the items in the List
    private final int mVerticalOffsetSize;
    //Stores the spacing to be applied between the items and its parent in the List
    private final int mHorizontalOffsetSize;

    public SpacingList(int verticalOffsetSize, int horizontalOffsetSize) {
        mVerticalOffsetSize = verticalOffsetSize;
        mHorizontalOffsetSize = horizontalOffsetSize;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //Get the Child V position in the adapter
        int position = parent.getChildAdapterPosition(view);

        //Evaluates to first item when position is 0
        boolean isFirstItem = (position == 0);

        //Set full spacing to the top when the Item is the First Item in the list
        if (isFirstItem) {
            outRect.top = mVerticalOffsetSize;
        }

        //Set full spacing to bottom
        outRect.bottom = mVerticalOffsetSize;
        //Set full spacing to left
        outRect.left = mHorizontalOffsetSize;
        //Set full spacing to right
        outRect.right = mHorizontalOffsetSize;
    }
}
