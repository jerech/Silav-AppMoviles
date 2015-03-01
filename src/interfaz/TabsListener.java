/*
* @Aplicación: Silav App Remises
 * @(#)TabsListener.java 0.1 28/07/14
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

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.util.Log;

public class TabsListener <T extends Fragment> implements TabListener{
	
	private Fragment frament;
	private String tag;
	
	public TabsListener(Bundle args, Activity a, String tag, Class<T> cls){
		this.tag=tag;
		frament=Fragment.instantiate(a, cls.getName());
		frament.setArguments(args);
		Log.d("Se instancio Tab:", "El Fragment que se instancio es "+tag);
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		ft.replace(android.R.id.content, frament, tag);
		Log.d("onTabSelected sobre:", (String) tab.getText());
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		ft.remove(frament);
		
	}

}
