
package com.bj4.dev.flurryhelper.dialogs;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;

import com.bj4.dev.flurryhelper.MainActivity;
import com.bj4.dev.flurryhelper.R;
import com.bj4.dev.flurryhelper.SharedData;
import com.bj4.dev.flurryhelper.SharedPreferencesHelper;
import com.bj4.dev.flurryhelper.utils.AppVersionInfo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public class SetVersionDialog extends DialogFragment {

    public static final String TAG = "SetVersionDialog";

    public static final String PROJECT_KEY = "project_key";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final ArrayList<String> adapter = new ArrayList<String>();
        final String projectKey = getArguments().getString(PROJECT_KEY);
        int selection = 0;
        if (projectKey != null) {
            AppVersionInfo info = SharedData.getVersionInfo(projectKey);
            if (info != null) {
                adapter.addAll(info.getVersions());
                selection = info.getSelectedVersionIndex();
            }
        }
        final CharSequence[] data = new CharSequence[adapter.size() + 1];
        data[0] = getActivity().getString(R.string.version_dialog_all_version);
        for (int i = 0; i < adapter.size(); i++) {
            data[i + 1] = adapter.get(i);
        }

        AlertDialog dialog = new AlertDialog.Builder(getActivity()).setSingleChoiceItems(data,
                selection, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedData.setSelectedVersion(projectKey, which);
                        ((MainActivity)getActivity()).onVersionChanged();
                    }
                }).create();
        return dialog;
    }
}
