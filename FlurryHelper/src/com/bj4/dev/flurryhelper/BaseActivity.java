
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

public abstract class BaseActivity extends Activity implements SetApiDialog.SetApiSuccess,
        MenuDialog.MenuDialogCallback {
    private View mActionBar;

    private ImageView mMenu;

    private TextView mTitle, mNavigator;

    private LinearLayout mNavigatorArea;

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
                dialog.show(getFragmentManager(), MenuDialog.TAG);
            }
        });
        mTitle = (TextView)findViewById(R.id.menu_title);
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
        previousDiaog = getFragmentManager().findFragmentByTag(ProjectFragment.TAG);
        if (previousDiaog == null) {
            // ignore
        } else {
            if (previousDiaog.getActivity() != this) {
                // ignore
            } else {
            }
        }
    }
}
