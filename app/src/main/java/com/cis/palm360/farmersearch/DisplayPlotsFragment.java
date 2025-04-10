package com.cis.palm360.farmersearch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cis.palm360.R;
import com.cis.palm360.ViewmapsActivity;
import com.cis.palm360.areacalculator.LocationProvider;
import com.cis.palm360.areacalculator.PreViewAreaCalScreen;
import com.cis.palm360.areaextension.FarmerPlotDetailsAdapter;
import com.cis.palm360.areaextension.RegistrationFlowScreen;
import com.cis.palm360.cloudhelper.Log;
import com.cis.palm360.common.CommonConstants;
import com.cis.palm360.common.CommonUiUtils;
import com.cis.palm360.common.CommonUtils;
import com.cis.palm360.conversion.ConversionMainFlowActivity;
import com.cis.palm360.cropmaintenance.CropMaintenanceHomeScreen;
import com.cis.palm360.cropmaintenance.HarvestingActivity;
import com.cis.palm360.cropmaintenance.PlantationAudit;
import com.cis.palm360.database.CCDataAccessHandler;
import com.cis.palm360.database.DataAccessHandler;
import com.cis.palm360.database.Palm3FoilDatabase;
import com.cis.palm360.database.Queries;
import com.cis.palm360.datasync.helpers.DataManager;
import com.cis.palm360.dbmodels.Address;
import com.cis.palm360.dbmodels.Farmer;
import com.cis.palm360.dbmodels.FileRepository;
import com.cis.palm360.dbmodels.Plot;
import com.cis.palm360.dbmodels.BasicFarmerDetails;
import com.cis.palm360.dbmodels.PlotDetailsObj;
import com.cis.palm360.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;


//Display Plot of the farmers Dialog
public class DisplayPlotsFragment extends DialogFragment implements FarmerPlotDetailsAdapter.ClickListener {

    private static final String LOG_TAG = DisplayPlotsFragment.class.getName();
    private static LocationProvider mLocationProvider;
    private List<PlotDetailsObj> plotDetailsObjArrayList = new ArrayList<>();
    private RecyclerView rvplotlist;
    private FarmerPlotDetailsAdapter farmerplotDetailsLVAdapter;
    private CCDataAccessHandler ccDataAccessHandler = null;
    private BasicFarmerDetails farmer;
    private DataAccessHandler dataAccessHandler = null;
    private int plotStatus;
    private double CurrentLatitude, CurrentLongitude;
    private TextView currentLocationTxt;
    private Button reScanBtn;
    private LinearLayout ReTakeGeoTagLL;
    private static String latLong = "";
    public String UpdatedDate, UpdatedByUserId, ServerUpdatedStatus;
    private Palm3FoilDatabase palm3FoilDatabase;
    private int IsRetakeGeoTagRequired = 0;
    public static String plotCode = "";
    LocationManager lm;

    private static final String ARG_BASIC_FARMER_DETAILS = "basicFarmerDetails";
    private static final String ARG_SELECTED_VILLAGE_IDS = "selectedVillageIds";

    private BasicFarmerDetails basicFarmerDetails;
    private String selectedVillageIds;
    //Gets location of the Plot
    public static LocationProvider getLocationProvider(Context context, boolean showDialog) {
        if (mLocationProvider == null) {
            mLocationProvider = new LocationProvider(context, mLatLong -> latLong = mLatLong);

        }
        if (mLocationProvider.getLocation(showDialog)) {
            return mLocationProvider;
        } else {
            return null;
        }

    }

    public String getLatLong(Context context, boolean showDialog) {

        mLocationProvider = getLocationProvider(context, showDialog);

        if (mLocationProvider != null) {
            latLong = mLocationProvider.getLatitudeLongitude();

        }

        return latLong;
    }


    public DisplayPlotsFragment() {
        // Required empty public constructor
    }
//    public static DisplayPlotsFragment newInstance(BasicFarmerDetails basicFarmerDetails, String selectedVillageIds) {
//        DisplayPlotsFragment fragment = new DisplayPlotsFragment();
//        Bundle args = new Bundle();
//        args.putSerializable(ARG_BASIC_FARMER_DETAILS, basicFarmerDetails); // Assuming BasicFarmerDetails implements Serializable
//        args.putString(ARG_SELECTED_VILLAGE_IDS, selectedVillageIds);
//        fragment.setArguments(args);
//        return fragment;
//    }
    public static DisplayPlotsFragment newInstance(BasicFarmerDetails basicFarmerDetails) {
        DisplayPlotsFragment fragment = new DisplayPlotsFragment();
        Bundle args = new Bundle();
        args.putSerializable("basicFarmerDetails", basicFarmerDetails);
        fragment.setArguments(args);
        return fragment;
    }

    //Initializing the Fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plots_display_screen, container);
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        view.setMinimumWidth((int) (displayRectangle.width() * 0.7f));
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                getLocationDetails();

            }
        } else {
            if (CommonUtils.isLocationPermissionGranted(getActivity())) {
                getLocationDetails();
            } else {
                UiUtils.showCustomToastMessage("Please Turn On GPS", getActivity(), 1);
            }
        }
//        if (getArguments() != null) {
//            basicFarmerDetails = (BasicFarmerDetails) getArguments().getSerializable(ARG_BASIC_FARMER_DETAILS);
//            selectedVillageIds = getArguments().getString(ARG_SELECTED_VILLAGE_IDS);
//            android.util.Log.v(LOG_TAG, "@@@ selectedVillageIds " + "== " + selectedVillageIds.toString());
//        }

        farmer = (BasicFarmerDetails) getArguments().getSerializable("basicFarmerDetails");

        ccDataAccessHandler = new CCDataAccessHandler(getActivity());
        palm3FoilDatabase = Palm3FoilDatabase.getPalm3FoilDatabase(getActivity());
        rvplotlist = (RecyclerView) view.findViewById(R.id.lv_farmerplotdetails);

        currentLocationTxt = (TextView) view.findViewById(R.id.currentLocationTxt);
        ReTakeGeoTagLL = (LinearLayout) view.findViewById(R.id.retakeGeoTagLL);
        reScanBtn = (Button) view.findViewById(R.id.reScanBtn);

        reScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtils.isLocationPermissionGranted(getActivity())) {
                    String latlong[] = getLatLong(getActivity(), false).split("@");
                    CurrentLatitude = Double.parseDouble(latlong[0]);
                    CurrentLongitude = Double.parseDouble(latlong[1]);

                    currentLocationTxt.setText("" + CurrentLatitude + " , " + CurrentLongitude);

                    updateReTakeGeoTag();

                }
            }
        });
        bindPlotData();

        return view;
    }

    //getting latlongs of the plot
    private void getLocationDetails() {
        String latlong[] = getLatLong(getActivity(), false).split("@");
        Log.d("latlong",latlong.length + "");
        //if (latlong == null) {
        if (isLocationEnabled(getContext()) == false){
            Toast.makeText(getActivity(), "Please turn on Location", Toast.LENGTH_SHORT).show();
        } else {
             CurrentLatitude = Double.parseDouble(latlong[0]);
            CurrentLongitude = Double.parseDouble(latlong[1]);
        }


    }

    //Checking whether location is enabled or not
    private boolean isLocationEnabled(Context context){
        int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                Settings.Secure.LOCATION_MODE_OFF);
        final boolean enabled = (mode != android.provider.Settings.Secure.LOCATION_MODE_OFF);
        return enabled;
    }

    //Updates Geotags if not taken
    private void updateReTakeGeoTag() {

        UpdatedDate = CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS);
        ServerUpdatedStatus = CommonConstants.ServerUpdatedStatus;
        UpdatedByUserId = String.valueOf(Integer.parseInt(CommonConstants.USER_ID));

        palm3FoilDatabase.UpdateGeoTagLatLng(UpdatedByUserId, UpdatedDate, CurrentLatitude, CurrentLongitude);

    }

    //Binds plots data based on plot status
    private void bindPlotData() {
        dataAccessHandler = new DataAccessHandler(getActivity());
        if (CommonUtils.isFromConversion()) {
            plotStatus = 83;
        } else if (CommonUtils.isFromCropMaintenance() || CommonUtils.isComplaint() || CommonUtils.isVisitRequests() || CommonUtils.isFromHarvesting() || CommonUtils.isFromPlantationAudit() ) {
            plotStatus = 88;
        } else if (CommonUtils.isFromFollowUp()) {
            plotStatus = 81;
        } else if (CommonUtils.isPlotSplitFarmerPlots()) {
            plotStatus = 258;
        }
        else if (CommonUtils.isFromviewonmaps()) {
            plotStatus = 85;
//
        }
        else {
            plotStatus = 89;
        }


        plotDetailsObjArrayList = ccDataAccessHandler.getPlotDetails(farmer.getFarmerCode(), plotStatus, (CommonUtils.isFromFollowUp()) ? true : true);
        if (plotDetailsObjArrayList != null && plotDetailsObjArrayList.size() > 0) {
            farmerplotDetailsLVAdapter = new FarmerPlotDetailsAdapter(getActivity(), plotDetailsObjArrayList, R.layout.small_plots_view, true);
            farmerplotDetailsLVAdapter.setOnClickListener(this);
            rvplotlist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            rvplotlist.setAdapter(farmerplotDetailsLVAdapter);
            farmerplotDetailsLVAdapter.clearSelection();
            farmerplotDetailsLVAdapter.selectedItems.clear();
        }
    }

    //On Plot Selected
    @Override
    public void onItemClicked(int position, View view) {
        if (view.getId() == R.id.ivb_plot_location_cropcollection) {
            UiUtils.showCustomToastMessage("This Plot  on Google Map", getActivity(), 1);

        } else {
            CommonConstants.PLOT_CODE = plotDetailsObjArrayList.get(position).getPlotID();
            plotCode = plotDetailsObjArrayList.get(position).getPlotID();
      String selectedLatLong = dataAccessHandler.getLatLongs(Queries.getInstance().queryCenterGeoTag());
         //   String selectedLatLong = dataAccessHandler.getLatLongs(Queries.getInstance().queryVerifyGeoTag());
            String plotArea = plotDetailsObjArrayList.get(position).getPlotArea();
            IsRetakeGeoTagRequired = dataAccessHandler.getSelectedRetakeGeoTag(Queries.getInstance().retakeGeoBoundry(CommonConstants.PLOT_CODE));

            /* comment for release apk */
//
//            if (true) {
//                moveToNextScreen();
//                return;
//            }
            String latlong[] = getLatLong(getActivity(), false).split("@");
//            CurrentLatitude = Double.parseDouble(latlong[0]);
//            CurrentLongitude = Double.parseDouble(latlong[1]);

            if (isLocationEnabled(getContext()) == false){
                Toast.makeText(getActivity(), "Please turn on Location", Toast.LENGTH_SHORT).show();
            } else {
                CurrentLatitude = Double.parseDouble(latlong[0]);
                CurrentLongitude = Double.parseDouble(latlong[1]);
            }

            if (isLocationEnabled(getContext()) == false){
                Toast.makeText(getActivity(), "Please turn on Location", Toast.LENGTH_SHORT).show();
            } else {
                if (CommonUtils.isFromFollowUp()) {
                    moveToNextScreen();
                    return;
                }
            }

            if (isLocationEnabled(getContext()) == false){
                Toast.makeText(getActivity(), "Please turn on Location", Toast.LENGTH_SHORT).show();
            } else {
                if (CommonUtils.isPlotSplitFarmerPlots()) {
                    moveToNextScreen();
                    return;
                }
            }

            if (CommonUtils.isFromviewonmaps()){
                moveToNextScreen();
                return;
            }

            if (TextUtils.isEmpty(plotArea)) {
                UiUtils.showCustomToastMessage("Plot area not found", getActivity(), 1);
                return;
            }

            if (CurrentLatitude == 0 || CurrentLongitude == 0) {
                UiUtils.showCustomToastMessage("Not able to find current location", getActivity(), 1);
                return;
            }

            if (!TextUtils.isEmpty(selectedLatLong)) {

                Log.v(LOG_TAG, "@@@@ data " + selectedLatLong);
                double actualDistance = 0;
                String[] yieldDataArr = selectedLatLong.split("-");

                if (yieldDataArr.length > 0 && !TextUtils.isEmpty(yieldDataArr[0]) && !TextUtils.isEmpty(yieldDataArr[1])) {

                    actualDistance = CommonUtils.distance(CurrentLatitude, CurrentLongitude, Double.parseDouble(yieldDataArr[0]), Double.parseDouble(yieldDataArr[1]), 'm');

                }
                Log.v(LOG_TAG, "@@@@ actual distance " + actualDistance);
                if (actualDistance <= CommonUtils.distanceToCompare(Double.parseDouble(plotDetailsObjArrayList.get(position).getTotalPalm()))) {

                    if (isLocationEnabled(getContext()) == false){
                        Toast.makeText(getActivity(), "Please turn on Location", Toast.LENGTH_SHORT).show();
                    } else {
                        moveToNextScreen();
                    }

                } else {


      moveToNextScreen();

//                    getLocationDetails();  //TODO comment for release apk
//                    if (CommonUtils.isFromConversion()) {
//                        if ((IsRetakeGeoTagRequired == 1)) {
//                            ReTakeGeoTagLL.setVisibility(View.VISIBLE);
//                        }
//                    }
//                    String units = "meters";
//                    if (actualDistance > 1000) {
//
//                        actualDistance = actualDistance * 0.001;
//                        units = "kilometers";
//                    }
//                    actualDistance = Double.parseDouble(CommonUtils.twoDForm.format(actualDistance));
//
//                    UiUtils.showCustomToastMessageLong("This location is not actual plot location, distance from plot is " + actualDistance + " " + units + " and it should be with in  " + CommonUtils.distanceToCompare(Double.parseDouble(plotDetailsObjArrayList.get(position).getTotalPalm())) + " meters", getActivity(), 1, Toast.LENGTH_LONG);
        }
         }
            else {
                UiUtils.showCustomToastMessage("Geo tag was not available in database", getActivity(), 1);
            }

        }
    }

    //moves to next screen when plot is selected
    public void moveToNextScreen() {

        Log.v(LOG_TAG, "@@@@ actual this record available");
        Farmer selectedFarmer = (Farmer) dataAccessHandler.getSelectedFarmerData(Queries.getInstance().getSelectedFarmer(farmer.getFarmerCode()), 0);
        Address selectedFarmerAddress = (Address) dataAccessHandler.getSelectedFarmerAddress(Queries.getInstance().getSelectedFarmerAddress(selectedFarmer.getAddresscode()), 0);
        FileRepository selectedFileRepository = dataAccessHandler.getSelectedFileRepository(Queries.getInstance().getSelectedFileRepositoryQuery(selectedFarmer.getCode(), 193));
        CommonConstants.CROP_MAINTENANCE_HISTORY_CODE = "";
        CommonConstants.CROP_MAINTENANCE_HISTORY_NAME = "";
        CommonConstants.FARMER_CODE = farmer.getFarmerCode();
        if (null != selectedFarmer && selectedFarmerAddress != null) {
            try {

                Plot plot = (Plot) dataAccessHandler.getSelectedPlotData(Queries.getInstance().getSelectedPlot(CommonConstants.PLOT_CODE), 0);
                DataManager.getInstance().addData(DataManager.PLOT_DETAILS, plot);
                Address savedAddressData = (Address) dataAccessHandler.getSelectedFarmerAddress(Queries.getInstance().getSelectedPlotAddress(plot.getAddesscode()), 0);
                DataManager.getInstance().addData(DataManager.VALIDATE_PLOT_ADDRESS_DETAILS, savedAddressData);
                CommonUiUtils.setGeoGraphicalData(selectedFarmer, getActivity());
                DataManager.getInstance().addData(DataManager.FARMER_PERSONAL_DETAILS, selectedFarmer);
                DataManager.getInstance().addData(DataManager.FARMER_ADDRESS_DETAILS, selectedFarmerAddress);
                if (selectedFileRepository != null)
                    DataManager.getInstance().addData(DataManager.FILE_REPOSITORY, selectedFileRepository);
                if (CommonUtils.isFromFollowUp()) {
                    startActivity(new Intent(getActivity(), RegistrationFlowScreen.class));
                    getActivity().finish();
                } else if (CommonUtils.isFromCropMaintenance() || CommonUtils.isVisitRequests()) {
                    Intent intent = new Intent(getActivity(), CropMaintenanceHomeScreen.class);
                    startActivity(intent);
                    getActivity().finish();
                } else if (CommonUtils.isPlotSplitFarmerPlots()) {
                    Log.e("========>366","Re Take Geo");
                    Intent intent = new Intent(getActivity(), PreViewAreaCalScreen.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                else if (CommonUtils.isFromHarvesting()) {
                    Intent intent = new Intent(getActivity(), HarvestingActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                else if (CommonUtils.isFromPlantationAudit()) {
                    Intent intent = new Intent(getActivity(), PlantationAudit.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                else if (CommonUtils.isFromviewonmaps()) {
                    Log.v(LOG_TAG, "@@@@ PlotCode isFromviewonmaps " +  CommonConstants.PLOT_CODE);
                    Log.v(LOG_TAG, "@@@@ PlotCode isFromviewonmaps selectedVillageIds " + selectedVillageIds);
                    Intent intent = new Intent(getActivity(), ViewmapsActivity.class);
                    intent.putExtra("plotcode", CommonConstants.PLOT_CODE);
                    startActivity(intent);
                    getActivity().finish();
                }
//                else if (CommonUtils.isComplaint()) {
//                    Intent intent = new Intent(getActivity(), ComplaintsScreenActivity.class);
//                    startActivity(intent);
//                    getActivity().finish();
//                }
                else {
                    Intent intent = new Intent(getActivity(), ConversionMainFlowActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            } catch (Exception e) {
                UiUtils.showCustomToastMessage("Error while moving to next screen", getActivity(), 1);
               Log.d("ExepectionMessage",e.getMessage());
            }
        }
    }

    public List<PlotDetailsObj> filterPlotsBasedOnDistance(List<PlotDetailsObj> plotDetailsObjArrayList) {
        List<PlotDetailsObj> plotDetailsObjList = new ArrayList<>();
        if (CurrentLatitude > 0 && CurrentLongitude > 0) {
            Log.v(LOG_TAG, "@@@@ currentLatitude " + CurrentLatitude + " currentLongitude " + CurrentLongitude);
            for (int i = 0; i < plotDetailsObjArrayList.size(); i++) {
                double actualDistance = CommonUtils.distance(CurrentLatitude, CurrentLongitude, plotDetailsObjArrayList.get(i).getLattitude(), plotDetailsObjArrayList.get(i).getLogitude(), 'm');
                Log.v(LOG_TAG, "@@@@ actual distance " + actualDistance);
                if (actualDistance <= 200) {
                    Log.v(LOG_TAG, "@@@@ actual this record available");
                    plotDetailsObjList.add(plotDetailsObjArrayList.get(i));
                } else {

                }
                if (i == plotDetailsObjArrayList.size()) {
                    return plotDetailsObjList;
                }
            }
        }
        return plotDetailsObjList;
    }
}