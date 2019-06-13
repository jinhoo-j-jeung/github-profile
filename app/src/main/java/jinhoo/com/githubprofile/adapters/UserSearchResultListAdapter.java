package jinhoo.com.githubprofile.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import jinhoo.com.githubprofile.R;

public class UserSearchResultListAdapter extends RecyclerView.Adapter<UserSearchResultListAdapter.ViewHolder> {
  private Context context;
  private ArrayList<String> usernames;
  private ArrayList<String> avatar_urls;
  private ArrayList<String> html_urls;

  public UserSearchResultListAdapter(Context context, ArrayList<String> usernames, ArrayList<String> avatar_urls, ArrayList<String> html_urls) {
    this.context = context;
    this.usernames = usernames;
    this.avatar_urls = avatar_urls;
    this.html_urls = html_urls;
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    TextView username_tv;
    TextView link_tv;
    ImageView avatar_iv;

    public ViewHolder(View v) {
      super(v);
      username_tv = v.findViewById(R.id.user_search_result_id_tv);
      link_tv = v.findViewById(R.id.user_search_result_link_tv);
      avatar_iv = v.findViewById(R.id.user_search_result_iv);
    }
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(context);
    View view = inflater.inflate(R.layout.user_serarch_list_row, null);
    ViewHolder vh = new ViewHolder(view);
    return vh;
  }

  @Override
  public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
    String username = usernames.get(position);
    String avatar_url = avatar_urls.get(position);
    String html_url = html_urls.get(position);

    holder.username_tv.setText(username);
    holder.link_tv.setText(html_url);
    Glide.with(context).load(avatar_url).into(holder.avatar_iv);

    //holder.link_tv.setText(link);
  }

  @Override
  public int getItemCount() {
    return usernames.size();
  }
}
