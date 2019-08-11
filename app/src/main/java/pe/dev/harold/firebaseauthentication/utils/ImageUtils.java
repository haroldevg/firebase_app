package pe.dev.harold.firebaseauthentication.utils;

import android.os.StrictMode;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageUtils {
    public static byte[] imageto64(String imageUrl) throws IOException {
        byte[] imageRaw = null;
        URL url = new URL(imageUrl);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        HttpURLConnection
                urlConnection = (HttpURLConnection) url
                .openConnection();
        urlConnection.setUseCaches(false);
        urlConnection.connect();
        if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
        {
            try
            {
                InputStream in = new BufferedInputStream(
                        urlConnection.getInputStream());
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int c;
                while ((c = in.read()) != -1)
                {
                    out.write(c);
                }
                out.flush();

                imageRaw = out.toByteArray();

                urlConnection.disconnect();
                in.close();
                out.close();
            } catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return imageRaw;
        }
        return null;
    }
}
