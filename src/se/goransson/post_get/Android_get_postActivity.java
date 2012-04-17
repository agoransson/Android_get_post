package se.goransson.post_get;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Android_get_postActivity extends Activity {
	TextView get_value, get_time;
	TextView post_value, post_time;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		get_value = (TextView) findViewById(R.id.get_value);
		get_time = (TextView) findViewById(R.id.get_time);

		post_value = (TextView) findViewById(R.id.post_value);
		post_time = (TextView) findViewById(R.id.post_time);

		try {
			parse_response(doGet());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			parse_response(doPost());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void parse_response(String json) throws JSONException {
		JSONObject root = new JSONObject(json);

		int value = root.getInt("value");
		String time = root.getString("time");

		if (root.getString("method").equals("get")) {
			get_value.setText(Integer.toString(value));
			get_time.setText(time);
		} else if (root.getString("method").equals("post")) {
			post_value.setText(Integer.toString(value));
			post_time.setText(time);
		}

	}

	private String doPost() throws ClientProtocolException, IOException,
			URISyntaxException {
		// En ny httpclient
		HttpClient client = new DefaultHttpClient();

		String url = "http://195.178.232.26/android.php";

		// Skapa metoden (GET)
		HttpPost post_request = new HttpPost(new URI(url));

		// Skapa params
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("value", "123"));
		nameValuePairs.add(new BasicNameValuePair("method", "post"));

		// Lägg till params till post
		post_request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		// Skicka request och få svar från servern
		HttpResponse response = client.execute(post_request);

		// Vi behöver en inputstream för att skapa en reader
		InputStream is = response.getEntity().getContent();
		InputStreamReader isreader = new InputStreamReader(is);
		BufferedReader reader = new BufferedReader(isreader);

		// Buffer för svaret
		StringBuffer sb = new StringBuffer();

		// Läs av hela svaret, rad för rad
		String line = "";
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}

		return sb.toString();
	}

	private String doGet() throws URISyntaxException, ClientProtocolException,
			IOException {
		// En ny httpclient
		HttpClient client = new DefaultHttpClient();

		String url = "http://195.178.232.26/android.php";

		// Lägg till params
		if (!url.endsWith("?"))
			url += "?";
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("value", "456"));
		params.add(new BasicNameValuePair("method", "get"));
		String paramString = URLEncodedUtils.format(params, "utf-8");
		url += paramString;

		// Skapa metoden (GET)
		HttpGet get_request = new HttpGet();

		// Peka på rätt skript
		get_request.setURI(new URI(url));

		// Skicka request och få svar från servern
		HttpResponse response = client.execute(get_request);

		// Vi behöver en inputstream för att skapa en reader
		InputStream is = response.getEntity().getContent();
		InputStreamReader isreader = new InputStreamReader(is);
		BufferedReader reader = new BufferedReader(isreader);

		// Buffer för svaret
		StringBuffer sb = new StringBuffer();

		// Läs av hela svaret, rad för rad
		String line = "";
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}

		return sb.toString();
	}
}