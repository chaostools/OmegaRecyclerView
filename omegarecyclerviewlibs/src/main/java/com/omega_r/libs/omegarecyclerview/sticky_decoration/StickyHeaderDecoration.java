package com.omega_r.libs.omegarecyclerview.sticky_decoration;

import android.graphics.Rect;
import android.view.View;

import com.omega_r.libs.omegarecyclerview.utils.ViewUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import static com.omega_r.libs.omegarecyclerview.utils.ViewUtils.isReverseLayout;

public class StickyHeaderDecoration extends StickyDecoration {

    public StickyHeaderDecoration(StickyAdapter adapter) {
        super(adapter);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter == null) return;

        int position = parent.getChildAdapterPosition(view);
        int headerHeight = 0;
        if (position != RecyclerView.NO_POSITION && hasSticker(position)
                && showHeaderAboveItem(parent, position)) {

            RecyclerView.ViewHolder stickyHolder = getStickyHolder(parent, position);
            if(stickyHolder != null) {
                View header = stickyHolder.itemView;
                headerHeight = header.getHeight();
                if (isReverseLayout(parent)) {
                    if (position != adapter.getItemCount() - 1) headerHeight -= mItemSpace;
                } else {
                    if (position != 0) headerHeight -= mItemSpace;
                }
            }
        }
        outRect.set(0, headerHeight, 0, 0);
    }

    private boolean showHeaderAboveItem(@NonNull RecyclerView parent, int position) {
        if (mStickyAdapter == null) return false;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager == null) return false;

        if (isReverseLayout(parent)) {
            int itemCount = layoutManager.getItemCount();
            return position == (itemCount - 1) || mStickyAdapter.getStickyId(position + 1)
                    != mStickyAdapter.getStickyId(position);
        } else {
            return position == 0 || mStickyAdapter.getStickyId(position - 1)
                    != mStickyAdapter.getStickyId(position);
        }
    }

    @Override
    int getOffset(@Nullable RecyclerView.ViewHolder stickyHolder, int stickerHeight, View next) {
        if (stickyHolder == null) return 0;
        return ((int) next.getY()) - (stickerHeight + stickyHolder.itemView.getHeight());
    }

    @Override
    protected int getStickerTop(boolean isReverseLayout, View child, View sticker, int layoutPos) {
        return (int) Math.max(0, child.getY() - sticker.getHeight());
    }

    @Override
    protected int getMeasureStickerWidthMode() {
        return View.MeasureSpec.EXACTLY;
    }

}
