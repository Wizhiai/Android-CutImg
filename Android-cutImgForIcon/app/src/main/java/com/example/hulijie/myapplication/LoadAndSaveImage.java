package com.example.hulijie.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadAndSaveImage {
    ImageView view;
    Context mContext;

    public LoadAndSaveImage() {
        // TODO Auto-generated constructor stub
    }

    public LoadAndSaveImage(Context context, String str, ImageView view) {
        // TODO Auto-generated constructor stub
        this.view = view;
        mContext = context;
        new Load_and_save_image().execute(str);
    }

    void release() {
        File file = new File("file://"
                + Environment.getExternalStorageDirectory().toString()
                + "/download_image.jpg");
        if (file.exists()) {
            boolean deleted = file.delete();
        }
    }

    class Load_and_save_image extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... sUrl) {
            // TODO Auto-generated method stub
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // download the file
                input = connection.getInputStream();
                File checker = new File("file://"
                        + Environment.getExternalStorageDirectory().toString()
                        + "/download_image.jpg");
                if (checker.exists()) {
                    boolean deleted = checker.delete();
                }
                output = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
                        + "/download_image.jpg");

                byte data[] = new byte[4096];
                int count;
                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
            } finally {
                try {
                    if (input != null)
                        input.close();
                    if (output != null) {
                        output.close();
                    }
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            Drawable d = Drawable.createFromPath(Environment
                    .getExternalStorageDirectory().toString()
                    + "/download_image.jpg");
            view.setImageDrawable(d);

        }


    }

    public void imageCut(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //开启裁剪功能
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        intent.putExtra("return-data", true);
        if (intent != null) {
            displayImage(intent);
        }
    }

    /**
     * 显示裁剪后的图片
     */
    private void displayImage(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            view.setImageBitmap(photo);
        }
    }
}