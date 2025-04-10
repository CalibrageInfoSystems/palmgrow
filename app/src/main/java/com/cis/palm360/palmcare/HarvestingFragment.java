package com.cis.palm360.palmcare;

import static com.cis.palm360.ui.MainLoginScreen.LOG_TAG;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cis.palm360.R;
import com.cis.palm360.cloudhelper.ApplicationThread;
import com.cis.palm360.database.DataAccessHandler;
import com.cis.palm360.database.Queries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HarvestingFragment extends Fragment {
    private EditText searchEditText;
    private android.widget.ProgressBar searchProgressBar;
    private ImageView clearSearchImageView;
    private TextView noDataTextView;
    String to_date, from_date;
    public static final int LIMIT = 30;
    private int offset;
    private List<NotVisitedPlotsInfo> notVisitedplotsInfoList = new ArrayList<>();
    private NoVisitsInfoAdapter noVisitsDetailsRecyclerAdapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView novisitplot_list;
    private DataAccessHandler dataAccessHandler;
    String searchKey = "";

    private boolean hasMoreItems = true;
    private boolean isSearch = false;
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            com.cis.palm360.cloudhelper.Log.d("WhatisinSearch", "is :"+ s);
            //
            offset = 0;
            ApplicationThread.uiPost(LOG_TAG, "search", new Runnable() {
                @Override
                public void run() {
                    doSearch(s.toString().trim());
                    if (s.toString().length() > 0) {
                        clearSearchImageView.setVisibility(View.VISIBLE);
                    } else {
                        clearSearchImageView.setVisibility(View.GONE);
                    }
                }
            }, 100);
        }

        @Override
        public void afterTextChanged(final Editable s) {

        }
    };

    public void doSearch(String searchQuery) {
        com.cis.palm360.cloudhelper.Log.d("DoSearchQuery", "is :" +  searchQuery);
        offset = 0;
        hasMoreItems = true;
        if (searchQuery !=null &  !TextUtils.isEmpty(searchQuery)  & searchQuery.length()  > 0) {

            offset = 0;
            isSearch = true;
            searchKey = searchQuery.trim();
            notvisitedplotslist(offset, from_date, to_date);
        } else {
            searchKey = "";
            isSearch = false;
            notvisitedplotslist(offset, from_date, to_date);
        }
    }

    private void notvisitedplotslist(final int offset, String from_date, String to_date) {
        //ProgressBar.showProgressBar(this, "Please wait...");

        if (searchProgressBar != null) {
            searchProgressBar.setVisibility(View.VISIBLE);
        }
        ApplicationThread.bgndPost(LOG_TAG, "notvisitedplots", new Runnable() {
            @Override
            public void run() {

                notVisitedplotsInfoList = (List<NotVisitedPlotsInfo>) dataAccessHandler.getNotvisitedplotInfo(Queries.getInstance().getnotvisitedpoltlistforHarvestingVist(LIMIT, offset,from_date, to_date, searchKey), 1);
                Collections.reverse(notVisitedplotsInfoList);

                ApplicationThread.uiPost(LOG_TAG, "", new Runnable() {
                    @Override
                    public void run() {
                        //ProgressBar.hideProgressBar();
                        searchProgressBar.setVisibility(View.GONE);
                        noVisitsDetailsRecyclerAdapter = new NoVisitsInfoAdapter(getActivity(), notVisitedplotsInfoList);
                        if (notVisitedplotsInfoList != null && !notVisitedplotsInfoList.isEmpty()) {
                            novisitplot_list.setVisibility(View.VISIBLE);
                            noDataTextView.setVisibility(View.GONE);
                            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            novisitplot_list.setLayoutManager(layoutManager);

                            novisitplot_list.setAdapter(noVisitsDetailsRecyclerAdapter);
                            //   setTitle(alert_type, offset == 0 ? alertsVisitsInfoList.size() : offset);
                        }
                        else{
                            novisitplot_list.setVisibility(View.GONE);
                            noDataTextView.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        });
    }


    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_harvest, container, false);

        // Initialize views
        novisitplot_list = view.findViewById(R.id.novisitplot_list);
        searchEditText = view.findViewById(R.id.searchtext);
        searchProgressBar = view.findViewById(R.id.searchprogrescrop);
        clearSearchImageView = view.findViewById(R.id.clearsearch);
        noDataTextView = view.findViewById(R.id.no_text);
        searchProgressBar = (android.widget.ProgressBar) view.findViewById(R.id.searchprogress);
        dataAccessHandler = new DataAccessHandler(getActivity());
        // Set up RecyclerView
        novisitplot_list.setLayoutManager(new LinearLayoutManager(getContext()));
        // Add your adapter to the RecyclerView
        // recyclerView.setAdapter(yourAdapter);

        // Set up search functionality and other view interactions here
        clearSearchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSearch = false;
                notVisitedplotsInfoList.clear();
                searchEditText.setText("");
            }
        });

        searchEditText.addTextChangedListener(mTextWatcher);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        from_date = sdf.format(new Date());
        to_date = sdf.format(new Date());
        Log.e("==From & todates",from_date + to_date);
        notvisitedplotslist(offset,from_date,to_date);
        return view;
    }

    private BroadcastReceiver mNotificationReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceive(Context context, Intent intent) {

            to_date = intent.getStringExtra("todate");
            from_date = intent.getStringExtra("fromdate");
            offset = offset + LIMIT;
            com.cis.palm360.uihelper.ProgressBar.showProgressBar(getActivity(), "Please wait...");
            notVisitedplotsInfoList = (List<NotVisitedPlotsInfo>) dataAccessHandler.getNotvisitedplotInfo(Queries.getInstance().getnotvisitedpoltlistforHarvestingVist(LIMIT, offset,from_date,to_date, searchKey), 1);
            Collections.reverse(notVisitedplotsInfoList);

            ApplicationThread.uiPost(LOG_TAG, "", new Runnable() {
                @Override
                public void run() {
                    com.cis.palm360.uihelper.ProgressBar.hideProgressBar();
                    noVisitsDetailsRecyclerAdapter = new NoVisitsInfoAdapter(getActivity(), notVisitedplotsInfoList);
                    if (notVisitedplotsInfoList != null && !notVisitedplotsInfoList.isEmpty() && notVisitedplotsInfoList.size()!= 0) {
                        novisitplot_list.setVisibility(View.VISIBLE);
                        noDataTextView.setVisibility(View.GONE);
                        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        novisitplot_list.setLayoutManager(layoutManager);

                        novisitplot_list.setAdapter(noVisitsDetailsRecyclerAdapter);
                        //   setTitle(alert_type, offset == 0 ? alertsVisitsInfoList.size() : offset);
                    }
                    else{
                        novisitplot_list.setVisibility(View.GONE);
                        noDataTextView.setVisibility(View.VISIBLE);
                    }
                }
            });


            //  registerReceiver();
        }
    };
    @Override
    public void onResume() {
        super.onResume();
        getContext().registerReceiver(mNotificationReceiver, new IntentFilter("KEY"));
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unregisterReceiver(mNotificationReceiver);
    }
    // Other fragment methods and logic
}
