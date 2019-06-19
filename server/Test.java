import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class Test {
    public static void main(String[] args) {
        Connection con = null;
        try {
            // JDBCドライバのロード - JDBC4.0（JDK1.6）以降は不要
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            // MySQLに接続
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/photo_app", "root", "Jinskm_1213");
            System.out.println("MySQLに接続できました。");

            // String sql = "INSERT INTO music VALUES (6, 'Michael Jackson', 'Butterflies', ?);";
            // PreparedStatement statement = con.prepareStatement(sql);
            // int year = 2000;
            // statement.setInt(1, year);  
            // int result = statement.executeUpdate();
            // System.out.println("結果１：" + result);

            String sql2 = "SELECT MAX(id) FROM music;";

            //Statement statement = connection.createStatement();
            //Statement statement = con.createStatement(sql);
            PreparedStatement statement = con.prepareStatement(sql2);
            // int year = 2000;
            // statement.setInt(1, year);  
            //int result = statement.executeUpdate();
            ResultSet result2 = statement.executeQuery();
            result2.next();
            int id = result2.getString(1);
            System.out.println(id);
            //System.out.println("結果１：" + result);
 
            // sql = "INSERT INTO photos (name, title, year) VALUES ('Aril Brikha', 'Groove La Chord', 1998);";
            // result = statement.executeUpdate(sql);
            // System.out.println("結果２：" + result);




            
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