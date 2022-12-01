package com.example.splashscreen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity implements LocationListener
{
    private LocationManager locationManager;
    private static String gps;
    private static int contgps;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLocation();
    }
    @SuppressLint("MissingPermission")
    void getLocation()
    {
        try
        {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]
                                {
                                        Manifest.permission.ACCESS_FINE_LOCATION
                                },
                        100);
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]
                                {
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                },
                        100);
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, MainActivity.this);
        } catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onLocationChanged(Location location)
    {
        try
        {
            contgps++;
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addressList.get(0).getAddressLine(0);
            gps = address;
            AdminSQLiteOpenHelper admin;
            SQLiteDatabase bdd;
            admin = new AdminSQLiteOpenHelper(this, "datos_gps", null, 1);
            bdd = admin.getWritableDatabase();
            bdd.delete("datos_gps", "1 = 1", null);
            ContentValues registro = new ContentValues();
            registro.put("ubicacion", gps);
            bdd.insert("datos_gps", null, registro);
            bdd.close();
            if (contgps <= 1)
            {
                //locationManager.removeUpdates(MainActivity.this);
                iniciar_login();
            }
        } catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void iniciar_login()
    {
        Intent intent = new Intent(MainActivity.this, inicioSesion.class);
        startActivity(intent);
        finish();
    }
}