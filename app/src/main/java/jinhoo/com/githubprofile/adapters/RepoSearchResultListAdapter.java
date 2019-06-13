package jinhoo.com.githubprofile.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import jinhoo.com.githubprofile.R;

public class RepoSearchResultListAdapter extends RecyclerView.Adapter<RepoSearchResultListAdapter.ViewHolder> {
  private Context context;
  private ArrayList<String> names;
  private ArrayList<String> owners;
  private ArrayList<String> avatar_urls;
  private ArrayList<String> html_urls;

  public RepoSearchResultListAdapter(Context context, ArrayList<String> names, ArrayList<String> owners, ArrayList<String> avatar_urls, ArrayList<String> html_urls) {
    this.context = context;
    this.names = names;
    this.owners = owners;
    this.avatar_urls = avatar_urls;
    this.html_urls = html_urls;
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    TextView title_tv;
    TextView username_tv;
    TextView link_tv;
    CircularImageView avatar_iv;

    public ViewHolder(View v) {
      super(v);
      title_tv = v.findViewById(R.id.repo_search_result_id_tv);
      username_tv = v.findViewById(R.id.repo_search_result_owner_tv);
      link_tv = v.findViewById(R.id.repo_search_result_link_tv);
      avatar_iv = v.findViewById(R.id.repo_search_result_iv);
    }
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(context);
    View view = inflater.inflate(R.layout.repo_serarch_list_row, null);
    ViewHolder vh = new ViewHolder(view);
    return vh;
  }

  @Override
  public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
    String name = names.get(position);
    String owner = owners.get(position);
    String avatar_url = avatar_urls.get(position);
    String html_url = html_urls.get(position);
    holder.title_tv.setText(name);
    holder.username_tv.setText(owner);
    holder.link_tv.setText(html_url);
    Glide.with(context).load(avatar_url).into(holder.avatar_iv);
  }

  @Override
  public int getItemCount() {
    return names.size();
  }
}
