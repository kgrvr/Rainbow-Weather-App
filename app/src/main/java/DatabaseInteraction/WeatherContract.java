package DatabaseInteraction;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by Kush on 16-07-2016.
 */
public class WeatherContract extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "weather_db";
    public static final int DATABASE_VERSION = 1;

    public WeatherContract(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public long normalizeDate(long startDate) {
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static final class LocationEntry implements BaseColumns {

        public static final String TABLE_NAME = "location";
        public static final String C_LOCATION_SETTING = "location_setting";
        public static final String C_CITY_NAME = "city_name";
        public static final String C_lON = "longitude";
        public static final String C_LAT = "latitude";

    }

    public static final class CurrentEntry implements BaseColumns {

        public static final String TABLE_NAME = "current";
        public static final String C_LOCATION_KEY = "location_key";
        public static final String C_DATE = "date";
        public static final String C_WEATHER_ID = "weather_id";
        public static final String C_SHORT_DESC = "short_desc";
        public static final String C_MIN_TEMP = "min";
        public static final String C_MAX_TEMP = "max";
        public static final String C_HUMIDITY = "humidity";
        public static final String C_PRESSURE = "pressure";
        public static final String C_WIND_SPEED = "wind_speed";
        public static final String C_DEGREES = "degrees";

    }

    public static final class ForecastEntry implements BaseColumns {

        public static final String TABLE_NAME = "forecast";
        public static final String C_LOCATION_KEY = "location_key";
        public static final String C_DATE = "date";
        public static final String C_WEATHER_ID = "weather_id";
        public static final String C_SHORT_DESC = "short_desc";
        public static final String C_MIN_TEMP = "min";
        public static final String C_MAX_TEMP = "max";
        public static final String C_HUMIDITY = "humidity";
        public static final String C_PRESSURE = "pressure";
        public static final String C_WIND_SPEED = "wind_speed";
        public static final String C_DEGREES = "degrees";

    }

}
