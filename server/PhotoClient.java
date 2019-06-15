import java.io.*;
import java.net.*;
import java.util.*;

class PhotoClient {
    static final int PORT = 8080;

    static Socket s;
    static ObjectInputStream ois;
    static ObjectOutputStream oos;

    static ArrayList<Post> posts;

    public static void main(String[] args) {
        getPosts();

        updatePosts();

        try {
            if (s != null) s.close();

            if (ois != null) ois.close();
            if (oos != null) oos.close();
        } catch (IOException ioe) {
        }
}

    static void getPosts() {
        try {
            InetAddress addr = InetAddress.getByName("jinsakuma.local");
            System.out.println(addr);
            s = new Socket(addr, 8080);
            ois = new ObjectInputStream(s.getInputStream());
            posts = (ArrayList<Post>) ois.readObject();
            System.out.println("now received: " + posts);
            //ois.close();
        } catch (Exception e) {
            System.out.println(e);
        } /*finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException ioe) {
                System.out.println(ioe);
            }
        }*/
    }

    static void updatePosts() {

        int id = 7;
        Post p = new Post(null);

        try {
            oos = new ObjectOutputStream(s.getOutputStream());

            oos.writeInt(id);
            oos.flush();
            System.out.println("send id");

            oos.writeObject(p);
            oos.flush();
            System.out.println("send post");

            //oos.close();

            ois = new ObjectInputStream(s.getInputStream());

            ArrayList<Post> newPosts;
            newPosts = (ArrayList<Post>) ois.readObject();

            System.out.println("received: " + newPosts);

        } catch (Exception e) {
            System.out.println(e);
        } /*finally {
            try {
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException ioe) {
                System.out.println(ioe);
            }
        }*/
    }
}
