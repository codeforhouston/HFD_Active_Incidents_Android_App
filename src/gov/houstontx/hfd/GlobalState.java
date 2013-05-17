package gov.houstontx.hfd;

//import java.util.ArrayList; 
//import java.util.List; 

import org.json.JSONArray;

//import com.google.android.maps.GeoPoint; 
import android.app.Application;   

public class GlobalState extends Application 
{ 
	private JSONArray jsonIncidents;
	
	public JSONArray getIncidents() 
	{ 
		return jsonIncidents; 
	} 
	
	public void setIncidents(JSONArray incidents) 
	{ 
		this.jsonIncidents = incidents; 
	} 
	
} 
