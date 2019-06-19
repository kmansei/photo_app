import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class Database {
    public static void main(String[] args) {
        Connection con = null;
        try {
            // JDBCドライバのロード - JDBC4.0（JDK1.6）以降は不要
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            // MySQLに接続
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/photo_app", "root", "Jinskm_1213");
            System.out.println("MySQLに接続できました。");

            //Statement statement = connection.createStatement();
            Statement statement = con.createStatement();

            String sql = "INSERT INTO photos VALUES (1, 'Michael Jackson', 'Butterflies', 2001);";
            int result = statement.executeUpdate(sql);
            System.out.println("結果１：" + result);
 
            sql = "INSERT INTO photos (name, title, year) VALUES ('Aril Brikha', 'Groove La Chord', 1998);";
            result = statement.executeUpdate(sql);
            System.out.println("結果２：" + result);




            
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            System.out.println("JDBCドライバのロードに失敗しました。");
        } catch (SQLException e) {
            System.out.println("MySQLに接続できませんでした。");
            System.out.println(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    System.out.println("MySQLのクローズに失敗しました。");
                }
            }
        }
    }
}