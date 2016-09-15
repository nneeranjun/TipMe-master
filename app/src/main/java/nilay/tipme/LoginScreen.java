package nilay.tipme;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.loopj.android.http.*;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.Header;
import me.pushy.sdk.Pushy;


public class LoginScreen extends Activity {
    String token;
    EditText user;
    EditText pass;
    TextView registerLink;
    TextView forgotLink;
    Button btnLogin;
    CheckBox checkBox;
    SharedPreferences sharedPreferences;
    boolean signedIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Pushy.listen(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (!sharedPreferences.getString("email", "").equals("")&&sharedPreferences.getBoolean("signedIn",false)) {
            startActivity(new Intent(getApplicationContext(), Tabs.class));
        } else {
            setContentView(R.layout.activity_login_screen);
            user = (EditText) findViewById(R.id.email);
            pass = (EditText) findViewById(R.id.password);
            registerLink = (TextView) findViewById(R.id.registerLink);
            forgotLink = (TextView) findViewById(R.id.forgotLink);
            btnLogin = (Button) findViewById(R.id.btnLogin);
            checkBox = (CheckBox) findViewById(R.id.signedIn);
            Typeface avenir = Typeface.createFromAsset(getAssets(), "avenir.otf");
            Typeface gotham = Typeface.createFromAsset(getAssets(), "gotham.ttf");
            user.setTypeface(avenir);
            pass.setTypeface(avenir);
            user.setCompoundDrawablePadding(30);
            pass.setCompoundDrawablePadding(30);
            registerLink.setTypeface(avenir);
            forgotLink.setTypeface(avenir);
            btnLogin.setTypeface(gotham);

            // Start IntentService to register this application with GCM.

            final AsyncHttpClient asyncHttpClient1 = new AsyncHttpClient();
            asyncHttpClient1.post("http://54.149.252.188/TipM/GetCompanyInfo.php", new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    String[] info = responseString.split(" ");
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("companyNumber", info[0]);
                    editor.putString("companyEmail", info[1]);
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

                }
            });


        }
    }

    public void login(View view) {
        signedIn = checkBox.isChecked();



        final String email = user.getText().toString();
        final String password = pass.getText().toString();
        if(email.matches("")||password.matches("")) {
            Toast.makeText(this,"Please enter a valid username or password",Toast.LENGTH_SHORT).show();

        }
        else{
            final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();

            requestParams.put("Email_Address",email);
            requestParams.put("Password", password);
            asyncHttpClient.post("http://54.149.252.188/TipM/Login.php", requestParams, new TextHttpResponseHandler() {
                ProgressDialog progressDialog;
                @Override
                public void onStart() {
                     progressDialog = new ProgressDialog(LoginScreen.this);
                    progressDialog.setTitle("Please wait");
                    progressDialog.setMessage("Logging in...");
                    progressDialog.show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    progressDialog.dismiss();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        if(responseString.contains("S")) {
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email", email);
                            editor.putBoolean("signedIn",signedIn);
                            editor.commit();
                            Intent intent =new Intent(getApplicationContext(),Tabs.class);
                            startActivity(intent);
                            progressDialog.dismiss();

                        }
                        else{
                            progressDialog.dismiss();
                           AlertDialog.Builder alertDialog =  new AlertDialog.Builder(LoginScreen.this);
                            alertDialog.setTitle("Login Failure").setMessage("Please enter valid login information");
                            alertDialog.show();
                        }
                }
            });


        }


    }
    public void goToSignUp(View view) {
        Intent intent= new Intent(getApplicationContext(),SignUp.class);
        startActivity(intent);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_screen, menu);
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



}




