package controladores;


import com.google.android.gms.gcm.GoogleCloudMessaging;


import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class GcmIntentService extends IntentService{
	public static final String ACTION_PASAJE =
	        "NUEVO_PASAJE";

    public GcmIntentService() {
        super("GcmIntentService");
  
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                Log.e("Send error: ",extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                Log.d("Deleted messages on server: ", extras.toString());
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            			
                		//Comunicamos los datos del pasaje
                        Intent bcIntent = new Intent();
                   
                        bcIntent.setAction(ACTION_PASAJE);
                        bcIntent.putExtra("cliente", extras.getString("cliente"));
                        bcIntent.putExtra("direccion", extras.getString("direccion"));
                        String id = extras.getString("id");
                        bcIntent.putExtra("id", Integer.parseInt(id));
                        bcIntent.putExtra("fecha", extras.getString("fecha"));
                       
                        sendBroadcast(bcIntent);
                        
		                Log.i("TAG", "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
	
	

}
