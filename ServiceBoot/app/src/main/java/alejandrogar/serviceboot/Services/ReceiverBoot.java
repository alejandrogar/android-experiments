package alejandrogar.serviceboot.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import alejandrogar.serviceboot.Main;

/**
 * Created by Manuel on 06/05/17.
 * <p>
 * Do not edit this file
 */

public class ReceiverBoot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // LANZAR SERVICIO
        Intent serviceIntent = new Intent(context, ServiceBoot.class);
        //serviceIntent.setAction("alejandrogar.Services.ServiceBoot");
        context.startService(serviceIntent);

        // LANZAR ACTIVIDAD
        Intent i = new Intent(context, Main.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}