package es.gonpuga.receiveroreo;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Created by gonzalo on 23/11/17.
 */

public class ServicioCirculoPolar extends Service implements LocationListener {
    private static final String TAG=ServicioCirculoPolar.class.getName();
    private LocationManager manejador;
    private static final int ID_NOTIFICACION_MAPA = 1;
    private static final int ID_NOTIFICACION_BACKGROUND = 2;
    private static final double LATITUD_CPOLAR = 66.55d;
    //notification channel required Android 0
    private NotificationManager mManager;
    private NotificationChannel androidChannel;
    public static final String CHANNEL_ID = "com.example.receiveroreo.ANDROID";
    public static final String CHANNEL_NAME = "ANDROID CHANNEL";

    @Override
    public void onCreate() {
        mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manejador = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.
                ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            manejador.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    10 * 1000, 5, this);
        }

        //create channels
        // create android channel (requires api 26)
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            androidChannel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            // Sets whether notifications posted to this channel should display notification lights
            androidChannel.enableLights(true);
            // Sets whether notification posted to this channel should vibrate.
            androidChannel.enableVibration(true);
            // Sets the notification light color for notifications posted to this channel
            androidChannel.setLightColor(Color.GREEN);
            // Sets whether notifications posted to this channel appear on the lockscreen or not
            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            //create channel
            mManager.createNotificationChannel(androidChannel);
            //launch foreground service before 5 seconds (ANR time)
            startForeground(ID_NOTIFICACION_BACKGROUND, getNotification());
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        if (latitude > LATITUD_CPOLAR) {
            Toast.makeText(this, "It's chilly here!", Toast.LENGTH_SHORT).show();
            mManager.notify(ID_NOTIFICACION_MAPA, getNotification());
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }


    private Notification getNotification()
    {
        //Launch MainActivity
        PendingIntent intencionPendiente = PendingIntent.getActivity(this,
                0,
                new Intent(this, MainActivity.class),
                0);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("¡Warning!")
                .setContentText("It's chilly here!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_dialog_alert))
                .setContentIntent(intencionPendiente)
                .setAutoCancel(true)
                .build();

        return notification;
    }

}
