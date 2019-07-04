package com.example.ca;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener{

    private List<Integer> imageOrders = null;

    private int counts = 0;
    private TextView scoreText = null;
    private TextView timeText = null;
    private ImageView firstImageView = null;
    private ImageView secondImageView = null;
    private int totalSecs = 0;
    private Handler handler = null;
    private Runnable runnable = null;
    private boolean win = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int[] choices = getIntent().getExtras().getIntArray("choices");
        Integer[] finalChoice = new Integer[12];

        for (int i = 0 ; i < choices.length ; i++){
            finalChoice[i] = choices[i];
            finalChoice[i+6] = choices[i];
        }

        imageOrders = Arrays.asList(finalChoice);
        Collections.shuffle(imageOrders);
        Log.d("SECOND","PASSED IMAGE ORDERS");
        setContentView(R.layout.activity_second);

        LinearLayout outerLayout = findViewById(R.id.selectedImgLayout);
        String localTo = getFilesDir().toString()+"/";
        int imgViewCount = 0;
        for (int i = 0 ; i <4 ; i++){
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setId(i+100);
            layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.FILL_PARENT,1.0f));
            for (int j = 0 ; j < 3 ; j++){
                ImageView img = new ImageView(this);
                img.setId(imgViewCount);
                img.setImageResource(R.drawable.default_img);
                img.setScaleType(ImageView.ScaleType.FIT_XY);
                img.setTag(String.valueOf(imageOrders.get(imgViewCount)));
                layout.addView(img,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,1.0f));
                img.setOnClickListener(this);
                Log.d("SECOND",String.valueOf(imgViewCount));
//                File file = new File(localTo+imageOrders.get(imgViewCount)+".jpg");
//                try {
//                    Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
//                    img.setImageBitmap(bitmap);
//                    layout.addView(img,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,1.0f));
//                }catch (Exception e){
//                    Log.d("SECOND","ERROR");
//                }

                imgViewCount++;
            }

            outerLayout.addView(layout);

        }

        scoreText = findViewById(R.id.scoreText);
        timeText  = findViewById(R.id.timeText);
        scoreText.setText("0/6 matches");
        timeText.setText("00:00:00");
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                totalSecs++;
                Log.d("TIMER",String.valueOf(totalSecs));
                int hours = totalSecs/3600;
                int minutes = (totalSecs % 3600)/60;
                int seconds = totalSecs % 60;
                timeText.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
                if (!win){
                    handler.postDelayed(this,1000);
                }
            }
        };
        handler.post(runnable);

    }

    @Override
    public void onClick(View v) {

        if (firstImageView == null){
            firstImageView = findViewById(v.getId());
            showImage(firstImageView);
        }else if (secondImageView == null){
            if (v.getId() != firstImageView.getId()){
                secondImageView = findViewById(v.getId());
                showImage(secondImageView);

                String firstTag = firstImageView.getTag().toString();
                String secondTag = secondImageView.getTag().toString();
                if (firstTag.equals(secondTag)){
                    counts++;
                    scoreText.setText(counts+"/6 matches");
                    firstImageView.setOnClickListener(null);
                    secondImageView.setOnClickListener(null);
                    firstImageView = null;
                    secondImageView = null;
                    if (counts == 6){
                        handler.removeCallbacks(runnable);
                        win = true;
                        Handler handler = new Handler();
                        final Context context = this;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(context,MainActivity.class);
                                context.startActivity(intent);
                            }
                        },2000);
                    }
                }else {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showDefaultImage();
                        }
                    },1000);
                }
            }


        }

    }

    public void showImage (ImageView v){
        try {
            String localTo = getFilesDir().toString()+"/";
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(localTo+v.getTag()+".jpg"));
            v.setImageBitmap(bitmap);
        }catch (Exception e){

        }
    }

    public void showDefaultImage(){
        firstImageView.setImageResource(R.drawable.default_img);
        secondImageView.setImageResource(R.drawable.default_img);
        firstImageView = null;
        secondImageView = null;
    }
}
