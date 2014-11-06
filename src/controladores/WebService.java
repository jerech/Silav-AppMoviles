package controladores;

import java.util.ArrayList;
import java.util.Vector;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import constantes.ConstantesWebService;

import modelo.Remis;;

public class WebService {
	
	    public ArrayList<Remis> obtenerRemises(final String usuario){
		final String nombreFuncionWebService = "obtenerRemises";
		
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
		ArrayList<Remis> listaRemises = null;
		
		try{
			androidHttpTransport = new HttpTransportSE(ConstantesWebService.URL);
			androidHttpTransport.debug=true;
					
			//Se llama al servicio web
			androidHttpTransport.call(ConstantesWebService.NAME_SPACE + "/" + nombreFuncionWebService, envelope);
	
			//guardar la respuesta en una variable
			SoapObject respuestaSoap = (SoapObject) envelope.bodyIn;
			
			Vector<?> respuestaVector = (Vector<?>) respuestaSoap.getProperty(0);
			int c = respuestaVector.size();
			
			listaRemises = new ArrayList<Remis>(c);
			for(int i=0;i<c;i++){
				Remis remisActual = new Remis();
				SoapObject s = (SoapObject) respuestaVector.get(i);
				remisActual.setNumero(Integer.parseInt(s.getProperty("numero").toString()));
				remisActual.setMarca(s.getProperty("marca").toString());
				remisActual.setModelo(s.getProperty("modelo").toString());
				
				listaRemises.add(remisActual);
			}
			
			}catch(Exception e){
					e.printStackTrace();
				}
		
		
		return listaRemises;
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
	
}
