package interfaz;

import com.appremises.R;

import controladores.BBDD;

import android.database.Cursor;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;

public class Configuracion extends PreferenceActivity{
	
	BBDD db;
	Cursor c;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.app_preferencias);
		ListPreference listPref = (ListPreference) findPreference("remis");

		
		//Llenamos el listpreference con los numeros de remises
		db = new BBDD(this);
		c = db.leerRemises();
		startManagingCursor(c);
		
		String[] entries = new String[c.getCount()];
		if(c.moveToFirst()){
			do{
				entries[c.getPosition()] = c.getString(0);
			}while(c.moveToNext());
			
		}
		
		listPref.setEntries(entries);
		listPref.setEntryValues(entries);
	
		
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		c.close();
		db.close();
	}
	
	

}
