package alee.info.addressbook.Activities;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import alee.info.addressbook.Adapters.SimpleAdapter;
import alee.info.addressbook.Pojo.RandomUsers;
import alee.info.addressbook.R;

public class AddressListActivity extends AppCompatActivity {

    RandomUsers ru;
    List<RandomUsers> randomUsers;
    public EditText search;
    RecyclerView mRecyclerView;
    public SimpleAdapter mAdapter;
    RequestQueue queue;
    String TAG = "AddressListActivity";
    String url = "https://randomuser.me/api/?results=500";
    ProgressDialog pd;

    JSONArray results = null;
    List<RandomUsers> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

        queue = Volley.newRequestQueue(this);
        randomUsers = new ArrayList<>();
        pd = new ProgressDialog(AddressListActivity.this);
        pd.setMessage("loading");
        pd.show();
        //Call API RandomUsers
        randomUsers.clear();
        getRandomUsersFromAPI();

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();

        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_action_bar, null);
        search = (EditText) mCustomView
                .findViewById(R.id.edtSearch);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        textSearchListener();

    }

    /**
     * function for list filteration based on name
     */
    public void textSearchListener() {

        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

                final List<RandomUsers> filteredList = new ArrayList<>();

                for (int i = 0; i < randomUsers.size(); i++) {

                    final String text = randomUsers.get(i).getName().toLowerCase();
                    if (text.contains(query)) {

                        filteredList.add(randomUsers.get(i));
                    }
                }

                mRecyclerView.setLayoutManager(new LinearLayoutManager(AddressListActivity.this));
                mAdapter = new SimpleAdapter(filteredList, AddressListActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();  // data set changed
            }
        });
    }

    /**
     * function to call randomusers API
     */
    public void getRandomUsersFromAPI() {
        Log.i(TAG, "Called getRandomUsers!");
        //make request to get random Users
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseResponse(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonObjReq);
    }


    /**
     * function to call randomusers API
     */
    private void parseResponse(JSONObject response) {


        data = new ArrayList<>();
        try {
            results = response.getJSONArray("results");

            if (results.length() > 0) {
                //make image request
                ImageRequest ir = new ImageRequest(results.getJSONObject(0).getJSONObject("picture").getString("thumbnail"), new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {


                        for (int i = 0; i < results.length(); i++) {
                            ru = new RandomUsers();
                            try {

                                Log.i(TAG, " Name : " + results.getJSONObject(i).getJSONObject("name").getString("first"));
                                ru.setName(results.getJSONObject(i).getJSONObject("name").getString("first") + " " + results.getJSONObject(i).getJSONObject("name").getString("last"));
                                ru.setEmail(results.getJSONObject(i).getString("email"));
                                ru.setGender(results.getJSONObject(i).getString("gender"));
                                ru.setDob(results.getJSONObject(i).getString("dob"));
                                ru.setCell(results.getJSONObject(i).getString("cell"));
                                ru.setPhone(results.getJSONObject(i).getString("phone"));
                                ru.setNat(results.getJSONObject(i).getString("nat"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (i == 0) {
                                ru.setPicture(response);
                            }
                            data.add(ru);

                            randomUsers = data;
                            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                            LinearLayoutManager llm = new LinearLayoutManager(AddressListActivity.this);
                            llm.setOrientation(LinearLayoutManager.VERTICAL);
                            mRecyclerView.setHasFixedSize(true);

                            mRecyclerView.setLayoutManager(llm);

                            // call the adapter with argument list of items and context.
                            mAdapter = new SimpleAdapter(data, AddressListActivity.this);
                            mRecyclerView.setAdapter(mAdapter);
                            Log.i(TAG, response.toString());

                        }
                        pd.dismiss();
                    }
                }, 0, 0, null, null);
                queue.add(ir);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
