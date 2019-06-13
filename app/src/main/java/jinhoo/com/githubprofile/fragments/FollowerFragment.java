package jinhoo.com.githubprofile.fragments;

import static android.content.Context.MODE_PRIVATE;
import static jinhoo.com.githubprofile.MainActivity.NETWORK_TAG;
import static jinhoo.com.githubprofile.MainActivity.TAG;
import static jinhoo.com.githubprofile.MainActivity.TAG2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import jinhoo.com.githubprofile.R;
import jinhoo.com.githubprofile.adapters.FollowerGridAdapter;
import jinhoo.com.githubprofile.adapters.FollowingGridAdapter;
import jinhoo.com.githubprofile.adapters.MainFollowerGridAdapter;
import jinhoo.com.githubprofile.network_util.AppSingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FollowerFragment extends android.support.v4.app.Fragment {
  RequestQueue queue;
  private GridView mGridView;
  ArrayList<String> follower_info = new ArrayList<>();

  public static FollowerFragment newInstance() {
    FollowerFragment fragment = new FollowerFragment();
    return fragment;
  }

  public static FollowerFragment newInstance(Boolean other_user) {
    FollowerFragment fragment = new FollowerFragment();
    Bundle args = new Bundle();
    args.putBoolean("other_user", other_user);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_follower, container, false);

    final String tag;
    Bundle args = getArguments();
    if(args == null) {
      tag = TAG;
    }
    else {
      tag = TAG2;
    }

    SharedPreferences mSharedPreferences = view.getContext().getSharedPreferences(tag, MODE_PRIVATE);
    String followers_url = mSharedPreferences.getString("followers_url", null);
    mGridView = view.findViewById(R.id.follower_gv);

    queue = AppSingleton.getInstance(getContext()).getRequestQueue();
    StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, followers_url,
        new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            try {
              JSONArray jsonArray = new JSONArray(response);
              for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String username = jsonObject.getString("login");
                String avatar_url = jsonObject.getString("avatar_url");
                String url = jsonObject.getString("url");
                follower_info.add(username + "," + avatar_url + "," + url);
              }
              if(tag == TAG) {
                MainFollowerGridAdapter mAdapter;
                mAdapter = new MainFollowerGridAdapter(getContext(), follower_info);
                mAdapter.notifyDataSetChanged();
                mGridView.setAdapter(mAdapter);
              }
              else {
                FollowerGridAdapter mAdapter;
                mAdapter = new FollowerGridAdapter(getContext(), follower_info);
                mAdapter.notifyDataSetChanged();
                mGridView.setAdapter(mAdapter);
              }



            } catch (JSONException e) {
              e.printStackTrace();
            }

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
      }};
    jsonObjectRequest.setTag(NETWORK_TAG);
    AppSingleton.getInstance(view.getContext()).addToRequestQueue(jsonObjectRequest);
    queue.getCache().clear();
    return view;
  }
}
