package com.cis.palm360.cropmaintenance;


import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.cis.palm360.R;
import com.cis.palm360.areaextension.UpdateUiListener;
import com.cis.palm360.common.CommonConstants;
import com.cis.palm360.common.CommonUtils;
import com.cis.palm360.conversion.PalmDetailsEditListener;
import com.cis.palm360.database.DataAccessHandler;
import com.cis.palm360.database.Queries;
import com.cis.palm360.datasync.helpers.DataManager;
import com.cis.palm360.dbmodels.CropMaintenanceDocs;
import com.cis.palm360.dbmodels.Fertilizer;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.cis.palm360.cropmaintenance.CommonUtilsNavigation.getKey;

import org.apache.commons.lang3.ArrayUtils;

//Used to recommend fertilizer during crop maintenance
//public class FertilizerFragment extends Fragment implements View.OnClickListener, PalmDetailsEditListener, UpdateUiListener, OnPageChangeListener, OnLoadCompleteListener {
//    private static final String LOG_TAG = FertilizerFragment.class.getName();
//    private String Monthnumber;
//    private ArrayList<Fertilizer> mFertilizerModelArray = new ArrayList<>();
//    private View rootView;
//    private Spinner sourceOfertilizerSpin,psourceOfertilizerSpin,bioFertilizerSpin,bioFertilizerSpin2, fertilizerProductNameSpin, uomSpin, frequencyOfApplicationSpin,fertilizerapplied,apptype,papptype,pfertilizerapplied;
//    private EditText dosageGivenEdt, lastAppliedDateEdt,dosageGivenEdt1, lastAppliedDateEdt1,dosageGivenEdt2, lastAppliedDateEdt2,
//            dosageGivenEdt3, lastAppliedDateEdt3,dosageGivenEdt4, lastAppliedDateEdt4,dosageGivenEdt5, lastAppliedDateEdt5,dosageGivenEdt6, lastAppliedDateEdt6, dosageGivenEdt7,lastAppliedDateEdt7,dosageGivenEdt8,lastAppliedDateEdt8,
//            dialog_otherEdt, otherEdt,comments,pcomments, sourceName,psourceName,Monthyear,pMonthyear;
//    private LinearLayout otherLay, headerLL;
//    private FragmentManager mFragmentManager;
//    private FragmentTransaction mFragmentTransaction;
//    private DateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
//    private DateFormat inputFormatYYMMDD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    private ArrayList<String> months;
//    Bundle args = new Bundle();
//    private String applydate=null ;
//    private String papplydate=null ;
//
//    private String selectedbiofertilizer1;
//    private String selectedbiofertilizer2;
//
//    private ArrayList<Fertilizer> fertilizernonbiolastvisitdatamap;
//    private ArrayList<Fertilizer> fertilizerbiolastvisitdatamap;
//
//    private String Month =null;
//    private  int Quater;
//    private int Year ;
//    private String pMonth =null;
//    private  int pQuater;
//    private int pYear ;
//    private int cy,cm,cq,py,pm,pq,caly,pcaly;
//    private long mindate,maxdate;
//    private long ppmindate,ppmaxdate;
//    private String pmindate,pmaxdate;
//    private String cmindate,cmaxdate;
//    private TextView tv3,tv4;
//    int bioFertilizerId=0;
//    int bioFertilizerId2=0;
//    private   String CMCode = "" ;
//    AdapterView.OnItemSelectedListener spinListener = new AdapterView.OnItemSelectedListener() {
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            switch (parent.getId()) {
//
//                case R.id.fertilizerProductNameSpin:
//                    if (fertilizerProductNameSpin.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.other))) {
////                        dialog.show();
//                        otherLay.setVisibility(View.VISIBLE);
//                    } else {
//                        otherLay.setVisibility(View.GONE);
//                    }
//
//                    break;
//            }
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//        }
//    };
//    private RecyclerView fertilizerList;
//    private Button saveBtn,historyBtn, pdfBtn;
//    private LinkedHashMap<String, String> fertilizerDataMap,bioDataMap, fertilizerTypeDataMap, uomDataMap, frequencyOfApplicationDataMap,IsAppliedDataMap,AppTypeDataMap, bioDataMap2;
//    private DataAccessHandler dataAccessHandler;
//    private GenericTypeAdapter fertilizerDataAdapter;
//    private Context mContext;
//
//    private UpdateUiListener updateUiListener;
//    private Button complaintsBtn;
//    private ActionBar actionBar;
//    private Button btnRecommnd;
//    private int cal = 99;
//    Toolbar toolbar;
//
//    File fileToDownLoad;
//    CropMaintenanceDocs cropMaintenanceDocs;
//    public FertilizerFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        rootView = inflater.inflate(R.layout.fertilizerdetailsfrag, container, false);
//        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
//        AppCompatActivity activity = (AppCompatActivity) getActivity();
//        activity.setSupportActionBar(toolbar);
//        actionBar = activity.getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setTitle(getActivity().getResources().getString(R.string.fertilizerApplication));
//
//        mContext = getActivity();
//        dataAccessHandler = new DataAccessHandler(getActivity());
//
//        cropMaintenanceDocs = (CropMaintenanceDocs) dataAccessHandler.getCMDocsData(Queries.getInstance().getFertilizerPDFfile(), 0);
//
//        if (cropMaintenanceDocs != null) {
//
//            fileToDownLoad = new File(CommonUtils.get3FFileRootPath() + "3F_CMDocs/" + cropMaintenanceDocs.getFileName() + cropMaintenanceDocs.getFileExtension());
//        }
//
//        setHasOptionsMenu(true);
//        initViews();
//        setViews();
//
//        bindData();
//
//        btnRecommnd=(Button) rootView.findViewById(R.id.btnRecommnd);
//        btnRecommnd.setOnClickListener(v -> {
//            RecomndFertilizerFragment mRecomNDScreen = new RecomndFertilizerFragment();
//            String backStateName = mRecomNDScreen.getClass().getName();
//            mRecomNDScreen.setArguments(args);
//            mFragmentManager = getActivity().getSupportFragmentManager();
//            mFragmentTransaction = mFragmentManager.beginTransaction();
//            mFragmentTransaction.setCustomAnimations(
//                    R.anim.enter_from_right,0,0, R.anim.exit_to_left
//            );
//            mFragmentTransaction.replace(android.R.id.content, mRecomNDScreen);
//            mFragmentTransaction.addToBackStack(backStateName);
//            mFragmentTransaction.commit();
//        });
//
//        return rootView;
//    }
//
//
//    private void bindData() {
//        mFertilizerModelArray = (ArrayList<Fertilizer>) DataManager.getInstance().getDataFromManager(DataManager.FERTILIZER);
//        if (mFertilizerModelArray != null) {
//
//            fertilizerapplied.setSelection(mFertilizerModelArray.get(0).getIsFertilizerApplied() == null ? 0 : mFertilizerModelArray.get(0).getIsFertilizerApplied() == 1 ? 1 : 2);
//            Monthyear.setText(mFertilizerModelArray.get(0).getApplicationMonth() == null ? "" : mFertilizerModelArray.get(0).getApplicationMonth());
//            sourceName.setText(mFertilizerModelArray.get(0).getSourceName() == null ? "" : mFertilizerModelArray.get(0).getSourceName());
//            Log.d("sourceOfertilizerSpin",mFertilizerModelArray.get(0).getFertilizersourcetypeid() + "" );
//            Log.d("apptype",mFertilizerModelArray.get(0).getApplicationType()  + "" );
//            sourceOfertilizerSpin.setSelection(mFertilizerModelArray.get(0).getFertilizersourcetypeid() == null ? 0 : CommonUtilsNavigation.getvalueFromHashMap(fertilizerDataMap, mFertilizerModelArray.get(0).getFertilizersourcetypeid()));
//            comments.setText(mFertilizerModelArray.get(0).getComments() == null ? "" : mFertilizerModelArray.get(0).getComments());
//
//            if (mFertilizerModelArray.get(0).getApplicationType() != null) {
//                String apptypedesc = mFertilizerModelArray.get(0).getApplicationType();
//                Log.d("apptype", apptypedesc);
//                //int apptypeid = dataAccessHandler.getOnlyOneIntValueFromDb(Queries.getInstance().getapptypeId(apptypedesc));
//                apptype.setSelection(mFertilizerModelArray.get(0).getApplicationType() == null ? 0 : CommonUtilsNavigation.getvalueFromHashMap(AppTypeDataMap, Integer.parseInt(apptypedesc)));
//            }
//
//
////            int arraySize = mFertilizerModelArray.size();
////            int[] integerArray = new int[arraySize]; // Creating an integer array of the specified size
////
////            double dosage = 0.0;
////
////            for (int i = 0; i< arraySize; i++){
////                integerArray[i] = mFertilizerModelArray.get(i).getFertilizerid();
////                Log.d("integerArray", integerArray[i] + "");
////                 dosage = mFertilizerModelArray.get(i).getDosage();
////
////                for (int j=0; j< integerArray.length; j++){
////                    if (contains(integerArray, 59)){
////                        int intValue = (int) dosage;
////                        dosageGivenEdt.setText(String.valueOf(intValue));
////                    }
////                    if (contains(integerArray, 61)){
////                        int intValue = (int) dosage;
////                        dosageGivenEdt1.setText(String.valueOf(intValue));
////                    }
////                }
////            }
//
//            int dosage1 = getDosageForFertilizerId(mFertilizerModelArray, 58);
//            dosageGivenEdt5.setText(String.valueOf(dosage1));
//
//            int dosage2 = getDosageForFertilizerId(mFertilizerModelArray, 59);
//            dosageGivenEdt.setText(String.valueOf(dosage2));
//
//            int dosage3 = getDosageForFertilizerId(mFertilizerModelArray, 60);
//            dosageGivenEdt2.setText(String.valueOf(dosage3));
//
//            int dosage4 = getDosageForFertilizerId(mFertilizerModelArray, 61);
//            dosageGivenEdt1.setText(String.valueOf(dosage4));
//
//            int dosage5 = getDosageForFertilizerId(mFertilizerModelArray, 63);
//            dosageGivenEdt3.setText(String.valueOf(dosage5));
//
//            int dosage6 = getDosageForFertilizerId(mFertilizerModelArray, 64);
//            dosageGivenEdt4.setText(String.valueOf(dosage6));
//
//            int dosage7 = getDosageForFertilizerId(mFertilizerModelArray, 229);
//            dosageGivenEdt6.setText(String.valueOf(dosage7));
//
//        }
////        if (null == mFertilizerModelArray)
////            mFertilizerModelArray = new ArrayList<Fertilizer>();
////
////        fertilizerDataAdapter = new GenericTypeAdapter(getActivity(), mFertilizerModelArray, fertilizerTypeDataMap, uomDataMap, GenericTypeAdapter.TYPE_FERTILIZER);
////        fertilizerList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
////        fertilizerList.setAdapter(fertilizerDataAdapter);
////        fertilizerDataAdapter.setEditClickListener(this);
//    }
//    public boolean contains(final int[] array, final int key) {
//        return ArrayUtils.contains(array, key);
//    }
//
//    private int getDosageForFertilizerId(List<Fertilizer> mFertilizerModelArray, int fertilizerId) {
//        for (Fertilizer model : mFertilizerModelArray) {
//            if (model.getFertilizerid() == fertilizerId) {
//                return (int) model.getDosage();
//            }
//        }
//        return 0; // Return a default value if the fertilizer ID is not found.
//    }
//
//
//    private void initViews() {
//        sourceOfertilizerSpin = (Spinner) rootView.findViewById(R.id.sourceOfertilizerSpin);
//       // psourceOfertilizerSpin = (Spinner) rootView.findViewById(R.id.psourceOfertilizerSpin);
//        apptype = (Spinner) rootView.findViewById(R.id.apptype);
//        //papptype = (Spinner) rootView.findViewById(R.id.papptype);
//        fertilizerapplied = (Spinner) rootView.findViewById(R.id.FertilizerApplied);
//        //pfertilizerapplied = (Spinner) rootView.findViewById(R.id.pFertilizerApplied);
//        fertilizerProductNameSpin = (Spinner) rootView.findViewById(R.id.fertilizerProductNameSpin);
//        uomSpin = (Spinner) rootView.findViewById(R.id.uomSpin);
//       // frequencyOfApplicationSpin = (Spinner) rootView.findViewById(R.id.frequencyOfApplicationSpin);
//        dosageGivenEdt = (EditText) rootView.findViewById(R.id.dosageGivenEdt);
//        dosageGivenEdt1 = (EditText) rootView.findViewById(R.id.dosageGivenEdt1);
//        dosageGivenEdt2 = (EditText) rootView.findViewById(R.id.dosageGivenEdt2);
//        dosageGivenEdt3 = (EditText) rootView.findViewById(R.id.dosageGivenEdt3);
//        dosageGivenEdt4 = (EditText) rootView.findViewById(R.id.dosageGivenEdt4);
//        dosageGivenEdt5 = (EditText) rootView.findViewById(R.id.dosageGivenEdt5);
//        dosageGivenEdt6 = (EditText) rootView.findViewById(R.id.dosageGivenEdt6);
//        dosageGivenEdt7 = (EditText) rootView.findViewById(R.id.dosageGivenEdt7);
//        dosageGivenEdt8 = (EditText) rootView.findViewById(R.id.dosageGivenEdt8);
//        bioFertilizerSpin = (Spinner) rootView.findViewById(R.id.bioFertilizerSpin);
//        bioFertilizerSpin2 = (Spinner) rootView.findViewById(R.id.bioFertilizerSpin2);
////        lastAppliedDateEdt = (EditText) rootView.findViewById(R.id.lastAppliedDateEdt);
////        lastAppliedDateEdt1 = (EditText) rootView.findViewById(R.id.lastAppliedDateEdt1);
////        lastAppliedDateEdt2 = (EditText) rootView.findViewById(R.id.lastAppliedDateEdt2);
////        lastAppliedDateEdt3 = (EditText) rootView.findViewById(R.id.lastAppliedDateEdt3);
////        lastAppliedDateEdt4 = (EditText) rootView.findViewById(R.id.lastAppliedDateEdt4);
////        lastAppliedDateEdt5 = (EditText) rootView.findViewById(R.id.lastAppliedDateEdt5);
////        lastAppliedDateEdt6 = (EditText) rootView.findViewById(R.id.lastAppliedDateEdt6);
////        lastAppliedDateEdt7 = (EditText) rootView.findViewById(R.id.lastAppliedDateEdt7);
////        lastAppliedDateEdt8 = (EditText) rootView.findViewById(R.id.lastAppliedDateEdt8);
//        comments = (EditText) rootView.findViewById(R.id.commentsTv);
//        //pcomments = (EditText) rootView.findViewById(R.id.pcommentsTv);
//        sourceName = (EditText) rootView.findViewById(R.id.nameofshopEv);
//        //psourceName = (EditText) rootView.findViewById(R.id.pnameofshopEv);
//        Monthyear = (EditText) rootView.findViewById(R.id.Monthyear);
//       // pMonthyear = (EditText) rootView.findViewById(R.id.pMonthyear);
//      //  lastAppliedDateEdt6 = (EditText) rootView.findViewById(R.id.lastAppliedDateEdt6);
//        otherEdt = (EditText) rootView.findViewById(R.id.otherEdt);
//        otherLay = (LinearLayout) rootView.findViewById(R.id.otherLay);
//        fertilizerList = (RecyclerView) rootView.findViewById(R.id.fertilizerList);
//        saveBtn = (Button) rootView.findViewById(R.id.saveBtn);
//        historyBtn = (Button) rootView.findViewById(R.id.historyBtn);
//        pdfBtn = (Button) rootView.findViewById(R.id.pdfBtn);
//        complaintsBtn = (Button) rootView.findViewById(R.id.complaintsBtn);
//        complaintsBtn.setVisibility(View.GONE);
//        complaintsBtn.setEnabled(false);
//        headerLL = (LinearLayout) rootView.findViewById(R.id.headerLL);
////        tv3 =  (TextView) rootView.findViewById(R.id.tv3);
////        tv4 =  (TextView) rootView.findViewById(R.id.id4);
 //
//        tv3 =  (TextView) rootView.findViewById(R.id.currentqtr);
//        tv4 =  (TextView) rootView.findViewById(R.id.prvsqtr);
//
//        if (cropMaintenanceDocs != null) {
//
//            if (null != fileToDownLoad && fileToDownLoad.exists()) {
//
//                pdfBtn.setVisibility(View.VISIBLE);
//
//            } else {
//                pdfBtn.setVisibility(View.GONE);
//            }
//        }
//
//        Calendar calendar = Calendar.getInstance();
//        cy = calendar.get(Calendar.YEAR);
//        cm = calendar.get(Calendar.MONTH);
//
//        if(cm>=0 && cm<=2){
//            cy=cy-1;
//
//            cq=4;
//            py=cy;
//            pq=3;
//            caly=cy+1;
//            pcaly=py;
//            calendar.clear();
//            calendar.set(caly, 0, 1);
//             mindate = calendar.getTimeInMillis();
//
//            calendar.clear();
//            calendar.set(caly, 2, 31);
//             maxdate = calendar.getTimeInMillis();
//
//            calendar.clear();
//            calendar.set(pcaly, 9, 1);
//            ppmindate = calendar.getTimeInMillis();
//
//            calendar.clear();
//            calendar.set(pcaly, 11, 31);
//            ppmaxdate = calendar.getTimeInMillis();
//            pm=8;
//             pmindate=String.valueOf(pcaly)+"-10-01";
//             pmaxdate=String.valueOf(pcaly)+"-12-31";
//            cmindate=String.valueOf(caly)+"-01-01";
//            cmaxdate=String.valueOf(caly)+"-03-31";
//        }else if (cm>=3 && cm<=5){
//            cq=1;
//            py=cy-1;
//            pq=4;
//            caly=cy;
//            pcaly=py+1;
//            calendar.clear();
//            calendar.set(caly, 3, 1);
//             mindate = calendar.getTimeInMillis();
//
//            calendar.clear();
//            calendar.set(caly, 5, 30);
//             maxdate = calendar.getTimeInMillis();
//
//            calendar.clear();
//            calendar.set(pcaly, 0, 1);
//            ppmindate = calendar.getTimeInMillis();
//
//            calendar.clear();
//            calendar.set(pcaly, 2, 31);
//            ppmaxdate = calendar.getTimeInMillis();
//            pm=0;
//            pmindate=String.valueOf(pcaly)+"-01-01";
//            pmaxdate=String.valueOf(pcaly)+"-03-31";
//            cmindate=String.valueOf(caly)+"-04-01";
//            cmaxdate=String.valueOf(caly)+"-06-30";
//        }else if (cm>=6 && cm<=8){
//            cq=2;
//            py=cy;
//            pq=1;
//            caly=cy;
//            pcaly=py;
//            calendar.clear();
//            calendar.set(caly, 6, 1);
//             mindate = calendar.getTimeInMillis();
//
//            calendar.clear();
//            calendar.set(caly, 8, 30);
//             maxdate = calendar.getTimeInMillis();
//
//            calendar.clear();
//            calendar.set(pcaly, 3, 1);
//            ppmindate = calendar.getTimeInMillis();
//
//            calendar.clear();
//            calendar.set(pcaly, 5, 30);
//            ppmaxdate = calendar.getTimeInMillis();
//            pm=3;
//            pmindate=String.valueOf(pcaly)+"-04-01";
//            pmaxdate=String.valueOf(pcaly)+"-06-30";
//            cmindate=String.valueOf(caly)+"-07-01";
//            cmaxdate=String.valueOf(caly)+"-09-30";
//        }else if (cm>=9 && cm<=11){
//            cq=3;
//            py=cy;
//            pq=2;
//            caly=cy;
//            pcaly=py;
//            calendar.clear();
//            calendar.set(caly, 9, 1);
//             mindate = calendar.getTimeInMillis();
//
//            calendar.clear();
//            calendar.set(caly, 11, 31);
//             maxdate = calendar.getTimeInMillis();
//
//            calendar.clear();
//            calendar.set(pcaly, 6, 1);
//            ppmindate = calendar.getTimeInMillis();
//
//            calendar.clear();
//            calendar.set(pcaly, 8, 30);
//            ppmaxdate = calendar.getTimeInMillis();
//            pm=6;
//            pmindate=String.valueOf(pcaly)+"-07-01";
//            pmaxdate=String.valueOf(pcaly)+"-09-30";
//            cmindate=String.valueOf(caly)+"-10-01";
//            cmaxdate=String.valueOf(caly)+"-12-31";
//        }
//        tv3.setText(String.valueOf(cq)+"-"+"Qtr"+"-"+String.valueOf(cy));
//        tv3.setText(String.valueOf(cq)+"-"+"Qtr"+"-"+String.valueOf(cy));
//
//
//        months = new ArrayList<>();
//        months.add("January");
//        months.add("February");
//        months.add("March");
//        months.add("April");
//        months.add("May");
//        months.add("June");
//        months.add("July");
//        months.add("August");
//        months.add("September");
//        months.add("October");
//        months.add("November");
//        months.add("December");
//        //complaintsBtn.setVisibility((CommonUiUtils.isComplaintsDataEntered()) ? View.GONE : View.VISIBLE);
//        Monthyear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
//                        .getInstance(cm, caly,mindate,maxdate);
//
//                dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(int year, int monthOfYear) {
//
//                        // do something
//
//                        Month = months.get(monthOfYear);
//                        Year =cy;
//                        Quater = cq;
//                        Monthnumber =String.valueOf(monthOfYear+1).length()==1? "0"+String.valueOf(monthOfYear+1) : String.valueOf(monthOfYear+1);
//                        Monthyear.setText(Month+"-"+String.valueOf(year));
//                        applydate = String.valueOf(year)+"-"+Monthnumber+"-01 00:00:00";
//
//                        //Toast.makeText(getActivity().getApplicationContext(),String.valueOf(year)+"-"+String.valueOf(monthOfYear),Toast.LENGTH_SHORT);
//                    }
//                });
//
//                dialogFragment.show(getActivity().getSupportFragmentManager(), null);
//
//
//            }
//
//        });
//
////        pMonthyear.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////                MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
////                        .getInstance(pm, pcaly,ppmindate,ppmaxdate);
////
////                dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
////                    @Override
////                    public void onDateSet(int year, int monthOfYear) {
////
////                        // do something
////
////                        pMonth = months.get(monthOfYear);
////                        pYear=cy;
////                        pQuater = pq;
////                        Monthnumber =String.valueOf(monthOfYear+1).length()==1? "0"+String.valueOf(monthOfYear+1) : String.valueOf(monthOfYear+1);
////                        pMonthyear.setText(pMonth+"-"+String.valueOf(year));
////                        papplydate = String.valueOf(year)+"-"+Monthnumber+"-01 00:00:00";
////
////                        //Toast.makeText(getActivity().getApplicationContext(),String.valueOf(year)+"-"+String.valueOf(monthOfYear),Toast.LENGTH_SHORT);
////                    }
////                });
////
////                dialogFragment.show(getActivity().getSupportFragmentManager(), null);
////
////
////            }
////
////        });
//
//
//        complaintsBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle dataBundle = new Bundle();
//                dataBundle.putBoolean(CommonConstants.KEY_NEW_COMPLAINT, true);
//                ComplaintDetailsFragment complaintDetailsFragment = new ComplaintDetailsFragment();
//                complaintDetailsFragment.setArguments(dataBundle);
//                complaintDetailsFragment.setUpdateUiListener(FertilizerFragment.this);
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .add(android.R.id.content, complaintDetailsFragment).addToBackStack(null)
//                        .commit();
//            }
//        });
//    }
//
//    private void fetchprevqtr(){
//        int yr=py;
//        int qr=pq;
//
//       DataAccessHandler dataAccessHandler = new DataAccessHandler(getActivity());
//
//
//        CMCode   =   dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getFertilizerPrevQtrCM(qr,yr,CommonConstants.PLOT_CODE,pmindate,pmaxdate));
//
//        if(CMCode!=null){
//            CommonConstants.Prev_Fertilizer_CMD=CMCode;
//            pMonthyear.setEnabled(false);
//            psourceName.setEnabled(false);
//            psourceOfertilizerSpin.setEnabled(false);
//            pfertilizerapplied.setEnabled(false);
//            pcomments.setEnabled(false);
//            papptype.setEnabled(false);
//            lastAppliedDateEdt.setEnabled(false);
//            lastAppliedDateEdt1.setEnabled(false);
//            lastAppliedDateEdt2.setEnabled(false);
//            lastAppliedDateEdt3.setEnabled(false);
//            lastAppliedDateEdt4.setEnabled(false);
//            lastAppliedDateEdt5.setEnabled(false);
//            lastAppliedDateEdt6.setEnabled(false);
//            lastAppliedDateEdt7.setEnabled(false);
//            lastAppliedDateEdt8.setEnabled(false);
//
//
//            lastAppliedDateEdt.setText(dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getFertilizerPrevQtrDosage(qr,yr,CMCode,59,pmindate,pmaxdate)));
//            lastAppliedDateEdt1.setText(dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getFertilizerPrevQtrDosage(qr,yr,CMCode,61,pmindate,pmaxdate)));
//            lastAppliedDateEdt2.setText(dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getFertilizerPrevQtrDosage(qr,yr,CMCode,60,pmindate,pmaxdate)));
//            lastAppliedDateEdt3.setText(dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getFertilizerPrevQtrDosage(qr,yr,CMCode,63,pmindate,pmaxdate)));
//            lastAppliedDateEdt4.setText(dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getFertilizerPrevQtrDosage(qr,yr,CMCode,64,pmindate,pmaxdate)));
//            lastAppliedDateEdt5.setText(dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getFertilizerPrevQtrDosage(qr,yr,CMCode,58,pmindate,pmaxdate)));
//            lastAppliedDateEdt6.setText(dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getFertilizerPrevQtrDosage(qr,yr,CMCode,229,pmindate,pmaxdate)));
//            lastAppliedDateEdt7.setText(dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getBioFertilizerPrevQtrDosage(qr,yr,CMCode,232,bioFertilizerId,pmindate,pmaxdate)));
//            lastAppliedDateEdt8.setText(dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getBioFertilizerPrevQtrDosage(qr,yr,CMCode,232,bioFertilizerId2,pmindate,pmaxdate)));
//            List<Fertilizer> fertilizerList = (List<Fertilizer>) dataAccessHandler.getFertilizerPrevQtrdtls(Queries.getInstance().getFertilizerPrevQtrdtls(qr,yr,CMCode,pmindate,pmaxdate),1);
//            if(fertilizerList.size()>0){
//
//                //psourceName.setText(fertilizerList.get(0).getSourceName());
//
//                if (fertilizerList.get(0).getSourceName().isEmpty() || fertilizerList.get(0).getSourceName() == null){
//
//                    psourceName.setText(fertilizerList.get(0).getSourceName());
//                }else{
//
//                    psourceName.setText("");
//                }
//
//                //pcomments.setText(fertilizerList.get(0).getComments());
//
//                if (fertilizerList.get(0).getComments().isEmpty() || fertilizerList.get(0).getComments() == null){
//
//                    pcomments.setText(fertilizerList.get(0).getComments());
//                }else{
//
//                    pcomments.setText("");
//                }
//
//                Set<String> keys = AppTypeDataMap.keySet();
//                int p=1;
//                if(fertilizerList.get(0).getApplicationType()!=null && !fertilizerList.get(0).getApplicationType().equals("null"))
//                {
//                    for(String k:keys){
//                        if(k.equals(fertilizerList.get(0).getApplicationType()))
//                        {
//                            break;
//                        }
//                        p++;
//                    }
//                    papptype.setSelection(p);
//                }else{
//                    papptype.setSelection(0);
//                }
//
//                if(fertilizerList.get(0).getFertilizersourcetypeid()!=null) {
//                    keys = fertilizerDataMap.keySet();
//                    p = 1;
//                    for (String k : keys) {
//                        if (k.equals(fertilizerList.get(0).getFertilizersourcetypeid().toString())) {
//                            break;
//                        }
//                        p++;
//                    }
//                    psourceOfertilizerSpin.setSelection(p);
//                }else
//                    psourceOfertilizerSpin.setSelection(0);
//
//
//                    keys = IsAppliedDataMap.keySet();
//                    p = 1;
//                    for (String k : keys) {
//                        if (k.equals(String.valueOf(fertilizerList.get(0).getIsFertilizerApplied()))) {
//                            break;
//                        }
//                        p++;
//                    }
//                    pfertilizerapplied.setSelection(p);
//
//                    if (fertilizerList.get(0).getApplicationMonth().contains("null") || fertilizerList.get(0).getApplicationMonth().isEmpty()){
//
//                        pMonthyear.setText("");
//                    }else{
//
//                        pMonthyear.setText(fertilizerList.get(0).getApplicationMonth()+"-"+String.valueOf(fertilizerList.get(0).getApplicationYear()));
//                    }
//
//
//
//            }
//        }else {
//            pMonthyear.setEnabled(true);
//            psourceName.setEnabled(true);
//            psourceOfertilizerSpin.setEnabled(true);
//            pfertilizerapplied.setEnabled(true);
//            pcomments.setEnabled(true);
//            papptype.setEnabled(true);
//            lastAppliedDateEdt.setEnabled(true);
//            lastAppliedDateEdt1.setEnabled(true);
//            lastAppliedDateEdt2.setEnabled(true);
//            lastAppliedDateEdt3.setEnabled(true);
//            lastAppliedDateEdt4.setEnabled(true);
//            lastAppliedDateEdt5.setEnabled(true);
//            lastAppliedDateEdt6.setEnabled(true);
//            lastAppliedDateEdt7.setEnabled(true);
//            lastAppliedDateEdt8.setEnabled(true);
//
//        }
//
//    }
//    private void setViews() {
//
//       // updateLabel();
//        saveBtn.setOnClickListener(this);
//        historyBtn.setOnClickListener(this);
//        pdfBtn.setOnClickListener(this);
////        lastAppliedDateEdt.setOnClickListener(this);
////        lastAppliedDateEdt1.setOnClickListener(this);
////        lastAppliedDateEdt2.setOnClickListener(this);
////        lastAppliedDateEdt3.setOnClickListener(this);
////        lastAppliedDateEdt4.setOnClickListener(this);
////        lastAppliedDateEdt5.setOnClickListener(this);
////        lastAppliedDateEdt6.setOnClickListener(this);
////        lastAppliedDateEdt7.setOnClickListener(this);
////        lastAppliedDateEdt8.setOnClickListener(this);
//
//        /*fertilizerProductNameSpin.setOnItemSelectedListener(spinListener);*/
//
//        IsAppliedDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getYesNo());
//        bioDataMap=dataAccessHandler.getGenericData(Queries.getInstance().getTypeCdDmtData("66"));
//        AppTypeDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getTypeCdDmtData("65"));
//        fertilizerDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getTypeCdDmtData("33"));
//        fertilizerTypeDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getLookUpData("23"));
//        frequencyOfApplicationDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getTypeCdDmtData("30"));
//        uomDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getUOM());
//
//        bioFertilizerSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(),"Bio Fertilizer1",bioDataMap));
//
//        //bioFertilizerSpin2.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(),"Bio Fertilizer2",bioDataMap));
//
//        sourceOfertilizerSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Source of Fertilizer", fertilizerDataMap));
//        //psourceOfertilizerSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Source of Fertilizer", fertilizerDataMap));
//
//        apptype.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Application Type", AppTypeDataMap));
//        //papptype.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Application Type", AppTypeDataMap));
//
//        fertilizerapplied.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Fertilizer Applied", IsAppliedDataMap));
//        //pfertilizerapplied.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Fertilizer Applied", IsAppliedDataMap));
//        //fertilizerProductNameSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "fertilizer Product Name", fertilizerTypeDataMap));
//        //uomSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Select UOM", uomDataMap));
//      //  frequencyOfApplicationSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Select Frequency of Application / yr", frequencyOfApplicationDataMap));
//
//
//        fertilizerapplied.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//
//                if (fertilizerapplied.getSelectedItemPosition() == 2){
//
//                    Monthyear.setEnabled(false);
//                    sourceName.setEnabled(false);
//                    //sourceOfertilizerSpin.setSelection(0);
//                    sourceOfertilizerSpin.setEnabled(false);
//                    comments.setEnabled(false);
//                   // apptype.setSelection(0);
//                    apptype.setEnabled(false);
//                    dosageGivenEdt.setEnabled(false);
//                    dosageGivenEdt1.setEnabled(false);
//                    dosageGivenEdt2.setEnabled(false);
//                    dosageGivenEdt3.setEnabled(false);
//                    dosageGivenEdt4.setEnabled(false);
//                    dosageGivenEdt5.setEnabled(false);
//                    dosageGivenEdt6.setEnabled(false);
//                    dosageGivenEdt7.setEnabled(false);
//                    dosageGivenEdt8.setEnabled(false);
//                }
//
//                if (fertilizerapplied.getSelectedItemPosition() == 1){
//
//                    Monthyear.setEnabled(true);
//                    sourceName.setEnabled(true);
//                    //sourceOfertilizerSpin.setSelection(0);
//                    sourceOfertilizerSpin.setEnabled(true);
//                    comments.setEnabled(true);
//                   // apptype.setSelection(0);
//                    apptype.setEnabled(true);
//                    dosageGivenEdt.setEnabled(true);
//                    dosageGivenEdt1.setEnabled(true);
//                    dosageGivenEdt2.setEnabled(true);
//                    dosageGivenEdt3.setEnabled(true);
//                    dosageGivenEdt4.setEnabled(true);
//                    dosageGivenEdt5.setEnabled(true);
//                    dosageGivenEdt6.setEnabled(true);
//                    dosageGivenEdt7.setEnabled(true);
//                    dosageGivenEdt8.setEnabled(true);
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
////        pfertilizerapplied.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
////            @Override
////            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
////
////                if (pfertilizerapplied.getSelectedItemPosition() == 2){
////
////                    pMonthyear.setEnabled(false);
////                    psourceName.setEnabled(false);
////                    psourceOfertilizerSpin.setSelection(0);
////                    psourceOfertilizerSpin.setEnabled(false);
////                    pcomments.setEnabled(false);
////                    papptype.setSelection(0);
////                    papptype.setEnabled(false);
////                    lastAppliedDateEdt.setEnabled(false);
////                    lastAppliedDateEdt1.setEnabled(false);
////                    lastAppliedDateEdt2.setEnabled(false);
////                    lastAppliedDateEdt3.setEnabled(false);
////                    lastAppliedDateEdt4.setEnabled(false);
////                    lastAppliedDateEdt5.setEnabled(false);
////                    lastAppliedDateEdt6.setEnabled(false);
////                    lastAppliedDateEdt7.setEnabled(false);
////                    lastAppliedDateEdt8.setEnabled(false);
////                }
////
////                if (pfertilizerapplied.getSelectedItemPosition() == 1){
////
////                    pMonthyear.setEnabled(true);
////                    psourceName.setEnabled(true);
////                    psourceOfertilizerSpin.setSelection(0);
////                    psourceOfertilizerSpin.setEnabled(true);
////                    pcomments.setEnabled(true);
////                    papptype.setSelection(0);
////                    papptype.setEnabled(true);
////                    lastAppliedDateEdt.setEnabled(true);
////                    lastAppliedDateEdt1.setEnabled(true);
////                    lastAppliedDateEdt2.setEnabled(true);
////                    lastAppliedDateEdt3.setEnabled(true);
////                    lastAppliedDateEdt4.setEnabled(true);
////                    lastAppliedDateEdt5.setEnabled(true);
////                    lastAppliedDateEdt6.setEnabled(true);
////                    lastAppliedDateEdt7.setEnabled(true);
////                    lastAppliedDateEdt8.setEnabled(true);
////                }
////
////            }
////
////            @Override
////            public void onNothingSelected(AdapterView<?> adapterView) {
////
////            }
////        });
//
//
//        bioFertilizerSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                if(bioFertilizerSpin.getSelectedItemPosition()!=0){
//                    bioFertilizerId=Integer.parseInt(bioDataMap.keySet().toArray()[position-1].toString());
//
//                    selectedbiofertilizer1 = bioFertilizerSpin.getSelectedItem().toString();
//                    Log.d("Selected Item", selectedbiofertilizer1 + "");
//
////                    Log.d("Selected Item", bioFertilizerSpin.getSelectedItem().toString());
////
//                    //Object ob = bioDataMap.remove(bioFertilizerSpin.getSelectedItemPosition());
//                    //Log.d("BioDataMapobject", ob.toString() + "");
//
//                    //bioDataMap.remove(bioFertilizerSpin.getSelectedItemId(), bioFertilizerSpin.getSelectedItem().toString());
////                    //Log.d("BioDataMap2", bioDataMap2.size() + "");
////                    Log.d("BioDataMap", bioDataMap.size() + "");
//                    bioFertilizerSpin2.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(),"Bio Fertilizer2",bioDataMap));
//
////                    if(CMCode!=null) {
////                        lastAppliedDateEdt7.setText(dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getBioFertilizerPrevQtrDosage(pq, py, CMCode, 232, bioFertilizerId,pmindate,pmaxdate)));
////                        lastAppliedDateEdt8.setText(dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getBioFertilizerPrevQtrDosage(pq, py, CMCode, 232, bioFertilizerId2,pmindate,pmaxdate)));
////                    }
//                    }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//        bioFertilizerSpin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                if(bioFertilizerSpin2.getSelectedItemPosition()!=0){
//                    bioFertilizerId2=Integer.parseInt(bioDataMap.keySet().toArray()[position-1].toString());
//                    selectedbiofertilizer2 = bioFertilizerSpin2.getSelectedItem().toString();
//                    Log.d("Selected Item2", selectedbiofertilizer2 + "");
//
//                    if (selectedbiofertilizer1 == selectedbiofertilizer2){
//                        Log.d("You have selected", "same fertilizers");
//                        Toast.makeText(mContext, "You have selected same fertilizers", Toast.LENGTH_SHORT).show();
//                        bioFertilizerSpin2.setSelection(0);
//                    }else{
//                        Log.d("You have selected", "different fertilizers");
//                    }
//
////                    if(CMCode!=null) {
////                        lastAppliedDateEdt7.setText(dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getBioFertilizerPrevQtrDosage(pq, py, CMCode, 232, bioFertilizerId,pmindate,pmaxdate)));
////                        lastAppliedDateEdt8.setText(dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getBioFertilizerPrevQtrDosage(pq, py, CMCode, 232, bioFertilizerId2,pmindate,pmaxdate)));
////                    }
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        //fetchprevqtr();
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.saveBtn:
//
//                mFertilizerModelArray = new ArrayList<Fertilizer>();
//
//                if(spinnerSelect(fertilizerapplied, "Fertilizer Applied", mContext)){
////                if (DataManager.getInstance().getDataFromManager(DataManager.RECMND_FERTILIZER) != null)
//                if (validateUI()) {
//                    Date date;
//                    String lastAppliedDate = "";
//                    String outputDate;
//                    mFertilizerModelArray.clear();
//
//                    for (int k = 0; k < 9; k++) {
//                        Fertilizer mFertilizerModel = new Fertilizer();
//                        Fertilizer mFertilizerModelp = new Fertilizer();
//                        switch (k) {
//                            case 0:
//                                if (dosageGivenEdt.getText().toString().length() > 0) {
//                                    mFertilizerModel.setFertilizerid(59);
//                                    mFertilizerModel.setDosage(Double.parseDouble(dosageGivenEdt.getText().toString()));
//                                    lastAppliedDate = applydate;
//                                    mFertilizerModel.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, sourceOfertilizerSpin.getSelectedItem().toString())));
//                                    mFertilizerModel.setSourceName(sourceName.getText().toString());
//                                    mFertilizerModel.setComments(comments.getText().toString());
//                                    mFertilizerModel.setUomid(1);
//                                    mFertilizerModel.setApplicationType(getKey(AppTypeDataMap, apptype.getSelectedItem().toString()));
//                                    mFertilizerModel.setApplicationMonth(Month);
//                                    mFertilizerModel.setApplicationYear(Year);
//                                    //mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
//                                    mFertilizerModel.setIsFertilizerApplied(1);
//                                    mFertilizerModel.setQuarter(Quater);
//
//                                    mFertilizerModel.setLastapplieddate(applydate);
//                                    mFertilizerModel.setApplyfertilizerfrequencytypeid(null);
//                                    mFertilizerModelArray.add(mFertilizerModel);
//                                }
//
////                                if (lastAppliedDateEdt.getText().toString().length() > 0 && lastAppliedDateEdt.isEnabled()) {
////                                    CommonConstants.Prev_Fertilizer_CMD = "Done";
////                                    mFertilizerModelp.setFertilizerid(59);
////                                    mFertilizerModelp.setDosage(Double.parseDouble(lastAppliedDateEdt.getText().toString()));
////                                    mFertilizerModelp.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, psourceOfertilizerSpin.getSelectedItem().toString())));
////                                    mFertilizerModelp.setSourceName(psourceName.getText().toString());
////                                    mFertilizerModelp.setComments(pcomments.getText().toString());
////                                    mFertilizerModelp.setUomid(1);
////                                    mFertilizerModelp.setApplicationType(getKey(AppTypeDataMap, papptype.getSelectedItem().toString()));
////                                    mFertilizerModelp.setApplicationMonth(pMonth);
////                                    mFertilizerModelp.setApplicationYear(pYear);
////                                    //mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
////                                    mFertilizerModelp.setIsFertilizerApplied(1);
////                                    mFertilizerModelp.setQuarter(pQuater);
////
////                                    mFertilizerModelp.setLastapplieddate(papplydate);
////                                    mFertilizerModelp.setApplyfertilizerfrequencytypeid(null);
////                                    mFertilizerModelArray.add(mFertilizerModelp);
////                                }
//                                break;
//                            case 1:
//                                if (dosageGivenEdt1.getText().toString().length() > 0) {
//                                    mFertilizerModel.setFertilizerid(61);
//                                    mFertilizerModel.setDosage(Double.parseDouble(dosageGivenEdt1.getText().toString()));
//                                    lastAppliedDate = applydate;
//                                    mFertilizerModel.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, sourceOfertilizerSpin.getSelectedItem().toString())));
//                                    mFertilizerModel.setSourceName(sourceName.getText().toString());
//                                    mFertilizerModel.setComments(comments.getText().toString());
//                                    mFertilizerModel.setUomid(1);
//                                    mFertilizerModel.setApplicationType(getKey(AppTypeDataMap, apptype.getSelectedItem().toString()));
//                                    mFertilizerModel.setApplicationMonth(Month);
//                                    mFertilizerModel.setApplicationYear(Year);
//                                    //mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
//                                    mFertilizerModel.setIsFertilizerApplied(1);
//                                    mFertilizerModel.setQuarter(Quater);
//
//                                    mFertilizerModel.setLastapplieddate(applydate);
//                                    mFertilizerModel.setApplyfertilizerfrequencytypeid(null);
//                                    mFertilizerModelArray.add(mFertilizerModel);
//                                }
////                                if (lastAppliedDateEdt1.getText().toString().length() > 0 && lastAppliedDateEdt1.isEnabled()) {
////                                    CommonConstants.Prev_Fertilizer_CMD = "Done";
////                                    mFertilizerModelp.setFertilizerid(61);
////                                    mFertilizerModelp.setDosage(Double.parseDouble(lastAppliedDateEdt1.getText().toString()));
////                                    mFertilizerModelp.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, psourceOfertilizerSpin.getSelectedItem().toString())));
////                                    mFertilizerModelp.setSourceName(psourceName.getText().toString());
////                                    mFertilizerModelp.setComments(pcomments.getText().toString());
////                                    mFertilizerModelp.setUomid(1);
////                                    mFertilizerModelp.setApplicationType(getKey(AppTypeDataMap, papptype.getSelectedItem().toString()));
////                                    mFertilizerModelp.setApplicationMonth(pMonth);
////                                    mFertilizerModelp.setApplicationYear(pYear);
////                                    //mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
////                                    mFertilizerModelp.setIsFertilizerApplied(1);
////                                    mFertilizerModelp.setQuarter(pQuater);
////
////                                    mFertilizerModelp.setLastapplieddate(papplydate);
////                                    mFertilizerModelp.setApplyfertilizerfrequencytypeid(null);
////                                    mFertilizerModelArray.add(mFertilizerModelp);
////                                }
//                                break;
//
//                            case 2:
//                                if (dosageGivenEdt2.getText().toString().length() > 0) {
//                                    mFertilizerModel.setFertilizerid(60);
//                                    mFertilizerModel.setDosage(Double.parseDouble(dosageGivenEdt2.getText().toString()));
//                                    lastAppliedDate = applydate;
//                                    mFertilizerModel.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, sourceOfertilizerSpin.getSelectedItem().toString())));
//                                    mFertilizerModel.setSourceName(sourceName.getText().toString());
//                                    mFertilizerModel.setComments(comments.getText().toString());
//                                    mFertilizerModel.setUomid(1);
//                                    mFertilizerModel.setApplicationType(getKey(AppTypeDataMap, apptype.getSelectedItem().toString()));
//                                    mFertilizerModel.setApplicationMonth(Month);
//                                    mFertilizerModel.setApplicationYear(Year);
//                                    //mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
//                                    mFertilizerModel.setIsFertilizerApplied(1);
//                                    mFertilizerModel.setQuarter(Quater);
//
//                                    mFertilizerModel.setLastapplieddate(applydate);
//                                    mFertilizerModel.setApplyfertilizerfrequencytypeid(null);
//                                    mFertilizerModelArray.add(mFertilizerModel);
//                                }
////                                if (lastAppliedDateEdt2.getText().toString().length() > 0 && lastAppliedDateEdt2.isEnabled()) {
////                                    CommonConstants.Prev_Fertilizer_CMD = "Done";
////                                    mFertilizerModelp.setFertilizerid(60);
////                                    mFertilizerModelp.setDosage(Double.parseDouble(lastAppliedDateEdt2.getText().toString()));
////                                    mFertilizerModelp.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, psourceOfertilizerSpin.getSelectedItem().toString())));
////                                    mFertilizerModelp.setSourceName(psourceName.getText().toString());
////                                    mFertilizerModelp.setComments(pcomments.getText().toString());
////                                    mFertilizerModelp.setUomid(1);
////                                    mFertilizerModelp.setApplicationType(getKey(AppTypeDataMap, papptype.getSelectedItem().toString()));
////                                    mFertilizerModelp.setApplicationMonth(pMonth);
////                                    mFertilizerModelp.setApplicationYear(pYear);
////                                    //mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
////                                    mFertilizerModelp.setIsFertilizerApplied(1);
////                                    mFertilizerModelp.setQuarter(pQuater);
////
////                                    mFertilizerModelp.setLastapplieddate(papplydate);
////                                    mFertilizerModelp.setApplyfertilizerfrequencytypeid(null);
////                                    mFertilizerModelArray.add(mFertilizerModelp);
////                                }
//                                break;
//
//                            case 3:
//                                if (dosageGivenEdt3.getText().toString().length() > 0) {
//                                    mFertilizerModel.setFertilizerid(63);
//                                    mFertilizerModel.setDosage(Double.parseDouble(dosageGivenEdt3.getText().toString()));
//                                    lastAppliedDate = applydate;
//                                    mFertilizerModel.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, sourceOfertilizerSpin.getSelectedItem().toString())));
//                                    mFertilizerModel.setSourceName(sourceName.getText().toString());
//                                    mFertilizerModel.setComments(comments.getText().toString());
//                                    mFertilizerModel.setUomid(1);
//                                    mFertilizerModel.setApplicationType(getKey(AppTypeDataMap, apptype.getSelectedItem().toString()));
//                                    mFertilizerModel.setApplicationMonth(Month);
//                                    mFertilizerModel.setApplicationYear(Year);
//                                    //mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
//                                    mFertilizerModel.setIsFertilizerApplied(1);
//                                    mFertilizerModel.setQuarter(Quater);
//
//                                    mFertilizerModel.setLastapplieddate(applydate);
//                                    mFertilizerModel.setApplyfertilizerfrequencytypeid(null);
//                                    mFertilizerModelArray.add(mFertilizerModel);
//                                }
////                                if (lastAppliedDateEdt3.getText().toString().length() > 0 && lastAppliedDateEdt3.isEnabled()) {
////                                    CommonConstants.Prev_Fertilizer_CMD = "Done";
////                                    mFertilizerModelp.setFertilizerid(63);
////                                    mFertilizerModelp.setDosage(Double.parseDouble(lastAppliedDateEdt3.getText().toString()));
////                                    mFertilizerModelp.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, psourceOfertilizerSpin.getSelectedItem().toString())));
////                                    mFertilizerModelp.setSourceName(psourceName.getText().toString());
////                                    mFertilizerModelp.setComments(pcomments.getText().toString());
////                                    mFertilizerModelp.setUomid(1);
////                                    mFertilizerModelp.setApplicationType(getKey(AppTypeDataMap, papptype.getSelectedItem().toString()));
////                                    mFertilizerModelp.setApplicationMonth(pMonth);
////                                    mFertilizerModelp.setApplicationYear(pYear);
////                                    //mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
////                                    mFertilizerModelp.setIsFertilizerApplied(1);
////                                    mFertilizerModelp.setQuarter(pQuater);
////
////                                    mFertilizerModelp.setLastapplieddate(papplydate);
////                                    mFertilizerModelp.setApplyfertilizerfrequencytypeid(null);
////                                    mFertilizerModelArray.add(mFertilizerModelp);
////                                }
//                                break;
//
//                            case 4:
//                                if (dosageGivenEdt4.getText().toString().length() > 0) {
//                                    mFertilizerModel.setFertilizerid(64);
//                                    mFertilizerModel.setDosage(Double.parseDouble(dosageGivenEdt4.getText().toString()));
//                                    lastAppliedDate = applydate;
//                                    mFertilizerModel.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, sourceOfertilizerSpin.getSelectedItem().toString())));
//                                    mFertilizerModel.setSourceName(sourceName.getText().toString());
//                                    mFertilizerModel.setComments(comments.getText().toString());
//                                    mFertilizerModel.setUomid(1);
//                                    mFertilizerModel.setApplicationType(getKey(AppTypeDataMap, apptype.getSelectedItem().toString()));
//                                    mFertilizerModel.setApplicationMonth(Month);
//                                    mFertilizerModel.setApplicationYear(Year);
//                                    // mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
//                                    mFertilizerModel.setIsFertilizerApplied(1);
//                                    mFertilizerModel.setQuarter(Quater);
//
//                                    mFertilizerModel.setLastapplieddate(applydate);
//                                    mFertilizerModel.setApplyfertilizerfrequencytypeid(null);
//                                    mFertilizerModelArray.add(mFertilizerModel);
//                                }
////                                if (lastAppliedDateEdt4.getText().toString().length() > 0 && lastAppliedDateEdt4.isEnabled()) {
////                                    CommonConstants.Prev_Fertilizer_CMD = "Done";
////                                    mFertilizerModelp.setFertilizerid(64);
////                                    mFertilizerModelp.setDosage(Double.parseDouble(lastAppliedDateEdt4.getText().toString()));
////                                    mFertilizerModelp.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, psourceOfertilizerSpin.getSelectedItem().toString())));
////                                    mFertilizerModelp.setSourceName(psourceName.getText().toString());
////                                    mFertilizerModelp.setComments(pcomments.getText().toString());
////                                    mFertilizerModelp.setUomid(1);
////                                    mFertilizerModelp.setApplicationType(getKey(AppTypeDataMap, papptype.getSelectedItem().toString()));
////                                    mFertilizerModelp.setApplicationMonth(pMonth);
////                                    mFertilizerModelp.setApplicationYear(pYear);
////                                    //mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
////                                    mFertilizerModelp.setIsFertilizerApplied(1);
////                                    mFertilizerModelp.setQuarter(pQuater);
////
////                                    mFertilizerModelp.setLastapplieddate(papplydate);
////                                    mFertilizerModelp.setApplyfertilizerfrequencytypeid(null);
////                                    mFertilizerModelArray.add(mFertilizerModelp);
////                                }
//                                break;
//
//                            case 5:
//                                if (dosageGivenEdt5.getText().toString().length() > 0) {
//                                    mFertilizerModel.setFertilizerid(58);
//                                    mFertilizerModel.setDosage(Double.parseDouble(dosageGivenEdt5.getText().toString()));
//                                    lastAppliedDate = applydate;
//                                    mFertilizerModel.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, sourceOfertilizerSpin.getSelectedItem().toString())));
//                                    mFertilizerModel.setSourceName(sourceName.getText().toString());
//                                    mFertilizerModel.setComments(comments.getText().toString());
//                                    mFertilizerModel.setUomid(1);
//                                    mFertilizerModel.setApplicationType(getKey(AppTypeDataMap, apptype.getSelectedItem().toString()));
//                                    mFertilizerModel.setApplicationMonth(Month);
//                                    mFertilizerModel.setApplicationYear(Year);
//                                    // mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
//                                    mFertilizerModel.setIsFertilizerApplied(1);
//                                    mFertilizerModel.setQuarter(Quater);
//
//                                    mFertilizerModel.setLastapplieddate(applydate);
//                                    mFertilizerModel.setApplyfertilizerfrequencytypeid(null);
//                                    mFertilizerModelArray.add(mFertilizerModel);
//                                }
////                                if (lastAppliedDateEdt5.getText().toString().length() > 0 && lastAppliedDateEdt5.isEnabled()) {
////                                    CommonConstants.Prev_Fertilizer_CMD = "Done";
////                                    mFertilizerModelp.setFertilizerid(58);
////                                    mFertilizerModelp.setDosage(Double.parseDouble(lastAppliedDateEdt5.getText().toString()));
////                                    mFertilizerModelp.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, psourceOfertilizerSpin.getSelectedItem().toString())));
////                                    mFertilizerModelp.setSourceName(psourceName.getText().toString());
////                                    mFertilizerModelp.setComments(pcomments.getText().toString());
////                                    mFertilizerModelp.setUomid(1);
////                                    mFertilizerModelp.setApplicationType(getKey(AppTypeDataMap, papptype.getSelectedItem().toString()));
////                                    mFertilizerModelp.setApplicationMonth(pMonth);
////                                    mFertilizerModelp.setApplicationYear(pYear);
////                                    //mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
////                                    mFertilizerModelp.setIsFertilizerApplied(1);
////                                    mFertilizerModelp.setQuarter(pQuater);
////
////                                    mFertilizerModelp.setLastapplieddate(papplydate);
////                                    mFertilizerModelp.setApplyfertilizerfrequencytypeid(null);
////                                    mFertilizerModelArray.add(mFertilizerModelp);
////                                }
//                                break;
//
//                            case 6:
//                                if (dosageGivenEdt6.getText().toString().length() > 0) {
//                                    mFertilizerModel.setFertilizerid(229);
//                                    mFertilizerModel.setDosage(Double.parseDouble(dosageGivenEdt6.getText().toString()));
//                                    lastAppliedDate = applydate;
//                                    mFertilizerModel.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, sourceOfertilizerSpin.getSelectedItem().toString())));
//                                    mFertilizerModel.setSourceName(sourceName.getText().toString());
//                                    mFertilizerModel.setComments(comments.getText().toString());
//                                    mFertilizerModel.setUomid(1);
//                                    mFertilizerModel.setApplicationType(getKey(AppTypeDataMap, apptype.getSelectedItem().toString()));
//                                    mFertilizerModel.setApplicationMonth(Month);
//                                    mFertilizerModel.setApplicationYear(Year);
//                                    //  mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
//                                    mFertilizerModel.setIsFertilizerApplied(1);
//                                    mFertilizerModel.setQuarter(Quater);
//
//                                    mFertilizerModel.setLastapplieddate(applydate);
//                                    mFertilizerModel.setApplyfertilizerfrequencytypeid(null);
//                                    mFertilizerModelArray.add(mFertilizerModel);
//                                }
////                                if (lastAppliedDateEdt6.getText().toString().length() > 0 && lastAppliedDateEdt6.isEnabled()) {
////                                    CommonConstants.Prev_Fertilizer_CMD = "Done";
////                                    mFertilizerModelp.setFertilizerid(229);
////                                    mFertilizerModelp.setDosage(Double.parseDouble(lastAppliedDateEdt6.getText().toString()));
////                                    mFertilizerModelp.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, psourceOfertilizerSpin.getSelectedItem().toString())));
////                                    mFertilizerModelp.setSourceName(psourceName.getText().toString());
////                                    mFertilizerModelp.setComments(pcomments.getText().toString());
////                                    mFertilizerModelp.setUomid(1);
////                                    mFertilizerModelp.setApplicationType(getKey(AppTypeDataMap, papptype.getSelectedItem().toString()));
////                                    mFertilizerModelp.setApplicationMonth(pMonth);
////                                    mFertilizerModelp.setApplicationYear(pYear);
////                                    //mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
////                                    mFertilizerModelp.setIsFertilizerApplied(1);
////                                    mFertilizerModelp.setQuarter(pQuater);
////
////                                    mFertilizerModelp.setLastapplieddate(papplydate);
////                                    mFertilizerModelp.setApplyfertilizerfrequencytypeid(null);
////                                    mFertilizerModelArray.add(mFertilizerModelp);
////                                }
//                                break;
//                            case 7:
//                                if (bioFertilizerSpin.getSelectedItemPosition() != 0 && dosageGivenEdt7.getText().toString().length() > 0) {
//                                    mFertilizerModel.setFertilizerid(bioFertilizerId);
//                                    mFertilizerModel.setBioFertilizerId(bioFertilizerId);
//                                    mFertilizerModel.setDosage(Double.parseDouble(dosageGivenEdt7.getText().toString()));
//                                    lastAppliedDate = applydate;
//                                    mFertilizerModel.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, sourceOfertilizerSpin.getSelectedItem().toString())));
//                                    mFertilizerModel.setSourceName(sourceName.getText().toString());
//                                    mFertilizerModel.setComments(comments.getText().toString());
//                                    mFertilizerModel.setUomid(1);
//                                    mFertilizerModel.setApplicationType(getKey(AppTypeDataMap, apptype.getSelectedItem().toString()));
//                                    mFertilizerModel.setApplicationMonth(Month);
//                                    mFertilizerModel.setApplicationYear(Year);
//                                    //  mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
//                                    mFertilizerModel.setIsFertilizerApplied(1);
//                                    mFertilizerModel.setQuarter(Quater);
//
//                                    mFertilizerModel.setLastapplieddate(applydate);
//                                    mFertilizerModel.setApplyfertilizerfrequencytypeid(null);
//                                    mFertilizerModelArray.add(mFertilizerModel);
//                                }
//
////                                if (lastAppliedDateEdt7.getText().toString().length() > 0 && lastAppliedDateEdt7.isEnabled() && bioFertilizerSpin.getSelectedItemPosition() != 0) {
////                                    CommonConstants.Prev_Fertilizer_CMD = "Done";
////                                    mFertilizerModelp.setFertilizerid(232);
////                                    mFertilizerModelp.setBioFertilizerId(bioFertilizerId);
////                                    mFertilizerModelp.setDosage(Double.parseDouble(lastAppliedDateEdt7.getText().toString()));
////                                    mFertilizerModelp.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, psourceOfertilizerSpin.getSelectedItem().toString())));
////                                    mFertilizerModelp.setSourceName(psourceName.getText().toString());
////                                    mFertilizerModelp.setComments(pcomments.getText().toString());
////                                    mFertilizerModelp.setUomid(1);
////                                    mFertilizerModelp.setApplicationType(getKey(AppTypeDataMap, papptype.getSelectedItem().toString()));
////                                    mFertilizerModelp.setApplicationMonth(pMonth);
////                                    mFertilizerModelp.setApplicationYear(pYear);
////                                    //mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
////                                    mFertilizerModelp.setIsFertilizerApplied(1);
////                                    mFertilizerModelp.setQuarter(pQuater);
////
////                                    mFertilizerModelp.setLastapplieddate(papplydate);
////                                    mFertilizerModelp.setApplyfertilizerfrequencytypeid(null);
////                                    mFertilizerModelArray.add(mFertilizerModelp);
////                                }
//                                break;
//                            case 8:
//                                if (bioFertilizerSpin2.getSelectedItemPosition() != 0 && dosageGivenEdt8.getText().toString().length() > 0) {
//                                    mFertilizerModel.setFertilizerid(bioFertilizerId2);
//                                    mFertilizerModel.setBioFertilizerId(bioFertilizerId2);
//                                    mFertilizerModel.setDosage(Double.parseDouble(dosageGivenEdt8.getText().toString()));
//                                    lastAppliedDate = applydate;
//                                    mFertilizerModel.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, sourceOfertilizerSpin.getSelectedItem().toString())));
//                                    mFertilizerModel.setSourceName(sourceName.getText().toString());
//                                    mFertilizerModel.setComments(comments.getText().toString());
//                                    mFertilizerModel.setUomid(1);
//                                    mFertilizerModel.setApplicationType(getKey(AppTypeDataMap, apptype.getSelectedItem().toString()));
//                                    mFertilizerModel.setApplicationMonth(Month);
//                                    mFertilizerModel.setApplicationYear(Year);
//                                    //  mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
//                                    mFertilizerModel.setIsFertilizerApplied(1);
//                                    mFertilizerModel.setQuarter(Quater);
//
//                                    mFertilizerModel.setLastapplieddate(applydate);
//                                    mFertilizerModel.setApplyfertilizerfrequencytypeid(null);
//                                    mFertilizerModelArray.add(mFertilizerModel);
//                                }
////                                if (lastAppliedDateEdt8.getText().toString().length() > 0 && lastAppliedDateEdt8.isEnabled() && bioFertilizerSpin2.getSelectedItemPosition() != 0) {
////                                    CommonConstants.Prev_Fertilizer_CMD = "Done";
////                                    mFertilizerModelp.setFertilizerid(232);
////                                    mFertilizerModelp.setBioFertilizerId(bioFertilizerId2);
////                                    mFertilizerModelp.setDosage(Double.parseDouble(lastAppliedDateEdt8.getText().toString()));
////                                    mFertilizerModelp.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, psourceOfertilizerSpin.getSelectedItem().toString())));
////                                    mFertilizerModelp.setSourceName(psourceName.getText().toString());
////                                    mFertilizerModelp.setComments(pcomments.getText().toString());
////                                    mFertilizerModelp.setUomid(1);
////                                    mFertilizerModelp.setApplicationType(getKey(AppTypeDataMap, papptype.getSelectedItem().toString()));
////                                    mFertilizerModelp.setApplicationMonth(pMonth);
////                                    mFertilizerModelp.setApplicationYear(pYear);
////                                    //mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
////                                    mFertilizerModelp.setIsFertilizerApplied(1);
////                                    mFertilizerModelp.setQuarter(pQuater);
////
////                                    mFertilizerModelp.setLastapplieddate(papplydate);
////                                    mFertilizerModelp.setApplyfertilizerfrequencytypeid(null);
////                                    mFertilizerModelArray.add(mFertilizerModelp);
////                                }
//                                break;
//
//                        }
//
//
//                    }
//                    Fertilizer mFertilizerModel = new Fertilizer();
//                    Fertilizer mFertilizerModelp = new Fertilizer();
//                    if (Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())) == 0) {
//                        mFertilizerModel.setFertilizerid(234);
//                        mFertilizerModel.setDosage(0.00);
//                        lastAppliedDate = applydate;
//                        mFertilizerModel.setFertilizersourcetypeid(155);
//                        mFertilizerModel.setSourceName("");
//                        mFertilizerModel.setComments("");
//                        mFertilizerModel.setUomid(1);
//
//                        mFertilizerModel.setApplicationMonth("");
//                        mFertilizerModel.setApplicationYear(cy);
//                        //  mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
//                        mFertilizerModel.setIsFertilizerApplied(0);
//                        mFertilizerModel.setQuarter(cq);
//
//                        mFertilizerModel.setLastapplieddate(cmindate+" 00:00:00");
//                        mFertilizerModel.setApplyfertilizerfrequencytypeid(null);
//                        mFertilizerModelArray.add(mFertilizerModel);
//                    }
////                    if (Integer.parseInt(getKey(IsAppliedDataMap, pfertilizerapplied.getSelectedItem().toString())) == 0) {
////                        CommonConstants.Prev_Fertilizer_CMD = "Done";
////                        mFertilizerModelp.setFertilizerid(234);
////                        mFertilizerModelp.setDosage(0.00);
////                        lastAppliedDate = applydate;
////                        mFertilizerModelp.setFertilizersourcetypeid(155);
////                        mFertilizerModelp.setSourceName("");
////                        mFertilizerModelp.setComments("");
////                        mFertilizerModelp.setUomid(1);
////
////                        mFertilizerModelp.setApplicationMonth("");
////                        mFertilizerModelp.setApplicationYear(py);
////                        //  mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
////                        mFertilizerModelp.setIsFertilizerApplied(0);
////                        mFertilizerModelp.setQuarter(pq);
////
////                        mFertilizerModelp.setLastapplieddate(pmindate+" 00:00:00");
////                        mFertilizerModelp.setApplyfertilizerfrequencytypeid(null);
////                        mFertilizerModelArray.add(mFertilizerModelp);
////                    }
//
//                    DataManager.getInstance().addData(DataManager.FERTILIZER, mFertilizerModelArray);
//                    clearFields();
//                   // fertilizerDataAdapter.notifyDataSetChanged();
//                    getFragmentManager().popBackStack();
//
//                    updateUiListener.updateUserInterface(0);
//
//                }
//        }
//                CommonUtilsNavigation.hideKeyBoard(getActivity());
//                break;
//
//
//
////            case R.id.lastAppliedDateEdt:
////                cal=0;
////                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, myCalendar
////                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
////                        myCalendar.get(Calendar.DAY_OF_MONTH));  //date is dateSetListener as per your code in question
////                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
////                datePickerDialog.show();
////                break;
////
////            case R.id.lastAppliedDateEdt1:
////                cal=1;
////                DatePickerDialog datePickerDialog1 = new DatePickerDialog(getActivity(), date, myCalendar
////                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
////                        myCalendar.get(Calendar.DAY_OF_MONTH));  //date is dateSetListener as per your code in question
////                datePickerDialog1.getDatePicker().setMaxDate(System.currentTimeMillis());
////                datePickerDialog1.show();
////                break;
////            case R.id.lastAppliedDateEdt2:
////                cal=2;
////                DatePickerDialog datePickerDialog2 = new DatePickerDialog(getActivity(), date, myCalendar
////                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
////                        myCalendar.get(Calendar.DAY_OF_MONTH));  //date is dateSetListener as per your code in question
////                datePickerDialog2.getDatePicker().setMaxDate(System.currentTimeMillis());
////                datePickerDialog2.show();
////                break;
////            case R.id.lastAppliedDateEdt3:
////                cal=3;
////                DatePickerDialog datePickerDialog3 = new DatePickerDialog(getActivity(), date, myCalendar
////                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
////                        myCalendar.get(Calendar.DAY_OF_MONTH));  //date is dateSetListener as per your code in question
////                datePickerDialog3.getDatePicker().setMaxDate(System.currentTimeMillis());
////                datePickerDialog3.show();
////                break;
////            case R.id.lastAppliedDateEdt4:
////                cal=4;
////                DatePickerDialog datePickerDialog4 = new DatePickerDialog(getActivity(), date, myCalendar
////                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
////                        myCalendar.get(Calendar.DAY_OF_MONTH));  //date is dateSetListener as per your code in question
////                datePickerDialog4.getDatePicker().setMaxDate(System.currentTimeMillis());
////                datePickerDialog4.show();
////                break;
////            case R.id.lastAppliedDateEdt5:
////                cal=5;
////                DatePickerDialog datePickerDialog5 = new DatePickerDialog(getActivity(), date, myCalendar
////                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
////                        myCalendar.get(Calendar.DAY_OF_MONTH));  //date is dateSetListener as per your code in question
////                datePickerDialog5.getDatePicker().setMaxDate(System.currentTimeMillis());
////                datePickerDialog5.show();
////                break;
////            case R.id.lastAppliedDateEdt6:
////                cal=6;
////                DatePickerDialog datePickerDialog6 = new DatePickerDialog(getActivity(), date, myCalendar
////                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
////                        myCalendar.get(Calendar.DAY_OF_MONTH));  //date is dateSetListener as per your code in question
////                datePickerDialog6.getDatePicker().setMaxDate(System.currentTimeMillis());
////                datePickerDialog6.show();
////                break;
////            case R.id.lastAppliedDateEdt7:
////                cal=7;
////                DatePickerDialog datePickerDialog7 = new DatePickerDialog(getActivity(), date, myCalendar
////                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
////                        myCalendar.get(Calendar.DAY_OF_MONTH));  //date is dateSetListener as per your code in question
////                datePickerDialog7.getDatePicker().setMaxDate(System.currentTimeMillis());
////                datePickerDialog7.show();
////                break;
//
//            case R.id.historyBtn:
//
//
////                Log.d("HistoryBtn", "Clciked");
////                cal=7;
////                CropMaintainanceHistoryFragment newFragment = new CropMaintainanceHistoryFragment();
////                Bundle bundle = new Bundle();
////                bundle.putInt("screen", 1);
////                newFragment.setArguments(bundle);
////                newFragment.show(getActivity().getFragmentManager(), "history");
//                showDialog(getContext());
//
//                break;
//
//            case R.id.pdfBtn:
//                showPDFDialog(getContext());
//                break;
//
//        }
//
//    }
//
//    public void showDialog(Context activity) {
//        final Dialog dialog = new Dialog(activity);
//        dialog.setCancelable(true);
//        dialog.setContentView(R.layout.fertilizerlastvisteddata);
//
//        Toolbar titleToolbar;
//        titleToolbar = (Toolbar) dialog.findViewById(R.id.titleToolbar);
//        titleToolbar.setTitle("Fertilizer History");
//        titleToolbar.setTitleTextColor(getResources().getColor(R.color.white));
//
//        FertilizerNonBioVisitedDataAdapter fertilizernonbioVisitedDataAdapter;
//        FertilizerBioVisitedDataAdapter fertilizerbioVisitedDataAdapter;
//
//
//        LinearLayout fertilizermainlyt = (LinearLayout) dialog.findViewById(R.id.fertilizermainlyt);
//        LinearLayout biolayout = (LinearLayout) dialog.findViewById(R.id.biolayout);
//
////        LinearLayout fertilizerappliedll = (LinearLayout) dialog.findViewById(R.id.fertilizerappliedll);
////        LinearLayout fertilizermonthyearll = (LinearLayout) dialog.findViewById(R.id.fertilizermonthyearll);
////        LinearLayout nameoftheshopll = (LinearLayout) dialog.findViewById(R.id.nameoftheshopll);
////        LinearLayout sourceoffertilizerll = (LinearLayout) dialog.findViewById(R.id.sourceoffertilizerll);
////        LinearLayout fertcommentsll = (LinearLayout) dialog.findViewById(R.id.fertcommentsll);
////        LinearLayout fertapptypell = (LinearLayout) dialog.findViewById(R.id.fertapptypell);
//
//
//        RecyclerView nonbiofertrcv = (RecyclerView) dialog.findViewById(R.id.nonbiofertrcv);
//        RecyclerView biofertrcv = (RecyclerView) dialog.findViewById(R.id.biofertrcv);
//
////        TextView fertilizerapplied = (TextView) dialog.findViewById(R.id.fertilizerapplied);
////        TextView fertilizermonthyear = (TextView) dialog.findViewById(R.id.fertilizermonthyear);
////        TextView nameoftheshop = (TextView) dialog.findViewById(R.id.nameoftheshop);
////        TextView sourceoffertilizer = (TextView) dialog.findViewById(R.id.sourceoffertilizer);
////        TextView fertcomments = (TextView) dialog.findViewById(R.id.fertcomments);
////        TextView fertapptype = (TextView) dialog.findViewById(R.id.fertapptype);
//
//        TextView norecords = (TextView) dialog.findViewById(R.id.fertilizernorecord_tv);
//
//
//        String lastVisitCode = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getLatestCropMaintanaceHistoryCode(CommonConstants.PLOT_CODE));
//        fertilizernonbiolastvisitdatamap = (ArrayList<Fertilizer>) dataAccessHandler.getFertilizerData(Queries.getInstance().getFertilizerCropMaintenanceHistoryData(lastVisitCode), 1);
//        fertilizerbiolastvisitdatamap = (ArrayList<Fertilizer>) dataAccessHandler.getFertilizerData(Queries.getInstance().getBioFertilizerCropMaintenanceHistoryData(lastVisitCode), 1);
//
//        if (fertilizernonbiolastvisitdatamap.size() > 0 || fertilizerbiolastvisitdatamap.size() > 0){
//            norecords.setVisibility(View.GONE);
//            fertilizermainlyt.setVisibility(View.VISIBLE);
//
//            if (fertilizernonbiolastvisitdatamap.size() > 0){
//                nonbiofertrcv.setVisibility(View.VISIBLE);
//                fertilizernonbioVisitedDataAdapter = new FertilizerNonBioVisitedDataAdapter(getActivity(), fertilizernonbiolastvisitdatamap,dataAccessHandler);
//                nonbiofertrcv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//                nonbiofertrcv.setAdapter(fertilizernonbioVisitedDataAdapter);
//            }else{
//                nonbiofertrcv.setVisibility(View.GONE);
//            }
//
//            if (fertilizerbiolastvisitdatamap.size() > 0){
//                biolayout.setVisibility(View.VISIBLE);
//                fertilizerbioVisitedDataAdapter = new FertilizerBioVisitedDataAdapter(getActivity(), fertilizerbiolastvisitdatamap,dataAccessHandler);
//                biofertrcv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//                biofertrcv.setAdapter(fertilizerbioVisitedDataAdapter);
//            }else{
//                biolayout.setVisibility(View.GONE);
//            }
//
//
//
//        }else{
//            fertilizermainlyt.setVisibility(View.GONE);
//            norecords.setVisibility(View.VISIBLE);
//        }
//
//        dialog.show();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, 500);
//    }
//
//    public void showPDFDialog(Context activity) {
//        final Dialog dialog = new Dialog(activity);
//        dialog.setCancelable(true);
//        dialog.setContentView(R.layout.pdfdialog);
//
//        Toolbar titleToolbar;
//        titleToolbar = (Toolbar) dialog.findViewById(R.id.titleToolbar);
//        titleToolbar.setTitle("Fertilizer PDF");
//        titleToolbar.setTitleTextColor(getResources().getColor(R.color.white));
//
//        PDFView fertpfdview;
//
//        fertpfdview = dialog.findViewById(R.id.fertpdfview);
//
//            fertpfdview.fromFile(fileToDownLoad)
//                    .defaultPage(0)
//                    .enableAnnotationRendering(true)
//                    .onPageChange(this)
//                    .onLoad(this)
//                    .scrollHandle(new DefaultScrollHandle(getActivity()))
//                    .load();
//
//        dialog.show();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, 500);
//    }
//
//
//    private void clearFields() {
//        sourceOfertilizerSpin.setSelection(0);
//        //fertilizerProductNameSpin.setSelection(0);
//      //  frequencyOfApplicationSpin.setSelection(0);
//        //uomSpin.setSelection(0);
//        dosageGivenEdt.setText("");
//        otherEdt.setText("");
//        //lastAppliedDateEdt.setText("");
//    }
//
////    private boolean validateUI() {
////        return (Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString()))==1 ? spinnerSelect(sourceOfertilizerSpin, "Source of fertilizer", mContext)&& edittextEampty(sourceName, "Shop Name", mContext)         && spinnerSelect(apptype , "Application Type", mContext)&& edittextEampty(Monthyear, "Month/Year", mContext):true)
////                &&     (Integer.parseInt(getKey(IsAppliedDataMap, pfertilizerapplied.getSelectedItem().toString()))==1 ? spinnerSelect(psourceOfertilizerSpin, "Source of fertilizer", mContext) && edittextEampty(psourceName, "Shop Name", mContext)         && spinnerSelect(papptype , "Application Type", mContext) &&
////                edittextEampty(pMonthyear, "Month/Year", mContext) :true)
////                //&& spinnerSelect(fertilizerProductNameSpin, "Fertilizer product name", mContext)
////                //&& spinnerSelect(uomSpin, "UOM", mContext)
////
////
////                && spinnerSelect(fertilizerapplied, "Fertilizer Applied", mContext)
////                && spinnerSelect(pfertilizerapplied, "Fertilizer Applied", mContext)
////
//////                && ( dosageGivenEdt.getText().toString().length()>0?  edittextEampty(lastAppliedDateEdt, "Last Applied Date", mContext) : true)
//////                && ( dosageGivenEdt1.getText().toString().length()>0?  edittextEampty(lastAppliedDateEdt1, "Last Applied Date", mContext) : true)
//////                && ( dosageGivenEdt2.getText().toString().length()>0?  edittextEampty(lastAppliedDateEdt2, "Last Applied Date", mContext) : true)
//////                && ( dosageGivenEdt3.getText().toString().length()>0?  edittextEampty(lastAppliedDateEdt3, "Last Applied Date", mContext) : true)
//////                && ( dosageGivenEdt4.getText().toString().length()>0?  edittextEampty(lastAppliedDateEdt4, "Last Applied Date", mContext) : true)
//////                && ( dosageGivenEdt5.getText().toString().length()>0?  edittextEampty(lastAppliedDateEdt5, "Last Applied Date", mContext) : true)
//////                && ( dosageGivenEdt6.getText().toString().length()>0?  edittextEampty(lastAppliedDateEdt6, "Last Applied Date", mContext) : true)
//////                && ( dosageGivenEdt7.getText().toString().length()>0?  edittextEampty(lastAppliedDateEdt7, "Last Applied Date", mContext) : true)
//////
////                ;
////
////    }
//private boolean validateUI() {
//
//    return (Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString()))==1 ?
//            spinnerSelect(sourceOfertilizerSpin, "Source of fertilizer", mContext)&& edittextEampty(sourceName, "Shop Name", mContext)
//                    && spinnerSelect(apptype , "Application Type", mContext)&& edittextEampty(Monthyear, "Month/Year", mContext):true)
////            &&
////            (Integer.parseInt(getKey(IsAppliedDataMap, pfertilizerapplied.getSelectedItem().toString()))==1 ?
////            spinnerSelect(psourceOfertilizerSpin, "Source of fertilizer", mContext) &&
////                    edittextEampty(psourceName, "Shop Name", mContext)
////                    && spinnerSelect(papptype , "Application Type", mContext) &&
////                    edittextEampty(pMonthyear, "Month/Year", mContext)
////                    :true)
//            && ( bioFertilizerSpin.getSelectedItemPosition() == 0  && dosageGivenEdt7.getText().toString().length() > 0)?spinnerSelect(bioFertilizerSpin, "Bio Fertilizer1", mContext) : true
//            && ( bioFertilizerSpin2.getSelectedItemPosition() == 0  && dosageGivenEdt8.getText().toString().length() > 0)?spinnerSelect(bioFertilizerSpin2, "Bio Fertilizer2", mContext) : true
////            && ( bioFertilizerSpin.getSelectedItemPosition() == 0  && lastAppliedDateEdt7.getText().toString().length() > 0)?spinnerSelect(bioFertilizerSpin, "Bio Fertilizer1", mContext) : true
////            && ( bioFertilizerSpin2.getSelectedItemPosition() == 0  && lastAppliedDateEdt8.getText().toString().length() > 0)?spinnerSelect(bioFertilizerSpin2, "Bio Fertilizer2", mContext) : true
//
//            //&& spinnerSelect(fertilizerProductNameSpin, "Fertilizer product name", mContext)
//            //&& spinnerSelect(uomSpin, "UOM", mContext)
//
//
//            && spinnerSelect(fertilizerapplied, "Fertilizer Applied", mContext)
////            && spinnerSelect(pfertilizerapplied, "Fertilizer Applied", mContext)
//
////                && ( dosageGivenEdt.getText().toString().length()>0?  edittextEampty(lastAppliedDateEdt, "Last Applied Date", mContext) : true)
////                && ( dosageGivenEdt1.getText().toString().length()>0?  edittextEampty(lastAppliedDateEdt1, "Last Applied Date", mContext) : true)
////                && ( dosageGivenEdt2.getText().toString().length()>0?  edittextEampty(lastAppliedDateEdt2, "Last Applied Date", mContext) : true)
////                && ( dosageGivenEdt3.getText().toString().length()>0?  edittextEampty(lastAppliedDateEdt3, "Last Applied Date", mContext) : true)
////                && ( dosageGivenEdt4.getText().toString().length()>0?  edittextEampty(lastAppliedDateEdt4, "Last Applied Date", mContext) : true)
////                && ( dosageGivenEdt5.getText().toString().length()>0?  edittextEampty(lastAppliedDateEdt5, "Last Applied Date", mContext) : true)
////                && ( dosageGivenEdt6.getText().toString().length()>0?  edittextEampty(lastAppliedDateEdt6, "Last Applied Date", mContext) : true)
////                && ( dosageGivenEdt7.getText().toString().length()>0?  edittextEampty(lastAppliedDateEdt7, "Last Applied Date", mContext) : true)
////
//            ;
//
//}
//
////    private void updateLabel() {
////        String myFormat = "MM/dd/yyyy"; //In which you need put here
////        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
////        if(cal==0)
////            lastAppliedDateEdt.setText(sdf.format(myCalendar.getTime()));
////        else if(cal==1)
////            lastAppliedDateEdt1.setText(sdf.format(myCalendar.getTime()));
////        else if(cal==2)
////            lastAppliedDateEdt2.setText(sdf.format(myCalendar.getTime()));
////        else if(cal==3)
////            lastAppliedDateEdt3.setText(sdf.format(myCalendar.getTime()));
////        else if(cal==4)
////            lastAppliedDateEdt4.setText(sdf.format(myCalendar.getTime()));
////        else if(cal==5)
////            lastAppliedDateEdt5.setText(sdf.format(myCalendar.getTime()));
////        else if(cal==6)
////            lastAppliedDateEdt6.setText(sdf.format(myCalendar.getTime()));
////        else if(cal==7)
////            lastAppliedDateEdt7.setText(sdf.format(myCalendar.getTime()));
////
////    }
//
//    @Override
//    public void onEditClicked(int position) {
//        Log.v(LOG_TAG, "@@@ selected position " + position);
//        mFertilizerModelArray.remove(position);
//        fertilizerDataAdapter.notifyDataSetChanged();
//    }
//
//    public void setUpdateUiListener(UpdateUiListener updateUiListener) {
//        this.updateUiListener = updateUiListener;
//    }
//
//    @Override
//    public void updateUserInterface(int refreshPosition) {
//        complaintsBtn.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void loadComplete(int nbPages) {
//
//    }
//
//    @Override
//    public void onPageChanged(int page, int pageCount) {
//
//    }
//}

//Used to recommend fertilizer during crop maintenance
public class FertilizerFragment extends Fragment implements View.OnClickListener, PalmDetailsEditListener, UpdateUiListener, OnPageChangeListener, OnLoadCompleteListener {
    private static final String LOG_TAG = FertilizerFragment.class.getName();
    private String Monthnumber;
    private ArrayList<Fertilizer> mFertilizerModelArray = new ArrayList<>();
    private View rootView;
    private Spinner sourceOfertilizerSpin,psourceOfertilizerSpin,bioFertilizerSpin,bioFertilizerSpin2, bioFertilizerSpin3,bioFertilizerSpin4,bioFertilizerSpin5,fertilizerProductNameSpin, uomSpin, frequencyOfApplicationSpin,fertilizerapplied,apptype,papptype,pfertilizerapplied;
    private EditText dosageGivenEdt, lastAppliedDateEdt,dosageGivenEdt1, lastAppliedDateEdt1,dosageGivenEdt2, lastAppliedDateEdt2,
            dosageGivenEdt3, lastAppliedDateEdt3,dosageGivenEdt4, lastAppliedDateEdt4,dosageGivenEdt5, lastAppliedDateEdt5,dosageGivenEdt6, lastAppliedDateEdt6, dosageGivenEdt7,lastAppliedDateEdt7,dosageGivenEdt8,lastAppliedDateEdt8,dosageGivenEdt31,dosageGivenEdt41,dosageGivenEdt51,
            dialog_otherEdt, otherEdt,comments,pcomments, sourceName,psourceName,Monthyear,pMonthyear;
    private LinearLayout otherLay, headerLL;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private DateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
    private DateFormat inputFormatYYMMDD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private ArrayList<String> months;
    Bundle args = new Bundle();
    private String applydate=null ;
    private String papplydate=null ;

    private String selectedbiofertilizer1;
    private String selectedbiofertilizer2;
    private String selectedbiofertilizer3;
    private String selectedbiofertilizer4;
    private String selectedbiofertilizer5;

    private ArrayList<Fertilizer> fertilizernonbiolastvisitdatamap;
    private ArrayList<Fertilizer> fertilizerbiolastvisitdatamap;

    private String Month =null;
    private  int Quater;
    private int Year ;
    private String pMonth =null;
    private  int pQuater;
    private int pYear ;
    private int cy,cm,cq,py,pm,pq,caly,pcaly;
    private long mindate,maxdate;
    private long ppmindate,ppmaxdate;
    private String pmindate,pmaxdate;
    private String cmindate,cmaxdate;
    private TextView tv3,tv4;
    int bioFertilizerId=0;
    int bioFertilizerId2=0;
    int bioFertilizerId3=0;
    int bioFertilizerId4=0;
    int bioFertilizerId5=0;
    private   String CMCode = "" ;
    int Biofertilizercount = 0;
    AdapterView.OnItemSelectedListener spinListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()) {

                case R.id.fertilizerProductNameSpin:
                    if (fertilizerProductNameSpin.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.other))) {
//                        dialog.show();
                        otherLay.setVisibility(View.VISIBLE);
                    } else {
                        otherLay.setVisibility(View.GONE);
                    }

                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
    private RecyclerView fertilizerList;
    private Button saveBtn,historyBtn, pdfBtn;
    private LinkedHashMap<String, String> fertilizerDataMap,bioDataMap, fertilizerTypeDataMap, uomDataMap, frequencyOfApplicationDataMap,IsAppliedDataMap,AppTypeDataMap, bioDataMap2;
    private DataAccessHandler dataAccessHandler;
    private GenericTypeAdapter fertilizerDataAdapter;
    private Context mContext;

    private UpdateUiListener updateUiListener;
    private Button complaintsBtn;
    private ActionBar actionBar;
    private Button btnRecommnd;
    private int cal = 99;
    Toolbar toolbar;

    File fileToDownLoad;
    CropMaintenanceDocs cropMaintenanceDocs;
    public FertilizerFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fertilizerdetailsfrag, container, false);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        actionBar = activity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getActivity().getResources().getString(R.string.fertilizerApplication));

        mContext = getActivity();
        dataAccessHandler = new DataAccessHandler(getActivity());

        cropMaintenanceDocs = (CropMaintenanceDocs) dataAccessHandler.getCMDocsData(Queries.getInstance().getFertilizerPDFfile(), 0);

        if (cropMaintenanceDocs != null) {

            fileToDownLoad = new File(CommonUtils.get3FFileRootPath() + "3F_CMDocs/" + cropMaintenanceDocs.getFileName() + cropMaintenanceDocs.getFileExtension());
        }

        setHasOptionsMenu(true);
        initViews();
        setViews();

        bindData();

        btnRecommnd=(Button) rootView.findViewById(R.id.btnRecommnd);
        btnRecommnd.setOnClickListener(v -> {
            RecomndFertilizerFragment mRecomNDScreen = new RecomndFertilizerFragment();
            String backStateName = mRecomNDScreen.getClass().getName();
            mRecomNDScreen.setArguments(args);
            mFragmentManager = getActivity().getSupportFragmentManager();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.setCustomAnimations(
                    R.anim.enter_from_right,0,0, R.anim.exit_to_left
            );
            mFragmentTransaction.replace(android.R.id.content, mRecomNDScreen);
            mFragmentTransaction.addToBackStack(backStateName);
            mFragmentTransaction.commit();
        });

        return rootView;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void bindData() {
        mFertilizerModelArray = (ArrayList<Fertilizer>) DataManager.getInstance().getDataFromManager(DataManager.FERTILIZER);
        if (mFertilizerModelArray != null) {
            int arraySize = mFertilizerModelArray.size();
            Log.e("====>216", mFertilizerModelArray.get(0).getBioFertilizerId() + "");
            fertilizerapplied.setSelection(mFertilizerModelArray.get(0).getIsFertilizerApplied() == null ? 0 : mFertilizerModelArray.get(0).getIsFertilizerApplied() == 1 ? 1 : 2);
            Monthyear.setText(mFertilizerModelArray.get(0).getApplicationMonth() == null ? "" : mFertilizerModelArray.get(0).getApplicationMonth());

            if (!TextUtils.isEmpty(mFertilizerModelArray.get(0).getApplicationMonth())){
                //Month = mFertilizerModelArray.get(0).getApplicationMonth();
                Month = mFertilizerModelArray.get(0).getApplicationMonth();
            }
            //Month = mFertilizerModelArray.get(0).getApplicationMonth();
            Log.d("Monthyear", mFertilizerModelArray.get(0).getApplicationMonth() + "");
            sourceName.setText(mFertilizerModelArray.get(0).getSourceName() == null ? "" : mFertilizerModelArray.get(0).getSourceName());
            Log.d("sourceOfertilizerSpin", mFertilizerModelArray.get(0).getFertilizersourcetypeid() + "");
            Log.d("apptype", mFertilizerModelArray.get(0).getApplicationType() + "");
            sourceOfertilizerSpin.setSelection(mFertilizerModelArray.get(0).getFertilizersourcetypeid() == null ? 0 : CommonUtilsNavigation.getvalueFromHashMap(fertilizerDataMap, mFertilizerModelArray.get(0).getFertilizersourcetypeid()));
            comments.setText(mFertilizerModelArray.get(0).getComments() == null ? "" : mFertilizerModelArray.get(0).getComments());

            if (mFertilizerModelArray.get(0).getApplicationType() != null) {
                String apptypedesc = mFertilizerModelArray.get(0).getApplicationType();
                Log.d("apptype", apptypedesc);
                //int apptypeid = dataAccessHandler.getOnlyOneIntValueFromDb(Queries.getInstance().getapptypeId(apptypedesc));
                apptype.setSelection(mFertilizerModelArray.get(0).getApplicationType() == null ? 0 : CommonUtilsNavigation.getvalueFromHashMap(AppTypeDataMap, Integer.parseInt(apptypedesc)));
            }

            Map<Integer, List<Fertilizer>> biofertilizers = new HashMap<>();
            for (Fertilizer fertilizer : mFertilizerModelArray) {
                biofertilizers.computeIfAbsent(fertilizer.getFertilizerid(), k -> new ArrayList<>()).add(fertilizer);
            }
            int specificFertilizerId = 232;
            if (biofertilizers.containsKey(specificFertilizerId)) {
                List<Fertilizer> specificFertilizers = biofertilizers.get(specificFertilizerId);
                Biofertilizercount = specificFertilizers.size();

                System.out.println("Same Fertilizer ID: " + specificFertilizerId + " - Number of Biofertilizers: " + Biofertilizercount);
                for (Fertilizer biofertilizerData : specificFertilizers) {
                    System.out.println("   Fertilizer ID: " + biofertilizerData.getFertilizerid() + " - Biofertilizer ID: " + biofertilizerData.getBioFertilizerId());
                }
            } else {
                System.out.println("Fertilizer ID " + specificFertilizerId + " not found in the list.");
            }
            for (int i = 0; i < arraySize; i++) {
                if (Biofertilizercount == 5) {
                    Log.e("====>232", mFertilizerModelArray.get(arraySize - 5).getBioFertilizerId() + "");

                    // Setting bioFertilizerSpin
                    if (mFertilizerModelArray.get(arraySize - 5).getBioFertilizerId() != null) {
                        bioFertilizerSpin.setSelection(CommonUtilsNavigation.getvalueFromHashMap(bioDataMap, mFertilizerModelArray.get(arraySize - 5).getBioFertilizerId()));
                        Integer dosage5 = getDosageForBioId(mFertilizerModelArray, mFertilizerModelArray.get(arraySize - 5).getBioFertilizerId());
                        if (dosage5 != null) {
                            dosageGivenEdt7.setText(String.valueOf(dosage5));
                        }
                    }

                    // Setting bioFertilizerSpin2
                    if (mFertilizerModelArray.get(arraySize - 4).getBioFertilizerId() != null) {
                        bioFertilizerSpin2.setSelection(CommonUtilsNavigation.getvalueFromHashMap(bioDataMap, mFertilizerModelArray.get(arraySize - 4).getBioFertilizerId()));
                        Integer dosage6 = getDosageForBioId(mFertilizerModelArray, mFertilizerModelArray.get(arraySize - 4).getBioFertilizerId());
                        if (dosage6 != null) {
                            dosageGivenEdt8.setText(String.valueOf(dosage6));
                        }
                    }

                    // Setting bioFertilizerSpin3
                    if (mFertilizerModelArray.get(arraySize - 3).getBioFertilizerId() != null) {
                        bioFertilizerSpin3.setSelection(CommonUtilsNavigation.getvalueFromHashMap(bioDataMap, mFertilizerModelArray.get(arraySize - 3).getBioFertilizerId()));
                        Integer dosage7 = getDosageForBioId(mFertilizerModelArray, mFertilizerModelArray.get(arraySize - 3).getBioFertilizerId());
                        if (dosage7 != null) {
                            dosageGivenEdt31.setText(String.valueOf(dosage7)); // Assuming a dosage EditText for bioFertilizerSpin3
                        }
                    }

                    // Setting bioFertilizerSpin4
                    if (mFertilizerModelArray.get(arraySize - 2).getBioFertilizerId() != null) {
                        bioFertilizerSpin4.setSelection(CommonUtilsNavigation.getvalueFromHashMap(bioDataMap, mFertilizerModelArray.get(arraySize - 2).getBioFertilizerId()));
                        Integer dosage8 = getDosageForBioId(mFertilizerModelArray, mFertilizerModelArray.get(arraySize - 2).getBioFertilizerId());
                        if (dosage8 != null) {
                            dosageGivenEdt41.setText(String.valueOf(dosage8)); // Assuming a dosage EditText for bioFertilizerSpin4
                        }
                    }

                    // Setting bioFertilizerSpin5
                    if (mFertilizerModelArray.get(arraySize - 1).getBioFertilizerId() != null) {
                        bioFertilizerSpin5.setSelection(CommonUtilsNavigation.getvalueFromHashMap(bioDataMap, mFertilizerModelArray.get(arraySize - 1).getBioFertilizerId()));
                        Integer dosage9 = getDosageForBioId(mFertilizerModelArray, mFertilizerModelArray.get(arraySize - 1).getBioFertilizerId());
                        if (dosage9 != null) {
                            dosageGivenEdt51.setText(String.valueOf(dosage9)); // Assuming a dosage EditText for bioFertilizerSpin5
                        }
                    }

                } else if (Biofertilizercount == 4) {
                    // Setting bioFertilizerSpin
                    if (mFertilizerModelArray.get(arraySize - 4).getBioFertilizerId() != null) {
                        bioFertilizerSpin.setSelection(CommonUtilsNavigation.getvalueFromHashMap(bioDataMap, mFertilizerModelArray.get(arraySize - 4).getBioFertilizerId()));
                        Integer dosage5 = getDosageForBioId(mFertilizerModelArray, mFertilizerModelArray.get(arraySize - 4).getBioFertilizerId());
                        if (dosage5 != null) {
                            dosageGivenEdt7.setText(String.valueOf(dosage5));
                        }
                    }

                    // Setting bioFertilizerSpin2
                    if (mFertilizerModelArray.get(arraySize - 3).getBioFertilizerId() != null) {
                        bioFertilizerSpin2.setSelection(CommonUtilsNavigation.getvalueFromHashMap(bioDataMap, mFertilizerModelArray.get(arraySize - 3).getBioFertilizerId()));
                        Integer dosage6 = getDosageForBioId(mFertilizerModelArray, mFertilizerModelArray.get(arraySize - 3).getBioFertilizerId());
                        if (dosage6 != null) {
                            dosageGivenEdt8.setText(String.valueOf(dosage6));
                        }
                    }

                    // Setting bioFertilizerSpin3
                    if (mFertilizerModelArray.get(arraySize - 2).getBioFertilizerId() != null) {
                        bioFertilizerSpin3.setSelection(CommonUtilsNavigation.getvalueFromHashMap(bioDataMap, mFertilizerModelArray.get(arraySize - 2).getBioFertilizerId()));
                        Integer dosage7 = getDosageForBioId(mFertilizerModelArray, mFertilizerModelArray.get(arraySize - 2).getBioFertilizerId());
                        if (dosage7 != null) {
                            dosageGivenEdt31.setText(String.valueOf(dosage7));
                        }
                    }

                    // Setting bioFertilizerSpin4
                    if (mFertilizerModelArray.get(arraySize - 1).getBioFertilizerId() != null) {
                        bioFertilizerSpin4.setSelection(CommonUtilsNavigation.getvalueFromHashMap(bioDataMap, mFertilizerModelArray.get(arraySize - 1).getBioFertilizerId()));
                        Integer dosage8 = getDosageForBioId(mFertilizerModelArray, mFertilizerModelArray.get(arraySize - 1).getBioFertilizerId());
                        if (dosage8 != null) {
                            dosageGivenEdt41.setText(String.valueOf(dosage8));
                        }
                    }

                } else if (Biofertilizercount == 3) {
                    // Setting bioFertilizerSpin
                    if (mFertilizerModelArray.get(arraySize - 3).getBioFertilizerId() != null) {
                        bioFertilizerSpin.setSelection(CommonUtilsNavigation.getvalueFromHashMap(bioDataMap, mFertilizerModelArray.get(arraySize - 3).getBioFertilizerId()));
                        Integer dosage5 = getDosageForBioId(mFertilizerModelArray, mFertilizerModelArray.get(arraySize - 3).getBioFertilizerId());
                        if (dosage5 != null) {
                            dosageGivenEdt7.setText(String.valueOf(dosage5));
                        }
                    }

                    // Setting bioFertilizerSpin2
                    if (mFertilizerModelArray.get(arraySize - 2).getBioFertilizerId() != null) {
                        bioFertilizerSpin2.setSelection(CommonUtilsNavigation.getvalueFromHashMap(bioDataMap, mFertilizerModelArray.get(arraySize - 2).getBioFertilizerId()));
                        Integer dosage6 = getDosageForBioId(mFertilizerModelArray, mFertilizerModelArray.get(arraySize - 2).getBioFertilizerId());
                        if (dosage6 != null) {
                            dosageGivenEdt8.setText(String.valueOf(dosage6));
                        }
                    }

                    // Setting bioFertilizerSpin3
                    if (mFertilizerModelArray.get(arraySize - 1).getBioFertilizerId() != null) {
                        bioFertilizerSpin3.setSelection(CommonUtilsNavigation.getvalueFromHashMap(bioDataMap, mFertilizerModelArray.get(arraySize - 1).getBioFertilizerId()));
                        Integer dosage7 = getDosageForBioId(mFertilizerModelArray, mFertilizerModelArray.get(arraySize - 1).getBioFertilizerId());
                        if (dosage7 != null) {
                            dosageGivenEdt31.setText(String.valueOf(dosage7));
                        }
                    }
                }
                    else if(Biofertilizercount == 2){
                    Log.e("====>232", mFertilizerModelArray.get(arraySize-1).getBioFertilizerId() + "");

                    if (mFertilizerModelArray.get(arraySize-2).getBioFertilizerId() != null) {
                        Log.e("====>232", mFertilizerModelArray.get(arraySize-2).getBioFertilizerId() + "");
                        bioFertilizerSpin.setSelection(mFertilizerModelArray.get(arraySize-2).getBioFertilizerId() == null ? 0 : CommonUtilsNavigation.getvalueFromHashMap(bioDataMap, mFertilizerModelArray.get(arraySize-2).getBioFertilizerId()));
                        Integer dosage8 = getDosageForBioId(mFertilizerModelArray, mFertilizerModelArray.get(arraySize-2).getBioFertilizerId());
                        if (dosage8 != null) {
                            dosageGivenEdt7.setText(String.valueOf(dosage8));
                        }
                    }
                    Log.e("====>273", mFertilizerModelArray.get(arraySize-1).getBioFertilizerId() + "");

                    if (mFertilizerModelArray.get(arraySize-1).getBioFertilizerId() != null) {
                        Log.e("====>276", mFertilizerModelArray.get(arraySize-1).getBioFertilizerId() + "");
                        bioFertilizerSpin2.setSelection(mFertilizerModelArray.get(arraySize-1).getBioFertilizerId() == null ? 0 : CommonUtilsNavigation.getvalueFromHashMap(bioDataMap, mFertilizerModelArray.get(arraySize-1).getBioFertilizerId()));
                        Integer dosage9 = getDosageForBioId(mFertilizerModelArray, mFertilizerModelArray.get(arraySize-1).getBioFertilizerId());
                        if (dosage9 != null) {
                            dosageGivenEdt8.setText(String.valueOf(dosage9));
                        }
                    }
                }
                else if(Biofertilizercount == 1){
                    Log.e("====>232", mFertilizerModelArray.get(arraySize-1).getBioFertilizerId() + "");

                    if (mFertilizerModelArray.get(arraySize-1).getBioFertilizerId() != null) {
                        Log.e("====>232", mFertilizerModelArray.get(arraySize-1).getBioFertilizerId() + "");
                        bioFertilizerSpin.setSelection(mFertilizerModelArray.get(arraySize-1).getBioFertilizerId() == null ? 0 : CommonUtilsNavigation.getvalueFromHashMap(bioDataMap, mFertilizerModelArray.get(arraySize-1).getBioFertilizerId()));
                        Integer dosage8 = getDosageForBioId(mFertilizerModelArray, mFertilizerModelArray.get(arraySize-1).getBioFertilizerId());
                        if (dosage8 != null) {
                            dosageGivenEdt7.setText(String.valueOf(dosage8));
                        }
                    }
                }
            }

//            for (int i = 0; i< arraySize; i++) {
//                if(Biofertilizercount == 2){
//                    Log.e("====>232", mFertilizerModelArray.get(arraySize-1).getBioFertilizerId() + "");
//
//                    if (mFertilizerModelArray.get(arraySize-2).getBioFertilizerId() != null) {
//                        Log.e("====>232", mFertilizerModelArray.get(arraySize-2).getBioFertilizerId() + "");
//                        bioFertilizerSpin.setSelection(mFertilizerModelArray.get(arraySize-2).getBioFertilizerId() == null ? 0 : CommonUtilsNavigation.getvalueFromHashMap(bioDataMap, mFertilizerModelArray.get(arraySize-2).getBioFertilizerId()));
//                        Integer dosage8 = getDosageForBioId(mFertilizerModelArray, mFertilizerModelArray.get(arraySize-2).getBioFertilizerId());
//                        if (dosage8 != null) {
//                            dosageGivenEdt7.setText(String.valueOf(dosage8));
//                        }
//                    }
//                    Log.e("====>273", mFertilizerModelArray.get(arraySize-1).getBioFertilizerId() + "");
//
//                    if (mFertilizerModelArray.get(arraySize-1).getBioFertilizerId() != null) {
//                        Log.e("====>276", mFertilizerModelArray.get(arraySize-1).getBioFertilizerId() + "");
//                        bioFertilizerSpin2.setSelection(mFertilizerModelArray.get(arraySize-1).getBioFertilizerId() == null ? 0 : CommonUtilsNavigation.getvalueFromHashMap(bioDataMap, mFertilizerModelArray.get(arraySize-1).getBioFertilizerId()));
//                        Integer dosage9 = getDosageForBioId(mFertilizerModelArray, mFertilizerModelArray.get(arraySize-1).getBioFertilizerId());
//                        if (dosage9 != null) {
//                            dosageGivenEdt8.setText(String.valueOf(dosage9));
//                        }
//                    }
//                }
//                else if(Biofertilizercount == 1){
//                    Log.e("====>232", mFertilizerModelArray.get(arraySize-1).getBioFertilizerId() + "");
//
//                    if (mFertilizerModelArray.get(arraySize-1).getBioFertilizerId() != null) {
//                        Log.e("====>232", mFertilizerModelArray.get(arraySize-1).getBioFertilizerId() + "");
//                        bioFertilizerSpin.setSelection(mFertilizerModelArray.get(arraySize-1).getBioFertilizerId() == null ? 0 : CommonUtilsNavigation.getvalueFromHashMap(bioDataMap, mFertilizerModelArray.get(arraySize-1).getBioFertilizerId()));
//                        Integer dosage8 = getDosageForBioId(mFertilizerModelArray, mFertilizerModelArray.get(arraySize-1).getBioFertilizerId());
//                        if (dosage8 != null) {
//                            dosageGivenEdt7.setText(String.valueOf(dosage8));
//                        }
//                    }
//                }
//
//
//            }


            Integer dosage1 = getDosageForFertilizerId(mFertilizerModelArray, 58);

            if(dosage1 != 0 ){
                dosageGivenEdt5.setText(String.valueOf(dosage1));}

            Integer dosage2 = getDosageForFertilizerId(mFertilizerModelArray, 59);
            if(dosage2 != 0){
                dosageGivenEdt.setText(String.valueOf(dosage2));}

            Integer dosage3 = getDosageForFertilizerId(mFertilizerModelArray, 60);
            if(dosage3 != 0){
                dosageGivenEdt2.setText(String.valueOf(dosage3));}

            Integer dosage4 = getDosageForFertilizerId(mFertilizerModelArray, 61);
            if(dosage4 != 0){
                dosageGivenEdt1.setText(String.valueOf(dosage4));}

            Integer dosage5 = getDosageForFertilizerId(mFertilizerModelArray, 63);
            if(dosage5 != 0){
                dosageGivenEdt3.setText(String.valueOf(dosage5));}

            Integer dosage6 = getDosageForFertilizerId(mFertilizerModelArray, 64);
            if(dosage6 != 0) {
                dosageGivenEdt4.setText(String.valueOf(dosage6));
            }
            Integer dosage7 = getDosageForFertilizerId(mFertilizerModelArray, 229);
            if(dosage7 != 0){
                dosageGivenEdt6.setText(String.valueOf(dosage7));
            }
        }
    }



    private int getDosageForBioId(List<Fertilizer> mFertilizerModelArray, Integer bioFertilizerId) {
        for (Fertilizer model : mFertilizerModelArray) {
            Log.e("==========>model", model.getBioFertilizerId() + "" + bioFertilizerId);
            if (model.getBioFertilizerId() != null && model.getBioFertilizerId().equals(bioFertilizerId)) {
                Log.e("==========>model2", model.getBioFertilizerId() + "" + bioFertilizerId);
                return (int) model.getDosage();
            }
        }
        return 0; // Return a default value if the fertilizer ID is not found.
    }
    public boolean contains(final int[] array, final int key) {
        return ArrayUtils.contains(array, key);
    }

    private int getDosageForFertilizerId(List<Fertilizer> mFertilizerModelArray, int fertilizerId) {
        for (Fertilizer model : mFertilizerModelArray) {
            if (model.getFertilizerid() == fertilizerId) {
                return (int) model.getDosage();
            }
        }
        return 0; // Return a default value if the fertilizer ID is not found.
    }

    private void initViews() {
        sourceOfertilizerSpin = (Spinner) rootView.findViewById(R.id.sourceOfertilizerSpin);
        //    psourceOfertilizerSpin = (Spinner) rootView.findViewById(R.id.psourceOfertilizerSpin);
        apptype = (Spinner) rootView.findViewById(R.id.apptype);
        //   papptype = (Spinner) rootView.findViewById(R.id.papptype);
        fertilizerapplied = (Spinner) rootView.findViewById(R.id.FertilizerApplied);
        //   pfertilizerapplied = (Spinner) rootView.findViewById(R.id.pFertilizerApplied);
        fertilizerProductNameSpin = (Spinner) rootView.findViewById(R.id.fertilizerProductNameSpin);
        uomSpin = (Spinner) rootView.findViewById(R.id.uomSpin);
        // frequencyOfApplicationSpin = (Spinner) rootView.findViewById(R.id.frequencyOfApplicationSpin);
        dosageGivenEdt = (EditText) rootView.findViewById(R.id.dosageGivenEdt);
        dosageGivenEdt1 = (EditText) rootView.findViewById(R.id.dosageGivenEdt1);
        dosageGivenEdt2 = (EditText) rootView.findViewById(R.id.dosageGivenEdt2);
        dosageGivenEdt3 = (EditText) rootView.findViewById(R.id.dosageGivenEdt3);
        dosageGivenEdt4 = (EditText) rootView.findViewById(R.id.dosageGivenEdt4);
        dosageGivenEdt5 = (EditText) rootView.findViewById(R.id.dosageGivenEdt5);
        dosageGivenEdt6 = (EditText) rootView.findViewById(R.id.dosageGivenEdt6);
        dosageGivenEdt7 = (EditText) rootView.findViewById(R.id.dosageGivenEdt7);
        dosageGivenEdt8 = (EditText) rootView.findViewById(R.id.dosageGivenEdt8);
        bioFertilizerSpin = (Spinner) rootView.findViewById(R.id.bioFertilizerSpin);
        bioFertilizerSpin2 = (Spinner) rootView.findViewById(R.id.bioFertilizerSpin2);
        bioFertilizerSpin3 = (Spinner) rootView.findViewById(R.id.bioFertilizerSpin3);
        bioFertilizerSpin4 = (Spinner) rootView.findViewById(R.id.bioFertilizerSpin4);
        bioFertilizerSpin5 = (Spinner) rootView.findViewById(R.id.bioFertilizerSpin5);
        dosageGivenEdt31 = (EditText) rootView.findViewById(R.id.dosageGivenEdt31);
        dosageGivenEdt41 = (EditText) rootView.findViewById(R.id.dosageGivenEdt41);
        dosageGivenEdt51 = (EditText) rootView.findViewById(R.id.dosageGivenEdt51);
//        lastAppliedDateEdt3 = (EditText) rootView.findViewById(R.id.lastAppliedDateEdt3);
//        lastAppliedDateEdt4 = (EditText) rootView.findViewById(R.id.lastAppliedDateEdt4);
//        lastAppliedDateEdt5 = (EditText) rootView.findViewById(R.id.lastAppliedDateEdt5);
//        lastAppliedDateEdt6 = (EditText) rootView.findViewById(R.id.lastAppliedDateEdt6);
//        lastAppliedDateEdt7 = (EditText) rootView.findViewById(R.id.lastAppliedDateEdt7);
//        lastAppliedDateEdt8 = (EditText) rootView.findViewById(R.id.lastAppliedDateEdt8);
        comments = (EditText) rootView.findViewById(R.id.commentsTv);
        //  pcomments = (EditText) rootView.findViewById(R.id.pcommentsTv);
        sourceName = (EditText) rootView.findViewById(R.id.nameofshopEv);
        //    psourceName = (EditText) rootView.findViewById(R.id.pnameofshopEv);
        Monthyear = (EditText) rootView.findViewById(R.id.Monthyear);
        //     pMonthyear = (EditText) rootView.findViewById(R.id.pMonthyear);
        //  lastAppliedDateEdt6 = (EditText) rootView.findViewById(R.id.lastAppliedDateEdt6);
        otherEdt = (EditText) rootView.findViewById(R.id.otherEdt);
        otherLay = (LinearLayout) rootView.findViewById(R.id.otherLay);
        fertilizerList = (RecyclerView) rootView.findViewById(R.id.fertilizerList);
        saveBtn = (Button) rootView.findViewById(R.id.saveBtn);
        historyBtn = (Button) rootView.findViewById(R.id.historyBtn);
        pdfBtn = (Button) rootView.findViewById(R.id.pdfBtn);
        complaintsBtn = (Button) rootView.findViewById(R.id.complaintsBtn);
        complaintsBtn.setVisibility(View.GONE);
        complaintsBtn.setEnabled(false);
        headerLL = (LinearLayout) rootView.findViewById(R.id.headerLL);
//        tv3 =  (TextView) rootView.findViewById(R.id.tv3);
//        tv4 =  (TextView) rootView.findViewById(R.id.id4);

        tv3 =  (TextView) rootView.findViewById(R.id.currentqtr);
        tv4 =  (TextView) rootView.findViewById(R.id.prvsqtr);

        if (cropMaintenanceDocs != null) {

            if (null != fileToDownLoad && fileToDownLoad.exists()) {

                pdfBtn.setVisibility(View.VISIBLE);

            } else {
                pdfBtn.setVisibility(View.GONE);
            }
        }

        Calendar calendar = Calendar.getInstance();
        cy = calendar.get(Calendar.YEAR);
        cm = calendar.get(Calendar.MONTH);

        if(cm>=0 && cm<=2){
            cy=cy-1;

            cq=4;
            py=cy;
            pq=3;
            caly=cy+1;
            pcaly=py;
            calendar.clear();
            calendar.set(caly, 0, 1);
            mindate = calendar.getTimeInMillis();

            calendar.clear();
            calendar.set(caly, 2, 31);
            maxdate = calendar.getTimeInMillis();

            calendar.clear();
            calendar.set(pcaly, 9, 1);
            ppmindate = calendar.getTimeInMillis();

            calendar.clear();
            calendar.set(pcaly, 11, 31);
            ppmaxdate = calendar.getTimeInMillis();
            pm=8;
            pmindate=String.valueOf(pcaly)+"-10-01";
            pmaxdate=String.valueOf(pcaly)+"-12-31";
            cmindate=String.valueOf(caly)+"-01-01";
            cmaxdate=String.valueOf(caly)+"-03-31";
        }else if (cm>=3 && cm<=5){
            cq=1;
            py=cy-1;
            pq=4;
            caly=cy;
            pcaly=py+1;
            calendar.clear();
            calendar.set(caly, 3, 1);
            mindate = calendar.getTimeInMillis();

            calendar.clear();
            calendar.set(caly, 5, 30);
            maxdate = calendar.getTimeInMillis();

            calendar.clear();
            calendar.set(pcaly, 0, 1);
            ppmindate = calendar.getTimeInMillis();

            calendar.clear();
            calendar.set(pcaly, 2, 31);
            ppmaxdate = calendar.getTimeInMillis();
            pm=0;
            pmindate=String.valueOf(pcaly)+"-01-01";
            pmaxdate=String.valueOf(pcaly)+"-03-31";
            cmindate=String.valueOf(caly)+"-04-01";
            cmaxdate=String.valueOf(caly)+"-06-30";
        }else if (cm>=6 && cm<=8){
            cq=2;
            py=cy;
            pq=1;
            caly=cy;
            pcaly=py;
            calendar.clear();
            calendar.set(caly, 6, 1);
            mindate = calendar.getTimeInMillis();

            calendar.clear();
            calendar.set(caly, 8, 30);
            maxdate = calendar.getTimeInMillis();

            calendar.clear();
            calendar.set(pcaly, 3, 1);
            ppmindate = calendar.getTimeInMillis();

            calendar.clear();
            calendar.set(pcaly, 5, 30);
            ppmaxdate = calendar.getTimeInMillis();
            pm=3;
            pmindate=String.valueOf(pcaly)+"-04-01";
            pmaxdate=String.valueOf(pcaly)+"-06-30";
            cmindate=String.valueOf(caly)+"-07-01";
            cmaxdate=String.valueOf(caly)+"-09-30";
        }else if (cm>=9 && cm<=11){
            cq=3;
            py=cy;
            pq=2;
            caly=cy;
            pcaly=py;
            calendar.clear();
            calendar.set(caly, 9, 1);
            mindate = calendar.getTimeInMillis();

            calendar.clear();
            calendar.set(caly, 11, 31);
            maxdate = calendar.getTimeInMillis();

            calendar.clear();
            calendar.set(pcaly, 6, 1);
            ppmindate = calendar.getTimeInMillis();

            calendar.clear();
            calendar.set(pcaly, 8, 30);
            ppmaxdate = calendar.getTimeInMillis();
            pm=6;
            pmindate=String.valueOf(pcaly)+"-07-01";
            pmaxdate=String.valueOf(pcaly)+"-09-30";
            cmindate=String.valueOf(caly)+"-10-01";
            cmaxdate=String.valueOf(caly)+"-12-31";
        }
        tv3.setText(String.valueOf(cq)+"-"+"Qtr"+"-"+String.valueOf(cy));
        tv3.setText(String.valueOf(cq)+"-"+"Qtr"+"-"+String.valueOf(cy));


        months = new ArrayList<>();
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");
        //complaintsBtn.setVisibility((CommonUiUtils.isComplaintsDataEntered()) ? View.GONE : View.VISIBLE);
        Monthyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                        .getInstance(cm, caly,mindate,maxdate);

                dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int monthOfYear) {

                        // do something

                        Month = months.get(monthOfYear);
                        Year =cy;
                        Quater = cq;
                        Monthnumber =String.valueOf(monthOfYear+1).length()==1? "0"+String.valueOf(monthOfYear+1) : String.valueOf(monthOfYear+1);
                        Monthyear.setText(Month+"-"+String.valueOf(year));
                        applydate = String.valueOf(year)+"-"+Monthnumber+"-01 00:00:00";
                        CommonConstants.fertilizerapplydate = applydate;

                        //Toast.makeText(getActivity().getApplicationContext(),String.valueOf(year)+"-"+String.valueOf(monthOfYear),Toast.LENGTH_SHORT);
                    }
                });

                dialogFragment.show(getActivity().getSupportFragmentManager(), null);


            }

        });

//        pMonthyear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
//                        .getInstance(pm, pcaly,ppmindate,ppmaxdate);
//
//                dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(int year, int monthOfYear) {
//
//                        // do something
//
//                        pMonth = months.get(monthOfYear);
//                        pYear=cy;
//                        pQuater = pq;
//                        Monthnumber =String.valueOf(monthOfYear+1).length()==1? "0"+String.valueOf(monthOfYear+1) : String.valueOf(monthOfYear+1);
//                        pMonthyear.setText(pMonth+"-"+String.valueOf(year));
//                        papplydate = String.valueOf(year)+"-"+Monthnumber+"-01 00:00:00";
//
//                        //Toast.makeText(getActivity().getApplicationContext(),String.valueOf(year)+"-"+String.valueOf(monthOfYear),Toast.LENGTH_SHORT);
//                    }
//                });
//
//                dialogFragment.show(getActivity().getSupportFragmentManager(), null);
//
//
//            }
//
//        });


        complaintsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle dataBundle = new Bundle();
                dataBundle.putBoolean(CommonConstants.KEY_NEW_COMPLAINT, true);
                ComplaintDetailsFragment complaintDetailsFragment = new ComplaintDetailsFragment();
                complaintDetailsFragment.setArguments(dataBundle);
                complaintDetailsFragment.setUpdateUiListener(FertilizerFragment.this);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(android.R.id.content, complaintDetailsFragment).addToBackStack(null)
                        .commit();
            }
        });
    }

    //    private void fetchprevqtr(){
//        int yr=py;
//        int qr=pq;
//
//        DataAccessHandler dataAccessHandler = new DataAccessHandler(getActivity());
//
//
//        CMCode   =   dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getFertilizerPrevQtrCM(qr,yr,CommonConstants.PLOT_CODE,pmindate,pmaxdate));
//
//        if(CMCode!=null){
//            CommonConstants.Prev_Fertilizer_CMD=CMCode;
//            pMonthyear.setEnabled(false);
//            psourceName.setEnabled(false);
//            psourceOfertilizerSpin.setEnabled(false);
//            pfertilizerapplied.setEnabled(false);
//            pcomments.setEnabled(false);
//            papptype.setEnabled(false);
//            lastAppliedDateEdt.setEnabled(false);
//            lastAppliedDateEdt1.setEnabled(false);
//            lastAppliedDateEdt2.setEnabled(false);
//            lastAppliedDateEdt3.setEnabled(false);
//            lastAppliedDateEdt4.setEnabled(false);
//            lastAppliedDateEdt5.setEnabled(false);
//            lastAppliedDateEdt6.setEnabled(false);
//            lastAppliedDateEdt7.setEnabled(false);
//            lastAppliedDateEdt8.setEnabled(false);
//
//
//            lastAppliedDateEdt.setText(dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getFertilizerPrevQtrDosage(qr,yr,CMCode,59,pmindate,pmaxdate)));
//            lastAppliedDateEdt1.setText(dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getFertilizerPrevQtrDosage(qr,yr,CMCode,61,pmindate,pmaxdate)));
//            lastAppliedDateEdt2.setText(dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getFertilizerPrevQtrDosage(qr,yr,CMCode,60,pmindate,pmaxdate)));
//            lastAppliedDateEdt3.setText(dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getFertilizerPrevQtrDosage(qr,yr,CMCode,63,pmindate,pmaxdate)));
//            lastAppliedDateEdt4.setText(dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getFertilizerPrevQtrDosage(qr,yr,CMCode,64,pmindate,pmaxdate)));
//            lastAppliedDateEdt5.setText(dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getFertilizerPrevQtrDosage(qr,yr,CMCode,58,pmindate,pmaxdate)));
//            lastAppliedDateEdt6.setText(dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getFertilizerPrevQtrDosage(qr,yr,CMCode,229,pmindate,pmaxdate)));
//            lastAppliedDateEdt7.setText(dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getBioFertilizerPrevQtrDosage(qr,yr,CMCode,232,bioFertilizerId,pmindate,pmaxdate)));
//            lastAppliedDateEdt8.setText(dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getBioFertilizerPrevQtrDosage(qr,yr,CMCode,232,bioFertilizerId2,pmindate,pmaxdate)));
//            List<Fertilizer> fertilizerList = (List<Fertilizer>) dataAccessHandler.getFertilizerPrevQtrdtls(Queries.getInstance().getFertilizerPrevQtrdtls(qr,yr,CMCode,pmindate,pmaxdate),1);
//            if(fertilizerList.size()>0){
//
//                //psourceName.setText(fertilizerList.get(0).getSourceName());
//
//                if (fertilizerList.get(0).getSourceName().isEmpty() || fertilizerList.get(0).getSourceName() == null){
//
//                //    psourceName.setText(fertilizerList.get(0).getSourceName());
//                }else{
//
//                //    psourceName.setText("");
//                }
//
//                //pcomments.setText(fertilizerList.get(0).getComments());
//
//                if (fertilizerList.get(0).getComments().isEmpty() || fertilizerList.get(0).getComments() == null){
//
//                    pcomments.setText(fertilizerList.get(0).getComments());
//                }else{
//
//                    pcomments.setText("");
//                }
//
//                Set<String> keys = AppTypeDataMap.keySet();
//                int p=1;
//                if(fertilizerList.get(0).getApplicationType()!=null && !fertilizerList.get(0).getApplicationType().equals("null"))
//                {
//                    for(String k:keys){
//                        if(k.equals(fertilizerList.get(0).getApplicationType()))
//                        {
//                            break;
//                        }
//                        p++;
//                    }
//                    papptype.setSelection(p);
//                }else{
//                    papptype.setSelection(0);
//                }
//
//                if(fertilizerList.get(0).getFertilizersourcetypeid()!=null) {
//                    keys = fertilizerDataMap.keySet();
//                    p = 1;
//                    for (String k : keys) {
//                        if (k.equals(fertilizerList.get(0).getFertilizersourcetypeid().toString())) {
//                            break;
//                        }
//                        p++;
//                    }
//                    psourceOfertilizerSpin.setSelection(p);
//                }else
//                    psourceOfertilizerSpin.setSelection(0);
//
//
//                keys = IsAppliedDataMap.keySet();
//                p = 1;
//                for (String k : keys) {
//                    if (k.equals(String.valueOf(fertilizerList.get(0).getIsFertilizerApplied()))) {
//                        break;
//                    }
//                    p++;
//                }
//                pfertilizerapplied.setSelection(p);
//
//                if (fertilizerList.get(0).getApplicationMonth().contains("null") || fertilizerList.get(0).getApplicationMonth().isEmpty()){
//
//                   // pMonthyear.setText("");
//                }else{
//
//                 //   pMonthyear.setText(fertilizerList.get(0).getApplicationMonth()+"-"+String.valueOf(fertilizerList.get(0).getApplicationYear()));
//                }
//
//
//
//            }
//        }
//        else {
////            pMonthyear.setEnabled(true);
////            psourceName.setEnabled(true);
////            psourceOfertilizerSpin.setEnabled(true);
////            pfertilizerapplied.setEnabled(true);
////            pcomments.setEnabled(true);
////            papptype.setEnabled(true);
////            lastAppliedDateEdt.setEnabled(true);
////            lastAppliedDateEdt1.setEnabled(true);
////            lastAppliedDateEdt2.setEnabled(true);
////            lastAppliedDateEdt3.setEnabled(true);
////            lastAppliedDateEdt4.setEnabled(true);
////            lastAppliedDateEdt5.setEnabled(true);
////            lastAppliedDateEdt6.setEnabled(true);
////            lastAppliedDateEdt7.setEnabled(true);
////            lastAppliedDateEdt8.setEnabled(true);
//
//        }
//
//    }
    private void setViews() {

        // updateLabel();
        saveBtn.setOnClickListener(this);
        historyBtn.setOnClickListener(this);
        pdfBtn.setOnClickListener(this);
//        lastAppliedDateEdt.setOnClickListener(this);
//        lastAppliedDateEdt1.setOnClickListener(this);
//        lastAppliedDateEdt2.setOnClickListener(this);
//        lastAppliedDateEdt3.setOnClickListener(this);
//        lastAppliedDateEdt4.setOnClickListener(this);
//        lastAppliedDateEdt5.setOnClickListener(this);
//        lastAppliedDateEdt6.setOnClickListener(this);
//        lastAppliedDateEdt7.setOnClickListener(this);
//        lastAppliedDateEdt8.setOnClickListener(this);

        /*fertilizerProductNameSpin.setOnItemSelectedListener(spinListener);*/

        IsAppliedDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getYesNo());
        bioDataMap=dataAccessHandler.getGenericData(Queries.getInstance().getTypeCdDmtData("66"));
        AppTypeDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getTypeCdDmtData("65"));
        fertilizerDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getTypeCdDmtData("33"));
        fertilizerTypeDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getLookUpData("23"));
        frequencyOfApplicationDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getTypeCdDmtData("30"));
        uomDataMap = dataAccessHandler.getGenericData(Queries.getInstance().getUOM());

        bioFertilizerSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(),"Bio Fertilizer1",bioDataMap));

        bioFertilizerSpin2.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(),"Bio Fertilizer2",bioDataMap));
        bioFertilizerSpin3.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(),"Bio Fertilizer3",bioDataMap));

        bioFertilizerSpin4.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(),"Bio Fertilizer4",bioDataMap));
        bioFertilizerSpin5.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(),"Bio Fertilizer5",bioDataMap));



        sourceOfertilizerSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Source of Fertilizer", fertilizerDataMap));
        //  psourceOfertilizerSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Source of Fertilizer", fertilizerDataMap));

        apptype.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Application Type", AppTypeDataMap));
        //  papptype.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Application Type", AppTypeDataMap));

        fertilizerapplied.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Fertilizer Applied", IsAppliedDataMap));
        // pfertilizerapplied.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Fertilizer Applied", IsAppliedDataMap));
        //fertilizerProductNameSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "fertilizer Product Name", fertilizerTypeDataMap));
        //uomSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Select UOM", uomDataMap));
        //  frequencyOfApplicationSpin.setAdapter(CommonUtilsNavigation.adapterSetFromHashmap(getActivity(), "Select Frequency of Application / yr", frequencyOfApplicationDataMap));


        fertilizerapplied.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (fertilizerapplied.getSelectedItemPosition() == 2){
                    Monthyear.setText("");
                    sourceName.setText("");
                    sourceOfertilizerSpin.setSelection(0);
                    comments.setText("");
                    apptype.setSelection(0);
                    dosageGivenEdt.setText("");
                    dosageGivenEdt1.setText("");
                    dosageGivenEdt2.setText("");
                    dosageGivenEdt3.setText("");
                    dosageGivenEdt4.setText("");
                    dosageGivenEdt5.setText("");
                    dosageGivenEdt6.setText("");
                    dosageGivenEdt7.setText("");
                    dosageGivenEdt8.setText("");
                    dosageGivenEdt31.setText("");
                    dosageGivenEdt41.setText("");
                    Monthyear.setEnabled(false);
                    sourceName.setEnabled(false);
                    //   sourceOfertilizerSpin.setSelection(0);
                    sourceOfertilizerSpin.setEnabled(false);
                    comments.setEnabled(false);
                    //  apptype.setSelection(0);
                    apptype.setEnabled(false);
                    dosageGivenEdt.setEnabled(false);
                    dosageGivenEdt1.setEnabled(false);
                    dosageGivenEdt2.setEnabled(false);
                    dosageGivenEdt3.setEnabled(false);
                    dosageGivenEdt4.setEnabled(false);
                    dosageGivenEdt5.setEnabled(false);
                    dosageGivenEdt6.setEnabled(false);
                    dosageGivenEdt7.setEnabled(false);
                    dosageGivenEdt8.setEnabled(false);
                    dosageGivenEdt31.setEnabled(false);
                    dosageGivenEdt41.setEnabled(false);
                    dosageGivenEdt51.setEnabled(false);
                    bioFertilizerSpin3.setSelection(0);
                    bioFertilizerSpin4.setSelection(0);
                    bioFertilizerSpin5.setSelection(0);
                    bioFertilizerSpin.setSelection(0);
                    bioFertilizerSpin2.setSelection(0);


                }

                if (fertilizerapplied.getSelectedItemPosition() == 1){

                    Monthyear.setEnabled(true);
                    sourceName.setEnabled(true);
                    //  sourceOfertilizerSpin.setSelection(0);
                    sourceOfertilizerSpin.setEnabled(true);
                    comments.setEnabled(true);
                    //    apptype.setSelection(0);
                    apptype.setEnabled(true);
                    dosageGivenEdt.setEnabled(true);
                    dosageGivenEdt1.setEnabled(true);
                    dosageGivenEdt2.setEnabled(true);
                    dosageGivenEdt3.setEnabled(true);
                    dosageGivenEdt4.setEnabled(true);
                    dosageGivenEdt5.setEnabled(true);
                    dosageGivenEdt6.setEnabled(true);
                    dosageGivenEdt7.setEnabled(true);
                    dosageGivenEdt8.setEnabled(true);
                    dosageGivenEdt31.setEnabled(true);
                    dosageGivenEdt41.setEnabled(true);
                    dosageGivenEdt51.setEnabled(true);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        pfertilizerapplied.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//
//                if (pfertilizerapplied.getSelectedItemPosition() == 2){
//
//                    pMonthyear.setEnabled(false);
//                    psourceName.setEnabled(false);
//                    psourceOfertilizerSpin.setSelection(0);
//                    psourceOfertilizerSpin.setEnabled(false);
//                    pcomments.setEnabled(false);
//                    papptype.setSelection(0);
//                    papptype.setEnabled(false);
//                    lastAppliedDateEdt.setEnabled(false);
//                    lastAppliedDateEdt1.setEnabled(false);
//                    lastAppliedDateEdt2.setEnabled(false);
//                    lastAppliedDateEdt3.setEnabled(false);
//                    lastAppliedDateEdt4.setEnabled(false);
//                    lastAppliedDateEdt5.setEnabled(false);
//                    lastAppliedDateEdt6.setEnabled(false);
//                    lastAppliedDateEdt7.setEnabled(false);
//                    lastAppliedDateEdt8.setEnabled(false);
//                }
//
//                if (pfertilizerapplied.getSelectedItemPosition() == 1){
//
//                    pMonthyear.setEnabled(true);
//                    psourceName.setEnabled(true);
//                    psourceOfertilizerSpin.setSelection(0);
//                    psourceOfertilizerSpin.setEnabled(true);
//                    pcomments.setEnabled(true);
//                    papptype.setSelection(0);
//                    papptype.setEnabled(true);
//                    lastAppliedDateEdt.setEnabled(true);
//                    lastAppliedDateEdt1.setEnabled(true);
//                    lastAppliedDateEdt2.setEnabled(true);
//                    lastAppliedDateEdt3.setEnabled(true);
//                    lastAppliedDateEdt4.setEnabled(true);
//                    lastAppliedDateEdt5.setEnabled(true);
//                    lastAppliedDateEdt6.setEnabled(true);
//                    lastAppliedDateEdt7.setEnabled(true);
//                    lastAppliedDateEdt8.setEnabled(true);
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

// Example spinner setup with duplicate check logic
        bioFertilizerSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (bioFertilizerSpin.getSelectedItemPosition() != 0) {
                    bioFertilizerId = Integer.parseInt(bioDataMap.keySet().toArray()[position - 1].toString());
                    selectedbiofertilizer1 = bioFertilizerSpin.getSelectedItem().toString();
                    Log.d("Selected Item1", selectedbiofertilizer1);

                    // Check if selectedbiofertilizer1 matches any other selected fertilizers
                    if (isDuplicateSelected(selectedbiofertilizer1, selectedbiofertilizer2, selectedbiofertilizer3, selectedbiofertilizer4, selectedbiofertilizer5)) {
                        Toast.makeText(mContext, "Duplicate fertilizer selection not allowed", Toast.LENGTH_SHORT).show();
                        bioFertilizerSpin.setSelection(0); // Reset selection
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        bioFertilizerSpin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (bioFertilizerSpin2.getSelectedItemPosition() != 0) {
                    bioFertilizerId2 = Integer.parseInt(bioDataMap.keySet().toArray()[position - 1].toString());
                    selectedbiofertilizer2 = bioFertilizerSpin2.getSelectedItem().toString();
                    Log.d("Selected Item2", selectedbiofertilizer2);
                    Log.d("Selected Item Id 2", bioFertilizerId2 + "");
                    if (isDuplicateSelected(selectedbiofertilizer2, selectedbiofertilizer1, selectedbiofertilizer3, selectedbiofertilizer4, selectedbiofertilizer5)) {
                        Toast.makeText(mContext, "Duplicate fertilizer selection not allowed", Toast.LENGTH_SHORT).show();
                        bioFertilizerSpin2.setSelection(0);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        bioFertilizerSpin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (bioFertilizerSpin3.getSelectedItemPosition() != 0) {
                    bioFertilizerId3 = Integer.parseInt(bioDataMap.keySet().toArray()[position - 1].toString());
                    selectedbiofertilizer3 = bioFertilizerSpin3.getSelectedItem().toString();
                    Log.d("Selected Item3", selectedbiofertilizer3);
                    Log.d("Selected Item Id3", bioFertilizerId3 + "");


                    if (isDuplicateSelected(selectedbiofertilizer3, selectedbiofertilizer1, selectedbiofertilizer2, selectedbiofertilizer4, selectedbiofertilizer5)) {
                        Toast.makeText(mContext, "Duplicate fertilizer selection not allowed", Toast.LENGTH_SHORT).show();
                        bioFertilizerSpin3.setSelection(0);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        bioFertilizerSpin4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (bioFertilizerSpin4.getSelectedItemPosition() != 0) {
                    bioFertilizerId4 = Integer.parseInt(bioDataMap.keySet().toArray()[position - 1].toString());
                    selectedbiofertilizer4 = bioFertilizerSpin4.getSelectedItem().toString();
                    Log.d("Selected Item4", selectedbiofertilizer4);
                    Log.d("Selected Item Id 4", bioFertilizerId4+ "");
                    if (isDuplicateSelected(selectedbiofertilizer4, selectedbiofertilizer1, selectedbiofertilizer2, selectedbiofertilizer3, selectedbiofertilizer5)) {
                        Toast.makeText(mContext, "Duplicate fertilizer selection not allowed", Toast.LENGTH_SHORT).show();
                        bioFertilizerSpin4.setSelection(0);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        bioFertilizerSpin5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (bioFertilizerSpin5.getSelectedItemPosition() != 0) {
                    bioFertilizerId5 = Integer.parseInt(bioDataMap.keySet().toArray()[position - 1].toString());
                    selectedbiofertilizer5 = bioFertilizerSpin5.getSelectedItem().toString();
                    Log.d("Selected Item5", selectedbiofertilizer5);
                    Log.d("Selected Item Id 5", bioFertilizerId5+ "");
                    if (isDuplicateSelected(selectedbiofertilizer5, selectedbiofertilizer1, selectedbiofertilizer2, selectedbiofertilizer3, selectedbiofertilizer4)) {
                        Toast.makeText(mContext, "Duplicate fertilizer selection not allowed", Toast.LENGTH_SHORT).show();
                        bioFertilizerSpin5.setSelection(0);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    

        // fetchprevqtr();
    }

    private boolean isDuplicateSelected(String selected, String... others) {
        for (String other : others) {
            if (selected != null && selected.equals(other)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveBtn:

                mFertilizerModelArray = new ArrayList<Fertilizer>();
                DataManager.getInstance().deleteData(DataManager.FERTILIZER);


               // if(spinnerSelect(fertilizerapplied, "Fertilizer Applied", mContext)){
//                if (DataManager.getInstance().getDataFromManager(DataManager.RECMND_FERTILIZER) != null)
                    if (validateUI()) {
                        Date date;
                        String lastAppliedDate = "";
                        String outputDate;

                        for (int k = 0; k < 12; k++) {
                            Fertilizer mFertilizerModel = new Fertilizer();
                            Fertilizer mFertilizerModelp = new Fertilizer();
                            switch (k) {
                                case 0:
                                    if (dosageGivenEdt.getText().toString().length() > 0 && dosageGivenEdt.getText().toString() != "0") {
                                        //CommonConstants.Prev_Fertilizer_CMD = "Done";
                                        mFertilizerModel.setFertilizerid(59);
                                        mFertilizerModel.setDosage(Double.parseDouble(dosageGivenEdt.getText().toString()));
                                        lastAppliedDate = applydate;
                                        mFertilizerModel.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, sourceOfertilizerSpin.getSelectedItem().toString())));
                                        mFertilizerModel.setSourceName(sourceName.getText().toString());
                                        mFertilizerModel.setComments(comments.getText().toString());
                                        mFertilizerModel.setUomid(1);
                                        mFertilizerModel.setApplicationType(getKey(AppTypeDataMap, apptype.getSelectedItem().toString()));
                                        mFertilizerModel.setApplicationMonth(Month);
                                        mFertilizerModel.setApplicationYear(Year);
                                        //mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
                                        mFertilizerModel.setIsFertilizerApplied(1);
                                        mFertilizerModel.setQuarter(Quater);

                                        mFertilizerModel.setLastapplieddate(CommonConstants.fertilizerapplydate);
                                        mFertilizerModel.setApplyfertilizerfrequencytypeid(null);
                                        mFertilizerModelArray.add(mFertilizerModel);
                                    }

//                                    if (lastAppliedDateEdt.getText().toString().length() > 0 && lastAppliedDateEdt.isEnabled()) {
//                                        CommonConstants.Prev_Fertilizer_CMD = "Done";
//                                        mFertilizerModelp.setFertilizerid(59);
//                                        mFertilizerModelp.setDosage(Double.parseDouble(lastAppliedDateEdt.getText().toString()));
//                                        mFertilizerModelp.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, psourceOfertilizerSpin.getSelectedItem().toString())));
//                                        mFertilizerModelp.setSourceName(psourceName.getText().toString());
//                                        mFertilizerModelp.setComments(pcomments.getText().toString());
//                                        mFertilizerModelp.setUomid(1);
//                                        mFertilizerModelp.setApplicationType(getKey(AppTypeDataMap, papptype.getSelectedItem().toString()));
//                                        mFertilizerModelp.setApplicationMonth(pMonth);
//                                        mFertilizerModelp.setApplicationYear(pYear);
//                                        //mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
//                                        mFertilizerModelp.setIsFertilizerApplied(1);
//                                        mFertilizerModelp.setQuarter(pQuater);
//
//                                        mFertilizerModelp.setLastapplieddate(papplydate);
//                                        mFertilizerModelp.setApplyfertilizerfrequencytypeid(null);
//                                        mFertilizerModelArray.add(mFertilizerModelp);
//                                    }
                                    break;
                                case 1:
                                    if (dosageGivenEdt1.getText().toString().length() > 0 && dosageGivenEdt1.getText().toString() != "0") {
                                       // CommonConstants.Prev_Fertilizer_CMD = "Done";
                                        mFertilizerModel.setFertilizerid(61);
                                        mFertilizerModel.setDosage(Double.parseDouble(dosageGivenEdt1.getText().toString()));
                                        lastAppliedDate = applydate;
                                        mFertilizerModel.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, sourceOfertilizerSpin.getSelectedItem().toString())));
                                        mFertilizerModel.setSourceName(sourceName.getText().toString());
                                        mFertilizerModel.setComments(comments.getText().toString());
                                        mFertilizerModel.setUomid(1);
                                        mFertilizerModel.setApplicationType(getKey(AppTypeDataMap, apptype.getSelectedItem().toString()));
                                        mFertilizerModel.setApplicationMonth(Month);
                                        mFertilizerModel.setApplicationYear(Year);
                                        //mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
                                        mFertilizerModel.setIsFertilizerApplied(1);
                                        mFertilizerModel.setQuarter(Quater);

                                        mFertilizerModel.setLastapplieddate(CommonConstants.fertilizerapplydate);
                                        mFertilizerModel.setApplyfertilizerfrequencytypeid(null);
                                        mFertilizerModelArray.add(mFertilizerModel);
                                    }
//                                    if (lastAppliedDateEdt1.getText().toString().length() > 0 && lastAppliedDateEdt1.isEnabled()) {
//                                        CommonConstants.Prev_Fertilizer_CMD = "Done";
//                                        mFertilizerModelp.setFertilizerid(61);
//                                        mFertilizerModelp.setDosage(Double.parseDouble(lastAppliedDateEdt1.getText().toString()));
//                                        mFertilizerModelp.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, psourceOfertilizerSpin.getSelectedItem().toString())));
//                                        mFertilizerModelp.setSourceName(psourceName.getText().toString());
//                                        mFertilizerModelp.setComments(pcomments.getText().toString());
//                                        mFertilizerModelp.setUomid(1);
//                                        mFertilizerModelp.setApplicationType(getKey(AppTypeDataMap, papptype.getSelectedItem().toString()));
//                                        mFertilizerModelp.setApplicationMonth(pMonth);
//                                        mFertilizerModelp.setApplicationYear(pYear);
//                                        //mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
//                                        mFertilizerModelp.setIsFertilizerApplied(1);
//                                        mFertilizerModelp.setQuarter(pQuater);
//
//                                        mFertilizerModelp.setLastapplieddate(papplydate);
//                                        mFertilizerModelp.setApplyfertilizerfrequencytypeid(null);
//                                        mFertilizerModelArray.add(mFertilizerModelp);
//                                    }
                                    break;

                                case 2:
                                    if (dosageGivenEdt2.getText().toString().length() > 0 && dosageGivenEdt2.getText().toString() != "0") {
                                        //CommonConstants.Prev_Fertilizer_CMD = "Done";
                                        mFertilizerModel.setFertilizerid(60);
                                        mFertilizerModel.setDosage(Double.parseDouble(dosageGivenEdt2.getText().toString()));
                                        lastAppliedDate = applydate;
                                        mFertilizerModel.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, sourceOfertilizerSpin.getSelectedItem().toString())));
                                        mFertilizerModel.setSourceName(sourceName.getText().toString());
                                        mFertilizerModel.setComments(comments.getText().toString());
                                        mFertilizerModel.setUomid(1);
                                        mFertilizerModel.setApplicationType(getKey(AppTypeDataMap, apptype.getSelectedItem().toString()));
                                        mFertilizerModel.setApplicationMonth(Month);
                                        mFertilizerModel.setApplicationYear(Year);
                                        //mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
                                        mFertilizerModel.setIsFertilizerApplied(1);
                                        mFertilizerModel.setQuarter(Quater);

                                        mFertilizerModel.setLastapplieddate(CommonConstants.fertilizerapplydate);
                                        mFertilizerModel.setApplyfertilizerfrequencytypeid(null);
                                        mFertilizerModelArray.add(mFertilizerModel);
                                    }
//                                    if (lastAppliedDateEdt2.getText().toString().length() > 0 && lastAppliedDateEdt2.isEnabled()) {
//                                        CommonConstants.Prev_Fertilizer_CMD = "Done";
//                                        mFertilizerModelp.setFertilizerid(60);
//                                        mFertilizerModelp.setDosage(Double.parseDouble(lastAppliedDateEdt2.getText().toString()));
//                                        mFertilizerModelp.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, psourceOfertilizerSpin.getSelectedItem().toString())));
//                                        mFertilizerModelp.setSourceName(psourceName.getText().toString());
//                                        mFertilizerModelp.setComments(pcomments.getText().toString());
//                                        mFertilizerModelp.setUomid(1);
//                                        mFertilizerModelp.setApplicationType(getKey(AppTypeDataMap, papptype.getSelectedItem().toString()));
//                                        mFertilizerModelp.setApplicationMonth(pMonth);
//                                        mFertilizerModelp.setApplicationYear(pYear);
//                                        //mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
//                                        mFertilizerModelp.setIsFertilizerApplied(1);
//                                        mFertilizerModelp.setQuarter(pQuater);
//
//                                        mFertilizerModelp.setLastapplieddate(papplydate);
//                                        mFertilizerModelp.setApplyfertilizerfrequencytypeid(null);
//                                        mFertilizerModelArray.add(mFertilizerModelp);
//                                    }
                                    break;

                                case 3:
                                    if (dosageGivenEdt3.getText().toString().length() > 0 && dosageGivenEdt3.getText().toString() != "0") {
                                        //CommonConstants.Prev_Fertilizer_CMD = "Done";
                                        mFertilizerModel.setFertilizerid(63);
                                        mFertilizerModel.setDosage(Double.parseDouble(dosageGivenEdt3.getText().toString()));
                                        lastAppliedDate = applydate;
                                        mFertilizerModel.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, sourceOfertilizerSpin.getSelectedItem().toString())));
                                        mFertilizerModel.setSourceName(sourceName.getText().toString());
                                        mFertilizerModel.setComments(comments.getText().toString());
                                        mFertilizerModel.setUomid(1);
                                        mFertilizerModel.setApplicationType(getKey(AppTypeDataMap, apptype.getSelectedItem().toString()));
                                        mFertilizerModel.setApplicationMonth(Month);
                                        mFertilizerModel.setApplicationYear(Year);
                                        //mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
                                        mFertilizerModel.setIsFertilizerApplied(1);
                                        mFertilizerModel.setQuarter(Quater);

                                        mFertilizerModel.setLastapplieddate(CommonConstants.fertilizerapplydate);
                                        mFertilizerModel.setApplyfertilizerfrequencytypeid(null);
                                        mFertilizerModelArray.add(mFertilizerModel);
                                    }
//                                    if (lastAppliedDateEdt3.getText().toString().length() > 0 && lastAppliedDateEdt3.isEnabled()) {
//                                        CommonConstants.Prev_Fertilizer_CMD = "Done";
//                                        mFertilizerModelp.setFertilizerid(63);
//                                        mFertilizerModelp.setDosage(Double.parseDouble(lastAppliedDateEdt3.getText().toString()));
//                                        mFertilizerModelp.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, psourceOfertilizerSpin.getSelectedItem().toString())));
//                                        mFertilizerModelp.setSourceName(psourceName.getText().toString());
//                                        mFertilizerModelp.setComments(pcomments.getText().toString());
//                                        mFertilizerModelp.setUomid(1);
//                                        mFertilizerModelp.setApplicationType(getKey(AppTypeDataMap, papptype.getSelectedItem().toString()));
//                                        mFertilizerModelp.setApplicationMonth(pMonth);
//                                        mFertilizerModelp.setApplicationYear(pYear);
//                                        //mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
//                                        mFertilizerModelp.setIsFertilizerApplied(1);
//                                        mFertilizerModelp.setQuarter(pQuater);
//
//                                        mFertilizerModelp.setLastapplieddate(papplydate);
//                                        mFertilizerModelp.setApplyfertilizerfrequencytypeid(null);
//                                        mFertilizerModelArray.add(mFertilizerModelp);
//                                    }
                                    break;

                                case 4:
                                    if (dosageGivenEdt4.getText().toString().length() > 0 && dosageGivenEdt4.getText().toString() != "0") {
                                       // CommonConstants.Prev_Fertilizer_CMD = "Done";
                                        mFertilizerModel.setFertilizerid(64);
                                        mFertilizerModel.setDosage(Double.parseDouble(dosageGivenEdt4.getText().toString()));
                                        lastAppliedDate = applydate;
                                        mFertilizerModel.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, sourceOfertilizerSpin.getSelectedItem().toString())));
                                        mFertilizerModel.setSourceName(sourceName.getText().toString());
                                        mFertilizerModel.setComments(comments.getText().toString());
                                        mFertilizerModel.setUomid(1);
                                        mFertilizerModel.setApplicationType(getKey(AppTypeDataMap, apptype.getSelectedItem().toString()));
                                        mFertilizerModel.setApplicationMonth(Month);
                                        mFertilizerModel.setApplicationYear(Year);
                                        // mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
                                        mFertilizerModel.setIsFertilizerApplied(1);
                                        mFertilizerModel.setQuarter(Quater);

                                        mFertilizerModel.setLastapplieddate(CommonConstants.fertilizerapplydate);
                                        mFertilizerModel.setApplyfertilizerfrequencytypeid(null);
                                        mFertilizerModelArray.add(mFertilizerModel);
                                    }
//                                    if (lastAppliedDateEdt4.getText().toString().length() > 0 && lastAppliedDateEdt4.isEnabled()) {
//                                        CommonConstants.Prev_Fertilizer_CMD = "Done";
//                                        mFertilizerModelp.setFertilizerid(64);
//                                        mFertilizerModelp.setDosage(Double.parseDouble(lastAppliedDateEdt4.getText().toString()));
//                                        mFertilizerModelp.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, psourceOfertilizerSpin.getSelectedItem().toString())));
//                                        mFertilizerModelp.setSourceName(psourceName.getText().toString());
//                                        mFertilizerModelp.setComments(pcomments.getText().toString());
//                                        mFertilizerModelp.setUomid(1);
//                                        mFertilizerModelp.setApplicationType(getKey(AppTypeDataMap, papptype.getSelectedItem().toString()));
//                                        mFertilizerModelp.setApplicationMonth(pMonth);
//                                        mFertilizerModelp.setApplicationYear(pYear);
//                                        //mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
//                                        mFertilizerModelp.setIsFertilizerApplied(1);
//                                        mFertilizerModelp.setQuarter(pQuater);
//
//                                        mFertilizerModelp.setLastapplieddate(papplydate);
//                                        mFertilizerModelp.setApplyfertilizerfrequencytypeid(null);
//                                        mFertilizerModelArray.add(mFertilizerModelp);
//                                    }
                                    break;

                                case 5:
                                    if (dosageGivenEdt5.getText().toString().length() > 0 && dosageGivenEdt5.getText().toString() != "0") {
                                        //CommonConstants.Prev_Fertilizer_CMD = "Done";
                                        mFertilizerModel.setFertilizerid(58);
                                        mFertilizerModel.setDosage(Double.parseDouble(dosageGivenEdt5.getText().toString()));
                                        lastAppliedDate = applydate;
                                        mFertilizerModel.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, sourceOfertilizerSpin.getSelectedItem().toString())));
                                        mFertilizerModel.setSourceName(sourceName.getText().toString());
                                        mFertilizerModel.setComments(comments.getText().toString());
                                        mFertilizerModel.setUomid(1);
                                        mFertilizerModel.setApplicationType(getKey(AppTypeDataMap, apptype.getSelectedItem().toString()));
                                        mFertilizerModel.setApplicationMonth(Month);
                                        mFertilizerModel.setApplicationYear(Year);
                                        // mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
                                        mFertilizerModel.setIsFertilizerApplied(1);
                                        mFertilizerModel.setQuarter(Quater);

                                        mFertilizerModel.setLastapplieddate(CommonConstants.fertilizerapplydate);
                                        mFertilizerModel.setApplyfertilizerfrequencytypeid(null);
                                        mFertilizerModelArray.add(mFertilizerModel);
                                    }
//                                    if (lastAppliedDateEdt5.getText().toString().length() > 0 && lastAppliedDateEdt5.isEnabled()) {
//                                        CommonConstants.Prev_Fertilizer_CMD = "Done";
//                                        mFertilizerModelp.setFertilizerid(58);
//                                        mFertilizerModelp.setDosage(Double.parseDouble(lastAppliedDateEdt5.getText().toString()));
//                                        mFertilizerModelp.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, psourceOfertilizerSpin.getSelectedItem().toString())));
//                                        mFertilizerModelp.setSourceName(psourceName.getText().toString());
//                                        mFertilizerModelp.setComments(pcomments.getText().toString());
//                                        mFertilizerModelp.setUomid(1);
//                                        mFertilizerModelp.setApplicationType(getKey(AppTypeDataMap, papptype.getSelectedItem().toString()));
//                                        mFertilizerModelp.setApplicationMonth(pMonth);
//                                        mFertilizerModelp.setApplicationYear(pYear);
//                                        //mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
//                                        mFertilizerModelp.setIsFertilizerApplied(1);
//                                        mFertilizerModelp.setQuarter(pQuater);
//
//                                        mFertilizerModelp.setLastapplieddate(papplydate);
//                                        mFertilizerModelp.setApplyfertilizerfrequencytypeid(null);
//                                        mFertilizerModelArray.add(mFertilizerModelp);
//                                    }
                                    break;

                                case 6:
                                    if (dosageGivenEdt6.getText().toString().length() > 0 && dosageGivenEdt6.getText().toString() != "0") {
                                        //CommonConstants.Prev_Fertilizer_CMD = "Done";
                                        mFertilizerModel.setFertilizerid(229);
                                        mFertilizerModel.setDosage(Double.parseDouble(dosageGivenEdt6.getText().toString()));
                                        lastAppliedDate = applydate;
                                        mFertilizerModel.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, sourceOfertilizerSpin.getSelectedItem().toString())));
                                        mFertilizerModel.setSourceName(sourceName.getText().toString());
                                        mFertilizerModel.setComments(comments.getText().toString());
                                        mFertilizerModel.setUomid(1);
                                        mFertilizerModel.setApplicationType(getKey(AppTypeDataMap, apptype.getSelectedItem().toString()));
                                        mFertilizerModel.setApplicationMonth(Month);
                                        mFertilizerModel.setApplicationYear(Year);
                                        //  mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
                                        mFertilizerModel.setIsFertilizerApplied(1);
                                        mFertilizerModel.setQuarter(Quater);

                                        mFertilizerModel.setLastapplieddate(CommonConstants.fertilizerapplydate);
                                        mFertilizerModel.setApplyfertilizerfrequencytypeid(null);
                                        mFertilizerModelArray.add(mFertilizerModel);
                                    }
//                                    if (lastAppliedDateEdt6.getText().toString().length() > 0 && lastAppliedDateEdt6.isEnabled()) {
//                                        CommonConstants.Prev_Fertilizer_CMD = "Done";
//                                        mFertilizerModelp.setFertilizerid(229);
//                                        mFertilizerModelp.setDosage(Double.parseDouble(lastAppliedDateEdt6.getText().toString()));
//                                        mFertilizerModelp.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, psourceOfertilizerSpin.getSelectedItem().toString())));
//                                        mFertilizerModelp.setSourceName(psourceName.getText().toString());
//                                        mFertilizerModelp.setComments(pcomments.getText().toString());
//                                        mFertilizerModelp.setUomid(1);
//                                        mFertilizerModelp.setApplicationType(getKey(AppTypeDataMap, papptype.getSelectedItem().toString()));
//                                        mFertilizerModelp.setApplicationMonth(pMonth);
//                                        mFertilizerModelp.setApplicationYear(pYear);
//                                        //mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
//                                        mFertilizerModelp.setIsFertilizerApplied(1);
//                                        mFertilizerModelp.setQuarter(pQuater);
//
//                                        mFertilizerModelp.setLastapplieddate(papplydate);
//                                        mFertilizerModelp.setApplyfertilizerfrequencytypeid(null);
//                                        mFertilizerModelArray.add(mFertilizerModelp);
//                                    }
                                    break;
                                case 7:


                                    if (bioFertilizerSpin.getSelectedItemPosition() != 0 && dosageGivenEdt7.getText().toString().length() > 0 && dosageGivenEdt7.getText().toString() != "0") {
                                        //CommonConstants.Prev_Fertilizer_CMD = "Done";
                                        mFertilizerModel.setFertilizerid(232);
                                        mFertilizerModel.setBioFertilizerId(bioFertilizerId);
                                        mFertilizerModel.setDosage(Double.parseDouble(dosageGivenEdt7.getText().toString()));
                                        lastAppliedDate = applydate;
                                        mFertilizerModel.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, sourceOfertilizerSpin.getSelectedItem().toString())));
                                        mFertilizerModel.setSourceName(sourceName.getText().toString());
                                        mFertilizerModel.setComments(comments.getText().toString());
                                        mFertilizerModel.setUomid(1);
                                        mFertilizerModel.setApplicationType(getKey(AppTypeDataMap, apptype.getSelectedItem().toString()));
                                        mFertilizerModel.setApplicationMonth(Month);
                                        mFertilizerModel.setApplicationYear(Year);
                                        //  mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
                                        mFertilizerModel.setIsFertilizerApplied(1);
                                        mFertilizerModel.setQuarter(Quater);

                                        mFertilizerModel.setLastapplieddate(CommonConstants.fertilizerapplydate);
                                        mFertilizerModel.setApplyfertilizerfrequencytypeid(null);
                                        mFertilizerModelArray.add(mFertilizerModel);

                                    }

//                                    if (lastAppliedDateEdt7.getText().toString().length() > 0 && lastAppliedDateEdt7.isEnabled() && bioFertilizerSpin.getSelectedItemPosition() != 0) {
//                                        CommonConstants.Prev_Fertilizer_CMD = "Done";
//                                        mFertilizerModelp.setFertilizerid(232);
//                                        mFertilizerModelp.setBioFertilizerId(bioFertilizerId);
//                                        mFertilizerModelp.setDosage(Double.parseDouble(lastAppliedDateEdt7.getText().toString()));
//                                        mFertilizerModelp.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, psourceOfertilizerSpin.getSelectedItem().toString())));
//                                        mFertilizerModelp.setSourceName(psourceName.getText().toString());
//                                        mFertilizerModelp.setComments(pcomments.getText().toString());
//                                        mFertilizerModelp.setUomid(1);
//                                        mFertilizerModelp.setApplicationType(getKey(AppTypeDataMap, papptype.getSelectedItem().toString()));
//                                        mFertilizerModelp.setApplicationMonth(pMonth);
//                                        mFertilizerModelp.setApplicationYear(pYear);
//                                        //mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
//                                        mFertilizerModelp.setIsFertilizerApplied(1);
//                                        mFertilizerModelp.setQuarter(pQuater);
//
//                                        mFertilizerModelp.setLastapplieddate(papplydate);
//                                        mFertilizerModelp.setApplyfertilizerfrequencytypeid(null);
//                                        mFertilizerModelArray.add(mFertilizerModelp);
//                                    }
                                    break;
                                case 8:
                                    if (bioFertilizerSpin2.getSelectedItemPosition() != 0 && dosageGivenEdt8.getText().toString().length() > 0 && dosageGivenEdt8.getText().toString() != "0") {
                                        //CommonConstants.Prev_Fertilizer_CMD = "Done";
                                        Log.d("Selected Item Id2", bioFertilizerId2+ "");

                                        mFertilizerModel.setFertilizerid(232);
                                        mFertilizerModel.setBioFertilizerId(bioFertilizerId2);
                                        mFertilizerModel.setDosage(Double.parseDouble(dosageGivenEdt8.getText().toString()));
                                        lastAppliedDate = applydate;
                                        mFertilizerModel.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, sourceOfertilizerSpin.getSelectedItem().toString())));
                                        mFertilizerModel.setSourceName(sourceName.getText().toString());
                                        mFertilizerModel.setComments(comments.getText().toString());
                                        mFertilizerModel.setUomid(1);
                                        mFertilizerModel.setApplicationType(getKey(AppTypeDataMap, apptype.getSelectedItem().toString()));
                                        mFertilizerModel.setApplicationMonth(Month);
                                        mFertilizerModel.setApplicationYear(Year);
                                        //  mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
                                        mFertilizerModel.setIsFertilizerApplied(1);
                                        mFertilizerModel.setQuarter(Quater);
                                        mFertilizerModel.setLastapplieddate(CommonConstants.fertilizerapplydate);
                                        mFertilizerModel.setApplyfertilizerfrequencytypeid(null);
                                        mFertilizerModelArray.add(mFertilizerModel);
                                    }
                                    break;
                                case 9:
                                    if (bioFertilizerSpin3.getSelectedItemPosition() != 0 && dosageGivenEdt31.getText().toString().length() > 0 && dosageGivenEdt31.getText().toString() != "0") {
                                        //CommonConstants.Prev_Fertilizer_CMD = "Done";
                                        Log.d("Selected Item Id 3", bioFertilizerId3 + "");

                                        mFertilizerModel.setFertilizerid(232);
                                        mFertilizerModel.setBioFertilizerId(bioFertilizerId3);
                                        mFertilizerModel.setDosage(Double.parseDouble(dosageGivenEdt31.getText().toString()));
                                        lastAppliedDate = applydate;
                                        mFertilizerModel.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, sourceOfertilizerSpin.getSelectedItem().toString())));
                                        mFertilizerModel.setSourceName(sourceName.getText().toString());
                                        mFertilizerModel.setComments(comments.getText().toString());
                                        mFertilizerModel.setUomid(1);
                                        mFertilizerModel.setApplicationType(getKey(AppTypeDataMap, apptype.getSelectedItem().toString()));
                                        mFertilizerModel.setApplicationMonth(Month);
                                        mFertilizerModel.setApplicationYear(Year);
                                        //  mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
                                        mFertilizerModel.setIsFertilizerApplied(1);
                                        mFertilizerModel.setQuarter(Quater);
                                        mFertilizerModel.setLastapplieddate(CommonConstants.fertilizerapplydate);
                                        mFertilizerModel.setApplyfertilizerfrequencytypeid(null);
                                        mFertilizerModelArray.add(mFertilizerModel);
                                    }
                                    break;
                                case 10:
                                    if (bioFertilizerSpin4.getSelectedItemPosition() != 0 && dosageGivenEdt41.getText().toString().length() > 0 && dosageGivenEdt41.getText().toString() != "0") {
                                        //CommonConstants.Prev_Fertilizer_CMD = "Done";
                                        Log.d("Selected Item Id 4", bioFertilizerId4 + "");
                                        mFertilizerModel.setFertilizerid(232);
                                        mFertilizerModel.setBioFertilizerId(bioFertilizerId4);
                                        mFertilizerModel.setDosage(Double.parseDouble(dosageGivenEdt41.getText().toString()));
                                        lastAppliedDate = applydate;
                                        mFertilizerModel.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, sourceOfertilizerSpin.getSelectedItem().toString())));
                                        mFertilizerModel.setSourceName(sourceName.getText().toString());
                                        mFertilizerModel.setComments(comments.getText().toString());
                                        mFertilizerModel.setUomid(1);
                                        mFertilizerModel.setApplicationType(getKey(AppTypeDataMap, apptype.getSelectedItem().toString()));
                                        mFertilizerModel.setApplicationMonth(Month);
                                        mFertilizerModel.setApplicationYear(Year);
                                        //  mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
                                        mFertilizerModel.setIsFertilizerApplied(1);
                                        mFertilizerModel.setQuarter(Quater);
                                        mFertilizerModel.setLastapplieddate(CommonConstants.fertilizerapplydate);
                                        mFertilizerModel.setApplyfertilizerfrequencytypeid(null);
                                        mFertilizerModelArray.add(mFertilizerModel);
                                    }
                                    break;
                                case 11:
                                    if (bioFertilizerSpin5.getSelectedItemPosition() != 0 && dosageGivenEdt51.getText().toString().length() > 0 && dosageGivenEdt51.getText().toString() != "0") {
                                        //CommonConstants.Prev_Fertilizer_CMD = "Done";
                                        Log.d("Selected Item Id 5", bioFertilizerId5+ "");
                                        mFertilizerModel.setFertilizerid(232);
                                        mFertilizerModel.setBioFertilizerId(bioFertilizerId5);
                                        mFertilizerModel.setDosage(Double.parseDouble(dosageGivenEdt51.getText().toString()));
                                        lastAppliedDate = applydate;
                                        mFertilizerModel.setFertilizersourcetypeid(Integer.parseInt(getKey(fertilizerDataMap, sourceOfertilizerSpin.getSelectedItem().toString())));
                                        mFertilizerModel.setSourceName(sourceName.getText().toString());
                                        mFertilizerModel.setComments(comments.getText().toString());
                                        mFertilizerModel.setUomid(1);
                                        mFertilizerModel.setApplicationType(getKey(AppTypeDataMap, apptype.getSelectedItem().toString()));
                                        mFertilizerModel.setApplicationMonth(Month);
                                        mFertilizerModel.setApplicationYear(Year);
                                        //  mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
                                        mFertilizerModel.setIsFertilizerApplied(1);
                                        mFertilizerModel.setQuarter(Quater);
                                        mFertilizerModel.setLastapplieddate(CommonConstants.fertilizerapplydate);
                                        mFertilizerModel.setApplyfertilizerfrequencytypeid(null);
                                        mFertilizerModelArray.add(mFertilizerModel);
                                    }
                                    break;
                            }


                        }
                        Fertilizer mFertilizerModel = new Fertilizer();
                        Fertilizer mFertilizerModelp = new Fertilizer();
                        if (Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())) == 0) {
                            //CommonConstants.Prev_Fertilizer_CMD = "Done";
                            mFertilizerModel.setFertilizerid(234);
                            mFertilizerModel.setDosage(0.00);
                            lastAppliedDate = applydate;
                            mFertilizerModel.setFertilizersourcetypeid(155);
                            mFertilizerModel.setSourceName("");
                            mFertilizerModel.setComments("");
                            mFertilizerModel.setUomid(1);

                            mFertilizerModel.setApplicationMonth("");
                            mFertilizerModel.setApplicationYear(cy);
                            //  mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
                            mFertilizerModel.setIsFertilizerApplied(0);
                            mFertilizerModel.setQuarter(cq);

                            mFertilizerModel.setLastapplieddate(cmindate+" 00:00:00");
                            mFertilizerModel.setApplyfertilizerfrequencytypeid(null);
                            mFertilizerModelArray.clear();
                            mFertilizerModelArray.add(mFertilizerModel);
                        }
//                        if (Integer.parseInt(getKey(IsAppliedDataMap, pfertilizerapplied.getSelectedItem().toString())) == 0) {
//                            CommonConstants.Prev_Fertilizer_CMD = "Done";
//                            mFertilizerModelp.setFertilizerid(234);
//                            mFertilizerModelp.setDosage(0.00);
//                            lastAppliedDate = applydate;
//                            mFertilizerModelp.setFertilizersourcetypeid(155);
//                            mFertilizerModelp.setSourceName("");
//                            mFertilizerModelp.setComments("");
//                            mFertilizerModelp.setUomid(1);
//
//                            mFertilizerModelp.setApplicationMonth("");
//                            mFertilizerModelp.setApplicationYear(py);
//                            //  mFertilizerModel.setIsFertilizerApplied(Integer.parseInt(getKey(IsAppliedDataMap, fertilizerapplied.getSelectedItem().toString())));
//                            mFertilizerModelp.setIsFertilizerApplied(0);
//                            mFertilizerModelp.setQuarter(pq);
//
//                            mFertilizerModelp.setLastapplieddate(pmindate+" 00:00:00");
//                            mFertilizerModelp.setApplyfertilizerfrequencytypeid(null);
//                            mFertilizerModelArray.add(mFertilizerModelp);
//                        }

                        DataManager.getInstance().addData(DataManager.FERTILIZER, mFertilizerModelArray);
                        mFertilizerModelArray = (ArrayList<Fertilizer>) DataManager.getInstance().getDataFromManager(DataManager.FERTILIZER);
                        Log.d("Monthyearsecondsave", mFertilizerModelArray.get(0).getApplicationMonth() + "");


                        getFragmentManager().popBackStack();
                        CommonUtilsNavigation.hideKeyBoard(getActivity());
                        clearFields();
                        //     fertilizerDataAdapter.notifyDataSetChanged();

                        updateUiListener.updateUserInterface(0);

                    }

                CommonUtilsNavigation.hideKeyBoard(getActivity());
                break;



//            case R.id.lastAppliedDateEdt:
//                cal=0;
//                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH));  //date is dateSetListener as per your code in question
//                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
//                datePickerDialog.show();
//                break;
//
//            case R.id.lastAppliedDateEdt1:
//                cal=1;
//                DatePickerDialog datePickerDialog1 = new DatePickerDialog(getActivity(), date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH));  //date is dateSetListener as per your code in question
//                datePickerDialog1.getDatePicker().setMaxDate(System.currentTimeMillis());
//                datePickerDialog1.show();
//                break;
//            case R.id.lastAppliedDateEdt2:
//                cal=2;
//                DatePickerDialog datePickerDialog2 = new DatePickerDialog(getActivity(), date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH));  //date is dateSetListener as per your code in question
//                datePickerDialog2.getDatePicker().setMaxDate(System.currentTimeMillis());
//                datePickerDialog2.show();
//                break;
//            case R.id.lastAppliedDateEdt3:
//                cal=3;
//                DatePickerDialog datePickerDialog3 = new DatePickerDialog(getActivity(), date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH));  //date is dateSetListener as per your code in question
//                datePickerDialog3.getDatePicker().setMaxDate(System.currentTimeMillis());
//                datePickerDialog3.show();
//                break;
//            case R.id.lastAppliedDateEdt4:
//                cal=4;
//                DatePickerDialog datePickerDialog4 = new DatePickerDialog(getActivity(), date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH));  //date is dateSetListener as per your code in question
//                datePickerDialog4.getDatePicker().setMaxDate(System.currentTimeMillis());
//                datePickerDialog4.show();
//                break;
//            case R.id.lastAppliedDateEdt5:
//                cal=5;
//                DatePickerDialog datePickerDialog5 = new DatePickerDialog(getActivity(), date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH));  //date is dateSetListener as per your code in question
//                datePickerDialog5.getDatePicker().setMaxDate(System.currentTimeMillis());
//                datePickerDialog5.show();
//                break;
//            case R.id.lastAppliedDateEdt6:
//                cal=6;
//                DatePickerDialog datePickerDialog6 = new DatePickerDialog(getActivity(), date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH));  //date is dateSetListener as per your code in question
//                datePickerDialog6.getDatePicker().setMaxDate(System.currentTimeMillis());
//                datePickerDialog6.show();
//                break;
//            case R.id.lastAppliedDateEdt7:
//                cal=7;
//                DatePickerDialog datePickerDialog7 = new DatePickerDialog(getActivity(), date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH));  //date is dateSetListener as per your code in question
//                datePickerDialog7.getDatePicker().setMaxDate(System.currentTimeMillis());
//                datePickerDialog7.show();
//                break;

            case R.id.historyBtn:


//                Log.d("HistoryBtn", "Clciked");
//                cal=7;
//                CropMaintainanceHistoryFragment newFragment = new CropMaintainanceHistoryFragment();
//                Bundle bundle = new Bundle();
//                bundle.putInt("screen", 1);
//                newFragment.setArguments(bundle);
//                newFragment.show(getActivity().getFragmentManager(), "history");
                showDialog(getContext());

                break;

            case R.id.pdfBtn:
                showPDFDialog(getContext());
                break;

        }

    }

    public void showDialog(Context activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.fertilizerlastvisteddata);

        Toolbar titleToolbar;
        titleToolbar = (Toolbar) dialog.findViewById(R.id.titleToolbar);
        titleToolbar.setTitle("Fertilizer History");
        titleToolbar.setTitleTextColor(getResources().getColor(R.color.white));

        FertilizerNonBioVisitedDataAdapter fertilizernonbioVisitedDataAdapter;
        FertilizerBioVisitedDataAdapter fertilizerbioVisitedDataAdapter;


        LinearLayout fertilizermainlyt = (LinearLayout) dialog.findViewById(R.id.fertilizermainlyt);
        LinearLayout biolayout = (LinearLayout) dialog.findViewById(R.id.biolayout);

//        LinearLayout fertilizerappliedll = (LinearLayout) dialog.findViewById(R.id.fertilizerappliedll);
//        LinearLayout fertilizermonthyearll = (LinearLayout) dialog.findViewById(R.id.fertilizermonthyearll);
//        LinearLayout nameoftheshopll = (LinearLayout) dialog.findViewById(R.id.nameoftheshopll);
//        LinearLayout sourceoffertilizerll = (LinearLayout) dialog.findViewById(R.id.sourceoffertilizerll);
//        LinearLayout fertcommentsll = (LinearLayout) dialog.findViewById(R.id.fertcommentsll);
//        LinearLayout fertapptypell = (LinearLayout) dialog.findViewById(R.id.fertapptypell);


        RecyclerView nonbiofertrcv = (RecyclerView) dialog.findViewById(R.id.nonbiofertrcv);
        RecyclerView biofertrcv = (RecyclerView) dialog.findViewById(R.id.biofertrcv);

//        TextView fertilizerapplied = (TextView) dialog.findViewById(R.id.fertilizerapplied);
//        TextView fertilizermonthyear = (TextView) dialog.findViewById(R.id.fertilizermonthyear);
//        TextView nameoftheshop = (TextView) dialog.findViewById(R.id.nameoftheshop);
//        TextView sourceoffertilizer = (TextView) dialog.findViewById(R.id.sourceoffertilizer);
//        TextView fertcomments = (TextView) dialog.findViewById(R.id.fertcomments);
//        TextView fertapptype = (TextView) dialog.findViewById(R.id.fertapptype);

        TextView norecords = (TextView) dialog.findViewById(R.id.fertilizernorecord_tv);


        String lastVisitCode = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getLatestCropMaintanaceHistoryCode(CommonConstants.PLOT_CODE));

        Log.e("======>lastVisitCode",lastVisitCode+"");

        fertilizernonbiolastvisitdatamap = (ArrayList<Fertilizer>) dataAccessHandler.getFertilizerData(Queries.getInstance().getFertilizerCropMaintenanceHistoryData(lastVisitCode), 1);
        fertilizerbiolastvisitdatamap = (ArrayList<Fertilizer>) dataAccessHandler.getFertilizerData(Queries.getInstance().getBioFertilizerCropMaintenanceHistoryData(lastVisitCode), 1);

        if (fertilizernonbiolastvisitdatamap.size() > 0 || fertilizerbiolastvisitdatamap.size() > 0){
            norecords.setVisibility(View.GONE);
            fertilizermainlyt.setVisibility(View.VISIBLE);

            if (fertilizernonbiolastvisitdatamap.size() > 0){
                nonbiofertrcv.setVisibility(View.VISIBLE);
                fertilizernonbioVisitedDataAdapter = new FertilizerNonBioVisitedDataAdapter(getActivity(), fertilizernonbiolastvisitdatamap,dataAccessHandler);
                nonbiofertrcv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                nonbiofertrcv.setAdapter(fertilizernonbioVisitedDataAdapter);
            }else{
                nonbiofertrcv.setVisibility(View.GONE);
            }

            if (fertilizerbiolastvisitdatamap.size() > 0){
                biolayout.setVisibility(View.VISIBLE);
                fertilizerbioVisitedDataAdapter = new FertilizerBioVisitedDataAdapter(getActivity(), fertilizerbiolastvisitdatamap,dataAccessHandler);
                biofertrcv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                biofertrcv.setAdapter(fertilizerbioVisitedDataAdapter);
            }else{
                biolayout.setVisibility(View.GONE);
            }



        }else{
            fertilizermainlyt.setVisibility(View.GONE);
            norecords.setVisibility(View.VISIBLE);
        }

        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 500);
    }

    public void showPDFDialog(Context activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.pdfdialog);

        Toolbar titleToolbar;
        titleToolbar = (Toolbar) dialog.findViewById(R.id.titleToolbar);
        titleToolbar.setTitle("Fertilizer PDF");
        titleToolbar.setTitleTextColor(getResources().getColor(R.color.white));

        PDFView fertpfdview;

        fertpfdview = dialog.findViewById(R.id.fertpdfview);

        fertpfdview.fromFile(fileToDownLoad)
                .defaultPage(0)
                .enableAnnotationRendering(true)
                .onPageChange(this)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(getActivity()))
                .load();

        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 500);
    }


    private void clearFields() {

        fertilizerapplied.setSelection(0);
        sourceOfertilizerSpin.setSelection(0);
        apptype.setSelection(0);
        otherEdt.setText("");
        dosageGivenEdt.setText("");
        // lastAppliedDateEdt.setText("");
        dosageGivenEdt1.setText("");
        //  lastAppliedDateEdt1.setText("");
        dosageGivenEdt2.setText("");
        //    lastAppliedDateEdt2.setText("");
        dosageGivenEdt3.setText("");
        //  lastAppliedDateEdt3.setText("");
        dosageGivenEdt4.setText("");
        // lastAppliedDateEdt4.setText("");
        dosageGivenEdt5.setText("");
        //lastAppliedDateEdt5.setText("");
        dosageGivenEdt6.setText("");
        // lastAppliedDateEdt6.setText("");
        dosageGivenEdt7.setText("");
        //lastAppliedDateEdt7.setText("");
        dosageGivenEdt8.setText("");
        // lastAppliedDateEdt8.setText("");

        otherEdt.setText("");
        comments.setText("");
        // pcomments.setText("");
        sourceName.setText("");
        //  psourceName.setText("");
        Monthyear.setText("");
        //   pMonthyear.setText("");




    }
    private boolean validateUI() {


        if (fertilizerapplied.getSelectedItemPosition() == 0){
            Toast.makeText(mContext, "Please Select Fertilizer Applied?", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (fertilizerapplied.getSelectedItemPosition() == 1){

            if (TextUtils.isEmpty(Monthyear.getText().toString())){
                Toast.makeText(mContext, "Please Select Month/Year", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (TextUtils.isEmpty(sourceName.getText().toString())){
                Toast.makeText(mContext, "Please Enter Shop Name", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (sourceOfertilizerSpin.getSelectedItemPosition() == 0){
                Toast.makeText(mContext, "Please Select Source of Fertilizer", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (apptype.getSelectedItemPosition() == 0){
                Toast.makeText(mContext, "Please Select App Type", Toast.LENGTH_SHORT).show();
                return false;
            }

//            if (TextUtils.isEmpty(dosageGivenEdt.getText().toString()) && TextUtils.isEmpty(dosageGivenEdt1.getText().toString()) && TextUtils.isEmpty(dosageGivenEdt2.getText().toString())
//            && TextUtils.isEmpty(dosageGivenEdt3.getText().toString()) && TextUtils.isEmpty(dosageGivenEdt4.getText().toString()) && TextUtils.isEmpty(dosageGivenEdt5.getText().toString())
//                    && TextUtils.isEmpty(dosageGivenEdt6.getText().toString())  && TextUtils.isEmpty(dosageGivenEdt7.getText().toString())  && TextUtils.isEmpty(dosageGivenEdt8.getText().toString())
//            ){
//                Toast.makeText(mContext, "Please Enter Any One Fertilizer Dosage", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//
//            if (dosageGivenEdt.getText().toString() == "0" && dosageGivenEdt1.getText().toString() == "0" && dosageGivenEdt2.getText().toString() == "0" &&
//                    dosageGivenEdt3.getText().toString() == "0" && dosageGivenEdt4.getText().toString() == "0" &&  dosageGivenEdt5.getText().toString() == "0" &&
//                    dosageGivenEdt6.getText().toString() == "0" && dosageGivenEdt7.getText().toString() == "0" &&  dosageGivenEdt8.getText().toString() == "0"){
//
//                Toast.makeText(mContext, "Please Enter Any One Fertilizer Dosage", Toast.LENGTH_SHORT).show();
//                return false;
//            }


            EditText[] dosageEditTexts = {
                    dosageGivenEdt, dosageGivenEdt1, dosageGivenEdt2, dosageGivenEdt3,
                    dosageGivenEdt4, dosageGivenEdt5, dosageGivenEdt6, dosageGivenEdt7, dosageGivenEdt8
            };

            boolean hasNonEmptyOrNonZeroValue = false;

            for (EditText editText : dosageEditTexts) {
                String value = editText.getText().toString().trim();
                if (!TextUtils.isEmpty(value) && !value.equals("0")) {
                    hasNonEmptyOrNonZeroValue = true;
                    break;
                }
            }

            if (!hasNonEmptyOrNonZeroValue) {
                Toast.makeText(mContext, "Please Enter Any One Fertilizer Dosage", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (bioFertilizerSpin2.getSelectedItemPosition() == 0 && (!TextUtils.isEmpty(dosageGivenEdt8.getText().toString().trim()) && !dosageGivenEdt8.getText().toString().trim().equals("0"))) {
                Toast.makeText(mContext, "Please Select BioFertilizer 2", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (bioFertilizerSpin.getSelectedItemPosition() == 0 && (!TextUtils.isEmpty(dosageGivenEdt7.getText().toString().trim()) && !dosageGivenEdt7.getText().toString().trim().equals("0"))) {
                Toast.makeText(mContext, "Please Select BioFertilizer 1", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (bioFertilizerSpin3.getSelectedItemPosition() == 0 && (!TextUtils.isEmpty(dosageGivenEdt31.getText().toString().trim()) && !dosageGivenEdt31.getText().toString().trim().equals("0"))) {
                Toast.makeText(mContext, "Please Select BioFertilizer 3", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (bioFertilizerSpin4.getSelectedItemPosition() == 0 && (!TextUtils.isEmpty(dosageGivenEdt41.getText().toString().trim()) && !dosageGivenEdt41.getText().toString().trim().equals("0"))) {
                Toast.makeText(mContext, "Please Select BioFertilizer 4", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (bioFertilizerSpin5.getSelectedItemPosition() == 0 && (!TextUtils.isEmpty(dosageGivenEdt51.getText().toString().trim()) && !dosageGivenEdt51.getText().toString().trim().equals("0"))) {
                Toast.makeText(mContext, "Please Select BioFertilizer 5", Toast.LENGTH_SHORT).show();
                return false;
            }

        }else{
            return  true;
        }

        return true;
    }

//    private void updateLabel() {
//        String myFormat = "MM/dd/yyyy"; //In which you need put here
//        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//        if(cal==0)
//            lastAppliedDateEdt.setText(sdf.format(myCalendar.getTime()));
//        else if(cal==1)
//            lastAppliedDateEdt1.setText(sdf.format(myCalendar.getTime()));
//        else if(cal==2)
//            lastAppliedDateEdt2.setText(sdf.format(myCalendar.getTime()));
//        else if(cal==3)
//            lastAppliedDateEdt3.setText(sdf.format(myCalendar.getTime()));
//        else if(cal==4)
//            lastAppliedDateEdt4.setText(sdf.format(myCalendar.getTime()));
//        else if(cal==5)
//            lastAppliedDateEdt5.setText(sdf.format(myCalendar.getTime()));
//        else if(cal==6)
//            lastAppliedDateEdt6.setText(sdf.format(myCalendar.getTime()));
//        else if(cal==7)
//            lastAppliedDateEdt7.setText(sdf.format(myCalendar.getTime()));
//
//    }

    @Override
    public void onEditClicked(int position) {
        Log.v(LOG_TAG, "@@@ selected position " + position);
        mFertilizerModelArray.remove(position);
        fertilizerDataAdapter.notifyDataSetChanged();
    }

    public void setUpdateUiListener(UpdateUiListener updateUiListener) {
        this.updateUiListener = updateUiListener;
    }

    @Override
    public void updateUserInterface(int refreshPosition) {
        complaintsBtn.setVisibility(View.GONE);
    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }
}