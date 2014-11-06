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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appremises.R;

public class TabPasaje extends Fragment {
	
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, Bundle savedInstanceState){
		
		View v=inflater.inflate(R.layout.tab_pasajes, container, false);
		return v;
	}
}
