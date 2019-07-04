package com.example.ca;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private EditText editText = null;
    private String url = "https://stocksnap.io/";
    private TextView textView = null;
    private ArrayList<String> urls = null;
    private RequestTask requestTask = null;
    private ImageDownloader imgDownloader = null;
    private ImageView [] imViews = new ImageView[20];
    private HashMap<Integer,Integer> choice = new HashMap<Integer,Integer>();
    private ProgressBar progressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout imgLayout = findViewById(R.id.imgLayout);

        int btnLabel = 0;

        for (int i = 1 ; i <= 4 ; i++){
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setId(i+100);
            layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,1.0f));
            for (int j = 1; j <= 5 ; j++){
                ImageView img = new ImageView(this);
                img.setId(btnLabel);
                //btn.setText(String.valueOf(j));
                //img.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,1.0f));
                img.setImageResource(R.drawable.default_img);
                img.setOnClickListener(this);
                img.setScaleType(ImageView.ScaleType.FIT_XY);
                imViews[btnLabel] = img;
                layout.addView(img,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,1.0f));
                btnLabel++;
            }

            imgLayout.addView(layout);
        }

        editText = findViewById(R.id.editText);
        progressBar = findViewById(R.id.progressBar);
        Button fetchBtn = findViewById(R.id.fetch_btn);
        fetchBtn.setOnClickListener(this);
        textView =  findViewById(R.id.progressText);
        textView.setText("Press fetch to download");
        requestTask = new RequestTask(this);
//        try{
//            File file = new File("data/data/com.example.ca/images/2.jpg");
//            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
//            Button btn = findViewById(5);
//            BitmapDrawable bdrawable = new BitmapDrawable(getResources(), bitmap);
//            btn.setBackground(bdrawable);
//        }catch (Exception e){
//
//        }




    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fetch_btn){
            url = editText.getText().toString();
            Log.d("ABC",url);

            textView.setText("Downloading 0 of 20 images");
            progressBar.setProgress(0);
            AsyncTask.Status taskStatus = requestTask.getStatus();
            if (requestTask != null && taskStatus != AsyncTask.Status.RUNNING){
                requestTask = new RequestTask(this);

                Log.d("STATUS","NOT RUNNING");
            }else if (taskStatus == AsyncTask.Status.PENDING || taskStatus == AsyncTask.Status.RUNNING ){
                requestTask.cancel(true);
                requestTask = new RequestTask(this);
            }

            if (imgDownloader != null && imgDownloader.getStatus() == AsyncTask.Status.RUNNING){
                imgDownloader.cancel(true);
                imgDownloader = null;
                Log.d ("ABC","not working to running");
                //imgDownloader = new ImageDownloader(this,url,urls);
                Log.d ("ABC",url);
            }

            clearImgViews();
            requestTask.execute(url);


        } else {
            if (imgDownloader != null && imgDownloader.getStatus() == AsyncTask.Status.FINISHED){
                chooseImage(v);

            }
        }

    }


    public void setUrls(ArrayList<String> urls){
        this.urls = urls;
    }


    public void downloadImageTask(String url){
       if (imgDownloader == null){
           imgDownloader = new ImageDownloader(this,url,urls);
       }

       if (imgDownloader.getStatus() == AsyncTask.Status.FINISHED){
           imgDownloader = new ImageDownloader(this,url,urls);
       }
       choice.clear();
       imgDownloader.execute();

    }

    private void clearImgViews(){
        for (ImageView im : imViews){
            im.setImageResource(R.drawable.default_img);
            im.setAlpha(1f);
        }
    }

    private void chooseImage(View v){
        Log.d("COUNT",String.valueOf(choice.size()));
        if (choice.get(v.getId()) == null){
            choice.put(v.getId(), v.getId());
            v.setAlpha(0.5f);
        }

        if (choice.size() == 6){
            Log.d("COUNT", "GO TO NEXT PAGE");
            for (Integer a : choice.keySet()){
                Log.d("CHOICE",String.valueOf(a));
            }

            Integer[] keys = choice.keySet().toArray(new Integer[choice.size()]);
            int [] passedArray = new int[keys.length];

            for (int i = 0 ; i < keys.length ; i++){
                passedArray[i] = keys[i];
            }


            Intent intent = new Intent(this,SecondActivity.class);
            intent.putExtra("choices",passedArray);
            startActivity(intent);
        }
    }
}

