package nilay.tipme;

import android.content.Context;
import android.graphics.Typeface;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nilay on 12/30/2015.
 */

public class CustomAdapterPayments extends BaseAdapter {
    ArrayList<JSONObject> arrayList;
    Context context;
    Typeface avenir;
    Typeface gotham;
    TextView emailView;
    TextView paymentView;
    TextView dateView;
    TextView nameView;
    ImageView profilePic;
    FirebaseStorage storage;
    StorageReference imagesRef;
    StorageReference storageRef;

    public CustomAdapterPayments(Context context, JSONArray jsonArray) {
        super();
        this.context = context;
        arrayList=new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                    arrayList.add(jsonArray.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public JSONObject getItem(int position) {
        JSONObject jsonObject = null;
        jsonObject = arrayList.get(position);
        return jsonObject;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject currentObject;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.payment_custom, parent, false);
        avenir= Typeface.createFromAsset(context.getAssets(), "avenir.otf");
        gotham= Typeface.createFromAsset(context.getAssets(), "gotham.ttf");
        emailView = (TextView) view.findViewById(R.id.email);
        emailView.setTypeface(gotham);
        paymentView = (TextView) view.findViewById(R.id.amount);
        paymentView.setTypeface(avenir);
        dateView = (TextView) view.findViewById(R.id.date);
        dateView.setTypeface(avenir);
        nameView = (TextView) view.findViewById(R.id.name);
        nameView.setTypeface(avenir);
        profilePic = (ImageView) view.findViewById(R.id.profile_image);

        currentObject = getItem(position);
        try {

            emailView.setText(currentObject.getString("Employee_Email"));
            paymentView.setText("$"+currentObject.getString("Payment_Amount"));
            dateView.setText(currentObject.getString("Payment_Date"));
            nameView.setText(currentObject.getString("First_Name")+" "+currentObject.getString("Last_Name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    public void add(JSONArray jSonArray) {
        try {
            for (int i = 0; i < jSonArray.length(); i++) {
                arrayList.add(jSonArray.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addRefresh(JSONArray jsonArray) {
        try {
                for (int i = jsonArray.length() - 1; i >= 0; i--) {
                    arrayList.add(0, jsonArray.getJSONObject(i));

                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}