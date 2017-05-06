package alejandrogar.mqttandroid;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Main extends AppCompatActivity implements MqttCallback {


    // Needed for MQTT connection
    MqttClient client;
    String broker = "tcp://iot.eclipse.org:1883";
    String topic        = "topic/groups/messages";
    int qos             = 2;
    String clientId;

    // Needed for show messages sended
    ListView chatMessages;
    ArrayList<Map<String, String>> messages;
    SimpleAdapter adapter;
    TextView message_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        chatMessages = (ListView) findViewById(R.id.chatMessages);
        message_text = (TextView) findViewById(R.id.message_text);
        messages = new ArrayList<>();

        adapter = new SimpleAdapter(this, messages,
                android.R.layout.simple_list_item_2,
                new String[] {"message", "from"},
                new int[] {android.R.id.text1,
                        android.R.id.text2});

        chatMessages.setAdapter(adapter);

        try {
            clientId = UUID.randomUUID().toString();
            client = new MqttClient(broker, clientId, new MemoryPersistence());
            client.setCallback(this);
            client.connect();
            client.subscribe(topic);

        } catch (MqttException e) {
            e.printStackTrace();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Objects.equals(message_text.getText().toString(), "")){
                    sendMessage(client , clientId, (message_text.getText().toString()));
                }
            }
        });
    }

    public void sendMessage(MqttClient client ,String clientId, String message){
        try {
            MqttMessage msg = new MqttMessage(message.getBytes());
            msg.setQos(qos);
            client.publish(topic, msg);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void connectionLost(Throwable cause) {
        Log.i("Connection status", "Conneection lost, cause" + cause);
    }

    @Override
    public void messageArrived(String topic, final MqttMessage message) throws Exception {
        Log.i("From topic: "+ topic,"Message: "+ message);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> messageToSend = new HashMap<>(2);
                messageToSend.put("message", message.toString());

                // Implement your own logic to set the correct client id
                // sending some json {"client": clientid, "messasge": message}
                // parsing and get the id from the message author.
                messageToSend.put("from", clientId);
                messages.add(messageToSend);
                adapter.notifyDataSetChanged();
                scrollMyListViewToBottom();
                message_text.setText("");
            }
        });
    }

    private void scrollMyListViewToBottom() {
        chatMessages.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                chatMessages.setSelection(adapter.getCount() - 1);
            }
        });
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        //Log.i("IMqttDeliveryToken", "token:" + token);
    }
}
