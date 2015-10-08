package com.chase.chaseatmlocator.framework;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.*;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.chase.chaseatmlocator.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class BaseFragment  extends Fragment{

    private LocationManager locationManager;
    private String jsonResponse;
    private Response response;
    private ListView listView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.current_location,container,false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Singelton.getInstance().setPosition(position);
                Intent i = new Intent(getActivity(),DetailsActivity.class);
                startActivity(i);
            }
        });
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L,
                500.0f, locationListener);
        Location location = locationManager
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            new NearestLocationAsynctask(latitude,longitude).execute();
        }else{
            Toast.makeText(getActivity(), "Unable to determine location", Toast.LENGTH_LONG).show();
        }
    }

    protected class NearestLocationAsynctask extends AsyncTask<Void,Void,Void> {

        private final double latitute;
        private final double longitude;

        public NearestLocationAsynctask(double latitute,double longitude){
            this.latitute = latitute;
            this.longitude = longitude;
        }
        public String uriBuilder(double latValue, double lngValue) {

            StringBuilder builder = new StringBuilder();
            builder.append("https://m.chase.com/PSRWeb/location/list.action?");
            builder.append("lat=");
            builder.append(latValue);
            builder.append("&lng=");
            builder.append(lngValue);
            return builder.toString();
        }
        private ProgressDialog progressDialog = new ProgressDialog(getActivity());
        private String message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "Wait", "Getting location details and nearest ATM locations near you");
        }



        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            Singelton.getInstance().setResponse(response);
            if(!response.getLocations().isEmpty()) {
                listView.setAdapter(new ListAdapter(getActivity(), response.getLocations()));
            }else{
                Toast.makeText(getActivity(),"No branches available at your location", Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL(uriBuilder(latitute,longitude));
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                jsonResponse = convertInputStreamToString(inputStream);
                response = jsonToResponse(jsonResponse);
            }catch (Exception e){
                message = e.getMessage();
            }
            return null;
        }
    }


    protected String convertInputStreamToString(InputStream inputstream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream), 1024);
        try {
            return readLines(reader);
        } finally {
            reader.close();
        }
    }


    protected Response jsonToResponse(String jsonResponse) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonResponse, Response.class);

    }


    protected String readLines(BufferedReader reader) throws IOException {
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line + '\n');
        }
        return builder.toString();
    }

    protected void updateWithNewLocation(android.location.Location location) {

        String latLongString = "";
        if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            latLongString = "Lat:" + lat + "\nLong:" + lng;
        } else {
            latLongString = "No location found";
        }
        //myLocationText.setText("Your Current Position is:\n" + latLongString);
    }

    private final LocationListener locationListener = new LocationListener() {

        public void onLocationChanged(android.location.Location location) {
            updateWithNewLocation(location);
        }

        public void onProviderDisabled(String provider) {
            updateWithNewLocation(null);
        }

        public void onProviderEnabled(String provider) {}

        public void onStatusChanged(String provider,int status,Bundle extras){}
    };

}
