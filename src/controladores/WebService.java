package controladores;

import java.util.ArrayList;
import java.util.Vector;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import constantes.ConstantesWebService;

import modelo.Movil;;

public class WebService {
	
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
			androidHttpTransport = new HttpTransportSE(ConstantesWebService.URL);
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
				androidHttpTransport = new HttpTransportSE(ConstantesWebService.URL);
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
				androidHttpTransport = new HttpTransportSE(ConstantesWebService.URL);
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
	
}
