package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendNotification extends AppCompatActivity
{

    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);


        if (getIntent().hasExtra("category"))
        {
            Intent intent = new Intent(SendNotification.this,ReceiveNotification.class);
            intent.putExtra("category",getIntent().getStringExtra("category"));
            intent.putExtra("brandId",getIntent().getStringExtra("brandId"));
            startActivity(intent);
        }
        Button button = findViewById(R.id.btn);
        mRequestQue = Volley.newRequestQueue(this);
        FirebaseMessaging.getInstance().subscribeToTopic("news");

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendNotification();
            }
        });

    }




    private void sendNotification()
    {

        JSONObject json = new JSONObject();
        try {
            json.put("to","/topics/"+"news");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","KR651");
            notificationObj.put("body","New Upload!");

            JSONObject extraData = new JSONObject();
            extraData.put("brandId","puma");
            extraData.put("category","Shoes");


            json.put("notification",notificationObj);
            json.put("data",extraData);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response)
                        {

                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    Log.d("MUR", "onError: "+error.networkResponse);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError
                {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAA8ZDWC-k:APA91bGisjf7EM_TdsIjaCC2uC4En0ugPz05-kxlLYuMnLzEOfFQYqv7myq1fSgHjIfXewqpAzyd3565McCgJ6GjUDaCMc2KzJKEd5BkuM_rNsqpPlrpcZbc4m8nl1pbcXSBY7PG-hLL");
                    return header;
                }
            };
            mRequestQue.add(request);
        }
        catch (JSONException e)

        {
            e.printStackTrace();
        }
    }
}
