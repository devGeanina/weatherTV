package data;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import Util.Utils;

public class WeatherHttpClient {

    private Context context;

    //getting the api data from openweather
    public String getWeatherData(String place){
        //connection to URL
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            connection = (HttpURLConnection) (new URL(Utils.BASE_URL + place)).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            //read the response from the url
            StringBuffer stringBuffer = new StringBuffer();
            inputStream = connection.getInputStream();

            BufferedReader  bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;

            while((line = bufferedReader.readLine()) != null){
                //new line after each read
                stringBuffer.append(line + "\r\n");
            }
            inputStream.close();
            connection.disconnect();
            return stringBuffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Weather app", "The URL or the connection is broken.");
        }
        //if we get nothing return null
        return null;
    }


    public String getForecastWeatherData(String location) {
        HttpURLConnection con = null;
        InputStream is = null;

        try {

            // Forecast
            String url = Utils.BASE_FORECAST_URL + location;

            url = url + "&cnt=1"+"&appId=" +Utils.APP_ID;
            con = (HttpURLConnection) (new URL(url)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            Log.v("url", con.toString());

            // Let's read the response
            StringBuffer buffer1 = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br1 = new BufferedReader(new InputStreamReader(is));
            String line1 = null;
            while ((line1 = br1.readLine()) != null)
                buffer1.append(line1 + "\r\n");

            is.close();
            con.disconnect();
            return buffer1.toString();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }
}
