
package com.bj4.dev.flurryhelper.dialogs;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

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
    }

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

        ArrayList<String> data = new ArrayList<String>();
        data.add(mContext.getString(R.string.action_access_api_key));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_list_item_1, data);
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final MenuDialogCallback cb = mCallback.get();
                        if (cb == null)
                            return;
                        switch (which) {
                            case 0:
                                cb.clickSetAPIKey();
                                break;
                        }
                    }
                }).setCancelable(true).setTitle(null).create();
        final Window window = dialog.getWindow();
        if (window != null) {
            window.setGravity(Gravity.TOP);
            window.setWindowAnimations(android.R.style.Animation_Toast);
        }
        return dialog;
    }
}
