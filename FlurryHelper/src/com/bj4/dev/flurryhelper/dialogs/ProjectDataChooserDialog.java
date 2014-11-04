
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

public class ProjectDataChooserDialog extends DialogFragment {

    public static final String TAG = "ProjectDataChooserDialog";

    private Context mContext;

    public static final String PROJECT_KEY = "project_key";

    public static final String BUNDLE_KEY_Y = "bundle_y";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = new ContextThemeWrapper(getActivity(),
                android.R.style.Theme_DeviceDefault_Light_Dialog);

        ArrayList<String> data = new ArrayList<String>();
        data.add(mContext.getString(R.string.project_data_chooser_appmetrics));
        data.add(mContext.getString(R.string.project_data_chooser_eventmetrics));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_list_item_1, data);
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String projectKey = getArguments().getString(PROJECT_KEY, null);
                        if (projectKey == null)
                            return;
                        switch (which) {
                            case 0:
                                ((MainActivity)getActivity()).enterAppMetricsFragment(projectKey);
                                break;
                            case 1:
                                ((MainActivity)getActivity()).enterEventMetricsFragment(projectKey);
                                break;
                        }
                    }
                }).setCancelable(true).setTitle(null).create();
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        final Window window = getDialog().getWindow();
        window.setWindowAnimations(android.R.style.Animation_Activity);
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        params.y = getArguments().getInt(BUNDLE_KEY_Y, 0);
        window.setAttributes(params);
    }
}
