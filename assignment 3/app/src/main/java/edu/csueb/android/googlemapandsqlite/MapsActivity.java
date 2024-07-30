package edu.csueb.android.googlemapandsqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MapsActivity";

    private final LatLng LOCATION_UNIV = new LatLng(37.65664, -122.05714);
    private final LatLng LOCATION_CS = new LatLng(37.65636, -122.05440);
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        LoaderManager.getInstance(this).initLoader(0, null, this);

        map.setOnMapClickListener(this::insert);

        map.setOnMapLongClickListener(point -> {
            Log.d(TAG, "Map long-clicked at: " + point);
            delete();
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class LocationInsertTask extends AsyncTask<ContentValues, Void, Void> {

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Log.d(TAG, "Inserting location: " + contentValues[0]);
            getContentResolver().insert(LocationsContentProvider.CONTENT_URI, contentValues[0]);
            return null;
        }
    }

    private class LocationDeleteTask extends AsyncTask<ContentValues, Void, Void> {

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Log.d(TAG, "Deleting all locations");
            getContentResolver().delete(LocationsContentProvider.CONTENT_URI, null, null);
            return null;
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Uri uri = LocationsContentProvider.CONTENT_URI;
        Log.d(TAG, "Creating loader for URI: " + uri);
        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> arg0, Cursor arg1) {
        int locationCount;
        double lat = 0;
        double lng = 0;
        float zoom = 10; //0

        // Number of locations available in the SQLite database table
        if (arg1 != null) {
            locationCount = arg1.getCount();
            // Move the current record pointer to the first row of the table
            arg1.moveToFirst();
        } else {
            locationCount = 0;
        }

        for (int i = 0; i < locationCount; i++) {
            // Get the latitude
            int latIndex = arg1.getColumnIndex(LocationsDB.LAT_COLUMN);
            if (latIndex >= 0) {
                lat = arg1.getDouble(latIndex);
            } else {
                Log.e(TAG, "Latitude column not found");
            }

            // Get the longitude
            int lngIndex = arg1.getColumnIndex(LocationsDB.LNG_COLUMN);
            if (lngIndex >= 0) {
                lng = arg1.getDouble(lngIndex);
            } else {
                Log.e(TAG, "Longitude column not found");
            }

            // Get the zoom level
            int zoomIndex = arg1.getColumnIndex(LocationsDB.ZOOM_LEVEL_COLUMN);
            if (zoomIndex >= 0) {
                zoom = arg1.getFloat(zoomIndex);
            } else {
                Log.e(TAG, "Zoom level column not found");
            }

            // Creating an instance of LatLng to plot the location in Google Maps
            LatLng location = new LatLng(lat, lng);

            // Drawing the marker in the Google Maps
            map.addMarker(new MarkerOptions().position(location));

            // Traverse the pointer to the next row
            arg1.moveToNext();
        }

        if (locationCount > 0) {
            // Moving CameraPosition to last clicked position
            map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));

            // Setting the zoom level in the map on last position  is clicked
            map.animateCamera(CameraUpdateFactory.zoomTo(zoom));
        }
    }


    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        Log.d(TAG, "Loader reset");
    }

    private void insert(LatLng point) {
        map.addMarker(new MarkerOptions().position(point));

        LocationInsertTask insertTask = new LocationInsertTask();
        ContentValues newValues = new ContentValues();
        newValues.put(LocationsDB.LAT_COLUMN, point.latitude);
        newValues.put(LocationsDB.LNG_COLUMN, point.longitude);
        newValues.put(LocationsDB.ZOOM_LEVEL_COLUMN, map.getCameraPosition().zoom);
        insertTask.execute(newValues);

        Toast.makeText(getApplicationContext(), "Marker is added to the map",
                Toast.LENGTH_SHORT).show();
    }

    private void delete() {
        map.clear();

        LocationDeleteTask deleteTask = new LocationDeleteTask();
        deleteTask.execute();

        Toast.makeText(getApplicationContext(), "All markers are removed",
                Toast.LENGTH_SHORT).show();
    }

    public void onClick_CS(View v) {
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_CS, 18);
        map.animateCamera(update);
    }

    public void onClick_Univ(View v) {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_UNIV, 14);
        map.animateCamera(update);
    }

    public void onClick_City(View v) {
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_UNIV, 10);
        map.animateCamera(update);
    }
}
