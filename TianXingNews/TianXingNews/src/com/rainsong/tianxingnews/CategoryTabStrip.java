package com.rainsong.tianxingnews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CategoryTabStrip extends HorizontalScrollView {
    private LayoutInflater mLayoutInflater;
    private final PageListener pageListener = new PageListener();
    private ViewPager pager;
    private LinearLayout tabsContainer;
    private int tabCount;

    private int currentPosition = 0;
    private float currentPositionOffset = 0f;

    private Rect indicatorRect;

    private LinearLayout.LayoutParams defaultTabLayoutParams;

    private int scrollOffset = 10;
    private int lastScrollX = 0;

    private Drawable indicator;
    private TextDrawable[] drawables;
    private Drawable left_edge;
    private Drawable right_edge;

    public CategoryTabStrip(Context context) {
        this(context, null);
    }

    public CategoryTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CategoryTabStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mLayoutInflater = LayoutInflater.from(context);
        drawables = new TextDrawable[3];
        int i = 0;
        while (i < drawables.length) {
            drawables[i] = new TextDrawable(getContext());
            i++;
        }

        indicatorRect = new Rect();

        setFillViewport(true);
        setWillNotDraw(false);

        // 标签容器
        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        scrollOffset = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);

        defaultTabLayoutParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        // 绘制高亮区域作为滑动分页指示器
        indicator = getResources()
                .getDrawable(R.drawable.bg_category_indicator);
        // 左右边界阴影效果
        left_edge = getResources()
                .getDrawable(R.drawable.ic_category_left_edge);
        right_edge = getResources().getDrawable(
                R.drawable.ic_category_right_edge);
    }

    // 绑定与CategoryTabStrip控件对应的ViewPager控件，实现联动
    public void setViewPager(ViewPager pager) {
        this.pager = pager;
        if (pager.getAdapter() == null) {
            throw new IllegalStateException(
                    "ViewPager does not have adapter instance.");
        }
        pager.setOnPageChangeListener(pageListener);
        notifyDataSetChanged();
    }

    // 当附加在ViewPager适配器上的数据发生变化时,应该调用该方法通知CategoryTabStrip刷新数据
    public void notifyDataSetChanged() {
        tabsContainer.removeAllViews();
        tabCount = pager.getAdapter().getCount();
        for (int i = 0; i < tabCount; i++) {
            addTab(i, pager.getAdapter().getPageTitle(i).toString());
        }
    }

    // 添加一个标签到导航菜单
    private void addTab(final int position, String title) {
        ViewGroup tab = (ViewGroup) mLayoutInflater.inflate(
                R.layout.category_tab, this, false);
        TextView category_text = (TextView) tab
                .findViewById(R.id.category_text);
        category_text.setText(title);
        category_text.setGravity(Gravity.CENTER);
        category_text.setSingleLine();
        category_text.setFocusable(true);
        category_text.setTextColor(getResources().getColor(
                R.color.category_tab_text));
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(position);
            }
        });
        tabsContainer.addView(tab, position, defaultTabLayoutParams);
    }

    // 计算滑动过程中矩形高亮区域的上下左右位置
    private void calculateIndicatorRect(Rect rect) {
        ViewGroup currentTab = (ViewGroup) tabsContainer
                .getChildAt(currentPosition);
        TextView category_text = (TextView) currentTab
                .findViewById(R.id.category_text);

        float left = (float) (currentTab.getLeft() + category_text.getLeft());
        float width = ((float) category_text.getWidth()) + left;

        if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {
            ViewGroup nextTab = (ViewGroup) tabsContainer
                    .getChildAt(currentPosition + 1);
            TextView next_category_text = (TextView) nextTab
                    .findViewById(R.id.category_text);

            float next_left = (float) (nextTab.getLeft() + next_category_text
                    .getLeft());
            left = left * (1.0f - currentPositionOffset) + next_left
                    * currentPositionOffset;
            width = width * (1.0f - currentPositionOffset)
                    + currentPositionOffset
                    * (((float) next_category_text.getWidth()) + next_left);
        }

        rect.set(((int) left) + getPaddingLeft(),
                getPaddingTop() + currentTab.getTop() + category_text.getTop(),
                ((int) width) + getPaddingLeft(), currentTab.getTop()
                        + getPaddingTop() + category_text.getTop()
                        + category_text.getHeight());
    }

    // 计算滚动范围
    private int getScrollRange() {
        return getChildCount() > 0 ? Math.max(0, getChildAt(0).getWidth()
                - getWidth() + getPaddingLeft() + getPaddingRight()) : 0;
    }

    // CategoryTabStrip与ViewPager联动逻辑
    private void scrollToChild(int position, int offset) {
        if (tabCount == 0) {
            return;
        }

        calculateIndicatorRect(indicatorRect);
        int newScrollX = lastScrollX;
        if (indicatorRect.left < getScrollX() + scrollOffset) {
            newScrollX = indicatorRect.left - scrollOffset;
        } else if (indicatorRect.right > getScrollX() + getWidth()
                - scrollOffset) {
            newScrollX = indicatorRect.right - getWidth() + scrollOffset;
        }
        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }
    }

    // 自定义绘图
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // 绘制高亮背景矩形红框
        calculateIndicatorRect(indicatorRect);
        if (indicator != null) {
            indicator.setBounds(indicatorRect);
            indicator.draw(canvas);
        }

        // 绘制背景红框内标签文本
        int i = 0;
        while (i < tabsContainer.getChildCount()) {
            if (i < currentPosition - 1 || i > currentPosition + 1) {
                i++;
            } else {
                ViewGroup tab = (ViewGroup) tabsContainer.getChildAt(i);
                TextView category_text = (TextView) tab
                        .findViewById(R.id.category_text);
                if (category_text != null) {
                    TextDrawable textDrawable = drawables[i - currentPosition
                            + 1];
                    int save = canvas.save();
                    calculateIndicatorRect(indicatorRect);
                    canvas.clipRect(indicatorRect);
                    textDrawable.setText(category_text.getText());
                    textDrawable.setTextSize(0, category_text.getTextSize());
                    textDrawable.setTextColor(getResources().getColor(
                            R.color.category_tab_highlight_text));
                    int left = tab.getLeft()
                            + category_text.getLeft()
                            + (category_text.getWidth() - textDrawable
                                    .getIntrinsicWidth()) / 2
                            + getPaddingLeft();
                    int top = tab.getTop()
                            + category_text.getTop()
                            + (category_text.getHeight() - textDrawable
                                    .getIntrinsicHeight()) / 2
                            + getPaddingTop();
                    textDrawable.setBounds(left, top,
                            textDrawable.getIntrinsicWidth() + left,
                            textDrawable.getIntrinsicHeight() + top);
                    textDrawable.draw(canvas);
                    canvas.restoreToCount(save);
                }
                i++;
            }
        }

        // 绘制左右边界阴影效果
        i = canvas.save();
        int top = getScrollX();
        int height = getHeight();
        int width = getWidth();
        canvas.translate((float) top, 0.0f);
        if (left_edge == null || top <= 0) {
            if (right_edge == null || top >= getScrollRange()) {
                canvas.restoreToCount(i);
            }
            right_edge.setBounds(width - right_edge.getIntrinsicWidth(), 0,
                    width, height);
            right_edge.draw(canvas);
            canvas.restoreToCount(i);
        }
        left_edge.setBounds(0, 0, left_edge.getIntrinsicWidth(), height);
        left_edge.draw(canvas);
        if (right_edge == null || top >= getScrollRange()) {
            canvas.restoreToCount(i);
        }
        right_edge.setBounds(width - right_edge.getIntrinsicWidth(), 0, width,
                height);
        right_edge.draw(canvas);
        canvas.restoreToCount(i);
    }

    private class PageListener implements OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset,
                int positionOffsetPixels) {
            currentPosition = position;
            currentPositionOffset = positionOffset;

            scrollToChild(position, (int) (positionOffset * tabsContainer
                    .getChildAt(position).getWidth()));
            invalidate();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                if (pager.getCurrentItem() == 0) {
                    // 滑动到最左边
                    scrollTo(0, 0);
                } else if (pager.getCurrentItem() == tabCount - 1) {
                    // 滑动到最右边
                    scrollTo(getScrollRange(), 0);
                } else {
                    scrollToChild(pager.getCurrentItem(), 0);
                }
            }
        }

        @Override
        public void onPageSelected(int position) {
        }
    }
}
