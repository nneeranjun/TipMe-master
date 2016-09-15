package nilay.tipme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.BraintreePaymentActivity;
import com.braintreepayments.api.PaymentRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

public class MakePayment extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String clientToken;

    AlertDialog.Builder builder;
    EditText paymentAmount;
    TextView name;
    TextView poweredBy;
    Button payButton;
    Typeface avenir;
    Typeface gotham;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);
        user = getIntent().getParcelableExtra("user");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        paymentAmount = (EditText)findViewById(R.id.amount);
        name = (TextView) findViewById(R.id.name);
        poweredBy = (TextView) findViewById(R.id.poweredBy);
        avenir = Typeface.createFromAsset(getApplicationContext().getAssets(), "avenir.otf");
        gotham = Typeface.createFromAsset(getApplicationContext().getAssets(), "gotham.ttf");


        name.setText(user.getFirstName()+" "+user.getLastName());
        payButton = (Button) findViewById(R.id.payButton);
        paymentAmount.setTypeface(avenir);
        name.setTypeface(avenir);
        poweredBy.setTypeface(gotham);
        payButton.setTypeface(gotham);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AsyncHttpClient asyncHttpClient1 = new AsyncHttpClient();
                final ProgressDialog progressDialog = new ProgressDialog(getApplication().getApplicationContext());
                progressDialog.setMessage("Please Wait");
                progressDialog.setTitle("Loading");

                final RequestParams requestParams = new RequestParams();
                requestParams.put("Email_Address", sharedPreferences.getString("email",""));
                requestParams.put("Employee_Email", user.emailAddress);

                asyncHttpClient1.post(getApplicationContext(),"http://54.149.252.188/TipM/GetToken.php", requestParams, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        progressDialog.dismiss();




                        clientToken = responseString;
                        PaymentRequest paymentRequest = new PaymentRequest().clientToken(clientToken);
                        startActivityForResult(paymentRequest.getIntent(getApplicationContext()), REQUEST_CODE);
                    }

                });
                // Customization customization = new Customization.CustomizationBuilder().primaryDescription("Tip").secondaryDescription(null).amount("$" + amount.getText().toString()).build();


            }
        });


    }





    int REQUEST_CODE = 100;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentMethodNonce paymentMethodNonce = data.getParcelableExtra(
                        BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE
                );
                String nonce = paymentMethodNonce.getNonce();

                postNonceToServer(nonce);
            }
        }
    }

    public void postNonceToServer(String nonce) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("Amount", paymentAmount.getText().toString());
        params.put("Customer_Email", sharedPreferences.getString("email", " "));
        params.put("Employee_Email", user.getEmailAddress());
        params.put("PaymentMethodNonce", nonce);


        client.post("http://54.149.252.188/TipM/PostPayment.php", params, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {


                switch (responseString) {
                    case "5":
                        builder = new AlertDialog.Builder(getApplicationContext());
                        builder.setTitle("Error");
                        builder.setMessage("Cannot make payment");
                        builder.create();
                        builder.show();
                        break;
                    default:
                        finish();

                }

                return;
            }
        });


    }
}
