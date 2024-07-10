package edu.csueb.android.weatherdemo;

import android.os.Bundle;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;


public class WeatherStation extends Activity {

    private SensorManager sensorManager;
    private TextView temperatureTextView;
    private TextView pressureTextView;
    private TextView lightTextView;

    private float currentTemperature = Float.NaN;
    private float currentPressure = Float.NaN;
    private float currentLight = Float.NaN;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temperatureTextView = findViewById(R.id.temperature);
        pressureTextView = findViewById(R.id.pressure);
        lightTextView = findViewById(R.id.light);
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        Timer updateTimer = new Timer("weatherUpdate");
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                updateGUI();
            }
        }, 0, 1000);
    }

    private final SensorEventListener tempSensorEventListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) { }

        public void onSensorChanged(SensorEvent event) {
            currentTemperature = event.values[0];
        }
    };

    private final SensorEventListener pressureSensorEventListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) { }

        public void onSensorChanged(SensorEvent event) {
            currentPressure = event.values[0];
        }
    };

    private final SensorEventListener lightSensorEventListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) { }

        public void onSensorChanged(SensorEvent event) {
            currentLight = event.values[0];
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (lightSensor != null)
            sensorManager.registerListener(lightSensorEventListener,
                    lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        else
            lightTextView.setText(R.string.light_sensor_unavailable);

        Sensor pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if (pressureSensor != null)
            sensorManager.registerListener(pressureSensorEventListener,
                    pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        else
            pressureTextView.setText(R.string.barometer_unavailable);

        Sensor temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if (temperatureSensor != null)
            sensorManager.registerListener(tempSensorEventListener,
                    temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        else
            temperatureTextView.setText(R.string.thermometer_unavailable);
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(pressureSensorEventListener);
        sensorManager.unregisterListener(tempSensorEventListener);
        sensorManager.unregisterListener(lightSensorEventListener);
        super.onPause();
    }

    private void updateGUI() {
        runOnUiThread(new Runnable() {
            public void run() {
                if (!Float.isNaN(currentPressure)) {
                    pressureTextView.setText(String.format("%s mBars", currentPressure));
                    pressureTextView.invalidate();
                }
                if (!Float.isNaN(currentLight)) {
                    String lightStr = "Sunny";
                    if (currentLight <= SensorManager.LIGHT_CLOUDY)
                        lightStr = "Night";
                    else if (currentLight <= SensorManager.LIGHT_OVERCAST)
                        lightStr = "Cloudy";
                    else if (currentLight <= SensorManager.LIGHT_SUNLIGHT)
                        lightStr = "Overcast";
                    lightTextView.setText(lightStr);
                    lightTextView.invalidate();
                }
                if (!Float.isNaN(currentTemperature)) {
                    temperatureTextView.setText(String.format("%s C", currentTemperature));
                    temperatureTextView.invalidate();
                }
            }
        });
    }
}
