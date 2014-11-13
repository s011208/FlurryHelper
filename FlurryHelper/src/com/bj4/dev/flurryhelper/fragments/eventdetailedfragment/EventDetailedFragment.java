
package com.bj4.dev.flurryhelper.fragments.eventdetailedfragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import com.bj4.dev.flurryhelper.MainActivity;
import com.bj4.dev.flurryhelper.R;
import com.bj4.dev.flurryhelper.SharedData;
import com.bj4.dev.flurryhelper.fragments.BaseFragment;
import com.bj4.dev.flurryhelper.utils.EventDetailed;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class EventDetailedFragment extends BaseFragment implements
        EventDetailedLoadingHelper.Callback {
    public static final String TAG = "EventDetailedFragment";

    public static final String PROJECT_KEY = "project_key";

    public static final String EVENT_NAME_KEY = "event_name_key";

    private String mProjectKey;

    private String mEventName;

    private View mContent;

    private Spinner mActionsSpinner;

    private ArrayAdapter<String> mActionSpinnerAdapter;

    private ListView mDetailContent;

    private EventDetailListAdapter mEventDetailListAdapter;

    private FrameLayout mChartContainer;

    private TextView mNavChart, mNavTable;

    private ViewSwitcher mChartSwitcher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mProjectKey = getArguments().getString(PROJECT_KEY);
        mEventName = getArguments().getString(EVENT_NAME_KEY);
        mContent = inflater.inflate(R.layout.event_detail_fragment, null);
        mActionsSpinner = (Spinner)mContent.findViewById(R.id.event_detailed_spinner);
        mDetailContent = (ListView)mContent.findViewById(R.id.event_detailed_content_list);
        mEventDetailListAdapter = new EventDetailListAdapter(getActivity(), mEventName);
        mDetailContent.setAdapter(mEventDetailListAdapter);
        mChartSwitcher = (ViewSwitcher)mContent.findViewById(R.id.chart_type_container);
        mNavChart = (TextView)mContent.findViewById(R.id.chart_type_chart);
        mNavTable = (TextView)mContent.findViewById(R.id.chart_type_table);
        mNavChart.setBackgroundResource(R.drawable.navigator_bg);
        mNavChart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mNavTable.setBackground(null);
                mNavChart.setBackgroundResource(R.drawable.navigator_bg);
                mChartSwitcher.setDisplayedChild(0);
            }
        });
        mNavTable.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mNavChart.setBackground(null);
                mNavTable.setBackgroundResource(R.drawable.navigator_bg);
                mChartSwitcher.setDisplayedChild(1);
            }
        });
        mChartContainer = (FrameLayout)mContent.findViewById(R.id.event_detailed_chart_container);
        final EventDetailed detailed = SharedData.getEventDetailedData(mEventName);
        if (detailed != null) {
            setSpinnerAdapter();
        } else {
            checkLoadingView(true, true);
            new EventDetailedLoadingHelper(getActivity(), mProjectKey, mEventName, this).execute();
        }
        ((MainActivity)getActivity()).setProjectKey(mProjectKey);
        return mContent;
    }

    private void setSpinnerAdapter() {
        final EventDetailed detailed = SharedData.getEventDetailedData(mEventName);
        mActionSpinnerAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.event_detail_spinner_text, detailed != null ? detailed.getAllActions()
                        : new ArrayList<String>());
        mActionsSpinner.setAdapter(mActionSpinnerAdapter);
        mActionsSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                final String action = mActionSpinnerAdapter.getItem(arg2);
                refreshEventDetailContent(action);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void refreshEventDetailContent(final String action) {
        if (mEventDetailListAdapter == null)
            return;
        mEventDetailListAdapter.setListContent(action);
        mDetailContent.smoothScrollToPosition(0);
        fillUpChart(action);
    }

    private void fillUpChart(final String action) {
        mChartContainer.removeAllViews();
        final ArrayList<String> nameList = new ArrayList<String>();

        final ArrayList<Long> valueList = new ArrayList<Long>();
        final EventDetailed detailed = SharedData.getEventDetailedData(mEventName);
        if (detailed == null)
            return;
        final Map<String, Long> parameters = detailed.getParameters(action);
        if (parameters == null)
            return;
        Iterator<String> iter = parameters.keySet().iterator();
        while (iter.hasNext()) {
            final String key = iter.next();
            final Long value = parameters.get(key);
            if (value == null || key == null)
                continue;
            nameList.add(key);
            valueList.add(value);
        }
        View pieChart = createPieChart(nameList, valueList);
        mChartContainer.addView(pieChart);
    }

    private View createPieChart(ArrayList<String> nameList, ArrayList<Long> valueList) {
        final int[] chartColors = new int[] {
                Color.rgb(0xff, 0xab, 0xab), Color.rgb(0xff, 0xc5, 0x59),
                Color.rgb(0xf5, 0xf5, 0x00), Color.rgb(0x6e, 0xff, 0x6e),
                Color.rgb(0x6e, 0xff, 0xff), Color.rgb(0x6e, 0x6e, 0xff),
                Color.rgb(0xac, 0x3b, 0xff), Color.rgb(0xff, 0x6e, 0xff)
        };
        Resources res = getActivity().getResources();
        final int margin = (int)res.getDimension(R.dimen.event_detail_chart_margin);
        CategorySeries series = new CategorySeries("");
        DefaultRenderer defaultRenderer = new DefaultRenderer();
        defaultRenderer.setApplyBackgroundColor(true);
        defaultRenderer.setBackgroundColor(Color.argb(100, 00, 00, 00));
        defaultRenderer.setLabelsColor(Color.BLACK);
        defaultRenderer.setLabelsTextSize(res
                .getDimension(R.dimen.event_detail_chart_label_textsize));
        defaultRenderer.setLegendTextSize(res
                .getDimension(R.dimen.event_detail_chart_legend_textsize));
        defaultRenderer.setMargins(new int[] {
                margin, margin, margin, margin
        });
        defaultRenderer.setZoomButtonsVisible(true);
        defaultRenderer.setStartAngle(0);
        final String skippedItem = res.getString(R.string.chart_skipped_data);
        long skippedValue = 0;
        for (int i = 0; i < valueList.size(); i++) {
            if (i >= 14) {
                // to avoid to much items
                skippedValue += valueList.get(i);
                if (i == valueList.size() - 1) {
                    series.add(skippedItem + "(" + skippedValue + ")", skippedValue);
                    SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
                    renderer.setColor(chartColors[(series.getItemCount() - 1) % chartColors.length]);
                    defaultRenderer.addSeriesRenderer(renderer);
                }
            } else {
                series.add(nameList.get(i) + "(" + valueList.get(i) + ")", valueList.get(i));
                SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
                renderer.setColor(chartColors[(series.getItemCount() - 1) % chartColors.length]);
                defaultRenderer.addSeriesRenderer(renderer);
            }
        }
        GraphicalView chartView = ChartFactory.getPieChartView(getActivity(), series,
                defaultRenderer);
        defaultRenderer.setClickEnabled(true);
        defaultRenderer.setSelectableBuffer(10);
        return chartView;
    }

    @Override
    public void notifyDataChanged() {
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                checkLoadingView(false, true);
                setSpinnerAdapter();
            }
        });
    }

    public static class EventDetailListAdapter extends BaseAdapter {
        private Context mContext;

        private LayoutInflater mInflater;

        private final String mEventName;

        private final ArrayList<String> mName = new ArrayList<String>();

        private final ArrayList<Long> mValue = new ArrayList<Long>();

        private long mTotalCount;

        public void clearData() {
            mName.clear();
            mValue.clear();
            mTotalCount = 0;
            notifyDataSetChanged();
        }

        public void setListContent(final String action) {
            final EventDetailed detailed = SharedData.getEventDetailedData(mEventName);
            if (detailed == null)
                return;
            final Map<String, Long> parameters = detailed.getParameters(action);
            if (parameters == null)
                return;
            Iterator<String> iter = parameters.keySet().iterator();
            mName.clear();
            mValue.clear();
            while (iter.hasNext()) {
                final String key = iter.next();
                final Long value = parameters.get(key);
                if (value == null || key == null)
                    continue;
                mName.add(key);
                mValue.add(value);
                mTotalCount += value;
            }
            notifyDataSetChanged();
        }

        public EventDetailListAdapter(Context context, String eventName) {
            mContext = context;
            mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mEventName = eventName;
        }

        @Override
        public int getCount() {
            return mName.size();
        }

        @Override
        public String getItem(int arg0) {
            return mName.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.event_detail_fragment_list_items, null);
                holder.mName = (TextView)convertView.findViewById(R.id.name);
                holder.mValue = (TextView)convertView.findViewById(R.id.value);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.mName.setText(mName.get(position));
            long value = mValue.get(position);
            int percentage = 0;
            if (mTotalCount == 0) {
                percentage = 0;
            } else {
                percentage = (int)((value / (float)mTotalCount) * 100);
            }
            holder.mValue.setText(mValue.get(position).toString() + "( " + percentage + "% )");
            return convertView;
        }

        private static class ViewHolder {
            TextView mName;

            TextView mValue;
        }
    }

    @Override
    public void askToReload() {
        setSpinnerAdapter();
        mEventDetailListAdapter.clearData();
        checkLoadingView(true, true);
        new EventDetailedLoadingHelper(getActivity(), mProjectKey, mEventName, this).execute();
    }
}
