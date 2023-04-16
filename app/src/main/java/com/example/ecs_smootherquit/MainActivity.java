package com.example.ecs_smootherquit;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /********************************/
        /*    Define all the buttons    */
        /********************************/
        DBHelper db=new DBHelper(this);


        Switch leds1 = (Switch) findViewById(R.id.LedS1);
        Switch leds2 = (Switch) findViewById(R.id.LedS2);
        Switch leds3 = (Switch) findViewById(R.id.LedS3);
        Button led3 = (Button) findViewById(R.id.Led3);
        TextView tv = (TextView)findViewById(R.id.textView);

         // Add 1 to month since January is represented by 0
        String i;

        leds1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    /* Switch is led 1 */

                    new Background_get().execute("leds1=1");
                    tv.setText(returnDate("var1"));

                    String txt=returnDate("var1");
                    Boolean check=db.insertData(txt);
                    if(check==true){
                        Toast.makeText(MainActivity.this, "Insert successful", Toast.LENGTH_SHORT).show();
                    }}
                else new Background_get().execute("led1=0");


            }
        });
        leds2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    /* Switch is led 1 */
                    new Background_get().execute("leds2=1");
                    tv.setText(returnDate("var2"));

                    String txt=returnDate("var2");
                    Boolean check=db.insertData(txt);
                    if(check==true){
                        Toast.makeText(MainActivity.this, "Insert successful", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    new Background_get().execute("leds2=0");
                }
            }
        });
        leds3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    /* Switch is led 1 */
                    new Background_get().execute("leds3=1");
                    tv.setText(returnDate("var3"));

                    String txt=returnDate("var3");
                    Boolean check=db.insertData(txt);
                    if(check==true){
                        Toast.makeText(MainActivity.this, "Insert successful", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    new Background_get().execute("leds3=0");
                }
            }
        });


        Button view = (Button) findViewById(R.id.Usage);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res=db.getData();
                if(res.getCount()==0){
                    Toast.makeText(MainActivity.this, "No entry", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuffer buffer=new StringBuffer();
                while(res.moveToNext()){
                    buffer.append("Date: "+res.getString(0)+"\n");
                }
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("User date entries");
                builder.setMessage(buffer.toString());
                builder.show();
            }
        });



        led3.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                new Background_get().execute("led3=1");
                tv.setText(returnDate("test3"));

                String txt=returnDate("test1");
                Boolean check=db.insertData(txt);
                if(check==true){
                    Toast.makeText(MainActivity.this, "Insert successful", Toast.LENGTH_SHORT).show();
                }

//                String theurl = "http://192.168.97.248/index.php?led3=1";
//                Uri urlstr = Uri.parse(theurl);
//                Intent urlintent = new Intent();
//                urlintent.setData(urlstr);
//                urlintent.setAction(Intent.ACTION_VIEW);
//                startActivity(urlintent);

                //httpCall("192.168.97.248/index.php?led3=1");



            }
        });


        mTextView = findViewById(R.id.sensor_data_text_view);

        Button button = findViewById(R.id.get_data_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadTask().execute(URL);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String text = mTextView.getText().toString();
                        // Do something with the text, such as pushing it to a database
                        // Hide the progress bar or indication

                        String txt=returnDate("button api");
                        String txt2 = mTextView.getText().toString();
                        String textf = txt+txt2;
                        Boolean check=db.insertData(textf);
                        if(check==true){
                            Toast.makeText(MainActivity.this, "Insert successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 5000);
            }



        });
    }
//    public void httpCall(String url){
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // enjoy your response
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // enjoy your error status
//            }
//        });
//
//        queue.add(stringRequest);
//    }
    private class Background_get extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                /* Change the IP to the IP you set in the arduino sketch */
                URL url = new URL("http://192.168.94.248/index.php?" + params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    result.append(inputLine).append("\n");

                in.close();
                connection.disconnect();
                return result.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public String returnDate(String i){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);

        String date = day + "/" + (month + 1) + "/" + year+"   "+hour+":"+min+":"+sec+"  "+i;
        return date;

    }




    /*
    HERE IS API REQUEST AND RESPONSE
     */

    private static final String URL = "http://192.168.94.248/index.php?var1=1";

    private TextView mTextView;

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                result = convertStreamToString(in);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return result;
        }

        protected void onPostExecute(String result) {
            mTextView.setText(result);
            String txt=returnDate("button api");
        }

        private String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return sb.toString();
        }
    }

}