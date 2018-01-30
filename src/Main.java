import org.json.simple.JSONObject;
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

                System.out.println(obj);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    public static int genUnder100Num(){
        return new Random().nextInt(100) +1;
    }

    public static int genPH(){
        return new Random().nextInt(7) +1;
    }
}
