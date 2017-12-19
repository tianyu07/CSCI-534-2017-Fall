import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.sql.*;
import java.util.regex.*;

/**
 * Created by fty_07 on 11/24/17.
 */
public class CompareAlgorithm {
    public static void main(String[] args){
        String url = new String("jdbc:mysql://localhost:3306/music_db?characterEncoding=utf8&useSSL=false");
        double anger = 0, joy = 0, fear = 0, sadness = 0, surprise = 0, minVar = Double.MAX_VALUE;
        String artist = "";
        String title = "";
        try(
                FileReader fr = new FileReader("output.txt");
                BufferedReader br = new BufferedReader(fr)
                ) {
            String lineTxt;

            while ((lineTxt = br.readLine()) != null) {
                if (isInteger(lineTxt) && Integer.parseInt(lineTxt) == 1) {
                    break;
                }
            }

            if ((lineTxt = br.readLine()) != null) {
                anger = Double.parseDouble(lineTxt);
            }
            if ((lineTxt = br.readLine()) != null) {
                joy = Double.parseDouble(lineTxt);
            }
            if ((lineTxt = br.readLine()) != null) {
                fear = Double.parseDouble(lineTxt);
            }
            if ((lineTxt = br.readLine()) != null) {
                sadness = Double.parseDouble(lineTxt);
            }
            if ((lineTxt = br.readLine()) != null) {
                surprise = Double.parseDouble(lineTxt);
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        try (
                Connection con = DriverManager.getConnection(url,"root","123456");
                Statement sta = con.createStatement();
                PrintWriter writer = new PrintWriter("MusicPlay.txt", "UTF-8")
                ) {

            ResultSet res;
            String sql;
            Class.forName("com.mysql.jdbc.Driver");
            sql = new String("select * from music_db");
            res = sta.executeQuery(sql);

            while(res.next()){
                double angerDB = res.getDouble("score_anger");
                double joyDB = res.getDouble("score_joy");
                double fearDB = res.getDouble("score_fear");
                double sadnessDB = res.getDouble("score_sadness");
                double surpriseDB = res.getDouble("score_surprise");

                double sum = anger * angerDB + joy * joyDB + fear * fearDB + sadness * sadnessDB + surprise * surpriseDB;
                double sumAsquare = anger * anger + joy * joy + fear * fear + sadness * sadness + surprise * surprise;
                double sumBsquare = angerDB * angerDB + joyDB * joyDB + fearDB * fearDB + sadnessDB * sadnessDB + surpriseDB * surpriseDB;
                double cosine = sum / (Math.sqrt(sumAsquare) * Math.sqrt(sumBsquare));

                System.out.println(cosine + "    " + minVar);

                if (1 - cosine < minVar) {
                    minVar = 1 - cosine;
                    artist = res.getString("artist");
                    title = res.getString("title");
                }

            }

            System.out.println("artist: " + artist + "   title: " + title);
            writer.println(title + " " + artist);

        } catch (ClassNotFoundException e){
            System.out.println("ClassNotFoundException");
        } catch (SQLException a){
            System.out.println("SQLException");
        } catch (Exception e){
            System.out.println("Unexpected Exception");
        }
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}
