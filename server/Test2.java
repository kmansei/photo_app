

public class Test2{

    //public static String str = "クラス変数が呼び出されました"; // クラス変数

    public static void main(String[] args){ // クラスメソッド

        //System.out.println(str); // クラス変数に直接アクセス

        DatabaseConnection databaseConnection = new DatabaseConnection(); // Test型のオブジェクトを生成

        databaseConnection.Connect(4, 2020);        // インスタンスメソッドにアクセスするために、Test型オブジェクトを参照

    }
}