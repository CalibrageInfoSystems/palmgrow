 package com.cis.palm360.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.pavlospt.CircleView;
import com.cis.palm360.FiltermapsActivity;
import com.cis.palm360.R;
import com.cis.palm360.activitylogdetails.LogBookScreenActivity;
import com.cis.palm360.alerts.AlertType;
import com.cis.palm360.alerts.AlertsDisplayScreen;
import com.cis.palm360.areaextension.location.LocationSelectionScreen;
import com.cis.palm360.common.CommonConstants;
import com.cis.palm360.common.CommonUtils;
import com.cis.palm360.database.DataAccessHandler;
import com.cis.palm360.database.Queries;
import com.cis.palm360.datasync.helpers.DataManager;
import com.cis.palm360.datasync.ui.RefreshSyncActivity;
import com.cis.palm360.farmersearch.SearchFarmerScreen;
import com.cis.palm360.palmcare.palmcareScreen;
import com.cis.palm360.utils.UiUtils;

import java.util.List;

import static com.cis.palm360.common.CommonUiUtils.resetPrevRegData;


//Home Screen
public class HomeScreen extends AppCompatActivity {

    private android.widget.LinearLayout areaExtensionRel;
    private android.widget.LinearLayout prospectiveFarmersRel;
    private android.widget.LinearLayout conversionRel;
    private android.widget.LinearLayout cropMaintenanceRel;
    private android.widget.LinearLayout harvestingRel, imagesRel, planationAuditRel;
    private LinearLayout visitDetails,mapsLayout;
            private RelativeLayout transportServiceLayout;
    private LinearLayout refreshRel;
    private CircleView circleView;
    private DataAccessHandler dataAccessHandler;
    LocationManager lm;

//Initializing the UI and On Click Listeners
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


        this.refreshRel = (LinearLayout) findViewById(R.id.refreshRel1);
        this.cropMaintenanceRel = (LinearLayout) findViewById(R.id.cropMaintenanceRel);
       // this.harvestingRel = (RelativeLayout) findViewById(R.id.harvestingRel);
        imagesRel = (LinearLayout) findViewById(R.id.imagesRel);
        this.conversionRel = (LinearLayout) findViewById(R.id.conversionRel);
        this.prospectiveFarmersRel = (LinearLayout) findViewById(R.id.prospectiveFarmersRel);
        this.areaExtensionRel = (LinearLayout) findViewById(R.id.areaExtensionRel);
        LinearLayout complaintshRel = (LinearLayout) findViewById(R.id.complaintshRel);
        LinearLayout krasRel = (LinearLayout) findViewById(R.id.krasRel);
        LinearLayout alertsRel = (LinearLayout) findViewById(R.id.alertsRel1);
        LinearLayout plotSplitRel = (LinearLayout) findViewById(R.id.plotSplitRel);
        visitDetails = findViewById(R.id.visitDetails);
        circleView = (CircleView) findViewById(R.id.countTxt);
        ImageView extension_logbook = (ImageView) findViewById(R.id.extensionlogbook);
        transportServiceLayout=(RelativeLayout) findViewById(R.id.transportServiceLayout);

        planationAuditRel = (LinearLayout) findViewById(R.id.planationAuditRel);

        mapsLayout = findViewById(R.id.mapsLayout);

        dataAccessHandler = new DataAccessHandler(this);

        RelativeLayout notificationsRel = (RelativeLayout) findViewById(R.id.notficationRel);

        notificationsRel.setOnClickListener(v -> startActivity(new Intent(HomeScreen.this, NotificationsScreen.class)));


        areaExtensionRel.setOnClickListener(view -> {
            FragmentManager fm = getSupportFragmentManager();
            RegistrationTypeChooser registrationTypeChooser = new RegistrationTypeChooser();
            registrationTypeChooser.show(fm, "fragment_edit_name");
        });

        complaintshRel.setOnClickListener(view -> {
         // create new activity  here shows list button add one button after then will call fragment complaintsDetailsFragment
          //  CommonConstants.REGISTRATION_SCREEN_FROM = CommonConstants.REGISTRATION_SCREEN_FROM_COMPLAINT;
           Intent in_compalints = new Intent(HomeScreen.this,ComplaintsScreenActivity.class).putExtra("plot",false);
//               Intent in_compalints = new Intent(HomeScreen.this,ComplaintsScreenActivity.class);
           startActivity(in_compalints);
        });

        prospectiveFarmersRel.setOnClickListener(view -> {
            resetPrevRegData();
            CommonConstants.REGISTRATION_SCREEN_FROM = CommonConstants.REGISTRATION_SCREEN_FROM_VPF;
            Intent intent = new Intent(HomeScreen.this, SearchFarmerScreen.class);
            startActivity(intent);

        });

        conversionRel.setOnClickListener(v -> {
            resetPrevRegData();
            CommonConstants.REGISTRATION_SCREEN_FROM = CommonConstants.REGISTRATION_SCREEN_FROM_CONVERSION;
            Intent intent = new Intent(HomeScreen.this, SearchFarmerScreen.class);
            startActivity(intent);
        });
//
//        cropMaintenanceRel.setOnClickListener(view -> {
//            resetPrevRegData();
//            CommonConstants.REGISTRATION_SCREEN_FROM = CommonConstants.REGISTRATION_SCREEN_FROM_CP_MAINTENANCE;
//            startActivity(new Intent(HomeScreen.this, SearchFarmerScreen.class));
//        });



        cropMaintenanceRel.setOnClickListener(view -> {
            resetPrevRegData();
            startActivity(new Intent(HomeScreen.this, palmcareScreen.class));
        });

//        harvestingRel.setOnClickListener(view -> {
//            resetPrevRegData();
//            CommonConstants.REGISTRATION_SCREEN_FROM = CommonConstants.REGISTRATION_SCREEN_FROM_HARVESTING;
//            startActivity(new Intent(HomeScreen.this, SearchFarmerScreen.class));
//        });

        planationAuditRel.setOnClickListener(view -> {
            resetPrevRegData();
            CommonConstants.REGISTRATION_SCREEN_FROM = CommonConstants.REGISTRATION_SCREEN_FROM_PLANTATION_AUDIT;
            startActivity(new Intent(HomeScreen.this, SearchFarmerScreen.class));
        });

        imagesRel.setOnClickListener(view -> {
            resetPrevRegData();
            CommonConstants.REGISTRATION_SCREEN_FROM = CommonConstants.REGISTRATION_SCREEN_FROM_IMAGESUPLOADING;
            startActivity(new Intent(HomeScreen.this, SearchFarmerScreen.class));
        });

        visitDetails.setOnClickListener(view -> {
            resetPrevRegData();
            CommonConstants.REGISTRATION_SCREEN_FROM = CommonConstants.REGISTRATION_SCREEN_FROM_VISIT_REQUESTS;
            startActivity(new Intent(HomeScreen.this, SearchFarmerScreen.class));
        });


        refreshRel.setOnClickListener(view -> {
            resetPrevRegData();
            startActivity(new Intent(HomeScreen.this, RefreshSyncActivity.class));
        });

//        mapsLayout.setOnClickListener(view -> {
//            resetPrevRegData();
//            CommonConstants.REGISTRATION_SCREEN_FROM = CommonConstants.REGISTRATION_SCREEN_FROM_Viewonmaps;
//            startActivity(new Intent(HomeScreen.this, SearchFarmerScreen.class));
//        });
        mapsLayout.setOnClickListener(view -> {
            resetPrevRegData();
            CommonConstants.REGISTRATION_SCREEN_FROM = CommonConstants.REGISTRATION_SCREEN_FROM_Viewonmaps;
            startActivity(new Intent(HomeScreen.this, FiltermapsActivity.class));
        });
//        transportServiceLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                resetPrevRegData();
//                CommonConstants.REGISTRATION_SCREEN_FROM = CommonConstants.REGISTRATION_SCREEN_FROM_VISIT_REQUESTS;
//                startActivity(new Intent(HomeScreen.this, TransportActivity.class));
//            }
//        });

        krasRel.setOnClickListener(v -> startActivity(new Intent(HomeScreen.this, KrasDisplayScreen.class)));

        alertsRel.setOnClickListener(v -> {
          FragmentManager fm = getSupportFragmentManager();
            AlertsChooser alertsTypeChooser = new AlertsChooser();
            alertsTypeChooser.show(fm, "fragment_edit_name");
//                startActivity(new Intent(HomeScreen.this, NotificationsScreen.class));

        });
        plotSplitRel.setOnClickListener(view -> {
            CommonConstants.REGISTRATION_SCREEN_FROM = CommonConstants.REGISTRATION_SCREEN_FROM_PLOT_SPLIT;
            startActivity(new Intent(HomeScreen.this, SearchFarmerScreen.class));

        });
        extension_logbook.setOnClickListener(v -> {

            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= 29) {
                if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    startActivity(new Intent(HomeScreen.this, LogBookScreenActivity.class));

                }else{
                    UiUtils.showCustomToastMessage("Please Trun On GPS", getApplicationContext(), 1);

                }

            }else{
                if (CommonUtils.isLocationPermissionGranted(getApplicationContext())) {
                    startActivity(new Intent(HomeScreen.this, LogBookScreenActivity.class));
                }else {

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Log.v("@@@LLL", "Hello1");
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                        Manifest.permission.ACCESS_FINE_LOCATION},
                                100);

                        if (CommonUtils.isLocationPermissionGranted(getApplicationContext())) {
                            startActivity(new Intent(HomeScreen.this, LogBookScreenActivity.class));
                        }else {
                            UiUtils.showCustomToastMessage("Please Trun On GPS", getApplicationContext(), 1);

                        }

                    }
                }
            }

        });
    }


    //on Resume method
    @Override
    public void onResume() {
        super.onResume();
        List<String> userActivities = (List<String>) DataManager.getInstance().getDataFromManager(DataManager.USER_ACTIVITY_RIGHTS);
        if (null != userActivities && userActivities.contains(CommonConstants.CanManageFarmers)) {
            handleViewVisibilities(View.VISIBLE);
        } else if (null != userActivities && userActivities.contains(CommonConstants.CanViewFarmers)) {
            handleViewVisibilities(View.INVISIBLE);
            prospectiveFarmersRel.setVisibility(View.VISIBLE);
        } else {
            handleViewVisibilities(View.INVISIBLE);
        }

        String unreadNotificationsCount = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getUnreadNotificationsCountQuery());
        circleView.setTitleText(unreadNotificationsCount);

    }

    //Dialog for choosing type of registration
    public static class RegistrationTypeChooser extends DialogFragment {

        public RegistrationTypeChooser() {
            // Required empty public constructor
        }

        public static RegistrationTypeChooser newInstance(String type) {
            RegistrationTypeChooser fragment = new RegistrationTypeChooser();
            Bundle args = new Bundle();
            args.putString("type", "" + type);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.area_extension_choose_dialog, container);
            Rect displayRectangle = new Rect();
            Window window = getActivity().getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            view.setMinimumWidth((int) (displayRectangle.width() * 0.7f));

            Button newRegRel = (Button) view.findViewById(R.id.firstRel);
            newRegRel.setOnClickListener(view1 -> {
                resetPrevRegData();
                startActivity(new Intent(getActivity(), LocationSelectionScreen.class));
                getDialog().dismiss();
                CommonConstants.REGISTRATION_SCREEN_FROM = CommonConstants.REGISTRATION_SCREEN_FROM_NEW_FARMER;
            });

            Button newPlotRegRel = (Button) view.findViewById(R.id.secondRel);
            newPlotRegRel.setOnClickListener(view12 -> {
                resetPrevRegData();
                CommonConstants.REGISTRATION_SCREEN_FROM = CommonConstants.REGISTRATION_SCREEN_FROM_NEW_PLOT;
                startActivity(new Intent(getActivity(), SearchFarmerScreen.class));
                getDialog().dismiss();
            });


            Button followUpRegRel = (Button) view.findViewById(R.id.thirdRel);
            followUpRegRel.setOnClickListener(view13 -> {
                resetPrevRegData();
                CommonConstants.REGISTRATION_SCREEN_FROM = CommonConstants.REGISTRATION_SCREEN_FROM_FOLLOWUP;
                startActivity(new Intent(getActivity(), SearchFarmerScreen.class));
                getDialog().dismiss();
            });

            return view;
        }
    }

    //Dialog for choosing
    public static class AlertsChooser extends DialogFragment {
        public static final int TYPE_PLOT_FOLLOWUP = 1;
        public static final int TYPE_PLOT_VISITS = 2;
        public static final int TYPE_PLOT_MISSING_TREES = 3;
        public static final int TYPE_PLOT_NOT_VISIT = 4;
        public static final String ALERT_TYPE = "alert_type";
        public CircleView plotFollowUpCountTxt, visitsCountTxt, missingTreesCountTxt;

        public AlertsChooser() {
            // Required empty public constructor
        }

        public static AlertsChooser newInstance(String type) {
            AlertsChooser fragment = new AlertsChooser();
            Bundle args = new Bundle();
            args.putString("type", "" + type);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.alerts_layout, container);
            Rect displayRectangle = new Rect();
            Window window = getActivity().getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            view.setMinimumWidth((int) (displayRectangle.width() * 0.7f));

            DataAccessHandler dataAccessHandler = new DataAccessHandler(getActivity());
            String plotFollowupCount = dataAccessHandler.getOnlyOneValueFromDb(Queries.getInstance().getAlertsCount(AlertType.ALERT_PLOT_FOLLOWUP.getValue()));

//            plotFollowUpCountTxt = (CircleView) view.findViewById(R.id.countPlotTxt);
//            visitsCountTxt = (CircleView) view.findViewById(R.id.visitsCountPlotTxt);
//            missingTreesCountTxt = (CircleView) view.findViewById(R.id.missingTreesCountPlotTxt);

         //   plotFollowUpCountTxt.setTitleText(plotFollowupCount);
            Button pfuLayout = (Button) view.findViewById(R.id.pfuLayout);

            final Intent alertsIntent = new Intent(getActivity(), AlertsDisplayScreen.class);
            pfuLayout.setOnClickListener(view1 -> {
                alertsIntent.putExtra(ALERT_TYPE, TYPE_PLOT_FOLLOWUP);
                startActivity(alertsIntent);
                dismiss();
            });

            Button visitsRel = (Button) view.findViewById(R.id.visitsRel);
            visitsRel.setOnClickListener(view12 -> {
                alertsIntent.putExtra(ALERT_TYPE, TYPE_PLOT_VISITS);
                startActivity(alertsIntent);
                dismiss();
            });


            Button missingTreesRel = (Button) view.findViewById(R.id.missingTreesRel);
            missingTreesRel.setOnClickListener(view13 -> {
                alertsIntent.putExtra(ALERT_TYPE, TYPE_PLOT_MISSING_TREES);
                startActivity(alertsIntent);
                dismiss();
            });

            Button notVisitedPlots = view.findViewById(R.id.notVisitedPlots);
            notVisitedPlots.setOnClickListener(v -> {
                alertsIntent.putExtra(ALERT_TYPE, TYPE_PLOT_NOT_VISIT);
                startActivity(alertsIntent);
                dismiss();
            });

            return view;
        }
    }

    //Show/hide functionality
    public void handleViewVisibilities(int visibility) {
        refreshRel.setVisibility(visibility);
        cropMaintenanceRel.setVisibility(visibility);
        areaExtensionRel.setVisibility(visibility);
        conversionRel.setVisibility(visibility);
        prospectiveFarmersRel.setVisibility(visibility);
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.plots_display_screen, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== R.id.english){
            CommonUtils.changeLanguage(this, "en");
            // UiUtils.showCustomToastMessage("You Seleceted English",CollectionCenterHomeScreen.this,1);
        }

        if (item.getItemId() == R.id.hindi){
            CommonUtils.changeLanguage(this,"hi");

        }
        switch (item.getItemId()){

            case R.id.english:
                CommonUtils.changeLanguage(this, "en");
                break;
            case R.id.hindi:
                CommonUtils.changeLanguage(this, "hi");
                break;



        }
        Intent refreshIntent=new Intent(HomeScreen.this,HomeScreen.class);
        finish();
        startActivity(refreshIntent);

       *//* Intent refresh = new Intent(this, CollectionCenterHomeScreen.class);
        startActivity(refresh);//Start the same Activity*//*
        return super.onOptionsItemSelected(item);
    }*/

}


