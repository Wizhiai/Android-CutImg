package com.example.hulijie.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    String imgUrl = "http://c.hiphotos.baidu.com/baike/c0%3Dbaike92%2C5%2C5%2C92%2C30/sign=ac7d7e6eb9389b502cf2e800e45c8eb8/43a7d933c895d14337e7805373f082025aaf0728.jpg";
    Bitmap bmImg;
    Bitmap bitmap2;
    ImageView imageView;
    Button cutImgBtn;
    Uri uri1;
    private File tempFile;
    Uri uri2;

    //    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imgView);

        new LoadAndSaveImage(getApplicationContext(), imgUrl, imageView);
        testOpen();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//这个方法没执行,要改进
                testOpen();
                Toast.makeText(MainActivity.this, "**3*", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            //相册
            case 98:
                imageCut(data.getData());
                break;
            // 取得裁剪后的图片
            case 100:
                if (data != null) {
                    displayImage(data);
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void imageCut(Uri uri) {
        Log.d("zhu","imageCut(Uri uri)==="+uri.toString());
        Toast.makeText(getApplicationContext(), uri.toString(), Toast.LENGTH_SHORT).show();
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
        startActivityForResult(intent, 100);
        Toast.makeText(MainActivity.this, "imageCut", Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示裁剪后的图片
     */
    private void displayImage(Intent picdata) {
        Toast.makeText(MainActivity.this, "displayImage", Toast.LENGTH_SHORT).show();
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            imageView.setImageBitmap(photo);
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
//        new LoadAndSaveImage().release();
    }


    class Load_and_save_image extends AsyncTask<String, Void, Void> {

        public Load_and_save_image(Context applicationContext, String string,
                                   ImageView image) {
            // TODO Auto-generated constructor stub
        }

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
            imageView.setImageDrawable(d);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    testOpen();
                }
            });
        };
    }

    public void testOpen() {
        imageCut(Uri.parse("file://"
                + Environment.getExternalStorageDirectory()
                .toString() + "/download_image.jpg"));
        Log.d("zhu","uri="+Uri.parse("file://"
                + Environment.getExternalStorageDirectory()
                .toString() + "/download_image.jpg").toString());
        Log.d("zhu", "file===" + ("file://"
                + Environment.getExternalStorageDirectory()
                .toString() + "/download_image.jpg").toString());
    }
}
