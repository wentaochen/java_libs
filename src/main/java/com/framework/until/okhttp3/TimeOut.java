package com.framework.until.okhttp3;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TimeOut {

	private static final OkHttpClient client = new OkHttpClient();

	//Names and values will be encoded using an HTML-compatible form URL encoding.
	public void runPost() throws Exception {
		RequestBody formBody = new FormBody.Builder().add("search",
				"Jurassic Park").build();
		Request request = new Request.Builder()
				.url("https://en.wikipedia.org/w/index.php").post(formBody)
				.build();

		Response response = client.newCall(request).execute();
		if (!response.isSuccessful())
			throw new IOException("Unexpected code " + response);

		System.out.println(response.body().string());
	}

	public void runHead() throws Exception {
		Request request = new Request.Builder()
				.url("https://api.github.com/repos/square/okhttp/issues")
				.header("User-Agent", "OkHttp Headers.java") // 覆盖
				.addHeader("Accept", "application/json; q=0.5") // 累加
				.addHeader("Accept", "application/vnd.github.v3+json").build();

		Response response = client.newCall(request).execute();
		if (!response.isSuccessful())
			throw new IOException("Unexpected code " + response);

		System.out.println("Server: " + response.header("Server"));
		System.out.println("Date: " + response.header("Date"));
		System.out.println("Vary: " + response.headers("Vary")); // List
	}

	public void runAsyn() throws Exception {
		Request request = new Request.Builder().url(
				"http://publicobject.com/helloworld.txt").build();

		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				e.printStackTrace();
			}

			@Override
			public void onResponse(Call call, Response response)
					throws IOException {
				if (!response.isSuccessful())
					throw new IOException("Unexpected code " + response);

				Headers responseHeaders = response.headers();
				for (int i = 0, size = responseHeaders.size(); i < size; i++) {
					System.out.println(responseHeaders.name(i) + ": "
							+ responseHeaders.value(i));
				}

				System.out.println(response.body().string());
			}
		});
	}

	/**
	 * if the response body is large (greater than 1 MiB), avoid string()
	 * because it will load the entire document into memory. In that case,
	 * prefer to process the body as a stream.
	 * 
	 * @throws Exception
	 */
	public void run() throws Exception {
		Request request = new Request.Builder().url(
				"http://publicobject.com/helloworld.txt").build();

		Response response = client.newCall(request).execute();
		if (!response.isSuccessful())
			throw new IOException("Unexpected code " + response);

		Headers responseHeaders = response.headers();
		for (int i = 0; i < responseHeaders.size(); i++) {
			System.out.println(responseHeaders.name(i) + ": "
					+ responseHeaders.value(i));
		}

		System.out.println(response.body().string());
	}

	public static void main(String[] args) throws IOException {
		OkHttpClient client = new OkHttpClient.Builder()
				.connectTimeout(10, TimeUnit.SECONDS)
				.writeTimeout(10, TimeUnit.SECONDS)
				.readTimeout(30, TimeUnit.SECONDS)
				.retryOnConnectionFailure(true).addInterceptor(new Interceptor() {
			        @Override
			        public Response intercept(Chain chain) throws IOException {
			            Request request = chain.request();

			            // try the request
			            Response response = chain.proceed(request);

			            int tryCount = 0;
			            while (!response.isSuccessful() && tryCount < 3) {

			               // Log.d("intercept", "Request is not successful - " + tryCount);
			            	System.out.println("intercept"+"Request is not successful - " + tryCount);
			                tryCount++;

			                // retry the request
			                response = chain.proceed(request);
			            }

			            // otherwise just pass the original response on
			            return response;
			        }
			    }).build();
		
		//client.interceptors().add(n);
		
		

		Request request = new Request.Builder().url(
				"http://localhost") // This URL is served with a 2
				.build();

		Response response = client.newCall(request).execute();
		System.out.println("Response completed: " + response);

		Headers responseHeaders = response.headers();
		for (int i = 0; i < responseHeaders.size(); i++) {
			System.out.println(responseHeaders.name(i) + ": "
					+ responseHeaders.value(i));
		}

	}

	// public static final MediaType MEDIA_TYPE_MARKDOWN
	// = MediaType.parse("text/x-markdown; charset=utf-8");
	//
	// private final OkHttpClient client = new OkHttpClient();
	//
	// public void run() throws Exception {
	// String postBody = ""
	// + "Releases\n"
	// + "--------\n"
	// + "\n"
	// + " * _1.0_ May 6, 2013\n"
	// + " * _1.1_ June 15, 2013\n"
	// + " * _1.2_ August 11, 2013\n";
	//
	// Request request = new Request.Builder()
	// .url("https://api.github.com/markdown/raw")
	// .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, postBody))
	// .build();
	//
	// Response response = client.newCall(request).execute();
	// if (!response.isSuccessful()) throw new IOException("Unexpected code " +
	// response);
	//
	// System.out.println(response.body().string());
	// }
}
