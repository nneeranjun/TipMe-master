package nilay.tipme;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.demievil.library.RefreshLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewReceipts extends android.support.v4.app.Fragment {
    RefreshLayout refreshLayout;
    ListView listView;
    View footerLayout;
    JSONArray temp;
    CustomAdapterReceipts customAdapterReceipts;
    Boolean empty=true;

    public ViewReceipts() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view  = inflater.inflate(R.layout.fragment_view_payment, container, false);
        listView = (ListView) view.findViewById(R.id.paymentList);
        refreshLayout = (RefreshLayout) view.findViewById(R.id.swipe_container);
        footerLayout = getLayoutInflater(null).inflate(R.layout.listview_footer, null);

        listView.addFooterView(footerLayout);
        refreshLayout.setChildView(listView);

        listView.setAdapter(customAdapterReceipts);


        final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final RequestParams requestParams= new RequestParams();
        requestParams.put("Last_Index",0);
        requestParams.put("Email_Address", sharedPreferences.getString("email", ""));
        asyncHttpClient.post("http://54.149.252.188/TipM/GetAllReceipts.php", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if (response.isNull(0)) {
                    refreshLayout.removeView(footerLayout);
                    empty=true;
                    String[] payments = {"No receipts yet"};
                    listView.setAdapter((new ArrayAdapter<String>(getActivity(), R.layout.empty_layout,R.id.empty_textview, payments)));
                } else {
                    empty = false;
                    customAdapterReceipts = new CustomAdapterReceipts(getActivity(), response);
                    listView.setAdapter(customAdapterReceipts);
                }
            }
        });


        refreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                int position = listView.getLastVisiblePosition();
                RequestParams request = new RequestParams();
                request.put("Last_Index", position);
                request.put("Email_Address", sharedPreferences.getString("email", ""));

                asyncHttpClient.post(getActivity(), "http://54.149.252.188/TipM/GetAllReceipts.php", request, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        if (response.isNull(0)) {
                            refreshLayout.setLoading(false);
                            refreshLayout.removeView(footerLayout);
                        } else {
                            customAdapterReceipts.add(response);
                            customAdapterReceipts.notifyDataSetChanged();
                            refreshLayout.setLoading(false);
                        }


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Toast.makeText(getActivity(), responseString, Toast.LENGTH_LONG).show();
                    }
                });


            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RequestParams request = new RequestParams();
                JSONObject firstObject;
                if (empty == true) {
                    firstObject = null;
                } else {
                    firstObject = customAdapterReceipts.getItem(0);
                }

                request.put("Email_Address", sharedPreferences.getString("email", ""));
                request.put("First_Object", firstObject);

                AsyncHttpClient asyncHttpClient1 = new AsyncHttpClient();
                asyncHttpClient1.post(getActivity(), "http://54.149.252.188/TipM/RefreshReceipts.php", request, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        if (response.isNull(0)) {
                            refreshLayout.setRefreshing(false);
                        } else {
                            if (empty == true) {
                                customAdapterReceipts = new CustomAdapterReceipts(getActivity(), response);
                                listView.setAdapter(customAdapterReceipts);
                                customAdapterReceipts.notifyDataSetChanged();
                                refreshLayout.setRefreshing(false);

                                empty=false;
                            } else {
                                customAdapterReceipts.addRefresh(response);
                                customAdapterReceipts.notifyDataSetChanged();
                                refreshLayout.setRefreshing(false);
                            }
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        refreshLayout.setRefreshing(false);
                    }
                });

            }
        });

        return view;
    }


}
