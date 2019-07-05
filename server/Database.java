import java.io.Serializable;

public class Database implements Serializable {
    public String imagePath; //画像のデータ
    public String user_name;


    public Database(String ImagePath, String name) {
        this.imagePath = ImagePath;
        this.user_name = name;
    }
}