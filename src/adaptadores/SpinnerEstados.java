package adaptadores;

import java.util.List;

import com.appremises.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ImageView;

public class SpinnerEstados extends ArrayAdapter<Estado>{
	private Context context;
	private List<Estado> datos=null;
	
	public SpinnerEstados(Context context, List<Estado> datos){
		super(context, R.layout.item_seleccionado_spinner,datos);
		this.context=context;
		this.datos=datos;
		
	}
	
	//este método establece el elemento seleccionado sobre el botón del spinner
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		if (convertView == null)
        {
             convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_seleccionado_spinner,null);
        }          
        ((TextView) convertView.findViewById(R.id.txt_spinner_select)).setText(datos.get(position).getNombre());
        ((ImageView) convertView.findViewById(R.id.img_estado)).setBackgroundResource(datos.get(position).getIcono());     
         
        return convertView;
		
	}
	
	public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        if (row == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.lista_item_spinner, parent, false);
        }
 
        if (row.getTag() == null)
        {
            EstadoHolder estadoHolder = new EstadoHolder();
            estadoHolder.setIcono((ImageView) row.findViewById(R.id.img_estado_lista));
            estadoHolder.setNombre((TextView) row.findViewById(R.id.txt_spinner_lista));
            row.setTag(estadoHolder);
        }
 
        //rellenamos el layout con los datos de la fila que se está procesando
        Estado estado = datos.get(position);     
        ((EstadoHolder) row.getTag()).getIcono().setImageResource(estado.getIcono());    
        ((EstadoHolder) row.getTag()).getNombre().setText(estado.getNombre());
 
        return row;
    }
	
	private class EstadoHolder{
		private TextView nombre;
		private ImageView icono;
		public TextView getNombre() {
			return nombre;
		}
		public void setNombre(TextView nombre) {
			this.nombre = nombre;
		}
		public ImageView getIcono() {
			return icono;
		}
		public void setIcono(ImageView icono) {
			this.icono = icono;
		}
		
	
		
	}

}
