import java.io.Serializable;
import java.util.Date;

class Post implements Serializable {
    Byte[] bmp;
    String text;
    Date date;
    String userName;
    Post(Byte[] bmp) {
        this.bmp = bmp;
    }
}