package modelo;

public class Movil {
	
	private int numero;
	private String marca;
	private String modelo;
	
	public Movil(){
		
	}
	public Movil(int numero){
		this.setNumero(numero);
	}
	

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	
	

}
