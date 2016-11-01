package co.icoms.test;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class MainActivity extends AppCompatActivity {
    Button button;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppController.getInstance().setTablet(this);

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                requestData();
                int currentOrientation = getResources().getConfiguration().orientation;
                if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                }
                else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                }
            }
        });

        setProgressDialog();
    }

    private void setProgressDialog(){
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Obteniendo Información");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                AppController.getInstance().cancelAllRequests();
            }
        });
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(AppController.getInstance().isTablet()){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
                }
            }
        });
    }

    private void requestData(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://jsonplaceholder.typicode.com/photos",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        button.setText("Request Complete");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                button.setText("Request Error");
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
