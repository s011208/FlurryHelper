
package com.bj4.dev.flurryhelper;

import com.bj4.dev.flurryhelper.dialogs.MenuDialog;
import com.bj4.dev.flurryhelper.dialogs.SetApiDialog;
import com.bj4.dev.flurryhelper.fragments.projectfragment.ProjectFragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public abstract class BaseActivity extends Activity implements SetApiDialog.SetApiSuccess,
        MenuDialog.MenuDialogCallback {

    public static final int EXPAND = 1;

    public static final int COLLAPSE = 0;

    private static final String PREVIOUS_EXPAND_STATE = "previous_expand_state";

    private View mActionBar;

    private ImageView mMenu;

    private TextView mTitle, mNavigator;

    private LinearLayout mNavigatorArea;

    private ViewSwitcher mExpandCollapse;

    private int mPreviousExpandState;

    private static String sActionBarTitle = "";

    private void setMainLayout() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public LinearLayout getNavigatorArea() {
        return mNavigatorArea;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setMainLayout();
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mPreviousExpandState = savedInstanceState.getInt(PREVIOUS_EXPAND_STATE, 0);
        }
    }

    public void initComponent() {
        initActionBar();
    }

    public void hideActionBar() {
        if (mActionBar == null)
            return;
        mActionBar.setVisibility(View.GONE);
    }

    public void showActionBar() {
        if (mActionBar == null)
            return;
        mActionBar.setVisibility(View.VISIBLE);
    }

    public void setActionBarTitle(String title) {
        if (mActionBar == null)
            return;
        mTitle.setText(title);
        sActionBarTitle = title;
    }

    public void showNavigator() {
        if (mNavigator != null) {
            mNavigator.setVisibility(View.VISIBLE);
        }
    }

    public void hideNavigator() {
        if (mNavigator != null) {
            mNavigator.setVisibility(View.GONE);
        }
    }

    private void initActionBar() {
        mActionBar = findViewById(R.id.action_bar);
        if (mActionBar == null) {
            return;
        }
        mNavigatorArea = (LinearLayout)findViewById(R.id.navigator_area);
        mNavigator = (TextView)findViewById(R.id.navigator);
        mMenu = (ImageView)findViewById(R.id.menu);
        mMenu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                MenuDialog dialog = new MenuDialog();
                dialog.setCallback(BaseActivity.this);
                Bundle args = new Bundle();
                args.putString(MenuDialog.PROJECT_KEY, null);
                dialog.setArguments(args);
                dialog.show(getFragmentManager(), MenuDialog.TAG);
            }
        });
        mTitle = (TextView)findViewById(R.id.menu_title);
        if (SharedData.getCompanyName() == null)
            mTitle.setText(sActionBarTitle);
        else {
            mTitle.setText(SharedData.getCompanyName().getCompanyName());
        }
        mExpandCollapse = (ViewSwitcher)findViewById(R.id.expand_collapse);
        mExpandCollapse.setDisplayedChild(mPreviousExpandState);
        mExpandCollapse.setInAnimation(this, R.anim.collapse_expand_switch_in);
        mExpandCollapse.setOutAnimation(this, R.anim.collapse_expand_switch_out);
        mExpandCollapse.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mExpandCollapse.showNext();
                onExpandCollapseClick();
            }
        });
    }

    protected void onSaveInstanceState(Bundle icicle) {
        super.onSaveInstanceState(icicle);
        icicle.putInt(PREVIOUS_EXPAND_STATE, mExpandCollapse.getDisplayedChild());
    }

    protected abstract void onExpandCollapseClick();

    public boolean isExpanded() {
        return mExpandCollapse.getDisplayedChild() == EXPAND;
    }

    public void showExpandCollapse(boolean show) {
        if (show)
            mExpandCollapse.setVisibility(View.VISIBLE);
        else
            mExpandCollapse.setVisibility(View.GONE);
    }

    public void onResume() {
        super.onResume();
        checkDialogStatus();
    }

    @Override
    public void clickSetAPIKey() {
        SetApiDialog dialog = new SetApiDialog();
        dialog.setCallback(this);
        dialog.show(getFragmentManager(), SetApiDialog.TAG);
    }

    private void checkDialogStatus() {
        Fragment previousDiaog = getFragmentManager().findFragmentByTag(SetApiDialog.TAG);
        if (previousDiaog == null) {
            // ignore
        } else {
            if (previousDiaog.getActivity() != this) {
                // ignore
            } else {
                // dialog on screen
                SetApiDialog dialog = (SetApiDialog)previousDiaog;
                dialog.setCallback(this);
                return;
            }
        }
    }
}
