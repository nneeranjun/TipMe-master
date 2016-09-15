package nilay.tipme;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.net.URL;

import me.pushy.sdk.Pushy;

public class RegisterForPushNotificationsAsync extends AsyncTask<Void, Void, Exception>
{
    Context context;
    public RegisterForPushNotificationsAsync(Context context) {
        this.context=context;
    }

    protected Exception doInBackground(Void... params)
    {
        try
        {
            // Acquire a unique registration ID for this device
            String registrationId = Pushy.register(context);

            // Send the registration ID to your backend server and store it for later
            sendRegistrationIdToBackendServer(registrationId);
        }
        catch( Exception exc )
        {
            // Return exc to onPostExecute
            return exc;
        }

        // We're good
        return null;
    }

    @Override
    protected void onPostExecute(Exception exc)
    {
        // Failed?
        if ( exc != null )
        {
            // Show error as toast message
            Toast.makeText(context, exc.toString(), Toast.LENGTH_LONG).show();
            return;
        }

        // Succeeded, do something to alert the user
    }

    // Example implementation
    void sendRegistrationIdToBackendServer(String registrationId) throws Exception
    {
        // The URL to the function in your backend API that stores registration IDs
        SyncHttpClient syncHttpClient = new SyncHttpClient();
        RequestParams requestParams = new RequestParams();
        requestParams.put("id",registrationId);
        syncHttpClient.post("http://54.149.252.188/TipM/PushyNotification.php", requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

            }
        });
    }
}
