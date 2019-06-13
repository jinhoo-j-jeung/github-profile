package jinhoo.com.githubprofile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;
import jinhoo.com.githubprofile.fragments.FollowerFragment;
import jinhoo.com.githubprofile.fragments.FollowingFragment;
import jinhoo.com.githubprofile.fragments.NotificationFragment;
import jinhoo.com.githubprofile.fragments.ProfileFragment;
import jinhoo.com.githubprofile.fragments.RepositoryFragment;
import jinhoo.com.githubprofile.network_util.AppSingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
  RequestQueue queue;
  public static String TAG = "User Github Information";
  public static String TAG2 = "Other Github Information";
  public static final String NETWORK_TAG = "GIT REQUEST";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    /*
     * Custom Toolbar for searching
     */
    Toolbar mToolbar = findViewById(R.id.actionbar_main);
    setSupportActionBar(mToolbar);
    TextView toolbar_title = findViewById(R.id.actionbar_logo_tv);
    toolbar_title.setText("My Github Account");

    /*
     * Navigation Drawer
     */
    final DrawerLayout mDrawerLayout = findViewById(R.id.drawer_layout);

    // Search button that opens the navigation drawer
    ImageButton drawer_bt = findViewById(R.id.actionbar_drawer_bt);
    drawer_bt.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        boolean isDrawerOpen = mDrawerLayout.isDrawerOpen(Gravity.START);
        if(isDrawerOpen) {
          mDrawerLayout.closeDrawer(Gravity.START);
        }
        else {
          mDrawerLayout.openDrawer(Gravity.START);
        }
      }
    });

    final RadioGroup radioGroup = findViewById(R.id.drawer_rg1);
    final RelativeLayout user_rl = findViewById(R.id.drawer_user_rl);
    final RelativeLayout repo_rl = findViewById(R.id.drawer_repo_rl);
    radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener(){
      @Override
      public void onCheckedChanged(RadioGroup group, int checkedId)
      {
        switch(checkedId)
        {
          case R.id.search_user:
            repo_rl.setVisibility(View.INVISIBLE);
            user_rl.setVisibility(View.VISIBLE);
            break;
          case R.id.search_repo:
            repo_rl.setVisibility(View.VISIBLE);
            user_rl.setVisibility(View.INVISIBLE);
            break;
        }
      }
    });
    final Switch user_asc_switch = findViewById(R.id.user_asc);
    final RadioGroup user_radioGroup = findViewById(R.id.drawer_user_rg1);
    user_radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch(i)
        {
          case R.id.sort_none:
            user_asc_switch.setVisibility(View.INVISIBLE);
            break;
          case R.id.sort_follower:
            user_asc_switch.setVisibility(View.VISIBLE);
            break;
          case R.id.sort_repo:
            user_asc_switch.setVisibility(View.VISIBLE);
            break;
        }
      }
    });

    final Switch repo_asc_switch = findViewById(R.id.repo_asc);
    final RadioGroup repo_radioGroup = findViewById(R.id.drawer_repo_rg1);
    repo_radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch(i)
        {
          case R.id.sort_none2:
            repo_asc_switch.setVisibility(View.INVISIBLE);
            break;
          case R.id.sort_star:
            repo_asc_switch.setVisibility(View.VISIBLE);
            break;
          case R.id.sort_fork:
            repo_asc_switch.setVisibility(View.VISIBLE);
            break;
        }
      }
    });

    Button submit = findViewById(R.id.drawer_submit);
    submit.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        EditText editText = findViewById(R.id.drawer_et);
        String query = editText.getText().toString();
        if (query.equals("")) {
          Toast.makeText(MainActivity.this, "Pleae Type Something!", Toast.LENGTH_SHORT).show();
        }
        else {
          int user_or_repo = radioGroup.getCheckedRadioButtonId();
          if(user_or_repo == R.id.search_user){
            Intent intent = new Intent(MainActivity.this, UserSearchResultActivity.class);
            int sort = user_radioGroup.getCheckedRadioButtonId();
            if(sort == R.id.sort_repo) {
              if(user_asc_switch.isChecked()){
                String url = "https://api.github.com/search/users?q="+query+"&sort=repositories&order=asc";
                intent.putExtra("url", url);
                startActivity(intent);
              }
              else {
                String url = "https://api.github.com/search/users?q="+query+"&sort=repositories";
                intent.putExtra("url", url);
                startActivity(intent);
              }
            }
            else if(sort == R.id.sort_follower) {
              if(user_asc_switch.isChecked()){
                String url = "https://api.github.com/search/users?q="+query+"&sort=followers&order=asc";
                intent.putExtra("url", url);
                startActivity(intent);
              }
              else {
                String url = "https://api.github.com/search/users?q="+query+"&sort=followers";
                intent.putExtra("url", url);
                startActivity(intent);
              }
            }
            else {
              String url = "https://api.github.com/search/users?q="+query;
              intent.putExtra("url", url);
              startActivity(intent);
            }
          }
          else {
            int sort = repo_radioGroup.getCheckedRadioButtonId();
            Intent intent = new Intent(MainActivity.this, RepoSearchResultActivity.class);
            if(sort == R.id.sort_star) {
              if(repo_asc_switch.isChecked()){
                String url = "https://api.github.com/search/repositories?q="+query+"&sort=stars&order=asc";
                intent.putExtra("url", url);
                startActivity(intent);
              }
              else {
                String url = "https://api.github.com/search/repositories?q="+query+"&sort=stars";
                intent.putExtra("url", url);
                startActivity(intent);
              }
            }
            else if(sort == R.id.sort_fork) {
              if(repo_asc_switch.isChecked()){
                String url = "https://api.github.com/search/repositories?q="+query+"&sort=forks&order=asc";
                intent.putExtra("url", url);
                startActivity(intent);
              }
              else {
                String url = "https://api.github.com/search/repositories?q="+query+"&sort=forks";
                intent.putExtra("url", url);
                startActivity(intent);
              }
            }
            else {
              String url = "https://api.github.com/search/repositories?q="+query;
              intent.putExtra("url", url);
              startActivity(intent);
            }
          }
        }
      }
    });

    /*
     * Shared Preferences
     */
    final SharedPreferences mSharedPreferences = this.getSharedPreferences(TAG, MODE_PRIVATE);
    final SharedPreferences.Editor mEditor = mSharedPreferences.edit();

    /*
     * Retreive Github data
     */
    String url = "https://api.github.com/user?access_token=9969557f16050f2475ed4515f9834e88150547c6";
    queue = AppSingleton.getInstance(this).getRequestQueue();

    StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, url,
        new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            Log.d("qwert", response);
            try {
              JSONObject jsonObject = new JSONObject(response);
              mEditor.putString("avatar_url", jsonObject.getString("avatar_url"));
              mEditor.putString("repos_url", jsonObject.getString("repos_url"));
              mEditor.putString("name", jsonObject.getString("name"));
              mEditor.putString("username", jsonObject.getString("login"));
              mEditor.putString("bio", jsonObject.getString("bio"));
              mEditor.putString("website", jsonObject.getString("blog"));
              mEditor.putString("email", jsonObject.getString("email"));
              mEditor.putString("repo_count", jsonObject.getString("public_repos"));
              mEditor.putString("following", jsonObject.getString("following"));
              mEditor.putString("followers", jsonObject.getString("followers"));
              mEditor.putString("created_date", jsonObject.getString("created_at"));
              mEditor.putString("updated_date", jsonObject.getString("updated_at"));
              mEditor.putString("followers_url", jsonObject.getString("followers_url"));
              mEditor.putString("following_url", jsonObject.getString("following_url"));
              mEditor.putString("starred_url", jsonObject.getString("starred_url"));
              mEditor.putString("starred_repos", "");
              mEditor.apply();
              //Log.d("qwert", jsonObject.getString("following_url"));
            } catch (JSONException e) {
              e.printStackTrace();
              Log.e("qwert", e.getMessage());
            }

          }
        }, new ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        if(Log.isLoggable("Volley Error", Log.ERROR)) Log.e("Volley Error", error.getMessage());
      }
    });
    jsonObjectRequest.setTag(NETWORK_TAG);
    AppSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    mEditor.apply();

    /*
     * Find starred repositories
     */
    String starred_url;
    String ss = mSharedPreferences.getString("starred_url", null);
    if(ss == null) {
      starred_url = "";
    }
    else {
      starred_url = mSharedPreferences.getString("starred_url", null).split("\\{")[0];
    }
    Log.d("poiu", starred_url);
    StringRequest starredRequest = new StringRequest(Request.Method.GET, starred_url,
        new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            try {
              JSONArray jsonArray = new JSONArray(response);
              for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String name = jsonObject.getString("name");
                String s = mSharedPreferences.getString("starred_repos", "");
                mEditor.putString("starred_repos", s+", "+name);
                mEditor.apply();
              }
              mEditor.apply();
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
    starredRequest.setTag(NETWORK_TAG);
    AppSingleton.getInstance(this).addToRequestQueue(starredRequest);
    queue.getCache().clear();

    String s = mSharedPreferences.getString("starred_repos", "nothing");
    Log.d("poiu", s);

    /*
     * Bottom Navigation Bar
     */
    BottomNavigationView mNavigationView = findViewById(R.id.main_navbar);
    mNavigationView.setOnNavigationItemSelectedListener
        (new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
              case R.id.navbar_profile:
                selectedFragment = ProfileFragment.newInstance();
                break;
              case R.id.navbar_repo:
                selectedFragment = RepositoryFragment.newInstance();
                break;
              case R.id.navbar_following:
                selectedFragment = FollowingFragment.newInstance();
                break;
              case R.id.navbar_follower:
                selectedFragment = FollowerFragment.newInstance();
                break;
              case R.id.navbar_notifications:
                selectedFragment = NotificationFragment.newInstance();
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_fl, selectedFragment);
            transaction.commit();
            return true;
          }
        });

    // Manually displays the first fragment - one time only
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.main_fl, ProfileFragment.newInstance());
    transaction.commit();
  }
}
