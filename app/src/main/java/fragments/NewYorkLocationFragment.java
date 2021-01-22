package fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import com.weathertv.R;
import java.text.DecimalFormat;
import model.CurrentCondition;
import model.Weather;

public class NewYorkLocationFragment extends Fragment{

    NewYorkCityListener activityListener;
    private TextView cityName;
    private TextView temperature;
    private ImageView iconView;
    private TextView description;
    private TextView humidity;
    private TextView pressure;
    private TextView wind;
    private TextView sunrise;
    private TextView sunset;
    private TextClock newYorkClock;
    private TextView updatedText;

    public interface NewYorkCityListener{
        void renderWeather();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            activityListener = (NewYorkCityListener) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString());
        }}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newyork_city_fragment,container,false);

        cityName = (TextView) view.findViewById (R.id.cityTextnewYork);
        cityName.setTextColor(getResources().getColor(R.color.custom));
        cityName.setTextSize(30);

        iconView = (ImageView) view.findViewById(R.id.thumbnailIconnewYork);
        iconView.setColorFilter(getResources().getColor(R.color.custom));

        int newHeight = 100;
        int newWidth = 100;
        iconView.requestLayout();
        iconView.getLayoutParams().height = newHeight;
        iconView.getLayoutParams().width = newWidth;
        iconView.setScaleType(ImageView.ScaleType.FIT_XY);

        temperature = (TextView) view.findViewById(R.id.tempTextnewYork);
        temperature.setTextColor(getResources().getColor(R.color.white));
        updatedText = (TextView) view.findViewById(R.id.updateText);

        description = (TextView) view.findViewById(R.id.cloudTextnewYork);
        description.setTextColor(getResources().getColor(R.color.white));
        humidity = (TextView) view.findViewById(R.id.humidTextnewYork);
        pressure = (TextView) view.findViewById(R.id.pressureTextnewYork);
        wind = (TextView) view.findViewById(R.id.windTextnewYork);
        sunrise = (TextView) view.findViewById(R.id.riseTextnewYork);
        sunset = (TextView) view.findViewById(R.id.setTextnewYork);
        newYorkClock = (TextClock) view.findViewById(R.id.newYorkClock);
        newYorkClock.setTimeZone("Europe/newYork");
        newYorkClock.setFormat24Hour("HH:mm:ss");
        newYorkClock.setTextColor(getResources().getColor(R.color.custom));

        return view;
    }

    public void setData(Weather weather) {
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        String tempFormat = decimalFormat.format(weather.currentCondition.getTemperature());

        cityName.setText(weather.place.getCity() + "," + weather.place.getCountry());
        temperature.setText("" + tempFormat + "°C");
        humidity.setText("Humidity: " + weather.currentCondition.getHumidity() + "%");
        pressure.setText("Pressure: " + weather.currentCondition.getPressure() + "hPa");
        wind.setText("Wind: " + weather.wind.getSpeed() + "mps");
        sunrise.setText(String.format("Sunrise: " + CurrentCondition.unixTimeStampToDateTime(weather.place.getSunrise())));
        sunset.setText(String.format("Sunset: " + CurrentCondition.unixTimeStampToDateTime(weather.place.getSunset())));
        description.setText(weather.currentCondition.getDescription());
        updatedText.setText(String.format("Last Updated: %s", CurrentCondition.getDateNow()));

        switch (weather.currentCondition.getIcon()) {
            case "01d":
                iconView.setImageResource(R.drawable.sun);
                break;
            case "01n":
                iconView.setImageResource(R.drawable.moon_night);
                break;
            case "02n":
                iconView.setImageResource(R.drawable.clouds_night);
                break;
            case "10n":
                iconView.setImageResource(R.drawable.fog_night);
                break;
            case "02d":
                iconView.setImageResource(R.drawable.light_clouds);
                break;
            case "50n":
            case "50d":
                iconView.setImageResource(R.drawable.fog_day);
                break;
            case "03d":
            case "03n":
                iconView.setImageResource(R.drawable.clouds);
                break;
            case "09d":
            case "09n":
                iconView.setImageResource(R.drawable.light_rain);
                break;
            case "04n":
            case "04d":
                iconView.setImageResource(R.drawable.broken_clouds);
                break;
            case "11d":
            case "11n":
            case "10d":
                iconView.setImageResource(R.drawable.rain);
                break;
            case "13d":
            case "13n":
                iconView.setImageResource(R.drawable.snow);
                break;
            default:
                iconView.setImageResource(Integer.parseInt(null));
        }
    }
}
