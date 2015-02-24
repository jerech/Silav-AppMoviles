package interfaz;


import com.google.android.gms.gcm.GoogleCloudMessaging;


import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class GcmIntentService extends IntentService{
	protected Handler mHandler;

    public GcmIntentService() {
        super("GcmIntentService");
        mHandler = new Handler();
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

                
                mHandler.post(new DisplayToast(extras.getString("mensaje")));
                Log.i("TAG", "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private class DisplayToast implements Runnable{
    	  String mText;

    	  public DisplayToast(String text){
    	    this.mText = text;
    	  }

    	  public void run(){
    	     Toast.makeText(getApplicationContext(), mText, Toast.LENGTH_SHORT).show();
    	  }
    	}
	
	

}
