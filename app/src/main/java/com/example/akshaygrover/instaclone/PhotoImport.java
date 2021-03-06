package com.example.akshaygrover.instaclone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PhotoImport extends AppCompatActivity {

    ImageView imageView;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1){

            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getPhoto();

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_import);

         imageView = (ImageView)findViewById(R.id.imageView2);

        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        else {
            getPhoto();
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==1 && requestCode == RESULT_OK && data != null){

            Uri selectedImage =data.getData();

            try {
                Bitmap  bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);


                Log.i("photo:","recieved");


               // imageView.setImageBitmap(bitmap);

                ByteArrayOutputStream stream =new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                byte[]  byteArray = stream.toByteArray();


                ParseFile file = new ParseFile("image.png",byteArray);

                ParseObject object =new ParseObject("Image");

                object.put("image",file);

                object.put("username", ParseUser.getCurrentUser().getUsername());

                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){

                            Toast.makeText(PhotoImport.this, "Image Shared", Toast.LENGTH_SHORT).show();
                        }
                        else {

                            Toast.makeText(PhotoImport.this, "Image cant be shared", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void getPhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent,1);
    }
}
