package modelo;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import constantes.ConstantesWebService;
import constantes.Estados;

public class Chofer {
	
	private String usuario;
	private String contrasenia;
	private Movil movil;
	private Estados estado;
	private double ubicacionLatitud;
	private double ubicacionLongitud;
	
	public Chofer(Estados estado){
		this.estado = estado;
	}
	public Chofer(){
		
	}
	
	public Chofer(String usuario, String contrasenia){
		this.setUsuario(usuario);
		this.setContrasenia(contrasenia);
	}
	
	public Chofer(String usuario){
		this.setUsuario(usuario);
	}
	
	public boolean conectarUsuario(){
		
		boolean respuesta=false;
			
		//Se crea un objeto de tipo soap.
		SoapObject rpc;
		rpc = new SoapObject(ConstantesWebService.NAME_SPACE, "conectarChofer");
		
		rpc.addProperty("usuario", getUsuario());
		rpc.addProperty("contrasenia", getContrasenia());
		rpc.addProperty("num_movil", getMovil().getNumero());
		rpc.addProperty("estado",getEstado().toString());
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut=rpc;
		
		//Se establece si el WS esta hecho en .net
		envelope.dotNet=false;
				
		envelope.encodingStyle=SoapSerializationEnvelope.XSD;
		
		//Para acceder al WS se crea un objeto de tipo HttpTransport
		HttpTransportSE androidHttpTransport=null;
		try{
			androidHttpTransport = new HttpTransportSE(ConstantesWebService.URL);
			androidHttpTransport.debug=true;
					
			//Se llama al servicio web
			androidHttpTransport.call(ConstantesWebService.NAME_SPACE + "/conectarChofer", envelope);
	
			//guardar la respuesta en una variable
			respuesta=(Boolean) envelope.getResponse();
			
			}catch(Exception e){
					System.out.println(e.getMessage());
					respuesta=false;
				}
		
		return respuesta;
	}
	
	public boolean desconectarUsuario(){
		boolean respuesta=false;
		final String nombreFuncionWebService = "desconectarChofer";
		//Se crea un objeto de tipo soap.
		SoapObject rpc;
		rpc = new SoapObject(ConstantesWebService.NAME_SPACE, nombreFuncionWebService);
		
		rpc.addProperty("usuario", getUsuario().toString());
		rpc.addProperty("num_movil", getMovil().getNumero());

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut=rpc;
		
		//Se establece si el WS esta hecho en .net
		envelope.dotNet=false;
				
		envelope.encodingStyle=SoapSerializationEnvelope.XSD;
		
		//Para acceder al WS se crea un objeto de tipo HttpTransport
		HttpTransportSE androidHttpTransport=null;
		try{
			androidHttpTransport = new HttpTransportSE(ConstantesWebService.URL);
			androidHttpTransport.debug=true;
					
			//Se llama al servicio web
			androidHttpTransport.call(ConstantesWebService.NAME_SPACE + "/" + nombreFuncionWebService, envelope);
	
			//guardar la respuesta en una variable
			respuesta=(Boolean) envelope.getResponse();
			
			}catch(Exception e){
					System.out.println(e.getMessage());
					respuesta=false;
				}
		
		return respuesta;
		
	}
public boolean actualizarEstado(){
		
		boolean respuesta=false;
		final String nombreFuncionWebService = "actualizarEstado";

		SoapObject rpc;
		rpc = new SoapObject(ConstantesWebService.NAME_SPACE, nombreFuncionWebService);
		
		rpc.addProperty("estado", getEstado().toString());
		rpc.addProperty("usuario", getUsuario());
		SoapSerializationEnvelope env = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		env.bodyOut=rpc;
		
		//Se establece si el WS esta hecho en .net
		env.dotNet=false;
				
		env.encodingStyle=SoapSerializationEnvelope.XSD;
		//Para acceder al WS se crea un objeto de tipo HttpTransport
		HttpTransportSE androidHttpTransport=null;
		androidHttpTransport = new HttpTransportSE(ConstantesWebService.URL);
		androidHttpTransport.debug=true;
		
		try{
					
			//Se llama al servicio web
			androidHttpTransport.call(ConstantesWebService.NAME_SPACE + "/" + nombreFuncionWebService, env);
	
			//guardar la respuesta en una variable
			respuesta=(Boolean) env.getResponse();
			
			}catch(Exception e){
					e.printStackTrace();
					respuesta=false;
				}
		
		return respuesta;
		
		
	}
	
	public boolean actualizarUbicacion(){
		boolean respuesta=false;

		SoapObject rpc;
		rpc = new SoapObject(ConstantesWebService.NAME_SPACE, "actualizarUbicacion");
		
		rpc.addProperty("usuario", getUsuario());
		rpc.addProperty("ulatitud", Double.toString(getUbicacionLatitud()));
		rpc.addProperty("ulongitud", Double.toString(getUbicacionLongitud()));
		
		SoapSerializationEnvelope env = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		env.bodyOut=rpc;
		
		//Se establece si el WS esta hecho en .net
		env.dotNet=false;
				
		env.encodingStyle=SoapSerializationEnvelope.XSD;
		//Para acceder al WS se crea un objeto de tipo HttpTransport
		HttpTransportSE androidHttpTransport=null;
		try{
			androidHttpTransport = new HttpTransportSE(ConstantesWebService.URL);
			androidHttpTransport.debug=true;
			
			//Se llama al servicio web
			androidHttpTransport.call(ConstantesWebService.NAME_SPACE + "/actualizarUbicacion", env);
	
			//guardar la respuesta en una variable
			respuesta=(Boolean) env.getResponse();
			
			}catch(Exception e){
				System.out.println(e.getMessage());
				respuesta=false;
			}
		
		return respuesta;
		
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
