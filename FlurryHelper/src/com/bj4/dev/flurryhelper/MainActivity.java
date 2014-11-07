
package com.bj4.dev.flurryhelper;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bj4.dev.flurryhelper.dialogs.SetApiDialog;
import com.bj4.dev.flurryhelper.fragments.appmetricsfragment.AppMetricsFragment;
import com.bj4.dev.flurryhelper.fragments.eventmetricsfragment.EventMetricsFragment;
import com.bj4.dev.flurryhelper.fragments.projectfragment.ProjectFragment;
import com.bj4.dev.flurryhelper.introduction.IntroductionView;
import com.bj4.dev.flurryhelper.introduction.IntroductionView.IntroductionViewCallback;
import com.bj4.dev.flurryhelper.utils.LoadingView;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.os.Build;

public class MainActivity extends BaseActivity implements IntroductionViewCallback {

    private IntroductionView mIntroductionView;

    private RelativeLayout mMainActivity;

    private LoadingView mLoadingView;

    private static final int FRAGMENT_TYPE_PROJECT = 0;

    private static final int FRAGMENT_TYPE_APPMETRICS = 1;

    private static final int FRAGMENT_TYPE_EVENTMETRICS = 2;

    private int mNavigationFragment = FRAGMENT_TYPE_PROJECT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedData.getInstance();// init singleton
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponent();
    }

    public void initComponent() {
        super.initComponent();
        mMainActivity = (RelativeLayout)findViewById(R.id.main_activity);
        boolean showIntroductionView = !SharedPreferencesHelper.getInstance(this)
                .hasShowIntroduction();
        if (showIntroductionView) {
            mIntroductionView = new IntroductionView(this);
            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            hideActionBar();
            mIntroductionView.setCallback(this);
            mMainActivity.addView(mIntroductionView, rl);
        } else {
            setApiSuccess(false);
        }
        getNavigatorArea().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                navigateFragment();
            }
        });
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    public synchronized void setApiSuccess() {
        setApiSuccess(true);
    }

    public synchronized void setApiSuccess(boolean clearOldData) {
        // start to load
        if (mIntroductionView != null) {
            mMainActivity.removeView(mIntroductionView);
            mIntroductionView = null;
            showActionBar();
        }
        if (clearOldData) {
            SharedData.getInstance().clearAllData();
        }
        showLoadingView(true);
        setActionBarTitle("");
        enterCompanyFragment();
    }

    @Override
    public void onBackPressed() {
        if (mNavigationFragment == FRAGMENT_TYPE_PROJECT) {
            super.onBackPressed();
        } else {
            navigateFragment();
        }
    }

    private void navigateFragment() {
        if (mNavigationFragment == FRAGMENT_TYPE_APPMETRICS
                || mNavigationFragment == FRAGMENT_TYPE_EVENTMETRICS) {
            enterCompanyFragment();
        }
    }

    public void enterCompanyFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
                .setCustomAnimations(R.anim.fragment_alpha_in, R.anim.fragment_alpha_out);
        ProjectFragment fragment = new ProjectFragment();
        fragmentTransaction.replace(R.id.fragment_main, fragment, ProjectFragment.TAG);
        fragmentTransaction.commit();
        mNavigationFragment = FRAGMENT_TYPE_PROJECT;
        hideNavigator();
        showExpandCollapse(false);
    }

    public void enterAppMetricsFragment(final String projectKey) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
                .setCustomAnimations(R.anim.fragment_alpha_in, R.anim.fragment_alpha_out);
        AppMetricsFragment fragment = new AppMetricsFragment();
        Bundle args = new Bundle();
        args.putString(AppMetricsFragment.PROJECT_KEY, projectKey);
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.fragment_main, fragment, AppMetricsFragment.TAG);
        fragmentTransaction.commit();
        mNavigationFragment = FRAGMENT_TYPE_APPMETRICS;
        showNavigator();
        showExpandCollapse(false);
    }

    public void enterEventMetricsFragment(final String projectKey) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
                .setCustomAnimations(R.anim.fragment_alpha_in, R.anim.fragment_alpha_out);
        EventMetricsFragment fragment = new EventMetricsFragment();
        Bundle args = new Bundle();
        args.putString(EventMetricsFragment.PROJECT_KEY, projectKey);
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.fragment_main, fragment, EventMetricsFragment.TAG);
        fragmentTransaction.commit();
        mNavigationFragment = FRAGMENT_TYPE_EVENTMETRICS;
        showNavigator();
        showExpandCollapse(true);
    }

    public boolean isLoadingViewShowing() {
        return mLoadingView != null && mLoadingView.getVisibility() == View.VISIBLE;
    }

    public synchronized void showLoadingView(boolean animate) {
        if (mLoadingView == null) {
            mLoadingView = new LoadingView(this);
            mLoadingView.setVisibility(View.GONE);
        }

        if (isLoadingViewShowing())
            return;
        if (mLoadingView.getParent() != null) {
            ((ViewGroup)mLoadingView.getParent()).removeView(mLoadingView);
        }
        if (animate) {
            ObjectAnimator oa = ObjectAnimator.ofFloat(mLoadingView, View.ALPHA,
                    mLoadingView.getAlpha(), 1);
            oa.setDuration(200);
            oa.addListener(new AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {
                    mLoadingView.setAlpha(0);
                    mLoadingView.setVisibility(View.VISIBLE);
                    mMainActivity.addView(mLoadingView);
                    mMainActivity.bringChildToFront(mLoadingView);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    // TODO Auto-generated method stub
                    mLoadingView.setAlpha(1);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    // TODO Auto-generated method stub

                }
            });
            oa.start();
        } else {
            mLoadingView.setAlpha(1);
            mLoadingView.setVisibility(View.VISIBLE);
            mMainActivity.addView(mLoadingView);
            mMainActivity.bringChildToFront(mLoadingView);
        }
    }

    public void hideLoadingView(boolean animate) {
        if (mLoadingView == null) {
            return;
        }
        if (isLoadingViewShowing() == false)
            return;
        if (animate) {
            ObjectAnimator oa = ObjectAnimator.ofFloat(mLoadingView, View.ALPHA,
                    mLoadingView.getAlpha(), 0);
            oa.setDuration(200);
            oa.addListener(new AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    // TODO Auto-generated method stub
                    mMainActivity.removeView(mLoadingView);
                    mLoadingView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    // TODO Auto-generated method stub

                }
            });
            oa.start();
        } else {
            mMainActivity.removeView(mLoadingView);
            mLoadingView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onExpandCollapseClick() {
        Fragment currentFragment = getFragmentManager().findFragmentByTag(EventMetricsFragment.TAG);
        if (currentFragment != null) {
            ((EventMetricsFragment)currentFragment).onExpandStatusChanged();
        }
    }
}
