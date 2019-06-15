package com.example.photoapp;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends IntentService {
    static final int PORT = 8080;

    static Socket s;
    static ObjectInputStream ois;
    static ObjectOutputStream oos;

    static ArrayList<Post> posts;

    public Client(){
        super("IntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent){
        Log.d("onHandleIntent", "moved in Client");
        //Post post = (Post)intent.getSerializableExtra("Post");
        updatePosts();
    }

    static void updatePosts() {

        int id = 7;
        Post p = new Post(null);

        try {
            Log.d("updatePosts", "start tcp");
            s = new Socket(InetAddress.getByName("192.168.2.106"), PORT);
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
            Log.d("updatePosts", "["+e+"]");
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
