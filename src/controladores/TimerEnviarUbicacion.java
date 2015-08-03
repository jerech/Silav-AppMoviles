package controladores;

import java.util.TimerTask;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import modelo.Usuario;

public class TimerEnviarUbicacion extends TimerTask{
	
	private Usuario usuario;
	private Context context;
	public static int idPasajePendienteRespuesta;
	public static String estadoPasajePendienteRespuesta;
	
	public TimerEnviarUbicacion(Usuario usuario, Context context){
		this.usuario = usuario;
		this.context = context;
		
	}

	@Override
	public void run() {
		TareaAsincronaUbicacion nuevaTareaAsincrona = new TareaAsincronaUbicacion();
		
		if(conInternet()){
			nuevaTareaAsincrona.execute("");
		}	
		
		
		Log.d("Ubicacion Usuario:", "lon:"+usuario.getUbicacionLongitud()+"- lat:"+usuario.getUbicacionLatitud());
		Log.d("Estado actual:", usuario.getEstado().toString());
		Log.d("Pasaje Pendiente","Pendiente de respuesta "+idPasajePendienteRespuesta);
		
	}
	
	
	private class TareaAsincronaUbicacion extends AsyncTask<String, Void, Object>{
		
		WebService ws;
		@Override
		protected Integer doInBackground(String... args){
			ws = new WebService();
			boolean respuesta = false;
			
			//Si el id de pasaje es distinto a cero, se envia la respuesta al id de pasaje guardado
			if(idPasajePendienteRespuesta != 0){
				Log.d("Pasaje pendiente", "Se envia la respuesta pendiente: "+estadoPasajePendienteRespuesta);
				respuesta = ws.notificarEstadoPasajeEnCurso(idPasajePendienteRespuesta, estadoPasajePendienteRespuesta, usuario.getUsuario());
				if(respuesta == true){
					idPasajePendienteRespuesta = 0;
				}
			}
			
			ws.actualizarUbicacion(usuario);
			
			return 1;
		}
	}
	
	public boolean conInternet() {

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
			Toast.makeText(context, "Verifique su conexión a Internet", Toast.LENGTH_LONG).show();
		}
		return false;
	}
	
	

}
