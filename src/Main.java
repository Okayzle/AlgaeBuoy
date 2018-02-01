import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDriverInformation;
import jdk.nashorn.internal.ir.debug.JSONWriter;
import org.bson.BSON;
import org.bson.BSONObject;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import sun.net.www.http.HttpClient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import static com.mongodb.client.model.Filters.eq;


public class Main {

    public static void main(String[] args) {

        long time = System.currentTimeMillis();
        long end = time + 15000;

        while(System.currentTimeMillis() < end){
            try {
                JSONObject obj = new JSONObject();

                JSONArray lakes = new JSONArray();

                JSONObject lake = new JSONObject();
                lake.put("lakeName","Lake Michigan");

                JSONArray buoys = new JSONArray();

                JSONObject buoy = new JSONObject();
                buoy.put("BuoyID",0);
                //buoy.put("buoyLocation","Bradford Beach");
                //buoy.put("currentConditionCode","green");

                JSONArray measurements = new JSONArray();

                JSONObject measurement = new JSONObject();
                java.util.Date date = new java.util.Date();
                date.setTime(System.currentTimeMillis());
                measurement.put("time", date.toString());
                measurement.put("salinity", genUnder100Num());
                measurement.put("turbidity", genUnder100Num());
                measurement.put("pH", genPH());

                measurements.add(measurement);
                buoy.put("measurements", measurements);
                buoys.add(buoy);
                lake.put("buoys", buoys);
                lakes.add(lake);
                obj.put("lakes",lakes);

                // pushes up to the DB
                pushDataToAPI(obj);
                pushDataToMongo(obj);
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
        MongoClient client = new MongoClient(new MongoClientURI("mongodb://GreenJake:GreenJake1@ds213338.mlab.com:13338/greenaglae"));
        //Document document = new Document();

//        for (Object key : payload.keySet()) {
//            document.append(key.toString(),payload.get(key));
//        }
//        System.out.println(document.toString());

        //BSONObject bsonObject = BSON.decode(payload.toJSONString().getBytes());
        //BSON bson = new BSON();
        Document document = Document.parse(payload.toJSONString());



        MongoCollection collection =
                client.getDatabase("BuoyData")
                        .getCollection(String.valueOf(payload.get("lakeID")));

        collection.insertOne(document);

    }

    public static int genUnder100Num(){
        return new Random().nextInt(100) +1;
    }

    public static int genPH(){
        return new Random().nextInt(7) +1;
    }
}
