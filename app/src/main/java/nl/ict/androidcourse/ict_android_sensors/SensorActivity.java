package nl.ict.androidcourse.ict_android_sensors;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class SensorActivity extends Activity implements SensorEventListener{

    private static final String TAG = "SensorActivity";

    private SensorManager mSensorManager;
    private Sensor mGyroscope;
    private Sensor mLightSensor;
    private Sensor mHeartbeatSensor;

    private ProgressBar mLightProgressBar;
    private int mMaxLightValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mHeartbeatSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_BEAT);

        mLightProgressBar = (ProgressBar)findViewById(R.id.progressBar2);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGyroscope != null) {
            mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            showErrorMessage("No gyroscope available!");
        }

        if (mLightSensor != null) {
            mLightProgressBar.setMax(mMaxLightValue);
            mSensorManager.registerListener(this, mLightSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            showErrorMessage("No light sensor available!");
        }

        if (mHeartbeatSensor != null) {
            mSensorManager.registerListener(this, mHeartbeatSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            showErrorMessage("No heartbeat sensor available!");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);

    }

    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType())
        {
            case Sensor.TYPE_GYROSCOPE:
                Log.d(TAG, "Received sensor data from gyroscope");
                break;
            case Sensor.TYPE_LIGHT:
                Log.d(TAG, "Received sensor data from light sensor: " + sensorEvent.values[0]);
                if ((sensorEvent.values[0]* 100) > mMaxLightValue ) {
                    mMaxLightValue = (int)(sensorEvent.values[0] *100);
                    mLightProgressBar.setMax(mMaxLightValue);
                }
                mLightProgressBar.setProgress((int)(sensorEvent.values[0]*100));
                break;
            default:
                Log.d(TAG, "Hmm Received sensor data from unknown sensor");
                break;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        switch (sensor.getType()) {
            case Sensor.TYPE_GYROSCOPE:
                Log.d(TAG, "Accuracy of gyroscope is now: "+ i);
                break;
            case Sensor.TYPE_LIGHT:
                Log.d(TAG, "Accuracy of light sensor is now: "+ i);
                break;
            default:
                break;
        }

    }

    private void showErrorMessage(final String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }
}
