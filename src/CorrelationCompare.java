import java.sql.*;
import java.io.*;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Created by fty_07 on 11/30/17.
 */
public class CorrelationCompare {
    public static void main(String[] args){
        Connection con = null;
        Statement sta1 = null;
        Statement sta2 = null;
        ResultSet res1 = null;
        ResultSet res2 = null;
        String sql1, sql2;
        String url = new String("jdbc:mysql://localhost:3306/music_db?characterEncoding=utf8&useSSL=false");
        try (
                PrintWriter writer = new PrintWriter("CorrelationStat.txt", "UTF-8")
                ){
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url,"root","123456");

            sta1 = con.createStatement();
            sql1 = new String("select * from lyrics_db");
            res1 = sta1.executeQuery(sql1);

            sta2 = con.createStatement();
            sql2 = new String("select * from music_db");
            res2 = sta2.executeQuery(sql2);

            int firstMatch = 0, secondMatch = 0, thirdMatch = 0, fourthMatch = 0;
            int match12 = 0, match23 = 0, match123 = 0, match1234 = 0;

            while (res1.next() && res2.next()) {
                double anger_lyrics = res1.getDouble("score_anger");
                double joy_lyrics = res1.getDouble("score_joy");
                double fear_lyrics = res1.getDouble("score_fear");
                double sadness_lyrics = res1.getDouble("score_sadness");

                double anger_music = res2.getDouble("score_anger");
                double joy_music = res2.getDouble("score_joy");
                double fear_music = res2.getDouble("score_fear");
                double sadness_music = res2.getDouble("score_sadness");

                PriorityQueue<Field> pq_lyrics = new PriorityQueue<>(new Comparator<Field>() {
                    @Override
                    public int compare(Field o1, Field o2) {
                        if (o1.val == o2.val) return 0;
                        return o1.val > o2.val ? -1 : 1;
                    }
                });

                pq_lyrics.offer(new Field("anger", anger_lyrics));
                pq_lyrics.offer(new Field("joy", joy_lyrics));
                pq_lyrics.offer(new Field("fear", fear_lyrics));
                pq_lyrics.offer(new Field("sadness", sadness_lyrics));

                PriorityQueue<Field> pq_music = new PriorityQueue<>(new Comparator<Field>() {
                    @Override
                    public int compare(Field o1, Field o2) {
                        if (o1.val == o2.val) return 0;
                        return o1.val > o2.val ? -1 : 1;
                    }
                });

                pq_music.offer(new Field("anger", anger_music));
                pq_music.offer(new Field("joy", joy_music));
                pq_music.offer(new Field("fear", fear_music));
                pq_music.offer(new Field("sadness", sadness_music));

                boolean match1 = false, match2 = false, match3 = false, match4 = false;

                writer.println("id: " + res1.getInt("id") + "  title: " + res1.getString("title"));

                while (!pq_lyrics.isEmpty() && !pq_music.isEmpty()) {
                    Field temp1 = pq_lyrics.poll();
                    Field temp2 = pq_music.poll();
                    writer.println(temp1.emotion + "  " + temp2.emotion);
                    if (temp1.emotion.equals(temp2.emotion)) {
                        if (pq_lyrics.size() == 3) {
                            match1 = true;
                        } else if (pq_lyrics.size() == 2) {
                            match2 = true;
                        } else if (pq_lyrics.size() == 1) {
                            match3 = true;
                        } else {
                            match4 = true;
                        }
                    }
                }

                if (match1) {
                    firstMatch++;
                } else if (match2) {
                    secondMatch++;
                } else if (match3) {
                    thirdMatch++;
                } else if (match4) {
                    fourthMatch++;
                }

                if (match1 && match2 && match3 && match4) {
                    match1234++;
                    match123++;
                    match12++;
                    match23++;
                } else if (match1 && match2 && match3) {
                    match123++;
                    match12++;
                    match23++;
                } else if (match1 && match2) {
                    match12++;
                } else if (match2 && match3) {
                    match23++;
                }

                writer.println("firstMatch: " + firstMatch + "  secondMatch: " + secondMatch + "  thirdMatch: " + thirdMatch + "  fourthMatch: " + fourthMatch);
                writer.println("match1234: " + match1234 + "  match123: " + match123 + "  match12: " + match12 + "  match23: " + match23);
                writer.println();


            }


        }catch (ClassNotFoundException e){
            System.out.println("ClassNotFoundException");

        }catch (SQLException a){
            System.out.println("SQLException");
        }
        catch (Exception b){

        }
        finally {
            try{
                if(con != null)
                    con.close();
                if (sta1 != null)
                    sta1.close();
                if (sta2 != null)
                    sta2.close();
                if(res1 != null)
                    res1.close();
                if(res2 != null)
                    res2.close();
            }catch (Exception e){

            }
        }
    }
}

class Field{
    public String emotion;
    public double val;

    public Field(String emotion, double val) {
        this.emotion = emotion;
        this.val = val;
    }
}
