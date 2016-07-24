package com.itskush.kushal.sunshine;

import android.text.format.Time;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * Created by Kush on 15-07-2016.
 */
public class JSONParsing {

    private String cityName = null, cityLongitude= null, cityLatitude = null;

    private String day, description, highAndLow, currentMain;

    private long roundedHigh, roundedLow, currentTemp, currentHumidity, currentWindSpeed, currentPressure, currentWindDeg;

    JSONParsing() {}

    public String getCityName() {
        return cityName;
    }

    public String getCityLongitude() {
        return cityLongitude;
    }

    public String getCityLatitude() {
        return cityLatitude;
    }

    public String getDay() {
        return day;
    }

    public String getDescription() {
        return description;
    }

    public String getHighAndLow() {
        return highAndLow;
    }

    public long getRoundedHigh() {
        return roundedHigh;
    }

    public long getRoundedLow() {
        return roundedLow;
    }

    public String getCurrentMain() {
        return currentMain;
//        return "Moon";
    }

    public long getCurrentTemp() {
        return currentTemp;
    }

    public long getCurrentHumidity() {
        return currentHumidity;
    }

    public long getCurrentWindSpeed() {
        return currentWindSpeed;
    }

    public long getCurrentPressure() {
        return currentPressure;
        //return 90000;
    }

    public String getCurrentWindDeg() {
        int val = (int) ((currentWindDeg/22.5)+.5);
        String[] direction= {"N","NNE","NE","ENE","E","ESE", "SE", "SSE","S","SSW","SW","WSW","W","WNW","NW","NNW"};
        return direction[val%16];
    }

    /* The date/time conversion code is going to be moved outside the asynctask later,
             * so for convenience we're breaking it out into its own method now.
             */
    private String getReadableDateString(long time){
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
        return shortenedDateFormat.format(time);
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    boolean check = true;
    private String formatHighLows(double high, double low) {
        // For presentation, assume the user doesn't care about tenths of a degree.
        if(check) {
            roundedHigh = Math.round(high);
            roundedLow = Math.round(low);
            check = false;
        }
        String highLowStr = Math.round(high) + "/" + Math.round(low);
        return highLowStr;
    }

    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    public String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String OWN_CITY = "city";
        final String OWM_LIST = "list";
        final String OWM_WEATHER = "weather";
        final String OWM_TEMPERATURE = "temp";
        final String OWM_MAX = "max";
        final String OWM_MIN = "min";
        final String OWM_DESCRIPTION = "main";

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

        JSONObject cityObject  = forecastJson.getJSONObject(OWN_CITY);
        cityName = cityObject.getString("name");
        JSONObject coordObject = cityObject.getJSONObject("coord");
        cityLongitude = coordObject.getString("lon");
        cityLatitude = coordObject.getString("lat");

        // OWM returns daily forecasts based upon the local time of the city that is being
        // asked for, which means that we need to know the GMT offset to translate this data
        // properly.

        // Since this data is also sent in-order and the first day is always the
        // current day, we're going to take advantage of that to get a nice
        // normalized UTC date for all of our weather.

        Time dayTime = new Time();
        dayTime.setToNow();

        // we start at the day returned by local time. Otherwise this is a mess.
        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

        // now we work exclusively in UTC
        dayTime = new Time();

        String[] resultStrs = new String[numDays];
        for(int i = 0; i < weatherArray.length(); i++) {
            // For now, using the format "Day, description, hi/low"

            // Get the JSON object representing the day
            JSONObject dayForecast = weatherArray.getJSONObject(i);

            if(i == 0) {
                day = "Today";
            } else if(i == 1){
                day = "Tomorrow";
            } else {
                // The date/time is returned as a long.  We need to convert that
                // into something human-readable, since most people won't read "1400356800" as
                // "this saturday".
                long dateTime;
                // Cheating to convert this to UTC time, which is what we want anyhow
                dateTime = dayTime.setJulianDay(julianStartDay+i);
                day = getReadableDateString(dateTime);
            }

            // description is in a child array called "weather", which is 1 element long.
            JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            description = weatherObject.getString(OWM_DESCRIPTION);

            // Temperatures are in a child object called "temp".  Try not to name variables
            // "temp" when working with temperature.  It confuses everybody.
            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
            double high = temperatureObject.getDouble(OWM_MAX);
            double low = temperatureObject.getDouble(OWM_MIN);

            highAndLow = formatHighLows(high, low);
            resultStrs[i] = day + "*" + description + "*" + Math.round(high) + "*" + Math.round(low) + "*" + dayForecast.getString("humidity");
        }

        return resultStrs;

    }

    public void getCurrentWeatherDataFromJson(String downloadedCurrentWeatherData) throws JSONException {
        final String OWM_WEATHER = "weather"; //JSONArray
        final String OWM_MAIN = "main"; //JSON value and JSONObject
        final String OWM_TEMP = "temp"; //JSON value
        final String OWM_HUMIDITY = "humidity";
        final String OWM_WIND = "wind";
        final String OWM_WIND_SPEED = "speed";
        final String OWM_WIND_DEG = "deg";
        final String OWM_PRESSURE = "pressure";

        JSONObject currentJson = new JSONObject(downloadedCurrentWeatherData);
        JSONArray weatherJson = currentJson.getJSONArray(OWM_WEATHER);

        JSONObject currentWeather = weatherJson.getJSONObject(0);
        currentMain = currentWeather.getString(OWM_MAIN);

        JSONObject mainJson = currentJson.getJSONObject(OWM_MAIN);
        currentTemp = mainJson.getLong(OWM_TEMP);
        currentHumidity = mainJson.getLong(OWM_HUMIDITY);
        currentPressure = mainJson.getLong(OWM_PRESSURE);

        JSONObject windJson = currentJson.getJSONObject(OWM_WIND);
        currentWindSpeed = windJson.getLong(OWM_WIND_SPEED);
        currentWindDeg = windJson.getLong(OWM_WIND_DEG);
    }
}
