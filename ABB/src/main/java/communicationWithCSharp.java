import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.Agent;
import model.CalculationType;
import model.ModeParameters;
import model.SchemeElement;
import model.SchemeRegim;
import mqtt.MyMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class communicationWithCSharp {



    //boolean exit = false;

    /*
    public static void RequestToCSharp(String [] CLASSTER,double[][] border_nodes) throws IOException {

        String path = "C:\\Users\\Admin\\Desktop\\Шапкин_Алексеева\\TextToC#.txt";

        ModeParameters mp = new ModeParameters();

        File file = new File(path);

        String ParametrName;

        mp.setType(CalculationType.Optimization);
        //mp.getRegim().add(new SchemeRegim(CLASSTER[0],CLASSTER[1],CLASSTER[2],CLASSTER[3],CLASSTER[4],CLASSTER[5]));

        mp.getRegim().regimTemplate = CLASSTER[0];
        mp.getRegim().ancTemplate = CLASSTER[1];
        mp.getRegim().transTemplate = CLASSTER[2];
        mp.getRegim().regim = CLASSTER[3];
        mp.getRegim().anc = CLASSTER[4];
        mp.getRegim().trans = CLASSTER[5];
        mp.getRegim().losses = CLASSTER[6];


        for (int i = 1; i < 3; i++) {

            if (i == 1) {
                ParametrName = "Vras";
            } else {
                ParametrName = "Pow";
            }

            for (int j = 0; j < border_nodes[0].length; j++) {

                mp.getElements().add(new SchemeElement((int)border_nodes[0][j], ParametrName, border_nodes[i][j]));
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writeValue(file, mp);
            }
        }


        Process process = new ProcessBuilder("D:\\VS projects\\ABBB\\ABBB\\bin\\Debug\\ABBB.exe", path).start();

        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        System.out.println("Получил значения из C#");
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }

        file.delete();

    }
*/

    public static void RequestToCSharpLocalOptimization(String [] CLASSTER, MyMqttClient mqtt, Agent myAgent) throws IOException, InterruptedException {


        String JSON;

        String Claster = CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1];
        //String path = "C:\\Users\\Admin\\Desktop\\Шапкин_Алексеева\\txt\\Optimization" + Claster + ".txt";


        ModeParameters mp = new ModeParameters();

        mp.setType(CalculationType.LocalOptimization);

        SchemeRegim REGIM = new SchemeRegim();
        mp.setRegim(REGIM);

        mp.getRegim().setRegimTemplate(CLASSTER[0]);
        mp.getRegim().setAncTemplate(CLASSTER[1]);
        mp.getRegim().setTransTemplate(CLASSTER[2]);
        mp.getRegim().setRegim(CLASSTER[3]);
        mp.getRegim().setAnc(CLASSTER[4]);
        mp.getRegim().setTrans(CLASSTER[5]);
        mp.getRegim().setLosses(CLASSTER[6]);

        //JSON = mp.toString();
        ObjectMapper objectMapper = new ObjectMapper();
        JSON = objectMapper.writeValueAsString(mp);


        String OPT_PATH;

        if (Claster.equals("rej23.rg2")) {
            OPT_PATH = "Opt_Agents_1";
        } else {
            OPT_PATH = "Opt_Agents_2";
        }

        String Topic = Claster+"ToCSharp";
        //String Topic = "T";

//        mqtt.sendMessage(Topic, "hello");
//        Process process = new ProcessBuilder("C:\\Users\\Admin\\Desktop\\Шапкин_Алексеева\\" + OPT_PATH + "\\Новая папка (3)\\ConsoleApplication1\\bin\\Debug\\Rastr_Optimizer.exe",Topic).start();

        String[] JSON_TO_JAVA = new String[1];

        //Thread.sleep(1500);

        mqtt.sendMessage(Topic,JSON);


        //myAgent.addBehaviour(new RecieveFromCSharp(CLASSTER,mqtt));

        /*
        mqtt.subscribe(Claster+"ToJava", new IMqttMessageListener() {

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                System.out.println(mqttMessage.toString());
                JSON_TO_JAVA[0] = mqttMessage.toString();
            }
        });

*/


        //mqtt.sendMessage(Topic,JSON);

       //return JSON_TO_JAVA[0];

    }


    public static void RequestToCSharpGradient(String [] CLASSTER, MyMqttClient mqtt, Agent myAgent) throws IOException, InterruptedException {


        String JSON;

        String Claster = CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1];
        //String path = "C:\\Users\\Admin\\Desktop\\Шапкин_Алексеева\\txt\\Optimization" + Claster + ".txt";


        ModeParameters mp = new ModeParameters();

        mp.setType(CalculationType.Gradient);

        SchemeRegim REGIM = new SchemeRegim();
        mp.setRegim(REGIM);

        mp.getRegim().setRegimTemplate(CLASSTER[0]);
        mp.getRegim().setAncTemplate(CLASSTER[1]);
        mp.getRegim().setTransTemplate(CLASSTER[2]);
        mp.getRegim().setRegim(CLASSTER[3]);
        mp.getRegim().setAnc(CLASSTER[4]);
        mp.getRegim().setTrans(CLASSTER[5]);
        mp.getRegim().setLosses(CLASSTER[6]);

        //JSON = mp.toString();
        ObjectMapper objectMapper = new ObjectMapper();
        JSON = objectMapper.writeValueAsString(mp);


        String OPT_PATH;

        if (Claster.equals("rej23.rg2")) {
            OPT_PATH = "Opt_Agents_1";
        } else {
            OPT_PATH = "Opt_Agents_2";
        }

        String Topic = Claster+"ToCSharp";
        //String Topic = "T";

//        mqtt.sendMessage(Topic, "hello");
//        Process process = new ProcessBuilder("C:\\Users\\Admin\\Desktop\\Шапкин_Алексеева\\" + OPT_PATH + "\\Новая папка (3)\\ConsoleApplication1\\bin\\Debug\\Rastr_Optimizer.exe",Topic).start();

        String[] JSON_TO_JAVA = new String[1];



        mqtt.sendMessage(Topic,JSON);


    }



    public static String RequestToCSharpOptimization(String [] CLASSTER, MyMqttClient mqtt) throws IOException {

        String JSON;

        String Claster = CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1];
        //String path = "C:\\Users\\Admin\\Desktop\\Шапкин_Алексеева\\txt\\Optimization" + Claster + ".txt";

        //String path = "C:\\Users\\Admin\\Desktop\\Шапкин_Алексеева\\txt\\Local"+CLASSTER[0]+".txt";


        ModeParameters mp = new ModeParameters();

        mp.setType(CalculationType.Optimization);

        SchemeRegim REGIM = new SchemeRegim();
        mp.setRegim(REGIM);

        mp.getRegim().setRegimTemplate(CLASSTER[0]);
        mp.getRegim().setAncTemplate(CLASSTER[1]);
        mp.getRegim().setTransTemplate(CLASSTER[2]);
        mp.getRegim().setRegim(CLASSTER[3]);
        mp.getRegim().setAnc(CLASSTER[4]);
        mp.getRegim().setTrans(CLASSTER[5]);
        mp.getRegim().setLosses(CLASSTER[6]);



        //JSON = mp.toString();

        ObjectMapper objectMapper = new ObjectMapper();
        JSON = objectMapper.writeValueAsString(mp);

        mqtt.sendMessage(Claster+"ToCSharp",JSON);


/*
        mqtt.subscribe(CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1] + "ToJava", new IMqttMessageListener() {

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                System.out.println(mqttMessage.toString());

                byte[] payload = mqttMessage.getPayload();
                String JSON = new String(payload, "UTF-8");

                //Сохраняем данные для второго агента (новый запрос)
                //ds.setJSON_TO_JAVA_1(JSON_TO_AGENTS);


            }

        });


 */


        /*
        mqtt.subscribe(Claster+"ToJava", new IMqttMessageListener() {

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                System.out.println(mqttMessage.toString());
                JSON_TO_JAVA[0] = mqttMessage.toString();
            }
        });
        */









        return JSON;

    }




    public static String AnswerFromCSharp(String [] CLASSTER) throws IOException {


        String Claster = CLASSTER[0].split("\\\\")[CLASSTER[0].split("\\\\").length - 1];
        String path = "C:\\Users\\Admin\\Desktop\\Шапкин_Алексеева\\txt\\Optimization" + Claster + ".txt";

        //String path = "C:\\Users\\Admin\\Desktop\\Шапкин_Алексеева\\txt\\Local"+CLASSTER[0]+".txt";


        String OPT_PATH;

        if (Claster.equals("rej23.rg2")) {
            OPT_PATH = "Opt_Agents_1";
        } else {
            OPT_PATH = "Opt_Agents_2";
        }


        //Получение JSON
        BufferedReader brr = new BufferedReader(new FileReader(path));
        String JSON = brr.readLine();
        brr.close();

        return JSON;

    }






/*
    public static double[][] DataFromTXT(String [] CLASSTER, String JSON_TO_AGENTS, String losses) throws IOException {

        List<Integer> borders = new ArrayList<>();
        List<Double> Vras = new ArrayList<>();
        List<Double> Pow = new ArrayList<>();


        String[] items = CLASSTER[0].split("\\\\");
        String path = "C:\\Users\\Admin\\Desktop\\Шапкин_Алексеева\\txt\\Local"+items[items.length-1]+".txt";

        //String path = "C:\\Users\\Admin\\Desktop\\Шапкин_Алексеева\\txt\\TextToC#.txt";


        File file2 = new File(path);

        ModeParameters Data = new ModeParameters();

        //Формирование Json
        JSON_TO_AGENTS = Data.toString();


        ObjectMapper objectMapperr = new ObjectMapper();
        try {
            Data = objectMapperr.readValue(file2, ModeParameters.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //int D = Data.elements.get(0).numberNode;

        //Фиксирование потерь
        losses = Data.regim.losses;

        int size_of_Data = Data.elements.size();

        for (int i=0;i<size_of_Data/2;i++){
            borders.add(Data.elements.get(i).numberNode);
            Vras.add(Data.elements.get(i).value);
            Pow.add(Data.elements.get(i+size_of_Data/2).value);
        }

        double[][] border_nodes = new double[borders.size()][3];

        for (int i=0; i<borders.size();i++){
            border_nodes[i][0] = borders.get(i);
            border_nodes[i][1] = Vras.get(i);
            border_nodes[i][2] = Pow.get(i);
        }


        return border_nodes;
    }
*/




}
