package gov.houstontx.hfd;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
//import android.widget.RelativeLayout;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapIncidentsActivity extends MapActivity {

	private MapController mapController;
	private MapView mapView;
	private LocationManager locationManager;
	
	List<Overlay> mapOverlays;
	Drawable drawable;
	Drawable drawableEms;
	MapItemizedOverlay itemizedOverlay;
	MapItemizedOverlay itemizedOverlayEms;
	JSONArray j;
	String currentFilter = "all";

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.map_incidents); // bind the layout to the activity

		// create a map view
		//RelativeLayout linearLayout = (RelativeLayout) findViewById(R.id.mainlayout);
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		//mapView.setStreetView(true);
		mapController = mapView.getController();
		GeoPoint houston = new GeoPoint(29762778, -95383056);
		mapController.setCenter(houston);
		mapController.setZoom(11); // Zoom 1 is world view
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new GeoUpdateHandler());
		
		 // Get values 
        GlobalState gs = (GlobalState) getApplication(); 
        j = gs.getIncidents();
        
        mapOverlays = mapView.getOverlays();
        drawable = this.getResources().getDrawable(R.drawable.fire);
        drawableEms = this.getResources().getDrawable(R.drawable.ems);
        drawableEms.setBounds(0, 0, drawableEms.getIntrinsicWidth(), drawableEms.getIntrinsicHeight());
        itemizedOverlay = new MapItemizedOverlay(drawable, this);      
        
        drawIncidents("all");
        		
	}
	
	protected void drawIncidents(String filter) {
		GeoPoint point; // = new GeoPoint(19240000,-99120000);
        OverlayItem overlayitem; // = new OverlayItem(point, "", "");
        
        if (itemizedOverlay.size() > 0) {
        	itemizedOverlay.clear();
        }
        
               
        for (int i = 0; i < j.length(); i++) {
            //incList[i] = valArrayInc.getJSONObject(i).getString("IncidentType");
            //    Log.i(TAG, "<jsonname" + i + ">\\n" + nameArray.getString(0) + "\\n</jsonname" + i + ">\\n" + "<jsonvalue" + i + ">\\n" + valArrayInc.getString(i) + "\\n</jsonvalue" + i + ">");
        	
        	
        	int lat = 0;
        	String eventType = "";
        	
        	try {
				eventType =j.getJSONObject(i).getString("IncidentType");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	if (filter.compareToIgnoreCase("all") == 0 || 
        	   (filter.compareToIgnoreCase("ems") == 0 && eventType.compareToIgnoreCase("Ems Event") == 0) || 
        	   (filter.compareToIgnoreCase("ems") != 0 && eventType.compareToIgnoreCase("Ems Event") != 0)) {        		
        		try {
					lat = getMicroDegrees(j.getJSONObject(i).getString("YCoord"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
	        	int lon = 0;
				try {
					lon = getMicroDegrees(j.getJSONObject(i).getString("XCoord"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
				
				String address = "";
				try {
					address =j.getJSONObject(i).getString("Address");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String units = "";
				try {
					units =j.getJSONObject(i).getString("Units");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
				if (lat != 0 && lon != 0) {
					point = new GeoPoint(lat, lon);
					
					if (eventType.compareToIgnoreCase("Ems Event") == 0)
					{
						overlayitem = new OverlayItem(point, eventType, address + "\r\n" + "Units: " + units);
						
						overlayitem.setMarker(drawableEms);
						itemizedOverlay.addOverlay(overlayitem);
					}
					else {
						overlayitem = new OverlayItem(point, eventType, address + "\r\n" + "Units: " + units);
						itemizedOverlay.addOverlay(overlayitem);
					}					
					
				}			
        	} //filter        	
        }  //for
            
        if(!mapOverlays.isEmpty())       
        {       
        	mapOverlays.clear();       
        	mapView.invalidate();   
        }
        
        mapOverlays.add(itemizedOverlay);
	}
	
	protected int getMicroDegrees(String s) {
		double d;
		 
		int ret = 0;
		
		try 
		{
			d = Double.parseDouble(s);
			
			ret = (int)(d);
		}
		catch (NumberFormatException e) 
		{
			
		}
		
		return ret;
		
			
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	public void filterFire(View view) {
		 drawIncidents("fire");
	 }
	
	public void filterEms(View view) {
		 drawIncidents("ems");
	 }

	public class GeoUpdateHandler implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			int lat = (int) (location.getLatitude() * 1E6);
			int lng = (int) (location.getLongitude() * 1E6);
			GeoPoint point = new GeoPoint(lat, lng);
			mapController.animateTo(point); //	mapController.setCenter(point);
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}
}
