package com.example.photoapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static android.net.Uri.parse;

public class Client extends AsyncTask<Void, String, List<byte[]>> {
    private CallBackTask callbacktask;
    private byte[] imageData;
    static final int PORT = 8080;

    static Socket s;
    static ObjectInputStream ois;
    static ObjectOutputStream oos;

    static List<Post> posts = new ArrayList<Post>();
    //private String uriString;
    public Client(byte[] images) {
        super();
        imageData = images;
    }

    @Override
    protected List<byte[]> doInBackground(Void... param){
        List<byte[]> newImages = new ArrayList<byte[]>();
        //Log.d("onHandleIntent", "moved in Client");
        //Uri uri = parse(uriString[0]);
        //Log.d("onHandleIntent", "["+uri+"]");
        //try{
            //Bitmap bmp = getBitmapFromUri(uri);

            //bitmap→byte配列(jpg)に変換
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            byte[] imageData = baos.toByteArray();
        newImages = updatePosts(imageData);
//            for (int i=0; i<newImages.size(); i++){
//                Post post = new Post(newImages.get(i));
//                posts.add(post);
//                Log.d("onActivityResult", "add new post");
//            }

//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.d("onActivityResult", "e");
//        }
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

    static List<byte[]> updatePosts(byte[] image) {

        int id = 3;
        List<byte[]> newImages = new ArrayList<byte[]>();

        try {
            Log.d("updatePosts", "start tcp");
            s = new Socket(InetAddress.getByName("192.168.2.106"), PORT);
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
