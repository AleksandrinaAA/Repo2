import com.fasterxml.jackson.databind.ObjectMapper;
import model.CalculationType;
import model.ModeParameters;
import model.SchemeElement;
import model.SchemeRegim;
import mqtt.MyMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.*;
import java.util.Arrays;

public class Main {

        public static void main (String[] args) throws IOException, InterruptedException {

            MyMqttClient mqtt = new MyMqttClient("tcp://localhost:1883");
            ///mqtt.sendMessage("/test","hello from java");
            mqtt.subscribe("fxTopic", new IMqttMessageListener() {
                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    System.out.println(mqttMessage.toString());
                }
            });




            while (true){
                mqtt.sendMessage("/test","hello from java", true);
                Thread.sleep(2000);
            }

            //Thread.sleep(20000);

            /*
            String[] CLASSTER = new String[6];

            String path = "C:\\Users\\Admin\\Desktop\\Шапкин_Алексеева\\txt\\Local23.txt";
            File file = new File(path);

            ModeParameters mp = new ModeParameters();

            int reg = 0;

            if(reg==0){
                mp.setType(CalculationType.LocalOptimization);
            }else{
                mp.setType(CalculationType.Calculation);
            }

            mp.getRegim().add(new SchemeRegim(CLASSTER[0],CLASSTER[1],CLASSTER[2],CLASSTER[3],CLASSTER[4],CLASSTER[5]));

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(file, mp);

            Process process = new ProcessBuilder("C:\\Users\\Admin\\Desktop\\Шапкин_Алексеева\\Opt_Agents\\Новая папка (3)\\ConsoleApplication1\\bin\\Debug\\Rastr_Optimizer.exe", path).start();


            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;

            System.out.println("Получил значения из C# (локальная оптимизация)");


            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
*/

            //String path = "D:\\Text.txt";

            //Функция выбора эквивалентных узлов на граничных линиях

            //Функция выбора очерёдности оптимизации





//            ModeParameters mp = new ModeParameters();
//
//            mp.setType(CalculationType.Calculation);
//
//            mp.getElements().add(new SchemeElement(10,-1,"Vras",6,1));
//
//            ObjectMapper objectMapper = new ObjectMapper();
//
//            //String s = objectMapper.writeValueAsString(mp);
//
//            objectMapper.writeValue(new File(path),mp);
//
//
//
//            Process process = new ProcessBuilder("D:\\VS projects\\ABBB\\ABBB\\bin\\Debug\\ABBB.exe",path).start();
//
//            //System.out.println(s);
//
//            InputStream is = process.getInputStream();
//            InputStreamReader isr = new InputStreamReader(is);
//            BufferedReader br = new BufferedReader(isr);
//            String line;
//
//            System.out.println("Получил значения из C#");
//            while ((line = br.readLine()) != null) {
//                System.out.println(line);
//            }




        }

    }
