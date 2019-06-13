package jinhoo.com.githubprofile.adapters;

import static jinhoo.com.githubprofile.MainActivity.NETWORK_TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import jinhoo.com.githubprofile.ProfileViewActivity;
import jinhoo.com.githubprofile.R;
import jinhoo.com.githubprofile.network_util.AppSingleton;
import jinhoo.com.githubprofile.network_util.MetaRequest;
import org.json.JSONException;
import org.json.JSONObject;

public class MainFollowerGridAdapter extends BaseAdapter {
  RequestQueue queue;
  private Context context;
  private ArrayList<String> follower_info;

  public MainFollowerGridAdapter(Context context, ArrayList<String> follower_info) {
    this.context = context;
    this.follower_info = follower_info;
  }

  @Override
  public int getCount() {
    return follower_info.size();
  }

  @Override
  public Object getItem(int i) {
    return null;
  }

  @Override
  public long getItemId(int i) {
    return 0;
  }

  @Override
  public View getView(int i, View view, ViewGroup viewGroup) {
    CardView cardView;
    RelativeLayout relativeLayout;
    LinearLayout linearLayout;
    TextView textView;
    ImageView imageView;
    ImageView followButton;

    cardView = new CardView(context);
    cardView.setLayoutParams(new GridView.LayoutParams(470, 470));
    relativeLayout = new RelativeLayout(context);
    relativeLayout.setLayoutParams(new GridView.LayoutParams(470, 470));
    relativeLayout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
    linearLayout = new LinearLayout(context);
    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
    linearLayout.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    textView = new TextView(context);
    textView.setLayoutParams(new GridView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    followButton = new ImageView(context);
    followButton.setImageResource(R.drawable.ic_grade_white_24dp);
    followButton.setLayoutParams(new GridView.LayoutParams(100, 100));
    imageView = new ImageView(context);
    imageView.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

    String[] data = follower_info.get(i).split(",");
    final String username = data[0];
    String image_url = data[1];
    final String api_url = data[2];

    textView.setText(username);
    textView.setPadding(10, 10, 10, 0);
    textView.setTextSize(25);
    textView.setTypeface(null, Typeface.BOLD);
    textView.setTextColor(Color.parseColor("white"));

    Glide.with(context).load(image_url).into(imageView);

    linearLayout.addView(textView);
    linearLayout.addView(followButton);
    relativeLayout.addView(imageView);
    relativeLayout.addView(linearLayout);
    cardView.addView(relativeLayout);

    imageView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(context, ProfileViewActivity.class);
        intent.putExtra("url", api_url);
        context.startActivity(intent);
      }
    });
    followButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        String url = "https://api.github.com/user/following/"+username;
        queue = AppSingleton.getInstance(context).getRequestQueue();

        MetaRequest jsonObjectRequest = new MetaRequest(Request.Method.PUT, url, null,
            new Response.Listener<JSONObject>() {
              @Override
              public void onResponse(JSONObject response) {
                try {
                  String status = response.getJSONObject("headers").getString("Status").toString();
                  if(status.contains("204")) {
                    Toast.makeText(context, "You're following "+username+" now!", Toast.LENGTH_LONG).show();
                  }
                  else if(status.contains("404")){
                    Toast.makeText(context, "Not Found", Toast.LENGTH_LONG).show();
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
    return cardView;
  }
}
