package com.cis.palm360.datasync.ui;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cis.palm360.R;
import com.cis.palm360.cloudhelper.ApplicationThread;
import com.cis.palm360.cloudhelper.CloudDataHandler;
import com.cis.palm360.cloudhelper.Config;
import com.cis.palm360.cloudhelper.Log;
import com.cis.palm360.common.CommonConstants;
import com.cis.palm360.common.CommonUtils;
import com.cis.palm360.database.DataAccessHandler;
import com.cis.palm360.database.DatabaseKeys;
import com.cis.palm360.database.Queries;
import com.cis.palm360.datasync.helpers.DataSyncHelper;
import com.cis.palm360.dbmodels.CropMaintenanceDocs;
import com.cis.palm360.dbmodels.DigitalContract;
import com.cis.palm360.dbmodels.UserSync;
import com.cis.palm360.uihelper.ProgressBar;
import com.cis.palm360.uihelper.ProgressDialogFragment;
import com.cis.palm360.utils.UiUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

/**
 * Created by skasam on 9/30/2016.
 */

//Sync Actions will be performed from here
public class RefreshSyncActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String LOG_TAG = RefreshSyncActivity.class.getName();
    private static int consignmentCount = 0, collectionsCount = 0, collectionPlotsCountInt = 0;
    private TextView tvfarmer, tvidproofs, tvaddress, tvfarmerhistory, tvbank, tvplot, tvplotcurrentcrop,
            tvneighbourplot, tvwaterresource, tvsoilresource, tvplotirrigation, geoBoundriesCountTxt, plantationCountTxt, uprootmentCountTxt, PlotGapFillingDetailsCount,hopCountTxt,
            tvfollowup, tvreferrals, tvmarketsurvey, tvimages, cmpCount, activity_logs, harvestor_visits,recovery_farmers, plantation_Audit,CropMaintenancehistory,
            HarvestorVisithistory;
    private Button btnsend, btnmastersync, btnDBcopy, transSyncBtn, btresetdatabase;
    private DataAccessHandler dataAccessHandler;
    private List<String> collectionCodes, consignmentCodes, farmerCodes, farmerBankCodes, idproofCodes, addressCodes, plotCodes, plotCurrentCropCodes, neighbourPlotCodes, waterResourceCodes,
            soilResourceCodes, plotIrrigationCodes, followupCodes, referralCodes, marketsurveyCodes;
    private List<Pair> collectionPlots = null;
    private List<Pair> farmerhistoryCodes = null;
    private ArrayList<String> tableNames = new ArrayList<>();
    public static ArrayList<String> allRefreshDataMap;
    private TextView vist_logstv;
    public static int resetFarmerCount = 0;
    public static int resetPlotCount = 0;
    public static int afterResetFarmerCount = 0;
    public static int afterResetPlotCount = 0;
    private boolean isDataUpdated = false;
    UserSync userSync;

    private DigitalContract digitalContract;
    private CropMaintenanceDocs cropMaintenanceDocs;

    private ArrayList<DigitalContract> digitalList = new ArrayList<>();
    private ArrayList<CropMaintenanceDocs> cmdocsList = new ArrayList<>(); ;

    //Initialing the Class and adding the tables to the arraylist
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.refresh_sync);
            Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.refresh);
        setSupportActionBar(toolbar);

        allRefreshDataMap = new ArrayList<>();
        allRefreshDataMap.add(DatabaseKeys.TABLE_ADDRESS);
        allRefreshDataMap.add(DatabaseKeys.TABLE_FARMER);
        allRefreshDataMap.add(DatabaseKeys.TABLE_PLOT);
        allRefreshDataMap.add(DatabaseKeys.TABLE_PLOTCURRENTCROP);
        allRefreshDataMap.add(DatabaseKeys.TABLE_NEIGHBOURPLOT);
        allRefreshDataMap.add(DatabaseKeys.TABLE_WATERESOURCE);
        allRefreshDataMap.add(DatabaseKeys.TABLE_PLOTIRRIGATIONTYPEXREF);
        allRefreshDataMap.add(DatabaseKeys.TABLE_SOILRESOURCE);
        allRefreshDataMap.add(DatabaseKeys.TABLE_FOLLOWUP);
        allRefreshDataMap.add(DatabaseKeys.TABLE_REFERRALS);
        allRefreshDataMap.add(DatabaseKeys.TABLE_MARKETSURVEY);
        allRefreshDataMap.add(DatabaseKeys.TABLE_FARMERHISTORY);
        allRefreshDataMap.add(DatabaseKeys.TABLE_IDENTITYPROOF);
        allRefreshDataMap.add(DatabaseKeys.TABLE_FARMERBANK);
        allRefreshDataMap.add(DatabaseKeys.TABLE_PLANTATION);
        allRefreshDataMap.add(DatabaseKeys.TABLE_PLOTLANDLORD);
        allRefreshDataMap.add(DatabaseKeys.TABLE_LANDLORDBANK);
        allRefreshDataMap.add(DatabaseKeys.TABLE_LANDLORDIDPROOFS);
        allRefreshDataMap.add(DatabaseKeys.TABLE_COOKINGOIL);
        allRefreshDataMap.add(DatabaseKeys.TABLE_CROPMAINTENANCEHISTORY);
        allRefreshDataMap.add(DatabaseKeys.TABLE_DISEASE);
        allRefreshDataMap.add(DatabaseKeys.TABLE_FERTLIZER);
        allRefreshDataMap.add(DatabaseKeys.TABLE_RECOMMND_FERTLIZER);
        allRefreshDataMap.add(DatabaseKeys.TABLE_HARVEST);
        allRefreshDataMap.add(DatabaseKeys.TABLE_HEALTHPLANTATION);
        allRefreshDataMap.add(DatabaseKeys.TABLE_INTERCROPPLANTATIONXREF);
        allRefreshDataMap.add(DatabaseKeys.TABLE_NUTRIENT);
        allRefreshDataMap.add(DatabaseKeys.TABLE_OWNERSHIPFILEREPO);
        allRefreshDataMap.add(DatabaseKeys.TABLE_PEST);
        allRefreshDataMap.add(DatabaseKeys.TABLE_UPROOTMENT);
        allRefreshDataMap.add(DatabaseKeys.TABLE_WEED);
        allRefreshDataMap.add(DatabaseKeys.TABLE_YIELDASSESMENT);
        allRefreshDataMap.add(DatabaseKeys.TABLE_WHITE);
        allRefreshDataMap.add(DatabaseKeys.TABLE_IDPROOFFILEREPOXREF);
        allRefreshDataMap.add(DatabaseKeys.TABLE_PESTCHEMICALXREF);
        allRefreshDataMap.add(DatabaseKeys.TABLE_PLANTATIONFILEREPOXREF);
        allRefreshDataMap.add(DatabaseKeys.TABLE_FILEREPOSITORY);
        allRefreshDataMap.add(DatabaseKeys.TABLE_GEOBOUNDARIES);
        allRefreshDataMap.add(DatabaseKeys.TABLE_COMPLAINT);
        allRefreshDataMap.add(DatabaseKeys.TABLE_COMPLAINTSTATUSHISTORY);
        allRefreshDataMap.add(DatabaseKeys.TABLE_COMPLAINTREPOSITORY);
        allRefreshDataMap.add(DatabaseKeys.TABLE_COMPLAINTTYPEXREF);
        allRefreshDataMap.add(DatabaseKeys.TABLE_NURSERYSAPLING_DETAILS);
        allRefreshDataMap.add(DatabaseKeys.TABLE_ADVANCED_DETAILS);
        allRefreshDataMap.add(DatabaseKeys.TABLE_Location_TRACKING_DETAILS);
        allRefreshDataMap.add(DatabaseKeys.TABLE_HarvestorVisitDetails);
        allRefreshDataMap.add(DatabaseKeys.TABLE_Recovery_Farmer_Group);
        allRefreshDataMap.add(DatabaseKeys.TABLE_Plantation_Audit_Answers);
        allRefreshDataMap.add(DatabaseKeys.TABLE_HarvestorVisitHistory);
        allRefreshDataMap.add(DatabaseKeys.TABLE_PlotGapFillingDetails);
        allRefreshDataMap.add(DatabaseKeys.TABLE_Ganoderma);
        dataAccessHandler = new DataAccessHandler(this);

        CommonUtils.currentActivity = this;

        initUI();
        consignmentCount = 0;
        collectionsCount = 0;
        collectionPlotsCountInt = 0;

    }

    /**
     * Intializing UI elements
     */
    private void initUI() {

        geoBoundriesCountTxt = findViewById(R.id.geoBoudariesCount);
        plantationCountTxt = findViewById(R.id.plantationCount);
        uprootmentCountTxt = findViewById(R.id.uprootmentCount);
        hopCountTxt = findViewById(R.id.hopCount);
        PlotGapFillingDetailsCount = findViewById(R.id.PlotGapFillingDetailsCount);
        tvfarmer = findViewById(R.id.farmerCount);
        tvidproofs = findViewById(R.id.idproofCount);
        tvaddress = findViewById(R.id.addressCount);
        tvfarmerhistory = findViewById(R.id.farmerhistoryCount);
        tvbank = findViewById(R.id.bankCount);
        tvplot = findViewById(R.id.plotCount);
        tvplotcurrentcrop = findViewById(R.id.plotcurrentcropCount);
        tvneighbourplot = findViewById(R.id.neighbourplotCount);
        tvwaterresource = findViewById(R.id.waterresourceCount);
        tvsoilresource = findViewById(R.id.soilresourceCount);
        tvplotirrigation = findViewById(R.id.plotirrigationCount);
        tvfollowup = findViewById(R.id.followupCount);
        tvreferrals = findViewById(R.id.referralCount);
        tvmarketsurvey = findViewById(R.id.marketsurveyCount);
        tvimages = findViewById(R.id.allimagesCount);
        cmpCount = findViewById(R.id.cmpCount);
        vist_logstv = findViewById(R.id.vist_logs);

        btnsend = findViewById(R.id.btsynctoserver);
        btnmastersync = findViewById(R.id.btnmastersync);
        btnDBcopy = findViewById(R.id.btcopydatabase);
        transSyncBtn = findViewById(R.id.transSyncBtn);
        btresetdatabase = findViewById(R.id.btresetdatabase);
        activity_logs = findViewById(R.id.activity_logs);
        harvestor_visits = findViewById(R.id.harvestor_visit);
        recovery_farmers = findViewById(R.id.recovery_farmers);
        plantation_Audit = findViewById(R.id.plantation_Audit);
        CropMaintenancehistory = findViewById(R.id.cropMaintenancehistory);
        HarvestorVisithistory =findViewById(R.id.HarvestorVisitHistory);

        btnsend.setOnLongClickListener(view -> {
            Log.v(LOG_TAG, "long pressed");
            CommonUtils.copyFile(RefreshSyncActivity.this);
            return true;
        });

        /** 199 to 207 ** line **/

//        transSyncBtn.setOnClickListener(view -> showTransactionsAlertDialog(false));

        transSyncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTransactionsAlertDialog(false);
//                List<UserSync> resetList = (List<UserSync>)dataAccessHandler.getUserSyncData(Queries.getInstance().countOfTraSync());
                List<UserSync> resetList = (List<UserSync>) dataAccessHandler.getUserSyncData(Queries.getInstance().countOfSync(CommonConstants.USER_ID));

                if (resetList.size() == 0) {
                    Log.v("@@@MM", "mas");
                    if(Integer.parseInt(CommonConstants.USER_ID) != 12345) {
                        addUserTraSyncDetails();
                    }
                } else {
                    dataAccessHandler.updateTransactionSync();
                }


            }
        });


        btresetdatabase.setOnClickListener(view -> {

            fetchCount();
            showTransactionsAlertDialog(true);
            // List<UserSync> resetList = (List<UserSync>)dataAccessHandler.getUserSyncData(Queries.getInstance().countOfResetdata());
            List<UserSync> resetList = (List<UserSync>) dataAccessHandler.getUserSyncData(Queries.getInstance().countOfSync(CommonConstants.USER_ID));

            if (resetList.size() == 0) {
                if(Integer.parseInt(CommonConstants.USER_ID) != 12345) {
                    Log.v("@@@MM", "mas");
                    addUserResetSyncDetails();
                }
            } else {
                dataAccessHandler.updateResetDataSync();
            }

        });

        btnsend.setOnClickListener(this);
        btnmastersync.setOnClickListener(this);
        btnDBcopy.setOnClickListener(this);


        bindData();

        if (tvfarmer.getText().toString().equalsIgnoreCase("0")
                && tvaddress.getText().toString().equalsIgnoreCase("0")
                && tvimages.getText().toString().equalsIgnoreCase("0")
                && tvplot.getText().toString().equalsIgnoreCase("0")
                && tvplotcurrentcrop.getText().toString().equalsIgnoreCase("0")
                && tvneighbourplot.getText().toString().equalsIgnoreCase("0")
                && tvwaterresource.getText().toString().equalsIgnoreCase("0")
                && tvsoilresource.getText().toString().equalsIgnoreCase("0")
                && tvplotirrigation.getText().toString().equalsIgnoreCase("0")
                && tvfollowup.getText().toString().equalsIgnoreCase("0")
                && tvreferrals.getText().toString().equalsIgnoreCase("0")
                && tvmarketsurvey.getText().toString().equalsIgnoreCase("0")
                && tvidproofs.getText().toString().equalsIgnoreCase("0")
                && tvfarmerhistory.getText().toString().equalsIgnoreCase("0")
                && tvbank.getText().toString().equalsIgnoreCase("0")
                && geoBoundriesCountTxt.getText().toString().equalsIgnoreCase("0")
                && uprootmentCountTxt.getText().toString().equalsIgnoreCase("0")
                && PlotGapFillingDetailsCount.getText().toString().equalsIgnoreCase("0")
                && plantationCountTxt.getText().toString().equalsIgnoreCase("0")
                && hopCountTxt.getText().toString().equalsIgnoreCase("0")
                && cmpCount.getText().toString().equalsIgnoreCase("0")
                && vist_logstv.getText().toString().equalsIgnoreCase("0")
                && activity_logs.getText().toString().equalsIgnoreCase("0")
                && harvestor_visits.getText().toString().equalsIgnoreCase("0")
        ) {

//            btresetdatabase.setEnabled(true);
//            btresetdatabase.setVisibility(View.VISIBLE);

        } else {
//            btresetdatabase.setEnabled(false);
//            btresetdatabase.setVisibility(View.GONE);
        }

    }

    //fetching the count from DB
    public void fetchCount() {
        resetFarmerCount = Integer.parseInt(dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getFarmerCount()));
        resetPlotCount = Integer.parseInt(dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getPlotCount()));
    }


    //Binding data to the fields based on status
    private void bindData() {
        try {

            tvfarmer.setText(dataAccessHandler.getCountValue(Queries.getInstance().getRefreshCountQuery("Farmer")));
            tvaddress.setText(dataAccessHandler.getCountValue(Queries.getInstance().getRefreshCountQuery("Address")));
            tvimages.setText(dataAccessHandler.getCountValue(Queries.getInstance().getRefreshCountQueryForFileRepo()));
            tvplot.setText(dataAccessHandler.getCountValue(Queries.getInstance().getRefreshCountQuery("Plot")));
            tvplotcurrentcrop.setText(dataAccessHandler.getCountValue(Queries.getInstance().getRefreshCountQuery("PlotCurrentCrop")));
            tvneighbourplot.setText(dataAccessHandler.getCountValue(Queries.getInstance().getRefreshCountQuery("NeighbourPlot")));
            tvwaterresource.setText(dataAccessHandler.getCountValue(Queries.getInstance().getRefreshCountQuery("WaterResource")));
            tvsoilresource.setText(dataAccessHandler.getCountValue(Queries.getInstance().getRefreshCountQuery("SoilResource")));
            tvplotirrigation.setText(dataAccessHandler.getCountValue(Queries.getInstance().getRefreshCountQuery("PlotIrrigationTypeXref")));
            tvfollowup.setText(dataAccessHandler.getCountValue(Queries.getInstance().getRefreshCountQuery("FollowUp")));
            tvreferrals.setText(dataAccessHandler.getCountValue(Queries.getInstance().getRefreshCountQuery("Referrals")));
            tvmarketsurvey.setText(dataAccessHandler.getCountValue(Queries.getInstance().getRefreshCountQuery("MarketSurvey")));
            tvidproofs.setText(dataAccessHandler.getCountValue(Queries.getInstance().getRefreshCountQuery("IdentityProof")));
            tvfarmerhistory.setText(dataAccessHandler.getCountValue(Queries.getInstance().getRefreshCountQuery("FarmerHistory ")));
            tvbank.setText(dataAccessHandler.getCountValue(Queries.getInstance().getRefreshCountQuery("FarmerBank")));

            geoBoundriesCountTxt.setText(dataAccessHandler.getCountValue(Queries.getInstance().getRefreshCountQuery("GeoBoundaries")));
            uprootmentCountTxt.setText(dataAccessHandler.getCountValue(Queries.getInstance().getRefreshCountQuery("Uprootment")));
            plantationCountTxt.setText(dataAccessHandler.getCountValue(Queries.getInstance().getRefreshCountQuery("Plantation")));
            hopCountTxt.setText(dataAccessHandler.getCountValue(Queries.getInstance().getRefreshCountQuery("HealthPlantation")));
            cmpCount.setText(dataAccessHandler.getCountValue(Queries.getInstance().getRefreshCountQuery("Complaints")));
            PlotGapFillingDetailsCount.setText(dataAccessHandler.getCountValue(Queries.getInstance().getRefreshCountQueryforPlotGapFillingDetails("PlotGapFillingDetails")));

            //getVistLogRecords
            String getVistLogRecords = dataAccessHandler.getCountValue(Queries.getInstance().getRefreshCountQuery("VisitLog"));
            Log.v(LOG_TAG, "getVistLogRecords " + getVistLogRecords);
            vist_logstv.setText(getVistLogRecords);
            String recomFertilizer = dataAccessHandler.getCountValue(Queries.getInstance().getRefreshCountQuery("FertilizerRecommendations"));
            Log.v(LOG_TAG, "recomFertilizer data count" + recomFertilizer);
            activity_logs.setText(dataAccessHandler.getCountValue(Queries.getInstance().getRefreshCountQuery("ActivityLog")));
            harvestor_visits.setText(dataAccessHandler.getCountValue(Queries.getInstance().getRefreshCountQuery("HarvestorVisitDetails")));
            recovery_farmers.setText(dataAccessHandler.getCountValue(Queries.getInstance().getRefreshCountQuery("RecoveryFarmerGroup")));
            plantation_Audit.setText(dataAccessHandler.getCountValue(Queries.getInstance().getRefreshCountQuery("PlotPlantationAuditDetails")));
            CropMaintenancehistory.setText(dataAccessHandler.getCountValue(Queries.getInstance().getRefreshCountQuery("CropMaintenanceHistory")));
            HarvestorVisithistory.setText(dataAccessHandler.getCountValue(Queries.getInstance().getRefreshCountQuery("HarvestorVisitHistory")));


            isDataUpdated = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //On Click Listeners
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btsynctoserver:

//                List<UserSync> traList = (List<UserSync>)dataAccessHandler.getUserSyncData(Queries.getInstance().countOfTraSync());
//                Log.v("@@@MM","trasize"+traList.size());
//                if(traList.size()==0){
//                    Log.v("@@@MM","mas");
//                    addUserTraSyncDetails();
//                }

                if (CommonUtils.isNetworkAvailable(RefreshSyncActivity.this)) {

                    btnsend.setVisibility(View.GONE);
                    isDataUpdated = false;
                    DataSyncHelper.performRefreshTransactionsSync(RefreshSyncActivity.this, new ApplicationThread.OnComplete() {
                        @Override
                        public void execute(boolean success, Object result, String msg) {
                            if (success) {
                                ApplicationThread.uiPost(LOG_TAG, "transactions sync message", new Runnable() {
                                    @Override
                                    public void run() {
                                        bindData();
//                                        Toasty.success(RefreshSyncActivity.this,"Successfully data sent to server",10).show();
                                        if (isDataUpdated) {
                                            UiUtils.showCustomToastMessage("Successfully data sent to server", RefreshSyncActivity.this, 0);
                                            ProgressBar.hideProgressBar();
                                            btnsend.setVisibility(View.VISIBLE);
                                          //  dataAccessHandler.updateUserSync();
                                        }

                                    }
                                });
                            } else {
                                ApplicationThread.uiPost(LOG_TAG, "transactions sync failed message", new Runnable() {
                                    @Override
                                    public void run() {
                                        bindData();
                                        Toasty.error(RefreshSyncActivity.this, "Data sending failed", 10).show();
//                                        Toast.makeText(RefreshSyncActivity.this, "Data sending failed", Toast.LENGTH_SHORT).show();
                                        ProgressBar.hideProgressBar();
                                        btnsend.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        }
                    });
                } else {
                    UiUtils.showCustomToastMessage("Please check network connection", RefreshSyncActivity.this, 1);
                    btnsend.setVisibility(View.VISIBLE);
                }


                break;

            case R.id.btnmastersync:

                if (CommonUtils.isNetworkAvailable(RefreshSyncActivity.this)) {
                    DataSyncHelper.performMasterSync(this, false, new ApplicationThread.OnComplete() {
                        @Override
                        public void execute(boolean success, Object result, String msg) {
                            ProgressBar.hideProgressBar();
                            if (success) {
                                if (!msg.equalsIgnoreCase("Sync is up-to-date")) {
                                    Toast.makeText(RefreshSyncActivity.this, "Data synced successfully", Toast.LENGTH_SHORT).show();
                                    digitalpdfsave();
                                    // List<UserSync> userSyncList = (List<UserSync>)dataAccessHandler.getUserSyncData(Queries.getInstance().countOfMasterSync());
                                    List<UserSync> userSyncList = (List<UserSync>) dataAccessHandler.getUserSyncData(Queries.getInstance().countOfSync(CommonConstants.USER_ID));

                                    if (userSyncList.size() == 0) {
                                        if(Integer.parseInt(CommonConstants.USER_ID) != 12345) {
                                            Log.v("@@@MM", "mas");
                                            addUserMasSyncDetails();
                                        }
                                    } else {
                                        dataAccessHandler.updateMasterSync();
                                    }

                                    // DataAccessHandler dataAccessHandler = new DataAccessHandler(RefreshSyncActivity.this);
                                    // dataAccessHandler.updateMasterSyncDate(false, CommonConstants.USER_ID);
                                } else {
                                    ApplicationThread.uiPost(LOG_TAG, "master sync message", new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(RefreshSyncActivity.this, "You have updated data", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } else {
                                Log.v(LOG_TAG, "@@@ Master sync failed " + msg);
                                ApplicationThread.uiPost(LOG_TAG, "master sync message", new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RefreshSyncActivity.this, "Master sync failed. Please try again", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                } else {
                    UiUtils.showCustomToastMessage("Please check network connection", RefreshSyncActivity.this, 1);
                }
                break;
            case R.id.btcopydatabase:
                showAlertDialog();
                break;
            default:
                break;
        }

    }

    //Adding Usersync data when transactionsync is performed
    public void addUserTraSyncDetails() {

        SimpleDateFormat simpledatefrmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentTime = simpledatefrmt.format(new Date());

        userSync = new UserSync();
        userSync.setUserId(Integer.parseInt(CommonConstants.USER_ID));
        userSync.setApp("3fMainApp");
        userSync.setDate(CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
        userSync.setMasterSync(0);
        userSync.setTransactionSync(1);
        userSync.setResetData(0);
        userSync.setIsActive(1);
        userSync.setCreatedByUserId(Integer.parseInt(CommonConstants.USER_ID));
        userSync.setCreatedDate(CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
        userSync.setUpdatedByUserId(Integer.parseInt(CommonConstants.USER_ID));
        userSync.setServerUpdatedStatus(0);
        userSync.setUpdatedDate(CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
        dataAccessHandler.addUserSync(userSync);

    }

    //Adding Usersync data when mastersync is performed

    public void addUserMasSyncDetails() {

        SimpleDateFormat simpledatefrmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentTime = simpledatefrmt.format(new Date());

        userSync = new UserSync();
        userSync.setUserId(Integer.parseInt(CommonConstants.USER_ID));
        userSync.setApp("" + "3fMainApp");
        userSync.setDate(CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
        userSync.setMasterSync(1);
        userSync.setTransactionSync(0);
        userSync.setResetData(0);
        userSync.setIsActive(1);
        userSync.setCreatedByUserId(Integer.parseInt(CommonConstants.USER_ID));
        userSync.setCreatedDate(CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
        userSync.setUpdatedByUserId(Integer.parseInt(CommonConstants.USER_ID));
        userSync.setUpdatedDate(CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
        userSync.setServerUpdatedStatus(0);

        long resul = dataAccessHandler.addUserSync(userSync);
        if (resul > -1) {
            Log.v("@@@MM", "Success");
        }

    }

    //Adding Usersync data when reset data is performed

    public void addUserResetSyncDetails() {

        SimpleDateFormat simpledatefrmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentTime = simpledatefrmt.format(new Date());

        userSync = new UserSync();
        userSync.setUserId(Integer.parseInt(CommonConstants.USER_ID));
        userSync.setApp("3fMainApp");
        userSync.setDate(CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
        userSync.setMasterSync(0);
        userSync.setTransactionSync(0);
        userSync.setResetData(1);
        userSync.setIsActive(1);
        userSync.setCreatedByUserId(Integer.parseInt(CommonConstants.USER_ID));
        userSync.setCreatedDate(CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
        userSync.setUpdatedByUserId(Integer.parseInt(CommonConstants.USER_ID));
        userSync.setUpdatedDate(CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
        userSync.setServerUpdatedStatus(0);
        dataAccessHandler.addUserSync(userSync);

    }


    //Upload DB method
    public void uploadDataBase(final File uploadDbFile, final ApplicationThread.OnComplete<String> onComplete) {
        if (null != uploadDbFile) {
            final long nanoTime = System.nanoTime();
            final String filePathToSave = "/sdcard/3f_" + CommonConstants.TAB_ID + "_" + nanoTime + "_v_" + CommonUtils.getAppVersion(RefreshSyncActivity.this) + ".gzip";
            final File toZipFile = getDbFileToUpload();
            CommonUtils.gzipFile(toZipFile, filePathToSave, new ApplicationThread.OnComplete<String>() {
                @Override
                public void execute(boolean success, String result, String msg) {
                    if (success) {
                        File dir = Environment.getExternalStorageDirectory();
                        File uploadFile = new File(dir, "3f_" + CommonConstants.TAB_ID + "_" + nanoTime + "_v_" + CommonUtils.getAppVersion(RefreshSyncActivity.this) + ".gzip");
                        Log.v(LOG_TAG, "@@@ file size " + uploadFile.length());
                        if (uploadFile != null) {
                            CloudDataHandler.uploadFileToServer(uploadFile, Config.live_url + Config.updatedbFile, new ApplicationThread.OnComplete<String>() {
                                @Override
                                public void execute(boolean success, String result, String msg) {
                                    onComplete.execute(success, result, msg);
                                }
                            });
                        } else {
                            onComplete.execute(false, "failed", "data base is empty");
                        }

                    } else {
                        onComplete.execute(success, result, msg);
                    }
                }
            });
        } else {
            onComplete.execute(false, "file upload failed", "null database");
        }

    }

    public boolean copy(File src, File dst) {

        try {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dst);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            return true;
        } catch (Exception e) {
            android.util.Log.w("Settings Backup", e);
            return false;
        }
    }

    //get the file to upload from the path
    public File getDbFileToUpload() {
        try {
//            File dir = Environment.getExternalStorageDirectory();
            File dbFileToUpload = new File("/sdcard/3F_Files/3F_Database/3foilpalm.sqlite");
            return dbFileToUpload;
        } catch (Exception e) {
            android.util.Log.w("Settings Backup", e);
        }
        return null;
    }


    public void uploadDatabaseFile() {
        ApplicationThread.bgndPost(LOG_TAG, "upload database..", new Runnable() {
            @Override
            public void run() {
                uploadDataBase(getDbFileToUpload(), new ApplicationThread.OnComplete<String>() {
                    @Override
                    public void execute(boolean success, String result, String msg) {
                        ProgressBar.hideProgressBar();
                        if (success) {
                            Log.v(LOG_TAG, "@@@ 3f db file upload success");
                            dataAccessHandler.executeRawQuery("DELETE FROM ErrorLogs");
                            Log.v(LOG_TAG, "delete table ErrorLogs");
                            CommonUtils.showToast("3f db file uploaded successfully", RefreshSyncActivity.this);
                        } else {
                            Log.v(LOG_TAG, "@@@ 3f db file upload failed due to " + msg);
                            CommonUtils.showToast("3f db file upload failed due to" + msg, RefreshSyncActivity.this);
                        }
                    }
                });
            }
        });
    }

    //Alert dialog for upload db
    public void showAlertDialog() {
        final Dialog dialog = new Dialog(RefreshSyncActivity.this);
        dialog.setContentView(R.layout.custom_alert_dailog);

        Button yesDialogButton = dialog.findViewById(R.id.Yes);
        Button noDialogButton = dialog.findViewById(R.id.No);
        TextView msg = dialog.findViewById(R.id.test);
        yesDialogButton.setTextColor(getResources().getColor(R.color.green));
        noDialogButton.setTextColor(getResources().getColor(R.color.btnPressedColor));
        msg.setText(R.string.do_you_want_to_upload_data_base_to_server);
        // if button is clicked, close the custom dialog
        yesDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtils.isNetworkAvailable(RefreshSyncActivity.this)) {
                    dialog.dismiss();
                    ProgressBar.showProgressBar(RefreshSyncActivity.this, "uploading database...");
                    CommonUtils.copyFile(RefreshSyncActivity.this);
                    uploadDatabaseFile();
                } else {
                    dialog.dismiss();
                    UiUtils.showCustomToastMessage("Please check network connection", RefreshSyncActivity.this, 1);
                }
            }
        });
        dialog.show();
        noDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                        Toast.makeText(getApplicationContext(),"Dismissed..!!",Toast.LENGTH_SHORT).show();
            }
        });

    }

//alert dialog for transactions sync
    public void showTransactionsAlertDialog(final boolean fromReset) {
        final Dialog dialog = new Dialog(RefreshSyncActivity.this);
        dialog.setContentView(R.layout.custom_alert_dailog);

        Button yesDialogButton = dialog.findViewById(R.id.Yes);
        Button noDialogButton = dialog.findViewById(R.id.No);
        TextView msg = dialog.findViewById(R.id.test);
        yesDialogButton.setTextColor(getResources().getColor(R.color.green));
        noDialogButton.setTextColor(getResources().getColor(R.color.btnPressedColor));
        msg.setText(R.string.you_want_to_perform_transactions_sync);
        // if button is clicked, close the custom dialog
        yesDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                List<UserSync> resetList = (List<UserSync>)dataAccessHandler.getUserSyncData(Queries.getInstance().countOfResetdata());
//                if(resetList.size()==0){
//                    Log.v("@@@MM","mas");
//                    addUserResetSyncDetails();
//                }
                if (CommonUtils.isNetworkAvailable(RefreshSyncActivity.this)) {
                    dialog.dismiss();
                    if (fromReset) {
                        DataSyncHelper.updateSyncDate(RefreshSyncActivity.this, null);
                        for (String s : allRefreshDataMap) {
                            dataAccessHandler.executeRawQuery("DELETE FROM " + s);
                            Log.v(LOG_TAG, "delete table" + s);
                        }
                    }
                    FragmentManager fm = getSupportFragmentManager();
                    ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
                    progressDialogFragment.show(fm, "progress dialog fragment");
                    DataSyncHelper.startTransactionSync(RefreshSyncActivity.this, progressDialogFragment);
                } else {
                    dialog.dismiss();
                    UiUtils.showCustomToastMessage("Please check network connection", RefreshSyncActivity.this, 1);
                }
            }
        });
        dialog.show();
        noDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                        Toast.makeText(getApplicationContext(),"Dismissed..!!",Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void updateCollectionsData() {
        if (null != collectionCodes && !collectionCodes.isEmpty()) {
            CloudDataHandler.getRecordStatus(String.format(Config.live_url + Config.findcollectioncode, collectionCodes.get(collectionsCount)), new ApplicationThread.OnComplete<String>() {
                @Override
                public void execute(boolean success, String result, String msg) {
                    if (success && !TextUtils.isEmpty(result) && result.equalsIgnoreCase("true")) {
                        dataAccessHandler.executeRawQuery(Queries.getInstance().updatedCollectionDetailsStatus("'" + collectionCodes.get(collectionsCount)) + "'");
                    }
                    collectionsCount++;
                    if (collectionsCount == collectionCodes.size()) {
                        Log.v(LOG_TAG, "update finished");
                        ApplicationThread.uiPost(LOG_TAG, "update ui", new Runnable() {
                            @Override
                            public void run() {
                                updateConsignmentData();
                            }
                        });
                    } else {
                        updateCollectionsData();
                    }
                }
            });
        } else {
            updateConsignmentData();
        }
    }

    public void updateConsignmentData() {
        if (null != consignmentCodes && !consignmentCodes.isEmpty()) {
            CloudDataHandler.getRecordStatus(String.format(Config.live_url + Config.findconsignmentcode, consignmentCodes.get(consignmentCount)), new ApplicationThread.OnComplete<String>() {
                @Override
                public void execute(boolean success, String result, String msg) {
                    if (success && !TextUtils.isEmpty(result) && result.equalsIgnoreCase("true")) {
                        dataAccessHandler.executeRawQuery(Queries.getInstance().updatedConsignmentDetailsStatus("'" + consignmentCodes.get(consignmentCount)) + "'");
                    }
                    consignmentCount++;
                    if (consignmentCount == consignmentCodes.size()) {
                        Log.v(LOG_TAG, "update finished");
                        ApplicationThread.uiPost(LOG_TAG, "update ui", new Runnable() {
                            @Override
                            public void run() {
                                updateCollectionPlotsData();
                            }
                        });
                    } else {
                        updateConsignmentData();
                    }
                }
            });
        } else {
            updateCollectionPlotsData();
        }
    }

    public void updateCollectionPlotsData() {
        if (null != collectionPlots && !collectionPlots.isEmpty()) {
            CloudDataHandler.getRecordStatus(String.format(Config.live_url + Config.findcollectionplotcode, collectionPlots.get(collectionPlotsCountInt).first, collectionPlots.get(collectionPlotsCountInt).second), new ApplicationThread.OnComplete<String>() {
                @Override
                public void execute(boolean success, String result, String msg) {
                    Pair collectionPlotPair = collectionPlots.get(collectionPlotsCountInt);
                    if (success && !TextUtils.isEmpty(result) && result.equalsIgnoreCase("true")) {
                        dataAccessHandler.executeRawQuery(Queries.getInstance().updatedCollectionPlotXRefDetailsStatus(collectionPlotPair.first.toString(), collectionPlotPair.second.toString()));
                    }
                    collectionPlotsCountInt++;
                    if (collectionPlotsCountInt == collectionPlots.size()) {
                        Log.v(LOG_TAG, "update finished");
                        ApplicationThread.uiPost(LOG_TAG, "update ui", new Runnable() {
                            @Override
                            public void run() {
                                ProgressBar.hideProgressBar();
                                bindData();
                            }
                        });
                    } else {
                        updateCollectionPlotsData();
                    }
                }
            });
        } else {
            ProgressBar.hideProgressBar();
        }
    }

    public void updateFarmerCodesData() {
        if (null != farmerCodes && !farmerCodes.isEmpty()) {
            CloudDataHandler.getRecordStatus(String.format(Config.live_url + Config.findcollectioncode, collectionCodes.get(collectionsCount)), new ApplicationThread.OnComplete<String>() {
                @Override
                public void execute(boolean success, String result, String msg) {
                    if (success && !TextUtils.isEmpty(result) && result.equalsIgnoreCase("true")) {
                        dataAccessHandler.executeRawQuery(Queries.getInstance().updatedCollectionDetailsStatus("'" + collectionCodes.get(collectionsCount)) + "'");
                    }
                    collectionsCount++;
                    if (collectionsCount == collectionCodes.size()) {
                        Log.v(LOG_TAG, "update finished");
                        ApplicationThread.uiPost(LOG_TAG, "update ui", new Runnable() {
                            @Override
                            public void run() {
                                updateConsignmentData();
                            }
                        });
                    } else {
                        updateCollectionsData();
                    }
                }
            });
        } else {
            updateConsignmentData();
        }
    }

    private void digitalpdfsave() {
        dataAccessHandler = new DataAccessHandler(RefreshSyncActivity.this);
        digitalList = (ArrayList<DigitalContract>) dataAccessHandler.getDigitalContractData(Queries.getInstance().getDigitalContract(),1);
        Log.e("========>datasize", digitalList.size() + "");
        for (int i = 0; i < digitalList.size(); i++) {
            int stateid = digitalList.get(i).getStateId();
            Log.e("========>156", stateid + "");
            // fileExtention =digitalContract.getFileExtension();
            // rootDirectory = new File(CommonUtils.get3FFileRootPath() + "3F_DigitalContract/");
            digitalContract = (DigitalContract) dataAccessHandler.getDigitalContractData(Queries.getInstance().getDigitalContractbystatecode(stateid), 0);
            String url = Config.image_url + "/" + digitalContract.getFileLocation() + "/" + digitalContract.getFILENAME() + digitalContract.getFileExtension();
            Log.e("========>171url", url + "");

            new DownloadpdfFileFromURL(digitalContract.getFILENAME(), digitalContract.getFileExtension()).execute(url);}
        cmdocssave();

//                                new DownloadpdfFileFromURL().execute(url);
    }
    private void cmdocssave() {
        dataAccessHandler = new DataAccessHandler(RefreshSyncActivity.this);
        cmdocsList = (ArrayList<CropMaintenanceDocs>) dataAccessHandler.getCMDocsData(Queries.getInstance().getAllCMDocs(),1);
        Log.e("cmdocsListsize", cmdocsList.size() + "");
        for (int i = 0; i < cmdocsList.size(); i++) {
            int sectionId = cmdocsList.get(i).getCMSectionId();
            Log.e("sectionId", sectionId + "");
            // fileExtention =digitalContract.getFileExtension();
            // rootDirectory = new File(CommonUtils.get3FFileRootPath() + "3F_DigitalContract/");
            cropMaintenanceDocs = (CropMaintenanceDocs) dataAccessHandler.getCMDocsData(Queries.getInstance().getCMDocsbysectionId(sectionId), 0);
            String cmdocurl = Config.image_url + "/" + cropMaintenanceDocs.getFileLocation() + "/" + cropMaintenanceDocs.getFileName() + cropMaintenanceDocs.getFileExtension();
            Log.e("cmdocurlurl", cmdocurl + "");

            new DownloadCMDocsFromURL(cropMaintenanceDocs.getFileName(), cropMaintenanceDocs.getFileExtension()).execute(cmdocurl);}
    }

    public class DownloadpdfFileFromURL extends AsyncTask<String, Void, String> {

        private boolean downloadSuccess = false;
        private String filename;
        private String fileExtension;

        public DownloadpdfFileFromURL(String filename, String fileExtension) {
            this.filename = filename;
            this.fileExtension = fileExtension;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String rootDirectory = CommonUtils.get3FFileRootPath() + "3F_DigitalContract/";
                File directory = new File(rootDirectory);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                OutputStream output = new FileOutputStream(rootDirectory + filename + fileExtension);

                byte data[] = new byte[1024];

                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
                downloadSuccess = true;
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                downloadSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            if (downloadSuccess) {
                File fileToDownload = new File(CommonUtils.get3FFileRootPath() + "3F_DigitalContract/" + filename + fileExtension);
                Log.d("File Path:", fileToDownload.getAbsolutePath());
                if (fileToDownload.exists()) {
                    Log.d("File Path:", fileToDownload.getAbsolutePath());
                } else {
                    UiUtils.showCustomToastMessage("File does not exist", RefreshSyncActivity.this, 1);
                }
            }
        }
    }


    public class DownloadCMDocsFromURL extends AsyncTask<String, Void, String> {

        private boolean downloadSuccess = false;
        private String filename;
        private String fileExtension;

        public DownloadCMDocsFromURL(String filename, String fileExtension) {
            this.filename = filename;
            this.fileExtension = fileExtension;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String rootDirectory = CommonUtils.get3FFileRootPath() + "3F_CMDocs/";
                File directory = new File(rootDirectory);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                OutputStream output = new FileOutputStream(rootDirectory + filename + fileExtension);

                byte data[] = new byte[1024];

                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
                downloadSuccess = true;
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                downloadSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            if (downloadSuccess) {
                File fileToDownload = new File(CommonUtils.get3FFileRootPath() + "3F_CMDocs/" + filename + fileExtension);
                Log.d("File Path:", fileToDownload.getAbsolutePath());
                if (fileToDownload.exists()) {
                    Log.d("File Path:", fileToDownload.getAbsolutePath());
                } else {
                    UiUtils.showCustomToastMessage("File does not exist", RefreshSyncActivity.this, 1);
                }
            }
        }
    }

}
