package jinhoo.com.githubprofile.adapters;

import static jinhoo.com.githubprofile.MainActivity.NETWORK_TAG;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import jinhoo.com.githubprofile.R;
import jinhoo.com.githubprofile.RepoDataActivity;
import jinhoo.com.githubprofile.network_util.AppSingleton;
import jinhoo.com.githubprofile.network_util.MetaRequest;
import org.json.JSONException;
import org.json.JSONObject;

public class MainRepoListAdapter extends RecyclerView.Adapter<MainRepoListAdapter.ViewHolder> {
  RequestQueue queue;
  private Context context;
  private ArrayList<String> repo_info;

  public MainRepoListAdapter(Context context, ArrayList<String> repo_info) {
    this.context = context;
    this.repo_info = repo_info;
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    TextView name_tv;
    TextView owner_tv;
    TextView description_tv;
    TextView link_tv;
    ImageView repo_profile_iv;
    RelativeLayout mRelativeLayout;
    ImageView repo_star_iv;

    public ViewHolder(View v) {
      super(v);
      repo_profile_iv = v.findViewById(R.id.repo_profile_iv);
      name_tv = v.findViewById(R.id.repo_list_row_name);
      owner_tv = v.findViewById(R.id.repo_list_row_author);
      description_tv = v.findViewById(R.id.repo_list_row_description);
      link_tv = v.findViewById(R.id.repo_list_row_link);
      mRelativeLayout = v.findViewById(R.id.repo_rl);
      repo_star_iv = v.findViewById(R.id.repo_list_star_iv);
    }
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(context);
    View view = inflater.inflate(R.layout.repo_list_row, null);
    ViewHolder vh = new ViewHolder(view);
    return vh;
  }

  @Override
  public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
    String vi = repo_info.get(position);
    String[] array = vi.split(",");
    final String name = array[0];
    final String owner = array[1];
    final String link = array[2];
    String avatar_url = array[3];
    String description = array[4];
    String starred = array[5];

    if(!starred.contains("unstarred")) {
      holder.repo_star_iv.setImageResource(R.drawable.ic_grade_yellow_24dp);
      holder.repo_star_iv.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          String url = "https://api.github.com/user/starred/"+owner+"/"+name;
          queue = AppSingleton.getInstance(context).getRequestQueue();

          MetaRequest jsonObjectRequest = new MetaRequest(Method.DELETE, url, null,
              new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                  try {
                    String status = response.getJSONObject("headers").getString("Status").toString();
                    if(status.contains("204")) {
                      holder.repo_star_iv.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    }
                    else {
                      Toast.makeText(context, "UnStarring Failed", Toast.LENGTH_LONG).show();
                    }
                  } catch (JSONException e) {
                    e.printStackTrace();
                  }
                }
              }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              Log.d("qwert", error.getMessage());
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
          AppSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
        }
      });
    }
    else {
      holder.repo_star_iv.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          String url = "https://api.github.com/user/starred/"+owner+"/"+name;
          queue = AppSingleton.getInstance(context).getRequestQueue();

          MetaRequest jsonObjectRequest = new MetaRequest(Method.PUT, url, null,
              new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                  try {
                    String status = response.getJSONObject("headers").getString("Status").toString();
                    if(status.contains("204")) {
                      holder.repo_star_iv.setImageResource(R.drawable.ic_grade_yellow_24dp);
                    }
                    else {
                      Toast.makeText(context, "Starring Failed", Toast.LENGTH_LONG).show();
                    }
                  } catch (JSONException e) {
                    e.printStackTrace();
                  }
                }
              }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              Log.d("qwert", error.getMessage());
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
          AppSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
        }
      });
    }
    Glide.with(context).load(avatar_url).into(holder.repo_profile_iv);
    holder.name_tv.setText(name);
    holder.name_tv.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(context, RepoDataActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("owner", owner);
        context.startActivity(intent);
      }
    });
    holder.owner_tv.setText("by "+owner);
    if(description.equals("null")) description = "No Desciption Available";
    holder.description_tv.setText(description);
    holder.link_tv.setText(link);
  }

  @Override
  public int getItemCount() {
    return repo_info.size();
  }
}
