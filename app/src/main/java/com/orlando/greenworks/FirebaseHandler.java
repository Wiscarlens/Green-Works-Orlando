package com.orlando.greenworks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

/*
 * This is a collaborative effort by the following team members:
 * Team members:
 * - Wiscarlens Lucius (Team Leader)
 * - Amanpreet Singh
 * - Alexandra Perez
 * - Eric Klausner
 * - Jordan Kinlocke
 * */

public class FirebaseHandler {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static final FirebaseStorage storage = FirebaseStorage.getInstance();
    private OnUserCreatedListener listener;

    public FirebaseHandler() {
    }

    public FirebaseHandler(OnUserCreatedListener listener) {
        this.listener = listener;
    }

    public static String getCurrentUserOnlineID(FirebaseAuth mAuth) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;
        return firebaseUser.getUid();
    }

    public void createUser(User newUser, Context context){
        // TODO: check if the email exists in the database

        mAuth.createUserWithEmailAndPassword(newUser.getEmail(), newUser.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        try {
                            String globalID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
//                            newUser.setUserID(globalID);


//                            if (profileImage != null){
//                                newUser.setProfileImagePath(globalID);
//                                uploadFile(profileImage, "Profiles/" + globalID);
//                            }

                            newUser.setEmail(null);
                            newUser.setPassword(null);

                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference();
                            currentUserDb.child("users").child(globalID).setValue(newUser);

                            Log.i("Firebase", "User Added Successfully!");

                        } catch (Exception e) {
                            Log.e("Firebase", "Error while adding user to online database", e);
                        }

                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Firebase", "createUserWithEmail:success");
                        Toast.makeText(context, "Account created!", Toast.LENGTH_SHORT).show();

                        removeFragment(); // Remove Fragment from stack
                        listener.onUserCreated(); // Notify user created

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Firebase", "createUserWithEmail:failure", task.getException());
                        // Inside your Fragment class
                        Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    public static void uploadFile(Drawable image, String imagePath) {
        Log.d("FirebaseHandler", "uploadFile: " + imagePath);
        Log.d("FirebaseHandler", "uploadFile: " + image);

        Bitmap bitmap = Utils.drawableToBitmap(image);

        // Compress the Bitmap into a ByteArrayOutputStream with 25% quality
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 25, baos);

        // Convert the ByteArrayOutputStream to a byte array
        byte[] imageData = baos.toByteArray();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference(imagePath);

        UploadTask uploadTask = storageReference.putBytes(imageData);
        uploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
            Log.e("FirebaseHandler", "Error while uploading image to Firebase Storage", exception);
        }).addOnSuccessListener(taskSnapshot -> Log.i("FirebaseHandler", "Image uploaded successfully"));
    }

    private void removeFragment() {
        if (listener != null) {
            if (listener instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) listener;
                RegistrationFragment registrationFragment = (RegistrationFragment) mainActivity.getSupportFragmentManager().findFragmentByTag("RegistrationFragment");
                if (registrationFragment != null) {
                    mainActivity.getSupportFragmentManager().beginTransaction().remove(registrationFragment).commit();
                }
            }
        }
    }

    public interface OnUserCreatedListener {
        void onUserCreated();
    }


//    public static void readItem(String tableName, Context context){
//        DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference(tableName);
//        firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                try (ItemDatabase itemDatabase = new ItemDatabase(context)) {
//                    // Iterate through Firebase data
//                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
//
//                        Item item = userSnapshot.getValue(Item.class);
//
//                        // Create item in the local database
//                        assert item != null;
//
//                        if (Objects.equals(MainActivity.currentUser.getCreatorID(), item.getCreatorID())){
//                            itemDatabase.createItem(item);
//
//                            if (item.getItemImagePath() != null){
//                                downloadAndSaveImagesLocally("Items", item.getImagePath(), context);
//                            }
//
//                        }
//
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Log.e("FirebaseItemDatabase", "Failed to sync item data from Firebase: " + e.getMessage());
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("UserDatabase", "Firebase data fetch cancelled: " + error.getMessage());
//
//            }
//        });
//
//    }


    // Synchronize user data from Firebase to SQLite
//    public static void readUser(String tableName, Context context){
//        DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference(tableName);
//
//        // Fetch data from Firebase
//        firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                try (UserDatabase userDatabase = new UserDatabase(context)) {
//                    // Iterate through Firebase data
//                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
//                        User firebaseUser = userSnapshot.getValue(User.class);
//
//                        // Create user in the local database
//                        assert firebaseUser != null;
//
//                        // Do not show current user in the list
//                        if (Objects.equals(firebaseUser.getGlobalID(), MainActivity.currentUser.getGlobalID())) {
//                            Log.i("FirebaseUserDatabase", "Skipping current user: " + firebaseUser.getFullName());
//                        } else {
//                            if (Objects.equals(MainActivity.currentUser.getCreatorID(), firebaseUser.getCreatorID())){
//                                userDatabase.createUser(firebaseUser);
//
//                                if (firebaseUser.getProfileImagePath() != null){
//                                    downloadAndSaveImagesLocally("Profiles", firebaseUser.getProfileImagePath(), context);
//                                }
//
//                                Log.i("FirebaseUserDatabase", "User with email " + firebaseUser.getEmail() + " added to SQLite.");
//                            }
//                        }
//
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Log.e("FirebaseUserDatabase", "Failed to sync user data from Firebase: " + e.getMessage());
//                }
//            }
//
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e("UserDatabase", "Firebase data fetch cancelled: " + databaseError.getMessage());
//            }
//
//
//        });
//    }

//    public static void downloadAndSaveImagesLocally(String folderName, String fileName, Context context) {
//        final long FIVE_MEGABYTES = 1024 * 1024 * 5;
//
//        String imagePath = folderName + "/" + fileName;
//        StorageReference imageRef = storage.getReference(imagePath);
//
//        try {
//            imageRef.getBytes(FIVE_MEGABYTES).addOnSuccessListener(bytes -> {
//                Drawable image = Utils.byteArrayToDrawable(bytes, context.getResources());
//
//                FileManager.saveImageLocally(context, image, folderName, fileName);
//            }).addOnFailureListener(exception -> {
//                if (exception instanceof StorageException) {
//                    int errorCode = ((StorageException) exception).getErrorCode();
//                    if (errorCode == StorageException.ERROR_OBJECT_NOT_FOUND || errorCode == StorageException.ERROR_RETRY_LIMIT_EXCEEDED) {
//                        // If the error is due to object not found or retry limit exceeded, retry after 5 seconds
//                        new Handler().postDelayed(() -> downloadAndSaveImagesLocally(folderName, fileName, context), 5000);
//                    }
//                } else {
//                    Log.e("Firebase", "Error downloading item image", exception);
//                }
//            });
//
//        } catch (IndexOutOfBoundsException e){
//            Log.e("Firebase", "The maximum allowed buffer size was exceeded.", e);
//        }
//    }
//
//    public static void createItem(Item newItem, Drawable itemImage){
//        DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference("items");
//
//        // Use push to generate a unique key
//        DatabaseReference newItemRef = itemsRef.push();
//
//        String globalID = newItemRef.getKey(); // Get get item global ID
//
//        newItem.setGlobalID(globalID);
//
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (currentUser != null) {
//            // Set the creatorID of the item to the uid of the currently authenticated user
//            newItem.setCreatorID(currentUser.getUid());
//
//            if(itemImage != null){
//                newItem.setImagePath(globalID);
//                Log.d("FirebaseHandler", "createItem: " + itemImage);
//            } else {
//                newItem.setImagePath(null);
//                Log.d("FirebaseHandler", "createItem: " + "No image");
//            }
//
//            // Set the item with the generated key
//            newItemRef.setValue(newItem).addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    if (itemImage != null){
//                        // Save the image to Firebase Storage with the generated key
//                        uploadFile(itemImage, "Items/" + globalID);
//                    }
//
//                    Log.i("Firebase", "Item Added Successfully!");
//                }
//            }).addOnFailureListener(e ->
//                    Log.d("Firebase", "createUserWithEmail:failure")
//            );
//        } else {
//            // User is not authenticated
//            Log.w("Firebase", "User is not authenticated. Cannot create item.");
//        }
//    }

}
