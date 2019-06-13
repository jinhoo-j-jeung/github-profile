package jinhoo.com.githubprofile.fragments;

import static android.content.Context.MODE_PRIVATE;
import static jinhoo.com.githubprofile.MainActivity.NETWORK_TAG;
import static jinhoo.com.githubprofile.MainActivity.TAG;
import static jinhoo.com.githubprofile.MainActivity.TAG2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import jinhoo.com.githubprofile.adapters.MainRepoListAdapter;
import jinhoo.com.githubprofile.adapters.RepoListAdapter;
import jinhoo.com.githubprofile.network_util.AppSingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileViewRepositoryFragment extends android.support.v4.app.Fragment {
  RequestQueue queue;
  private RecyclerView mRecyclerView;
  private LayoutManager mLayoutManager;
  ArrayList<String> repo_info = new ArrayList<>();

  public static ProfileViewRepositoryFragment newInstance() {
    ProfileViewRepositoryFragment fragment = new ProfileViewRepositoryFragment();
    return fragment;
  }

  public static ProfileViewRepositoryFragment newInstance(Boolean other_user) {
    ProfileViewRepositoryFragment fragment = new ProfileViewRepositoryFragment();
    Bundle args = new Bundle();
    args.putBoolean("other_user", other_user);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_profileview_repo, container, false);

    final String tag;
    Bundle args = getArguments();
    if(args == null) {
      tag = TAG;
    }
    else {
      tag = TAG2;
    }

    final SharedPreferences mSharedPreferences = view.getContext().getSharedPreferences(tag, MODE_PRIVATE);
    String repo_url = mSharedPreferences.getString("repos_url", null);

    mRecyclerView = view.findViewById(R.id.profileview_repo_rv);

    queue = AppSingleton.getInstance(getContext()).getRequestQueue();

    /*
     * Get all the repositories
     */
    StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, repo_url,
        new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            try {
              JSONArray jsonArray = new JSONArray(response);
              for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String name = jsonObject.getString("name");
                JSONObject jsonOwner = jsonObject.getJSONObject("owner");
                String avatar_url = jsonOwner.getString("avatar_url");
                String owner = jsonOwner.getString("login");
                String url = jsonObject.getString("html_url");
                String description = jsonObject.getString("description");

                repo_info.add(name + "," + owner + "," + url + "," + avatar_url+","+description);

              }
              mLayoutManager = new LinearLayoutManager(getContext());
              mRecyclerView.setLayoutManager(mLayoutManager);
              RepoListAdapter mAdapter = new RepoListAdapter(getContext(), repo_info);
              mAdapter.notifyDataSetChanged();
              mRecyclerView.setAdapter(mAdapter);

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
