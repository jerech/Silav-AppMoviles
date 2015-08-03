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

import java.util.Timer;
import java.util.concurrent.TimeUnit;

import modelo.Pasaje;
import modelo.Usuario;
import modelo.Movil;

import com.appremises.R;

import constantes.Estados;
import controladores.GcmIntentService;
import controladores.TimerEnviarUbicacion;
import controladores.WebService;
import dao.PasajeDAO;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PantallaPrincipal extends ActionBarActivity{

	public static final String PASAJE_PENDIENTE_RESPUESTA = "pasaje_pendiente_respuesta";
	public static final String NOMBRE_SHARED_PREFERENCE = "preferencias_principal";
	private Tab tabMovil;
	private Tab tabMapa;
	private Tab tabPasajes;
	Spinner spinnerEstados;
	TextView txtDireccion;
	TextView txtCliente;
	TextView txtHora;
	private static Usuario usuario;
	private LocationListener locListener;
	private LocationManager locManager;
	final int NUM_NOTIFICACION_APP = 1; 
	TextView txtCronometro;
	protected Pasaje pasaje;
	protected Dialog dialog;
	private boolean isDoInBackground;
	private boolean notificacionCorriendo = false;
	private Timer timer;
	protected ActionBar actionBar;
	protected SharedPreferences prefs;
	protected SharedPreferences prefsPrincipal;
	MediaPlayer mp;

	@Override
	public void onCreate(Bundle savedIntanceState){
		super.onCreate(savedIntanceState);
	
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		pasaje = new Pasaje();
		
		Movil movil = new Movil();
		usuario = new Usuario(Estados.LIBRE);
		getUsuario().setUbicacionLatitud(0.1);
		getUsuario().setUbicacionLongitud(0.1);
		getUsuario().setMovil(movil);
	
		
		Bundle extras = getIntent().getExtras();
		if(extras!=null){
		    getUsuario().getMovil().setNumero(extras.getInt("numeroMovil"));	    
		}		
		prefs = getSharedPreferences(
				InicioSesion.NOMBRE_SHARED_PREFERENCE,
				Context.MODE_PRIVATE);
		prefsPrincipal = getSharedPreferences(PantallaPrincipal.NOMBRE_SHARED_PREFERENCE, Context.MODE_PRIVATE);
		
		String usuarioGuardado = prefs.getString(InicioSesion.PROPERTY_USER, "none");
		getUsuario().setUsuario(usuarioGuardado);
		
		
		
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
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setearUbicacionMovil();
		
		//Registramos en intent service para  poder recibir la respuesta en el broadcast receiver
		IntentFilter filter = new IntentFilter();
		filter.addAction(GcmIntentService.ACTION_PASAJE);
		PasajeReceiver rcv = new PasajeReceiver();
		registerReceiver(rcv, filter);
		
		TimerEnviarUbicacion timerTask = new TimerEnviarUbicacion(usuario, getApplicationContext());
		timer = new Timer();
		timer.scheduleAtFixedRate(timerTask, 0, 5000);
		
		
			
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		isDoInBackground = true;
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if(notificacionCorriendo==true){
			NotificationManager gestorNotificacion = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			gestorNotificacion.cancel(NUM_NOTIFICACION_APP);
			notificacionCorriendo = false;
		}
			
		isDoInBackground = false;
		
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
		item.setTitle(getUsuario().getUsuario());  
		
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
		timer.cancel();
		timer.purge();
        
		super.onDestroy();
		
	}
	
		
	protected void setearUbicacionMovil(){
			
		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);		
    	locListener = new ListenerUbicacion();   
    	locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 28, locListener);
	    	
	}
		
	

	private class ListenerUbicacion implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			
			getUsuario().setUbicacionLatitud(location.getLatitude());
			getUsuario().setUbicacionLongitud(location.getLongitude());
					
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		
			if(status == LocationProvider.OUT_OF_SERVICE || 
					status == LocationProvider.TEMPORARILY_UNAVAILABLE){
				getUsuario().setEstado(Estados.INACTIVO);
			}
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
	
	
	private class TareaAsincronaDesconectarChofer extends AsyncTask<String, Void, Object>{
		
		private ProgressDialog progressDialog;
		private WebService ws;
		
		public TareaAsincronaDesconectarChofer(Activity activity){
			
            this.progressDialog = new ProgressDialog(activity);
            this.progressDialog.setTitle("Desconectando...");
            this.progressDialog.setMessage("La sesión se está cerrando.");
            if(!this.progressDialog.isShowing()){
                this.progressDialog.show();
            }
		}
	
		protected Integer doInBackground(String... args){
			ws = new WebService();
			boolean desconectado = false;
			while(!desconectado){
				desconectado = ws.desconectarUsuario(getUsuario());
			}
			
			return 1;
		}
		
		protected void onPostExecute(Object result){
					
			//Se elimina la pantalla de por favor esperar
			this.progressDialog.dismiss();		
			Intent miIntent = new Intent(PantallaPrincipal.this, InicioSesion.class);
			miIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			startActivity(miIntent);
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
			miConstructor.setContentTitle("Silav");
			miConstructor.setContentText("Nuevo Pasaje");
			miConstructor.setContentInfo("Aceptar o rechazar el pasaje");
			miConstructor.setVibrate(new long[] { 1000, 1000, 1000, 1200 });
			miConstructor.setTicker("SiLAV");
			miConstructor.setOngoing(false);
		
			notificacionCorriendo = true;
			
			Intent intent = new Intent(PantallaPrincipal.this, PantallaPrincipal.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
			PendingIntent intentPendiente = PendingIntent.getActivity(PantallaPrincipal.this, 0, intent, 0);
			miConstructor.setContentIntent(intentPendiente);
			NotificationManager gestorNotificacion = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			gestorNotificacion.notify(NUM_NOTIFICACION_APP, miConstructor.build());
			
			
		}

	
	}
	
	public class PasajeReceiver extends BroadcastReceiver {
		 
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        if(intent.getAction().equals(GcmIntentService.ACTION_PASAJE)) {
	        	Bundle extras = intent.getExtras();
	            mostrarMensajePasaje(extras.getString("direccion"), extras.getString("cliente"), extras.getInt("id"), extras.getString("fecha"));
	        
	        }
	    }
	}
	
	private void mostrarMensajePasaje(String direccion, String cliente, int id, String fecha){
		actionBar.setSelectedNavigationItem(1);
		mp = MediaPlayer.create(this, R.raw.alarma_pasaje);
		mp.setVolume((float)1, (float)1);
		mp.start();
		
		
		if(isDoInBackground == true){
			TareaAsincronaNotificacion taNotificacion = new TareaAsincronaNotificacion();
			taNotificacion.execute("");
		}
		
		// custom dialog 
		Log.d("fecha",fecha.toString());
		pasaje.setId(id);
		pasaje.setFecha(fecha);
		pasaje.setCliente(cliente);
		pasaje.setDireccion(direccion);
		
		
		int androidVersion = Build.VERSION.SDK_INT;
		
		Log.d("Version: ", "El numero de version es: "+androidVersion);
		
		if(androidVersion < Build.VERSION_CODES.ICE_CREAM_SANDWICH){
			dialog = new Dialog(PantallaPrincipal.this, R.style.Theme_Base_AppCompat_Dialog_Light_FixedSize);
		}else{
			dialog = new Dialog(PantallaPrincipal.this);
		}
		
		dialog.setContentView(R.layout.dialogo_pasaje);
		dialog.setTitle("Aceptar Pasaje");
 
			// set the custom dialog components - text, image and button
			TextView txtIdDialog = (TextView) dialog.findViewById(R.id.txt_dialog_id);
			txtIdDialog.setText(pasaje.getId()+"");
			TextView txtDireccionDialog = (TextView) dialog.findViewById(R.id.txt_dialog_direccion);
			txtDireccionDialog.setText(pasaje.getDireccion());
			TextView txtClienteDialog = (TextView) dialog.findViewById(R.id.txt_dialog_cliente);
			txtClienteDialog.setText(pasaje.getCliente());
 
			Button btnSi = (Button) dialog.findViewById(R.id.btn_dialog_si);
			Button btnNo = (Button) dialog.findViewById(R.id.btn_dialog_no);
	
			
			btnSi.setOnClickListener(new OnClickListener() {
				
				
				@Override
				public void onClick(View arg0) {
					mp.stop();
					TareaAsincronaNotificarEstadoPasaje tn = new TareaAsincronaNotificarEstadoPasaje();
					txtDireccion = (TextView) findViewById(R.id.txtDireccion);
					txtCliente = (TextView) findViewById(R.id.txtCliente);
					txtHora = (TextView) findViewById(R.id.txtHoraSolicitado);
					txtCronometro = (TextView) findViewById(R.id.cronometro);
					spinnerEstados = (Spinner) findViewById(R.id.spinnerEstados);
					
					PasajeDAO pasajeDao = new PasajeDAO(getApplicationContext());
					pasajeDao.nuevo(pasaje);
					
					txtDireccion.setText(pasaje.getDireccion());
					TabMovil.direccionPasaje = pasaje.getDireccion();
					
					txtCliente.setText(pasaje.getCliente());
					TabMovil.clientePasaje = pasaje.getCliente();
					
					String hora = pasaje.getFecha().split(" ")[1];		
					txtHora.setText(hora);
					TabMovil.horaPasaje = hora;
						
					spinnerEstados.setSelection(1);
					spinnerEstados.setEnabled(false);
				
					txtCronometro = (TextView) findViewById(R.id.cronometro);
					txtCronometro.setText("00:10:00");
					final CounterClass timer = new CounterClass(600000,1000); 
					timer.start();
					
					TimerEnviarUbicacion.estadoPasajePendienteRespuesta = "asignado";
					if(conInternet()){
						tn.execute("asignado","si");
					}else{
						Log.d("Guardo el id", "Se guarda el id de pasaje pendiente respuesta");
						TimerEnviarUbicacion.idPasajePendienteRespuesta = pasaje.getId();
					}
					dialog.dismiss();
				}
					
			});
			
			btnNo.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mp.stop();
					TareaAsincronaNotificarEstadoPasaje tn = new TareaAsincronaNotificarEstadoPasaje();
					TimerEnviarUbicacion.estadoPasajePendienteRespuesta = "rechazado";
					if(conInternet()){
						tn.execute("rechazado","no");
						
					}else{			
						Log.d("Guardo el id", "Se guarda el id de pasaje pendiente respuesta: "+TimerEnviarUbicacion.estadoPasajePendienteRespuesta);
						TimerEnviarUbicacion.idPasajePendienteRespuesta = pasaje.getId();
					}
					dialog.dismiss();
					
				}
			});
 
			dialog.show();
	}
	
	
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("DefaultLocale")
	public class CounterClass extends CountDownTimer {  
         public CounterClass(long millisInFuture, long countDownInterval) {  
              super(millisInFuture, countDownInterval);  
         }  
         @Override  
        public void onFinish() {  
          txtCronometro.setText("");  
          spinnerEstados.setEnabled(true);
          spinnerEstados.setSelection(0);
          txtCliente.setText("");
          txtDireccion.setText("");
          txtHora.setText("");
          TabMovil.clientePasaje = null;
          TabMovil.direccionPasaje = null;
          TabMovil.horaPasaje = null;
          TabMovil.cronometro = null;
          
        }  
       
         @Override  
         public void onTick(long millisUntilFinished) {  
               long millis = millisUntilFinished;  
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),  
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),  
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));   
                txtCronometro.setText(hms);  
                TabMovil.cronometro = hms;
         }  
    }  


	private class TareaAsincronaNotificarEstadoPasaje extends AsyncTask<String, Void, Object>{
		private WebService ws;
		boolean respuesta;
		boolean esAceptado = false;
		
		protected void onPreExecute(){
			
		}
		protected Integer doInBackground(String... args){
			ws = new WebService();
			if(args[1].equals("si")){
				esAceptado = true;
			}
			respuesta=ws.notificarEstadoPasajeEnCurso(pasaje.getId(), args[0], getUsuario().getUsuario());	
			if(respuesta == false){
				TimerEnviarUbicacion.idPasajePendienteRespuesta = pasaje.getId();
			}else{
				TimerEnviarUbicacion.idPasajePendienteRespuesta = 0;
			}
			
			return 1;
		}
		
		protected void onPostExecute(Object result){
			
			
			if(respuesta && esAceptado){
				Toast.makeText(getApplicationContext(), "El pasaje fue aceptado", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(getApplicationContext(), "El pasaje fue cancelado", Toast.LENGTH_LONG).show();
			}
			
		}

	}
	
	public boolean conInternet() {
		Context context = getApplicationContext();
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo[] netInfo = connectivityManager.getAllNetworkInfo();
			if (netInfo != null) {
				for (NetworkInfo net : netInfo) {
					if (net.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} 
		else {
			Toast.makeText(getApplicationContext(), "Verifique su conexión a Internet", Toast.LENGTH_LONG).show();
		}
		return false;
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
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public static void setEstadoUsuario(Estados e){
		usuario.setEstado(e);
	}
	
	
		
}
