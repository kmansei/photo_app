package com.example.photoapp;

import android.os.AsyncTask;
import android.util.Log;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client extends AsyncTask<Void, String, List<Post>> {
    private CallBackTask callbacktask;
    private byte[] imageData;
    private int id;
    private String user_name;

    static final int PORT = 8080;
    static Socket s;
    static ObjectInputStream ois;
    static ObjectOutputStream oos;

    //接続先IP
    //public static String ip = "192.168.0.8";
    public static String ip = "10.24.85.133";

    public Client(int currentId, byte[] images, String name) {
        super();
        id = currentId;
        imageData = images;
        user_name = name;
    }

    @Override
    protected List<Post> doInBackground(Void... param){
        List<Post> posts = updatePosts(id, imageData, user_name);
        return posts;
    }

    @Override
    protected void onPostExecute(List<Post> result) {
        super.onPostExecute(result);
        callbacktask.CallBack(result);
    }

    public void setOnCallBack(CallBackTask _cbj) {
        callbacktask = _cbj;
    }

    public static class CallBackTask {
        public void CallBack(List<Post> result) {
        }
    }

    static List<Post> updatePosts(int id, byte[] image, String user_name) {

        //int id = 3;
        List<Post> posts = new ArrayList<>();
        List<byte[]> images = new ArrayList<>();
        List<String> names = new ArrayList<>();

        try {
            Log.d("updatePosts", "start tcp");

            s = new Socket(InetAddress.getByName(ip), PORT);
            oos = new ObjectOutputStream(s.getOutputStream());

            oos.writeInt(id);
            oos.flush();
            System.out.println("send id");

            oos.writeObject(image);
            oos.flush();
            System.out.println("send image");

            oos.writeUTF(user_name);
            oos.flush();
            System.out.println("send user_name");

            ois = new ObjectInputStream(s.getInputStream());

            images = (List<byte[]>) ois.readObject();
            names = (List<String>) ois.readObject();

            Log.d("updatePosts", "received posts");
            for(int i=0; i<images.size(); i++){
                Post p = new Post(images.get(i), names.get(i));
                posts.add(p);
            }

            Log.d("updatePosts", "received posts");

        } catch (Exception e) {
            Log.d("updatePosts", "["+e+"]");
        }
        return posts;
    }
}
