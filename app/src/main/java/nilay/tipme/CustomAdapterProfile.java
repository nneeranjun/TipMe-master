package nilay.tipme;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nneeranjun on 8/10/16.
 */
public class CustomAdapterProfile extends BaseAdapter {
    JSONObject profileInfo;
    Context context;
    int[] icons = {R.drawable.viewprofilename, R.drawable.profileemail,R.drawable.profilephone, R.drawable.profileaddress};
    ImageView icon;
    EditText content;

    public CustomAdapterProfile(Context context, JSONObject profileInfo) {
        this.context = context;
        this.profileInfo = profileInfo;
    }
    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int i) {
        return profileInfo;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View getView = layoutInflater.inflate(R.layout.custom_profile_info,viewGroup, false);
        Typeface avenir = Typeface.createFromAsset(context.getAssets(),"avenir.otf");


        content = (EditText) getView.findViewById(R.id.content);

        icon = (ImageView) getView.findViewById(R.id.icon);
        content.setEnabled(false);
        content.setTypeface(avenir);

        switch (i){
            case 0:
                try {

                    content.setText(profileInfo.getString("First_Name")+" "+profileInfo.getString("Last_Name"));

                    icon.setImageResource(icons[i]);
                } catch (JSONException e) {

                }
                break;
            case 1:
                try {
                    content.setText(profileInfo.getString("Email_Address"));
                    icon.setImageResource(icons[i]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    content.setText(profileInfo.getString("Phone_Number"));
                    icon.setImageResource(icons[i]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    content.setText(profileInfo.getString("Street_Address")+ " " +profileInfo.getString("City")+" "+profileInfo.getString("State")+" " +profileInfo.getString("ZipCode"));
                    icon.setImageResource(icons[i]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

        }

return getView;

    }
}
