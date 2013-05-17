package gov.houstontx.hfd;

//import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
//import android.widget.ListAdapter;
//import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.ListActivity;
import android.content.Intent;
import android.widget.ArrayAdapter;

public class HFDActivity extends ListActivity {
   
    String TAG = "TEST";
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incidents);


        String result = queryRESTurl("http://hfdapp.houstontx.gov/JSONTest/WebService1.asmx/GetIncidentsPOST");
        try {
            JSONObject json = new JSONObject(result);
            JSONArray nameArray = json.names();
            JSONArray valArray = json.toJSONArray(nameArray);
            JSONObject jsonInc = new JSONObject(valArray.getString(0));
            JSONArray valArrayInc = jsonInc.getJSONArray("ActiveIncidentDataTable");    //valArray.getJSONObject(0).getJSONArray("d");

            // Set values globally
            GlobalState gs = (GlobalState) getApplication(); 
            gs.setIncidents(valArrayInc);
           
            String[] incList = new String[valArrayInc.length()];

            for (int i = 0; i < valArrayInc.length(); i++) {
                incList[i] = valArrayInc.getJSONObject(i).getString("IncidentType");
            //    Log.i(TAG, "<jsonname" + i + ">\\n" + nameArray.getString(0) + "\\n</jsonname" + i + ">\\n" + "<jsonvalue" + i + ">\\n" + valArrayInc.getString(i) + "\\n</jsonvalue" + i + ">");
            }


           // ListView lst = (ListView)findViewById(R.id.txtList);

            this.setListAdapter(new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, incList ));

        }
        catch (JSONException e) {
            Log.e("JSON", "There was an error parsing the JSON", e);
        }

    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		
		case R.id.mapincidents:
			Intent i = new Intent(HFDActivity.this, MapIncidentsActivity.class);
			startActivity(i);
			break;
			
		case R.id.preferences:
			// Launch Preference activity
			/*
			Intent i = new Intent(HelloPreferences.this, Preferences.class);
			startActivity(i);
			// Some feedback to the user
			Toast.makeText(HelloPreferences.this,
					"Here you can enter your user credentials.",
					Toast.LENGTH_LONG).show();
			*/
			Toast.makeText(this, "Just a test", Toast.LENGTH_SHORT).show();
			break;

		}

		
		return true;
	}


    public String queryRESTurl(String url) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");

        HttpResponse response;
        try
        {
            response = httpclient.execute(httppost);
            Log.i(TAG, "Status:[" + response.getStatusLine().toString() + "]");
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                String result = convertStreamToString(instream);
                Log.i(TAG, "Result of conversion: [" + result + "]");
                instream.close();
                return result;
        }
        } catch (ClientProtocolException e) {
            Log.e("REST", "There was a protocol based error", e);
        } catch (IOException e) {
            Log.e("REST", "There was an IO Stream related error", e);
        }
        return null;
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {

            try {
                is.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

}