package modelo;

public class Remis {
	
	private int numero;
	private String marca;
	private String modelo;
	
	public Remis(){
		
	}
	public Remis(int numero){
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
