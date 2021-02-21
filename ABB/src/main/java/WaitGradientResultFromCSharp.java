import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import model.ModeParameters;
import mqtt.MyMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;

public class WaitGradientResultFromCSharp extends OneShotBehaviour {

    MyMqttClient mqtt;

    String  [] CLASSTER;

    AID top;
    DataStore ds = new DataStore();

    public WaitGradientResultFromCSharp(String[] CLASSTER, AID top, MyMqttClient mqtt) {
        this.mqtt=mqtt;
        this.CLASSTER=CLASSTER;
        this.top=top;
    }

    @Override
    public void action() {
        mqtt.publishMessage(CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1] + "ToJava");
        mqtt.unsubscribe(CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1] + "ToJava");


        mqtt.subscribe(CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1] + "ToJava", new IMqttMessageListener() {

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                System.out.println(getAgent().getLocalName() + "получил ответ с С# о параметрах по град \n" + mqttMessage.toString());

                byte[] payload = mqttMessage.getPayload();
                String JSON_TO_AGENTS = new String(payload, "UTF-8");

                ModeParameters mp = new ModeParameters();
                //Работа с файлом
                ObjectMapper objectMapperr = new ObjectMapper();
                try {
                    mp = objectMapperr.readValue(JSON_TO_AGENTS, ModeParameters.class);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Проверка на окончание шага по параметру, чтобы сменить роли между агентами
                if (mp.getRegim().getLosses()=="Stop"){
                    //Тут можно вызвать класс агентов снова
                    int a=5;
                }

                //Сохраняем данные для второго агента (новый запрос)
                ds.setJSON_TO_JAVA_1(JSON_TO_AGENTS);

                mqtt.unsubscribe(CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1] + "ToJava");
                //exit = true;
            }

        });


    }


}
