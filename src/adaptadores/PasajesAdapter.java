package adaptadores;

import java.util.ArrayList;
import java.util.List;

import com.appremises.R;

import modelo.Pasaje;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class PasajesAdapter extends BaseAdapter implements Filterable{
	private Context context;
	private List<Pasaje> pasajes;

	public PasajesAdapter(Context context, List<Pasaje> pasajes) {
		this.context=context;
		this.pasajes=pasajes;
		// TODO Auto-generated constructor stub
	}
	
	public void updatePasajes(List<Pasaje> pasajes) {
        this.pasajes = pasajes;
        notifyDataSetChanged();
    }
	
	static class ViewHolder{
		public TextView fecha;
		public TextView id;
		public TextView cliente;
		public TextView direccion;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			
		convertView = LayoutInflater.from(context).inflate(R.layout.item_lista_pasajes, parent, false);
			
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.id = (TextView) convertView.findViewById(R.id.txtId);
			viewHolder.cliente = (TextView) convertView.findViewById(R.id.txtCliente);
			viewHolder.direccion = (TextView) convertView.findViewById(R.id.txtDireccion);
			viewHolder.fecha=(TextView)convertView.findViewById(R.id.txtFecha);
			convertView.setTag(viewHolder);
		}
		
		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.id.setText(Integer.toString((pasajes.get(position).getId())));
		holder.fecha.setText(pasajes.get(position).getFecha());
		holder.direccion.setText(pasajes.get(position).getDireccion());
		holder.cliente.setText(pasajes.get(position).getCliente());
		return convertView;
	}
	@Override
	public Filter getFilter() {
		
	    Filter filter = new Filter() {

	        @Override
	        protected FilterResults performFiltering(CharSequence constraint) {
	            FilterResults results = new FilterResults();
	            ArrayList<Pasaje> FilteredArrList = new ArrayList<Pasaje>();


	            if (constraint == null || constraint.length() == 0) {
	            	
	                results.count = pasajes.size();
	                results.values =pasajes;
	                
	            } else {
	                constraint = constraint.toString();
	                for (int i = 0; i < pasajes.size(); i++) {
	                    Pasaje pasaje = pasajes.get(i);
	                    if (pasaje.getFecha().contains(constraint.toString())) {
	                        FilteredArrList.add(pasaje);
	                 
	                    }
	                    
	                    if (String.valueOf(pasaje.getId()).contains(constraint.toString())){
	                    	FilteredArrList.add(pasaje);
	                    }
	                    
	                    if(pasaje.getCliente().contains(constraint.toString())){
	                    	FilteredArrList.add(pasaje);
	                    }
	                }
	                // set the Filtered result to return
	                results.count = FilteredArrList.size();
	                results.values = FilteredArrList;
	                
	            }
	            return results;
	        }
	        
	        @SuppressWarnings("unchecked")
	        @Override
	        protected void publishResults(CharSequence constraint,FilterResults results) {

	        	if(results.count==0){
	        		notifyDataSetInvalidated();
	        	}
	        	else{
	        		pasajes = (ArrayList<Pasaje>) results.values; // has the filtered values
	            	notifyDataSetChanged();  // notifies the data with new filtered values
	        	}
	        }
	    };
	    return filter;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pasajes.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return pasajes.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return pasajes.get(position).getId();
	}

}
