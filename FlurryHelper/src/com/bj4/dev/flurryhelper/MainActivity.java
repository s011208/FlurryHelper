
package com.bj4.dev.flurryhelper;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bj4.dev.flurryhelper.dialogs.SetApiDialog;
import com.bj4.dev.flurryhelper.fragments.ProjectFragment;
import com.bj4.dev.flurryhelper.introduction.IntroductionView;
import com.bj4.dev.flurryhelper.introduction.IntroductionView.IntroductionViewCallback;
import com.bj4.dev.flurryhelper.utils.LoadingView;

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
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.os.Build;

public class MainActivity extends BaseActivity implements IntroductionViewCallback {

    private IntroductionView mIntroductionView;

    private RelativeLayout mMainActivity;

    private LoadingView mLoadingView;

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
            setApiSuccess();
        }
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    public synchronized void setApiSuccess() {
        // start to load
        if (mIntroductionView != null) {
            mMainActivity.removeView(mIntroductionView);
            mIntroductionView = null;
            showActionBar();
        }
        showLoadingView();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ProjectFragment fragment = new ProjectFragment();
        fragmentTransaction.add(R.id.fragment_main, fragment, ProjectFragment.TAG);
        fragmentTransaction.commit();
    }

    public synchronized void showLoadingView() {
        if (mLoadingView == null) {
            mLoadingView = new LoadingView(this);
        }
        if (mLoadingView.getParent() != null) {
            ((ViewGroup)mLoadingView.getParent()).removeView(mLoadingView);
        }
        mMainActivity.addView(mLoadingView);
        mMainActivity.bringChildToFront(mLoadingView);
    }

    public void hideLoadingView() {
        if (mLoadingView == null) {
            return;
        }
        mMainActivity.removeView(mLoadingView);
    }
}
