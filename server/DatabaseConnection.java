import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    public DatabaseConnection() {
        //this.id = id;
        //this.bytes = bytes;
    }

    public List<String> Connect(int id, String path){
        Connection con = null;
        PreparedStatement statement = null;
        String sql = null;
        List<String> paths = new ArrayList<String>();
        try {
            // JDBCドライバのロード - JDBC4.0（JDK1.6）以降は不要
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            // MySQLに接続
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/photo_app", "root", "Jinskm_1213");
            System.out.println("MySQLに接続できました。");

            //最大idの取得
            sql = "SELECT MAX(id) FROM photos;";
            statement = con.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            result.next();
            int nextID = Integer.parseInt(result.getString(1)) + 1;
            System.out.println(nextID);
            
            //画像URLの格納
            sql = "INSERT INTO photos VALUES (?, ?);";
            statement = con.prepareStatement(sql);
            statement.setInt(1, nextID);
            statement.setString(2, path);
            int result2= statement.executeUpdate();
            System.out.println("結果１：" + result2);

            //Client側の未更新データを取得
            sql = "SELECT * FROM photos WHERE id > ?;";
            statement = con.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet result3 = statement.executeQuery();
            System.out.println("画像pathの取得");
            
            for (int i=0; i<(nextID - id); i++){
                result3.next();
                path = result3.getString(2);
                System.out.println(path);
                paths.add(path);
                System.out.println("added List");
            }

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
        return paths;
    }
}