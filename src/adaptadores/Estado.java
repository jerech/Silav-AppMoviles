package adaptadores;

public class Estado {
	
	String nombre;
	int icono;
	
	public Estado(String nombre, int icono){
		super();
		this.nombre=nombre;
		this.icono=icono;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getIcono() {
		return icono;
	}

	public void setIcono(int icono) {
		this.icono = icono;
	}
	
	

}
