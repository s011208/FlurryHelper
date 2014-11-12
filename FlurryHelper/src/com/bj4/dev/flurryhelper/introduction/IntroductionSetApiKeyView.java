
package com.bj4.dev.flurryhelper.introduction;

import java.lang.ref.WeakReference;

import com.bj4.dev.flurryhelper.R;
import com.bj4.dev.flurryhelper.SharedPreferencesHelper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IntroductionSetApiKeyView extends RelativeLayout {
    private EditText mSetApiText;

    private Button mConfirm;

    private TextView mFlurryLink;

    private WeakReference<IntroductionView> mParent;

    private WeakReference<Context> mContext;

    public IntroductionSetApiKeyView(Context context) {
        this(context, null);
    }

    public IntroductionSetApiKeyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IntroductionSetApiKeyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = new WeakReference<Context>(context);
    }

    public void setParent(IntroductionView parent) {
        mParent = new WeakReference<IntroductionView>(parent);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        mSetApiText = (EditText)findViewById(R.id.set_api_txt);
        // XXX remove later
        // mSetApiText.setText("H27G8H743385X8CFS3VQ");
        mConfirm = (Button)findViewById(R.id.set_api_txt_confirm);
        mConfirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final String apiKey = mSetApiText.getText().toString();
                if ("".equals(apiKey) == false && apiKey != null) {
                    if (mParent != null && mContext != null) {
                        final Context context = mContext.get();
                        final IntroductionView parent = mParent.get();
                        if (parent != null && context != null) {
                            SharedPreferencesHelper.getInstance(context).setAPIKey(apiKey);
                            SharedPreferencesHelper.getInstance(context).setShowIntroduction(true);
                            parent.setApiKey();
                            return;
                        }
                    }
                }
                // TODO insert failed
            }
        });
        mFlurryLink = (TextView)findViewById(R.id.flurry_manual_link);
        mFlurryLink.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final Context context = mContext.get();
                if (context == null)
                    return;
                String url = "http://support.flurry.com/index.php?title=API/GettingStarted";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        });
    }
}
