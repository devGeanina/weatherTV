
package com.weathertv.tv;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.weathertv.R;

import java.util.Timer;

import data.JSONWeatherParser;
import data.WeatherHttpClient;
import fragments.NewYorkLocationFragment;
import fragments.ParisLocationFragment;
import fragments.LondonLocationFragment;
import fragments.TaipeiLocationFragment;
import model.Weather;

    public class MainActivity extends AppCompatActivity implements NewYorkLocationFragment.NewYorkCityListener, ParisLocationFragment.ParisCityListener, TaipeiLocationFragment.TaipeiCityListener, LondonLocationFragment.LondonCityListener {

        String cityNewYork = "New York,US";
        String cityParis = "Paris,FR";
        String cityTaipei = "Taipei,TW";
        String cityLondon = "London,UK";
        Weather weather = new Weather();
        WeatherTask weatherTaskNY = new WeatherTask();
        WeatherTask weatherTaskParis = new WeatherTask();
        WeatherTask weatherTaskLondon = new WeatherTask();
        WeatherTask weatherTaskTaipei = new WeatherTask();
        final Handler handler = new Handler();

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            renderWeather();
        }

        public void renderWeather() {
            try {
                weatherTaskNY.execute(new String[]{cityNewYork + "&units=metric" + "&appId=" + Util.Utils.APP_ID});
                weatherTaskParis.execute(new String[]{cityParis + "&units=metric" + "&appId=" + Util.Utils.APP_ID});
                weatherTaskLondon.execute(new String[]{cityLondon + "&units=metric" + "&appId=" + Util.Utils.APP_ID});
                weatherTaskTaipei.execute(new String[]{cityTaipei + "&units=metric" + "&appId=" + Util.Utils.APP_ID});
            }catch (Exception e){
                Log.v("Error,", "executing tasks.");
            }
        }

        public void refresh(){
            handler.postDelayed(new Runnable() {
                public void run() {
                    if(weatherTaskNY.isCancelled() || weatherTaskParis.isCancelled() || weatherTaskTaipei.isCancelled() || weatherTaskLondon.isCancelled()){
                       Log.v("Tasks can't refresh,","keeping the last downloaded data");
                    }else {
                        finish();
                        startActivity(getIntent());
                        handler.postDelayed(this, 120000);
                    }
                }
            }, 120000);
        }


        private class WeatherTask extends AsyncTask<String, Void, Weather> {
            @Override
            protected Weather doInBackground(String... params) {
                final String data = ((new WeatherHttpClient()).getWeatherData(params[0]));
                weather.iconData = weather.currentCondition.getIcon();
                weather = JSONWeatherParser.getWeather(data);
                return weather;
            }

            @Override
            protected void onPostExecute(Weather weather) {
                super.onPostExecute(weather);
                try{
                    if(weatherTaskNY.getStatus().equals(Status.PENDING) || weatherTaskNY.getStatus().equals(Status.RUNNING)) {
                        NewYorkLocationFragment fragment = (NewYorkLocationFragment) getFragmentManager().findFragmentById(R.id.fragmentNewYork);
                        fragment.setData(weather);
                    }

                    if(weatherTaskLondon.getStatus().equals(Status.PENDING) || weatherTaskLondon.getStatus().equals(Status.RUNNING)) {
                        LondonLocationFragment fragment = (LondonLocationFragment) getFragmentManager().findFragmentById(R.id.fragmentLondon);
                        fragment.setData(weather);
                    }

                    if(weatherTaskTaipei.getStatus().equals(Status.PENDING) || weatherTaskTaipei.getStatus().equals(Status.RUNNING)) {
                        TaipeiLocationFragment fragment = (TaipeiLocationFragment) getFragmentManager().findFragmentById(R.id.fragmentTaipei);
                        fragment.setData(weather);
                    }

                    if(weatherTaskParis.getStatus().equals(Status.PENDING) || weatherTaskParis.getStatus().equals(Status.RUNNING)){
                        ParisLocationFragment fragment = (ParisLocationFragment) getFragmentManager().findFragmentById(R.id.fragmentParis);
                        fragment.setData(weather);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Weather app", "The async task has failed.");
                    Toast.makeText(MainActivity.this, "The internet may be down.", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            switch (item.getItemId()) {
                case R.id.change_id:
                    startActivity(new Intent(MainActivity.this, ForecastActivity.class));
                    break;
                case R.id.refresh:
                    finish();
                    startActivity(getIntent());
                    break;
                default:
                    return super.onOptionsItemSelected(item);
            }
            return true;
        }
}
