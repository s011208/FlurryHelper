
package com.bj4.dev.flurryhelper;

import com.bj4.dev.flurryhelper.dialogs.MenuDialog;
import com.bj4.dev.flurryhelper.dialogs.SetApiDialog;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;

public abstract class BaseActivity extends Activity implements SetApiDialog.SetApiSuccess,
        MenuDialog.MenuDialogCallback {
    private View mActionBar;

    private ImageView mMenu;

    private void setMainLayout() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

    private void initActionBar() {
        mActionBar = findViewById(R.id.action_bar);
        if (mActionBar == null) {
            return;
        }
        mMenu = (ImageView)findViewById(R.id.menu);
        mMenu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                MenuDialog dialog = new MenuDialog();
                dialog.setCallback(BaseActivity.this);
                dialog.show(getFragmentManager(), MenuDialog.TAG);
            }
        });
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
