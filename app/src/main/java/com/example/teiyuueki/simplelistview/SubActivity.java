package com.example.teiyuueki.simplelistview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by teiyuueki on 16/04/01.
 */


public class SubActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Intent intent = getIntent();
        String imagePath = intent.getStringExtra("imagePath");
        ImageView imageView = (ImageView) findViewById(R.id.selectedImage);
        Bitmap bmp = BitmapFactory.decodeFile(imagePath);
        imageView.setImageBitmap(bmp);
    }

}
