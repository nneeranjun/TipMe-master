package nilay.tipme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;

import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class SignUp extends AppCompatActivity {
    EditText editText;
    EditText editText1;
    EditText editText2;
    EditText editText3;
    EditText editText4;
    EditText editText5;
    EditText editText6;
    EditText emailText;
    EditText passText;
    TextView imageView;
    ImageView profilePicture;
    Button registerButton;
 AlertDialog alertDialog;
    private Uri outputFileUri;
    SharedPreferences sharedPreferences;
    Uri selectedImageUri;
     File sdImageMainDirectory;
    FirebaseStorage storage;
    StorageReference imagesRef;
    StorageReference storageRef;
    private final int SELECT_PICTURE_CODE = 1231;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Typeface avenir = Typeface.createFromAsset(getAssets(),"avenir.otf");
        Typeface gotham = Typeface.createFromAsset(getAssets(),"gotham.ttf");
         editText = (EditText)findViewById(R.id.firstName);
         editText2 = (EditText)findViewById(R.id.lastName);
         editText3= (EditText)findViewById(R.id.phoneNumber);
         editText4 = (EditText)findViewById(R.id.zipCode);
         emailText = (EditText)findViewById(R.id.emailRegister);
         editText1 = (EditText)findViewById(R.id.streetAddress);
         imageView = (TextView)findViewById(R.id.btnLinkToLoginScreen);
         passText = (EditText)findViewById(R.id.passwordRegister);
         editText5 = (EditText)findViewById(R.id.city);
         editText6 = (EditText)findViewById(R.id.state);
        profilePicture = (ImageView)findViewById(R.id.profile_image);
        registerButton =(Button) findViewById(R.id.registerButton);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://tip-me-f11b7.appspot.com/");




        editText.setTypeface(avenir);
        editText2.setTypeface(avenir);
        editText3.setTypeface(avenir);
        editText4.setTypeface(avenir);
        editText5.setTypeface(avenir);
        editText6.setTypeface(avenir);
        editText1.setTypeface(avenir);
        imageView.setTypeface(avenir);
        passText.setTypeface(avenir);
        registerButton.setTypeface(gotham);


    }
    public void next(View view) {

        String firstName = editText.getText().toString();

        String lastName = editText2.getText().toString();

        String phoneNumber = editText3.getText().toString();

        String zipCode = editText4.getText().toString();

        final String email = emailText.getText().toString();


        String password = passText.getText().toString();


        String streetAddress = editText1.getText().toString();
        String city = editText5.getText().toString();
        String state = editText6.getText().toString();
        AsyncHttpClient client = new AsyncHttpClient();

        if (firstName.equals("") || lastName.equals("") || phoneNumber.equals("") || password.equals("") || streetAddress.equals("") || city.equals("")
                || city.equals("") || state.equals("")) {
            if (!isValidEmail(email)) {


                Toast.makeText(getApplicationContext(),"Invalid email",Toast.LENGTH_LONG).show();
            } else if (!isValidCellPhone(phoneNumber)) {
                Toast.makeText(getApplicationContext(),"Invalid phone number",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),"Invalid information",Toast.LENGTH_LONG).show();
            }
        } else {

            if(sdImageMainDirectory==null){
                try
                {
                   sdImageMainDirectory=new File("blank_profile.jpg");
                    InputStream inputStream = getResources().openRawResource(+ R.drawable.blankprofile);
                    OutputStream out=new FileOutputStream(sdImageMainDirectory);
                    byte buf[]=new byte[1024];
                    int len;
                    while((len=inputStream.read(buf))>0)
                        out.write(buf,0,len);
                    out.close();
                    inputStream.close();
                }
                catch (IOException e){}
            }
        }




            final RequestParams params = new RequestParams();
            params.put("First_Name", firstName);
            params.put("Phone_Number", phoneNumber);
            params.put("Street_Address", streetAddress);
            params.put("City", city);
            params.put("State", state);
            params.put("ZipCode", zipCode);
            params.put("Last_Name", lastName);
            params.put("Email_Address", email);
            params.put("Password", password);


            client.post("http://54.149.252.188/TipM/Signup.php", params, new AsyncHttpResponseHandler() {
                ProgressDialog progressDialog;


                @Override
                public void onStart() {
                    progressDialog = new ProgressDialog(SignUp.this);
                    progressDialog.setTitle("Please wait");
                    progressDialog.setMessage("Registering...");
                    progressDialog.show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", email);
                    editor.commit();
            StorageReference  imagesRef = storageRef.child(sharedPreferences.getString("email",""));
                   Bitmap image = ((BitmapDrawable) profilePicture.getDrawable()).getBitmap();
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

                    progressDialog.dismiss();
                    alertDialog = new AlertDialog.Builder(SignUp.this).create();
                    alertDialog.setMessage("Would you like to enter your bank account information in order to send tips?");
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getApplicationContext(), MerchantAccount.class));
                            finish();
                        }
                    });
                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getApplicationContext(), Tabs.class));
                            finish();
                        }
                    });
                    alertDialog.show();

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    progressDialog.dismiss();


                }

            });
        }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public boolean isValidCellPhone(String number)
    {
        return android.util.Patterns.PHONE.matcher(number).matches();
    }

    public void changePicture(View view){
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
        root.mkdirs();
        final String fname = "img_"+ System.currentTimeMillis() + ".jpg";
        sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            if (resultCode == RESULT_OK) {
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
                        profilePicture.setImageURI(selectedImageUri);
                    } else {
                        selectedImageUri = data == null ? null : data.getData();
                        profilePicture.setImageURI(selectedImageUri);
                    }
                }
            }
    }
    public void backToLogin(View view){
        finish();
    }
}
