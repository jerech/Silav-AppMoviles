package controladores;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class BBDD extends SQLiteOpenHelper{
	
	private static String name = "base_datos";
	private static int version = 1;
	private static CursorFactory cursorFactory = null;
	
	protected static String TableRemises = "remises";
	private String SQLCreateTablaRemises = "CREATE TABLE " + TableRemises +  " (numero INT primary key, marca VARCHAR(60), modelo VARCHAR(60)) ";
	public BBDD(Context context){
		super(context, name, cursorFactory, version);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQLCreateTablaRemises);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	public void insertarRemis(int numero, String marca, String modelo){
		 SQLiteDatabase db = getWritableDatabase();
		 if(db!=null){
		  db.execSQL("INSERT INTO " + TableRemises + 
		  " (numero, marca, modelo) " +
		  " VALUES('" + numero + "', '" + marca + "', '" + modelo +"')");
		  
		  db.close();   
		 }
		}
	public Cursor leerRemises(){
		 SQLiteDatabase db = getReadableDatabase();
		  Cursor c = db.rawQuery("SELECT numero AS _id, marca, modelo"+
				  " FROM "+TableRemises, null); 
		  
		 return c;
		}
	
	public void borrarTodosLosRemises(){
		SQLiteDatabase db = getWritableDatabase();
		 if(db!=null){
		  db.execSQL("DELETE FROM "+TableRemises+" WHERE 1");
		  db.close();   
		 }
	}
	
	
	

}
