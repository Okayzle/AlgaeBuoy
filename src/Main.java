import com.mongodb.MongoClient;
import com.mongodb.client.MongoDriverInformation;
import jdk.nashorn.internal.ir.debug.JSONWriter;
import org.json.simple.JSONObject;
import sun.net.www.http.HttpClient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;


public class Main {

    public static void main(String[] args) {

        long time = System.currentTimeMillis();
        long end = time + 15000;

        while(System.currentTimeMillis() < end){
            try {
                JSONObject obj = new JSONObject();
                obj.put("salinity", genUnder100Num());
                obj.put("turbidity", genUnder100Num());
                obj.put("pH", genPH());
                // pushes up to the DB
                pushDataToAPI(obj);
                System.out.println(obj);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static void pushDataToAPI(JSONObject payload) {
        try {
            URL url = new URL("localhost:4200/api/upload_data");
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection)con;
            con.setDoInput(true);
            http.setRequestMethod("POST");
            con.setRequestProperty("Content-Type","application/json;charset=UTF-8");
            OutputStream os = http.getOutputStream();
            os.write(payload.toJSONString().getBytes("UTF-8"));

        }catch (IOException e){
        }
    }

    public static void pushDataToMongo(JSONObject payload){
        MongoClient client = new MongoClient();
    }

    public static int genUnder100Num(){
        return new Random().nextInt(100) +1;
    }

    public static int genPH(){
        return new Random().nextInt(7) +1;
    }
}
