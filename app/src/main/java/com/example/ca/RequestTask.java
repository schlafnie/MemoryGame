package com.example.ca;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestTask extends AsyncTask<String, String, String> {

    private MainActivity main = null;


    public RequestTask(MainActivity main){
        this.main = main;
    }

    @Override
    protected String doInBackground(String... page) {
        String line = "";
        try {
            URL url = new URL(page[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty ( "User-Agent", "Mozilla" );
            conn.connect();

            StringBuilder total = new StringBuilder();

            InputStream is = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            while((line = reader.readLine()) != null && !isCancelled()) {
                total.append(line);
            }
            line = total.toString();

        } catch (Exception e) {

        }


        String imgRegex = "(?i)<img[^>]+?src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
        Pattern p = Pattern.compile(imgRegex);
        Matcher m = p.matcher(line);
        ArrayList<String> imagePaths = new ArrayList<String>();

        while (m.find() && imagePaths.size() < 20 && !isCancelled()) {
            String imgSrc = m.group(1);
            if (imgSrc.toLowerCase().contains(".jpg")){
                imagePaths.add(imgSrc);
            }

        }
        if (!isCancelled()){
            main.setUrls(imagePaths);
            main.downloadImageTask("abc");
        }


        return line;
    }

    @Override
    protected void onPostExecute(String result) {
//        String imgRegex = "(?i)<img[^>]+?src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
//        Pattern p = Pattern.compile(imgRegex);
//        Matcher m = p.matcher(result);
//        ArrayList<String> imagePaths = new ArrayList<String>();
//
//        while (m.find() && imagePaths.size() < 20) {
//            String imgSrc = m.group(1);
//            if (imgSrc.toLowerCase().contains(".jpg")){
//                imagePaths.add(imgSrc);
//            }
//
//        }
//
//        main.setUrls(imagePaths);
//

    }

}