
package com.bj4.dev.flurryhelper;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bj4.dev.flurryhelper.dialogs.SetApiDialog;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.os.Build;

/**
 * Document doc; String title = null; try { doc =
 * Jsoup.connect("https://play.google.com/store/apps/details?id=" + mPkg).get();
 * Elements eles = doc.select("div[class=document-title]").select("div"); for
 * (Element ele : eles) { title = ele.text(); } Context context =
 * mContext.get(); if (context != null) { getInstance(context).addTitle(mPkg,
 * title); } } catch (IOException e) { Log.w(TAG, "failed", e); }
 * 
 * @author Yen-Hsun_Huang
 */
public class MainActivity extends Activity implements SetApiDialog.SetApiSuccess {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onResume() {
        super.onResume();
        checkDialogStatus();
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

    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_access_api_key:
                SetApiDialog dialog = new SetApiDialog();
                dialog.setCallback(this);
                dialog.show(getFragmentManager(), SetApiDialog.TAG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void setApiSuccess() {
        // start to load
    }
}
