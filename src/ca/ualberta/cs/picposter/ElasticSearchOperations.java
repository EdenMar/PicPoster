package ca.ualberta.cs.picposter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;
import ca.ualberta.cs.picposter.model.PicPostModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class ElasticSearchOperations
{

	private static Gson gson = new Gson();
	private static HttpClient client = new DefaultHttpClient();
	
	public static void pushPicPostModel(final PicPostModel model) {
		Thread thread = new Thread() {
					
			
			
			@Override
			public void run() {
				//Gson gson = new Gson();
				//HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost("http://cmput301.softwareprocess.es:8080/testing/emar/");
				
				try
				{
					String jsonString = gson.toJson(model);
					request.setEntity(new StringEntity(jsonString));
					HttpResponse response = client.execute(request);
					response.getStatusLine().toString();
					
					Log.w("ElasticSearch", response.getStatusLine().toString());
					HttpEntity entity = response.getEntity();
					
					BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
					String output = reader.readLine();
					while (output != null) {
						Log.w("ElasticSearch", output);
						output = reader.readLine();
					}
						
				} 
				
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		};
		
		thread.start();
	}
	
	public void searchPicPost(String str) throws ClientProtocolException, IOException {
		HttpGet searchRequest = new HttpGet("http://cmput301.softwareprocess.es:8080/testing/emar/_search?pretty=1&q=" +
				java.net.URLEncoder.encode(str,"UTF-8"));
		searchRequest.setHeader("Accept","application/json");
		HttpResponse response = client.execute(searchRequest);
		String status = response.getStatusLine().toString();
		System.out.println(status);

		String json = getEntityContent(response);

		Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<PicPostModel>>(){}.getType();
		ElasticSearchSearchResponse<PicPostModel> esResponse = gson.fromJson(json, elasticSearchSearchResponseType);
		System.err.println(esResponse);
		for (ElasticSearchResponse<PicPostModel> m : esResponse.getHits()) {
			PicPostModel model = m.getSource();
			System.err.println(model);
		}
		try {
			EntityUtils.consume(searchRequest);
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
			
		searchRequest.releaseConnection();
	}
	
	String getEntityContent(HttpResponse response) throws IOException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader((response.getEntity().getContent())));
		String output;
		System.err.println("Output from Server -> ");
		String json = "";
		while ((output = br.readLine()) != null) {
			System.err.println(output);
			json += output;
		}
		System.err.println("JSON:"+json);
		return json;
}
	
}
