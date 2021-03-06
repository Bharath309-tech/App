package com.example.notificationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private TextView view;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText) findViewById((R.id.username));
        password = (EditText) findViewById(R.id.password);
        view =(TextView) findViewById(R.id.view);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    // CALL GetText method to make post method call
                    GetText();
                    // getLogin();
                }
                catch(Exception ex)
                {
                    view.setText(" url exeption! " );
                }

            }
        });
    }

    private void GetText()  throws UnsupportedEncodingException {

        final String uname = username.getText().toString();
        final String pass = password.getText().toString();

        if (uname.equalsIgnoreCase("")) {

            username.setError("Enter Username");
        }
        if (pass.equalsIgnoreCase("")) {
            password.setError("Enter Password");
        } else {

            String url = "http://10.0.2.2:3000/login";

            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // view.setText("Login Success");
                    System.out.println(response);
                    String[] name = response.split("\"");
                    openActivity(name[7]);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    view.setText("Invalid Username Or Password");
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", uname);
                    params.put("password", pass);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    // params.put("Content-Type","application/json; charset=utf-8");
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }

    }

    private void openActivity(String  uname) {
        Intent intent = new Intent(this,Activity2.class);
        intent.putExtra("NAME",uname);
        startActivity(intent);
    }

    protected void getLogin() {
        final String mURL = "http://10.0.2.2:3000/login";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        // Post params to be sent to the server
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username.getText().toString());
        params.put("password", password.getText().toString());

        JsonObjectRequest req = new JsonObjectRequest(mURL, new JSONObject(
                params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject obj = response
                            .getJSONObject("some_json_obj");
                    view.setText("Login Succesful");

                    Log.w("myApp",
                            "status code..." + obj.getString("name"));

                    // VolleyLog.v("Response:%n %s", response.toString(4));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("error in response", "Error: " + error.getMessage());

                view.setText(error.toString());
            }
        });

        // add the request object to the queue to be executed
        requestQueue.add(req);
    }


}

