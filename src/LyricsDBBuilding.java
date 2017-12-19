/**
 * Created by fty_07 on 11/21/17.
 */
//STEP 1. Import required packages
import java.sql.*;
import java.io.*;

public class LyricsDBBuilding {
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
                FileReader fr = new FileReader("lyrics.txt");
                BufferedReader br = new BufferedReader(fr)
                ) {
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 3: Open a connection
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected database successfully...");

            //STEP 4: Execute a query
            System.out.println("Inserting records into the table...");

            String line;
            StringBuffer sql;
            stmt = conn.createStatement();
            for (int i = 1; i < 200; i++) {
                sql = new StringBuffer();
                sql.append("INSERT INTO lyrics_db VALUES (");
                for (int j = 0; j < 10; j++) {
                    line = br.readLine();
                    if (j == 0) {
                        sql.append(i + ", ");
                    } else if (j >= 1 && j <= 3) {
                        String afterDecode = line.replaceAll("'", "''");
                        sql.append("'" + afterDecode + "', ");
                    } else if (j >= 4 && j <= 8) {
                        sql.append(line + ", ");
                    } else {
                        sql.append("'')");
                    }
                }

                System.out.println(sql.toString());
                stmt.executeUpdate(sql.toString());
            }
            System.out.println("Inserted records into the table...");

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