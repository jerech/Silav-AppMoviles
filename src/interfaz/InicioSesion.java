/*
* @Aplicación: Silav App Remises
 * @(#)InicioSesion.java 0.1 28/07/14
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

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import modelo.Chofer;
import modelo.Movil;

import com.appremises.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import constantes.ConstantesWebService;
import constantes.Estados;
import controladores.BBDD;
import controladores.WebService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InicioSesion extends Activity{
	
	
	private EditText editTxtUsuario;
	private EditText editTxtPass;
	private Button btnIniciar;
	private ProgressDialog dialogoProgreso;
	private Context context;
	private Chofer chofer;
	
	//Variables para implemetar GCM
	//private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	public static final String EXTRA_MESSAGE = "message";
	private static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private static final String PROPERTY_EXPIRATION_TIME = "onServerExpirationTimeMs";
	private static final String PROPERTY_USER = "usuario";
	public static final long EXPIRATION_TIME_MS = 1000 * 3600 * 24 * 7;
	String SENDER_ID = "63779176750";//numero de proyecto en la cuenta de google console
	static final String TAG = "GCM Silav";
	private String regid;
	private GoogleCloudMessaging gcm;
	
	public InicioSesion(){
	}
	
	@Override
	public void onCreate(Bundle savedIntanceState){
		super.onCreate(savedIntanceState);
		setContext(this);
		setContentView(R.layout.iu_inicio_sesion);
		
		setEditTxtUsuario((EditText) findViewById(R.id.txtUsuario));
		setEditTxtPass((EditText) findViewById(R.id.txtPass));
		setBtnIniciar((Button) findViewById(R.id.btnIniciar));
		
		//Se obtiene el numero de remis desde las preferencias de la app
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		int num = Integer.parseInt(pref.getString("remis", "0"));
		
		//Se obtiene nombre o ip se sitio con la preferencias de la app
		String sitio = pref.getString("direccion", "none");
		ConstantesWebService.URL = "http://"+sitio+"/WebService/servicio.php".trim();
		ConstantesWebService.NAME_SPACE = "http://"+sitio+"/WebService".trim();
		
		Movil movil = new Movil(num);
		chofer = new Chofer(Estados.LIBRE);
		getChofer().setMovil(movil);
		
		ListenerClick listenerClick = new ListenerClick();
		getBtnIniciar().setOnClickListener(listenerClick);	
		
	}//fin del método OnCreate
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_inicio, menu);
		return true;
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
		case R.id.conf:
			Intent miIntent = new Intent(InicioSesion.this, Configuracion.class);
	        miIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        startActivity(miIntent);
			break;

		default:
			break;
		}
		return true;
	}
	private class ListenerClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			String nombreUsuario = getEditTxtUsuario().getText().toString();
			String contraseniaUsuario = getEditTxtPass().getText().toString();
			getChofer().setUsuario(nombreUsuario);
			getChofer().setContrasenia(contraseniaUsuario);
			
			if(conInternet()){
				// Usamos una AsyncTask, para mostrar una ventana de espera, mientras se consulta al Web Service
				TareaAsincronaAutenticarChofer tareaAsincrona = new TareaAsincronaAutenticarChofer();
				tareaAsincrona.execute("");
				String tituloDialogo = "Por favor espere.";
				String cuerpoMsjDialogo = "Autenticando usuario.";
				setDialogoProgreso(ProgressDialog.show(getContext(), tituloDialogo, cuerpoMsjDialogo,true,false));	
			}
		}
	}
	
	
	private class TareaAsincronaAutenticarChofer extends AsyncTask<String, Void, Object>{
		
		boolean respuesta;
		
		protected Integer doInBackground(String... args){
			
			respuesta = getChofer().conectarUsuario()&&sincronizarBDMoviles();
			if(respuesta){
				obtenerRegistroGCM();
			}
			return 1;
		}
		
		protected void onPostExecute(Object result){
			
			//Se elimina la pantalla de por favor esperar
			getDialogoProgreso().dismiss();
			
			//Se muestra el resultado
			if(respuesta){
				
				Intent miIntent = new Intent(InicioSesion.this, PantallaPrincipal.class);
				miIntent.putExtra("usuario", getChofer().getUsuario());
				miIntent.putExtra("numeroMovil", getChofer().getMovil().getNumero());
				miIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				
				startActivity(miIntent);
			}else{
				String cuerpoMsjDialogo = "Usuario o contraseña incorrecto.";
				Toast.makeText(getContext(), cuerpoMsjDialogo, Toast.LENGTH_LONG).show();
			}
			super.onPostExecute(result);
		}
		
	}//Fin Clase Tarea Asincrona
	
		
	private boolean sincronizarBDMoviles(){
		boolean respuesta = false;
		WebService ws = new WebService();
		ArrayList<Movil> listaMoviles = ws.obtenerMoviles(getChofer().getUsuario().toString());
		
		if(!listaMoviles.isEmpty()){
			respuesta = true;
			
			BBDD bd = new BBDD(this);
			bd.borrarTodosLosMoviles();
			Iterator< Movil> i=listaMoviles.iterator();
			while(i.hasNext()){
				Movil r = i.next();
				bd.insertarMovil(r.getNumero(), r.getMarca(), r.getModelo());
			}
			
			bd.close();
		}		
		return respuesta;
	}

	private boolean obtenerRegistroGCM(){
		context = getApplicationContext();
		 boolean resultado = false;
        //Chequemos si está instalado Google Play Services
        //if(checkPlayServices())
        //{
                gcm = GoogleCloudMessaging.getInstance(InicioSesion.this);
 
                //Obtenemos el Registration ID guardado
                regid = getRegistrationId(context);
 
                //Si no disponemos de Registration ID comenzamos el registro
                if (regid.equals("")) {
                	
                	 
                    try
                    {
                        if (gcm == null)
                        {
                            gcm = GoogleCloudMessaging.getInstance(context);
                        }
         
                        //Nos registramos en los servidores de GCM
                        regid = gcm.register(SENDER_ID);
         
                        Log.d(TAG, "Registrado en GCM: registration_id=" + regid);
         
                        //Nos registramos en nuestro servidor
                        WebService ws = new WebService();
                        boolean registrado = ws.enviarClaveGCM(getEditTxtUsuario().getText().toString(), regid);
         
                        //Guardamos los datos del registro
                        if(registrado)
                        {
                            setRegistrationId(context, getEditTxtUsuario().getText().toString(), regid);
                        }
                    }
                    catch (IOException ex)
                    {
                        Log.d(TAG, "Error registro en GCM:" + ex.getMessage());
                    }
                }
        //}
        //else
        //{
            //    Log.i(TAG, "No se ha encontrado Google Play Services.");
            //}
                
          return resultado;
	}
	
	private String getRegistrationId(Context context){
		SharedPreferences prefs = getSharedPreferences(
		InicioSesion.class.getSimpleName(),
		Context.MODE_PRIVATE);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		
		if (registrationId.length() == 0){
			Log.d(TAG, "Registro GCM no encontrado.");
			return "";
		}
		String registeredUser = prefs.getString(PROPERTY_USER, "user");
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		long expirationTime = prefs.getLong(PROPERTY_EXPIRATION_TIME, -1);
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
		String expirationDate = sdf.format(new Date(expirationTime));
		Log.d(TAG, "Registro GCM encontrado (usuario=" + registeredUser +
					", version=" + registeredVersion +
					", expira=" + expirationDate + ")");
		
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion){
		Log.d(TAG, "Nueva versión de la aplicación.");
		return "";
		}
		else if (System.currentTimeMillis() > expirationTime){
		Log.d(TAG, "Registro GCM expirado.");
		return "";
		}
		else if (!getEditTxtUsuario().getText().toString().equals(registeredUser)){
		Log.d(TAG, "Nuevo nombre de usuario.");
		return "";
		}
		return registrationId;
	}
	
	private static int getAppVersion(Context context){
		try{
			PackageInfo packageInfo = context.getPackageManager()
			.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		}
			catch (NameNotFoundException e)
		{
				throw new RuntimeException("Error al obtener versión: " + e);
		}
	}
	
	private void setRegistrationId(Context context, String user, String regId){
		SharedPreferences prefs = getSharedPreferences(
	    InicioSesion.class.getSimpleName(),
	        Context.MODE_PRIVATE);
	 
	    int appVersion = getAppVersion(context);
	 
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(PROPERTY_USER, user);
	    editor.putString(PROPERTY_REG_ID, regId);
	    editor.putInt(PROPERTY_APP_VERSION, appVersion);
	    editor.putLong(PROPERTY_EXPIRATION_TIME,
	    System.currentTimeMillis() + EXPIRATION_TIME_MS);
	 
	    editor.commit();
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
			Logger.getLogger(InicioSesion.class.getName()).log(Level.INFO, "Sin conexión a Internet");
		}
		return false;
	}
	
	public EditText getEditTxtUsuario() {
		return editTxtUsuario;
	}

	public void setEditTxtUsuario(EditText editTxtUsuario) {
		this.editTxtUsuario = editTxtUsuario;
	}

	public EditText getEditTxtPass() {
		return editTxtPass;
	}

	public void setEditTxtPass(EditText editTxtPass) {
		this.editTxtPass = editTxtPass;
	}

	public Button getBtnIniciar() {
		return btnIniciar;
	}

	public void setBtnIniciar(Button btnIniciar) {
		this.btnIniciar = btnIniciar;
	}

	public ProgressDialog getDialogoProgreso() {
		return dialogoProgreso;
	}

	public void setDialogoProgreso(ProgressDialog dialogoProgreso) {
		this.dialogoProgreso = dialogoProgreso;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Chofer getChofer() {
		return chofer;
	}

	public void setChofer(Chofer chofer) {
		this.chofer = chofer;
	}	
	

}
