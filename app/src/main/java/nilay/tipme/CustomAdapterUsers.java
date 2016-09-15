package nilay.tipme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nneeranjun on 7/17/16.
 */
public class CustomAdapterUsers extends BaseAdapter implements Filterable {
JSONArray users;
    ImageView profilePicture;
    TextView name;
    TextView email;
    Context context;
    Typeface avenir;
    Typeface gotham;
    List<User> userList = new ArrayList<>();
     List<User> filteredUsers = new ArrayList<>();
    public CustomAdapterUsers(Context context, JSONArray jsonArray){
            this.context = context;
        users = jsonArray;
        for(int i=0;i<users.length();i++){
            try{
                userList.add(new User(jsonArray.getJSONObject(i).getString("first_name"),jsonArray.getJSONObject(i).getString("last_name"),jsonArray.getJSONObject(i).getString("email_address")));

            }catch (Exception e){

            }
        }

    }

    @Override
    public int getCount() {
        return users.length();
    }

    @Override
    public User getItem(int i) {
        if(i<filteredUsers.size()) {
            return filteredUsers.get(i);
        }
        else {
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View rootView = layoutInflater.inflate(R.layout.custom_search_user,viewGroup, false);
        if(i<filteredUsers.size()) {
            User user = filteredUsers.get(i);
            name = (TextView) rootView.findViewById(R.id.name);
            email = (TextView) rootView.findViewById(R.id.searchEmail);
            avenir = Typeface.createFromAsset(context.getAssets(), "avenir.otf");
            gotham = Typeface.createFromAsset(context.getAssets(), "gotham.ttf");
            name.setTypeface(avenir);
            email.setTypeface(gotham);
            name.setText(user.getFirstName() + " " + user.getLastName());
            email.setText(user.getEmailAddress());
        }


        return rootView;
    }

    public Filter getFilter(){
        return new UserFilter(this, userList);
    }


    class SelectUserListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(context,MakePayment.class);
            intent.putExtra("user",filteredUsers.get(i));
            context.startActivity(intent);
        }
    }
}
