package com.example.finalproject;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PhotoPreview extends AppCompatActivity
{
    private static final int REQUEST_FOR_LOCATION = 123 ;

    public static class Post
    {
        public String uid;
        public String url;
        public Object timestamp;
        public String description;
        public int likeCount = 0;
        public String category;
        public float confidence;
        public Map<String, Boolean> likes = new HashMap<>();
        public Post(String uid, String url, String description, String category, float confidence)
        {
            this.uid=uid;
            this.url=url;
            this.description=description;
            this.category=category;
            this.confidence=confidence;
            this.timestamp= ServerValue.TIMESTAMP; }
        public Object getTimestamp(){
            return timestamp;
        }
        public Post() {

        }

    }
    Uri uri;
    EditText description;
    String category;
    float confidence;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);
        uri= Uri.parse(getIntent().getStringExtra("uri"));
        ImageView imageView=findViewById(R.id.previewImage);
        imageView.setImageURI(uri);
        description=findViewById(R.id.description);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_FOR_LOCATION);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_FOR_LOCATION && ((grantResults.length>0 && grantResults[0]!=PackageManager.PERMISSION_GRANTED) || (grantResults.length>1 && grantResults[1]!=PackageManager.PERMISSION_GRANTED))){
            Toast.makeText(this, "We need to access your location", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage() throws InterruptedException {
        FirebaseStorage storage= FirebaseStorage.getInstance();
        final String fileNameInStorage= UUID.randomUUID().toString(); String path="images/"+ fileNameInStorage+".jpg";
        final StorageReference imageRef=storage.getReference(path);
        category = classifyTry(this, uri);
        confidence = confTry(this, uri);
        imageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override public void onSuccess(final Uri uri) {
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference postsRef = database.getReference("Posts");
                        DatabaseReference newPostRef = postsRef.push();
                        newPostRef.setValue(new Post(currentUser.getUid(),uri.toString(),description.getText().toString(), category, confidence))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override public void onSuccess(Void aVoid) {
                                        Toast.makeText(PhotoPreview.this, "Success", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PhotoPreview.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override public void onFailure(@NonNull Exception e) {
                Toast.makeText(PhotoPreview.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public String classifyTry(Context context, final Uri uri) {
        InputImage image = null;
        ImageLabeler labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);
        try {
            image = InputImage.fromFilePath(context, uri);
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        labeler.process(image)
                .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                    @Override
                    public void onSuccess(List<ImageLabel> labels) {
                        category = labels.get(0).getText();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...
                    }
                });
        return category;
    }


    public float confTry(Context context, final Uri uri) {
        InputImage image = null;
        ImageLabeler labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);
        try {
            image = InputImage.fromFilePath(context, uri);
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        labeler.process(image)
                .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                    @Override
                    public void onSuccess(List<ImageLabel> labels) {
                        confidence = labels.get(0).getConfidence();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...
                    }
                });
        return confidence;
    }



    public void Publish(View view) throws InterruptedException {
        uploadImage();
        finish();
    }
}
