package jinhoo.com.githubprofile;

import static jinhoo.com.githubprofile.MainActivity.NETWORK_TAG;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import jinhoo.com.githubprofile.adapters.UserSearchResultListAdapter;
import jinhoo.com.githubprofile.network_util.AppSingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserSearchResultActivity extends AppCompatActivity {
  RequestQueue queue;
  private RecyclerView mRecyclerView;
  private LayoutManager mLayoutManager;
  ArrayList<String> usernames = new ArrayList<>();
  ArrayList<String> avatar_urls = new ArrayList<>();
  ArrayList<String> html_urls = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_search_result);

    Bundle bundle = getIntent().getExtras();
    String url = bundle.getString("url");

    mRecyclerView = findViewById(R.id.user_search_result_rv);
    queue = AppSingleton.getInstance(this).getRequestQueue();

    final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,  url, null,
        new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
            try {
              JSONArray jsonArray = response.getJSONArray("items");
              for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String username = jsonObject.getString("login");
                String avatar_url = jsonObject.getString("avatar_url");
                String html_url = jsonObject.getString("html_url");
                usernames.add(username);
                avatar_urls.add(avatar_url);
                html_urls.add(html_url);
              }
            } catch (JSONException e1) {
              e1.printStackTrace();
            }
            mLayoutManager = new LinearLayoutManager(UserSearchResultActivity.this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            UserSearchResultListAdapter mAdapter = new UserSearchResultListAdapter(UserSearchResultActivity.this, usernames, avatar_urls, html_urls);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(mAdapter);
          }
        }, new ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        if(Log.isLoggable("Volley Error", Log.ERROR)) Log.e("Volley Error", error.getMessage());
      }
    }){
      @Override
      public Map<String,String> getHeaders() {
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","token 9969557f16050f2475ed4515f9834e88150547c6");
        return headers;
      }
    };
    jsonObjectRequest.setTag(NETWORK_TAG);
    AppSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    queue.getCache().clear();

  }
}
