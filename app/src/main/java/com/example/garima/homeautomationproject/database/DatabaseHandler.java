package com.example.garima.homeautomationproject.database;


import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.garima.homeautomationproject.model.HomeModel;
import com.example.garima.homeautomationproject.model.RoomModel;
import com.example.garima.homeautomationproject.model.DeviceModel;
import com.example.garima.homeautomationproject.model.SwitchModel;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHandler.class.getSimpleName();
    // Database Version
    private static final int DATABASE_VERSION = 10;

    // Database Name
    public static final String DATABASE_NAME = "HomeAutomationDataBase";
    public static final String TABLE_ROOM = "Room";
    public static final String TABLE_HOME= "HOME";
    public static final String TABLE_DEVICE = "Device";
    public static final String TABLE_SWITCH = "Switch";
    public static final String KEY_ROOMNAME = "RoomName";
    public static final String KEY_DEVICENAME = "deviceName";
    public static final String KEY_DEVICECODE = "deviceCode";
    public static final String KEY_SWITCHNAME = "switchName";
    public static final String KEY_SWITCHTYPE = "switchType";
    public static final String KEY_SWITCHSTATE= "switchState";
    public static final String KEY_DEVICETYPE= "deviceType";
    public static final String KEY_HOMENAME= "homeName";


    public DatabaseHandler(Context context) {
        super(context, "/mnt/sdcard/HomeAutomationDataBase.db", null, DATABASE_VERSION);

        Log.v(TAG, "Databaser object created");
    }

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_HOME = "CREATE TABLE " + TABLE_HOME + "("
                + KEY_HOMENAME + " text" + ");";

        String CREATE_TABLE_ROOM = "CREATE TABLE " + TABLE_ROOM + "("+ KEY_HOMENAME + " text," +
                 KEY_ROOMNAME + " text" + ");";

        String CREATE_TABLE_DEVICE = "CREATE TABLE " + TABLE_DEVICE + "("+ KEY_HOMENAME + " text,"
                + KEY_ROOMNAME + " text," + KEY_DEVICENAME + " text," + KEY_DEVICETYPE + " text," + KEY_DEVICECODE + " text " + ");";


        String CREATE_TABLE_SWITCH= "CREATE TABLE " + TABLE_SWITCH + "("+ KEY_HOMENAME + " text,"
                + KEY_ROOMNAME + " text," + KEY_DEVICETYPE + " text,"+KEY_DEVICENAME + " text," + KEY_DEVICECODE + " text ," + KEY_SWITCHNAME + " text ,"+KEY_SWITCHSTATE + " text , " + KEY_SWITCHTYPE+ " text " +");";


        db.execSQL(CREATE_TABLE_HOME);
        db.execSQL(CREATE_TABLE_ROOM);
        db.execSQL(CREATE_TABLE_DEVICE);
        db.execSQL(CREATE_TABLE_SWITCH);


        Log.v(TAG, "Database table created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROOM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SWITCH);
        onCreate(db);
    }
         /*Room Curd's*/
    public List<RoomModel> getRoomList(String homeName) {
        ArrayList<RoomModel> list = new ArrayList<RoomModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_ROOM +" WHERE " + KEY_HOMENAME + " =?";
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, new String[]{homeName});
            try {
                if (cursor.moveToFirst()) {
                    do {
                        RoomModel data = new RoomModel();
                        data.setHomeName(cursor.getString(0));
                        data.setRoomName(cursor.getString(1));
                        list.add(data);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }

        return list;

    }
    public void deleteRoom(String roomName,String homeName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ROOM,KEY_HOMENAME + "=? AND " + KEY_ROOMNAME + "=?",
                new String[] { homeName,roomName});
        db.close();
    }
    public void addRoom(RoomModel roomModel) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_ROOMNAME, roomModel.getRoomName());
            values.put(KEY_HOMENAME, roomModel.getHomeName());
            db.insert(TABLE_ROOM, null, values);
            Log.v(TAG, "Database insert taxi Lat Long data table");
            db.close();
        }catch (Exception e)
        {
            Log.e("Execp in add Room",e.getMessage());
        }

    }
    public int updateRoomName(String prevRoom,String newRoom,String homeName) {
        int result =0 ;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_ROOMNAME, newRoom);
            result = db.update(TABLE_ROOM, values, KEY_ROOMNAME + " = ? AND " + KEY_HOMENAME + "=?",
                    new String[]{prevRoom, homeName});
            return result;

        } catch (Exception e) {
            Log.e("Execp in  update Room", e.getMessage());
        }
       return result;
    }
    public boolean checkRoomAlreadyExist(String roomName,String homeName) {
        String query = "SELECT " + KEY_ROOMNAME + " FROM " + TABLE_ROOM + " WHERE " + KEY_ROOMNAME + " =?"+" AND "+ KEY_HOMENAME + " =?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{roomName,homeName});
        if (cursor.getCount() > 0) {
            return false;
        } else
            return true;
    }




/*-----------------------------------------Device Curd  ----------------------------------------*/
    public List<DeviceModel> getDeviceList(String roomName,String homeName) {
        ArrayList<DeviceModel> list = new ArrayList<DeviceModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_DEVICE + " WHERE " + KEY_ROOMNAME + "=?" + " AND "+  KEY_HOMENAME + "=?";
        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery(selectQuery, new String[]{roomName,homeName});
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        DeviceModel data = new DeviceModel();
                        //only one column
                        data.setHomeName(cursor.getString(0));
                        data.setRoomName(cursor.getString(1));
                        data.setDeviceName(cursor.getString(2));
                        data.setDeviceType(cursor.getString(3));
                        data.setDeviceCode(cursor.getString(4));

                        list.add(data);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }

        return list;

    }
    public void addDevice(DeviceModel deviceModel) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_ROOMNAME, deviceModel.getRoomName());
            values.put(KEY_HOMENAME, deviceModel.getHomeName());
            values.put(KEY_DEVICENAME, deviceModel.getDeviceName());
            values.put(KEY_DEVICETYPE, deviceModel.getDeviceType());
            values.put(KEY_DEVICECODE, deviceModel.getDeviceCode());

            db.insert(TABLE_DEVICE, null, values);
            db.close();
        }catch (Exception e)
        {
            Log.e("Excep in add Device",e.getMessage());
        }

    }
    public boolean checkDeviceAlreadyExist(String deviceCode,String homeName,String roomName) {
        String query = "SELECT " + KEY_DEVICECODE + " FROM " + TABLE_DEVICE + " WHERE " + KEY_DEVICECODE + " =?" + " AND "+ KEY_HOMENAME + " =?" +" AND "+ KEY_ROOMNAME+ " =?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{deviceCode,homeName,roomName});
        if (cursor.getCount() > 0) {
            return false;
        } else
            return true;
    }
    public int updateDevice(String pervDeviceCode,DeviceModel deviceModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DEVICECODE, deviceModel.getDeviceCode());

        return db.update(TABLE_DEVICE, values, KEY_DEVICECODE + " = ?" +" AND " +KEY_ROOMNAME + " = ?"+" AND "+KEY_HOMENAME+ " = ?",
                new String[] {pervDeviceCode,deviceModel.getRoomName(),deviceModel.getHomeName()});


    }

    public void deleteDevice(DeviceModel  deviceModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DEVICE, KEY_DEVICECODE + " = ?"+" AND " +KEY_ROOMNAME + " = ?"+" AND "+KEY_HOMENAME+ " = ?",
                new String[] { deviceModel.getDeviceCode(),deviceModel.getRoomName(),deviceModel.getHomeName()});
        db.close();
    }
/*-----------------------------------------Switch's Crud ----------------------*/

    public ArrayList<SwitchModel> getSwitchList(String deviceCode,String homeName,String roomName) {
        ArrayList<SwitchModel> list = new ArrayList<SwitchModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_SWITCH + " WHERE " + KEY_DEVICECODE + "=?"+" AND "+  KEY_HOMENAME + "=?"+" AND " + KEY_ROOMNAME + "=?";
        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery(selectQuery, new String[]{deviceCode,homeName,roomName});
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        SwitchModel data = new SwitchModel();
                        //only one column
                        data.setHomeName(cursor.getString(0));
                        data.setRoomName(cursor.getString(1));
                        data.setDeviceType(cursor.getString(2));
                        data.setDeviceName(cursor.getString(3));
                        data.setDeviceCode(cursor.getString(4));
                        data.setSwitchName(cursor.getString(5));
                        data.setSwitchState(cursor.getString(6));
                        data.setSwitchType(cursor.getString(7));
                        list.add(data);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }

        return list;

    }







    public void addSwitch(SwitchModel switchModel) {
        try {


            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_HOMENAME, switchModel.getHomeName());
            values.put(KEY_ROOMNAME, switchModel.getRoomName());
            values.put(KEY_DEVICENAME, switchModel.getDeviceName());
            values.put(KEY_DEVICETYPE, switchModel.getDeviceType());
            values.put(KEY_DEVICECODE, switchModel.getDeviceCode());
            values.put(KEY_SWITCHNAME, switchModel.getSwitchName());
            values.put(KEY_SWITCHSTATE, switchModel.getSwitchState());
            values.put(KEY_SWITCHTYPE, switchModel.getSwitchType());


            // Inserting Row
            db.insert(TABLE_SWITCH, null, values);
          //  Log.v(TAG, "Databaser insert TABLE_SWITCH");

            db.close();
        }catch(Exception e)
        {
            Log.e("Excep in switch insert",e.getMessage());
        }
    // Closing database connection
    }
    public int updateSwitch(String newSwitchName,SwitchModel switchModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SWITCHNAME, newSwitchName);
        return db.update(TABLE_SWITCH, values, KEY_SWITCHNAME + " = ?"+" AND "+KEY_ROOMNAME + " = ?"+" AND "+KEY_HOMENAME + " = ?",
                new String[] {switchModel.getSwitchName(),switchModel.getRoomName(),switchModel.getHomeName()});
    }
    public void deleteSwitch(SwitchModel  switchModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SWITCH, KEY_SWITCHNAME + " = ?"+" AND "+KEY_ROOMNAME + " = ?"+" AND "+KEY_HOMENAME + " = ?",
                new String[] { switchModel.getSwitchName(),switchModel.getRoomName(),switchModel.getHomeName()});
        db.close();
    }


    public boolean checkSwitchNameExist(String switchName,String homeName,String roomName,String deviceCode) {
        String query = "SELECT " + KEY_SWITCHNAME + " FROM " + TABLE_SWITCH + " WHERE " + KEY_SWITCHNAME + " =?"+ "AND " + KEY_ROOMNAME+ " =?"+"AND " + KEY_HOMENAME+ " =?"+"AND " + KEY_DEVICECODE+ " =?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{switchName,roomName,homeName,deviceCode});
        if (cursor.getCount() > 0) {
            return false;
        } else
            return true;
    }



    /*---------------------------------------------Home Curd-----------------------------------*/
    public void addHome(HomeModel homeModel)
    {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_HOMENAME, homeModel.getHomeName());
            db.insert(TABLE_HOME, null, values);
            Log.v(TAG, "Database insert taxi Lat Long data table");
            db.close();

        }catch (Exception e)
        {
            Log.e("Exception is Home",e.getMessage());
        }

    }
    public boolean checkHomeNameAlreadyExist(String homeName) {
        String query = "SELECT " + KEY_HOMENAME + " FROM " + TABLE_HOME + " WHERE " + KEY_HOMENAME + " =?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{homeName});
        if (cursor.getCount() > 0) {
            return false;
        } else
            return true;
    }

    public int updateHomeName(String prevHomeName,String newHomeName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_HOMENAME, newHomeName);
        return db.update(TABLE_HOME, values, KEY_HOMENAME + " = ?",
                new String[] {prevHomeName});


    }
    public void deleteHome(String  homeName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HOME, KEY_HOMENAME + " = ?",
                new String[] { homeName});
        db.close();
    }
    public ArrayList<HomeModel> getHomeList() {
        ArrayList<HomeModel> list = new ArrayList<HomeModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_HOME ;
        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery(selectQuery, new String[]{});
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        HomeModel data = new HomeModel();
                        //only one column
                        data.setHomeName(cursor.getString(0));
                        list.add(data);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }

        return list;

    }


}