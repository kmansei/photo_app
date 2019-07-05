import java.io.Serializable;

public class Post implements Serializable {
  public byte[] imageData;
  public String user_name;

  public Post(byte[] ImageData, String name){
    this.imageData = ImageData;
    this.user_name = name;
  }
}
