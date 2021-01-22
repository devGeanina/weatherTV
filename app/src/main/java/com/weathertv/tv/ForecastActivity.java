package com.weathertv.tv;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.weathertv.R;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import data.JSONWeatherParser;
import data.WeatherHttpClient;
import model.Weather;

public class ForecastActivity extends AppCompatActivity {

    private TextView cityText;
    private TextView description;
    private TextView temp;
    private ImageView weatherIcon;
    private TextView tempMax;
    private TextView night;
    private TextView morning;
    private TextView tempMin;
    private TextView evening;
    private ImageButton backToHome;
    Weather weather = new Weather();
    Timer repeatRefresh = new Timer();
    //refresh at 3h
    int repeatInterval = 10800000;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        cityText = (TextView) findViewById(R.id.city_field);
        //default city
        cityText.setText("Paris,FR");
        cityText.setTextColor(getResources().getColor(R.color.custom));
        temp = (TextView) findViewById(R.id.current_temperature_field);
        temp.setTextColor(getResources().getColor(R.color.white));
        description = (TextView) findViewById(R.id.details_field);
        tempMax = (TextView) findViewById(R.id.tempmax);
        tempMin = (TextView) findViewById(R.id.tempmin);
        night = (TextView) findViewById(R.id.night);
        morning = (TextView) findViewById(R.id.tempmorning);
        evening = (TextView) findViewById(R.id.tempevening);
        backToHome = (ImageButton) findViewById(R.id.backToHome);
        backToHome.setImageResource(R.drawable.back_button);
        backToHome.setOnClickListener(btnListener);

        weatherIcon = (ImageView) findViewById(R.id.weather_icon);
        int newHeight = 100;
        int newWidth = 100;
        weatherIcon.requestLayout();
        weatherIcon.getLayoutParams().height = newHeight;
        weatherIcon.getLayoutParams().width = newWidth;
        weatherIcon.setScaleType(ImageView.ScaleType.FIT_XY);
        weatherIcon.setColorFilter(getResources().getColor(R.color.custom));

        changeCity("Paris,FR");

        repeatRefresh.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                changeCity("Paris,FR");
            }
        },0,repeatInterval);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_forecast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.change_cityId) {
            showInputDialog();
        }
        return false;
    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change city");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String city = input.getText().toString();
                changeCity(city);
                cityText.setText(city);
            }
        });
        builder.show();
    }

    public void changeCity(String city) {
        WeatherTask weatherTask = new WeatherTask();
        weatherTask.execute(new String[]{city + "&units=metric"});
    }

    private class WeatherTask extends AsyncTask<String, Void, Weather> {
        @Override
        protected Weather doInBackground(String... params) {
            String data = ((new WeatherHttpClient()).getForecastWeatherData(params[0]));
            weather.iconData = weather.currentCondition.getIcon();
            weather = JSONWeatherParser.getWeatherForecast(data);
            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);
            try {
                DecimalFormat decimalFormat = new DecimalFormat("#.#");
                String tempFormat = decimalFormat.format(weather.dayForecast.getDay());
                temp.setText("" + tempFormat + "°C");

                switch (weather.currentCondition.getIcon()) {
                    case "01d":
                        weatherIcon.setImageResource(R.drawable.sun);
                        weatherIcon.setColorFilter(getResources().getColor(R.color.custom));
                        break;
                    case "01n":
                        weatherIcon.setImageResource(R.drawable.moon_night);
                        break;
                    case "02n":
                        weatherIcon.setImageResource(R.drawable.clouds_night);
                        break;
                    case "10n":
                        weatherIcon.setImageResource(R.drawable.fog_night);
                        break;
                    case "02d":
                        weatherIcon.setImageResource(R.drawable.light_clouds);
                        break;
                    case "50n":
                    case "50d":
                        weatherIcon.setImageResource(R.drawable.fog_day);
                        break;
                    case "03d":
                    case "03n":
                        weatherIcon.setImageResource(R.drawable.clouds);
                        break;
                    case "09d":
                    case "09n":
                        weatherIcon.setImageResource(R.drawable.light_rain);
                        break;
                    case "04n":
                    case "04d":
                        weatherIcon.setImageResource(R.drawable.broken_clouds);
                        break;
                    case "11d":
                    case "11n":
                    case "10d":
                        weatherIcon.setImageResource(R.drawable.rain);
                        break;
                    case "13d":
                    case "13n":
                        weatherIcon.setImageResource(R.drawable.snow);
                        break;
                    default:
                        weatherIcon.setImageResource(Integer.parseInt(null));
                }

                description.setText(weather.currentCondition.getCondition() + " (" +weather.currentCondition.getDescription()+")");
                tempMax.setText("Max. Temperature: "+decimalFormat.format(weather.dayForecast.getMax())+"°C");
                tempMin.setText("Min. Temperature: "+decimalFormat.format(weather.dayForecast.getMin())+"°C");
                night.setText("Average night temperature: " + decimalFormat.format(weather.dayForecast.getNight())+"°C");
                morning.setText("Feels like: "+decimalFormat.format(weather.dayForecast.getMorning())+"°C"+" in the morning");
                evening.setText("Feels like: "+decimalFormat.format(weather.dayForecast.getEve())+"°C"+ " in the evening");

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Weather app", "The async task for forecast has failed.");
            }
        }
    }

    private View.OnClickListener btnListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            Intent intent = new Intent(ForecastActivity.this, MainActivity.class);
            startActivity(intent);
        }
    };
}
