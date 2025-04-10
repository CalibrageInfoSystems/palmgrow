package com.cis.palm360.areaextension;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cis.palm360.R;
import com.cis.palm360.common.CommonConstants;
import com.cis.palm360.common.CommonUtils;
import com.cis.palm360.cropmaintenance.CommonUtilsNavigation;
import com.cis.palm360.database.DataAccessHandler;
import com.cis.palm360.database.DatabaseKeys;
import com.cis.palm360.database.Queries;
import com.cis.palm360.datasync.helpers.DataManager;
import com.cis.palm360.dbmodels.FollowUp;
import com.cis.palm360.farmersearch.SearchFarmerScreen;
import com.cis.palm360.uihelper.MonthYearPickerDialog;
import com.cis.palm360.utils.UiUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */

//to take conversion potential score
public class ConversionPotentialFragment extends Fragment  {

    private static final String LOG_TAG = ConversionPotentialFragment.class.getName();

    private EditText mainissuecurrentcropEdit, commentsEdit, harverstmonthofcurrentcropEdit,expectedmonthofshowing;
    private Spinner potentialscoreSpin, farmerReadytoConverSpin;
    private Button converPotentialsaveBtn;
    private View rootView;
    private String mainIssueCurrentCrop, comments, harvestMonthOfCurrentCrop,expectedmonthofshowing_str, farmerreadytoConvertCode, potentialScoreCode, readytoconverSpnrValue;
    private ActionBar actionBar;
    private LinearLayout parentLayout, oldRecordLayout,expectedmonthshowing_LL;
    private String fromwhichScreen = " ";
    private FollowUp followUp;
    private UpdateUiListener updateUiListener;
    private DatePickerDialog.OnDateSetListener datePickerListener,harvestingDatePickListener;
    private boolean isFromUpdateUi = false;
    private TextView commentsTxt, hmccTxt, conversionPotentialScoreTxt, isFarmerReadyToConvert, mainIssueTxt;
    private DataAccessHandler dataAccessHandler = null;
    boolean isFirstTime = false;
    MonthYearPickerDialog newFragment = null;
    private String farmerReadytoConverSpin_value;
    private DateFormat inputFormat = new SimpleDateFormat("MM/yyyy");
    private  DateFormat outputFormat = new SimpleDateFormat("yyyy-MM");
    private boolean bindData = false;

    public void setUpdateUiListener(UpdateUiListener updateUiListener) {
        this.updateUiListener = updateUiListener;
    }

    public ConversionPotentialFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_conversionpotential, container, false);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        actionBar = activity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(activity.getResources().getString(R.string.conversion_potential_and_notes));

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            fromwhichScreen = bundle.getString("whichScreen", null);
        }
        dataAccessHandler = new DataAccessHandler(getActivity());
        initializeUI();

        followUp = (FollowUp) DataManager.getInstance().getDataFromManager(DataManager.PLOT_FOLLOWUP);

        if (followUp == null && CommonUtils.isFromFollowUp()) {
            followUp = (FollowUp) dataAccessHandler.getFollowupData(Queries.getInstance().getFollowUpBinding(CommonConstants.PLOT_CODE), 0);
            try {
                bindLastRecordData();
            } catch (Exception e) {
                e.printStackTrace();
                UiUtils.showCustomToastMessageLong("Follow Up Data Is Not Available So Click on Reset Data To Get Follow Up Data", getActivity(), 1, Toast.LENGTH_LONG);
            }
        } else {
            followUp = (FollowUp) DataManager.getInstance().getDataFromManager(DataManager.PLOT_FOLLOWUP);
            boolean recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(DatabaseKeys.TABLE_FOLLOWUP, "PlotCode", CommonConstants.PLOT_CODE));
            if (followUp != null) {
                try {
                    bindData();
                } catch (Exception e) {
                    e.printStackTrace();
                    UiUtils.showCustomToastMessageLong("Follow Up Data Is Not Available So Click on Reset Data To Get Follow Up Data", getActivity(), 1, Toast.LENGTH_LONG);

                }
                isFromUpdateUi = true;
            } else {
                followUp = new FollowUp();
            }
            if (recordExisted) {
                try {
                    bindLastRecordData();
                } catch (Exception e) {
                    e.printStackTrace();
                    UiUtils.showCustomToastMessageLong("Follow Up Data Is Not Available So Click on Reset Data To Get Follow Up Data", getActivity(), 1, Toast.LENGTH_LONG);

                }
            }
        }

        return rootView;
    }

    private void bindLastRecordData()throws Exception {
        oldRecordLayout.setVisibility(View.VISIBLE);
        mainIssueTxt.setText("" + followUp.getIssuedetails());
        commentsTxt.setText("" + followUp.getComments());
        hmccTxt.setText("" + followUp.getHarvestingmonth());
        conversionPotentialScoreTxt.setText(""+followUp.getPotentialscore());
        isFarmerReadyToConvert.setText((followUp.getIsfarmerreadytoconvert() == 0) ? "No" : "Yes");
    }

    private void initializeUI() {
        parentLayout = (LinearLayout) rootView.findViewById(R.id.parent_layout);
        expectedmonthshowing_LL = (LinearLayout) rootView.findViewById(R.id.expectedmonthshowing_LL);
        harverstmonthofcurrentcropEdit = (EditText) rootView.findViewById(R.id.harverstmonthofcurrentcropEdit);
        expectedmonthofshowing = (EditText) rootView.findViewById(R.id.expectedmonthofshowing);
        farmerReadytoConverSpin = (Spinner) rootView.findViewById(R.id.farmerReady_converSpin);
        potentialscoreSpin = (Spinner) rootView.findViewById(R.id.potentialscoreSpin);
        commentsEdit = (EditText) rootView.findViewById(R.id.commentsEdit);
        mainissuecurrentcropEdit = (EditText) rootView.findViewById(R.id.mainissuecurrentcropEdit);
        converPotentialsaveBtn = (Button) rootView.findViewById(R.id.conersionPotentialSaveBtn);
        commentsTxt = (TextView) rootView.findViewById(R.id.commentsTxt);
        hmccTxt = (TextView) rootView.findViewById(R.id.hmccTxt);
        conversionPotentialScoreTxt = (TextView) rootView.findViewById(R.id.conversionPotentialScoreTxt);
        isFarmerReadyToConvert = (TextView) rootView.findViewById(R.id.isFarmerReadyToConvert);
        mainIssueTxt = (TextView) rootView.findViewById(R.id.mainIssueTxt);
        oldRecordLayout = (LinearLayout) rootView.findViewById(R.id.oldRecordLayout);

        if (fromwhichScreen.equalsIgnoreCase("conversionpersonalhomepage")) {
            mainissuecurrentcropEdit.setEnabled(false);
            commentsEdit.setEnabled(false);
            harverstmonthofcurrentcropEdit.setEnabled(false);
        }

        parentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CommonUtilsNavigation.hideKeyBoard(getActivity());
                return false;
            }
        });

        harverstmonthofcurrentcropEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(!harverstmonthofcurrentcropEdit.getText().toString().equalsIgnoreCase("")){
                        String CurrentString = harverstmonthofcurrentcropEdit.getText().toString();
                        String[] separated = CurrentString.split("/");
                        newFragment = new MonthYearPickerDialog();
                        newFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
                        newFragment.setListener(datePickerListener, separated[0], separated[1]);
                    }else{
                        newFragment = new MonthYearPickerDialog();
                        newFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
                        newFragment.setListener(datePickerListener,null,null);

                    }

                }
              

        });
        expectedmonthofshowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!expectedmonthofshowing.getText().toString().equalsIgnoreCase("")){
                    String CurrentString = expectedmonthofshowing.getText().toString();
                    String[] separated = CurrentString.split("/");
                    newFragment = new MonthYearPickerDialog();
                    newFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
                    newFragment.setListener(harvestingDatePickListener, separated[0], separated[1]);
                }else{
                    newFragment = new MonthYearPickerDialog();
                    newFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
                    newFragment.setListener(harvestingDatePickListener,null,null);

                }
            }
        });
        converPotentialsaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {

                    if (farmerReadytoConverSpin_value.equalsIgnoreCase("Yes")){

                        if ((SearchFarmerScreen.FarmerImage == true) || (null !=DataManager.getInstance().getDataFromManager(DataManager.FILE_REPOSITORY))){
                            //Log.d("ReadyconvertValueyes", farmerReadytoConverSpin.getSelectedItemPosition() +"Yes"+ farmerReadytoConverSpin_value);
                            saveConversionData();
                        }else {
                            UiUtils.showCustomToastMessage("Please Take Farmer Photo  ",getActivity(),1);
                        }
                    }else {
                        if (potentialscoreSpin.getSelectedItemPosition() == 10){
                            UiUtils.showCustomToastMessage("Conversion Potential Score Should Be Below 9 ",getActivity(),1);
                        }else {
                            saveConversionData();
                            //Log.d("ReadyconvertValueyes", farmerReadytoConverSpin.getSelectedItemPosition() +"No"+ farmerReadytoConverSpin_value);
                        }
                    }
                }
            }
        });


        datePickerListener = (view, selectedDay, selectedMonth, selectedYear) -> {
            Calendar calendar = Calendar.getInstance();
//                if (calendar.get(Calendar.YEAR) == selectedYear && selectedMonth + 1 <= calendar.get(Calendar.MONTH)) {
//                    UiUtils.showCustomToastMessageLong("Invalid Month", getActivity(), 0);
//                } else {
                calendar.set(selectedYear, selectedMonth+1, selectedDay, 0, 0, 0);
                harverstmonthofcurrentcropEdit.setText(new SimpleDateFormat("MM/yyyy").format(calendar.getTime()));

//                }
        };
        harvestingDatePickListener = (view, selectedDay, selectedMonth, selectedYear) -> {
            Calendar calendar = Calendar.getInstance();
//                if (calendar.get(Calendar.YEAR) == selectedYear && selectedMonth + 1 <= calendar.get(Calendar.MONTH)) {
//                    UiUtils.showCustomToastMessageLong("Invalid Month", getActivity(), 0);
//                } else {
            calendar.set(selectedYear, selectedMonth+1, selectedDay, 0, 0, 0);
            expectedmonthofshowing.setText(new SimpleDateFormat("MM/yyyy").format(calendar.getTime()));
//                }
        };
        farmerReadytoConverSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                farmerReadytoConverSpin_value = farmerReadytoConverSpin.getSelectedItem().toString();
                if(farmerReadytoConverSpin_value.equalsIgnoreCase("Yes"))
                {
                    expectedmonthshowing_LL.setVisibility(View.VISIBLE);
                    potentialscoreSpin.setSelection(10);
                    potentialscoreSpin.setFocusable(false);
                    potentialscoreSpin.setEnabled(false);
                }
                else  if(farmerReadytoConverSpin_value.equalsIgnoreCase("No")){
                    potentialscoreSpin.setFocusable(true);
                    potentialscoreSpin.setEnabled(true);
                    expectedmonthshowing_LL.setVisibility(View.GONE);

                    if (bindData){

                    }else {
                        potentialscoreSpin.setSelection(0);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    private void bindData() throws Exception {
        bindData = true;
        mainissuecurrentcropEdit.setText("" + followUp.getIssuedetails());
        commentsEdit.setText("" + followUp.getComments());
        harverstmonthofcurrentcropEdit.setText("" + followUp.getHarvestingmonth());
        try {
            potentialscoreSpin.setSelection(followUp.getPotentialscore());
            farmerReadytoConverSpin.setSelection((followUp.getIsfarmerreadytoconvert() == 1) ? 1 : 2);
            if(followUp.getIsfarmerreadytoconvert() == 1){

                expectedmonthofshowing.setText(""+followUp.getExpectedMonthofSowing());

            }

        }
        catch (Exception e)
        {
            potentialscoreSpin.setSelection(0);
            farmerReadytoConverSpin.setSelection(1);


        }

    }

    private void saveConversionData() {
//        followUp = new FollowUp();
        followUp.setIssuedetails(mainIssueCurrentCrop);
        followUp.setComments(comments);
       /* Date date = null;
        try {
            date = inputFormat.parse(harverstmonthofcurrentcropEdit.getText().toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String lastDate = outputFormat.format(date);*/
        followUp.setHarvestingmonth(harverstmonthofcurrentcropEdit.getText().toString());
        followUp.setPotentialscore(potentialscoreSpin.getSelectedItemPosition());
        followUp.setIsfarmerreadytoconvert((farmerReadytoConverSpin.getSelectedItemPosition() == 1) ? 1 : 0);
        followUp.setExpectedMonthofSowing(expectedmonthofshowing.getText().toString());
        //Log.d("ReadytoConvert",  followUp.getIsfarmerreadytoconvert() + "");
        DataManager.getInstance().addData(DataManager.PLOT_FOLLOWUP, followUp);
        updateUiListener.updateUserInterface(0);
        getFragmentManager().popBackStack();
    }

    private boolean validate() {
        mainIssueCurrentCrop = mainissuecurrentcropEdit.getText().toString().trim();
        comments = commentsEdit.getText().toString();
        harvestMonthOfCurrentCrop = harverstmonthofcurrentcropEdit.getText().toString();
        expectedmonthofshowing_str = expectedmonthofshowing.getText().toString();
//        if (TextUtils.isEmpty(mainIssueCurrentCrop)) {
//            UiUtils.showCustomToastMessageLong(getActivity().getResources().getString(R.string.error_issueofcurrentcrop), getActivity(), 1);
//            mainissuecurrentcropEdit.requestFocus();
//            return false;
//        }
        if (CommonUtils.isEmptySpinner(farmerReadytoConverSpin)) {
            UiUtils.showCustomToastMessage("Please Select Farmer Is Ready To Convert or Not", getActivity(), 1);
            return false;
        }

        if (CommonUtils.isEmptySpinner(potentialscoreSpin)) {
            UiUtils.showCustomToastMessage("Please Select Conversion Potential Score", getActivity(), 1);
            return false;
        }

        if (TextUtils.isEmpty(harvestMonthOfCurrentCrop)) {
            UiUtils.showCustomToastMessage("Please Select Harvesting Month Of Current Crop", getActivity(), 1);
            harverstmonthofcurrentcropEdit.requestFocus();
            return false;
        }

        if (farmerReadytoConverSpin.getSelectedItemPosition() == 1 && TextUtils.isEmpty(expectedmonthofshowing_str)) {
            UiUtils.showCustomToastMessage("Please Select Expected Month Of Sowing ", getActivity(), 1);
            expectedmonthofshowing.requestFocus();
            return false;
        }


        if (TextUtils.isEmpty(commentsEdit.getText().toString())) {
            UiUtils.showCustomToastMessage("Please Enter Comments", getActivity(), 1);
            commentsEdit.requestFocus();
            return false;
        }
        return true;
    }

}
