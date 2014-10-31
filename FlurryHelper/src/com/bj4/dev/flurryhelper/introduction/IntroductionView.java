
package com.bj4.dev.flurryhelper.introduction;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.bj4.dev.flurryhelper.R;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class IntroductionView extends FrameLayout implements OnPageChangeListener {
    public interface IntroductionViewCallback {
        public void setApiSuccess();
    }

    private Context mContext;

    private LayoutInflater mInflater;

    private ViewPager mIntroductionPager;

    private IntroductionPagerAdapter mIntroductionPagerAdapter;

    private WeakReference<IntroductionViewCallback> mCallback;

    public void setCallback(IntroductionViewCallback cb) {
        mCallback = new WeakReference<IntroductionViewCallback>(cb);
    }

    public IntroductionView(Context context) {
        this(context, null);
    }

    public IntroductionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IntroductionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mIntroductionPagerAdapter = new IntroductionPagerAdapter(generateIntroductionViews());
        final RelativeLayout parent = (RelativeLayout)mInflater.inflate(
                R.layout.introduction_viewpager, null);
        mIntroductionPager = (ViewPager)parent.findViewById(R.id.introduction_pager);
        mIntroductionPager.setAdapter(mIntroductionPagerAdapter);
        mIntroductionPager.setOnPageChangeListener(this);
        addView(parent);
    }

    protected void setApiKey() {
        if (mCallback != null) {
            final IntroductionViewCallback cb = mCallback.get();
            if (cb != null) {
                cb.setApiSuccess();
            }
        }
    }

    private ArrayList<View> generateIntroductionViews() {
        final ArrayList<View> rtn = new ArrayList<View>();
        IntroductionSetApiKeyView apiKeyView = (IntroductionSetApiKeyView)mInflater.inflate(R.layout.introduction_setapikey_view, null);
        apiKeyView.setParent(this);
        rtn.add(apiKeyView);
        return rtn;
    }

    public static class IntroductionPagerAdapter extends PagerAdapter {
        private List<View> mListViews;

        public IntroductionPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mListViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mListViews.get(position), 0);
            return mListViews.get(position);
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int arg0) {
        // TODO Auto-generated method stub

    }
}
