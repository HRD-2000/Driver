package com.example.WhereIsMyDriver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity {

    String result,drop_down_url,header;
    List <dropdown_pojo> model;
    Button button1;
    CircleImageView circleImageView;
    AutoCompleteTextView autoCompleteTextView;
    Spinner spinner;
    LoadingWithAnim loadingDialog;
    TextView startLoc,endLoc;


    public static final int REQUEST_CHECK_SETTING = 101;
    public static final int REQUEST_CHECK_SETTING_1 = 102;
    private String[] permission = {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};
    LocationRequest locationRequest;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getPermission();
        //Route_user.setRoutes();

        circleImageView = findViewById(R.id.circleImageView);
        spinner = findViewById(R.id.spinner);
        button1 = findViewById(R.id.button1);
        header = getString(R.string.header);
        startLoc = findViewById(R.id.textView4);
        endLoc = findViewById(R.id.textView5);

        drop_down_url = header + "route_dropdown.php";
        loadingDialog = new LoadingWithAnim(Home.this);

        new retrieve().execute();






        /*circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this,MapsActivity.class);
                startActivity(intent);
            }
        });*/

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this,MapsActivity.class);
                startActivity(intent);
            }
        });





        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(Home.this,""+model.get(position).getDriver_start_loc(),Toast.LENGTH_SHORT).show();
                String start_loc = model.get(position).getDriver_start_loc();
                String end_loc = model.get(position).getDriver_end_loc();

                startLoc.setText(start_loc);
                endLoc.setText(end_loc);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





    }

    private void getPermission() {

        Toast.makeText(this, "In getPermission method", Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permission,REQUEST_CHECK_SETTING);
            }else {
                Toast.makeText(this, "Permission Granted already 1", Toast.LENGTH_SHORT).show();
                getTurnOnGps();
                //startActivity(new Intent(Home.this,MapsActivity.class));
            }
        }else {
            Toast.makeText(this, "Permission Granted already 2", Toast.LENGTH_SHORT).show();
            getTurnOnGps();
            //startActivity(new Intent(Home.this,MapsActivity.class));

        }
    }

    private void getTurnOnGps() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response =task.getResult(ApiException.class);
                    Toast.makeText(Home.this, "GPS is On!!", Toast.LENGTH_SHORT).show();
                    Log.d("test", "onComplete: GPS is on");
                    //startActivity(new Intent(home.this,MapsActivity.class));
                } catch (ApiException e) {
                    switch (e.getStatusCode()){

                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                            try {
                                resolvableApiException.startResolutionForResult(Home.this,REQUEST_CHECK_SETTING_1);
                            } catch (IntentSender.SendIntentException sendIntentException) {

                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            }
        });




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("test", "onRequestPermissionsResult: 1 invoked");
       if (requestCode == REQUEST_CHECK_SETTING){
           Log.d("test", "onRequestPermissionsResult: 2 invoked");
           if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
               Log.d("test", "onRequestPermissionsResult: 3 invoked");
               getTurnOnGps();
           }else {
               Log.d("test", "onRequestPermissionsResult: 4 invoked");
               getTurnOnGps();
           }
       }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_CHECK_SETTING_1){

                switch (resultCode){

                    case Activity.RESULT_OK:
                        Toast.makeText(getApplicationContext(),"GPS is Turned ON",Toast.LENGTH_SHORT).show();
                        Log.d("test", "onActivityResult: GPS is Turned On");
                        //startActivity(new Intent(home.this,MapsActivity.class));
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(this, "GPS is required to be turned on", Toast.LENGTH_SHORT).show();
                        Log.d("test", "onActivityResult: GPS is rquired to be turned on");
                }

            }
    }

    public class retrieve extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loadingDialog.startLoadingDialog();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try
            {
                JsonParser o = new JsonParser();
                result = o.insert(drop_down_url);
                model = new ArrayList<>();

                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("res");

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject11 = jsonArray.getJSONObject(i);
                    dropdown_pojo p = new dropdown_pojo();
                    p.setDriver_id(jsonObject11.getString("driver_id"));
                    p.setDriver_profile_pic(jsonObject11.getString("driver_profile_pic"));
                    p.setDriver_end_loc(jsonObject11.getString("driver_end_loc"));
                    p.setDriver_start_loc(jsonObject11.getString("driver_start_loc"));
                    model.add(p);
                }
            }
            catch ( JSONException e)
            {
                e.printStackTrace();
                //  Toast.makeText(Login.this, "Please check your Internet Connection and Retry", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadingDialog.dismissDialog();
            Spinner_Adapter spinnerAdapter = new Spinner_Adapter(Home.this,R.layout.drop_down,model);
            spinner.setAdapter(spinnerAdapter);

            //SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
            //String start_loc = sharedPreferences.getString("s_loc","");
            //String end_loc = sharedPreferences.getString("e_loc","");

            //startLoc.setText(start_loc);
            //endLoc.setText(end_loc);
        }
    }

}