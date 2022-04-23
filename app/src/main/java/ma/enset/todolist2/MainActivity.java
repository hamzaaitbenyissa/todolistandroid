package ma.enset.todolist2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String myUrl = "http://192.168.1.13:8082/tasks";
    TextView TaskField;
    ProgressDialog progressDialog;
    Button addtaskbutton;
    RecyclerView recyclerView;
    ArrayList<Task> tasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TaskField = (TextView) findViewById(R.id.TaskField);
        addtaskbutton = (Button) findViewById(R.id.addtaskbutton);
        recyclerView = findViewById(R.id.recyclerView);

        MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
        myAsyncTasks.execute();

//         implement setOnClickListener event on addtaskbutton button
        addtaskbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create object of MyAsyncTasks class and execute it

                AddAsyncTasks addAsyncTasks = new AddAsyncTasks();
                addAsyncTasks.execute();
            }
        });


    }


    public class MyAsyncTasks extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog to show the user what is happening

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Fetching Data");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            // Fetch data from the API in the background.

            String result = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(myUrl);
                    //open a URL coonnection

                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader isw = new InputStreamReader(in);
                    int data = isw.read();

                    while (data != -1) {
                        result += (char) data;
                        data = isw.read();

                    }

                    // return the data to onPostExecute method
                    return result;

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            // show results

            // dismiss the progress dialog after receiving data from API
            progressDialog.dismiss();
            try {
                tasks.clear();

                JSONArray jsonArray = new JSONArray(s);

                for (int i = 0; i < jsonArray.length(); i++) {
                    tasks.add(new Task(jsonArray.getJSONObject(i).getString("id"),
                            jsonArray.getJSONObject(i).getString("description")));
                }

                TaskField.setText("");

                TaksAdapter TaksAdapter = new TaksAdapter(MainActivity.this, MainActivity.this, tasks);
                recyclerView.setAdapter(TaksAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));


                //Display data with the Textview

                System.out.println("Tasks" + tasks.toString());
            } catch (Exception e) {
                TaskField.setText("An error occurred ! ");
                System.out.println("An error occurred ! ");
                e.printStackTrace();

            }

        }
    }


    public class AddAsyncTasks extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog to show the user what is happening

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Adding Task");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            String result = "";

            try {

                URL url;
                HttpURLConnection urlConnection = null;
                url = new URL(myUrl);

                //open a URL coonnection
                urlConnection = (HttpURLConnection) url.openConnection();

                //JSON String need to be constructed for the specific resource.
                //We may construct complex JSON using any third-party JSON libraries such as jackson or org.json

                try {
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");
                    urlConnection.setRequestProperty("Accept", "application/json");

                    
                    String jsonInputString = "{\"description\": \""+TaskField.getText().toString()+"\"}";

                    try (OutputStream os = urlConnection.getOutputStream()) {
                        byte[] input = jsonInputString.getBytes("utf-8");
                        os.write (input, 0, input.length);
                    }

                    int code = urlConnection.getResponseCode();
                    System.out.println("getResponseCode " + code);


                } catch (Exception e) {
                    System.out.println("post methode exception");
                    e.printStackTrace();

                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            // Fetch data from the API in the background.


            return "result";
        }

        @Override
        protected void onPostExecute(String s) {

            // show results

            // dismiss the progress dialog after receiving data from API
            progressDialog.dismiss();
            try {
                MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
                myAsyncTasks.execute();

            } catch (Exception e) {
                System.out.println("ors matawyaa");
                e.printStackTrace();

            }

        }
    }


}
