package nilay.tipme;

import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nneeranjun on 8/27/16.
 */
public class UserFilter extends Filter {
    CustomAdapterUsers adapterUsers;
    List<User> originalList;
    List<User> filteredList;

    public UserFilter(CustomAdapterUsers adapterUsers, List<User> users) {
        super();
        this.adapterUsers = adapterUsers;
        originalList = users;
        filteredList = new ArrayList<>();

    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        filteredList.clear();
        final FilterResults results = new FilterResults();

        if (charSequence == null || charSequence.length() == 0) {
            filteredList.addAll(originalList);
        } else {
            final String filterPattern = charSequence.toString().toLowerCase().trim();

            // Your filtering logic goes in here
            for (final User user : originalList) {
                if (user.firstName.toLowerCase().startsWith(filterPattern) || user.lastName.toLowerCase().startsWith(filterPattern) || user.emailAddress.toLowerCase().startsWith(filterPattern)) {
                    filteredList.add(user);

                }
            }
        }
        results.values = filteredList;
        results.count = filteredList.size();
        return results;


    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapterUsers.filteredUsers.clear();

            adapterUsers.filteredUsers.addAll((List) filterResults.values);

            adapterUsers.notifyDataSetChanged();

    }
}

