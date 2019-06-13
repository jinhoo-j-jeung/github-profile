package jinhoo.com.githubprofile.adapters;

import static jinhoo.com.githubprofile.MainActivity.NETWORK_TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Orientation;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import jinhoo.com.githubprofile.ProfileViewActivity;
import jinhoo.com.githubprofile.R;
import jinhoo.com.githubprofile.network_util.AppSingleton;
import jinhoo.com.githubprofile.network_util.MetaRequest;
import org.json.JSONException;
import org.json.JSONObject;

public class FollowerGridAdapter extends BaseAdapter {
  RequestQueue queue;
  private Context context;
  private ArrayList<String> follower_info;

  public FollowerGridAdapter(Context context, ArrayList<String> follower_info) {
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
    TextView textView;
    ImageView imageView;
    ImageView followButton;

    cardView = new CardView(context);
    cardView.setLayoutParams(new GridView.LayoutParams(470, 470));
    relativeLayout = new RelativeLayout(context);
    relativeLayout.setLayoutParams(new GridView.LayoutParams(470, 470));
    relativeLayout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
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


    relativeLayout.addView(imageView);
    relativeLayout.addView(textView);
    cardView.addView(relativeLayout);

    imageView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(context, ProfileViewActivity.class);
        intent.putExtra("url", api_url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
      }
    });
    return cardView;
  }
}
