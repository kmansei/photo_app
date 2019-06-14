import java.io.*;
import java.net.*;
import java.util.*;

class PhotoServer {
    static final int PORT = 8080;
    static Socket s = null;
    static ServerSocket ss = null;
    
    public static void main(String[] args) {
        try {
            ss = new ServerSocket(PORT);
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
    ArrayList<Post> posts;

    ServerThread(Socket s) {
        this.s = s;
        posts = new ArrayList<Post>();
    }

    public void run() {
        System.out.println("\trun: " + Thread.currentThread());
        try {
            //ois = new ObjectInputStream(s.getInputStream());
            oos = new ObjectOutputStream(s.getOutputStream());
            System.out.println("init ois and oos");

            getPostsDB(); // not implemented
            posts.add(new Post(null));
            posts.add(new Post(null));
            posts.add(new Post(null));
            posts.add(new Post(null));
            posts.add(new Post(null));
            posts.add(new Post(null));
            posts.add(new Post(null));
            posts.add(new Post(null));
            posts.add(new Post(null));
            posts.add(new Post(null));
            int databaseID = 10;

            System.out.println(posts);

            oos.writeObject(posts);
            oos.flush();
            System.out.println("send \"posts\" to client: " + s);
            
            //oos.close();
            ois = new ObjectInputStream(s.getInputStream());

            int id;
            Post p;

            id = ois.readInt();
            System.out.println("receive id: " + id);

            p = (Post) ois.readObject();
            System.out.println("receive post: " + p);

            posts.add(p);
            databaseID++;

            //ois.close();

            // this is database zone

            oos = new ObjectOutputStream(s.getOutputStream());

            ArrayList<Post> newPosts = new ArrayList<Post>(posts.subList(id + 1, databaseID));

            oos.writeObject(newPosts);
            System.out.println("send sublist");

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
    void getPostsDB() {
        System.out.println("get the tiimeline from the databese :D");
    }
}
