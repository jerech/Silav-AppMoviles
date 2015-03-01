package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import modelo.Usuario;

public class UsuarioDAO extends DBHelper {
	
	private SQLiteDatabase mDB;
	public static final String TABLA = "USUARIO";
	
	public static final String NOMBRE = "nombre_usuario";
	public static final String CONTRASENIA = "contrasenia_usuario";
	
	public static final int NOMBRE_INDEX = 0;
	public static final int CONTRASENIA_INDEX = 1;	
    
    public static final String  CREATE = "CREATE TABLE " + TABLA + " (" 
	    + NOMBRE + " VARCHAR(50) PRIMARY KEY NOT NULL, "
	    + CONTRASENIA + " VARCHAR(50))";
    

	public UsuarioDAO(Context context) {
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
	
	public void nuevo(String nombre, String contrasenia){
		ContentValues parametros = new ContentValues();
		parametros.put(NOMBRE, nombre);
		parametros.put(CONTRASENIA, contrasenia);
		
		abrirDB();
		mDB.insert(TABLA, null, parametros);
		mDB.close();
	}
	
	public boolean validarUsuario(String contrasenia){
        String selectQuery = "SELECT  * FROM " + TABLA;  
        abrirDB();
        Cursor cursor = mDB.rawQuery(selectQuery, null);
		boolean resultado = false;
		
		// Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
			String cont = cursor.getString(CONTRASENIA_INDEX);
			if(cont.equals(contrasenia)){
				resultado = true;
			}else{
				resultado = false;
			}
        }
        cursor.close();
        mDB.close();
        
        return resultado;
    }
	
	public void ejecutarSentencia(String query){
		abrirDB();
		mDB.execSQL(query);				
	}
	
	public Usuario obtenerUsuario() {
		String selectQuery = "SELECT  * FROM " + TABLA;
		abrirDB();
		Cursor cursor = mDB.rawQuery(selectQuery, null);
		Usuario usuario=new Usuario();
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {			
			usuario.setUsuario(cursor.getString(NOMBRE_INDEX));
			usuario.setContrasenia(cursor.getString(CONTRASENIA_INDEX));
		}else{
			usuario = null;
		}
		cursor.close();
		mDB.close();
		// return user
		return usuario;
	}
	
	private void abrirDB(){
		if(!mDB.isOpen()){
        	mDB = getWritableDatabase();
        }
	}

}
