package com.example.WhereIsMyDriver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button login_button;
    String username,password;

    EditText edt_password,edt_username;
    LoadingWithAnim loadingDialog;

    String result , login_url;

    SharedPreferences mSP;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor editor;

    ArrayList<Login_pojo> model;

    String header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login_button = (Button)findViewById(R.id.login_button);
        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_password = (EditText) findViewById(R.id.edt_password);
        TextView textView = (TextView)findViewById(R.id.textView);

        mSP = getSharedPreferences("login", Context.MODE_PRIVATE);
        header = getString(R.string.header);


        loadingDialog = new LoadingWithAnim(MainActivity.this);


        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Toast.makeText(getApplicationContext(),"NAHI MILENGA PASSWORD",Toast.LENGTH_SHORT).show();
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
               // startActivity(new Intent(MainActivity.this, Home.class));

                if (edt_username.getText().toString().length()==0 ) {
                    edt_username.setError("Username can't be empty");
                }else if(edt_password.getText().toString().isEmpty()){
                    edt_password.setError("Password can't be empty");
                }else{
                    login_url = header+"driver_login.php?driver_username="+edt_username.getText().toString();
                    new retrieve().execute();

                    Log.v("Login",""+login_url);

                }

            }
        });


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
                result = o.insert(login_url);
                model = new ArrayList<>();

                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("res");

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject11 = jsonArray.getJSONObject(i);
                    Login_pojo p = new Login_pojo();

                    p.setUsername(jsonObject11.getString("driver_username"));
                    p.setPassword(jsonObject11.getString("driver_password"));
                    model.add(p);

                    username = p.getUsername();
                    password = p.getPassword();

                    Log.v("Login_DATA",""+username+" "+password);

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

            if (edt_username.getText().toString().equals(username) && edt_password.getText().toString().equals(password))
            {
                Toast.makeText(MainActivity.this, " Login sucessful... ", Toast.LENGTH_SHORT).show();
                loadingDialog.dismissDialog();
                Intent intent = new Intent(MainActivity.this, Home.class);

                startActivity(intent);

                mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                editor = mSharedPreferences.edit();
                editor.putString("email_id",edt_username.getText().toString());
                editor.putString("password",edt_password.getText().toString());
                editor.commit();

                mSP.edit().putBoolean("logged",true).apply();

                Log.d("sucess",""+password);
                Log.d("sucess",""+username);


            }
            else{
                loadingDialog.dismissDialog();
                Toast.makeText(MainActivity.this, " Email or Password is incorrect!! ", Toast.LENGTH_SHORT).show();
            }



        }
    }
}