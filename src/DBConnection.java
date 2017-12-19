import java.sql.*;
/**
 * Created by fty_07 on 11/20/17.
 */

public class DBConnection {
    public static void main(String[] args){
        Connection con = null;
        Statement sta = null;
        ResultSet res = null;
        String sql;
        String url = new String("jdbc:mysql://localhost:3306/music_db?characterEncoding=utf8&useSSL=false");
        try{
            System.out.println("*");
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url,"root","123456");
            sta = con.createStatement();
            sql = new String("select * from lyrics_db");
            res = sta.executeQuery(sql);
            System.out.println("id                 title                 artist");
            while(res.next()){
                System.out.print(res.getString("id"));
                System.out.print("\t");
                System.out.print(res.getString("title"));
                System.out.print("\t");
                System.out.print(res.getString("artist") + "\n");
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
                if (sta != null)
                    sta.close();
                if(res != null)
                    res.close();
            }catch (Exception e){

            }
        }
    }
}