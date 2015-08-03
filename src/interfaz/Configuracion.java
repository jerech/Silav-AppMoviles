package interfaz;

import java.util.Iterator;
import java.util.List;

import modelo.Movil;

import com.appremises.R;

import constantes.ConstantesWebService;

import dao.MovilDAO;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.util.Log;

public class Configuracion extends PreferenceActivity{
	
	MovilDAO movilDao;
	List<Movil> moviles;
	ListPreference listPref;
	EditTextPreference textEdit;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.app_preferencias);
		listPref = (ListPreference) findPreference("remis");
		textEdit = (EditTextPreference) findPreference("direccion");
	
		//Llenamos el listpreference con los numeros de remises
		movilDao = new MovilDAO(this);
		moviles = movilDao.obtenerMoviles();
		Iterator<Movil> iMovil = moviles.iterator();
		String[] entries = new String[moviles.size()];
		int i=0;
		Movil m = null;
		while (iMovil.hasNext()) {
			m = iMovil.next();
			entries[i] = Integer.toString(m.getNumero()); 
		}
		
		listPref.setEntries(entries);
		listPref.setEntryValues(entries);
		
		textEdit.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				ConstantesWebService.URL = "http://"+newValue.toString()+"/WebService/servicio.php".trim();
				ConstantesWebService.NAME_SPACE = "http://"+newValue.toString()+"/WebService".trim();
				Log.d("A cambiado una preferencia", newValue.toString());
				return true;
			}
		});
	
		
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	

}
