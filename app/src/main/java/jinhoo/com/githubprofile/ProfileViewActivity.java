package jinhoo.com.githubprofile;

import static jinhoo.com.githubprofile.MainActivity.NETWORK_TAG;
import static jinhoo.com.githubprofile.MainActivity.TAG2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
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
import jinhoo.com.githubprofile.fragments.ProfileFragment;
import jinhoo.com.githubprofile.fragments.ProfileViewRepositoryFragment;
import jinhoo.com.githubprofile.fragments.RepositoryFragment;
import jinhoo.com.githubprofile.network_util.AppSingleton;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileViewActivity extends AppCompatActivity {
  RequestQueue queue;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profileview);

    Toolbar mToolbar = findViewById(R.id.actionbar_user);
    setSupportActionBar(mToolbar);
    final TextView toolbar_title = findViewById(R.id.useractionbar_logo_tv);

    Bundle bundle = getIntent().getExtras();
    String url = bundle.getString("url");

    /*
     * Shared Preferences
     */
    SharedPreferences mSharedPreferences = this.getSharedPreferences(TAG2, MODE_PRIVATE);
    final SharedPreferences.Editor mEditor = mSharedPreferences.edit();

    /*
     * Retreive Github data
     */
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
              toolbar_title.setText(jsonObject.getString("name")+"'s Github");
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
    }){
      @Override
      public Map<String,String> getHeaders() {
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","token 9969557f16050f2475ed4515f9834e88150547c6");
        return headers;
      }};
    jsonObjectRequest.setTag(NETWORK_TAG);
    AppSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    mEditor.apply();

    /*
     * Bottom Navigation Bar
     */
    BottomNavigationView mNavigationView = findViewById(R.id.profileview_navbar);
    mNavigationView.setOnNavigationItemSelectedListener
        (new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
              case R.id.ou_navbar_profile:
                selectedFragment = ProfileFragment.newInstance(true);
                break;
              case R.id.ou_navbar_repo:
                selectedFragment = ProfileViewRepositoryFragment.newInstance(true);
                break;
              case R.id.ou_navbar_following:
                selectedFragment = FollowingFragment.newInstance(true);
                break;
              case R.id.ou_navbar_follower:
                selectedFragment = FollowerFragment.newInstance(true);
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.profileview_fl, selectedFragment);
            transaction.commit();
            return true;
          }
        });

    // Manually displays the first fragment - one time only
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.profileview_fl, ProfileFragment.newInstance(true));
    transaction.commit();
  }
}

