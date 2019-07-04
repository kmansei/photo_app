import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

class PhotoServer {
    static final int PORT = 8080;
    static Socket s = null;
    static ServerSocket ss = null;

    public static void main(String[] args) {
        try {
            ss = new ServerSocket(PORT);
            System.out.println("waiting");
            while (true) {
                s = ss.accept();
                System.out.println("accept: " + s);
                new ServerThread(s).start();
            }
        } catch (IOException ioe) {
        } finally {
            try {
                if (ss != null) ss.close();
                if (s != null) s.close();
            } catch (IOException ioe) {
            }
        }
    }
}

class ServerThread extends Thread {
    Socket s;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    String dir = "./images/";
    DatabaseConnection databaseConnection;

    ServerThread(Socket s) {
        this.s = s;
        databaseConnection = new DatabaseConnection();
    }

    public void run() {
        System.out.println("\trun: " + Thread.currentThread());
        try {
            int id;
            byte[] img;
            String user_name;

            String img_path = null;
            BufferedImage img_new = null;

            List<Post> posts = new ArrayList<>();
            //DBの結果
            List<Database> DBResults = new ArrayList<Database>();

            ois = new ObjectInputStream(s.getInputStream());
            //id取得
            id = ois.readInt();
            System.out.println("receive id: " + id);
            //画像のbyte配列取得
            img = (byte[]) ois.readObject();
            System.out.println("receive image");
            //ユーザーネーム取得
            user_name = ois.readUTF();
            System.out.println("receive user_name: "+ user_name);

            //画像の保存
            if(img != null){
                img_path = saveImage(img);
            }

            DBResults = databaseConnection.Connect(id, img_path, user_name);
            for (int i=0; i<DBResults.size(); i++){
                img_new = ImageIO.read(new File(DBResults.get(i).imagePath));
                try{
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    BufferedOutputStream os = new BufferedOutputStream( bos );
                    img_new.flush();
                    ImageIO.write( img_new, "jpg", os ); //. jpg 型
                    byte[] byteimg = bos.toByteArray();

                    Post p = new Post(byteimg, DBResults.get(i).user_name);
                    posts.add(p);
                }catch( Exception e ){}
            }

            oos = new ObjectOutputStream(s.getOutputStream());

            List<byte[]> images = new ArrayList<>();
            posts.forEach(i -> images.add(i.imageData));

            List<String> names = new ArrayList<>();
            posts.forEach(i -> names.add(i.user_name));

            oos.writeObject(images);
            oos.writeObject(names);

            oos.flush();
            System.out.println("send images to client: " + s);

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                if (ois != null) ois.close();
                if (oos != null) oos.close();
                if (s != null) s.close();
            } catch (IOException ioe) {
            }
        }
    }
    String saveImage(byte[] img) {
        try{
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(img));
            System.out.println("saving image");
            //データベースの最新のID+1を取得
            int latestID = databaseConnection.GetLatestId();
            String out_path = dir + String.valueOf(latestID) + ".jpg";
            FileOutputStream out = new FileOutputStream(out_path);
            ImageIO.write( image, "jpg", out);
            System.out.println("image saved");
            return out_path;
        }catch( Exception e ){}
        return "error";
    }

}
