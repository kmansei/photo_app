package com.example.photoapp;

import android.os.AsyncTask;
import android.util.Log;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client extends AsyncTask<Void, String, List<byte[]>> {
    private CallBackTask callbacktask;
    private byte[] imageData;
    private int id;

    static final int PORT = 8080;
    static Socket s;
    static ObjectInputStream ois;
    static ObjectOutputStream oos;

    public Client(int currentId, byte[] images) {
        super();
        id = currentId;
        imageData = images;
    }

    @Override
    protected List<byte[]> doInBackground(Void... param){
        List<byte[]> newImages = new ArrayList<byte[]>();
        newImages = updatePosts(id, imageData);
        return newImages;
    }

    @Override
    protected void onPostExecute(List<byte[]> result) {
        super.onPostExecute(result);
        callbacktask.CallBack(result);
    }

    public void setOnCallBack(CallBackTask _cbj) {
        callbacktask = _cbj;
    }

    public static class CallBackTask {
        public void CallBack(List<byte[]> result) {
        }
    }

    static List<byte[]> updatePosts(int id, byte[] image) {

        //int id = 3;
        List<byte[]> newImages = new ArrayList<byte[]>();

        try {
            Log.d("updatePosts", "start tcp");
            s = new Socket(InetAddress.getByName("192.168.48.237"), PORT);
            oos = new ObjectOutputStream(s.getOutputStream());

            oos.writeInt(id);
            oos.flush();
            System.out.println("send id");

            oos.writeObject(image);
            oos.flush();
            System.out.println("send post");

            //oos.close();

            ois = new ObjectInputStream(s.getInputStream());

            newImages = (List<byte[]>) ois.readObject();
            Log.d("updatePosts", "received byte images");

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
        return newImages;
    }
}
