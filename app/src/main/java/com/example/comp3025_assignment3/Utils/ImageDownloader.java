package com.example.comp3025_assignment3.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDownloader {
    // Since Android does not allow ImageViews images to be a URL, it is necessary to convert the image to a Bitmap, which can then be set to the ImageView.

    // Static method to avoid creating an instance of the class.
    public static void loadImageFromUrl(ImageView imageView, String imageUrl) {
        // Create a new thread, as to not block the main thread.
        new Thread(() -> {
            // Try to download the image from the URL.
            try {
                // Create a new URL object with the image URL.
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                // Get the input stream from the connection.
                InputStream input = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);

                // Set the image on the main thread.
                new Handler(Looper.getMainLooper()).post(() -> {
                    imageView.setImageBitmap(bitmap);
                });
            } catch (Exception e) {
                Log.e("ImageDownloader", "Error loading image: " + e.getMessage());
                // Since there is a default image in the XML, this exception can be ignored.
            }
        }).start(); // Start the thread
    }
}