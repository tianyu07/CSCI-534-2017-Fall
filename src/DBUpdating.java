import java.io.*;
import java.sql.*;

/**
 * Created by fty_07 on 11/29/17.
 */
public class DBUpdating {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/music_db?characterEncoding=utf8&useSSL=false";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "123456";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try(
                FileReader fr = new FileReader("music.txt");
                BufferedReader br = new BufferedReader(fr)
        ) {
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected database successfully...");
            System.out.println("Updating records into the table...");

            String line;
            String sql;
            double anger = 0, joy = 0, fear = 0, sadness = 0;
            Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);

            sql = new String("select * from music_db");
            System.out.println(sql);
            ResultSet res = st.executeQuery(sql);

            while (res.next()) {
                double angerDB = res.getDouble("score_anger");
                double joyDB = res.getDouble("score_joy");
                double fearDB = res.getDouble("score_fear");
                double sadnessDB = res.getDouble("score_sadness");

                for (int j = 0; j < 6; j++) {
                    line = br.readLine();
                    switch (j) {
                        case 2:
                            joy = Double.parseDouble(line);
                            break;
                        case 3:
                            sadness = Double.parseDouble(line);
                            break;
                        case 4:
                            anger = Double.parseDouble(line);
                            break;
                        case 5:
                            fear = Double.parseDouble(line);
                            break;
                        default:
                            break;
                    }
                }

                res.updateDouble("score_anger", anger * 0.2 + angerDB * 0.8);
                res.updateDouble("score_joy", joy * 0.2 + joyDB * 0.8);
                res.updateDouble("score_fear", fear * 0.2 + fearDB * 0.8);
                res.updateDouble("score_sadness", sadness * 0.2 + sadnessDB * 0.8);
                res.updateRow();
            }

            System.out.println("Updated records into the table...");

        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    conn.close();
            }catch(SQLException se){
            }// do nothing
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
    }//end main
}//end JDBCExample
