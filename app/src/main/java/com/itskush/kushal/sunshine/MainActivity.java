package com.itskush.kushal.sunshine;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

//    List<String> listData;
    List<recyclerViewModel> listData;
//    ArrayAdapter<String> arrayAdapter;
    recyclerViewAdapter recyclerViewAdapter;
    RecyclerView recyclerView;
    JSONParsing jsonParsing;


    //link to weather of Faridabad using api key:
    //private static final String url = "http://api.openweathermap.org/data/2.5/weather?id=1271951&mode=json&units=metric&appid=";

    ImageView todayWeatherImageView;
    //View relativeBackground;
    TextView todayDateTextView, todayCurrentWeatherTextView, todaysWeatherTV, todayCurrentHumidity, todayCurrentWindSpeed, todayIn, todayCurrentPressure, todayCurrentWindDeg;
    RelativeLayout todayLinearLayout;
    CollapsingToolbarLayout collapsingToolbar;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();

//        String[] data = {" "};
        recyclerView = (RecyclerView) findViewById(R.id.listview_forecast);
//        listData = new ArrayList<String>(Arrays.asList(data));
        listData = new ArrayList<>();
        recyclerViewAdapter = new recyclerViewAdapter(listData);
        RecyclerView.LayoutManager layoutManager = new CustomLinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

//        arrayAdapter = new ArrayAdapter<String>(this, R.layout.ist_item_forcast, R.id.list_item_forcast_textview, listData);

//        ListView listView = (ListView) super.findViewById(R.id.listview_forecast);
//        listView.setAdapter(arrayAdapter);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //openPreferredLocationInMap(getSharedPreferencesValue("location", "Delhi"));
//                updateForecast();
//            }
//        });

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intentDetailedActivity = new Intent(MainActivity.this, DetailedActivity.class).putExtra("com.itskush.kushal.sunshine.ListItemText", arrayAdapter.getItem(position));
//                if(arrayAdapter.getItem(position) != " ") startActivity(intentDetailedActivity);
//            }
//        });

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // TODO Handle item click
//                        Intent intentDetailedActivity = new Intent(MainActivity.this, DetailedActivity.class)
//                                .putExtra("com.itskush.kushal.sunshine.ListItemText", listData.get(position).getHeadingText());
//                        if(listData.get(position).getHeadingText() != " ") startActivity(intentDetailedActivity);
                    }
                })
        );

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int scrollDy = 0;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollDy += dy;
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(scrollDy==0&&(newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE))
                {
                    AppBarLayout appBarLayout = ((AppBarLayout) findViewById(R.id.appbar));
                    appBarLayout.setExpanded(true);
                }
            }
        });


        todayLinearLayout = (RelativeLayout) findViewById(R.id.todayLinearLayout);
        todayWeatherImageView = (ImageView) findViewById(R.id.today_weather_image);
        //relativeBackground = (View) findViewById(R.id.relative_background);
        todayDateTextView = (TextView) findViewById(R.id.today_date_textview);
        todayCurrentWeatherTextView = (TextView) findViewById(R.id.today_current_weather_textview);
        todayCurrentHumidity = (TextView) findViewById(R.id.today_current_humidity_weather_textview);
        todayCurrentWindSpeed = (TextView) findViewById(R.id.today_current_wind_speed_weather_textview);
        todayIn = (TextView) findViewById(R.id.today_in_textview);
        todayCurrentPressure = (TextView) findViewById(R.id.today_current_pressure_weather_textview);
        todayCurrentWindDeg = (TextView) findViewById(R.id.today_current_wind_deg_weather_textview);

        todayCurrentWeatherTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        updateForecast();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intentSettings = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intentSettings);
            return true;
        }

        if (id == R.id.refresh) {
            Toast.makeText(MainActivity.this, "Refreshing..", Toast.LENGTH_SHORT).show();
            updateForecast();
            return true;
        }

        if (id == R.id.share) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Try my new weather app :)" + " #WeatherApp #MadeBy #Kushal");
            shareIntent.setType("text/plain");
            startActivity(shareIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void optionButton(View view) {
//        MainActivity.this.openOptionsMenu();;
        toolbar.showOverflowMenu();
    }

    public void refreshButton(View view) {
        Toast.makeText(MainActivity.this, "Refreshing..", Toast.LENGTH_SHORT).show();
        updateForecast();
    }

    public void shareButton(View view) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Try my new weather app :)" + " #WeatherApp #MadeBy #Kushal");
        shareIntent.setType("text/plain");
        startActivity(shareIntent);
    }

    public void menuButton(View view) {
        Intent intentSettings = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intentSettings);
    }

    private String[] updateForecast() {
        ConnectivityManager connectivityManager = (ConnectivityManager) super.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadDataTask().execute(getSharedPreferencesValue("location", "New+Delhi").trim().replace(" ", "+"));
        } else {
            Toast.makeText(MainActivity.this, "Internet connection not available. Please check your network connections.", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateForecast();
    }

    private String getSharedPreferencesValue(String key, String defValue) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        return pref.getString(key, defValue);
    }

    private void setSharedPreferencesValue(String key, String value) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        pref.edit().putString(key, value).apply();
    }

    private void openPreferredLocationInMap(String cityName) {
        Uri geoLocation = Uri.parse("geo:" + jsonParsing.getCityLongitude() + "," + jsonParsing.getCityLatitude() + "?z=11").buildUpon()
                .appendQueryParameter("q", jsonParsing.getCityName())
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "Couldn't call " + cityName + ", no receiving apps installed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initCollapsingToolbar() {
        collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;
            TableRow tr1 = (TableRow) findViewById(R.id.tableRow1);
            TableRow tr5 = (TableRow) findViewById(R.id.tableRow5);

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    //collapsingToolbar.setTitle("Forecast for " + getSharedPreferencesValue("cnt", "7") + " Days");
                    tr1.setVisibility(View.INVISIBLE);
                    tr5.setVisibility(View.INVISIBLE);
                    isShow = true;
                    //todayLinearLayout.setVisibility(View.INVISIBLE);
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    todayLinearLayout.setVisibility(View.VISIBLE);
                    tr1.setVisibility(View.VISIBLE);
                    tr5.setVisibility(View.VISIBLE);
                    isShow = false;
                }
            }
        });
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////

    public class DownloadDataTask extends AsyncTask<String, Void, String[]> {


        private static final String apiId = "Your_API_Key";
        private String downloadedCurrentData, downloadedForecastData;

        //link to weather of Faridabad using api key:
        //private static final String url = "http://api.openweathermap.org/data/2.5/weather?id=1271951&mode=json&units=metric&appid=";

        @Override
        protected String[] doInBackground(String... cityID) {
            try {
                String format;
                if (getSharedPreferencesValue("units", "C").charAt(0) == 'C')
                    format = "metric";
                else
                    format = "imperial";

                jsonParsing = new JSONParsing();

                downloadedCurrentData = downloadData(buildURI(cityID[0], "json", format, "1", true));
                jsonParsing.getCurrentWeatherDataFromJson(downloadedCurrentData);

                downloadedForecastData = downloadData(buildURI(cityID[0], "json", format, getSharedPreferencesValue("cnt", "7"), false));
                String[] data = jsonParsing.getWeatherDataFromJson(downloadedForecastData,
                        Integer.parseInt(getSharedPreferencesValue("cnt", "7")));
                return data;
            } catch (Exception e) {
                //return "Unable to retrieve data.";
                return null;
            }
        }

        private String downloadData(String urlLink) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL(urlLink);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(15000);
                urlConnection.setReadTimeout(10000);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
                return forecastJsonStr;
            } catch (IOException e) {
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                    }
                }
            }
        }

        private String buildURI(String cityID, String format, String units, String days, boolean isCurrentWeather) {
            final String BASE_URL;
            if (!isCurrentWeather)
                BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
            else
                BASE_URL = "http://api.openweathermap.org/data/2.5/weather?";
            final String ID_PARAM = "q";
            final String MODE_PARAM = "mode";
            final String UNITS_PARAM = "units";
            final String DAYS_COUNT = "cnt";
            final String APPID_PARAM = "appid";

            Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(ID_PARAM, cityID)
                    .appendQueryParameter(MODE_PARAM, format)
                    .appendQueryParameter(UNITS_PARAM, units)
                    .appendQueryParameter(DAYS_COUNT, days)
                    .appendQueryParameter(APPID_PARAM, apiId).build();

            return buildUri.toString();
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                recyclerViewModel  recyclerViewModel;
                StringTokenizer stringTokenizer;
//                arrayAdapter.clear();
                listData.clear();
                for (String i : result){ //arrayAdapter.add(i);
                    if(i != null) {
                        stringTokenizer = new StringTokenizer(i, "*");
                        recyclerViewModel = new recyclerViewModel(getResourseImageIcon(i),
                                stringTokenizer.nextToken(), //date
                                stringTokenizer.nextToken(), //desc
                                "↑ " + stringTokenizer.nextToken() + "°", //high
                                "↓ " + stringTokenizer.nextToken() + "°", //low
                                "Humidity  " + stringTokenizer.nextToken() + "%"); //humidity
                        listData.add(recyclerViewModel);
                    }
                }
                recyclerView.setAdapter(recyclerViewAdapter);

                // for today's linear layout
                String today = result[0];

                todayDateTextView.setText(jsonParsing.getCurrentMain());
                todayCurrentWeatherTextView.setText(Long.toString(jsonParsing.getCurrentTemp()) + "°");
                todayCurrentHumidity.setText(Long.toString(jsonParsing.getCurrentHumidity()));
                todayCurrentWindSpeed.setText(Long.toString(jsonParsing.getCurrentWindSpeed()));
                todayCurrentPressure.setText(Long.toString(jsonParsing.getCurrentPressure()));
                todayCurrentWindDeg.setText(jsonParsing.getCurrentWindDeg());

                //MainActivity.this.setTitle("Today in " + jsonParsing.getCityName());
                todayIn.setText(jsonParsing.getCityName());

                String currentMain = jsonParsing.getCurrentMain();
                if(currentMain != null) {
                    if(getSharedPreferencesValue("color", "random").charAt(0) == 'A') {
                        if (currentMain.equals("Rain")) {
                            todayWeatherImageView.setImageResource(R.drawable.rain_icon);
                            changeWeatherViewColor("#3F51B5", "#3F51B5", "#303F9F");  //indigo
//                    relativeBackground.setImageResource(R.drawable.rain);
                        } else if (currentMain.equals("Mist")|| currentMain.equals("Haze")) {
                            todayWeatherImageView.setImageResource(R.drawable.fog_icon);
                            changeWeatherViewColor("#9E9E9E", "#9E9E9E", "#616161");  //grey
//                    relativeBackground.setImageResource(R.drawable.mist);
                        } else if (currentMain.equals("Clouds")) {
                            todayWeatherImageView.setImageResource(R.drawable.clouds_icon);
                            changeWeatherViewColor("#455A64", "#455A64", "#263238");  //blue grey
                        } else if (currentMain.equals("Sunny") || currentMain.equals("Clear")) {
                            todayWeatherImageView.setImageResource(R.drawable.sun_icon);
                            changeWeatherViewColor("#FFC107", "#FFC107", "#FFA000");  //Amber
                        } else if (currentMain.equals("Night")) {
                            todayWeatherImageView.setImageResource(R.drawable.moon_icon);
                            changeWeatherViewColor("#616161", "#616161", "#212121");  //Grey
                        } else if (currentMain.equals("Storm") || currentMain.equals("Thunderstorm")) {
                            todayWeatherImageView.setImageResource(R.drawable.storm_icon);
                            changeWeatherViewColor("#9E9E9E", "#9E9E9E", "#616161");  //Grey
                        } else {
                            todayWeatherImageView.setImageResource(R.mipmap.ic_launcher);
                            changeWeatherViewColor("#3F51B5", "#3F51B5", "#303F9F");  //default
                        }
                    }
                    else {
                        if (currentMain.equals("Rain")) {
                            todayWeatherImageView.setImageResource(R.drawable.rain_icon);
                        } else if (currentMain.equals("Mist") || currentMain.equals("Haze")) {
                            todayWeatherImageView.setImageResource(R.drawable.fog_icon);
                        } else if (currentMain.equals("Clouds")) {
                            todayWeatherImageView.setImageResource(R.drawable.clouds_icon);
                        } else if (currentMain.equals("Sunny" )|| currentMain.equals("Clear")) {
                            todayWeatherImageView.setImageResource(R.drawable.sun_icon);
                        } else if (currentMain.equals("Night")) {
                            todayWeatherImageView.setImageResource(R.drawable.moon_icon);
                        } else if (currentMain.equals("Storm") || currentMain.equals("Thunderstorm")) {
                            todayWeatherImageView.setImageResource(R.drawable.storm_icon);
                        } else {
                            todayWeatherImageView.setImageResource(R.mipmap.ic_launcher);
                        }
                        changeRandomViewColor();
                    }
                }
            }
        }

        private void changeWeatherViewColor(String relativeBackgroundColor, String actionBarColor, String statusBarColor) {
            if(relativeBackgroundColor.equals(actionBarColor))
                startAnimation(relativeBackgroundColor, statusBarColor);
        }

        private void changeRandomViewColor() {
            String[] materialColor = {"#f44336", "#E91E63", "#9C27B0", "#673AB7",
                    "#3F51B5", "#2196F3", "#03A9F4", "#00BCD4", "#009688",
                    "#4CAF50", "#8BC34A", "#CDDC39", "#FFC107",
                    "#FF9800", "#FF5722"};
            String[] materialDarkColor = {"#d32f2f", "#C2185B", "#7B1FA2", "#512DA8",
                    "#303F9F", "#1976D2", "#0288D1", "#0097A7", "#00796B",
                    "#388E3C", "#689F38", "#AFB42B", "#FFA000",
                    "#F57C00", "#E64A19"};

            int colorInt = new Random().nextInt(materialColor.length - 1);

            String color = materialColor[colorInt];
            String colorDark = materialDarkColor[colorInt];

            startAnimation(color, colorDark);

        }

        private void startAnimation(String color, String colorDark) {

            ValueAnimator valueAnimator = ValueAnimator.ofObject(new ArgbEvaluator(),
                    ((ColorDrawable) collapsingToolbar.getBackground()).getColor(),
                    Color.parseColor(color));
            valueAnimator.setDuration(1000);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    collapsingToolbar.setBackgroundColor((int) animation.getAnimatedValue());
                }
            });
            valueAnimator.start();

            ValueAnimator valueAnimator1 = ValueAnimator.ofObject(new ArgbEvaluator(),
                    ((ColorDrawable) collapsingToolbar.getBackground()).getColor(),
                    Color.parseColor(color));
            valueAnimator1.setDuration(1000);
            valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable((int) animation.getAnimatedValue()));
                }
            });
            valueAnimator1.start();

            final Window window = MainActivity.this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ValueAnimator valueAnimator2 = ValueAnimator.ofObject(new ArgbEvaluator(),
                    window.getStatusBarColor(),
                    Color.parseColor(colorDark));
            valueAnimator2.setDuration(1000);
            valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    window.setStatusBarColor((int) animation.getAnimatedValue());
                }
            });
            valueAnimator2.start();
        }

        private int getResourseImageIcon(String text) {
            if(text != null)
            if (text.contains("Rain")) {
                return R.drawable.rain_icon;
            } else if (text.contains("Mist") || text.contains("Haze")) {
                return (R.drawable.fog_icon);
            } else if (text.contains("Clouds")) {
                return (R.drawable.clouds_icon);
            } else if (text.contains("Sunny") || text.contains("Clear")) {
                return (R.drawable.sun_icon);
            } else if (text.contains("Night")) {
                return (R.drawable.moon_icon);
            } else if (text.contains("Storm") || text.contains("Thunderstorm")) {
                return (R.drawable.storm_icon);
            } else {
                return (R.mipmap.ic_launcher);
            }
            return 0;
        }
    }

}



