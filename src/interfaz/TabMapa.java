/*
* @Aplicación: Silav App Remises
 * @(#)TabMapa.java 0.1 28/07/14
 *
/**
 *
 * @autor Jeremías Chaparro
 * @version 0.1, 28/07/14
 * 
 **
 * @Modificaciones relevantes:
 * 09/09/14-Se refactoriza el código tratando de cumplir con Estandar - Jeremías Chaparro
 *
 */

package interfaz;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.SimpleLocationOverlay;

import com.appremises.R;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class TabMapa extends Fragment{
	
	private MapView mapView;

	public View onCreateView(LayoutInflater inflater, 
	        					ViewGroup container, 
	        					Bundle savedInstanceState){
		
		View v=inflater.inflate(R.layout.tab_mapa, container, false);
		
		setMapView((MapView) v.findViewById(R.id.mapaStreet));
		getMapView().setBuiltInZoomControls(true);
		getMapView().setMultiTouchControls(true);
		
		MapController mapControl = getMapView().getController();
		mapControl.setZoom(15);
		
		LocationManager locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
		Location l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(l!=null){
			GeoPoint miPuntoGeografico =  new GeoPoint(l);
			mapControl.setCenter(miPuntoGeografico);
		
			SimpleLocationOverlay miOverlay = new SimpleLocationOverlay(getActivity().getApplicationContext());
			getMapView().getOverlays().add(miOverlay);
			miOverlay.setLocation(miPuntoGeografico);
		}
		
		return v;
	}
	
	
	
	public MapView getMapView() {
		return mapView;
	}

	public void setMapView(MapView mapView) {
		this.mapView = mapView;
	}


}
