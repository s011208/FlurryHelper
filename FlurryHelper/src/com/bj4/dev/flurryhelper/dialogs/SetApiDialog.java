
package com.bj4.dev.flurryhelper.dialogs;

import java.lang.ref.WeakReference;

import com.bj4.dev.flurryhelper.R;
import com.bj4.dev.flurryhelper.SharedPreferencesHelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public class SetApiDialog extends DialogFragment {

    public static final String TAG = "SetApiDialog";

    public interface SetApiSuccess {
        public void setApiSuccess();
    }

    private Context mContext;

    private View mContentView;

    private EditText mTxt;

    private WeakReference<SetApiSuccess> mCallback;

    public void setCallback(SetApiSuccess callback) {
        mCallback = new WeakReference<SetApiSuccess>(callback);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = new ContextThemeWrapper(getActivity(),
                android.R.style.Theme_DeviceDefault_Light_Dialog);
        initContent();
        AlertDialog dialog = new AlertDialog.Builder(mContext).setView(mContentView)
                .setCancelable(false).setTitle(mContext.getString(R.string.action_access_api_key))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String newApiKey = mTxt.getText().toString();
                        final String currentApiKey = SharedPreferencesHelper.getInstance(mContext)
                                .getAPIKey();
                        if (newApiKey != null && newApiKey.isEmpty() == false
                                && newApiKey.equals(currentApiKey) == false) {
                            SharedPreferencesHelper.getInstance(mContext).setAPIKey(
                                    mTxt.getText().toString());
                            if (mCallback.get() != null) {
                                mCallback.get().setApiSuccess();
                            }
                        }
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                }).create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return dialog;
    }

    private void initContent() {
        LayoutInflater inflater = (LayoutInflater)mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.set_api_dialog, null);
        mTxt = (EditText)mContentView.findViewById(R.id.set_api_edit);
        mTxt.setText(SharedPreferencesHelper.getInstance(mContext).getAPIKey());
    }
}
