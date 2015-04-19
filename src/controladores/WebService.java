package controladores;

import java.util.ArrayList;
import java.util.Vector;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import constantes.ConstantesWebService;

import modelo.Movil;
import modelo.Usuario;

public class WebService {
	
public boolean loginUsuario(Usuario usuario){
	boolean respuesta=false;
	
	//Se crea un objeto de tipo soap.
	SoapObject rpc;
	rpc = new SoapObject(ConstantesWebService.NAME_SPACE, "login");
	
	rpc.addProperty("usuario", usuario.getUsuario());
	rpc.addProperty("contrasenia", usuario.getContrasenia());
	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	envelope.bodyOut=rpc;
	
	//Se establece si el WS esta hecho en .net
	envelope.dotNet=false;
			
	envelope.encodingStyle=SoapSerializationEnvelope.XSD;
	
	//Para acceder al WS se crea un objeto de tipo HttpTransport
	HttpTransportSE androidHttpTransport=null;
	try{
		androidHttpTransport = new HttpTransportSE(ConstantesWebService.URL,4000);
		androidHttpTransport.debug=true;
				
		//Se llama al servicio web
		androidHttpTransport.call(ConstantesWebService.NAME_SPACE + "/login", envelope);

		//guardar la respuesta en una variable
		respuesta=(Boolean) envelope.getResponse();
		
		}catch(Exception e){
				System.out.println(e.getMessage());
				respuesta=false;
			}
	
	return respuesta;
	
}
	
public boolean conectarUsuario(Usuario usuario){
		
		boolean respuesta=false;
			
		//Se crea un objeto de tipo soap.
		SoapObject rpc;
		rpc = new SoapObject(ConstantesWebService.NAME_SPACE, "conectarChofer");
		
		rpc.addProperty("usuario", usuario.getUsuario());
		rpc.addProperty("contrasenia", usuario.getContrasenia());
		rpc.addProperty("num_movil", usuario.getMovil().getNumero());
		rpc.addProperty("estado",usuario.getEstado().toString());
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut=rpc;
		
		//Se establece si el WS esta hecho en .net
		envelope.dotNet=false;
				
		envelope.encodingStyle=SoapSerializationEnvelope.XSD;
		
		//Para acceder al WS se crea un objeto de tipo HttpTransport
		HttpTransportSE androidHttpTransport=null;
		try{
			androidHttpTransport = new HttpTransportSE(ConstantesWebService.URL,4000);
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
		public boolean desconectarUsuario(Usuario usuario){
			boolean respuesta=false;
			final String nombreFuncionWebService = "desconectarChofer";
			//Se crea un objeto de tipo soap.
			SoapObject rpc;
			rpc = new SoapObject(ConstantesWebService.NAME_SPACE, nombreFuncionWebService);
			
			rpc.addProperty("usuario", usuario.getUsuario().toString());
			rpc.addProperty("num_movil", usuario.getMovil().getNumero());
		
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut=rpc;
			
			//Se establece si el WS esta hecho en .net
			envelope.dotNet=false;
					
			envelope.encodingStyle=SoapSerializationEnvelope.XSD;
			
			//Para acceder al WS se crea un objeto de tipo HttpTransport
			HttpTransportSE androidHttpTransport=null;
			try{
				androidHttpTransport = new HttpTransportSE(ConstantesWebService.URL,3000);
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
	
		public boolean actualizarUbicacion(Usuario usuario){
			boolean respuesta=false;

			SoapObject rpc;
			rpc = new SoapObject(ConstantesWebService.NAME_SPACE, "actualizarUbicacion");
			
			rpc.addProperty("usuario", usuario.getUsuario());
			rpc.addProperty("ulatitud", Double.toString(usuario.getUbicacionLatitud()));
			rpc.addProperty("ulongitud", Double.toString(usuario.getUbicacionLongitud()));
			
			SoapSerializationEnvelope env = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			env.bodyOut=rpc;
			
			//Se establece si el WS esta hecho en .net
			env.dotNet=false;
					
			env.encodingStyle=SoapSerializationEnvelope.XSD;
			//Para acceder al WS se crea un objeto de tipo HttpTransport
			HttpTransportSE androidHttpTransport=null;
			try{
				androidHttpTransport = new HttpTransportSE(ConstantesWebService.URL,2000);
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
		
		public boolean actualizarEstadoUsuario(Usuario usuario){
			
			boolean respuesta=false;
			final String nombreFuncionWebService = "actualizarEstado";

			SoapObject rpc;
			rpc = new SoapObject(ConstantesWebService.NAME_SPACE, nombreFuncionWebService);
			
			rpc.addProperty("estado", usuario.getEstado().toString());
			rpc.addProperty("usuario", usuario.getUsuario());
			SoapSerializationEnvelope env = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			env.bodyOut=rpc;
			
			//Se establece si el WS esta hecho en .net
			env.dotNet=false;
					
			env.encodingStyle=SoapSerializationEnvelope.XSD;
			//Para acceder al WS se crea un objeto de tipo HttpTransport
			HttpTransportSE androidHttpTransport=null;
			androidHttpTransport = new HttpTransportSE(ConstantesWebService.URL,2500);
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
		
	    public ArrayList<Movil> obtenerMoviles(final String usuario){
		final String nombreFuncionWebService = "obtenerMoviles";
		
		//Se crea un objeto de tipo soap.
		SoapObject rpc;
		rpc = new SoapObject(ConstantesWebService.NAME_SPACE, nombreFuncionWebService);
		
		rpc.addProperty("usuario", usuario);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut=rpc;
		
		//Se establece si el WS esta hecho en .net
		envelope.dotNet=false;
				
		envelope.encodingStyle=SoapSerializationEnvelope.XSD;
		
		//Para acceder al WS se crea un objeto de tipo HttpTransport
		HttpTransportSE androidHttpTransport=null;
		ArrayList<Movil> listaMoviles = null;
		
		try{
			androidHttpTransport = new HttpTransportSE(ConstantesWebService.URL,3500);
			androidHttpTransport.debug=true;
					
			//Se llama al servicio web
			androidHttpTransport.call(ConstantesWebService.NAME_SPACE + "/" + nombreFuncionWebService, envelope);
	
			//guardar la respuesta en una variable
			SoapObject respuestaSoap = (SoapObject) envelope.bodyIn;
			
			Vector<?> respuestaVector = (Vector<?>) respuestaSoap.getProperty(0);
			int c = respuestaVector.size();
			
			listaMoviles = new ArrayList<Movil>(c);
			for(int i=0;i<c;i++){
				Movil movilActual = new Movil();
				SoapObject s = (SoapObject) respuestaVector.get(i);
				movilActual.setNumero(Integer.parseInt(s.getProperty("numero").toString()));
				movilActual.setMarca(s.getProperty("marca").toString());
				movilActual.setModelo(s.getProperty("modelo").toString());
				
				listaMoviles.add(movilActual);
			}
			
			}catch(Exception e){
					e.printStackTrace();
				}
		
		
		return listaMoviles;
	}
	    
	    public boolean enviarMensajeSos(String usuario){
	    	final String nombreFuncionWebService = "mensajeSos";
			boolean respuesta;
			//Se crea un objeto de tipo soap.
			SoapObject rpc;
			rpc = new SoapObject(ConstantesWebService.NAME_SPACE, nombreFuncionWebService);
			
			rpc.addProperty("usuario", usuario);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut=rpc;
			
			//Se establece si el WS esta hecho en .net
			envelope.dotNet=false;
					
			envelope.encodingStyle=SoapSerializationEnvelope.XSD;
			
			//Para acceder al WS se crea un objeto de tipo HttpTransport
			HttpTransportSE androidHttpTransport=null;
			try{
				androidHttpTransport = new HttpTransportSE(ConstantesWebService.URL,3000);
				androidHttpTransport.debug=true;
						
				//Se llama al servicio web
				androidHttpTransport.call(ConstantesWebService.NAME_SPACE + "/"+nombreFuncionWebService, envelope);
		
				//guardar la respuesta en una variable
				respuesta=(Boolean) envelope.getResponse();
				
				}catch(Exception e){
						System.out.println(e.getMessage());
						respuesta=false;
					}
			
			return respuesta;
	    }
	    
	    public boolean enviarClaveGCM(String usuario,String claveGCM){
	    	final String nombreFuncionWebService = "asignarClaveGCM";
			boolean respuesta = false;
			//Se crea un objeto de tipo soap.
			SoapObject rpc;
			rpc = new SoapObject(ConstantesWebService.NAME_SPACE, nombreFuncionWebService);
			
			rpc.addProperty("usuario", usuario);
			rpc.addProperty("claveGCM", claveGCM);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut=rpc;
			
			//Se establece si el WS esta hecho en .net
			envelope.dotNet=false;
					
			envelope.encodingStyle=SoapSerializationEnvelope.XSD;
			
			//Para acceder al WS se crea un objeto de tipo HttpTransport
			HttpTransportSE androidHttpTransport=null;
			try{
				androidHttpTransport = new HttpTransportSE(ConstantesWebService.URL,2500);
				androidHttpTransport.debug=true;
						
				//Se llama al servicio web
				androidHttpTransport.call(ConstantesWebService.NAME_SPACE + "/"+nombreFuncionWebService, envelope);
		
				//guardar la respuesta en una variable
				respuesta=(Boolean) envelope.getResponse();
				
				}catch(Exception e){
						System.out.println(e.getMessage());
						respuesta=false;
					}
			
			return respuesta;
	    }
	    
	    public boolean notificarEstadoPasajeEnCurso(int idPasaje, String estado){
			boolean respuesta=false;
			final String nombreFuncion = "notificarEstadoPasajeEnCurso";
			SoapObject rpc;
			rpc = new SoapObject(ConstantesWebService.NAME_SPACE, nombreFuncion);
			
			rpc.addProperty("idPasaje", idPasaje);
			rpc.addProperty("estado", estado);
		
			SoapSerializationEnvelope env = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			env.bodyOut=rpc;
			
			//Se establece si el WS esta hecho en .net
			env.dotNet=false;
					
			env.encodingStyle=SoapSerializationEnvelope.XSD;
			//Para acceder al WS se crea un objeto de tipo HttpTransport
			HttpTransportSE androidHttpTransport=null;
			try{
				androidHttpTransport = new HttpTransportSE(ConstantesWebService.URL,2500);
				androidHttpTransport.debug=true;
				
				//Se llama al servicio web
				androidHttpTransport.call(ConstantesWebService.NAME_SPACE+"/"+nombreFuncion, env);
				
				//guardar la respuesta en una variable
				respuesta=(Boolean) env.getResponse();
				
				}catch(Exception e){
					System.out.println(e.getMessage());
					respuesta=false;
				}
			
			return respuesta;	
		}
	
}
