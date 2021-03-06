
package com.bj4.dev.flurryhelper.dialogs;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.bj4.dev.flurryhelper.MainActivity;
import com.bj4.dev.flurryhelper.R;
import com.bj4.dev.flurryhelper.SharedPreferencesHelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;

public class MenuDialog extends DialogFragment {
    public interface MenuDialogCallback {
        public void clickSetAPIKey();

        public void clickRefresh();
    }

    public static final String PROJECT_KEY = "project_key";

    public static final String TAG = "MenuDialog";

    private Context mContext;

    private WeakReference<MenuDialogCallback> mCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setCallback(MenuDialogCallback cb) {
        mCallback = new WeakReference<MenuDialogCallback>(cb);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = new ContextThemeWrapper(getActivity(),
                android.R.style.Theme_DeviceDefault_Light_Dialog);
        Bundle args = getArguments();
        final String projectKey;
        if (args != null) {
            projectKey = args.getString(PROJECT_KEY);
        } else {
            projectKey = null;
        }
        ArrayList<String> data = new ArrayList<String>();
        final String refresh = mContext.getString(R.string.menu_refresh_data);
        data.add(refresh);
        final String apiKey = mContext.getString(R.string.menu_set_api_key);
        data.add(apiKey);
        final String timePeriod = mContext.getString(R.string.menu_set_time_period);
        data.add(timePeriod);
        final String version = mContext.getString(R.string.menu_set_version);
        if (projectKey != null) {
            data.add(version);
        }
        final String showChart = mContext.getString(R.string.menu_show_chart_on_project_fragment);
        final String hideChart = mContext.getString(R.string.menu_hide_chart_on_project_fragment);
        if (((MainActivity)getActivity()).getNavigatorType() == MainActivity.FRAGMENT_TYPE_PROJECT) {
            data.add(SharedPreferencesHelper.getInstance(getActivity())
                    .isShowChartInProjectFragment() ? hideChart : showChart);
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_list_item_1, data);
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final MenuDialogCallback cb = mCallback.get();
                        final String selectedItem = adapter.getItem(which);
                        if (cb == null)
                            return;
                        if (refresh.equals(selectedItem)) {
                            cb.clickRefresh();
                        } else if (apiKey.equals(selectedItem)) {
                            cb.clickSetAPIKey();
                        } else if (timePeriod.equals(selectedItem)) {
                            showDisplayPeriodDialog();
                        } else if (version.equals(selectedItem)) {
                            showSetVersionDialog(projectKey);
                        } else if (showChart.equals(selectedItem)) {
                            SharedPreferencesHelper.getInstance(getActivity())
                                    .setShowChartInProjectFragment(true);
                        } else if (hideChart.equals(selectedItem)) {
                            SharedPreferencesHelper.getInstance(getActivity())
                                    .setShowChartInProjectFragment(false);
                        }
                    }
                }).setCancelable(true).setTitle(null).create();
        return dialog;
    }

    private void showSetVersionDialog(final String projectKey) {
        SetVersionDialog dialog = new SetVersionDialog();
        Bundle b = new Bundle();
        b.putString(SetVersionDialog.PROJECT_KEY, projectKey);
        dialog.setArguments(b);
        dialog.show(getFragmentManager(), SetVersionDialog.TAG);
    }

    private void showDisplayPeriodDialog() {
        new AlertDialog.Builder(mContext)
                .setSingleChoiceItems(
                        new CharSequence[] {
                                mContext.getString(R.string.diaply_period_last_day),
                                mContext.getString(R.string.diaply_period_last_week),
                                mContext.getString(R.string.diaply_period_last_month),
                                mContext.getString(R.string.diaply_period_three_months),
                                mContext.getString(R.string.diaply_period_half_year),
                                mContext.getString(R.string.diaply_period_year),
                        }, SharedPreferencesHelper.getInstance(mContext).getDisplayPeriod(),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                SharedPreferencesHelper.getInstance(mContext)
                                        .setDisplayPeriod(arg1);
                                arg0.dismiss();
                            }
                        }).create().show();
    }

    @Override
    public void onStart() {
        super.onStart();
        final Window window = getDialog().getWindow();
        window.setWindowAnimations(android.R.style.Animation_Translucent);
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.RIGHT | Gravity.TOP;
        params.y = (int)getActivity().getResources().getDimension(R.dimen.action_bar_height);
        params.width = (int)getActivity().getResources().getDimension(R.dimen.menu_dialog_width);
        window.setAttributes(params);
    }
}
