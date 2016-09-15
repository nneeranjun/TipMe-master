package nilay.tipme;


import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectUser extends android.support.v4.app.Fragment {
    AutoCompleteTextView searchView;
    ListView listView;
    TextView recent;
    SharedPreferences sharedPreferences;


    View view;

    public SelectUser() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_select_user, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
         searchView = (AutoCompleteTextView) view.findViewById(R.id.searchView);

        recent= (TextView) view.findViewById(R.id.recent);
        Typeface avenir = Typeface.createFromAsset(getActivity().getAssets(),"avenir.otf");
        Typeface gotham = Typeface.createFromAsset(getActivity().getAssets(),"gotham.ttf");
        searchView.setTypeface(avenir);
        recent.setTypeface(avenir);

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();


        asyncHttpClient.post(getActivity().getApplicationContext(),"http://54.149.252.188/TipM/GetAllUsers.php",null,new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                final CustomAdapterUsers customAdapterUsers = new CustomAdapterUsers(getContext(),response);
               searchView.setAdapter(customAdapterUsers);

                searchView.setThreshold(1);
                searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        User user =customAdapterUsers.getItem(i);
                        Intent intent = new Intent(getContext(),MakePayment.class);
                        intent.putExtra("user",user);
                        startActivity(intent);
                        searchView.setText("");
                    }
                });



            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

            }
                });

        return view;
    }
}
