package com.example.finalproject;


        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.util.Log;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.URL;

public class GetHttp {
    // Given a string representation of a URL, sets up a connection and gets
    // an input stream.
    public static InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }
    public static Bitmap downloadBMP(String urlString) throws IOException {
        Bitmap image = null;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            image = BitmapFactory.decodeStream(connection.getInputStream());
        }
        return image;
    }

    public static double getJSONDouble(String strUrl, String strAttr) throws IOException, JSONException {
        //Start of JSON reading of UV factor:
        //create the network connection:
        URL UVurl = new URL(strUrl);
        HttpURLConnection UVConnection = (HttpURLConnection) UVurl.openConnection();
        InputStream inStream = UVConnection.getInputStream();

        //create a JSON object from the response
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
        StringBuilder sb = new StringBuilder();

        String line = null;
        while ((line = reader.readLine()) != null)
        {
            sb.append(line + "\n");
        }
        String result = sb.toString();

        //now a JSON table:
        JSONObject jObject = new JSONObject(result);
        double aDouble = jObject.getDouble(strAttr);
        Log.i("UV is:", ""+ aDouble);
        //END of UV rating
        return aDouble;
    }

}
