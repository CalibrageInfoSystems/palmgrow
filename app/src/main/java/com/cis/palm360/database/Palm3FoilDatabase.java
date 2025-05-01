package com.cis.palm360.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cis.palm360.common.CommonConstants;
import com.cis.palm360.common.CommonUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * Created by skasam on 1/3/2017.
 */

//Database creation, copying will be done
public class Palm3FoilDatabase extends SQLiteOpenHelper {

    public static final String LOG_TAG = Palm3FoilDatabase.class.getName();
    public final static int DATA_VERSION = 37; // Changed on 12th Nov 2024
    private final static String DATABASE_NAME = "3foilpalm.sqlite";
    public static final String Lock = "dblock";

    private static Palm3FoilDatabase palm3FoilDatabase;
    private static SQLiteDatabase mSqLiteDatabase = null;
    private static String DB_PATH;
    private static String FULL_DB_PATH;

    private final Context mContext;

    public Palm3FoilDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATA_VERSION);
        this.mContext = context;

        File dbDirectory = new File(CommonUtils.get3FFileRootPath() + "3F_Database");
        if (!dbDirectory.exists()) {
            boolean created = dbDirectory.mkdirs();
            Log.d(LOG_TAG, "DB directory created: " + created);
        }

        DB_PATH = dbDirectory.getAbsolutePath() + File.separator;
        FULL_DB_PATH = DB_PATH + DATABASE_NAME;

        Log.v("The Database Path", FULL_DB_PATH);
    }

    public static synchronized Palm3FoilDatabase getPalm3FoilDatabase(Context context) {
        synchronized (Lock) {
            if (palm3FoilDatabase == null) {
                palm3FoilDatabase = new Palm3FoilDatabase(context);
            }
            return palm3FoilDatabase;
        }
    }

    public static SQLiteDatabase openDataBaseNew() throws SQLException {
        if (mSqLiteDatabase == null || !mSqLiteDatabase.isOpen()) {
            mSqLiteDatabase = SQLiteDatabase.openDatabase(FULL_DB_PATH, null,
                    SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.CREATE_IF_NECESSARY | SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        }
        return mSqLiteDatabase;
    }

    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public void createDataBase() throws IOException {
        if (!checkDataBase()) {
            try {
                copyDataBase(); // You may need to define this or clarify if you're copying from assets
                Log.v("dbcopied:::", "true");
            } catch (Exception e) {
                e.printStackTrace();
                throw new Error("Error copying database");
            }

            try {
                openDataBaseNew();
            } catch (Exception e) {
                e.printStackTrace();
                throw new Error("Error opening database");
            }
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            Log.e("DB Check Path", FULL_DB_PATH);
            checkDB = SQLiteDatabase.openDatabase(FULL_DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            Log.w(LOG_TAG, "Database does not exist yet.");
        }

        if (checkDB != null) {
            checkDB.close();
            return true;
        }

        return false;
    }




    /*  private boolean checkDataBase() {
        boolean dataBaseExisted = false;
        try {
            String check_Path = DB_PATH + DATABASE_NAME;
            mSqLiteDatabase = SQLiteDatabase.openDatabase(check_Path, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (Exception ex) {
            // TODO: handle exception
            ex.printStackTrace();
        }
        return mSqLiteDatabase != null;
    }
*/
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    private void copyDataBase() throws IOException {
        File dbDir = new File(DB_PATH);
        if (!dbDir.exists()) {
            dbDir.mkdir();

        }
        InputStream myInput = mContext.getAssets().open(DATABASE_NAME);
        OutputStream myOutput = new FileOutputStream(DB_PATH + DATABASE_NAME);
        copyFile(myInput, myOutput);

    }

    public boolean UpdateGeoTagLatLng(String UpdatedByUserId,  String UpdatedDate,double Latitude,double Longitude) {
//        SQLiteDatabase db = this.getWritableDatabase();

        try {
            openDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("UpdatedByUserId",UpdatedByUserId);
            contentValues.put("UpdatedDate",UpdatedDate);
            contentValues.put("Latitude",Latitude);
            contentValues.put("Longitude",Longitude);
            contentValues.put("ServerUpdatedStatus",0);

            mSqLiteDatabase.update(DatabaseKeys.TABLE_GEOBOUNDARIES, contentValues,
                    "PlotCode" + "=?" + " and "  +
                            "GeoCategoryTypeId" + "=?",
                    new String[] {
                            CommonConstants.PLOT_CODE,
                            "207"}) ;
//            mSqLiteDatabase.update(DatabaseKeys.TABLE_GEOBOUNDARIES, contentValues, " PlotCode = " + CommonConstants.PLOT_CODE + " AND " + " GeoCategoryTypeId = 207 ", null);
//            mSqLiteDatabase.insert(DatabaseKeys.TABLE_Location_TRACKING_DETAILS, null, contentValues);
            Log.v("userdata","data for user"+contentValues);

        }catch (Exception e)
        {
            Log.v("ReTagDeoTag","Data Update failed due to"+e);
        }
        return true;
    }

    public void UpdateFarmerhistory(String UpdatedByUserId, String UpdatedDate, String IsActive) {
//        SQLiteDatabase db = this.getWritableDatabase();

        try {
            openDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("UpdatedByUserId",UpdatedByUserId);
            contentValues.put("UpdatedDate",UpdatedDate);
            contentValues.put("IsActive",IsActive);

            mSqLiteDatabase.update("FarmerHistory", contentValues, "PlotCode = " + CommonConstants.PLOT_CODE + " AND IsActive = 1", null);
//            mSqLiteDatabase.insert(DatabaseKeys.TABLE_Location_TRACKING_DETAILS, null, contentValues);
            Log.v("userdata","data for user"+contentValues);
        }catch (Exception e){
            Log.v("Farmer History","Data Update failed due to"+e);
        }

    }

    public void insertErrorLogs(String tableName, String error){
        try {
            openDatabase();
            ContentValues errorValues = new ContentValues();
            errorValues.put("TableName",tableName);
            errorValues.put("Error",error);
            errorValues.put("CreatedDate",CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
            mSqLiteDatabase.insert("ErrorLogs",null,errorValues);
            Log.v("Error Details","Error Details inserted ");
        }catch (SQLiteException se){
            Log.v("Error Details","Error Details failed to Inserted "+se);
            se.printStackTrace();
        }

    }

    //FLOG_TRACKING......
    public boolean insertLatLong (double Latitude, double Longitude,String IsActive,String CreatedByUserId, String CreatedDate,String UpdatedByUserId,  String UpdatedDate, String IMEINumber, String ServerUpdatedStatus) {
//        SQLiteDatabase db = this.getWritableDatabase();

        try {
            openDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("UserId",CreatedByUserId);
            contentValues.put(DatabaseKeys.LATITUDE,Latitude);
            contentValues.put(DatabaseKeys.LONGITUDE,Longitude);
            contentValues.put("Address", "Testing");
            contentValues.put("LogDate",UpdatedDate);
            contentValues.put("ServerUpdatedStatus",0);

            mSqLiteDatabase.insert(DatabaseKeys.TABLE_Location_TRACKING_DETAILS, null, contentValues);
            Log.v("userdata","data for user"+contentValues);
        }catch (Exception e){
            Log.v("UserDetails","Data insert failed due to"+e);
        }

        return true;
    }
    public boolean insertLogDetails(String ClientName,String MobileNumber,String Location,String Details,float Latitude,float Longitude,String ServerUpdatedStatus ){
        try {
            openDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("ClientName",ClientName);
            contentValues.put("MobileNumber", MobileNumber);
            contentValues.put("Location",Location);
            contentValues.put("Details",Details);
            contentValues.put("Latitude",Latitude);
            contentValues.put("Longitude",Longitude);
            contentValues.put("CreatedByUserId",CommonConstants.USER_ID);
            contentValues.put("CreatedDate",CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
            contentValues.put("ServerUpdatedStatus",0);

            mSqLiteDatabase.insert(DatabaseKeys.TABLE_VisitLog, null, contentValues);
            Log.v("logdetails","Log Detaails are inserted sucessfully"+contentValues);
        }catch (Exception e){
            Log.v("logdetails","Log Detaails are not inserted"+e);
        }
        return true;
    }
    public boolean insertHarvestorVisitHistory(String Code,String Name,String PlotCode){
        try {
            openDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("Code",Code);
            contentValues.put("Name", Name);
            contentValues.put("PlotCode",PlotCode);
            contentValues.put("CreatedByUserId",CommonConstants.USER_ID);
            contentValues.put("CreatedDate",CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
            contentValues.put("ServerUpdatedStatus",0);

            mSqLiteDatabase.insert(DatabaseKeys.TABLE_HarvestorVisitHistory, null, contentValues);
            Log.v("logdetails","TABLE_HarvestorVisitHistory are inserted sucessfully"+contentValues);
        }catch (Exception e){
            Log.v("logdetails","TABLE_HarvestorVisitHistory are not inserted"+e);
        }
        return true;
    }

    public boolean insertHarvestorVisitDetails(String HarvestorVisitCode,int HarvestorTypeId,int HarvestorId,boolean HasPole,int Interval,Double Quantity,String CCCode,double UnRipen,
         double UnderRipe,  double Ripen, double OverRipe, double Diseased,double EmptyBunches ,boolean FarmerAvailable,String Name,String MobileNumber,
                                               String Village,String Mandal



    ){
        try {
            openDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("HarvestorVisitCode",HarvestorVisitCode);
            contentValues.put("HarvestorTypeId", HarvestorTypeId);
            contentValues.put("HarvestorId",HarvestorId);
            contentValues.put("HasPole",HasPole);
//            contentValues.put("TonnageCost",TonnageCost);
            contentValues.put("Interval",Interval);
            contentValues.put("Quantity",Quantity);
            contentValues.put("CCCode",CCCode);
            contentValues.put("UnRipen",UnRipen);
            contentValues.put("UnderRipe",UnderRipe);
            contentValues.put("Ripen",Ripen);
            contentValues.put("OverRipe",OverRipe);
            contentValues.put("Diseased",Diseased);
            contentValues.put("Diseased",Diseased);
            contentValues.put("EmptyBunches",EmptyBunches);
//            contentValues.put("FFBQualityLong",FFBQualityLong);
//            contentValues.put("FFBQualityMedium",FFBQualityMedium);
//            contentValues.put("FFBQualityShort",FFBQualityShort);
//            contentValues.put("FFBQualityOptimum",FFBQualityOptimum);
            contentValues.put("FarmerAvailable",FarmerAvailable);
//            contentValues.put("DetailesInformed",DetailesInformed);
            contentValues.put("Name",Name);
            contentValues.put("MobileNumber",MobileNumber);
            contentValues.put("Village",Village);
            contentValues.put("Mandal",Mandal);
            contentValues.put("CreatedByUserId",CommonConstants.USER_ID);
            contentValues.put("CreatedDate",CommonUtils.getcurrentDateTime(CommonConstants.DATE_FORMAT_DDMMYYYY_HHMMSS));
            contentValues.put("ServerUpdatedStatus",0);

            mSqLiteDatabase.insert(DatabaseKeys.TABLE_HarvestorVisitDetails, null, contentValues);
            Log.v("logdetails","Log TABLE_HarvestorVisitDetails are inserted sucessfully"+contentValues);
        }catch (Exception e){
            Log.v("logdetails","Log TABLE_HarvestorVisitDetails are not inserted"+e);
        }
        return true;
    }

    /* Open the database */
    public void openDataBase() throws SQLException {

        String check_Path = DB_PATH + DATABASE_NAME;
        if (mSqLiteDatabase != null) {
            mSqLiteDatabase.close();
            mSqLiteDatabase = null;
            mSqLiteDatabase = SQLiteDatabase.openDatabase(check_Path, null, SQLiteDatabase.OPEN_READWRITE);
        } else {
            mSqLiteDatabase = SQLiteDatabase.openDatabase(check_Path, null, SQLiteDatabase.OPEN_READWRITE);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(DATABASE_NAME).getPath();
        if(mSqLiteDatabase != null && mSqLiteDatabase.isOpen()) {
            return;
        }
        mSqLiteDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }
}
