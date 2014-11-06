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

import java.util.ArrayList;
import java.util.Iterator;

import modelo.Chofer;
import modelo.Remis;

import com.appremises.R;

import constantes.Estados;
import controladores.BBDD;
import controladores.WebService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
		
		Remis remis = new Remis(num);
		chofer = new Chofer(Estados.LIBRE);
		getChofer().setRemis(remis);
		
		ListenerClick listenerClick = new ListenerClick();
		getBtnIniciar().setOnClickListener(listenerClick);	
		
	}//fin del método OnCreate
	
	
	private class ListenerClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			String nombreUsuario = getEditTxtUsuario().getText().toString();
			String contraseniaUsuario = getEditTxtPass().getText().toString();
			getChofer().setUsuario(nombreUsuario);
			getChofer().setContrasenia(contraseniaUsuario);
			
			
			// Usamos una AsyncTask, para mostrar una ventana de espera, mientras se consulta al Web Service
			TareaAsincronaAutenticarChofer tareaAsincrona = new TareaAsincronaAutenticarChofer();
			tareaAsincrona.execute("");
			String tituloDialogo = "Por favor espere.";
			String cuerpoMsjDialogo = "Autenticando usuario.";
			setDialogoProgreso(ProgressDialog.show(getContext(), tituloDialogo, cuerpoMsjDialogo,true,false));		
		}
	}
	
	
	private class TareaAsincronaAutenticarChofer extends AsyncTask<String, Void, Object>{
		
		boolean respuesta;
		
		protected Integer doInBackground(String... args){
			
			respuesta = getChofer().conectarUsuario()&&sincronizarBDRemises();
			return 1;
		}
		
		protected void onPostExecute(Object result){
			
			//Se elimina la pantalla de por favor esperar
			getDialogoProgreso().dismiss();
			
			//Se muestra el resultado
			if(respuesta){
				
				Intent miIntent = new Intent(InicioSesion.this, PantallaPrincipal.class);
				miIntent.putExtra("usuario", getChofer().getUsuario());
				miIntent.putExtra("numeroRemis", getChofer().getRemis().getNumero());
				miIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				
				startActivity(miIntent);
			}else{
				String cuerpoMsjDialogo = "Usuario o contraseña incorrecto.";
				Toast.makeText(getContext(), cuerpoMsjDialogo, Toast.LENGTH_LONG).show();
			}
			super.onPostExecute(result);
		}
		
	}//Fin Clase Tarea Asincrona
	
		
	private boolean sincronizarBDRemises(){
		boolean respuesta = false;
		WebService ws = new WebService();
		ArrayList<Remis> listaRemises = ws.obtenerRemises(getChofer().getUsuario().toString());
		
		if(!listaRemises.isEmpty()){
			respuesta = true;
			
			BBDD bd = new BBDD(this);
			bd.borrarTodosLosRemises();
			Iterator< Remis> i=listaRemises.iterator();
			while(i.hasNext()){
				Remis r = i.next();
				bd.insertarRemis(r.getNumero(), r.getMarca(), r.getModelo());
			}
			
			bd.close();
		}
		
		return respuesta;
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
