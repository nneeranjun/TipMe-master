package nilay.tipme;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;


import com.google.firebase.storage.UploadTask;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewProfile extends android.support.v4.app.Fragment {

    View rootView;
    ListView listView;
    CustomAdapterProfile customAdapterProfile;
    TextView name;
    TextView tipInfo;
    FirebaseStorage storage;
    StorageReference imagesRef;
    private Uri outputFileUri;
    final int SELECT_PICTURE_CODE = 12334;
    Uri selectedImageUri;
    File sdImageMainDirectory;
ImageView proflePicture;
    File profileFile;
    StorageReference storageRef;

SharedPreferences sharedPreferences;

    public ViewProfile() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
}

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_view_profile,container,false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        proflePicture  =(ImageView) rootView.findViewById(R.id.profile_image);
        listView = (ListView) rootView.findViewById(R.id.profileInfo);
        tipInfo = (TextView) rootView.findViewById(R.id.tipInfo);

        storage = FirebaseStorage.getInstance();
         storageRef = storage.getReferenceFromUrl("gs://tip-me-f11b7.appspot.com/");
        StorageReference picture = storageRef.child(sharedPreferences.getString("email",""));
        final long ONE_MEGABYTE = 1024 * 1024;
        picture.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                proflePicture.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        proflePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePicture();




            }
        });

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        name = (TextView) rootView.findViewById(R.id.name);



        requestParams.put("Email_Address",sharedPreferences.getString("email",""));
        asyncHttpClient.post("http://54.149.252.188/TipM/ViewProfile.php", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
              try {
                  customAdapterProfile = new CustomAdapterProfile(getContext(), response.getJSONObject(0));
                 listView.setAdapter(customAdapterProfile);

                  name.setText(response.getJSONObject(0).getString("First_Name")+" "+response.getJSONObject(0).getString("Last_Name"));
                  tipInfo.setText(response.getJSONObject(0).getString("Payments")+" Tips \u2022 "+response.getJSONObject(0).getString("Receipts")+" Receipts");



              }
              catch (JSONException e){
                  e.printStackTrace();
              }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
        return rootView;


    }




        @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void changePicture(){
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
        root.mkdirs();
        final String fname = "img_"+ System.currentTimeMillis() + ".jpg";
        sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getActivity().getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, SELECT_PICTURE_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == SELECT_PICTURE_CODE) {
                final boolean isCamera;
                if (data == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }


                if (isCamera) {
                    selectedImageUri = outputFileUri;
                    proflePicture.setImageURI(selectedImageUri);
                    imagesRef = storageRef.child(sharedPreferences.getString("email",""));
                    Bitmap image = ((BitmapDrawable) proflePicture.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    StorageMetadata storageMetadata  = new StorageMetadata.Builder().setContentType("image/png").build();
                    UploadTask uploadTask = imagesRef.putBytes(byteArray,storageMetadata);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
                } else {
                    selectedImageUri = data == null ? null : data.getData();
                    proflePicture.setImageURI(selectedImageUri);
                    imagesRef = storageRef.child(sharedPreferences.getString("email",""));
                    Bitmap image = ((BitmapDrawable) proflePicture.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    StorageMetadata storageMetadata  = new StorageMetadata.Builder().setContentType("image/png").build();
                    UploadTask uploadTask = imagesRef.putBytes(byteArray,storageMetadata);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
                }
            }
        }
    }

}
