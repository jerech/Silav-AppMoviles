package dao;

import java.util.ArrayList;
import java.util.List;

import modelo.Movil;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MovilDAO extends DBHelper{
	private SQLiteDatabase mDB;
	public static final String TABLA = "MOVIL";
	
	public static final String NUMERO = "numero";
	public static final String MARCA = "marca";
	public static final String MODELO = "modelo";
	
	public static final int NUMERO_INDEX = 0;
	public static final int MARCA_INDEX = 1;
	public static final int MODELO_INDEX = 2;
    
    public static final String  CREATE = "CREATE TABLE " + TABLA + " (" 
	    + NUMERO + " INT PRIMARY KEY NOT NULL, "
    	+ MARCA + " VARCHAR(50), "
	    + MODELO + " VARCHAR(50))";
    

	public MovilDAO(Context context) {
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
	
	public void nuevo(int numero, String marca, String modelo){
		ContentValues parametros = new ContentValues();
		parametros.put(NUMERO, numero);
		parametros.put(MARCA, marca);
		parametros.put(MODELO, modelo);
		
		abrirDB();
		mDB.insert(TABLA, null, parametros);
		mDB.close();
	}
	
	
	public void ejecutarSentencia(String query){
		abrirDB();
		mDB.execSQL(query);				
	}
	
	public Movil obtenerMovil() {
		String selectQuery = "SELECT  * FROM " + TABLA;
		abrirDB();
		Cursor cursor = mDB.rawQuery(selectQuery, null);
		Movil movil=new Movil();
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {			
			movil.setNumero(cursor.getInt(NUMERO_INDEX));
			movil.setMarca(cursor.getString(MARCA_INDEX));
			movil.setModelo(cursor.getString(MODELO_INDEX));
		}else{
			movil = null;
		}
		cursor.close();
		mDB.close();
		// return user
		return movil;
	}
	
	public void borrarTodosLosMoviles(){
		abrirDB();
		 if(mDB!=null){
		  mDB.execSQL("DELETE FROM "+TABLA+" WHERE 1");
		  mDB.close();   
		 }
	}
	
	public List<Movil> obtenerMoviles(){
		 abrirDB();
		  Cursor c = mDB.rawQuery("SELECT "+NUMERO+" AS _id, "+MARCA+", "+MODELO+
				  " FROM "+TABLA, null);
		  List< Movil> moviles = new ArrayList<Movil>();
		  c.moveToFirst();
		  for(int i=0; i<c.getCount(); i++){
			  Movil movil = new Movil();
			  movil.setNumero(c.getInt(NUMERO_INDEX));
			  movil.setMarca(c.getString(MARCA_INDEX));
			  movil.setModelo(c.getString(MODELO_INDEX));
			  
			  moviles.add(movil);
			  c.moveToNext();
		  }
		  c.close();
		  mDB.close();
		 return moviles;
		}
	
	private void abrirDB(){
		if(!mDB.isOpen()){
        	mDB = getWritableDatabase();
        }
	}
}
