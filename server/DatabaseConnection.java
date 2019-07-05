import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    //String databasePath = "jdbc:mysql://localhost/photo_app";
    String databasePath = "jdbc:mysql://localhost:3306/photo_app";
    String user = "root";
    String password = "Jinskm_1213";
    //String password = "";

    public List<Database> Connect(int id, String path, String user_name){
        Connection con = null;
        PreparedStatement statement = null;
        String sql = null;
        List<Database> DBposts = new ArrayList<Database>();
        try {
            // JDBCドライバのロード - JDBC4.0（JDK1.6）以降は不要
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            // MySQLに接続
            con = DriverManager.getConnection(databasePath, user, password);
            System.out.println("MySQLに接続できました。");

            int nextID = GetLatestId();
            System.out.println(nextID);

            if(path != null){
                //画像URLの格納
                sql = "INSERT INTO photos VALUES (?, ?, ?);";
                statement = con.prepareStatement(sql);
                statement.setInt(1, nextID);
                statement.setString(2, path);
                statement.setString(3, user_name);
                int result2= statement.executeUpdate();
                System.out.println("結果1：" + result2);
            }else{
                System.out.println("Only update");
                nextID--;
            }

            //Client側の未更新データを取得
            sql = "SELECT * FROM photos WHERE id > ?;";
            statement = con.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet result3 = statement.executeQuery();
            System.out.println("画像pathの取得");

            for (int i=0; i<(nextID - id); i++){
                result3.next();
                path = result3.getString(2);
                user_name = result3.getString(3);

                System.out.println(path+" "+user_name);

                Database p = new Database(path, user_name);
                DBposts.add(p);

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
        return DBposts;
    }

    //最新のidを取得
    public int GetLatestId() throws SQLException {
        Connection con = DriverManager.getConnection(databasePath, user, password);
        String sql = "SELECT MAX(id) FROM photos;";
        PreparedStatement statement = con.prepareStatement(sql);
        ResultSet result = statement.executeQuery();
        result.next();
        return Integer.parseInt(result.getString(1)) + 1;
    }
}
