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
import com.anychart.enums.Align;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.LegendLayout;
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

public class RepoData2Activity extends AppCompatActivity {
  RequestQueue queue;
  List<DataEntry> data = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_repodata2);

    Bundle bundle = getIntent().getExtras();
    final String name = bundle.getString("name");
    final String owner = bundle.getString("owner");

    queue = AppSingleton.getInstance(this).getRequestQueue();

    /*
     * Retreive Contributions data
     */
    String contributor_url = "https://api.github.com/repos/"+owner+"/"+name+"/contributors";
    StringRequest contributorRequest = new StringRequest(Request.Method.GET, contributor_url,
        new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            try {
              JSONArray jsonArray = new JSONArray(response);
              for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String username = jsonObject.getString("login");
                String contributions = jsonObject.getString("contributions");
                data.add(new ValueDataEntry(username, Integer.valueOf(contributions)));
              }
              AnyChartView anyChartView2 = findViewById(R.id.any_chart_view2);
              Pie pie = AnyChart.pie();
              pie.data(data);
              pie.title("Contributors");
              anyChartView2.setChart(pie);

            } catch (JSONException e) {
              e.printStackTrace();
            }
          }
        }, new ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        if(Log.isLoggable("Volley Error", Log.ERROR)) Log.e("Volley Error", error.getMessage());
      }
    })
    {
      @Override
      public Map<String,String> getHeaders() {
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","token 9969557f16050f2475ed4515f9834e88150547c6");
        return headers;
      }};
    contributorRequest.setTag(NETWORK_TAG);
    AppSingleton.getInstance(this).addToRequestQueue(contributorRequest);

  }
}
