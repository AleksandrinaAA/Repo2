public class DataStore {

    public static String  JSON_TO_JAVA_1;
    public static String  JSON_TO_JAVA_2;

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        DataStore.count = count;
    }

    public static int  count = 0;

    public String getJSON_TO_JAVA_1() {
        return JSON_TO_JAVA_1;
    }

    public String getJSON_TO_JAVA_2() {
        return JSON_TO_JAVA_2;
    }

    public void setJSON_TO_JAVA_1(String JSON_TO_JAVA_1) {
        this.JSON_TO_JAVA_1 = JSON_TO_JAVA_1;
    }

    public void setJSON_TO_JAVA_2(String JSON_TO_JAVA_2) {
        this.JSON_TO_JAVA_2 = JSON_TO_JAVA_2;
    }

}
