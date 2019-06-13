package jinhoo.com.githubprofile.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import jinhoo.com.githubprofile.ProfileViewActivity;

public class FollowingGridAdapter extends BaseAdapter {
  private Context context;
  private ArrayList<String> following_info;
  private boolean other_user;

  public FollowingGridAdapter(Context context, ArrayList<String> following_info) {
    this.context = context;
    this.following_info = following_info;
  }

  @Override
  public int getCount() {
    return following_info.size();
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

    cardView = new CardView(context);
    cardView.setLayoutParams(new GridView.LayoutParams(470, 470));
    relativeLayout = new RelativeLayout(context);
    relativeLayout.setLayoutParams(new GridView.LayoutParams(470, 470));
    relativeLayout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
    textView = new TextView(context);
    textView.setLayoutParams(new GridView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    imageView = new ImageView(context);
    imageView.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

    String[] data = following_info.get(i).split(",");
    String username = data[0];
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

    cardView.setOnClickListener(new OnClickListener() {
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
