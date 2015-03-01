package modelo;

import constantes.Estados;

public class Usuario {

	private String usuario;
	private String contrasenia;
	private Movil movil;
	private Estados estado;
	private double ubicacionLatitud;
	private double ubicacionLongitud;
	
	public Usuario(Estados estado){
		this.estado = estado;
	}
	public Usuario(){
		
	}
	
	public Usuario(String usuario, String contrasenia){
		this.setUsuario(usuario);
		this.setContrasenia(contrasenia);
	}
	
	public Usuario(String usuario){
		this.setUsuario(usuario);
	}
	
	

	public String getUsuario() {
		return usuario;
	}
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getContrasenia() {
		return contrasenia;
	}
	
	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	public Movil getMovil() {
		return movil;
	}

	public void setMovil(Movil movil) {
		this.movil = movil;
	}

	public Estados getEstado() {
		return estado;
	}

	public void setEstado(Estados estado) {
		this.estado = estado;
	}

	public double getUbicacionLatitud() {
		return ubicacionLatitud;
	}

	public void setUbicacionLatitud(double ubicacionLatitud) {
		this.ubicacionLatitud = ubicacionLatitud;
	}

	public double getUbicacionLongitud() {
		return ubicacionLongitud;
	}

	public void setUbicacionLongitud(double ubicacionLongitud) {
		this.ubicacionLongitud = ubicacionLongitud;
	}
	

}
