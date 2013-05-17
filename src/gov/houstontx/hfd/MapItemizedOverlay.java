package gov.houstontx.hfd;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MapItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	
	public MapItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}
	public MapItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		 mContext = context;
	}

	public void addOverlay(OverlayItem overlay) {    
		mOverlays.add(overlay);    
		populate();
		
	}
	
	public void clear() {
		mOverlays.clear();
		populate();
	}
	
	

	@Override
	public int size() {
		return mOverlays.size();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}
	
	@Override 
    protected boolean onTap(int i) { 
		OverlayItem item = mOverlays.get(i);  
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);  
		dialog.setTitle(item.getTitle());  
		dialog.setMessage(item.getSnippet());  
		dialog.show();		
		return true;
		
		//startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + mLatitude + "," + mLongitude)));
    } 

	 

}
