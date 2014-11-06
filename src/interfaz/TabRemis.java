/*
* @Aplicación: Silav App Remises
 * @(#)TabRemis.java 0.1 28/07/14
 *
/**
 *
 * @autor Jeremías Chaparro
 * @version 0.1, 28/07/14
 * 
 **
 * @Modificaciones relevantes:
 * 09/09/14-Se refactoriza código, nombres de variables y métodos-Jeremías Chaparro
 * 
 *
 */

package interfaz;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import modelo.Chofer;
import modelo.Remis;

import com.appremises.R;

import constantes.Estados;
import controladores.WebService;

import adaptadores.Estado;
import adaptadores.SpinnerEstados;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;

public class TabRemis extends Fragment{
	
	private Spinner spinnerEstados;
	private Chofer chofer;
	private TextView txtCliente;
	private ImageButton btnAltavoz;
	private ImageButton btnPantalla;
	private ImageButton btnSos;
	
	public TabRemis(){
		setChofer(new Chofer());
		getChofer().setRemis(new Remis());
	}
	
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, Bundle savedInstanceState){
		
		View viewRoot = inflater.inflate(R.layout.tab_remis, container, false);	
		
		txtCliente = (TextView) viewRoot.findViewById(R.id.txtCliente);
		btnAltavoz = (ImageButton) viewRoot.findViewById(R.id.btnAltavoz);
		btnPantalla = (ImageButton) viewRoot.findViewById(R.id.btnPantalla);
		btnSos = (ImageButton) viewRoot.findViewById(R.id.btnSos);
			
		Bundle extras = this.getArguments();
		if(extras!=null){
		    getChofer().setUsuario(extras.getString("usuario"));
		    getChofer().getRemis().setNumero(extras.getInt("numeroRemis"));	    
		}	
		
		//Datos de spinner
		List<Estado> itemsDeSpinner = new ArrayList<Estado>(3);
		Estado estadoLibre = new Estado(getString(R.string.libre),R.drawable.estado_icono_l);
		Estado estadoOcupado = new Estado(getString(R.string.ocupado),R.drawable.estado_icono_o);
		Estado estadoDesconectado = new Estado(getString(R.string.inactivo),R.drawable.estado_icono_i);
		SpinnerEstados adaptadorSpinner = new SpinnerEstados(viewRoot.getContext(), itemsDeSpinner); 
		
		itemsDeSpinner.add(estadoLibre);
		itemsDeSpinner.add(estadoOcupado);
		itemsDeSpinner.add(estadoDesconectado);
		
		setSpinnerEstados( (Spinner) viewRoot.findViewById(R.id.spinnerEstados));
		getSpinnerEstados().setAdapter(adaptadorSpinner);
		
		ListenerItemSeleccionado miListenerItemSeleccionado = new ListenerItemSeleccionado();
		getSpinnerEstados().setOnItemSelectedListener(miListenerItemSeleccionado);
		
		ListenerClickAltavoz lAltavoz = new ListenerClickAltavoz();
		btnAltavoz.setOnClickListener(lAltavoz);
		
		ListenerClickPantalla lPantalla = new ListenerClickPantalla();
		btnPantalla.setOnClickListener(lPantalla);
		
		ListenerClickSos lSos = new ListenerClickSos();
		btnSos.setOnClickListener(lSos);
		
		return viewRoot;
	}
	
	
	private class ListenerItemSeleccionado implements OnItemSelectedListener{

		TareaAsincronaActualizarEstado tareaAsincrona;
		
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
        {
        
        	switch(position){
	        	case 0:
	        			if(getChofer().getEstado() != Estados.LIBRE){
	        				getChofer().setEstado(Estados.LIBRE);
	            			tareaAsincrona = new TareaAsincronaActualizarEstado();
	            			tareaAsincrona.execute("");
	            			Toast.makeText(adapterView.getContext(), "Se a cambiado el Estado a 'LIBRE'.", Toast.LENGTH_SHORT).show();
	        			}
	        		break;
	        	case 1:
	        			if(getChofer().getEstado() != Estados.OCUPADO){
	        				getChofer().setEstado(Estados.OCUPADO);
	            			tareaAsincrona = new TareaAsincronaActualizarEstado();
	            			tareaAsincrona.execute("");
		            		Toast.makeText(adapterView.getContext(), "Se a cambiado el Estado a 'OCUPADO'.", Toast.LENGTH_SHORT).show();
	        			}
	            	break;
	        	case 2:
	        			if(getChofer().getEstado() != Estados.INACTIVO){
	        				getChofer().setEstado(Estados.INACTIVO);
	            			tareaAsincrona = new TareaAsincronaActualizarEstado();
	            			tareaAsincrona.execute("");
		            		Toast.makeText(adapterView.getContext(), "Se a cambiado el Estado a 'INACTIVO'.", Toast.LENGTH_SHORT).show();
	        			}
	            	break;
        	}
        }

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class TareaAsincronaActualizarEstado extends AsyncTask<String, Void, Object>{
			
			protected Integer doInBackground(String... args){
				getChofer().actualizarEstado();
				
				return 1;
			}
	}

	
	private class ListenerClickAltavoz implements OnClickListener{

		@Override
		public void onClick(View v) {
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
			if(!pref.getBoolean("audio", false)){
				new Voz();				
			}
			
		}
		
	}
	private class ListenerClickPantalla implements OnClickListener{

		@Override
		public void onClick(View v) {
			WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
			params.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
			params.screenBrightness = 0;
			getActivity().getWindow().setAttributes(params);
		}
		
	}
	private class ListenerClickSos implements OnClickListener{

		@Override
		public void onClick(View v) {
			TareaAsincronaMensajeSos ta = new TareaAsincronaMensajeSos();
			ta.execute("");
			
			
		}
		
	}
	
	private class TareaAsincronaMensajeSos extends AsyncTask<String, Void, Object>{

		@Override
		protected Object doInBackground(String... params) {
			WebService ws = new WebService();
			ws.enviarMensajeSos(getChofer().getUsuario());
				
			
			
			return 1;
		}
		
	}
	
	public class Voz implements TextToSpeech.OnInitListener{
		private TextToSpeech tts = new TextToSpeech(getActivity(), this);
		@Override
		public void onInit(int status) {
			
			if (status == TextToSpeech.SUCCESS) {
				 
	            int result = tts.setLanguage(new Locale("spa"));
	 
	            if (result == TextToSpeech.LANG_MISSING_DATA
	                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
	                Log.e("TTS", "El lenguaje no es soportado");
	            } else {
	            	
	                speakOut();
	            }
	 
	        } else {
	            Log.e("TTS", "Inicializacion fallida!");
	        }
			
		}
		private void speakOut(){
			String texto = txtCliente.getText().toString();
			String textoVacio = "No hay pasaje.";
			
				if(!texto.equalsIgnoreCase("")){
					tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null);
				}else{
					tts.speak(textoVacio, TextToSpeech.QUEUE_FLUSH, null);
				}			
		}
		
	
	}

	public Spinner getSpinnerEstados() {
		return spinnerEstados;
	}

	public void setSpinnerEstados(Spinner spinner) {
		this.spinnerEstados = spinner;
	}

	public Chofer getChofer() {
		return chofer;
	}

	public void setChofer(Chofer chofer) {
		this.chofer = chofer;
	}
	
	


}
