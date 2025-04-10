package com.cis.palm360.cropmaintenance;

import static android.app.PendingIntent.getActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cis.palm360.R;
import com.cis.palm360.areaextension.AreaWaterTypeFragment;
import com.cis.palm360.areaextension.GeoTagFragment;
import com.cis.palm360.areaextension.PersonalDetailsFragment;
import com.cis.palm360.areaextension.PlotDetailsFragment;
import com.cis.palm360.areaextension.ReferralsFragment;
import com.cis.palm360.areaextension.SoilTypeFragment;
import com.cis.palm360.areaextension.TypeChooserDialog;
import com.cis.palm360.areaextension.UpdateUiListener;
import com.cis.palm360.areaextension.WaterSoilTypeDialogFragment;
import com.cis.palm360.cloudhelper.ApplicationThread;
import com.cis.palm360.cloudhelper.Log;
import com.cis.palm360.common.CommonConstants;
import com.cis.palm360.common.CommonUiUtils;
import com.cis.palm360.common.CommonUtils;
import com.cis.palm360.common.FiscalDate;
import com.cis.palm360.conversion.InterCropDetailsFragment;
import com.cis.palm360.database.DataAccessHandler;
import com.cis.palm360.database.DataSavingHelper;
import com.cis.palm360.database.DatabaseKeys;
import com.cis.palm360.database.Queries;
import com.cis.palm360.datasync.helpers.DataManager;
import com.cis.palm360.dbmodels.Farmer;
import com.cis.palm360.dbmodels.Harvest;
import com.cis.palm360.dbmodels.InterCropPlantationXref;
import com.cis.palm360.dbmodels.Plantation;
import com.cis.palm360.dbmodels.Plot;
import com.cis.palm360.dbmodels.WhiteFlyAssessment;
import com.cis.palm360.farmersearch.DisplayPlotsFragment;
import com.cis.palm360.dbmodels.CropModel;
import com.cis.palm360.ui.ComplaintsScreenActivity;
import com.cis.palm360.ui.HomeScreen;
import com.cis.palm360.ui.OilPalmBaseActivity;
import com.cis.palm360.uihelper.ProgressBar;
import com.cis.palm360.utils.UiUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cdflynn.android.library.checkview.CheckView;

import static com.cis.palm360.common.CommonUiUtils.isDISEASEdata;
import static com.cis.palm360.common.CommonUiUtils.isFarmerMandatoryDataEntered;
import static com.cis.palm360.common.CommonUiUtils.isGandermaDataEntered;
import static com.cis.palm360.common.CommonUiUtils.isGandermaDatacheck;
import static com.cis.palm360.common.CommonUiUtils.isNDdata;
import static com.cis.palm360.common.CommonUiUtils.ispestdata;

//Crop maintenance home screen modules
public class CropMaintenanceHomeScreen extends OilPalmBaseActivity implements TypeChooserDialog.typeChooser, UpdateUiListener, WaterSoilTypeDialogFragment.onTypeSelected {

    public static Bitmap userImageData;
    public static byte[] imageData = null;
    private Button viewSnapShotBtn;
    private Button updateFarmerBtn;
    private Button updatePlotBtn;
    private String PlotDistrictId = "0";
    private String secondPreviousFYWhiteFlyList="0";
    private String previousFYWhiteFlyList="0";
    private String Qtrfertilizer="0";
    private String cocaintercrop="0";
    private Button wspBtn;
    private Button plantationBtn;
    private Button currentPlantation;
    private Button fertilizerBtn;
    private Button Recom_fertilizerBtn;
    private Button pestBtn;
    private Button diseaseBtn;
    private Button whiteFlyBtn;
    private Button weedManagementBtn;
    private Button hopBtn;
    private Button harvestDetailsBtn;
    private Button complaintsBtn;
    private Button referralsBtn;
    private Button geoTagsBtn;
    private Button yieldBtn;
    private Button finishBtn, interCropDetailsBtn, fertilizer_detailsBtn, bankDetailsBtn, farmerIdBtn,ganodermaInfestation;
    private File fileToUpload;
    private String bindImageClass = "";
    private DataAccessHandler dataAccessHandler;
    private boolean doubleback = false;
    List<CropModel> cropModelList = new ArrayList<>();
    private ScrollView scrollViewLayout;
    CheckView checkView;
    private String pmindate,pmaxdate;
    Calendar calendar;
    public String currentFYStr, previousFYStr, secondpreviousFYStr;
    public String currentYearStr, previousYearStr, secondpreviousYearStr;


    //Initializing the Class
    @Override
    public void Initialize() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View parentView = inflater.inflate(R.layout.content_crop_maintenance_home_screen, null);
        baseLayout.addView(parentView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        android.util.Log.v("@@@KK",CommonConstants.PLOT_CODE);
        initView();
        setTile(getString(R.string.crop_maintenance));
    }

    //Initializing the UI
    private void initView() {
        Log.d("CommonPlotCode", CommonConstants.PLOT_CODE);
        CommonConstants.CURRENT_TREE=1;
        //CommonConstants.Prev_Fertilizer_CMD="";
        CommonConstants.perc_tree= ' ';
        CommonConstants.perc_tree_pest=' ';
        CommonConstants.perc_tree_disease=' ';
        dataAccessHandler = new DataAccessHandler(this);
        viewSnapShotBtn = (Button) findViewById(R.id.viewSnapShotBtn);
        updateFarmerBtn = (Button) findViewById(R.id.personalDetailsBtn);
        updateFarmerBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
        updatePlotBtn = (Button) findViewById(R.id.plotDetailsBtn);
        updatePlotBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
        wspBtn = (Button) findViewById(R.id.wspBtn);
        plantationBtn = (Button) findViewById(R.id.plantationBtn);
        currentPlantation = (Button) findViewById(R.id.currentPlantation);
        fertilizerBtn = (Button) findViewById(R.id.fertilizerBtn);
        Recom_fertilizerBtn = (Button) findViewById(R.id.Recom_fertilizer_detailsBtn);
        pestBtn = (Button) findViewById(R.id.pestBtn);
        diseaseBtn = (Button) findViewById(R.id.diseaseBtn);
        ganodermaInfestation = (Button)findViewById(R.id.ganodermaInfestation);
        weedManagementBtn = (Button) findViewById(R.id.weedManagementBtn);
        hopBtn = (Button) findViewById(R.id.hopBtn);
        harvestDetailsBtn = (Button) findViewById(R.id.harvestDetailsBtn);
        complaintsBtn = (Button) findViewById(R.id.complaintsBtn);
        referralsBtn = (Button) findViewById(R.id.referralsBtn);
        yieldBtn = (Button) findViewById(R.id.yieldBtn);
        whiteFlyBtn = (Button) findViewById(R.id.whiteFlyBtn);

        finishBtn = (Button) findViewById(R.id.finishBtn);
        interCropDetailsBtn = (Button) findViewById(R.id.interCropDetailsBtn);
        fertilizer_detailsBtn = (Button) findViewById(R.id.fertilizer_detailsBtn);
        bankDetailsBtn = (Button) findViewById(R.id.farmerBankBtn);
        farmerIdBtn = (Button) findViewById(R.id.farmerIdBtn);
        geoTagsBtn = (Button) findViewById(R.id.geoTagsBtn);
        scrollViewLayout = (ScrollView) findViewById(R.id.mainScroll);
         checkView = findViewById(R.id.check);

        if (CommonUtils.isFromCropMaintenance()){
            if (null != DataManager.getInstance().getDataFromManager(DataManager.FARMER_PERSONAL_DETAILS)) {
                Farmer savedFarmerData = (Farmer) DataManager.getInstance().getDataFromManager(DataManager.FARMER_PERSONAL_DETAILS);
                Plot savedPlot = (Plot) DataManager.getInstance().getDataFromManager(DataManager.PLOT_DETAILS);
//                CommonConstants.FARMER_CODE = savedFarmerData.getCode();
                if (CommonConstants.FARMER_CODE.equalsIgnoreCase(savedPlot.getFarmercode())){
//                    UiUtils.showCustomToastMessage("Farmer Code Matches", CropMaintenanceHomeScreen.this, 0);
                    CommonConstants.FARMER_CODE = savedFarmerData.getCode();
                }else {
                    UiUtils.showCustomToastMessage( "Farmer Code IS Not Matches", CropMaintenanceHomeScreen.this, 1);
                    finish();
                }
            }
        }

        setOnClickListeners();

        Log.v(CropMaintenanceHomeScreen.class.getCanonicalName(), "#### check "+isFarmerMandatoryDataEntered());

        calendar = Calendar.getInstance();
        final FiscalDate fiscalDate = new com.cis.palm360.common.FiscalDate(calendar);
        final String financialYear = fiscalDate.getFinancialYearr(calendar);

        currentYearStr = financialYear + "";
        previousYearStr = Integer.parseInt(financialYear) - 1 + "";
        secondpreviousYearStr = Integer.parseInt(financialYear) - 2 + "";

        currentFYStr = financialYear + " - " + (Integer.parseInt(financialYear) + 1);
        previousFYStr = (Integer.parseInt(financialYear) - 1) +  " - " + financialYear;
        secondpreviousFYStr = Integer.parseInt(financialYear) - 2 + "-" + (Integer.parseInt(financialYear) - 1);

    }

    //Setting the onclick listeners
    public void setOnClickListeners() {
        viewSnapShotBtn.setOnClickListener(this);
        updateFarmerBtn.setOnClickListener(this);
        updatePlotBtn.setOnClickListener(this);
        wspBtn.setOnClickListener(this);
        plantationBtn.setOnClickListener(this);
        currentPlantation.setOnClickListener(this);
        fertilizerBtn.setOnClickListener(this);
        Recom_fertilizerBtn.setOnClickListener(this);
        pestBtn.setOnClickListener(this);
        diseaseBtn.setOnClickListener(this);
        ganodermaInfestation.setOnClickListener(this);
        hopBtn.setOnClickListener(this);
        weedManagementBtn.setOnClickListener(this);
        harvestDetailsBtn.setOnClickListener(this);
        complaintsBtn.setOnClickListener(this);
        referralsBtn.setOnClickListener(this);
        finishBtn.setOnClickListener(this);
        interCropDetailsBtn.setOnClickListener(this);
        fertilizer_detailsBtn.setOnClickListener(this);
        bankDetailsBtn.setOnClickListener(this);
        farmerIdBtn.setOnClickListener(this);
        geoTagsBtn.setOnClickListener(this);
        yieldBtn.setOnClickListener(this);
        whiteFlyBtn.setOnClickListener(this);
    }

    //On click listeners for each module in crop maintenance
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.viewSnapShotBtn:
                replaceFragment(new SnapShotFragment());
                break;

            case R.id.personalDetailsBtn:
                PersonalDetailsFragment personalDetailsFragment = new PersonalDetailsFragment();
                personalDetailsFragment.setUpdateUiListener(this);
                replaceFragment(personalDetailsFragment);
                break;

            case R.id.plotDetailsBtn:
                PlotDetailsFragment plotDetailsFragment = new PlotDetailsFragment();
                plotDetailsFragment.setUpdateUiListener(this);
                replaceFragment(plotDetailsFragment);
                break;

            case R.id.wspBtn:
                FragmentManager fm = getSupportFragmentManager();
                WaterSoilTypeDialogFragment mWaterSoilTypeDialogFragment = new WaterSoilTypeDialogFragment();
                mWaterSoilTypeDialogFragment.setOnTypeSelected(this);
                mWaterSoilTypeDialogFragment.show(fm, "fragment_edit_name");
                break;

            case R.id.plantationBtn:
                CropPlantationFragment cropPlantationFragment = new CropPlantationFragment();
                cropPlantationFragment.setUpdateUiListener(this);
                replaceFragment(cropPlantationFragment);
                break;
            case R.id.currentPlantation:
                CurrentPlantationFragment currentPlantationFragment = new CurrentPlantationFragment();
                currentPlantationFragment.setUpdateUiListener(this);
                replaceFragment(currentPlantationFragment);
                break;

            case R.id.interCropDetailsBtn:
                InterCropDetailsFragment interCropDetailsFragment = new InterCropDetailsFragment();
                interCropDetailsFragment.setUpdateUiListener(this);
                replaceFragment(interCropDetailsFragment, null);
                break;
            case R.id.fertilizer_detailsBtn:
                FertilizerFragment mFertilizerFragment = new FertilizerFragment();
                mFertilizerFragment.setUpdateUiListener(this);
                replaceFragment(mFertilizerFragment);
                break;
            case R.id.fertilizerBtn:
                NDScreen ndScreen = new NDScreen();
                ndScreen.setUpdateUiListener(this);
                replaceFragment(ndScreen);
                break;
            case R.id.Recom_fertilizer_detailsBtn:
                RecomndFertilizerFragment recomndFertilizerFragment = new RecomndFertilizerFragment();
                recomndFertilizerFragment.setUpdateUiListener(this);
                replaceFragment(recomndFertilizerFragment);
                break;

            case R.id.pestBtn:
                PestDetailsFragment mPestDetailsFragment = new PestDetailsFragment();
                mPestDetailsFragment.setUpdateUiListener(this);
                replaceFragment(mPestDetailsFragment);
                break;

            case R.id.ganodermaInfestation:
                GanodermaInfestationFragment mGanodermaInfestationFragment = new GanodermaInfestationFragment();
                mGanodermaInfestationFragment.setUpdateUiListener(this);
                replaceFragment(mGanodermaInfestationFragment);
                break;

            case R.id.diseaseBtn:
                DiseaseDetailsFragment mDiseaseDetailsFragment = new DiseaseDetailsFragment();
                mDiseaseDetailsFragment.setUpdateUiListener(this);
                replaceFragment(mDiseaseDetailsFragment);
                break;

            case R.id.weedManagementBtn:
                WMODetailsFragment mWMODetailsFragment = new WMODetailsFragment();
                mWMODetailsFragment.setUpdateUiListener(this);
                replaceFragment(mWMODetailsFragment);
                break;
            case R.id.hopBtn:
                bindImageClass = "HealthOfPlantationDetailsFragment";
                   if (!isNDdata(this)) {
                UiUtils.showCustomToastMessage("Please Add Nutrient Deficiencies Data", CropMaintenanceHomeScreen.this, 0);
            }
                   else if (!ispestdata(this)) {
                UiUtils.showCustomToastMessage("Please Add Pest Data", CropMaintenanceHomeScreen.this, 0);
            }
            else if (!isDISEASEdata(this)) {
                UiUtils.showCustomToastMessage("Please Add Disease Data", CropMaintenanceHomeScreen.this, 0);
            }

//                if (DataManager.getInstance().getDataFromManager(DataManager.NUTRIENT_DETAILS) == null && CommonConstants.CURRENT_TREE>0) {
//                    UiUtils.showCustomToastMessage("Please Add Nutrient Deficiencies Data", CropMaintenanceHomeScreen.this, 0);
//                }
////                else  if (DataManager.getInstance().getDataFromManager(DataManager.PEST_DETAILS) == null && CommonConstants.CURRENT_TREE>0) {
////                    UiUtils.showCustomToastMessage("Please Add Pest Data", CropMaintenanceHomeScreen.this, 0);
////                }
//                else  if (DataManager.getInstance().getDataFromManager(DataManager.DISEASE_DETAILS) == null && CommonConstants.CURRENT_TREE>0) {
//                    UiUtils.showCustomToastMessage("Please Add Disease Data", CropMaintenanceHomeScreen.this, 0);
//                }
                else  if (DataManager.getInstance().getDataFromManager(DataManager.WEEDING_DETAILS) == null && CommonConstants.CURRENT_TREE>0) {
                    UiUtils.showCustomToastMessage("Please Add Weed Management Data", CropMaintenanceHomeScreen.this, 0);
                }
                else  if (DataManager.getInstance().getDataFromManager(DataManager.FERTILIZER) == null && CommonConstants.CURRENT_TREE>0) {
                    UiUtils.showCustomToastMessage("Please Add Fertilizer Data", CropMaintenanceHomeScreen.this, 0);
                }
                else {
                    HealthOfPlantationDetailsFragment mHealthOfPlantationDetailsFragment = new HealthOfPlantationDetailsFragment();
                    mHealthOfPlantationDetailsFragment.setUpdateUiListener(this);
                    replaceFragment(mHealthOfPlantationDetailsFragment);
               }

                break;
            case R.id.harvestDetailsBtn:
                FFB_HarvestDetailsFragment mFFB_HarvestDetailsFragment = new FFB_HarvestDetailsFragment();
                mFFB_HarvestDetailsFragment.setUpdateUiListener(this);
                replaceFragment(mFFB_HarvestDetailsFragment);
                break;
            case R.id.complaintsBtn:
                bindImageClass = "ComplaintDetailsFragment";
//                ComplaintDetailsFragment mComplaintDetailsFragment = new ComplaintDetailsFragment();
//                mComplaintDetailsFragment.setUpdateUiListener(this);
//                replaceFragment(mComplaintDetailsFragment);
                startActivity(new Intent(CropMaintenanceHomeScreen.this, ComplaintsScreenActivity.class).putExtra("plot",true));
                break;
            case R.id.referralsBtn:
                ReferralsFragment mReferralsFragment = new ReferralsFragment();
                mReferralsFragment.setUpdateUiListener(this);
                replaceFragment(mReferralsFragment);
                break;

            case R.id.marketSurveyBtn:
                break;

            case R.id.farmerBankBtn:
                replaceFragment(new BankDetailsFragment());
                break;
            case R.id.farmerIdBtn:
                replaceFragment(new CropMaintanenceIdProofsDetails());
                break;
            case R.id.geoTagsBtn:
                GeoTagFragment geoTagFragment = new GeoTagFragment();
                replaceFragment(geoTagFragment);
                geoTagFragment.setUpdateUiListener(this);
                break;
            case R.id.whiteFlyBtn:
                WhiteFlyFragment witeFlyFragment = new WhiteFlyFragment();
                witeFlyFragment.setUpdateUiListener(this);
                replaceFragment(witeFlyFragment);
                break;
            case R.id.yieldBtn:
                YieldFragment yieldFragment = new YieldFragment();
                yieldFragment.setUpdateUiListener(this);
                replaceFragment(yieldFragment);
                break;
                case R.id.finishBtn:
                     PlotDistrictId = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getPlotDistrictId());
                     secondPreviousFYWhiteFlyList = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getsecondpreviousyearWhiteFlyCount(secondpreviousYearStr));
                     previousFYWhiteFlyList = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getpreviousyearWhiteFlyCount(previousYearStr));
                    Log.e("PLOT_SoilType_DATA",DataManager.getInstance().getDataFromManager(DataManager.SoilType)+"");
                    Log.e("PLOT_SOURCE_OF_WATER_PAIR",DataManager.getInstance().getDataFromManager(DataManager.SOURCE_OF_WATER)+"");
                    Calendar c = Calendar.getInstance();
                    int month = c.get(Calendar.MONTH) ;
                    int year = c.get(Calendar.YEAR);
                    int Quater = month<=2 ? 1 : month<=5 ? 2 : month<=8 ? 3 : 4;

                    if(month>=0 && month<=2){
                        year=year-1;

                        pmindate=String.valueOf(year)+"-09-01";
                        pmaxdate=String.valueOf(year)+"-12-31";
                    }else if (month>=3 && month<=5){

                        pmindate=String.valueOf(year)+"-01-01";
                        pmaxdate=String.valueOf(year)+"-03-31";
                    }else if (month>=6 && month<=8){

                        pmindate=String.valueOf(year)+"-01-01";
                        pmaxdate=String.valueOf(year)+"-03-31";
                    }else if (month>=9 && month<=11){

                        pmindate=String.valueOf(year)+"-01-01";
                        pmaxdate=String.valueOf(year)+"-03-31";
                    }
                    Qtrfertilizer = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getFertilizerQtrCMCnt(year,Quater,CommonConstants.PLOT_CODE,pmindate,pmaxdate));
                    cocaintercrop = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getCocaInterCropCnt());
                Log.v("@@@raring","pest "+CommonConstants.perc_tree_pest+" nutrint "+CommonConstants.perc_tree+" diseases "+CommonConstants.perc_tree_disease);
                Plot enteredPlot = (Plot) DataManager.getInstance().getDataFromManager(DataManager.PLOT_DETAILS);
                    Log.d("GanodermaCheck", "Ganoderma infestation visibility: " + (ganodermaInfestation.getVisibility() == View.VISIBLE));
                    Log.d("GanodermaCheck", "Ganoderma data entered: " + isGandermaDatacheck());
                if (DisplayPlotsFragment.plotCode.equalsIgnoreCase(enteredPlot.getCode())){

                    if (!isFarmerMandatoryDataEntered()) {
                        UiUtils.showCustomToastMessage("Please Enter Mandatory Data For Farmer Income/ Guardian", CropMaintenanceHomeScreen.this, 0);
                    } else if(!issecondpreviousFYWhiteFly() && CommonConstants.CURRENT_TREE>0){
                        UiUtils.showCustomToastMessage("Please Enter White Fly Data For" + secondpreviousFYStr +"Year", CropMaintenanceHomeScreen.this, 0);
                    }
                    else if(!ispreviousFYWhiteFly() && CommonConstants.CURRENT_TREE>0){
                        UiUtils.showCustomToastMessage("Please Enter White Fly Data For" + previousFYStr +" Year", CropMaintenanceHomeScreen.this, 0);
                    }
                    else if(!iscurrentFYWhiteFly() && CommonConstants.CURRENT_TREE>0){
                        UiUtils.showCustomToastMessage("Please Enter White Fly Data For" + currentFYStr +" Year", CropMaintenanceHomeScreen.this, 0);
                    }
                    else if (!ispestdata(this) && CommonConstants.CURRENT_TREE>0) {
                        UiUtils.showCustomToastMessage("Please Add Pest Data", CropMaintenanceHomeScreen.this, 0);
                    }
                    else if (!isDISEASEdata(this) && CommonConstants.CURRENT_TREE>0) {
                        UiUtils.showCustomToastMessage("Please Add Disease Data", CropMaintenanceHomeScreen.this, 0);
                    }
                    else if (!isNDdata(this) && CommonConstants.CURRENT_TREE>0) {
                        UiUtils.showCustomToastMessage("Please Add Nutrient Deficiencies Data", CropMaintenanceHomeScreen.this, 0);
                    }
                    else if (!CommonUiUtils.isConversionPlotAddressDataEntered()) {
                        UiUtils.showCustomToastMessage("Please Enter Mandatory Data For Plot", CropMaintenanceHomeScreen.this, 0);
                    }else if (!CommonUiUtils.isPlotDataEntered()) {
                        UiUtils.showCustomToastMessage("Please Enter OwnerShip And Caretaker Details Of Plot", CropMaintenanceHomeScreen.this, 0);
                    } else  if (!isPlantationData()) {
                        UiUtils.showCustomToastMessage("Please Enter Plantation Data", CropMaintenanceHomeScreen.this, 0);
                    } else if (!iswater()) {
                        UiUtils.showCustomToastMessage("Please Enter Water Details", CropMaintenanceHomeScreen.this, 0);
                    }
                    else if (!issoilpower()) {
                        UiUtils.showCustomToastMessage("Please Enter Soil Details", CropMaintenanceHomeScreen.this, 0);
                    }
                    else  if (!isInterCrop() && CommonConstants.CURRENT_TREE > 0) {
                        UiUtils.showCustomToastMessage("Please Enter Inter Crop Data", CropMaintenanceHomeScreen.this, 0);
                    }


                //    else if (ganodermaInfestation.getVisibility() == View.VISIBLE) {
                    else  if (!isGandermaDatacheck() && ganodermaInfestation.getVisibility() == View.VISIBLE) {
                            UiUtils.showCustomToastMessage(
                                    "Please Enter Ganoderma Infestation Data",
                                    CropMaintenanceHomeScreen.this,
                                    0
                            );
                        }

                    else {
                        ProgressBar.showProgressBar(CropMaintenanceHomeScreen.this,"Please wait data is Updating in DataBase.....");
//                        DataSavingHelper.saveFarmerAddressData(this, new ApplicationThread.OnComplete<String>() {
//                            @Override
//                            public void execute(boolean success, String result, String msg) {
//                                if (success) {
//                                    ProgressBar.hideProgressBar();
//
//                                    UiUtils.showCustomToastMessage("Crop Maintenance Details Data updated successfully", CropMaintenanceHomeScreen.this, 0);
//                                    scrollViewLayout.setVisibility(View.GONE);
//                                   checkView.check();
//                                    new Timer().schedule(new TimerTask(){
//                                        public void run() {
//                                            startActivity(new Intent(CropMaintenanceHomeScreen.this, HomeScreen.class));
//                                            finishAffinity();
//                                            Log.d("MainActivity:", "onCreate: waiting 2 seconds for MainActivity... loading PrimaryActivity.class");
//                                        }
//                                    }, 2000 );
//                                } else {
//                                   // ProgressBar.hideProgressBar();
//                                    Log.v("@@@","data saving failed.....");
//                                    UiUtils.showCustomToastMessage("Data saving failed", CropMaintenanceHomeScreen.this, 0);
//                                }
//                            }
//                        });

                        Boolean dataUpdated = (Boolean) DataManager.getInstance().getDataFromManager(DataManager.IS_PLOTS_DATA_UPDATED);
                        if (dataUpdated == true){
                            DataSavingHelper.saveFarmerAddressData(this, new ApplicationThread.OnComplete<String>() {
                                @Override
                                public void execute(boolean success, String result, String msg) {
                                    if (success) {
                                        ProgressBar.hideProgressBar();

                                        UiUtils.showCustomToastMessage("Crop Maintenance Details Data updated successfully", CropMaintenanceHomeScreen.this, 0);
                                        scrollViewLayout.setVisibility(View.GONE);
                                        checkView.check();
                                        new Timer().schedule(new TimerTask(){
                                            public void run() {
                                                startActivity(new Intent(CropMaintenanceHomeScreen.this, HomeScreen.class));
                                                finishAffinity();
                                                Log.d("MainActivity:", "onCreate: waiting 2 seconds for MainActivity... loading PrimaryActivity.class1");
                                            }
                                        }, 2000 );
                                    } else {
                                        // ProgressBar.hideProgressBar();
                                        Log.v("@@@","data saving failed.....");
                                        UiUtils.showCustomToastMessage("Data saving failed", CropMaintenanceHomeScreen.this, 0);
                                    }
                                }
                            });
                        }else {
                            DataSavingHelper.saveCropMaintenanceHistoryData(this, new ApplicationThread.OnComplete<String>() {
                                @Override
                                public void execute(boolean success, String result, String msg) {
                                    if (success) {
                                        ProgressBar.hideProgressBar();

                                        UiUtils.showCustomToastMessage("Crop Maintenance Details Data updated successfully", CropMaintenanceHomeScreen.this, 0);
                                        scrollViewLayout.setVisibility(View.GONE);
                                        checkView.check();
                                        new Timer().schedule(new TimerTask(){
                                            public void run() {
                                                startActivity(new Intent(CropMaintenanceHomeScreen.this, HomeScreen.class));
                                                finishAffinity();
                                                Log.d("MainActivity:", "onCreate: waiting 2 seconds for MainActivity... loading PrimaryActivity.class2");
                                            }
                                        }, 2000 );
                                    }
                                    else {
                                        // ProgressBar.hideProgressBar();
                                        Log.v("@@@","data saving failed.....");
                                        UiUtils.showCustomToastMessage("Data saving failed", CropMaintenanceHomeScreen.this, 0);
                                    }
                                }
                            });
                        }
                    }
                }
                else {

                    UiUtils.showCustomToastMessage("PLOT CODE IS NOT MATCHED", CropMaintenanceHomeScreen.this, 0);

                }

                break;
        }
    }


    //gets plantation data
    private boolean isPlantationData()
    {

        Plantation msPlantationModel = (Plantation) dataAccessHandler.getPlantationDataset(Queries.getInstance().getPlantatiobData(CommonConstants.PLOT_CODE), 0);
        if (null != msPlantationModel || DataManager.getInstance().getDataFromManager(DataManager.CURRENT_PLANTATION)!= null) {
           return  true;
        }
        return  false;
    }

    //gets fertilizer data
    private boolean isFertilizer() {
//        if(CommonConstants.Prev_Fertilizer_CMD.length()==0){
//            return false;
//        }
//        else
        if ((DataManager.getInstance().getDataFromManager(DataManager.FERTILIZER) != null) && (DataManager.getInstance().getDataFromManager(DataManager.FERTILIZER) != null)) {
            return true;
        }

//        else{
//        if(!Qtrfertilizer.equals("0")) return false;
//            else return true;
//        }
        return true;
    }
    private boolean isInterCrop() {
        // Retrieve the data from the manager
        Object plotInterCropData = DataManager.getInstance().getDataFromManager(DataManager.PLOT_INTER_CROP_DATA);
        Object plotInterCropDataPair = DataManager.getInstance().getDataFromManager(DataManager.PLOT_INTER_CROP_DATA_PAIR);

        Log.e("PLOT_INTER_CROP_DATA", plotInterCropData + " | cocaintercrop.equals('0'): " + cocaintercrop.equals("0"));
        Log.e("PLOT_INTER_CROP_DATA_PAIR", plotInterCropDataPair + " | cocaintercrop.equals('0'): " + cocaintercrop.equals("0"));

        // Check if PLOT_INTER_CROP_DATA is an ArrayList
        if (plotInterCropData instanceof ArrayList) {
            ArrayList<?> dataList = (ArrayList<?>) plotInterCropData;

            // Check if the list is empty or null
            if (dataList != null && !dataList.isEmpty() && plotInterCropDataPair != null && !plotInterCropDataPair.equals("null")) {
                return true;
            } else {
                Log.e("Validation", "PLOT_INTER_CROP_DATA is empty or PLOT_INTER_CROP_DATA_PAIR is null.");
                return false;
            }
        } else {
            Log.e("Validation", "PLOT_INTER_CROP_DATA is not of type ArrayList.");
            return false;
        }
    }


//    //gets Intercrop data
//    private boolean isInterCrop() {
//        // Log data for debugging
//        Log.e("PLOT_INTER_CROP_DATA", DataManager.getInstance().getDataFromManager(DataManager.PLOT_INTER_CROP_DATA).toString() + "" +cocaintercrop.equals("0"));
//        Log.e("PLOT_INTER_CROP_DATA_PAIR", DataManager.getInstance().getDataFromManager(DataManager.PLOT_INTER_CROP_DATA_PAIR) + "" +cocaintercrop.equals("0"));
//
//        // Check if both data entries are non-null
//        if (DataManager.getInstance().getDataFromManager(DataManager.PLOT_INTER_CROP_DATA_PAIR) != null &&
//                DataManager.getInstance().getDataFromManager(DataManager.PLOT_INTER_CROP_DATA) != null) {
//            return true;
//        } else {
//            // Check the value of `cocaintercrop`
//            return false;
//        }
//    }

   private boolean issoilpower()
    {
        Log.e("PLOT_SoilType_DATA",DataManager.getInstance().getDataFromManager(DataManager.SoilType)+"");
        Log.e("PLOT_SOURCE_OF_WATER_PAIR",DataManager.getInstance().getDataFromManager(DataManager.SOURCE_OF_WATER)+"");

        if (DataManager.getInstance().getDataFromManager(DataManager.SoilType) != null) {
            return true;
        }
            else
                return false;


    }

    private boolean iswater()
    {
        Log.e("PLOT_SoilType_DATA",DataManager.getInstance().getDataFromManager(DataManager.SoilType)+"");
        Log.e("PLOT_SOURCE_OF_WATER_PAIR",DataManager.getInstance().getDataFromManager(DataManager.SOURCE_OF_WATER)+"");

        if (
                DataManager.getInstance().getDataFromManager(DataManager.SOURCE_OF_WATER) != null) {
            return true;
        }
        else
            return false;


    }


//what happens when 2018 whitefly is selected
    private boolean issecondpreviousFYWhiteFly() {
            boolean s=true;
        if (secondPreviousFYWhiteFlyList.length() > 0){
            if (Integer.parseInt(secondPreviousFYWhiteFlyList) == 0) {
//                List<WhiteFlyAssessment>  whiteFlyList = (List<WhiteFlyAssessment>) DataManager.getInstance().getDataFromManager(DataManager.WHITE_FLY_18);
//                if ( whiteFlyList.size()>0  ) {
                if ( (DataManager.getInstance().getDataFromManager(DataManager.WHITE_FLY_18) != null)  ) {
                    List<WhiteFlyAssessment> whiteFlyAssessmentList18 = (List<WhiteFlyAssessment>) DataManager.getInstance().getDataFromManager(DataManager.WHITE_FLY_18);
                    if(null != whiteFlyAssessmentList18 && !whiteFlyAssessmentList18.isEmpty())
                    s= true;
                }
                else{
                    if(PlotDistrictId.length()>0){
                        if(PlotDistrictId.equals("1")){
                            s= false;
                        }
                    }else{
                        s= true;
                    }
                }
            }
         }
       return s;

    }

    //what happens when 2019 whitefly is selected

    private boolean ispreviousFYWhiteFly() {
        boolean s=true;
        if (secondPreviousFYWhiteFlyList.length() > 0){
            if (Integer.parseInt(previousFYWhiteFlyList) == 0) {
//                List<WhiteFlyAssessment>  whiteFlyList = (List<WhiteFlyAssessment>) DataManager.getInstance().getDataFromManager(DataManager.WHITE_FLY_19);
//                if ( whiteFlyList.size()>0  ) {
                if ( (DataManager.getInstance().getDataFromManager(DataManager.WHITE_FLY_19) != null)  ) {
                    List<WhiteFlyAssessment> whiteFlyAssessmentList19 = (List<WhiteFlyAssessment>) DataManager.getInstance().getDataFromManager(DataManager.WHITE_FLY_19);
                    if(null != whiteFlyAssessmentList19 && !whiteFlyAssessmentList19.isEmpty())
                    s= true;
                }
                else{
                    if(PlotDistrictId.length()>0){
                        if(PlotDistrictId.equals("1")){
                            s= false;
                        }
                    }else{
                        s= true;
                    }
                }
            }
        }
        return s;

    }

    //When current years whitefly is selected
    private boolean iscurrentFYWhiteFly()
    {
        boolean s=true;
//        List<WhiteFlyAssessment>  whiteFlyList = (List<WhiteFlyAssessment>) DataManager.getInstance().getDataFromManager(DataManager.WHITE_FLY);
//        if ( whiteFlyList.size()>0  ) {

            if ( (DataManager.getInstance().getDataFromManager(DataManager.WHITE_FLY) != null)  ) {
                List<WhiteFlyAssessment> whiteFlyAssessmentList = (List<WhiteFlyAssessment>) DataManager.getInstance().getDataFromManager(DataManager.WHITE_FLY);
                if(null != whiteFlyAssessmentList && !whiteFlyAssessmentList.isEmpty() )
            s= true;
        }
        else{
            if(PlotDistrictId.length()>0){
                if(PlotDistrictId.equals("1")){
                    s= false;
                }
            }else{
                s= true;
            }
        }
        return s;
    }



//on Activity result
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null == data) return;
        if (requestCode == CommonUtils.FROM_CAMERA) {
            onCaptureImageResult(data);
        } else if (requestCode == CommonUtils.FROM_GALLERY) {
        }
    }

    //on Image captured
    private void onCaptureImageResult(Intent data) {
        userImageData = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        if (null == userImageData)
            return;
        userImageData.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        imageData = bytes.toByteArray();

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        fileToUpload = destination;
        FileOutputStream fo;

        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ExifInterface exif = new ExifInterface(fileToUpload.getName());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

            int rotation = 0;
            if (orientation == 6) rotation = 90;
            else if (orientation == 3) rotation = 180;
            else if (orientation == 8) rotation = 270;

            if (rotation != 0) {
                Matrix matrix = new Matrix();
                matrix.postRotate(rotation);
                System.out.println("++++++++++++++++++++++++++++");

                // Rotate Bitmap

                Bitmap rotated = Bitmap.createBitmap(userImageData, 0, 0, userImageData.getWidth(), userImageData.getHeight(), matrix, true);
                userImageData.recycle();
                userImageData = rotated;
                rotated = null;
            }
        } catch (Exception e) {
            Log.e("CropMaintenanceHomeScreen",e.getMessage());
        }


    }

    //on type of module choosed
    @Override
    public void typeChoosed(int selectedType) {
        if (selectedType == 1) {
            replaceFragment(new FertilizerFragment());
        } else {
            replaceFragment(new NDScreen());
        }
    }

    @Override
    public void updateUserInterface(int refreshPosition) {
        uiRefresh();
    }

    //Updates UI based on other modules on Resume
//    private void uiRefresh() {
//        Log.v(CropMaintenanceHomeScreen.class.getName(), "@@@@ isMandatoryDataEnteredForCropMaintenance "+CommonUiUtils.isMandatoryDataEnteredForCropMaintenance());
//
//        finishBtn.setEnabled(CommonUiUtils.isMandatoryDataEnteredForCropMaintenance());
//        finishBtn.setFocusable(CommonUiUtils.isMandatoryDataEnteredForCropMaintenance());
//        finishBtn.setClickable(CommonUiUtils.isMandatoryDataEnteredForCropMaintenance());
//        if (CommonUiUtils.isMandatoryDataEnteredForCropMaintenance()) {
//            finishBtn.setAlpha(1.0f);
//        } else {
//            finishBtn.setAlpha(0.5f);
//        }
//        Log.v(CropMaintenanceHomeScreen.class.getCanonicalName(), "#### check PEST "+isGandermaDataEntered(this));
//        if (isGandermaDataEntered(this)) {
//            ganodermaInfestation.setVisibility(View.VISIBLE); // Make the button visible
//        } else {
//            ganodermaInfestation.setVisibility(View.GONE); // Hide the button
//        }
//        //Log.e("pest Details",DataManager.getInstance().getDataFromManager(DataManager.PEST_DETAILS)+"" );
//        Object plotInterCropData = DataManager.getInstance().getDataFromManager(DataManager.PLOT_INTER_CROP_DATA);
//        Log.e("plotInterCropData",plotInterCropData+"" + CommonConstants.CURRENT_TREE);
//        if (DataManager.getInstance().getDataFromManager(DataManager.FARMER_PERSONAL_DETAILS) != null) {
//            Farmer savedFarmerData = (Farmer) DataManager.getInstance().getDataFromManager(DataManager.FARMER_PERSONAL_DETAILS);
//            CommonConstants.FARMER_CODE = savedFarmerData.getCode();
//            updateFarmerBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
//        }
//        if (DataManager.getInstance().getDataFromManager(DataManager.PLOT_ADDRESS_DETAILS) != null) {
//            updatePlotBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
//        }
//
//        farmerIdBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
//        bankDetailsBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
//
//        Plantation msPlantationModel = (Plantation) dataAccessHandler.getPlantationDataset(Queries.getInstance().getPlantatiobData(CommonConstants.PLOT_CODE), 0);
//        if (DataManager.getInstance().getDataFromManager(DataManager.PLANTATION_CON_DATA)!= null || null != msPlantationModel ) {
//            plantationBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
//        }
//
//        InterCropPlantationXref mCropModel = (InterCropPlantationXref) dataAccessHandler.getInterCropPlantationXrefData(Queries.getInstance().getInterCropPlantationXref(CommonConstants.PLOT_CODE), 0);
//
////        if ((null != mCropModel || DataManager.getInstance().getDataFromManager(DataManager.PLOT_INTER_CROP_DATA) != null)||CommonConstants.CURRENT_TREE==0 || !cocaintercrop.equals("0")) {
////        interCropDetailsBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
////        }
//        if ( isInterCrop() ||CommonConstants.CURRENT_TREE==0 ) {
//            interCropDetailsBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
//        }
//        else
//        {
//            interCropDetailsBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn));
//
//        }
//        if (DataManager.getInstance().getDataFromManager(DataManager.FERTILIZER) != null ||CommonConstants.CURRENT_TREE==0) {
//            fertilizer_detailsBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
//        }
//
////        if (DataManager.getInstance().getDataFromManager(DataManager.FERTILIZER) != null ||CommonConstants.CURRENT_TREE==0) {
////            fertilizer_detailsBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
////        }
//        if (DataManager.getInstance().getDataFromManager(DataManager.RECMND_FERTILIZER) != null ||CommonConstants.CURRENT_TREE==0) {
//            Recom_fertilizerBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
//        }
//        if (ispestdata(this)) {
//            pestBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark)); // Make the button visible
//        }
//        else
//        {
//            pestBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn));
//           // pestBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
//        }
//
//        if (isDISEASEdata(this)) {
//            diseaseBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark)); // Make the button visible
//        }
//        else
//        {
//            diseaseBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn));
//            // pestBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
//        }
//        if (isNDdata(this)) {
//            fertilizerBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark)); // Make the button visible
//        }
//        else
//        {
//            fertilizerBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn));
//            // pestBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
//        }
//
////        if (DataManager.getInstance().getDataFromManager(DataManager.DISEASE_DETAILS) != null ||CommonConstants.CURRENT_TREE==0) {
////            diseaseBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
////        }
////        else
////        {
////            diseaseBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn));
////            // pestBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
////        }
////        if (DataManager.getInstance().getDataFromManager(DataManager.NUTRIENT_DETAILS) != null ||CommonConstants.CURRENT_TREE==0) {
////            fertilizerBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
////        }
//        if (DataManager.getInstance().getDataFromManager(DataManager.GANODERMA_DETAILS) != null ) {
//            ganodermaInfestation.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
//        }
//        if (CommonConstants.CURRENT_TREE==0) {
//            weedManagementBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
//            hopBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
//            yieldBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
//            whiteFlyBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
//        }
//
//        if (DataManager.getInstance().getDataFromManager(DataManager.WEEDING_DETAILS) != null ) {
//            weedManagementBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
//        }
//        if (DataManager.getInstance().getDataFromManager(DataManager.WEEDING_HEALTH_OF_PLANTATION_DETAILS) != null) {
//            hopBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
//        }
//        Harvest mHarvest = (Harvest) dataAccessHandler.getHarvestData(Queries.getInstance().getHarvestBinding(CommonConstants.PLOT_CODE), 0);
//
//        if (null != mHarvest ||(DataManager.getInstance().getDataFromManager(DataManager.FFB_HARVEST_DETAILS) != null) ||CommonConstants.CURRENT_TREE==0) {
//            harvestDetailsBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
//        }
//
//
////        if (CommonUiUtils.isComplaintsDataEntered()) {
////            complaintsBtn.setBackgroundColor(ContextCompat.getColor(this,R.color.green_dark));
////        }
//        if (DataManager.getInstance().getDataFromManager(DataManager.REFERRALS_DATA) != null) {
//            referralsBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
//        }
////        interCropDetailsBtn.setBackgroundColor(ContextCompat.getColor(this,R.color.green_dark));
//        if (null != DataManager.getInstance().getDataFromManager(DataManager.CURRENT_PLANTATION)) {
//            currentPlantation.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
//        }
//        if (null != DataManager.getInstance().getDataFromManager(DataManager.PLOT_GEO_TAG)) {
//            geoTagsBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
//        }
//        if (null != DataManager.getInstance().getDataFromManager(DataManager.YIELD_ASSESSMENT)) {
//            yieldBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
//        }
//
//        if (null != DataManager.getInstance().getDataFromManager(DataManager.WHITE_FLY)
//                || null != DataManager.getInstance().getDataFromManager(DataManager.WHITE_FLY_18)
//                || null != DataManager.getInstance().getDataFromManager(DataManager.WHITE_FLY_19)) {
//            whiteFlyBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
//        }
//
//        boolean sourceResourcerecordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(DatabaseKeys.TABLE_SOILRESOURCE, "PlotCode", CommonConstants.PLOT_CODE));
//        boolean waterResourcerecordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(DatabaseKeys.TABLE_WATERESOURCE, "PlotCode", CommonConstants.PLOT_CODE));
//
////        if (waterResourcerecordExisted || sourceResourcerecordExisted) {
////            wspBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
////        }
//
//        if(null != DataManager.getInstance().getDataFromManager(DataManager.SoilType) && null != DataManager.getInstance().getDataFromManager(DataManager.SOURCE_OF_WATER ))
//        {
//            wspBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
//        }
//    }
    private void uiRefresh() {
        Log.v(CropMaintenanceHomeScreen.class.getName(), "@@@@ isMandatoryDataEnteredForCropMaintenance "
                + CommonUiUtils.isMandatoryDataEnteredForCropMaintenance());

        boolean isMandatoryDataEntered = CommonUiUtils.isMandatoryDataEnteredForCropMaintenance();
        finishBtn.setEnabled(isMandatoryDataEntered);
        finishBtn.setFocusable(isMandatoryDataEntered);
        finishBtn.setClickable(isMandatoryDataEntered);
        finishBtn.setAlpha(isMandatoryDataEntered ? 1.0f : 0.5f);

        Log.v(CropMaintenanceHomeScreen.class.getCanonicalName(), "#### check Disease " + isGandermaDataEntered(this));
        if (isGandermaDataEntered(this)) {
            ganodermaInfestation.setVisibility(View.VISIBLE); // Make the button visible
        } else {
            ganodermaInfestation.setVisibility(View.GONE); // Hide the button
        }

        Object plotInterCropData = DataManager.getInstance().getDataFromManager(DataManager.PLOT_INTER_CROP_DATA);
        Log.e("plotInterCropData", plotInterCropData + "" + CommonConstants.CURRENT_TREE);

        if (DataManager.getInstance().getDataFromManager(DataManager.FARMER_PERSONAL_DETAILS) != null) {
            Farmer savedFarmerData = (Farmer) DataManager.getInstance().getDataFromManager(DataManager.FARMER_PERSONAL_DETAILS);
            CommonConstants.FARMER_CODE = savedFarmerData.getCode();
            updateFarmerBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
        }

        if (DataManager.getInstance().getDataFromManager(DataManager.PLOT_ADDRESS_DETAILS) != null) {
            updatePlotBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
        }

        farmerIdBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
        bankDetailsBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));

        Plantation msPlantationModel = (Plantation) dataAccessHandler.getPlantationDataset(Queries.getInstance().getPlantatiobData(CommonConstants.PLOT_CODE), 0);
        if (DataManager.getInstance().getDataFromManager(DataManager.PLANTATION_CON_DATA) != null || msPlantationModel != null) {
            plantationBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
        }

        InterCropPlantationXref mCropModel = (InterCropPlantationXref) dataAccessHandler.getInterCropPlantationXrefData(Queries.getInstance().getInterCropPlantationXref(CommonConstants.PLOT_CODE), 0);
        if (isInterCrop() || CommonConstants.CURRENT_TREE == 0) {
            interCropDetailsBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
        } else {
            interCropDetailsBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn));
        }

        if (DataManager.getInstance().getDataFromManager(DataManager.FERTILIZER) != null || CommonConstants.CURRENT_TREE == 0) {
            fertilizer_detailsBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
        }

        if (DataManager.getInstance().getDataFromManager(DataManager.RECMND_FERTILIZER) != null || CommonConstants.CURRENT_TREE == 0) {
            Recom_fertilizerBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
        }
//        if (isNDdata(this)) {
//            fertilizerBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark)); // Make the button visible
//        } else {
//            fertilizerBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn));
//        }
//
//        if (ispestdata(this)) {
//            pestBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark)); // Make the button visible
//        } else {
//            pestBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn));
//        }
//
//        if (isDISEASEdata(this)) {
//            diseaseBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark)); // Make the button visible
//        } else {
//            diseaseBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn));
//        }

        if (isNDdata(this)) {
            fertilizerBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark)); // Make the button visible
        } else if (!ispestdata(this) && CommonConstants.CURRENT_TREE == 0){
            fertilizerBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
        }else{
            fertilizerBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn));
        }


        if (ispestdata(this)) {
            pestBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark)); // Make the button visible
        } else if (!ispestdata(this) && CommonConstants.CURRENT_TREE == 0){
            pestBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
        }else{
            pestBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn));
        }

        if (isDISEASEdata(this)) {
            diseaseBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark)); // Make the button visible
        } else if (!ispestdata(this) && CommonConstants.CURRENT_TREE == 0){
            diseaseBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
        }else{
            diseaseBtn.setBackground(getResources().getDrawable(R.drawable.rounded_btn));
        }

        if (DataManager.getInstance().getDataFromManager(DataManager.GANODERMA_DETAILS) != null) {
            ganodermaInfestation.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
        }else {
            ganodermaInfestation.setBackground(getResources().getDrawable(R.drawable.rounded_btn));
        }

        if (CommonConstants.CURRENT_TREE == 0) {
            weedManagementBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
            hopBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
            yieldBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
            whiteFlyBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
        }

        if (DataManager.getInstance().getDataFromManager(DataManager.WEEDING_DETAILS) != null) {
            weedManagementBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
        }

        if (DataManager.getInstance().getDataFromManager(DataManager.WEEDING_HEALTH_OF_PLANTATION_DETAILS) != null) {
            hopBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
        }

        Harvest mHarvest = (Harvest) dataAccessHandler.getHarvestData(Queries.getInstance().getHarvestBinding(CommonConstants.PLOT_CODE), 0);
        if (mHarvest != null || (DataManager.getInstance().getDataFromManager(DataManager.FFB_HARVEST_DETAILS) != null) || CommonConstants.CURRENT_TREE == 0) {
            harvestDetailsBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
        }

        if (DataManager.getInstance().getDataFromManager(DataManager.REFERRALS_DATA) != null) {
            referralsBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
        }

        if (null != DataManager.getInstance().getDataFromManager(DataManager.CURRENT_PLANTATION)) {
            currentPlantation.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
        }

        if (null != DataManager.getInstance().getDataFromManager(DataManager.PLOT_GEO_TAG)) {
            geoTagsBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
        }

        if (null != DataManager.getInstance().getDataFromManager(DataManager.YIELD_ASSESSMENT)) {
            yieldBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
        }

        if (null != DataManager.getInstance().getDataFromManager(DataManager.WHITE_FLY)
                || null != DataManager.getInstance().getDataFromManager(DataManager.WHITE_FLY_18)
                || null != DataManager.getInstance().getDataFromManager(DataManager.WHITE_FLY_19)) {
            whiteFlyBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
        }

        boolean sourceResourcerecordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(DatabaseKeys.TABLE_SOILRESOURCE, "PlotCode", CommonConstants.PLOT_CODE));
        boolean waterResourcerecordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(DatabaseKeys.TABLE_WATERESOURCE, "PlotCode", CommonConstants.PLOT_CODE));

        if (null != DataManager.getInstance().getDataFromManager(DataManager.SoilType)
                && null != DataManager.getInstance().getDataFromManager(DataManager.SOURCE_OF_WATER)) {
            wspBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.green_dark));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiRefresh();
    }

    @Override
    public void onTypeSelected(int type) {
        AreaWaterTypeFragment areaWaterTypeFragment = new AreaWaterTypeFragment();
        areaWaterTypeFragment.setUpdateUiListener(this);
        SoilTypeFragment soilTypeFragment = new SoilTypeFragment();
        soilTypeFragment.setUpdateUiListener(this);
        replaceFragment(type == 1 ? areaWaterTypeFragment : soilTypeFragment);
    }


//on back pressed
    @SuppressLint("SetTextI18n")
    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        int count = fm.getBackStackEntryCount();
        if (count > 0) {
            fm.popBackStack();
        } else {
            if (doubleback) {
                finish();
//                    finishAffinity();
            } else {
                doubleback = true;
                // custom dialog
                final Dialog dialog = new Dialog(CropMaintenanceHomeScreen.this);
                dialog.setContentView(R.layout.custom_alert_dailog);
                Button yesDialogButton = (Button) dialog.findViewById(R.id.Yes);
                Button noDialogButton = (Button) dialog.findViewById(R.id.No);
                TextView msg = (TextView) dialog.findViewById(R.id.test);
                msg.setText(getResources().getString(R.string.Message));
                // if button is clicked, close the custom dialog
                yesDialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataManager.getInstance().deleteData(DataManager.CURRENT_PLANTATION);
                        DataManager.getInstance().deleteData(DataManager.FARMER_ADDRESS_DETAILS);
                        DataManager.getInstance().deleteData(DataManager.PLOT_DETAILS);
                        DataManager.getInstance().deleteData(DataManager.VALIDATE_PLOT_ADDRESS_DETAILS);
                        DataManager.getInstance().deleteData(DataManager.PLOT_ADDRESS_DETAILS);
                        DataManager.getInstance().deleteData(DataManager.PLOT_NEIGHBOURING_PLOTS_DATA_PAIR);
                        DataManager.getInstance().deleteData(DataManager.PLOT_CURRENT_CROPS_DATA_PAIR);
                        DataManager.getInstance().deleteData(DataManager.PLANTATION_CON_DATA);
                        DataManager.getInstance().deleteData(DataManager.PLOT_INTER_CROP_DATA_PAIR);
                        DataManager.getInstance().deleteData(DataManager.PLOT_INTER_CROP_DATA);
                        DataManager.getInstance().deleteData(DataManager.FERTILIZER);
                        DataManager.getInstance().deleteData(DataManager.NUTRIENT_DETAILS);
                        DataManager.getInstance().deleteData(DataManager.RECMND_FERTILIZER);
                        DataManager.getInstance().deleteData(DataManager.PEST_DETAILS);
                        DataManager.getInstance().deleteData(DataManager.CHEMICAL_DETAILS);
                        DataManager.getInstance().deleteData(DataManager.MAIN_PEST_DETAIL);
                        DataManager.getInstance().deleteData(DataManager.DISEASE_DETAILS);
                        DataManager.getInstance().deleteData(DataManager.WEEDING_DETAILS);
                        DataManager.getInstance().deleteData(DataManager.HOP_FILE_REPOSITORY_PLANTATION);
                        DataManager.getInstance().deleteData(DataManager.WEEDING_HEALTH_OF_PLANTATION_DETAILS);
                        DataManager.getInstance().deleteData(DataManager.FFB_HARVEST_DETAILS);
                        DataManager.getInstance().deleteData(DataManager.COMPLAINT_DETAILS);
                        DataManager.getInstance().deleteData(DataManager.COMPLAINT_STATUS_HISTORY);
                        DataManager.getInstance().deleteData(DataManager.COMPLAINT_TYPE);
                        DataManager.getInstance().deleteData(DataManager.COMPLAINT_REPOSITORY);
                        DataManager.getInstance().deleteData(DataManager.REFERRALS_DATA);
                        DataManager.getInstance().deleteData(DataManager.PLOT_GEO_TAG);
                        DataManager.getInstance().deleteData(DataManager.WHITE_FLY_18);
                        DataManager.getInstance().deleteData(DataManager.WHITE_FLY_19);
                        DataManager.getInstance().deleteData(DataManager.WHITE_FLY);
                        DataManager.getInstance().deleteData(DataManager.YIELD_ASSESSMENT);
                        DataManager.getInstance().deleteData(DataManager.SOURCE_OF_WATER);
                        DataManager.getInstance().deleteData(DataManager.SoilType);
                        DataManager.getInstance().deleteData(DataManager.GANODERMA_DETAILS);
                      DataManager.getInstance().deleteData(DataManager.TypeOfIrrigation);
//                        DataManager.getInstance().deleteData(DataManager.PLOT_INTER_CROP_DATA);
                    //    DataManager.getInstance().deleteData(DataManager);
                        CommonConstants.fertilizerapplydate = "";

                        finish();
//                        Toast.makeText(getApplicationContext(),"Dismissed..!!",Toast.LENGTH_SHORT).show();
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

     new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleback = false;
                    }
                }, 2000);
            }
        }
    }
}
