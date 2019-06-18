import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.util.List;
import java.io.File;

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
    List<String> paths;

    ServerThread(Socket s) {
        this.s = s;
        //posts = new ArrayList<Post>();
    }

    public void run() {
        System.out.println("\trun: " + Thread.currentThread());
        try {
            int id;
            byte[] img;
            String img_path = null;
            BufferedImage img_new = null;
            List<byte[]> imgList = new ArrayList<byte[]>();
            
            ois = new ObjectInputStream(s.getInputStream());
            //id取得
            id = ois.readInt();
            System.out.println("receive id: " + id);
            //画像のbyte配列取得
            img = (byte[]) ois.readObject();
            System.out.println("receive post");
            //ois.close();
            
            //画像の保存
            img_path = saveImage(img);

            DatabaseConnection databaseConnection = new DatabaseConnection();
            
            paths = databaseConnection.Connect(id, img_path);
            for (int i=0; i<paths.size(); i++){
                //System.out.println(paths.get(i));
                img_new = ImageIO.read(new File(paths.get(i)));
                try{
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    BufferedOutputStream os = new BufferedOutputStream( bos );
                    img_new.flush();
                    ImageIO.write( img_new, "png", os ); //. png 型
                    byte[] byteimg = bos.toByteArray();
                    imgList.add(byteimg);
                }catch( Exception e ){}
            }

            // posts.add(p);
            //databaseID++;

            //ois.close();

            // this is database zone

            oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeObject(imgList);
            oos.flush();
            System.out.println("send images to client: " + s);
            // System.out.println("send sublist");

            // //ois = new ObjectInputStream(s.getInputStream());
            // oos = new ObjectOutputStream(s.getOutputStream());
            // System.out.println("init ois and oos");

            // oos.writeObject(posts);
            // oos.flush();
            // System.out.println("send \"posts\" to client: " + s);

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
            //Fileクラスのオブジェクトを生成する
            File imgDir = new File(dir);
            //listFilesメソッドを使用して一覧を取得する
            File[] fileList = imgDir.listFiles();
            int fileId = fileList.length;
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(img));
            System.out.println("saving image");
            String out_path = dir + String.valueOf(fileId) + ".png";
            FileOutputStream out = new FileOutputStream(out_path);
            ImageIO.write( image, "png", out);
            System.out.println("image saved");
            return out_path;
        }catch( Exception e ){}
            return "error";
    }

}
