package com.cis.palm360.FaLogTracking;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.cis.palm360.R;
import com.cis.palm360.areacalculator.LocationProvider;
import com.cis.palm360.cloudhelper.ApplicationThread;
import com.cis.palm360.common.CommonConstants;
import com.cis.palm360.common.CommonUtils;
import com.cis.palm360.database.DataAccessHandler;
import com.cis.palm360.database.Palm3FoilDatabase;
import com.cis.palm360.database.Queries;
import com.cis.palm360.datasync.helpers.DataSyncHelper;
import com.cis.palm360.dbmodels.UserDetails;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by BaliReddy on 10/12/2017.
 */

//Used to track location
public class FalogService extends Service implements LocationListener {

    private static final String LOG_TAG = "MyService";

    private static LocationProvider mLocationProvider;
    private static String latLong = "";
//    private final int INTERVAL = 5000;
//    private Timer timer = new Timer();

    PowerManager.WakeLock wakeLock;
    public Context context;
    double latitude, longitude;
    private Palm3FoilDatabase palm3FoilDatabase;
    private static final int MIN_UPDATE_TIME = 0;
    private static final int MIN_UPDATE_DISTANCE = 250;
    private Location location;

    public LocationManager locationManager;
    public String CreatedDate, UpdatedDate, ServerUpdatedStatus, CreatedByUserId, UpdatedByUserId, IsActive, IMEINumber;
    public String USER_ID_TRACKING;
    public static final String ACTION_LOCATION_UPDATED_TRACKING = "com.oilpalm3f.mainapp.falogService.location.updated";
    public static final String ACTION_START = "com.oilpalm3f.mainapp.falogService.start";
    public static final String TRACKING_LONGITUDE = "geo_longitude";
    public static final String TRACKING_LATITUDE = "geo_latitude";
    private DataAccessHandler dataAccessHandler = null;

    Calendar calendar = Calendar.getInstance();

    private static final float MAX_ACCURACY_THRESHOLD = 10.0f; // Adjust this threshold value as needed

    private static final double MINIMUM_MOVEMENT_SPEED = 1.0; // Adjust this threshold value as needed


    @Override
    public void onCreate() {
        super.onCreate();
//        palm3FoilDatabase = new Palm3FoilDatabase(this);
        Log.v(LOG_TAG, "Congrats! MyService Created");
//        Toast.makeText(this, "Congrats! MyService Created", Toast.LENGTH_LONG).show();
        Log.d(LOG_TAG, "onCreate");


    }

    //to get the location
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

    //to get latlongs of the location
    public String getLatLong(Context context, boolean showDialog) {

        mLocationProvider = getLocationProvider(context, showDialog);

        if (mLocationProvider != null) {
            latLong = mLocationProvider.getLatitudeLongitude();


        }
        return latLong;
    }

//starts location service
    public void startLocationService(ApplicationThread.OnComplete onComplete) {
        Log.d(LOG_TAG, "start location service");
        String providerType = null;
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            boolean gpsProviderEnabled = locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean networkProviderEnabled = locationManager != null && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (gpsProviderEnabled) {
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_UPDATE_TIME, MIN_UPDATE_DISTANCE, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    providerType = "gps";
                    com.cis.palm360.cloudhelper.Log.d(LOG_TAG, "gps lbs provider:" + (location == null ? "null" : String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude())));
                    //updateLocation(location);
                }
            }
            if (networkProviderEnabled) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_UPDATE_TIME, MIN_UPDATE_DISTANCE, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    providerType = "network";
                    com.cis.palm360.cloudhelper.Log.d(LOG_TAG, "network lbs provider:" + (location == null ? "null" : String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude())));
                    // updateLocation(location);
                }
            }


        } catch (Exception e) {
            Log.e(LOG_TAG, "Cannot get location", e);
        }

        if (onComplete != null) {
            onComplete.execute(location != null, location, providerType);
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    //start location command
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        IMEINumber = CommonUtils.getIMEInumber(this);
        context = getApplicationContext();
        com.cis.palm360.cloudhelper.Log.d(LOG_TAG, "start location service & location listener");
        // mahesh uncomented
        ApplicationThread.nuiPost(LOG_TAG, "start lococation service", new Runnable() {
            @Override
            public void run() {
                startLocationService(null);

            }
        });
        // end
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
            String channelName = "My Background Service";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            Notification notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.print_logo)
                    .setContentTitle("App is running in background")
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();
            startForeground(2, notification);

        } else {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("running service")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            Notification notification = builder.build();

            startForeground(1, notification);
        }
//        return START_NOT_STICKY;

        try {
            palm3FoilDatabase = Palm3FoilDatabase.getPalm3FoilDatabase(this);
            palm3FoilDatabase.createDataBase();
            dataAccessHandler = new DataAccessHandler(context);
        } catch (Exception e) {
            e.getMessage();
        }

        String query = Queries.getInstance().getUserDetailsNewQuery(CommonUtils.getIMEInumber(this));

        DataAccessHandler dataAccessHandler = new DataAccessHandler(this);
        final UserDetails userDetails = (UserDetails) dataAccessHandler.getUserDetails(query, 0);

        if (null != userDetails) {
            USER_ID_TRACKING = userDetails.getId();
            Log.v(LOG_TAG, "Start Service userId" + USER_ID_TRACKING);
        }

//        return super.onStartCommand(intent, flags, startId);
        return  START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "onDestroy");
        super.onDestroy();
    }


    @Override
    public void onLocationChanged(Location location) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("appprefs", MODE_PRIVATE);
        boolean isFreshInstall = sharedPreferences.getBoolean(CommonConstants.IS_FRESH_INSTALL, true);

        Date date = calendar.getTime();

        // Creating a SimpleDateFormat object with the desired format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Formatting the date to string
        String formattedDate = sdf.format(date);

        System.out.println("Formatted Date: " + formattedDate);

        if (location != null) {
            // Check if latitude and longitude are empty or null
            String selectedLatLong = dataAccessHandler.getFalogLatLongs(Queries.getInstance().queryVerifyFalogDistance(formattedDate));
            if (TextUtils.isEmpty(selectedLatLong)) {
                // No latitude and longitude entries found in the table, force insertion of a new record
                if (location.getAccuracy() <= MAX_ACCURACY_THRESHOLD){
                    processLocationUpdate(location);
                }else{
                    Log.d(LOG_TAG, "Initial Location - Device is stationary or accuracy is not within the threshold, ignoring location update");
                }

            }

            else {

                Log.d("LocationHasSpeed", location.hasSpeed()  + "");
                Log.d("LocationSpeed", location.getSpeed()  + "");
                Log.d("LocationAccuracy", location.getAccuracy()  + "");


                // Check if the device is moving based on speed
                if (location.hasSpeed() && location.getSpeed() >= MINIMUM_MOVEMENT_SPEED && location.getAccuracy() <= MAX_ACCURACY_THRESHOLD) {
                    // Device is moving and accuracy is within the threshold, process the location update
                    processLocationUpdate(location);
                } else {
                    // Device is either stationary or accuracy is not within the threshold, ignore the location update
                    Log.d(LOG_TAG, "Device is stationary or accuracy is not within the threshold, ignoring location update");
                }
            }
        }
    }

    private void processLocationUpdate(Location location) {

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        CommonConstants.Current_Latitude = latitude;
        CommonConstants.Current_Longitude = longitude;

        CreatedDate = CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS_SSS);
        UpdatedDate = CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS_SSS);
        ServerUpdatedStatus = CommonConstants.ServerUpdatedStatus;
        CreatedByUserId = USER_ID_TRACKING + "";
        UpdatedByUserId = USER_ID_TRACKING + "";

        IsActive = "1";

        Date date = calendar.getTime();

        // Creating a SimpleDateFormat object with the desired format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Formatting the date to string
        String formattedDate = sdf.format(date);

        System.out.println("Formatted Date: " + formattedDate);

        String selectedLatLong = dataAccessHandler.getFalogLatLongs(Queries.getInstance().queryVerifyFalogDistance(formattedDate));
        if (!TextUtils.isEmpty(selectedLatLong)) {
            Log.v(LOG_TAG, "@@@@ data " + selectedLatLong);
            double actualDistance = 0;
            String[] yieldDataArr = selectedLatLong.split("-");

            if (yieldDataArr.length > 0 && !TextUtils.isEmpty(yieldDataArr[0]) && !TextUtils.isEmpty(yieldDataArr[1])) {

                actualDistance = CommonUtils.distance(latitude, longitude,
                        Double.parseDouble(yieldDataArr[0]),
                        Double.parseDouble(yieldDataArr[1]), 'm');

            }

            Log.v(LOG_TAG, "@@@@ actual distance " + actualDistance);

            if (actualDistance >= 250) {

                if (Integer.parseInt(CreatedByUserId) != 12345 && Integer.parseInt(UpdatedByUserId) != 12345) {
                    palm3FoilDatabase.insertLatLong(latitude, longitude, IsActive, CreatedByUserId, CreatedDate, UpdatedByUserId, UpdatedDate, IMEINumber, ServerUpdatedStatus);
                }

                DataSyncHelper.sendTrackingData(context, new ApplicationThread.OnComplete() {
                    @Override
                    public void execute(boolean success, Object result, String msg) {
                        if (success) {
                            Log.v(LOG_TAG, "sent success");
                        } else {
                            Log.e(LOG_TAG, "sent failed");
                        }
                    }
                });
            } else {
                // Handle case where distance is less than 50
            }
        } else {
            if (Integer.parseInt(CreatedByUserId) != 12345 && Integer.parseInt(UpdatedByUserId) != 12345) {
                palm3FoilDatabase.insertLatLong(latitude, longitude, IsActive, CreatedByUserId, CreatedDate, UpdatedByUserId, UpdatedDate, IMEINumber, ServerUpdatedStatus);
            }

            DataSyncHelper.sendTrackingData(context, new ApplicationThread.OnComplete() {
                @Override
                public void execute(boolean success, Object result, String msg) {
                    if (success) {
                        Log.v(LOG_TAG, "sent success");
                    } else {
                        Log.e(LOG_TAG, "sent failed");
                    }
                }
            });
        }
    }

    //What should happen on location changed
//    @Override
//    public void onLocationChanged(Location location) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences("appprefs", MODE_PRIVATE);
//        boolean isFreshInstall = sharedPreferences.getBoolean(CommonConstants.IS_FRESH_INSTALL, true);
//
//        if (location != null) {
//
//            String latlong[] = getLatLong(FalogService.this, false).split("@");
//
////            Toast.makeText(getApplicationContext(), "location "+ String.valueOf(location.getLatitude()) + "/" + String.valueOf(location.getLongitude()), Toast.LENGTH_SHORT).show();
//
//            Log.d(LOG_TAG, "updateTracking location:" + String.valueOf(location.getLatitude()) + "/" + String.valueOf(location.getLongitude()));
//            latitude = Double.parseDouble(latlong[0]);
//            longitude = Double.parseDouble(latlong[1]);
//            CommonConstants.Current_Latitude = latitude;
//            CommonConstants.Current_Longitude = longitude;
//
//
//            CreatedDate = CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS_SSS);
//            UpdatedDate = CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS_SSS);
//            ServerUpdatedStatus = CommonConstants.ServerUpdatedStatus;
//            CreatedByUserId = USER_ID_TRACKING;
//            UpdatedByUserId = USER_ID_TRACKING;
//
//            IsActive = "1";
//
//            String selectedLatLong = dataAccessHandler.getFalogLatLongs(Queries.getInstance().queryVerifyFalogDistance());
//            if (!TextUtils.isEmpty(selectedLatLong)) {
//                Log.v(LOG_TAG, "@@@@ data " + selectedLatLong);
//                double actualDistance = 0;
//                String[] yieldDataArr = selectedLatLong.split("-");
//
//                if (yieldDataArr.length > 0 && !TextUtils.isEmpty(yieldDataArr[0]) && !TextUtils.isEmpty(yieldDataArr[1])) {
//
//                    actualDistance = CommonUtils.distance(latitude, longitude,
//                            Double.parseDouble(yieldDataArr[0]),
//                            Double.parseDouble(yieldDataArr[1]), 'm');
//
//                }
//
//                Log.v(LOG_TAG, "@@@@ actual distance " + actualDistance);
//
//                if (actualDistance >= 250) {
//
//                    if(Integer.parseInt(CreatedByUserId) != 12345 && Integer.parseInt(UpdatedByUserId) != 12345) {
//                        palm3FoilDatabase.insertLatLong(latitude, longitude, IsActive, CreatedByUserId, CreatedDate, UpdatedByUserId, UpdatedDate, IMEINumber, ServerUpdatedStatus);
//                    }
//
//                    DataSyncHelper.sendTrackingData(context, new ApplicationThread.OnComplete() {
//                        @Override
//                        public void execute(boolean success, Object result, String msg) {
//                            if (success) {
//                                Log.v(LOG_TAG, "sent success");
//                            } else {
//                                Log.e(LOG_TAG, "sent failed");
//                            }
//                        }
//                    });
//                } else {
//
////                    UiUtils.showCustomToastMessage("plz wiat for 250M", context, 0);
//
//                }
//            } else {
//                if(Integer.parseInt(CreatedByUserId) != 12345 && Integer.parseInt(UpdatedByUserId) != 12345) {
//                    palm3FoilDatabase.insertLatLong(latitude, longitude, IsActive, CreatedByUserId, CreatedDate, UpdatedByUserId, UpdatedDate, IMEINumber, ServerUpdatedStatus);
//                }
//
//                DataSyncHelper.sendTrackingData(context, new ApplicationThread.OnComplete() {
//                    @Override
//                    public void execute(boolean success, Object result, String msg) {
//                        if (success) {
//                            com.oilpalm3f.mainapp.cloudhelper.Log.v(LOG_TAG, "sent success");
//                        } else {
//                            com.oilpalm3f.mainapp.cloudhelper.Log.e(LOG_TAG, "sent failed");
//                        }
//                    }
//                });
//            }
//
//
//        }
//
//
//    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
