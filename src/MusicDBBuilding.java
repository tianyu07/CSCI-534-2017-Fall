import java.sql.*;
import java.io.*;
/**
 * Created by fty_07 on 11/30/17.
 */

public class MusicDBBuilding {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/music_db?characterEncoding=utf8&useSSL=false";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "123456";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try (
                FileReader fr = new FileReader("music.txt");
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
                sql.append("INSERT INTO music_db VALUES (");
                sql.append(i + ", ");
                for (int j = 0; j < 6; j++) {
                    line = br.readLine();
                    if (j == 0) {
                        String afterDecode = line.replaceAll("'", "''");
                        sql.append("'" + afterDecode + "', ");
                    } else if (j >= 1 && j <= 4) {
                        sql.append(line + ", ");
                    } else {
                        sql.append(line + ")");
                    }
                }

                System.out.println(sql.toString());
                stmt.executeUpdate(sql.toString());
            }
            System.out.println("Inserted records into the table...");

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    conn.close();
            } catch (SQLException se) {
            }// do nothing
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
    }//end main
}