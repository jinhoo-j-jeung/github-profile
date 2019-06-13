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
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import jinhoo.com.githubprofile.R;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {
  private Context context;
  private ArrayList<String> notification_info;

  public NotificationListAdapter(Context context, ArrayList<String> notification_info) {
    this.context = context;
    this.notification_info = notification_info;
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    TextView notify_row_repo_tv;
    TextView notfiy_row_type_tv;
    TextView notify_row_title_tv;
    CircularImageView notfiy_row_avatar_iv;
    ImageView notify_row_read_iv;

    public ViewHolder(View v) {
      super(v);
      notify_row_repo_tv = v.findViewById(R.id.notify_row_repo_tv);
      notfiy_row_type_tv = v.findViewById(R.id.notify_row_type_tv);
      notify_row_title_tv = v.findViewById(R.id.notify_row_title_tv);
      notfiy_row_avatar_iv = v.findViewById(R.id.notify_row_avarat_iv);
      notify_row_read_iv = v.findViewById(R.id.notify_row_read_iv);
    }
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(context);
    View view = inflater.inflate(R.layout.notification_list_row, null);
    ViewHolder vh = new ViewHolder(view);
    return vh;
  }

  @Override
  public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
    String vi = notification_info.get(position);
    //notification_info.add(title+","+type+","+repo_name+","+repo_link+","+owner_avatar_url+","+unread);
    String[] array = vi.split(",");
    String title = array[0];
    String type = array[1];
    String repo_name = array[2];
    final String repo_link = array[3];
    String owner_avatar_url = array[4];
    String unread = array[5];

    holder.notify_row_repo_tv.setText(repo_name);
    holder.notify_row_repo_tv.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(repo_link));
        context.startActivity(intent);
      }
    });
    holder.notfiy_row_type_tv.setText("<"+type+">");
    holder.notify_row_title_tv.setText(title);
    Glide.with(context).load(owner_avatar_url).into(holder.notfiy_row_avatar_iv);
    if(unread.equals("false")) {
      holder.notify_row_read_iv.setImageResource(R.drawable.ic_check_green_24dp);
    }
    else {
      holder.notify_row_read_iv.setImageResource(R.drawable.ic_error_outline_red_24dp);
    }
    //holder.link_tv.setText(link);
  }

  @Override
  public int getItemCount() {
    return notification_info.size();
  }
}
