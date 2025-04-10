package com.cis.palm360.datasync.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.cis.palm360.FaLogTracking.LocationTracker;
import com.cis.palm360.cloudhelper.ApplicationThread;
import com.cis.palm360.cloudhelper.CloudDataHandler;
import com.cis.palm360.cloudhelper.Config;
import com.cis.palm360.cloudhelper.HttpClient;
import com.cis.palm360.cloudhelper.Log;
import com.cis.palm360.common.CommonConstants;
import com.cis.palm360.common.CommonUtils;
import com.cis.palm360.dbmodels.AnnualTagetsKRA;
import com.cis.palm360.dbmodels.Collection;
import com.cis.palm360.database.DataAccessHandler;
import com.cis.palm360.database.DatabaseKeys;
import com.cis.palm360.database.Queries;
import com.cis.palm360.datasync.refreshsyncmodel.ImageDetails;
import com.cis.palm360.datasync.ui.RefreshSyncActivity;
import com.cis.palm360.dbmodels.ActivityLog;
import com.cis.palm360.dbmodels.Address;
import com.cis.palm360.dbmodels.AdvancedDetails;
import com.cis.palm360.dbmodels.CollectionPlotXref;
import com.cis.palm360.dbmodels.ComplaintRepository;
import com.cis.palm360.dbmodels.ComplaintRepositoryRefresh;
import com.cis.palm360.dbmodels.ComplaintStatusHistory;
import com.cis.palm360.dbmodels.ComplaintTypeXref;
import com.cis.palm360.dbmodels.Complaints;
import com.cis.palm360.dbmodels.CookingOil;
import com.cis.palm360.dbmodels.CropMaintenanceHistory;
import com.cis.palm360.dbmodels.Disease;
import com.cis.palm360.dbmodels.Farmer;
import com.cis.palm360.dbmodels.FarmerBank;
import com.cis.palm360.dbmodels.FarmerHistory;
import com.cis.palm360.dbmodels.Fertilizer;
import com.cis.palm360.dbmodels.FileRepository;
import com.cis.palm360.dbmodels.FollowUp;
import com.cis.palm360.dbmodels.Ganoderma;
import com.cis.palm360.dbmodels.GeoBoundaries;
import com.cis.palm360.dbmodels.Harvest;
import com.cis.palm360.dbmodels.HarvestorVisitDetails;
import com.cis.palm360.dbmodels.HarvestorVisitHistory;
import com.cis.palm360.dbmodels.Healthplantation;
import com.cis.palm360.dbmodels.IdentityProof;
import com.cis.palm360.dbmodels.IdentityProofFileRepositoryXref;
import com.cis.palm360.dbmodels.InterCropPlantationXref;
import com.cis.palm360.dbmodels.LandlordBank;
import com.cis.palm360.dbmodels.LandlordIdProof;
import com.cis.palm360.dbmodels.MarketSurvey;
import com.cis.palm360.dbmodels.MonthlyTagetsKRA;
import com.cis.palm360.dbmodels.NeighbourPlot;
import com.cis.palm360.dbmodels.NurserySaplingDetails;
import com.cis.palm360.dbmodels.Nutrient;
import com.cis.palm360.dbmodels.Ownershipfilerepository;
import com.cis.palm360.dbmodels.Pest;
import com.cis.palm360.dbmodels.PestChemicalXref;
import com.cis.palm360.dbmodels.Plantation;
import com.cis.palm360.dbmodels.PlantationAuditAnswersModel;
import com.cis.palm360.dbmodels.PlantationFileRepositoryXref;
import com.cis.palm360.dbmodels.Plot;
import com.cis.palm360.dbmodels.PlotCurrentCrop;
import com.cis.palm360.dbmodels.PlotFFBDetails;
import com.cis.palm360.dbmodels.PlotGapFillingDetails;
import com.cis.palm360.dbmodels.PlotGradingDetails;
import com.cis.palm360.dbmodels.PlotIrrigationTypeXref;
import com.cis.palm360.dbmodels.PlotLandlord;
import com.cis.palm360.dbmodels.RecommndFertilizer;
import com.cis.palm360.dbmodels.RecoveryFarmerGroup;
import com.cis.palm360.dbmodels.Referrals;
import com.cis.palm360.dbmodels.SoilResource;
import com.cis.palm360.dbmodels.Uprootment;
import com.cis.palm360.dbmodels.UserSync;
import com.cis.palm360.dbmodels.VisitLog;
import com.cis.palm360.dbmodels.VisitRequests;
import com.cis.palm360.dbmodels.WaterResource;
import com.cis.palm360.dbmodels.Weed;
import com.cis.palm360.dbmodels.DataCountModel;
import com.cis.palm360.dbmodels.WhiteFlyAssessment;
import com.cis.palm360.dbmodels.YieldAssessment;
import com.cis.palm360.uihelper.ProgressBar;
import com.cis.palm360.uihelper.ProgressDialogFragment;
import com.cis.palm360.utils.UiUtils;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.cis.palm360.cloudhelper.HttpClient.getOkHttpClient;
import static com.cis.palm360.common.CommonConstants.selectedPlotCode;

//Sync functionality can be handled from here
@SuppressWarnings("unchecked")
public class DataSyncHelper {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String LOG_TAG = DataSyncHelper.class.getName();

    //public static String PREVIOUS_SYNC_DATE = null;
    public static String PREVIOUS_SYNC_DATE = "previous_sync_date";
    public static String FIRST_TIME = null;
    public static String NULLDATE;
    //public static String PREVIOUS_SYNC_DATE = "";
    public static LinkedHashMap<String, List> dataToUpdate = new LinkedHashMap<>();
    public static int countCheck, transactionsCheck = 0, imagesCount = 0, reverseSyncTransCount = 0, innerCountCheck = 0;
    public static List<String> refreshtableNamesList = new ArrayList<>();
    public static LinkedHashMap<String, List> refreshtransactionsDataMap = new LinkedHashMap<>();
    private static String IMEINUMBER;
    public static int resetCount;
    public static int FarmerDataCount = 0;
    public static int PlotDataCount = 0;
    public static int AdvanceTourPlan = 0;
    public static int FarmerResetCount;
    public static int PlotResetCount;

    public static RecoveryFarmerGroup recdata = new RecoveryFarmerGroup();
    static boolean recrecordExisted = false;

//used to perform Master Sync
    public static synchronized void performMasterSync(final Context context, final boolean firstTimeInsertFinished, final ApplicationThread.OnComplete onComplete) {
        IMEINUMBER = CommonUtils.getIMEInumber(context);
        LinkedHashMap<String, String> syncDataMap = new LinkedHashMap<>();
        syncDataMap.put("LastUpdatedDate", "");
        syncDataMap.put("IMEINumber", IMEINUMBER);
        countCheck = 0;
        final DataAccessHandler dataAccessHandler = new DataAccessHandler(context);
        ProgressBar.showProgressBar(context, "Making data ready for you...");
        CloudDataHandler.getMasterData(Config.live_url + Config.masterSyncUrl, syncDataMap, new ApplicationThread.OnComplete<HashMap<String, List>>() {
            @Override
            public void execute(boolean success, final HashMap<String, List> masterData, String msg) {
                if (success) {
                    if (masterData != null && masterData.size() > 0) {
                        Log.v(LOG_TAG, "@@@ Master sync is success and data size is " + masterData.size());

                        final Set<String> tableNames = masterData.keySet();
                        masterData.remove("CcRate");
                        for (final String tableName : tableNames) {
                            Log.v(LOG_TAG, "@@@ Delete Query " + String.format(Queries.getInstance().deleteTableData(), tableName));
                            ApplicationThread.dbPost("Master Data Sync..", "master data", new Runnable() {
                                @Override
                                public void run() {
                                    countCheck++;
                                    if (!firstTimeInsertFinished) {
                                        dataAccessHandler.deleteRow(tableName, null, null, false, new ApplicationThread.OnComplete<String>() {
                                            @Override
                                            public void execute(boolean success, String result, String msg) {
                                                if (success) {
                                                    dataAccessHandler.insertData(true, tableName, masterData.get(tableName), new ApplicationThread.OnComplete<String>() {
                                                        @Override
                                                        public void execute(boolean success, String result, String msg) {
                                                            if (success) {
                                                                Log.v(LOG_TAG, "@@@ sync success for " + tableName);
                                                            } else {
                                                                Log.v(LOG_TAG, "@@@ check 1 " + masterData.size() + "...pos " + countCheck);
                                                                Log.v(LOG_TAG, "@@@ sync failed for " + tableName + " message " + msg);
                                                            }
                                                            if (countCheck == masterData.size()) {
                                                                Log.v(LOG_TAG, "@@@ Done with master sync " + countCheck);
                                                                ProgressBar.hideProgressBar();
                                                                onComplete.execute(true, null, "Sync is success");
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    Log.v(LOG_TAG, "@@@ Master table deletion failed for " + tableName);
                                                }
                                            }
                                        });
                                    } else {
                                        dataAccessHandler.insertData(tableName, masterData.get(tableName), new ApplicationThread.OnComplete<String>() {
                                            @Override
                                            public void execute(boolean success, String result, String msg) {
                                                if (success) {
                                                    Log.v(LOG_TAG, "@@@ sync success for " + tableName);
                                                    Log.v(LOG_TAG, "@@@ sync success for " + tableName);
                                                } else {
                                                    Log.v(LOG_TAG, "@@@ check 2 " + masterData.size() + "...pos " + countCheck);
                                                    Log.v(LOG_TAG, "@@@ sync failed for " + tableName + " message " + msg);
                                                }
                                                if (countCheck == masterData.size()) {
                                                    Log.v(LOG_TAG, "@@@ Done with master sync " + countCheck);
                                                    ProgressBar.hideProgressBar();
                                                    onComplete.execute(true, null, "Sync is success");
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    } else {
                        ProgressBar.hideProgressBar();
                        Log.v(LOG_TAG, "@@@ Sync is up-to-date");
                        onComplete.execute(true, null, "Sync is up-to-date");
                    }
                } else {
                    ProgressBar.hideProgressBar();
                    onComplete.execute(false, null, "Master sync failed. Please try again");
                }
            }
        });
    }

    //to perform send data to server
    public static synchronized void performRefreshTransactionsSync(final Context context, final ApplicationThread.OnComplete onComplete) {
        countCheck = 0;
        transactionsCheck = 0;
        reverseSyncTransCount = 0;
        imagesCount = 0;
        refreshtableNamesList.clear();
        refreshtransactionsDataMap.clear();
        final DataAccessHandler dataAccessHandler = new DataAccessHandler(context);
        ProgressBar.showProgressBar(context, "Sending data to server...");
        ApplicationThread.bgndPost(LOG_TAG, "getting transactions data", new Runnable() {
            @Override
            public void run() {
                getRefreshSyncTransDataMap(context, new ApplicationThread.OnComplete<LinkedHashMap<String, List>>() {
                    @Override
                    public void execute(boolean success, final LinkedHashMap<String, List> transDataMap, String msg) {
                        if (success) {
                            if (transDataMap != null && transDataMap.size() > 0) {
                                Log.v(LOG_TAG, "transactions data size " + transDataMap.size());
                                Set<String> transDataTableNames = transDataMap.keySet();
                                refreshtableNamesList.addAll(transDataTableNames);
                                refreshtransactionsDataMap = transDataMap;
                                sendTrackingData(context, onComplete);
                                postTransactionsDataToCloud(context, refreshtableNamesList.get(transactionsCheck), dataAccessHandler, onComplete);
                            }
                        } else {
                            ProgressBar.hideProgressBar();
                            Log.v(LOG_TAG, "@@@ Transactions sync failed due to data retrieval error");
                            onComplete.execute(false, null, "Transactions sync failed due to data retrieval error");
                        }
                    }
                });
            }
        });

    }

    //post transaction data to cloud
    public static void postTransactionsDataToCloud(final Context context, final String tableName, final DataAccessHandler dataAccessHandler, final ApplicationThread.OnComplete onComplete) {

        List cctransDataList = refreshtransactionsDataMap.get(tableName);

        if (null != cctransDataList && cctransDataList.size() > 0) {
            Type listType = new TypeToken<List>() {
            }.getType();
            Gson gson = new GsonBuilder().serializeNulls().create();

            String dat = gson.toJson(cctransDataList, listType);
            JSONObject transObj = new JSONObject();
            try {
                transObj.put(tableName, new JSONArray(dat));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.v(LOG_TAG, "@@@@ check.." + transObj.toString());
            CommonConstants.SyncTableName = tableName;
            CloudDataHandler.placeDataInCloud(context, transObj, Config.live_url + Config.transactionSyncURL, new ApplicationThread.OnComplete<String>() {
                @Override
                public void execute(boolean success, String result, String msg) {
                    if (success) {
                        dataAccessHandler.executeRawQuery(String.format(Queries.getInstance().updateServerUpdatedStatus(), tableName));
                        Log.v(LOG_TAG, "@@@ Transactions sync success for " + tableName);
                        transactionsCheck++;
                        if (transactionsCheck == refreshtransactionsDataMap.size()) {
                            Log.v(LOG_TAG, "@@@ Done with transactions sync " + transactionsCheck);
                            onComplete.execute(true, null, "Sync is success");

                        } else {
                            postTransactionsDataToCloud(context, refreshtableNamesList.get(transactionsCheck), dataAccessHandler, onComplete);
                        }
                    } else {
                        ApplicationThread.uiPost(LOG_TAG, "Sync is failed", new Runnable() {
                            @Override
                            public void run() {
                                UiUtils.showCustomToastMessage("Sync failed for " + tableName, context, 1);
                            }
                        });
                        transactionsCheck++;
                        if (transactionsCheck == refreshtransactionsDataMap.size()) {
                            Log.v(LOG_TAG, "@@@ Done with transactions sync " + transactionsCheck);
                            final List<ImageDetails> imagesData = dataAccessHandler.getImageDetails();
                            if (null != imagesData && !imagesData.isEmpty()) {
                                sendImageDetails(context, imagesData, dataAccessHandler, onComplete);
                            } else {
                                ProgressBar.hideProgressBar();
                                onComplete.execute(true, null, "Sync is success");
                            }
                        } else {
                            postTransactionsDataToCloud(context, refreshtableNamesList.get(transactionsCheck), dataAccessHandler, onComplete);
                        }
                        Log.v(LOG_TAG, "@@@ Transactions sync failed for " + tableName);
                        Log.v(LOG_TAG, "@@@ Transactions sync due to " + result);

                    }
                }
            });
        } else {
            transactionsCheck++;
            if (transactionsCheck == refreshtransactionsDataMap.size()) {
                Log.v(LOG_TAG, "@@@ Done with transactions sync " + transactionsCheck);
                final List<ImageDetails> imagesData = dataAccessHandler.getImageDetails();
                if (null != imagesData && !imagesData.isEmpty()) {
                    sendImageDetails(context, imagesData, dataAccessHandler, onComplete);
                } else {
                    ProgressBar.hideProgressBar();
                    onComplete.execute(true, null, "Sync is success");
                    Log.v(LOG_TAG, "@@@ Done with transactions sync " + transactionsCheck);

                }
            } else {
                postTransactionsDataToCloud(context, refreshtableNamesList.get(transactionsCheck), dataAccessHandler, onComplete);
            }
        }
    }

    //to send image details to server
    public static void sendImageDetails(final Context context, final List<ImageDetails> imagesData, final DataAccessHandler dataAccessHandler, final ApplicationThread.OnComplete onComplete) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        String dat = gson.toJson(imagesData.get(imagesCount));
        JSONObject transObj = null;
        try {
            transObj = new JSONObject(dat);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v(LOG_TAG, "@@@@ check.." + transObj.toString());

        CloudDataHandler.placeDataInCloud(context, transObj, Config.live_url + Config.imageUploadURL, new ApplicationThread.OnComplete<String>() {
            @Override
            public void execute(boolean success, String result, String msg) {
                if (success) {
                    dataAccessHandler.executeRawQuery(Queries.getInstance().updatedImageDetailsStatus(imagesData.get(imagesCount).getCollectionCode(), imagesData.get(imagesCount).getFarmerCode(),
                            100));
                    imagesCount++;
                    if (imagesCount == imagesData.size()) {
                        ProgressBar.hideProgressBar();
                        onComplete.execute(true, "", "sync success");
                    } else {
                        sendImageDetails(context, imagesData, dataAccessHandler, onComplete);
                    }
                } else {
                    imagesCount++;
                    if (imagesCount == imagesData.size()) {
                        ProgressBar.hideProgressBar();
                        onComplete.execute(true, "", "sync success");
                        selectedPlotCode.clear();
                    } else {
                        sendImageDetails(context, imagesData, dataAccessHandler, onComplete);
                    }
                    onComplete.execute(false, result, "sync failed due to " + msg);
                }
            }
        });
    }

//used to set data which has to be sent to server
    private static void getRefreshSyncTransDataMap(final Context context, final ApplicationThread.OnComplete onComplete) {

        final DataAccessHandler dataAccessHandler = new DataAccessHandler(context);

        List<Address> addressList = (List<Address>) dataAccessHandler.getSelectedFarmerAddress(Queries.getInstance().getAddressRefresh(), 1);
        List<Farmer> farmersList = (List<Farmer>) dataAccessHandler.getSelectedFarmerData(Queries.getInstance().getSelectedFarmerRefresh(), 1);
        List<Plot> plotList = (List<Plot>) dataAccessHandler.getSelectedPlotData(Queries.getInstance().getPlotRefresh(), 1);
        List<PlotCurrentCrop> plotCurrentCropList = (List<PlotCurrentCrop>) dataAccessHandler.getSelectedPlotCurrentCropData(Queries.getInstance().getPlotCurrentCropRefresh(), 1);
        List<NeighbourPlot> neighbourPlotList = (List<NeighbourPlot>) dataAccessHandler.getSelectedNeighbourPlotData(Queries.getInstance().getNeighbourPlotRefresh(), 1);
        List<WaterResource> waterResourceList = (List<WaterResource>) dataAccessHandler.getWaterResourceData(Queries.getInstance().getWaterResourceRefresh(), 1);
        List<SoilResource> soilResourceList = (List<SoilResource>) dataAccessHandler.getSoilResourceData(Queries.getInstance().getSoilResourceRefresh(), 1);
        List<PlotIrrigationTypeXref> PlotIrrigationTypeXrefList = (List<PlotIrrigationTypeXref>) dataAccessHandler.getPlotIrrigationXRefData(Queries.getInstance().getPlotIrrigationTypeXrefRefresh(), 1);
        List<FollowUp> followUpList = (List<FollowUp>) dataAccessHandler.getFollowupData(Queries.getInstance().getFollowUpRefresh(), 1);
        List<Referrals> referralsList = (List<Referrals>) dataAccessHandler.getReferralsData(Queries.getInstance().getReferralsRefresh(), 1);
        List<MarketSurvey> marketSurveyList = (List<MarketSurvey>) dataAccessHandler.getMarketSurveyData(Queries.getInstance().getMarketSurveyRefresh(), 1);
        List<FarmerHistory> farmerHistoryList = (List<FarmerHistory>) dataAccessHandler.getFarmerHistoryData(Queries.getInstance().getFarmerHistoryRefresh());
        List<IdentityProof> identityProofList = (List<IdentityProof>) dataAccessHandler.getIdProofsData(Queries.getInstance().getIdentityProofRefresh(), 1);
        List<FarmerBank> farmerBankList = (List<FarmerBank>) dataAccessHandler.getFarmerBankData(Queries.getInstance().getFarmerBankRefresh(), 1);
        List<PlotLandlord> plotLandlordList = (List<PlotLandlord>) dataAccessHandler.getPlotLandLordData(Queries.getInstance().getPlotLandlordRefresh(), 1);
        List<LandlordBank> landlordBankList = (List<LandlordBank>) dataAccessHandler.getLandLordBankData(Queries.getInstance().getLandlordBankRefresh(), 1);
        List<LandlordIdProof> landlordIdProofList = (List<LandlordIdProof>) dataAccessHandler.getLandLordIDProofsData(Queries.getInstance().getLandlordIDProofsRefresh(), 1);
        List<CookingOil> cookingOilList = (List<CookingOil>) dataAccessHandler.getCookingOilData(Queries.getInstance().getCookingOilRefresh(), 1);
        List<CropMaintenanceHistory> maintenanceHistoryList = (List<CropMaintenanceHistory>) dataAccessHandler.getCropMaintanceHistoryData(Queries.getInstance().getCropMaintanenanceHistoryRefresh(), 1);

        List<Pest> pestList = (List<Pest>) dataAccessHandler.getPestData(Queries.getInstance().getPestRefresh(), 1);
        List<Ganoderma> ganodermatList = (List<Ganoderma>) dataAccessHandler.getGanodermaData(Queries.getInstance().getGanodermaRefresh(), 1);

        List<Plantation> plantationList = (List<Plantation>) dataAccessHandler.getPlantationData(Queries.getInstance().getPlantationRefresh(), 1);


        List<Disease> diseaseList = (List<Disease>) dataAccessHandler.getDiseaseData(Queries.getInstance().getDiseaseRefresh(), 1);
        List<Fertilizer> fertilizerList = (List<Fertilizer>) dataAccessHandler.getFertilizerData(Queries.getInstance().getFertilizerRefresh(), 1);
        List<Harvest> harvestList = (List<Harvest>) dataAccessHandler.getHarvestData(Queries.getInstance().getHarvestRefresh(), 1);
        List<Healthplantation> healthplantationList = (List<Healthplantation>) dataAccessHandler.getHealthplantationData(Queries.getInstance().getHealthPlantationRefresh(), 1);
        List<InterCropPlantationXref> interCropPlantationXrefList = (List<InterCropPlantationXref>) dataAccessHandler.getInterCropPlantationXrefData(Queries.getInstance().getInterCropPlantationXrefRefresh(), 1);
        List<Nutrient> nutrientList = (List<Nutrient>) dataAccessHandler.getNutrientData(Queries.getInstance().getNutrientRefresh(), 1);
        List<RecommndFertilizer> recomFertilizer = (List<RecommndFertilizer>) dataAccessHandler.getRecomFertlizerData(Queries.getInstance().getRecomFertilizerRefresh(), 1);
        List<Ownershipfilerepository> ownershipfilerepositoryList = (List<Ownershipfilerepository>) dataAccessHandler.getOwnershipfilerepositoryData(Queries.getInstance().getOwnerShipFileRepositoryRefresh(), 1);
        List<Uprootment> uprootmentList = (List<Uprootment>) dataAccessHandler.getUprootmentData(Queries.getInstance().getUprootmentRefresh(), 1);
        List<Weed> weedtList = (List<Weed>) dataAccessHandler.getWeedData(Queries.getInstance().getWeedRefresh(), 1);
        List<YieldAssessment> YieldList = (List<YieldAssessment>) dataAccessHandler.getYieldData(Queries.getInstance().getYieldRefresh(), 1);
        List<WhiteFlyAssessment> WhiteList = (List<WhiteFlyAssessment>) dataAccessHandler.getWhiteData(Queries.getInstance().getWhiteRefresh(), 1);
        List<IdentityProofFileRepositoryXref> identityProofFileRepositoryXreftList = (List<IdentityProofFileRepositoryXref>) dataAccessHandler.getIdentityProofFileRepositoryXrefData(Queries.getInstance().getIdentityProofFileRepositoryXrefRefresh(), 1);
        List<PestChemicalXref> pestChemicalXrefList = (List<PestChemicalXref>) dataAccessHandler.getPestChemicalXrefData(Queries.getInstance().getPestChemicalXrefRefresh(), 1);
        List<PlantationFileRepositoryXref> plantationFileRepositoryXrefList = (List<PlantationFileRepositoryXref>) dataAccessHandler.getPlantationFileRepositoryXrefData(Queries.getInstance().getPlantationFileRepositoryXrefRefresh(), 1);
        List<ActivityLog> activityLogList = dataAccessHandler.getActivityLogData();
        List<GeoBoundaries> geoBoundariesList = (List<GeoBoundaries>) dataAccessHandler.getGeoTagData(Queries.getInstance().getGeoBoundariesRefresh(), 1);
        List<Complaints> complaintsList = (List<Complaints>) dataAccessHandler.getComplaints(Queries.getInstance().getComplaintData(), 1);
        List<ComplaintStatusHistory> complaintStatusHistoryList = (List<ComplaintStatusHistory>) dataAccessHandler.getComplaintStatusHistory(Queries.getInstance().getComplaintStatusHistory(), 1);
        List<ComplaintRepositoryRefresh> complaintRepositoryList = (List<ComplaintRepositoryRefresh>) dataAccessHandler.getComplaintRefreshRepository(Queries.getInstance().getComplaintRepository(), 1);
        List<ComplaintTypeXref> complaintTypeXrefList = (List<ComplaintTypeXref>) dataAccessHandler.getComplaintTypeXref(Queries.getInstance().getComplaintTypeXref(), 1);
        List<FileRepository> fileRepoList = (List<FileRepository>) dataAccessHandler.getFileRepositoryData(Queries.getInstance().getFileRepositoryRefresh(), 1);
        List<VisitLog> visitLogList = (List<VisitLog>) dataAccessHandler.getVisitLogData(Queries.getInstance().getVistLogs());
        List<UserSync> userSyncList = (List<UserSync>) dataAccessHandler.getUserSyncData(Queries.getInstance().getUserSyncDetails());
      //  List<Alerts> alertsList = (List<Alerts>) dataAccessHandler.getAlertsDetails(Queries.getInstance().getAlertsDetailsQueryToSendCloud(), 1, true);
        List<HarvestorVisitHistory> harvestorVisithistoryList = (List<HarvestorVisitHistory>) dataAccessHandler.getSelectedHarvestorHistoryData(Queries.getInstance().getSelectedHarvestorHistoryRefresh(), 1);
        List<HarvestorVisitDetails> harvestorVisitList = (List<HarvestorVisitDetails>) dataAccessHandler.getSelectedHarvestorData(Queries.getInstance().getSelectedHarvestorRefresh(), 1);
        List<RecoveryFarmerGroup> recoveryFarmerGroupList = (List<RecoveryFarmerGroup>) dataAccessHandler.getSelectedRecoveryFarmerData(Queries.getInstance().getSelectedRecoveryFarmersRefresh(), 1);
        List<PlantationAuditAnswersModel> plantationAuditAnswerList = (List<PlantationAuditAnswersModel>) dataAccessHandler.getPlantationAuditAnswerData(Queries.getInstance().getPlantationAuditAnswersRefresh(), 1);

        List<PlotGapFillingDetails> PlotgapfillingList = (List<PlotGapFillingDetails>) dataAccessHandler.getPlotGapFillingDetails(Queries.getInstance().getplotgapfillingrefresh(), 1);


        LinkedHashMap<String, List> allRefreshDataMap = new LinkedHashMap<>();
        allRefreshDataMap.put(DatabaseKeys.TABLE_ADDRESS, addressList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_FARMER, farmersList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_PLOT, plotList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_PLOTCURRENTCROP, plotCurrentCropList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_NEIGHBOURPLOT, neighbourPlotList);
//        allRefreshDataMap.put(DatabaseKeys.TABLE_WATERESOURCE, waterResourceList);
//        allRefreshDataMap.put(DatabaseKeys.TABLE_PLOTIRRIGATIONTYPEXREF, PlotIrrigationTypeXrefList);
//        allRefreshDataMap.put(DatabaseKeys.TABLE_SOILRESOURCE, soilResourceList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_FOLLOWUP, followUpList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_REFERRALS, referralsList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_MARKETSURVEY, marketSurveyList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_FARMERHISTORY, farmerHistoryList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_IDENTITYPROOF, identityProofList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_FARMERBANK, farmerBankList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_PLANTATION, plantationList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_PLOTLANDLORD, plotLandlordList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_LANDLORDBANK, landlordBankList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_LANDLORDIDPROOFS, landlordIdProofList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_COOKINGOIL, cookingOilList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_CROPMAINTENANCEHISTORY, maintenanceHistoryList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_DISEASE, diseaseList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_FERTLIZER, fertilizerList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_HARVEST, harvestList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_HEALTHPLANTATION, healthplantationList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_INTERCROPPLANTATIONXREF, interCropPlantationXrefList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_WATERESOURCE, waterResourceList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_PLOTIRRIGATIONTYPEXREF, PlotIrrigationTypeXrefList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_SOILRESOURCE, soilResourceList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_NUTRIENT, nutrientList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_RECOMMND_FERTLIZER, recomFertilizer);
        allRefreshDataMap.put(DatabaseKeys.TABLE_OWNERSHIPFILEREPO, ownershipfilerepositoryList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_PEST, pestList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_Ganoderma, ganodermatList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_UPROOTMENT, uprootmentList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_WEED, weedtList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_YIELDASSESMENT, YieldList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_WHITE, WhiteList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_IDPROOFFILEREPOXREF, identityProofFileRepositoryXreftList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_PESTCHEMICALXREF, pestChemicalXrefList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_PLANTATIONFILEREPOXREF, plantationFileRepositoryXrefList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_ACTIVITYLOG, activityLogList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_FILEREPOSITORY, fileRepoList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_GEOBOUNDARIES, geoBoundariesList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_COMPLAINT, complaintsList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_COMPLAINTREPOSITORY, complaintRepositoryList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_COMPLAINTTYPEXREF, complaintTypeXrefList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_COMPLAINTSTATUSHISTORY, complaintStatusHistoryList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_VisitLog, visitLogList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_UserSync, userSyncList);
        //allRefreshDataMap.put(DatabaseKeys.TABLE_ALERTS, alertsList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_HarvestorVisitHistory, harvestorVisithistoryList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_HarvestorVisitDetails, harvestorVisitList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_Recovery_Farmer_Group, recoveryFarmerGroupList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_Plantation_Audit_Answers, plantationAuditAnswerList);
        allRefreshDataMap.put(DatabaseKeys.TABLE_PlotGapFillingDetails, PlotgapfillingList);


//        allRefreshDataMap.put(DatabaseKeys.TABLE_Location_TRACKING_DETAILS, gpsTrackingList);


        onComplete.execute(true, allRefreshDataMap, "here is collection of table transactions data");

    }

    //to start get data from server
    public static void  startTransactionSync(final Context context, final ProgressDialogFragment progressDialogFragment) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("", MODE_PRIVATE);
        String date = sharedPreferences.getString(PREVIOUS_SYNC_DATE, null);

     final String finalDate = date;//
      //  final String finalDate = "2021-08-04 18:34:46";//
        Log.v(LOG_TAG, "@@@ Date " + date);
        progressDialogFragment.updateText("Getting total records count");
        final ProgressDialogFragment finalProgressDialogFragment = progressDialogFragment;
        getCountOfHits(date, new ApplicationThread.OnComplete() {
            @Override
            public void execute(boolean success, Object result, String msg) {
                if (success) {
                    Log.v(LOG_TAG, "@@@@ count here " + result.toString());
                    List<DataCountModel> dataCountModelList = (List<DataCountModel>) result;
                    prepareIndexes(date, dataCountModelList, context, finalProgressDialogFragment);
                } else {
                    if (null != finalProgressDialogFragment) {
                        finalProgressDialogFragment.dismiss();
                    }
                    Log.v(LOG_TAG, "Transaction sync failed due to data issue-->" + msg);
                    UiUtils.showCustomToastMessage("Transaction sync failed due to data issue", context, 1);
                }
            }
        });
    }

//preparing indexes for getting data from server
    public static void prepareIndexes(final String date, List<DataCountModel> countData, final Context context, ProgressDialogFragment progressDialogFragment) {
        if (!countData.isEmpty()) {
            reverseSyncTransCount = 0;
            transactionsCheck = 0;
            dataToUpdate.clear();
            final DataAccessHandler dataAccessHandler = new DataAccessHandler(context);
            new DownLoadData(context, date, countData, 0, 0, dataAccessHandler, progressDialogFragment).execute();
        } else {
            ProgressBar.hideProgressBar();
            if (null != progressDialogFragment) {
                progressDialogFragment.dismiss();
            }

            CommonUtils.showMyToast("There is no transactions data to sync", context);
        }
    }

    //to make number of hits of an api and request object
    public static void getCountOfHits(String date, final ApplicationThread.OnComplete onComplete) {
        String countUrl = "";
        LinkedHashMap<String, String> syncDataMap = new LinkedHashMap<>();
        syncDataMap.put("Date", TextUtils.isEmpty(date) ? "null" : date);
 //syncDataMap.put("Date","2025-02-12 12:48:04");
        syncDataMap.put("UserId", CommonConstants.USER_ID);
        syncDataMap.put("IsUserDataAccess", CommonConstants.migrationSync);
        countUrl = Config.live_url + Config.getTransCount;
        CloudDataHandler.getGenericData(countUrl, syncDataMap, new ApplicationThread.OnComplete<List<DataCountModel>>() {
            @Override
            public void execute(boolean success, List<DataCountModel> result, String msg) {
                onComplete.execute(success, result, msg);
            }
        });
    }


    //update sync date
    public static void updateSyncDate(Context context, String date) {
        Log.v(LOG_TAG, "@@@ saving date into");
//        //SharedPreferences sharedPreferences = context.getSharedPreferences("appprefs", MODE_PRIVATE);
        SharedPreferences sharedPreferences = context.getSharedPreferences("", MODE_PRIVATE);
        sharedPreferences.edit().putString(PREVIOUS_SYNC_DATE, date).apply();
        Log.v(LOG_TAG, "@@@ saving date into " + PREVIOUS_SYNC_DATE + " " + date);
        //PrefUtil.putString(context, PREVIOUS_SYNC_DATE, CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));

    }

    //to check whether we can proceed for transaction sync
    public static void ableToProceedToTransactionSync(final String password, final ApplicationThread.OnComplete onComplete) {
        CloudDataHandler.getGenericData(Config.live_url + String.format(Config.validateTranSync, password), new ApplicationThread.OnComplete<String>() {
            @Override
            public void execute(boolean success, String result, String msg) {
                onComplete.execute(success, result, msg);
            }
        });
    }

    //update or insert data
//    private static void updateOrInsertData(final String tableName, List dataToInsert, String whereCondition, boolean recordExisted, DataAccessHandler dataAccessHandler, final ApplicationThread.OnComplete onComplete) {
//        if (recordExisted) {
//            dataAccessHandler.updateData(tableName, dataToInsert, true, whereCondition, new ApplicationThread.OnComplete<String>() {
//                @Override
//                public void execute(boolean success, String result, String msg) {
//                    onComplete.execute(success, null, "Sync is " + success + " for " + tableName);
//                }
//            });
//        } else {
//            dataAccessHandler.insertData(tableName, dataToInsert, new ApplicationThread.OnComplete<String>() {
//                @Override
//                public void execute(boolean success, String result, String msg) {
//                    onComplete.execute(true, null, "Sync is " + success + " for " + tableName);
//                }
//            });
//        }
//    }

    private static void updateOrInsertData(final String tableName, List dataToInsert, String whereCondition, boolean recordExisted, DataAccessHandler dataAccessHandler, final ApplicationThread.OnComplete onComplete) {

        if(recordExisted && tableName != "RecoveryFarmerGroup"){
            dataAccessHandler.updateData(tableName, dataToInsert, true, whereCondition, new ApplicationThread.OnComplete<String>() {
                @Override
                public void execute(boolean success, String result, String msg) {
                    onComplete.execute(success, null, "Sync is " + success + " for " + tableName);
                }
            });
        }
        else if(tableName == "RecoveryFarmerGroup"){
            if (recrecordExisted){
                dataAccessHandler.executeRawQuery("Delete from RecoveryFarmerGroup where FarmerCode = '" + recdata.getFarmerCode() + "' AND RecoveryFarmerCode = '" + recdata.getRecoveryFarmercode() + "', AND IsActive = '" + recdata.getIsActive() + "'");
                //dataAccessHandler.executeRawQuery("Delete from RecoveryFarmerGroup where FarmerCode = '" + recdata.getFarmerCode() + "' AND RecoveryFarmerCode = '" + recdata.getRecoveryFarmercode() + "'");
                dataAccessHandler.insertData(tableName, dataToInsert, new ApplicationThread.OnComplete<String>() {
                    @Override
                    public void execute(boolean success, String result, String msg) {
                        onComplete.execute(true, null, "Sync is " + success + " for " + tableName);
                    }
                });
            }else{
                dataAccessHandler.insertData(tableName, dataToInsert, new ApplicationThread.OnComplete<String>() {
                    @Override
                    public void execute(boolean success, String result, String msg) {
                        onComplete.execute(true, null, "Sync is " + success + " for " + tableName);
                    }
                });
            }
        }
        else {
            dataAccessHandler.insertData(tableName, dataToInsert, new ApplicationThread.OnComplete<String>() {
                @Override
                public void execute(boolean success, String result, String msg) {
                    onComplete.execute(true, null, "Sync is " + success + " for " + tableName);
                }
            });
        }
    }

    //update data into database
    private static synchronized void updateDataIntoDataBase(final LinkedHashMap<String, List> transactionsData, final DataAccessHandler dataAccessHandler, final String tableName, final ApplicationThread.OnComplete onComplete) {
        final List dataList = transactionsData.get(tableName);
        Log.e("dataList=======>",dataList+"");
        List dataToInsert = new ArrayList();
        JSONObject ccData = null;
        Gson gson = new GsonBuilder().serializeNulls().create();

        boolean recordExisted = false;
        String whereCondition = null;

        if (dataList.size() > 0) {
            if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_FILEREPOSITORY)) {
                FileRepository fileRepository = (FileRepository) dataList.get(innerCountCheck);
                whereCondition = " where  FarmerCode = '" + fileRepository.getFarmercode() + "'";
                recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(tableName, "FarmerCode", fileRepository.getFarmercode()));
            } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_ADDRESS)) {
                Address addressData = (Address) dataList.get(innerCountCheck);
                addressData.setServerupdatedstatus(1);
                whereCondition = " where  Code = '" + addressData.getCode() + "'";
                try {
                    ccData = new JSONObject(gson.toJson(addressData));
                    dataToInsert.add(CommonUtils.toMap(ccData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
                recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(tableName, "Code", addressData.getCode()));
            } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_FARMER)) {
                Farmer farmerData = (Farmer) dataList.get(innerCountCheck);
                farmerData.setServerupdatedstatus(1);
                whereCondition = " where  Code = '" + farmerData.getCode() + "'";
                try {
                    ccData = new JSONObject(gson.toJson(farmerData));
                    dataToInsert.add(CommonUtils.toMap(ccData));
                    recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(tableName, "Code", farmerData.getCode()));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
            } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_PLOT)) {
                Plot plotData = (Plot) dataList.get(innerCountCheck);
                plotData.setServerupdatedstatus(1);
                whereCondition = " where  Code= '" + plotData.getCode() + "'";
                try {
                    ccData = new JSONObject(gson.toJson(plotData));
                    dataToInsert.add(CommonUtils.toMap(ccData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
                recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(tableName, "Code", plotData.getCode()));
            } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_FARMERHISTORY)) {
                FarmerHistory farmerHistoryData = (FarmerHistory) dataList.get(innerCountCheck);
                farmerHistoryData.setServerUpdatedStatus(1);
                whereCondition = " where  PlotCode = '" + farmerHistoryData.getPlotcode() + "' and StatusTypeId = '" + farmerHistoryData.getStatustypeid() + "'";
                try {
                    ccData = new JSONObject(gson.toJson(farmerHistoryData));
                    dataToInsert.add(CommonUtils.toMap(ccData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
                recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInFarmerHistoryTable(tableName, "PlotCode", farmerHistoryData.getPlotcode(), String.valueOf(farmerHistoryData.getStatustypeid())));
            } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_PLANTATION)) {
                Plantation plantation = (Plantation) dataList.get(innerCountCheck);
                plantation.setServerUpdatedStatus(1);
                whereCondition = " where  PlotCode = '" + plantation.getPlotcode() + "' and " +
                        "  CreatedByUserId = " + plantation.getCreatedbyuserid() + " and " +
                        "  SaplingSourceId = " + plantation.getSaplingsourceid() + " and " +
                        "  SaplingVendorId = " + plantation.getSaplingvendorid() + " and " +
                        "  CropVarietyId = " + plantation.getCropVarietyId() + " and " +
                        "  GFReceiptNumber = '" + plantation.getGFReceiptNumber() + "' and" +
                        " datetime(CreatedDate) = datetime('" + plantation.getCreateddate() + "')";
                try {
                    ccData = new JSONObject(gson.toJson(plantation));
                    dataToInsert.add(CommonUtils.toMap(ccData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
                recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkPlantationRecordStatusInTable(tableName, plantation.getPlotcode(), plantation.getCreatedbyuserid(),plantation.getSaplingsourceid(),plantation.getSaplingvendorid(),plantation.getCropVarietyId(), plantation.getCreateddate(), plantation.getGFReceiptNumber()));
            } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_UPROOTMENT)) {
                Uprootment uprootment = (Uprootment) dataList.get(innerCountCheck);
                uprootment.setServerupdatedstatus(1);
                whereCondition = " where  PlotCode = '" + uprootment.getPlotcode() + "'";
                try {
                    ccData = new JSONObject(gson.toJson(uprootment));
                    dataToInsert.add(CommonUtils.toMap(ccData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
                recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(tableName, "PlotCode", uprootment.getPlotcode()));
            } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_PEST)) {
                Pest pest = (Pest) dataList.get(innerCountCheck);
                pest.setServerUpdatedStatus(1);
                whereCondition = " where  PlotCode = '" + pest.getPlotCode() + "'";
                try {
                    ccData = new JSONObject(gson.toJson(pest));
                    dataToInsert.add(CommonUtils.toMap(ccData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
                recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(tableName, "PlotCode", pest.getPlotCode()));
            } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_PESTCHEMICALXREF)) {
                PestChemicalXref pestChemicalXref = (PestChemicalXref) dataList.get(innerCountCheck);
                pestChemicalXref.setServerUpdatedStatus(1);
                whereCondition = " where  PestCode = '" + pestChemicalXref.getPestCode() + "'";
                try {
                    ccData = new JSONObject(gson.toJson(pestChemicalXref));
                    dataToInsert.add(CommonUtils.toMap(ccData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
                recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(tableName, "PestCode", pestChemicalXref.getPestCode()));
            } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_NUTRIENT)) {
                Nutrient nutrient = (Nutrient) dataList.get(innerCountCheck);
                nutrient.setServerUpdatedStatus(1);
                whereCondition = " where  PlotCode = '" + nutrient.getPlotcode() + "'";
                try {
                    ccData = new JSONObject(gson.toJson(nutrient));
                    dataToInsert.add(CommonUtils.toMap(ccData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
                recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(tableName, "PlotCode", nutrient.getPlotcode()));
            } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_WEED)) {
                Weed weed = (Weed) dataList.get(innerCountCheck);
                weed.setServerupdatedstatus(1);
                whereCondition = " where  PlotCode = '" + weed.getPlotCode() + "'";
                try {
                    ccData = new JSONObject(gson.toJson(weed));
                    dataToInsert.add(CommonUtils.toMap(ccData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
                recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(tableName, "PlotCode", weed.getPlotCode()));
            } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_COLLECTION)) {
                Collection data = (Collection) dataList.get(innerCountCheck);
                data.setServerUpdatedStatus(1);
                whereCondition = " where Code = '" + data.getCode() + "'";
                try {
                    ccData = new JSONObject(gson.toJson(data));
                    dataToInsert.add(CommonUtils.toMap(ccData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
                recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(tableName, "Code", data.getCode()));
            } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_COLLECTIONPLOTXREF)) {
                CollectionPlotXref data = (CollectionPlotXref) dataList.get(innerCountCheck);
                data.setServerUpdatedStatus(1);
                whereCondition = " where CollectionCode = '" + data.getCollectionCode() + "'";
                try {
                    ccData = new JSONObject(gson.toJson(data));
                    dataToInsert.add(CommonUtils.toMap(ccData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
                recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(tableName, "CollectionCode", data.getCollectionCode()));
            } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_HEALTHOFPLANTATIONDETAILS)) {
                Healthplantation data = (Healthplantation) dataList.get(innerCountCheck);
                data.setServerUpdatedStatus(1);
                whereCondition = " where PlotCode = '" + data.getPlotCode() + "'";
                try {
                    ccData = new JSONObject(gson.toJson(data));
                    dataToInsert.add(CommonUtils.toMap(ccData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
                recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(tableName, "PlotCode", data.getPlotCode()));
            }
            else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_GEOBOUNDARIES)) {
                GeoBoundaries data = (GeoBoundaries) dataList.get(innerCountCheck);
                data.setServerupdatedstatus(1);
                whereCondition = " where PlotCode = '" + data.getPlotcode() + "'";
                try {
                    ccData = new JSONObject(gson.toJson(data));
                    dataToInsert.add(CommonUtils.toMap(ccData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
                recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(tableName, "PlotCode", data.getPlotcode()));
            }
            else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_FARMERBANK)) {
                FarmerBank data = (FarmerBank) dataList.get(innerCountCheck);
                data.setServerUpdatedStatus(1);
                whereCondition = " where FarmerCode = '" + data.getFarmercode() + "'";
                try {
                    ccData = new JSONObject(gson.toJson(data));
                    dataToInsert.add(CommonUtils.toMap(ccData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
                recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(tableName, "FarmerCode", data.getFarmercode()));
            }
            else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_IDENTITYPROOF)) {
                IdentityProof data = (IdentityProof) dataList.get(innerCountCheck);
                data.setServerUpdatedStatus(1);
               // whereCondition = " where FarmerCode = '" + data.getFarmercode() + "'";
                whereCondition = " where  FarmerCode = '" + data.getFarmercode()  + "' and IDProofTypeId = '" + data.getIdprooftypeid() + "'";
                try {
                    ccData = new JSONObject(gson.toJson(data));
                    dataToInsert.add(CommonUtils.toMap(ccData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
                //recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(tableName, "FarmerCode", data.getFarmercode()));
                recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInIdentityProofTable(tableName, "FarmerCode", data.getFarmercode(),String.valueOf(data.getIdprooftypeid())));
            } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_PLOTLANDLORD)) {
                PlotLandlord data = (PlotLandlord) dataList.get(innerCountCheck);
                data.setServerupdatedstatus(1);
                whereCondition = " where PlotCode = '" + data.getPlotcode() + "'";
                try {
                    ccData = new JSONObject(gson.toJson(data));
                    dataToInsert.add(CommonUtils.toMap(ccData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
                recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(tableName, "PlotCode", data.getPlotcode()));
            } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_LANDLORDBANK)) {
                LandlordBank data = (LandlordBank) dataList.get(innerCountCheck);
                data.setServerUpdatedStatus(1);
                whereCondition = " where PlotCode = '" + data.getPlotcode() + "'";
                try {
                    ccData = new JSONObject(gson.toJson(data));
                    dataToInsert.add(CommonUtils.toMap(ccData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
                recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(tableName, "PlotCode", data.getPlotcode()));
            } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_LANDLORDIDPROOFS)) {
                LandlordIdProof data = (LandlordIdProof) dataList.get(innerCountCheck);
                data.setServerUpdatedStatus(1);
                whereCondition = " where PlotCode = '" + data.getPlotCode() + "'";
                try {
                    ccData = new JSONObject(gson.toJson(data));
                    dataToInsert.add(CommonUtils.toMap(ccData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
                recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(tableName, "PlotCode", data.getPlotCode()));
            } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_CROPMAINTENANCEHISTORY)) {
                CropMaintenanceHistory data = (CropMaintenanceHistory) dataList.get(innerCountCheck);
                data.setServerUpdatedStatus(1);
                whereCondition = " where Code = '" + data.getCode() + "'";
                try {
                    ccData = new JSONObject(gson.toJson(data));
                    dataToInsert.add(CommonUtils.toMap(ccData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
                recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(tableName, "Code", data.getCode()));
            } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_COMPLAINT)) {
                Complaints data = (Complaints) dataList.get(innerCountCheck);
                data.setServerUpdatedStatus(1);
                whereCondition = " where Code = '" + data.getCode() + "'";
                try {
                    ccData = new JSONObject(gson.toJson(data));
                    dataToInsert.add(CommonUtils.toMap(ccData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
                recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(tableName, "Code", data.getCode()));

            } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_COMPLAINTSTATUSHISTORY)) {
                ComplaintStatusHistory data = (ComplaintStatusHistory) dataList.get(innerCountCheck);
                data.setServerUpdatedStatus(1);
                whereCondition = " where ComplaintCode = '" + data.getComplaintCode() + "'";
                try {
                    ccData = new JSONObject(gson.toJson(data));
                    dataToInsert.add(CommonUtils.toMap(ccData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
                recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(tableName, "ComplaintCode", "StatusTypeId", "IsActive", data.getComplaintCode(), data.getStatusTypeId(), data.getIsActive()));
            } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_COMPLAINTREPOSITORY)) {
                ComplaintRepository data = (ComplaintRepository) dataList.get(innerCountCheck);
                data.setServerUpdatedStatus(1);
                whereCondition = " where ComplaintCode = '" + data.getComplaintCode() + "'" + " and FileExtension = " + "'" + data.getFileExtension() + "'";
                try {
                    ccData = new JSONObject(gson.toJson(data));
                    dataToInsert.add(CommonUtils.toMap(ccData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
                recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(tableName, "ComplaintCode", "FileExtension", "FileName", data.getComplaintCode(), data.getFileExtension(), data.getFileName()));
                if (recordExisted && data.getFileExtension().equalsIgnoreCase(".mp3")) {
                    CommonUtils.checkAndDeleteFile(CommonUtils.getAudioFilePath(data.getComplaintCode() + ".mp3"));
                }
            } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_COMPLAINTTYPEXREF)) {
                ComplaintTypeXref data = (ComplaintTypeXref) dataList.get(innerCountCheck);
                data.setServerUpdatedStatus(1);
                whereCondition = " where ComplaintCode = '" + data.getComplaintCode() + "'";
                try {
                    ccData = new JSONObject(gson.toJson(data));
                    dataToInsert.add(CommonUtils.toMap(ccData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
                recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(tableName, "ComplaintCode", data.getComplaintCode()));
            } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_ADVANCED_DETAILS)) {
                AdvancedDetails data = (AdvancedDetails) dataList.get(innerCountCheck);

                whereCondition = " where PlotCode = '" + data.getPlotCode() + "' and ReceiptNumber = '" + data.getReceiptNumber() + "' and CreatedDate = '" + data.getCreatedDate() + "' ";
                try {
                    ccData = new JSONObject(gson.toJson(data));
                    dataToInsert.add(CommonUtils.toMap(ccData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
                recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInAdvanceDetailsTable(data.getPlotCode(), data.getReceiptNumber(), data.getCreatedDate()));
            } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_NURSERYSAPLING_DETAILS)) {
                NurserySaplingDetails data = (NurserySaplingDetails) dataList.get(innerCountCheck);

//                whereCondition = " where ComplaintCode = '" + data.getComplaintCode() + "'";
                try {
                    ccData = new JSONObject(gson.toJson(data));
                    dataToInsert.add(CommonUtils.toMap(ccData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
//                recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(tableName, "ComplaintCode", data.getComplaintCode()));
            }


            else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_PlotFFB_Details)) {
                PlotFFBDetails data = (PlotFFBDetails) dataList.get(innerCountCheck);
                whereCondition = " where Code = '" + data.getCode() + "'";
                try {
                    ccData = new JSONObject(gson.toJson(data));
                    dataToInsert.add(CommonUtils.toMap(ccData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
                recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(tableName, "Code", data.getCode()));
            }

            else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_PlotGrading_Details)) {
                PlotGradingDetails data = (PlotGradingDetails) dataList.get(innerCountCheck);
                whereCondition = " where PlotCode = '" + data.getPlotCode() + "'";
                try {
                    ccData = new JSONObject(gson.toJson(data));
                    dataToInsert.add(CommonUtils.toMap(ccData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
                recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(tableName, "PlotCode", data.getPlotCode()));
            }

            else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_Recovery_Farmer_Group)) {
                recdata = (RecoveryFarmerGroup) dataList.get(innerCountCheck);
                recdata.setServerUpdatedStatus(1);
                //Log.d("FarmerCode",data.getFarmerCode());
                whereCondition = "where FarmerCode = '" + recdata.getFarmerCode()  + "' and RecoveryFarmerCode = '" + recdata.getRecoveryFarmercode() + "'";
                //whereCondition = "where FarmerCode = '" + data.getFarmerCode()  + "' and RecoveryFarmerCode = '" + data.getRecoveryFarmercode() + "'";
                try {
                    ccData = new JSONObject(gson.toJson(recdata));
                    dataToInsert.add(CommonUtils.toMap(ccData));

                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
                //recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInRecoveryFarmerTable(tableName,data.getId(),"FarmerCode", data.getFarmerCode(),String.valueOf(data.getRecoveryFarmercode())));
                recrecordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInRecoveryFarmerTable(tableName,"FarmerCode", recdata.getFarmerCode(),String.valueOf(recdata.getRecoveryFarmercode()),recdata.getIsActive()));
                //recrecordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInRecoveryFarmerTable(tableName,"FarmerCode", recdata.getFarmerCode(),String.valueOf(recdata.getRecoveryFarmercode())));
                Log.d("RecordExists", recrecordExisted + "");
            }
            else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_HarvestorVisitHistory)) {
                HarvestorVisitHistory data = (HarvestorVisitHistory) dataList.get(innerCountCheck);
                whereCondition = " where Code = '" + data.getCode() + "'";
                try {
                    ccData = new JSONObject(gson.toJson(data));
                    dataToInsert.add(CommonUtils.toMap(ccData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
                recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(tableName, "Code", data.getCode()));
            }
            else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_PlotGapFillingDetails)) {
                PlotGapFillingDetails data = (PlotGapFillingDetails) dataList.get(innerCountCheck);
                whereCondition = " where PlotCode = '" + data.getPlotCode() + "'";
                try {
                    ccData = new JSONObject(gson.toJson(data));
                    dataToInsert.add(CommonUtils.toMap(ccData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "####" + e.getLocalizedMessage());
                }
                recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(tableName, "PlotCode", data.getPlotCode()));
            }

            else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_AnnualTagetsKRA)) {
                AnnualTagetsKRA data = (AnnualTagetsKRA) dataList.get(innerCountCheck);
                Log.e(LOG_TAG, "#### AnnualTagetsKRA data before JSON conversion: " + gson.toJson(data));
                if (data == null) {
                    Log.e(LOG_TAG, "#### AnnualTagetsKRA data is null at index: " + innerCountCheck);
                    return;
                }
                whereCondition = " where KRACode = '" + data.getKraCode() + "'";

//                if (data != null) { // Ensure data is not null

                try {
                    ccData = new JSONObject(gson.toJson(data));
                    Log.e(LOG_TAG, "#### JSON Object for AnnualTagetsKRA: " + ccData.toString());
                    dataToInsert.add(CommonUtils.toMap(ccData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "#### Error parsing JSON for AnnualTagetsKRA: " + e.getLocalizedMessage());
                }

                    recordExisted = dataAccessHandler.checkValueExistedInDatabase(Queries.getInstance().checkRecordStatusInTable(tableName, "KRACode", data.getKraCode()));
//                } else {
//                    Log.e(LOG_TAG, "#### AnnualTagetsKRA data is null");
//                }
            }
            else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_MonthlyTagetsKRA)) {
                MonthlyTagetsKRA data = (MonthlyTagetsKRA) dataList.get(innerCountCheck);
                Log.e(LOG_TAG, "#### MonthlyTagetsKRA data" + data);
                whereCondition = " where KRACode = '" + data.getKraCode()
                        + "' and MonthNumber = " + data.getMonthNumber();

                //     if (data != null) { // Ensure data is not null
                    try {
                        ccData = new JSONObject(gson.toJson(data));
                        dataToInsert.add(CommonUtils.toMap(ccData));


                    } catch (JSONException e) {
                        Log.e(LOG_TAG, "#### Error parsing JSON for MonthlyTagetsKRA: " + e.getLocalizedMessage());
                    }
                recordExisted = dataAccessHandler.checkValueExistedInDatabase(
                        Queries.getInstance().checkRecordStatusInMonthlytargetTable(tableName,  data.getKraCode(), data.getMonthNumber()));
//
            }

            if (dataList.size() != innerCountCheck) {
                updateOrInsertData(tableName, dataToInsert, whereCondition, recordExisted, dataAccessHandler, new ApplicationThread.OnComplete() {
                    @Override
                    public void execute(boolean success, Object result, String msg) {
                        innerCountCheck++;
                        if (innerCountCheck == dataList.size()) {
                            innerCountCheck = 0;
                            onComplete.execute(true, "", "");
                        } else {
                            updateDataIntoDataBase(transactionsData, dataAccessHandler, tableName, onComplete);
                        }
                    }
                });
            } else {
                onComplete.execute(true, "", "");
            }
        } else {
            innerCountCheck++;
            if (innerCountCheck == dataList.size()) {
                innerCountCheck = 0;
                onComplete.execute(true, "", "");
            } else {
                updateDataIntoDataBase(transactionsData, dataAccessHandler, tableName, onComplete);
            }
        }

    }

    //update transaction data
    public static synchronized void updateTransactionData(final LinkedHashMap<String, List> transactionsData, final DataAccessHandler dataAccessHandler, final List<String> tableNames, final ProgressDialogFragment progressDialogFragment, final ApplicationThread.OnComplete onComplete) {
        progressDialogFragment.updateText("Updating data...");
        if (transactionsData != null && transactionsData.size() > 0) {
            Log.v(LOG_TAG, "@@@ Transactions sync is success and data size is " + transactionsData.size());
            final String tableName = tableNames.get(reverseSyncTransCount);
            innerCountCheck = 0;
            updateDataIntoDataBase(transactionsData, dataAccessHandler, tableName, new ApplicationThread.OnComplete() {
                @Override
                public void execute(boolean success, Object result, String msg) {
                    if (success) {
                        reverseSyncTransCount++;
                        if (reverseSyncTransCount == transactionsData.size()) {
                            onComplete.execute(success, "data updated successfully", "");
                        } else {
                            updateTransactionData(transactionsData, dataAccessHandler, tableNames, progressDialogFragment, onComplete);
                        }
                    } else {
                        reverseSyncTransCount++;
                        if (reverseSyncTransCount == transactionsData.size()) {
                            onComplete.execute(success, "data updated successfully", "");
                        } else {
                            updateTransactionData(transactionsData, dataAccessHandler, tableNames, progressDialogFragment, onComplete);
                        }
                    }
                }
            });
        } else {
            onComplete.execute(false, "data not found to save", "");
        }
    }

    //send location tracking data to server
    public static void sendTrackingData(final Context context, final ApplicationThread.OnComplete onComplete) {
        final DataAccessHandler dataAccessHandler = new DataAccessHandler(context);
        List<LocationTracker> gpsTrackingList = (List<LocationTracker>) dataAccessHandler.getGpsTrackingData(Queries.getInstance().getGpsTrackingRefresh(), 1);
        if (null != gpsTrackingList && !gpsTrackingList.isEmpty()) {

            for (LocationTracker tracker : gpsTrackingList) {
                String address = getAddressFromLatLong(context, tracker.getLatitude(), tracker.getLongitude());
                tracker.setAddress(address);
            }

            Type listType = new TypeToken<List>() {
            }.getType();
            Gson gson = null;
            gson = new GsonBuilder().serializeNulls().create();
            String dat = gson.toJson(gpsTrackingList, listType);
            JSONObject transObj = new JSONObject();
            try {
                transObj.put(DatabaseKeys.TABLE_Location_TRACKING_DETAILS, new JSONArray(dat));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.v(LOG_TAG, "@@@@ check.." + transObj.toString());
            CloudDataHandler.placeDataInCloud(context, transObj, Config.live_url + Config.locationTrackingURL, new ApplicationThread.OnComplete<String>() {
                        @Override
                        public void execute(boolean success, String result, String msg) {
                            if (success) {
                                dataAccessHandler.executeRawQuery(String.format(Queries.getInstance().updateServerUpdatedStatus(), DatabaseKeys.TABLE_Location_TRACKING_DETAILS));
                                Log.v(LOG_TAG, "@@@ Transactions sync success for " + DatabaseKeys.TABLE_Location_TRACKING_DETAILS);
                                onComplete.execute(true, null, "Sync is success");
                            } else {
                                onComplete.execute(false, null, "Sync is failed");
                            }
                        }
                    }
            );

        }
    }

    public static String getAddressFromLatLong(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<android.location.Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                android.location.Address address = addresses.get(0);
                return address.getAddressLine(0);
                //return address.getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unknown Location";
    }

    //to get alerts data
//    public static void getAlertsData(final Context context, final ApplicationThread.OnComplete<String> onComplete) {
//        CloudDataHandler.getGenericData(Config.live_url + Config.GET_ALERTS + CommonConstants.USER_ID, new ApplicationThread.OnComplete<String>() {
//            @Override
//            public void execute(boolean success, String result, String msg) {
//                if (success) {
//                    final DataAccessHandler dataAccessHandler = new DataAccessHandler(context);
//                    dataAccessHandler.executeRawQuery("delete from Alerts");
//                    LinkedHashMap<String, List> dataMap = new LinkedHashMap<>();
//                    JSONArray resultArray = null;
//                    try {
//                        resultArray = new JSONArray(result);
////                        dataMap.put("Alerts", CommonUtils.toList(resultArray));
//                        List dataList = new ArrayList();
//                        dataList.add(CommonUtils.toList(resultArray));
//                        dataAccessHandler.insertData(DatabaseKeys.TABLE_ALERTS, CommonUtils.toList(resultArray), new ApplicationThread.OnComplete<String>() {
//                            @Override
//                            public void execute(boolean success, String result, String msg) {
//                                if (success) {
//                                    onComplete.execute(true, "", "");
//                                } else {
//                                    onComplete.execute(false, "", "");
//                                }
//                            }
//                        });
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        onComplete.execute(false, "", "");
//                    }
//                } else {
//                    onComplete.execute(false, "", "");
//                }
//            }
//        });
//    }

    //Download data from the server
    public static class DownLoadData extends AsyncTask<String, String, String> {

        private static final MediaType TEXT_PLAIN = MediaType.parse("application/x-www-form-urlencoded");
        private Context context;
        private String date;
        private List<DataCountModel> totalData;
        private int totalDataCount;
        private int currentIndex;
        private DataAccessHandler dataAccessHandler;
        private ProgressDialogFragment progressDialogFragment;


        public DownLoadData(final Context context, final String date, final List<DataCountModel> totalData, int totalDataCount, int currentIndex, DataAccessHandler dataAccessHandler, ProgressDialogFragment progressDialogFragment) {
            this.context = context;
            this.totalData = totalData;
            this.date = date;
            this.totalDataCount = totalDataCount;
            this.currentIndex = currentIndex;
            this.dataAccessHandler = dataAccessHandler;
            this.progressDialogFragment = progressDialogFragment;
        }

        @Override
        protected String doInBackground(String... params) {
            String resultMessage = null;
            Response response = null;
            String countUrl = Config.live_url + String.format(Config.getTransData, totalData.get(totalDataCount).getMethodName());
            Log.v(LOG_TAG, "@@@ data sync url " + countUrl);
            final String tableName = totalData.get(totalDataCount).getTableName();

            progressDialogFragment.updateText("Downloading " + tableName + " (" + currentIndex + "/" + totalData.get(totalDataCount).getCount() + ")" + " data");
            if (currentIndex == 0) {
                if (tableName.equalsIgnoreCase("Farmer")) {
                    FarmerDataCount = totalData.get(totalDataCount).getCount();
                    Log.d("FarmerDataCount", FarmerDataCount + "");
                } else if (tableName.equalsIgnoreCase("Plot")) {
                    PlotDataCount = totalData.get(totalDataCount).getCount();
                    Log.d("PlotDataCount", PlotDataCount + "");

                }
            }
            try {
                URL obj = new URL(countUrl);
                Map<String, String> syncDataMap = new LinkedHashMap<>();
          syncDataMap.put("Date", TextUtils.isEmpty(date) ? "null" : date);
           //  syncDataMap.put("Date", "2025-02-12 12:48:04");
                syncDataMap.put("UserId", CommonConstants.USER_ID);
                syncDataMap.put("IsUserDataAccess", CommonConstants.migrationSync);
                syncDataMap.put("Index", String.valueOf(currentIndex));
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setRequestProperty("User-Agent", USER_AGENT);

                final StringBuilder sb = new StringBuilder();
                boolean first = true;
                RequestBody requestBody = null;
                for (Map.Entry<String, String> entry : syncDataMap.entrySet()) {
                    if (first) first = false;
                    else sb.append("&");

                    sb.append(URLEncoder.encode(entry.getKey(), HTTP.UTF_8)).append("=")
                            .append(URLEncoder.encode(entry.getValue().toString(), HTTP.UTF_8));

                    Log.d(LOG_TAG, "\nposting key: " + entry.getKey() + " -- value: " + entry.getValue());
                }
                requestBody = RequestBody.create(TEXT_PLAIN, sb.toString());

                Request request = HttpClient.buildRequest(countUrl, "POST", (requestBody != null) ? requestBody : RequestBody.create(TEXT_PLAIN, "")).build();
                OkHttpClient client = getOkHttpClient();
                response = client.newCall(request).execute();
                int statusCode = response.code();

                final String strResponse = response.body().string();


                Log.d(LOG_TAG, " ############# POST RESPONSE ################ (" + statusCode + ")\n\n" + strResponse + "\n\n");
                JSONArray dataArray = new JSONArray(strResponse);

                if (statusCode == HttpURLConnection.HTTP_OK) {

                    if (TextUtils.isEmpty(date)) {
                        if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_COMPLAINTTYPEXREF)) {
                            Log.v(LOG_TAG, "@@@@ Data insertion status comp ");
                        } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_COMPLAINTREPOSITORY)) {
                            Log.v(LOG_TAG, "@@@@ Data insertion status comp2 ");
                        }
                        List dataToInsert = new ArrayList();
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject eachDataObject = dataArray.getJSONObject(i);
                            dataToInsert.add(CommonUtils.toMap(eachDataObject));
                        }
                        dataAccessHandler.insertData(tableName, dataToInsert, new ApplicationThread.OnComplete<String>() {
                            @Override
                            public void execute(boolean success, String result, String msg) {
                                if (success) {
                                    Log.v(LOG_TAG, "@@@@ Data insertion status " + result);
                                    if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_FARMER)) {
                                        if (currentIndex == 0) {
                                            FarmerResetCount = 1;
                                        } else {
                                            FarmerResetCount = FarmerResetCount + 1;
                                        }
                                    } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_PLOT)) {
                                        if (currentIndex == 0) {
                                            PlotResetCount = 1;
                                        } else {
                                            PlotResetCount = PlotResetCount + 1;
                                        }
                                    }
                                } else {
                                    Log.v(LOG_TAG, "@@@@ Data insertion Failed In Table-" + tableName + "Due to" + result);
                                }

                            }
                        });
                    }
                    else {
                        if (tableName.equalsIgnoreCase("FileRepository")) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<FileRepository>>() {
                            }.getType();
                            List<FileRepository> fileRepositoryInnerList = gson.fromJson(dataArray.toString(), type);
                            if (null != fileRepositoryInnerList && fileRepositoryInnerList.size() > 0)
                                dataToUpdate.put(tableName, fileRepositoryInnerList);
                        } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_ADDRESS)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<Address>>() {
                            }.getType();
                            List<Address> addressDataList = gson.fromJson(dataArray.toString(), type);
                            if (null != addressDataList && addressDataList.size() > 0)
                                dataToUpdate.put(tableName, addressDataList);
                        } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_FARMER)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<Farmer>>() {
                            }.getType();
                            List<Farmer> farmerDataList = gson.fromJson(dataArray.toString(), type);
                            if (null != farmerDataList && farmerDataList.size() > 0)
                                dataToUpdate.put(tableName, farmerDataList);
                        } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_PLOT)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<Plot>>() {
                            }.getType();
                            List<Plot> plotDataList = gson.fromJson(dataArray.toString(), type);
                            if (null != plotDataList && plotDataList.size() > 0)
                                dataToUpdate.put(tableName, plotDataList);
                        } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_FARMERHISTORY)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<FarmerHistory>>() {
                            }.getType();
                            List<FarmerHistory> farmerHistoryDataList = gson.fromJson(dataArray.toString(), type);
                            if (null != farmerHistoryDataList && farmerHistoryDataList.size() > 0)
                                dataToUpdate.put(tableName, farmerHistoryDataList);
                        } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_HEALTHPLANTATION)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<Healthplantation>>() {
                            }.getType();
                            List<Healthplantation> healthplantationList = gson.fromJson(dataArray.toString(), type);
                            if (null != healthplantationList && healthplantationList.size() > 0)
                                dataToUpdate.put(tableName, healthplantationList);
                        } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_PEST)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<Pest>>() {
                            }.getType();
                            List<Pest> dataList = gson.fromJson(dataArray.toString(), type);
                            if (null != dataList && dataList.size() > 0)
                                dataToUpdate.put(tableName, dataList);
                        } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_PESTCHEMICALXREF)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<PestChemicalXref>>() {
                            }.getType();
                            List<PestChemicalXref> dataList = gson.fromJson(dataArray.toString(), type);
                            dataToUpdate.put(tableName, dataList);
                        }
                        else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_Ganoderma)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<Ganoderma>>() {
                            }.getType();
                            List<Ganoderma> dataList = gson.fromJson(dataArray.toString(), type);
                            dataToUpdate.put(tableName, dataList);}
                            else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_NUTRIENT)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<Nutrient>>() {
                            }.getType();
                            List<Nutrient> dataList = gson.fromJson(dataArray.toString(), type);
                            if (null != dataList && dataList.size() > 0)
                                dataToUpdate.put(tableName, dataList);
                        } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_DISEASE)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<Disease>>() {
                            }.getType();
                            List<Disease> dataList = gson.fromJson(dataArray.toString(), type);
                            if (null != dataList && dataList.size() > 0)
                                dataToUpdate.put(tableName, dataList);
                        } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_WEED)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<Weed>>() {
                            }.getType();
                            List<Weed> dataList = gson.fromJson(dataArray.toString(), type);
                            if (null != dataList && dataList.size() > 0)
                                dataToUpdate.put(tableName, dataList);
                        } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_COLLECTION)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<Collection>>() {
                            }.getType();
                            List<Collection> dataList = gson.fromJson(dataArray.toString(), type);
                            if (null != dataList && dataList.size() > 0)
                                dataToUpdate.put(tableName, dataList);
                        } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_COLLECTIONPLOTXREF)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<CollectionPlotXref>>() {
                            }.getType();
                            List<CollectionPlotXref> dataList = gson.fromJson(dataArray.toString(), type);
                            if (null != dataList && dataList.size() > 0)
                                dataToUpdate.put(tableName, dataList);
                        } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_GEOBOUNDARIES)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<GeoBoundaries>>() {
                            }.getType();
                            List<GeoBoundaries> dataList = gson.fromJson(dataArray.toString(), type);
                            if (null != dataList && dataList.size() > 0)
                                dataToUpdate.put(tableName, dataList);
                        } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_PLANTATION)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<Plantation>>() {
                            }.getType();
                            List<Plantation> dataList = gson.fromJson(dataArray.toString(), type);
                            if (null != dataList && dataList.size() > 0)
                                dataToUpdate.put(tableName, dataList);
                        } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_FARMERBANK)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<FarmerBank>>() {
                            }.getType();
                            List<Plantation> dataList = gson.fromJson(dataArray.toString(), type);
                            if (null != dataList && dataList.size() > 0)
                                dataToUpdate.put(tableName, dataList);
                        } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_IDENTITYPROOF)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<IdentityProof>>() {
                            }.getType();
                            List<Plantation> dataList = gson.fromJson(dataArray.toString(), type);
                            if (null != dataList && dataList.size() > 0)
                                dataToUpdate.put(tableName, dataList);
                        } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_PLOTLANDLORD)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<PlotLandlord>>() {
                            }.getType();
                            List<Plantation> dataList = gson.fromJson(dataArray.toString(), type);
                            if (null != dataList && dataList.size() > 0)
                                dataToUpdate.put(tableName, dataList);
                        } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_LANDLORDBANK)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<LandlordBank>>() {
                            }.getType();
                            List<Plantation> dataList = gson.fromJson(dataArray.toString(), type);
                            if (null != dataList && dataList.size() > 0)
                                dataToUpdate.put(tableName, dataList);
                        } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_LANDLORDIDPROOFS)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<LandlordIdProof>>() {
                            }.getType();
                            List<Plantation> dataList = gson.fromJson(dataArray.toString(), type);
                            if (null != dataList && dataList.size() > 0)
                                dataToUpdate.put(tableName, dataList);
                        } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_CROPMAINTENANCEHISTORY)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<CropMaintenanceHistory>>() {
                            }.getType();
                            List<CropMaintenanceHistory> dataList = gson.fromJson(dataArray.toString(), type);
                            if (null != dataList && dataList.size() > 0)
                                dataToUpdate.put(tableName, dataList);
                        } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_COMPLAINT)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<Complaints>>() {
                            }.getType();
                            List<Complaints> dataList = gson.fromJson(dataArray.toString(), type);
                            if (null != dataList && dataList.size() > 0)
                                dataToUpdate.put(tableName, dataList);
                        } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_COMPLAINTSTATUSHISTORY)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<ComplaintStatusHistory>>() {
                            }.getType();
                            List<ComplaintStatusHistory> dataList = gson.fromJson(dataArray.toString(), type);
                            if (null != dataList && dataList.size() > 0)
                                dataToUpdate.put(tableName, dataList);
                        } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_COMPLAINTREPOSITORY)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<ComplaintRepository>>() {
                            }.getType();
                            List<ComplaintRepository> dataList = gson.fromJson(dataArray.toString(), type);
                            if (null != dataList && dataList.size() > 0)
                                dataToUpdate.put(tableName, dataList);
                        } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_COMPLAINTTYPEXREF)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<ComplaintTypeXref>>() {
                            }.getType();
                            List<ComplaintTypeXref> dataList = gson.fromJson(dataArray.toString(), type);
                            if (null != dataList && dataList.size() > 0)
                                dataToUpdate.put(tableName, dataList);
                        } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_RECOMMND_FERTLIZER)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<RecommndFertilizer>>() {
                            }.getType();
                            List<RecommndFertilizer> recommndFertilizerList = gson.fromJson(dataArray.toString(), type);
                            if (null != recommndFertilizerList && recommndFertilizerList.size() > 0)
                                dataToUpdate.put(tableName, recommndFertilizerList);

                        } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_ADVANCED_DETAILS)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<AdvancedDetails>>() {
                            }.getType();
                            List<AdvancedDetails> advancedDtails = gson.fromJson(dataArray.toString(), type);
                            if (null != advancedDtails && advancedDtails.size() > 0)
                                dataToUpdate.put(tableName, advancedDtails);

                        } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_NURSERYSAPLING_DETAILS)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<NurserySaplingDetails>>() {
                            }.getType();
                            List<NurserySaplingDetails> advancedDtails = gson.fromJson(dataArray.toString(), type);
                            if (null != advancedDtails && advancedDtails.size() > 0)
                                dataToUpdate.put(tableName, advancedDtails);

                        } else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_visit_Details)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<VisitRequests>>() {
                            }.getType();
                            List<VisitRequests> visitDtails = gson.fromJson(dataArray.toString(), type);
                            if (null != visitDtails && visitDtails.size() > 0)
                                dataToUpdate.put(tableName, visitDtails);

                        }
                        else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_PlotFFB_Details)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<PlotFFBDetails>>() {
                            }.getType();
                            List<PlotFFBDetails> plotFFBDetails = gson.fromJson(dataArray.toString(), type);
                            if (null != plotFFBDetails && plotFFBDetails.size() > 0)
                                dataToUpdate.put(tableName, plotFFBDetails);
                        }
                        else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_PlotGrading_Details)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<PlotGradingDetails>>() {
                            }.getType();
                            List<PlotGradingDetails> plotGradingDetails = gson.fromJson(dataArray.toString(), type);
                            if (null != plotGradingDetails && plotGradingDetails.size() > 0)
                                dataToUpdate.put(tableName, plotGradingDetails);
                        }
                        else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_Recovery_Farmer_Group)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<RecoveryFarmerGroup>>() {
                            }.getType();
                            List<RecoveryFarmerGroup> recoveryFarmerGroups = gson.fromJson(dataArray.toString(), type);
                            if (null != recoveryFarmerGroups && recoveryFarmerGroups.size() > 0)
                                dataToUpdate.put(tableName, recoveryFarmerGroups);

                        }
                        else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_HarvestorVisitHistory)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<HarvestorVisitHistory>>() {
                            }.getType();
                            List<HarvestorVisitHistory> HarvestinghistoryDtails = gson.fromJson(dataArray.toString(), type);
                            if (null != HarvestinghistoryDtails && HarvestinghistoryDtails.size() > 0)
                                dataToUpdate.put(tableName, HarvestinghistoryDtails);

                        }
                        else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_PlotGapFillingDetails)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<PlotGapFillingDetails>>() {
                            }.getType();
                            List<PlotGapFillingDetails> plotgapfillingdetails = gson.fromJson(dataArray.toString(), type);
                            if (null != plotgapfillingdetails && plotgapfillingdetails.size() > 0)
                                dataToUpdate.put(tableName, plotgapfillingdetails);

                        }
                        else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_AnnualTagetsKRA)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<AnnualTagetsKRA>>() {
                            }.getType();
                            List<AnnualTagetsKRA> AnunualTagetsKRAdetails = gson.fromJson(dataArray.toString(), type);
                            if (null != AnunualTagetsKRAdetails && AnunualTagetsKRAdetails.size() > 0)
                                dataToUpdate.put(tableName, AnunualTagetsKRAdetails);

                        }
                        else if (tableName.equalsIgnoreCase(DatabaseKeys.TABLE_MonthlyTagetsKRA)) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<MonthlyTagetsKRA>>() {
                            }.getType();
                            List<MonthlyTagetsKRA> MonthlyTagetsKRAdetails = gson.fromJson(dataArray.toString(), type);
                            if (null != MonthlyTagetsKRAdetails && MonthlyTagetsKRAdetails.size() > 0)
                                dataToUpdate.put(tableName, MonthlyTagetsKRAdetails);

                        }
                    }
                    resultMessage = "success";
                } else {
                    resultMessage = "failed";
                }
            } catch (Exception e) {
                resultMessage = e.getMessage();
                Log.e(LOG_TAG, "@@@ data sync failed for " + tableName);
            }
            return resultMessage;
        }

        @Override
        protected void onPostExecute(String result) {
            currentIndex++;
            if (currentIndex == totalData.get(totalDataCount).getCount()) {
                currentIndex = 0;
                totalDataCount++;
                if (totalDataCount == totalData.size()) {
                    Log.v(LOG_TAG, "@@@ done with data syncing");
                    if (TextUtils.isEmpty(date)) {
                        ProgressBar.hideProgressBar();
                        if (null != progressDialogFragment && !CommonUtils.currentActivity.isFinishing()) {
                            progressDialogFragment.dismiss();

                        }
                        Integer resetFarmerCount = Integer.parseInt(dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getFarmerCount()));
                        Integer resetPlotCount = Integer.parseInt(dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getPlotCount()));

                        Log.d("FarmerResetCount", FarmerResetCount + "");
                        Log.d("PlotResetCount", PlotResetCount + "");
                        Log.d("FarmerDataCount", FarmerDataCount + "");
                        Log.d("PlotDataCount", PlotDataCount + "");

                        if ((FarmerDataCount == FarmerResetCount) && (PlotDataCount ==  PlotResetCount)) {
                            if (resetFarmerCount != null && resetPlotCount != null) {

                                UiUtils.showCustomToastMessage("Data synced successfully", context, 0);
                                updateSyncDate(context, CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));

                            } else {
                                UiUtils.showCustomToastMessage("Data is not Synced Properly Again its DownLoading the Data ", context, 1);
                               Log.d("FromRestCount", "Yes");
                                if (CommonUtils.isNetworkAvailable(context)) {
                                    updateSyncDate(context, null);
                                    for (String s : RefreshSyncActivity.allRefreshDataMap) {
                                        dataAccessHandler.executeRawQuery("DELETE FROM " + s);
                                        Log.v(LOG_TAG, "delete table" + s);
                                    }
                                    progressDialogFragment = new ProgressDialogFragment();
                                    startTransactionSync(context, progressDialogFragment);
                                } else {
                                    UiUtils.showCustomToastMessage("Please check network connection", context, 1);
                                }

                            }

                        } else {
                            UiUtils.showCustomToastMessage("Data is not Synced Properly Again its DownLoading the Data", context, 1);
                            Log.d("FromRestCount", "No");
                            if (CommonUtils.isNetworkAvailable(context)) {
                                updateSyncDate(context, null);
                                for (String s : RefreshSyncActivity.allRefreshDataMap) {
                                    dataAccessHandler.executeRawQuery("DELETE FROM " + s);
                                    Log.v(LOG_TAG, "delete table" + s);
                                }
                                progressDialogFragment = new ProgressDialogFragment();
                                startTransactionSync(context, progressDialogFragment);

                            } else {
                                UiUtils.showCustomToastMessage("Please check network connection", context, 1);
                            }
                        }

                    } else {
                        reverseSyncTransCount = 0;
                        Set tableNames = dataToUpdate.keySet();
                        List<String> tableNamesList = new ArrayList();
                        tableNamesList.addAll(tableNames);
                        updateTransactionData(dataToUpdate, dataAccessHandler, tableNamesList, progressDialogFragment, new ApplicationThread.OnComplete() {
                            @Override
                            public void execute(boolean success, Object result, String msg) {

                                //if (dataToUpdate.size() >0){
                                if (success) {
                                    updateSyncDate(context, CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
                                    UiUtils.showCustomToastMessage("Data synced successfully", context, 0);
                                }  else {

                                    UiUtils.showCustomToastMessage("There is no transactions data to sync", context, 1);
                                }
                                //}
//                                else {
//                                    UiUtils.showCustomToastMessage("There is no transactions data to sync", context, 1);
//                                }

                                if (null != progressDialogFragment && !CommonUtils.currentActivity.isFinishing()) {
                                    progressDialogFragment.dismiss();
                                }
                            }
                        });
                    }
                } else {
                    Log.v(LOG_TAG, "@@@ data downloading next count " + currentIndex + " out of " + totalData.size());
                    new DownLoadData(context, date, totalData, totalDataCount, currentIndex, dataAccessHandler, progressDialogFragment).execute();
                }
            } else {
                Log.v(LOG_TAG, "@@@ data downloading next count " + currentIndex + " out of " + totalData.size());
                new DownLoadData(context, date, totalData, totalDataCount, currentIndex, dataAccessHandler, progressDialogFragment).execute();
            }
        }
    }

}