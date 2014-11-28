package controladores;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class BBDD extends SQLiteOpenHelper{
	
	private static String name = "base_datos_1";
	private static int version = 1;
	private static CursorFactory cursorFactory = null;
	
	protected static String TableMoviles = "moviles";
	private String SQLCreateTablaMoviles = "CREATE TABLE " + TableMoviles +  " (numero INT primary key, marca VARCHAR(60), modelo VARCHAR(60)) ";
	public BBDD(Context context){
		super(context, name, cursorFactory, version);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQLCreateTablaMoviles);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	public void insertarMovil(int numero, String marca, String modelo){
		 SQLiteDatabase db = getWritableDatabase();
		 if(db!=null){
		  db.execSQL("INSERT INTO " + TableMoviles + 
		  " (numero, marca, modelo) " +
		  " VALUES('" + numero + "', '" + marca + "', '" + modelo +"')");
		  
		  db.close();   
		 }
		}
	public Cursor leerMoviles(){
		 SQLiteDatabase db = getReadableDatabase();
		  Cursor c = db.rawQuery("SELECT numero AS _id, marca, modelo"+
				  " FROM "+TableMoviles, null); 
		  
		 return c;
		}
	
	public void borrarTodosLosMoviles(){
		SQLiteDatabase db = getWritableDatabase();
		 if(db!=null){
		  db.execSQL("DELETE FROM "+TableMoviles+" WHERE 1");
		  db.close();   
		 }
	}
	
	
	

}
