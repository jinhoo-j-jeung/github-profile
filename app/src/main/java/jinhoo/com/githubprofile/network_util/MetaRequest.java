package jinhoo.com.githubprofile.network_util;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;

public class MetaRequest extends JsonObjectRequest {

  public MetaRequest(int method, String url, JSONObject jsonRequest, Response.Listener
      <JSONObject> listener, Response.ErrorListener errorListener) {
    super(method, url, jsonRequest, listener, errorListener);
  }

  @Override
  protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
    try {
      String jsonString = new String(response.data,
          HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
      JSONObject jsonResponse = new JSONObject();
      jsonResponse.put("headers", new JSONObject(response.headers));
      return Response.success(jsonResponse,
          HttpHeaderParser.parseCacheHeaders(response));
    } catch (UnsupportedEncodingException e) {
      return Response.error(new ParseError(e));
    } catch (JSONException je) {
      return Response.error(new ParseError(je));
    }
  }
}
