package alejandrogar.serviceboot;

import android.app.ActivityManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import alejandrogar.serviceboot.Services.ServiceBoot;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("Services runing:", isServiceRunning()+"");

        if(!isServiceRunning()){
            // LANZAR SERVICIO
            Intent serviceIntent = new Intent(this, ServiceBoot.class);
            //serviceIntent.setAction("alejandrogar.Services.ServiceBoot");
            startService(serviceIntent);
        }
    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if("alejandrogar.serviceboot.Services".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}