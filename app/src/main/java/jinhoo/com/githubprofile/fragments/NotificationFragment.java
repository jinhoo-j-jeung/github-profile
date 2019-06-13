package jinhoo.com.githubprofile.fragments;

import static android.content.Context.MODE_PRIVATE;
import static jinhoo.com.githubprofile.MainActivity.NETWORK_TAG;
import static jinhoo.com.githubprofile.MainActivity.TAG;
import static jinhoo.com.githubprofile.MainActivity.TAG2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import jinhoo.com.githubprofile.R;
import jinhoo.com.githubprofile.adapters.FollowerGridAdapter;
import jinhoo.com.githubprofile.adapters.MainFollowerGridAdapter;
import jinhoo.com.githubprofile.adapters.MainRepoListAdapter;
import jinhoo.com.githubprofile.adapters.NotificationListAdapter;
import jinhoo.com.githubprofile.network_util.AppSingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NotificationFragment extends android.support.v4.app.Fragment {
  RequestQueue queue;
  private RecyclerView mRecyclerView;
  private LayoutManager mLayoutManager;
  ArrayList<String> notification_info = new ArrayList<>();

  public static NotificationFragment newInstance() {
    NotificationFragment fragment = new NotificationFragment();
    return fragment;
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_notification, container, false);
    mRecyclerView = view.findViewById(R.id.notification_rv);
    final String notification_url = "https://api.github.com/notifications?all=1";
    Log.d("qwert", notification_url);
    queue = AppSingleton.getInstance(getContext()).getRequestQueue();
    JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET,  notification_url, null,
        new Response.Listener<JSONArray>() {
          @Override
          public void onResponse(JSONArray response) {
            try {
              for(int i = 0; i < response.length(); i++) {
                JSONObject jsonObject = (JSONObject) response.get(i);
                JSONObject subjectObject = jsonObject.getJSONObject("subject");
                JSONObject repoObject = (JSONObject) jsonObject.getJSONObject("repository");
                JSONObject ownerObject = (JSONObject) repoObject.getJSONObject("owner");
                String title = subjectObject.getString("title");
                String type = subjectObject.getString("type");
                String repo_name = repoObject.getString("name");
                String repo_link = "https://github.com/"+repoObject.getString("full_name");
                String owner_avatar_url = ownerObject.getString("avatar_url");
                String unread = jsonObject.getString("unread");
                notification_info.add(title+","+type+","+repo_name+","+repo_link+","+owner_avatar_url+","+unread);
              }
            } catch (JSONException e1) {
              e1.printStackTrace();
            }
            mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            NotificationListAdapter mAdapter = new NotificationListAdapter(getContext(), notification_info);
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
      @Override
      public Map<String, String> getParams() {
        Map<String,String> params = new HashMap<>();
        params.put("all",String.valueOf(1));
        return params;
      }
    };
    jsonObjectRequest.setTag(NETWORK_TAG);
    AppSingleton.getInstance(view.getContext()).addToRequestQueue(jsonObjectRequest);
    queue.getCache().clear();

    return view;
  }
}
