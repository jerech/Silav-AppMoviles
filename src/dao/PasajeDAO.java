package dao;


import java.util.ArrayList;
import java.util.List;

import modelo.Pasaje;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PasajeDAO extends DBHelper{
	private SQLiteDatabase mDB;
	public static final String TABLA = "PASAJE";
	
	public static final String ID = "id";
	public static final String FECHA = "fecha";
	public static final String DIRECCION = "direccion";
	public static final String CLIENTE = "cliente";
	
	public static final int ID_INDEX = 0;
	public static final int FECHA_INDEX = 1;
	public static final int DIRECCION_INDEX = 2;
	public static final int CLIENTE_INDEX = 3;

    
    public static final String  CREATE = "CREATE TABLE " + TABLA + " (" 
	    + ID + " INT PRIMARY KEY NOT NULL, "
    	+ FECHA + " DATETIME, "
	    + DIRECCION + " VARCHAR(50),"
	    + CLIENTE + " VARCHAR(50))";
    

	public PasajeDAO(Context context) {
		super(context);
		this.mDB = getWritableDatabase();
		mDB.close();
	}
	
	public void onCreate(SQLiteDatabase db) {
		super.onCreate(db);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		super.onUpgrade(db, oldVersion, newVersion);
	}
	
	public long insert(ContentValues contentValues){
		abrirDB();
		long rowID = mDB.insert(TABLA, null, contentValues);
		mDB.close();
		return rowID;		
	}
	
	public int del(){
		abrirDB();
		int cnt = mDB.delete(TABLA, null, null);	
		mDB.close();
		return cnt;
	}
	
	public void nuevo(Pasaje pasaje){
		ContentValues parametros = new ContentValues();
		parametros.put(ID, pasaje.getId());
		parametros.put(DIRECCION, pasaje.getDireccion());
		parametros.put(FECHA, pasaje.getFecha());
		parametros.put(CLIENTE, pasaje.getCliente());
		
		abrirDB();
		mDB.insert(TABLA, null, parametros);
		mDB.close();
		
		Log.d("Nuevo Pasaje", "Se guardo un pasaje con id: "+pasaje.getId());
	}
	
	
	public void ejecutarSentencia(String query){
		abrirDB();
		mDB.execSQL(query);				
	}
	
	public Pasaje obtenerPasaje() {
		String selectQuery = "SELECT * FROM " + TABLA;
		abrirDB();
		Cursor cursor = mDB.rawQuery(selectQuery, null);
		Pasaje pasaje=new Pasaje();
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {			
			pasaje.setId(cursor.getInt(ID_INDEX));
			pasaje.setCliente(cursor.getString(CLIENTE_INDEX));
			pasaje.setDireccion(cursor.getString(DIRECCION_INDEX));		
			pasaje.setFecha(cursor.getString(FECHA_INDEX));
			
			
		}else{
			pasaje = null;
		}
		cursor.close();
		mDB.close();
		// return user
		return pasaje;
	}
	
	public void borrarTodosLosPasajes(){
		abrirDB();
		 if(mDB!=null){
		  mDB.execSQL("DELETE FROM "+TABLA+" WHERE 1");
		  mDB.close();   
		 }
	}
	
	public List<Pasaje> obtenerPasajes(){
		 abrirDB();
		  Cursor c = mDB.rawQuery("SELECT * FROM "+TABLA, null);
		  List<Pasaje> pasajes = new ArrayList<Pasaje>();
		  c.moveToFirst();
		  for(int i=0; i<c.getCount(); i++){
			  Pasaje pasaje = new Pasaje();
			  pasaje.setId(c.getInt(ID_INDEX));
				pasaje.setCliente(c.getString(CLIENTE_INDEX));
				pasaje.setDireccion(c.getString(DIRECCION_INDEX));
				pasaje.setFecha(c.getString(FECHA_INDEX));
					  
			  pasajes.add(pasaje);
			  c.moveToNext();
		  }
		  c.close();
		  mDB.close();
		 return pasajes;
		}
	
	private void abrirDB(){
		if(!mDB.isOpen()){
        	mDB = getWritableDatabase();
        }
	}
}
