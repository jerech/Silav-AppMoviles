/*
* @Aplicación: Silav App Remises
 * @(#)PantallaPrincipal.java 0.1 28/07/14
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

import modelo.Chofer;
import modelo.Movil;

import com.appremises.R;


import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

public class PantallaPrincipal extends ActionBarActivity{

	private Tab tabMovil;
	private Tab tabMapa;
	private Tab tabPasajes;
	private Chofer chofer;
	private LocationListener locListener;
	private LocationManager locManager;
	final int NUM_NOTIFICACION_APP = 1; 

	

	@Override
	public void onCreate(Bundle savedIntanceState){
		super.onCreate(savedIntanceState);
	
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		
		TareaAsincronaNotificacion n = new TareaAsincronaNotificacion();
		n.execute("");
		
		Movil movil = new Movil();
		setChofer(new Chofer());
		getChofer().setMovil(movil);
		
		Bundle extras = getIntent().getExtras();
		if(extras!=null){
		    getChofer().setUsuario(extras.getString("usuario"));
		    getChofer().getMovil().setNumero(extras.getInt("numeroMovil"));	    
		}		
		
		
		
		TabsListener<TabMapa> listenerMapa = new TabsListener<TabMapa>(extras,this, "MAPA", TabMapa.class);
		setTabMapa(actionBar.newTab().setTabListener(listenerMapa));
		getTabMapa().setText("MAPA");
		actionBar.addTab(getTabMapa());
		
		TabsListener<TabMovil> listenerMovil = new TabsListener<TabMovil>(extras,this, "MOVIL", TabMovil.class);
		setTabMovil(actionBar.newTab().setTabListener(listenerMovil));
		getTabMovil().setText("MOVIL");
		actionBar.addTab(getTabMovil());
		
		TabsListener<TabPasaje> listenerPasaje = new TabsListener<TabPasaje>(extras,this, "PASAJES", TabPasaje.class);
		setTabPasajes(actionBar.newTab().setTabListener(listenerPasaje));
		getTabPasajes().setText("PASAJES");
		actionBar.addTab(getTabPasajes());		
		
		//Se determina que Tab se muestra al iniciar PantallaPrincipal
		actionBar.setSelectedNavigationItem(1);
		setearUbicacionMovil();
		
			
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		//Matener pantalla encendida o no
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		if(pref.getBoolean("pantalla", false)){
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}else{
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			}
		getWindow().getAttributes().screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE ;
	}
	
	@Override
	public void onBackPressed() {
		//Método vacio para que el botón back no haga nada
	 }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		MenuItem item = menu.getItem(2);
		item.setTitle(getChofer().getUsuario());  
		
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		
	    switch (item.getItemId()) {
	    case R.id.action_settings:
	        Intent miIntent = new Intent(PantallaPrincipal.this, Configuracion.class);
	        miIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        startActivity(miIntent);
	        return true;
	    case R.id.action_info:
	        Toast.makeText(getApplicationContext(), "INFO", Toast.LENGTH_SHORT).show();
	        return true;	
	    case R.id.action_salir:
	    	TareaAsincronaDesconectarChofer tareaAsincrona;	
			tareaAsincrona = new TareaAsincronaDesconectarChofer(PantallaPrincipal.this);
			tareaAsincrona.execute("");  
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		locManager.removeUpdates(locListener);
        NotificationManager gestorNotificacion = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		gestorNotificacion.cancel(NUM_NOTIFICACION_APP);
		Intent miIntent = new Intent(PantallaPrincipal.this, InicioSesion.class);
		miIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(miIntent);
		super.onDestroy();
		
	}
	
		
		protected void setearUbicacionMovil(){
			
			locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);		
	    	locListener = new ListenerUbicacion();   
	    	locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 28, locListener);
	    	
		}
		
	

	private class ListenerUbicacion implements LocationListener{
		int c=0;

		@Override
		public void onLocationChanged(Location location) {
			TareaAsincronaUbicacion nuevaTareaAsincrona = new TareaAsincronaUbicacion();
			getChofer().setUbicacionLatitud(location.getLatitude());
			getChofer().setUbicacionLongitud(location.getLongitude());
			nuevaTareaAsincrona.execute("");
			c++;
			Toast.makeText(getApplicationContext(), "Se envia ubicación:"+c,Toast.LENGTH_SHORT).show();
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "GPS está desactivado.", Toast.LENGTH_SHORT).show();
			
		}
		
	}
	
	private class TareaAsincronaUbicacion extends AsyncTask<String, Void, Object>{
			
			
			@Override
			protected Integer doInBackground(String... args){
				
				getChofer().actualizarUbicacion();
				
				return 1;
			}
	}
	
	private class TareaAsincronaDesconectarChofer extends AsyncTask<String, Void, Object>{
		
		private ProgressDialog progressDialog;
		
		public TareaAsincronaDesconectarChofer(Activity activity){
			
            this.progressDialog = new ProgressDialog(activity);
            this.progressDialog.setTitle("Desconectando.");
            this.progressDialog.setMessage("La sesión se está cerrando.");
            if(!this.progressDialog.isShowing()){
                this.progressDialog.show();
            }
		}
	
		protected Integer doInBackground(String... args){
			boolean desconectado = false;
			while(!desconectado){
				desconectado = getChofer().desconectarUsuario();
			}
			
			return 1;
		}
		
		protected void onPostExecute(Object result){
					
			//Se elimina la pantalla de por favor esperar
			this.progressDialog.dismiss();		
			
			finish();
			
		}		
					
	}
	
	private class TareaAsincronaNotificacion extends AsyncTask<String, Void, Object>{
		
		@Override
		protected Object doInBackground(String... params) {
			establecerNotificacionDeAplicacion();
			return null;
		} 
	
	private void establecerNotificacionDeAplicacion(){
			//Se crea la notificacion de la app
			NotificationCompat.Builder miConstructor = new NotificationCompat.Builder(PantallaPrincipal.this);
			miConstructor.setSmallIcon(R.drawable.ic_launcher);
			miConstructor.setContentTitle("AppMoviles");
			miConstructor.setContentText("Aplicación encendida");
			miConstructor.setContentInfo("Ok");
			miConstructor.setTicker("SiLAV");
			miConstructor.setOngoing(true);
		
			Intent intent = new Intent(PantallaPrincipal.this, PantallaPrincipal.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
			PendingIntent intentPendiente = PendingIntent.getActivity(PantallaPrincipal.this, 0, intent, 0);
			miConstructor.setContentIntent(intentPendiente);
			NotificationManager gestorNotificacion = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			gestorNotificacion.notify(NUM_NOTIFICACION_APP, miConstructor.build());
		}

	
	}


	public Tab getTabMovil() {
		return tabMovil;
	}

	public void setTabMovil(Tab tabMovil) {
		this.tabMovil = tabMovil;
	}

	public Tab getTabMapa() {
		return tabMapa;
	}

	public void setTabMapa(Tab tabMapa) {
		this.tabMapa = tabMapa;
	}

	public Tab getTabPasajes() {
		return tabPasajes;
	}

	public void setTabPasajes(Tab tabPasajes) {
		this.tabPasajes = tabPasajes;
	}
	
	public Chofer getChofer() {
		return chofer;
	}

	public void setChofer(Chofer chofer) {
		this.chofer = chofer;
	}
	
		
}
