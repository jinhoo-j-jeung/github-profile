package jinhoo.com.githubprofile;

import static jinhoo.com.githubprofile.MainActivity.NETWORK_TAG;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jinhoo.com.githubprofile.network_util.AppSingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RepoDataActivity extends AppCompatActivity {
  RequestQueue queue;
  List<DataEntry> data = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_repodata);

    Bundle bundle = getIntent().getExtras();
    final String name = bundle.getString("name");
    final String owner = bundle.getString("owner");

    //getSupportActionBar().setTitle(owner+"/"+name);

    queue = AppSingleton.getInstance(this).getRequestQueue();

    /*
     * Retreive Weekly Commit Data
     */
    String stat_url = "https://api.github.com/repos/"+owner+"/"+name+"/stats/participation";
    StringRequest statRequest = new StringRequest(Request.Method.GET, stat_url,
        new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            try {
              JSONObject jsonObject = new JSONObject(response);
              JSONArray jsonArray = jsonObject.getJSONArray("all");
              for(int i = 32; i < jsonArray.length(); i++) {
                int value = jsonArray.getInt(i);
                data.add(new ValueDataEntry(String.valueOf(i-31), value));
              }
              Log.d("puui", String.valueOf(data.size()));
              if(data.size() != 0) {
                AnyChartView anyChartView = findViewById(R.id.any_chart_view);
                Cartesian cartesian = AnyChart.column();
                Column column = cartesian.column(data);
                column.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0d)
                    .offsetY(5d)
                    .format("{%Value}{groupsSeparator: }");

                cartesian.animation(true);
                cartesian.title("The # of Commits in Last 20 Weeks");

                cartesian.yScale().minimum(0d);

                //cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

                cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
                cartesian.interactivity().hoverMode(HoverMode.BY_X);

                cartesian.xAxis(0).title("Week");
                cartesian.yAxis(0).title("Commit");
                anyChartView.setChart(cartesian);
              }
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
    statRequest.setTag(NETWORK_TAG);
    AppSingleton.getInstance(this).addToRequestQueue(statRequest);

    Button button = findViewById(R.id.contributor_bt);
    button.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(RepoDataActivity.this, RepoData2Activity.class);
        intent.putExtra("name", name);
        intent.putExtra("owner", owner);
        startActivity(intent);
      }
    });
  }
}
