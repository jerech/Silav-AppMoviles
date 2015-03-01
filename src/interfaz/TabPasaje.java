/*
* @Aplicación: Silav App Remises
 * @(#)TabPasaje.java 0.1 28/07/14
 *
/**
 *
 * @autor Jeremías Chaparro
 * @version 0.1, 28/07/14
 * 
 **
 * @Modificaciones relevantes:
 * 
 *
 */

package interfaz;

import java.util.List;

import modelo.Pasaje;

import adaptadores.PasajesAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.appremises.R;

import dao.PasajeDAO;

public class TabPasaje extends Fragment {
	private ListView listaPasajes;
	private EditText editBusquedaPasajes;
	private PasajeDAO p;
	private List<Pasaje> pasajes;
	private PasajesAdapter adaptadorPasajes;
	
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, Bundle savedInstanceState){
		
		View viewRoot=inflater.inflate(R.layout.tab_pasajes, container, false);
		listaPasajes = (ListView) viewRoot.findViewById(R.id.listaPasajes);
		editBusquedaPasajes = (EditText) viewRoot.findViewById(R.id.txt_busqueda_pasajes);
		p = new PasajeDAO(getActivity().getApplicationContext());
		pasajes = p.obtenerPasajes();
		adaptadorPasajes = new PasajesAdapter(getActivity().getBaseContext(), pasajes);
		adaptadorPasajes.updatePasajes(pasajes);
		listaPasajes.setAdapter(adaptadorPasajes);
		registerForContextMenu(listaPasajes);
		
		return viewRoot;
	}
}
