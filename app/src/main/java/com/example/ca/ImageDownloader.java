package com.example.ca;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ImageDownloader extends AsyncTask <String,Integer, Bitmap> {

    private MainActivity parent ;
    private String urlPath ;
    private ProgressBar progressBar ;
    private TextView progressText;
    private int loop = 0;
    private int updateImageCount = 0;
    private ArrayList<String> imgPaths = null;
    public ImageDownloader(MainActivity mainActivity , String urlPath, ArrayList<String> imgPaths){
        this.parent = mainActivity;
        this.urlPath = urlPath;
        this.imgPaths = imgPaths;
        progressBar = parent.findViewById(R.id.progressBar);
        progressText = parent.findViewById(R.id.progressText);
    }

    @Override
    protected Bitmap doInBackground(String... strings) {

        while (loop < 20 && !isCancelled()){

            long totalDownloaded = 0;
            int readLen = 0;
            String localTo = parent.getFilesDir().toString()+"/" +loop+".jpg";

            try {
                URL url = new URL(imgPaths.get(loop));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();


                byte [] data = new byte[1024];
                InputStream in = url.openStream();
                BufferedInputStream bufIn = new BufferedInputStream(in, 2048);
                OutputStream out = new FileOutputStream(localTo);

                while ((readLen = bufIn.read(data)) != -1){
                    totalDownloaded += readLen;
                    out.write(data, 0, readLen);
                }


                Log.d("ABC","FINISHED DOWNLOADING "+loop);
                publishProgress(loop);
                loop++;
            }catch (Exception e){
                //Toast.makeText(parent,e.getMessage(),Toast.LENGTH_SHORT).show();
                Log.d("ABC","INTERRUPTED");
                Log.d("ABC",e.getMessage());
                Log.d("ABC",String.valueOf(isCancelled()));
                e.printStackTrace();
                break;
            }
        }


        return null;
    }




    @Override
    protected void onProgressUpdate(Integer... values) {
        String localTo = parent.getFilesDir().toString()+"/" +updateImageCount+".jpg";
        Log.d("CREATION","Progress UPDATE" + updateImageCount);
        File file = new File(localTo);
        try {
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 4;
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            //BitmapDrawable bdrawable = new BitmapDrawable(parent.getResources(),bitmap);
            int id = parent.getResources().getIdentifier(String.valueOf(updateImageCount),"id","com.example.ca");
            ImageView img = parent.findViewById(id);
            img.setImageBitmap(bitmap);
            progressBar.setProgress(Math.round(100*(updateImageCount+1)/20));
            progressText.setText("Downloading "+(updateImageCount+1)+" of 20 Images");
            Log.d("CREATION",updateImageCount+" btn update");
            if (updateImageCount+1 == 20){
                progressText.setText("Completely Downloaded.");
            }
            updateImageCount++;
        }catch (Exception e){
            Log.d("ERROR",e.getMessage());
        }
    }
}
