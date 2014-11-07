
package com.bj4.dev.flurryhelper.fragments.projectfragment;

import java.io.IOException;
import java.lang.ref.WeakReference;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.bj4.dev.flurryhelper.MainActivity;
import com.bj4.dev.flurryhelper.R;
import com.bj4.dev.flurryhelper.SharedData;
import com.bj4.dev.flurryhelper.SharedPreferencesHelper;
import com.bj4.dev.flurryhelper.dialogs.MenuDialog;
import com.bj4.dev.flurryhelper.dialogs.ProjectDataChooserDialog;
import com.bj4.dev.flurryhelper.utils.CompanyName;
import com.bj4.dev.flurryhelper.utils.Utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * http://api.flurry.com/appInfo/getAllApplications?apiAccessCode=7
 * H27G8H743385X8CFS3VQ
 * http://api.flurry.com/appMetrics/ActiveUsersByWeek?
 * apiAccessCode=H27G8H743385X8CFS3VQ&apiKey=KKVRQV7Y3FFF5GD6KVRG&startDate=2014-01-01&endDate=2014-12-31
 * 
 * @author yenhsunhuang
 */
public class ProjectFragment extends Fragment {
    public static final String TAG = "ProjectFragment";

    private View mContent;

    private ListView mProjectInfos;

    private ProjectInfoAdapter mProjectInfoAdapter;

    public void notifyDataChanged() {
        final MainActivity activity = (MainActivity)getActivity();
        activity.hideLoadingView(true);
        final SharedData sd = SharedData.getInstance();
        final CompanyName currentCompany = sd.getCompanyName();
        if (currentCompany != null) {
            activity.setActionBarTitle(currentCompany.getCompanyName());
            mProjectInfoAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getActivity(), "retrieve company data failed", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContent = inflater.inflate(R.layout.project_fragment, null);
        mProjectInfos = (ListView)mContent.findViewById(R.id.project_infos);
        mProjectInfoAdapter = new ProjectInfoAdapter(getActivity());
        mProjectInfos.setAdapter(mProjectInfoAdapter);
        mProjectInfos.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                ProjectDataChooserDialog dialog = new ProjectDataChooserDialog();
                Bundle args = new Bundle();
                args.putString(ProjectDataChooserDialog.PROJECT_KEY,
                        mProjectInfoAdapter.getItem(position).getApiKey());
                args.putInt(ProjectDataChooserDialog.BUNDLE_KEY_Y,
                        (int)arg1.getY() + arg1.getHeight() / 2);
                dialog.setArguments(args);
                dialog.show(getFragmentManager(), ProjectDataChooserDialog.TAG);
            }
        });
        final SharedData sd = SharedData.getInstance();
        final CompanyName currentCompany = sd.getCompanyName();
        if (currentCompany == null) {
            new RetrieveApplicationInfoTask(this, getActivity()).execute();
        } else {
            notifyDataChanged();
        }
        return mContent;
    }

}
