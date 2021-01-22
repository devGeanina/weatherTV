package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class WeatherDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weather.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "weatherCity";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CITY = "cityName";
   // public static final String COLUMN_IP = "ip";

    public WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        COLUMN_CITY + " TEXT NOT NULL " +

                    //    COLUMN_IP + " INTEGER NOT NULL " +
                        ");";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addCity(String city){
        ContentValues values = new ContentValues();
        values.put(COLUMN_CITY, city);
       // values.put(COLUMN_IP, );
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //print the database
    // 1 - select every row
    public String databaseToString(){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME+ " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        //first row in the results
        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("cityName"))!=null){
                dbString += c.getString(c.getColumnIndex("cityName"));
                dbString += "\n";
            }
        }
        db.close();
        return dbString;
    }
}
