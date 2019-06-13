package jinhoo.com.githubprofile.fragments;

import static android.content.Context.MODE_PRIVATE;
import static jinhoo.com.githubprofile.MainActivity.TAG;
import static jinhoo.com.githubprofile.MainActivity.TAG2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import jinhoo.com.githubprofile.R;

public class ProfileFragment extends android.support.v4.app.Fragment {
  public static ProfileFragment newInstance() {
    ProfileFragment fragment = new ProfileFragment();
    return fragment;
  }

  public static ProfileFragment newInstance(Boolean other_user) {
    ProfileFragment fragment = new ProfileFragment();
    Bundle args = new Bundle();
    args.putBoolean("other_user", other_user);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_profile, container, false);

    String tag;
    Bundle args = getArguments();
    if(args == null) {
      tag = TAG;
    }
    else {
      tag = TAG2;
    }

    SharedPreferences mSharedPreferences = view.getContext().getSharedPreferences(tag, MODE_PRIVATE);

    TextView username_tv = view.findViewById(R.id.profile_username_tv);
    username_tv.setText("@"+mSharedPreferences.getString("username", "failed"));

    int t_idx = mSharedPreferences.getString("created_date", "failed").indexOf('T');
    TextView created_tv = view.findViewById(R.id.profile_date_created_tv);
    if(t_idx < 0) {
      created_tv.setText("hi");
    }
    else {
      created_tv.setText(mSharedPreferences.getString("created_date", "failed").substring(0, t_idx));
    }

    TextView updated_tv = view.findViewById(R.id.profile_date_updated_tv);
    t_idx = mSharedPreferences.getString("updated_date", "failed").indexOf('T');
    if(t_idx < 0){
      updated_tv.setText("hi");
    }
    else {
      updated_tv
          .setText(mSharedPreferences.getString("updated_date", "failed").substring(0, t_idx));
    }
    ImageView profile_image = view.findViewById(R.id.profile_iv);
    RequestOptions requestOptions = new RequestOptions();
    requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
    Glide.with(view.getContext()).load(mSharedPreferences.getString("avatar_url", null)).apply(requestOptions).into(profile_image);

    TextView repo_tv = view.findViewById(R.id.profile_repo_tv);
    repo_tv.setText(mSharedPreferences.getString("repo_count", "-1"));
    if(tag == TAG) {
      repo_tv.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          FragmentTransaction transaction = getFragmentManager().beginTransaction();
          transaction.replace(R.id.main_fl, RepositoryFragment.newInstance());
          transaction.commit();
        }
      });
    }
    else {
      repo_tv.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          FragmentTransaction transaction = getFragmentManager().beginTransaction();
          transaction.replace(R.id.profileview_fl, ProfileViewRepositoryFragment.newInstance(true));
          transaction.commit();
        }
      });
    }
    TextView follower_tv = view.findViewById(R.id.profile_follower_tv);
    follower_tv.setText(mSharedPreferences.getString("followers", "-1"));
    if(tag == TAG) {
      follower_tv.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          FragmentTransaction transaction = getFragmentManager().beginTransaction();
          transaction.replace(R.id.main_fl, FollowerFragment.newInstance());
          transaction.commit();
        }
      });
    }
    else {
      follower_tv.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          FragmentTransaction transaction = getFragmentManager().beginTransaction();
          transaction.replace(R.id.profileview_fl, FollowerFragment.newInstance(true));
          transaction.commit();
        }
      });
    }
    TextView following_tv = view.findViewById(R.id.profile_following_tv);
    following_tv.setText(mSharedPreferences.getString("following", "-1"));
    if(tag == TAG) {
      following_tv.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          FragmentTransaction transaction = getFragmentManager().beginTransaction();
          transaction.replace(R.id.main_fl, FollowingFragment.newInstance());
          transaction.commit();
        }
      });
    }
    else {
      following_tv.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          FragmentTransaction transaction = getFragmentManager().beginTransaction();
          transaction.replace(R.id.profileview_fl, FollowingFragment.newInstance(true));
          transaction.commit();
        }
      });
    }
    TextView name_tv = view.findViewById(R.id.profile_name_tv);
    name_tv.setText(mSharedPreferences.getString("name", "failed"));

    TextView bio_tv = view.findViewById(R.id.profile_bio_tv);
    bio_tv.setText(mSharedPreferences.getString("bio", "failed"));

    TextView website_tv = view.findViewById(R.id.profile_website_tv);
    website_tv.setText(mSharedPreferences.getString("website", "failed"));

    TextView email_tv = view.findViewById(R.id.profile_email_tv);
    String email = mSharedPreferences.getString("email", "failed");
    if(email.equals("null")) {
      email = "No Public Email";
    }
    email_tv.setText(email);

    return view;
  }
}
