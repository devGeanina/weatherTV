package data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Util.Utils;
import model.Place;
import model.Weather;

public class JSONWeatherParser {

    //parse the URL data
    public static Weather getWeather(String data){

        //create jsonobj from data
        Weather weather = new Weather();
        try {
            JSONObject jsonObject = new JSONObject(data);

            Place place = new Place();
            JSONObject coordonatesObject = Utils.getObject("coord",jsonObject);
            place.setLatitude(Utils.getFloat("lat",coordonatesObject));
            place.setLongitude(Utils.getFloat("lon",coordonatesObject));

            JSONObject sysObject = Utils.getObject("sys",jsonObject);
            place.setCountry(Utils.getString("country", sysObject));
            place.setLastUpdate(Utils.getInt("dt",jsonObject));
            place.setSunrise(Utils.getInt("sunrise",sysObject));
            place.setSunset(Utils.getInt("sunset",sysObject));
            place.setCity(Utils.getString("name",jsonObject));
            weather.place = place;

            //the weather info - http://openweathermap.org/weather-data
            JSONArray jsonArray = jsonObject.getJSONArray("weather");
            JSONObject jsonWeather = jsonArray.getJSONObject(0);
            //array has 1 obj - 0
            weather.currentCondition.setWeatherId(Utils.getInt("id",jsonWeather));
            weather.currentCondition.setDescription(Utils.getString("description", jsonWeather));
            weather.currentCondition.setCondition(Utils.getString("main",jsonWeather));
            weather.currentCondition.setIcon(Utils.getString("icon",jsonWeather));

            JSONObject mainObj = Utils.getObject("main", jsonObject);
            weather.currentCondition.setHumidity(Utils.getInt("humidity",mainObj));
            weather.currentCondition.setPressure(Utils.getInt("pressure",mainObj));
            weather.currentCondition.setMinTemp(Utils.getFloat("temp_min",mainObj));
            weather.currentCondition.setMaxTemp(Utils.getFloat("temp_max",mainObj));
            weather.currentCondition.setTemperature(Utils.getDouble("temp",mainObj));

            JSONObject windObject = Utils.getObject("wind", jsonObject);
            weather.wind.setSpeed(Utils.getFloat("speed",windObject));

            JSONObject cloudsObject = Utils.getObject("clouds",jsonObject);
            weather.clouds.setPrecipitation(Utils.getInt("all",cloudsObject));
            return weather;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Weather", "The parsed data is either incomplete or parsed wrongly.");
            return null;
        }
    }

    public static Weather getWeatherForecast(String data) {

        Weather weather = new Weather();
        try {

            JSONObject jsonObject = new JSONObject(data);

            JSONArray jArr = jsonObject.getJSONArray("list");
            // We traverse all the array and parse the data
            for (int i=0; i < jArr.length(); i++) {
            JSONObject jDayForecast = jArr.getJSONObject(i);
                // We retrieve the timestamp (dt)
            weather.dayForecast.setTimestamp(jDayForecast.getLong("dt"));
            // Temp is an object
            JSONObject jTempObj = jDayForecast.getJSONObject("temp");

            weather.dayForecast.setDay(Utils.getFloat("day",jTempObj));
            weather.dayForecast.setEve(Utils.getFloat("eve",jTempObj));
            weather.dayForecast.setMax(Utils.getFloat("max",jTempObj));
            weather.dayForecast.setMin(Utils.getFloat("min",jTempObj));
            weather.dayForecast.setMorning(Utils.getFloat("morn",jTempObj));
            weather.dayForecast.setNight(Utils.getFloat("night",jTempObj));

            // pressure & humidity
            weather.currentCondition.setPressure((float) jDayForecast.getDouble("pressure"));
            weather.currentCondition.setHumidity((float) jDayForecast.getDouble("humidity"));

            // the weather
            JSONArray jWeatherArr = jDayForecast.getJSONArray("weather");
            JSONObject jWeatherObj = jWeatherArr.getJSONObject(0);
            weather.currentCondition.setWeatherId(Utils.getInt("id", jWeatherObj));
            weather.currentCondition.setDescription(Utils.getString("description", jWeatherObj));
            weather.currentCondition.setCondition(Utils.getString("main", jWeatherObj));
            weather.currentCondition.setIcon(Utils.getString("icon", jWeatherObj));
            }

            Log.v("data",data);
            return weather;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Day Forecast", "The parsed data is either incomplete or parsed wrongly.");
            return null;
        }
    }
}
