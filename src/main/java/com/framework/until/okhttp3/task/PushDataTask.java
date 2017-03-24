package com.framework.until.okhttp3.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;

import com.framework.until.okhttp3.intercepter.RetryIntercepter;

public class PushDataTask implements Runnable {

	private final OkHttpClient client;

	public static final MediaType MediaType_Json = MediaType
			.parse("application/json; charset=utf-8");

	public volatile String url;

	public PushDataTask(String url) {
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		logging.setLevel(Level.BASIC);
		this.url = url;
		this.client = new OkHttpClient.Builder()
				// 连接超时时间
				.connectTimeout(5, TimeUnit.SECONDS)
				// 读的时间
				.readTimeout(5, TimeUnit.SECONDS)
				// 写的时间
				.writeTimeout(5, TimeUnit.SECONDS)
				// 网络重试
				.retryOnConnectionFailure(true).addInterceptor(logging)
				// 业务层重试?
				.addInterceptor(new RetryIntercepter(3)).build();
	}

	@Override
	public void run() {
		try {
			long begin = System.currentTimeMillis();
			for (int i = 0; i < 100; i++) {

				// String data =
				// "{\"mac\":\"112223344\",\"last_update_time\":\"20160530062749\",\"data\":000000,\"reserver\":\"\"}";
				// System.out.println(i+" "+HttpUtils.post(url, data, "UTF-8",
				// 5000,
				// 5000));

				try {
					String data = "{\"mac\":\"112223344\",\"last_update_time\":\"20160530062749\",\"data\":000000,\"reserver\":\"\"}";
					RequestBody body = RequestBody.create(MediaType_Json, data);
					Request request = new Request.Builder().url(url)
							.addHeader("Connection", "Keep-Alive").post(body)
							.build();
					// System.out.println(request.headers());
					Response response = client.newCall(request).execute();
					// System.out.println(i + " "
					// + client.connectionPool().connectionCount() + ""
					// + response.body().string());

					response.body().close();
					for (String name : response.headers().names()) {
						System.out.println(name + ":" + response.header(name));
					}
					System.out.println("------");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			System.out.println(System.currentTimeMillis() - begin);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		List result=new ArrayList();
		int i=0;
		final int M=1024*1024;
		System.out.println(Runtime.getRuntime().freeMemory()/M);  
	    System.out.println(Runtime.getRuntime().totalMemory()/M);  
	    System.out.println(Runtime.getRuntime().maxMemory()/M);  
	    
	    
		while(true){
			OkHttpClient client = new OkHttpClient.Builder()
			// 连接超时时间
					.connectTimeout(5, TimeUnit.SECONDS)
					// 读的时间
					.readTimeout(5, TimeUnit.SECONDS)
					// 写的时间
					.writeTimeout(5, TimeUnit.SECONDS)
					// 网络重试
					.retryOnConnectionFailure(true)
					// 业务层重试?
					.addInterceptor(new RetryIntercepter(3)).build();
			result.add(client);
			System.out.println(result.size());
			System.out.println(Runtime.getRuntime().freeMemory()/M);  
		    System.out.println(Runtime.getRuntime().totalMemory()/M);  
		    System.out.println(Runtime.getRuntime().maxMemory()/M);  
		}
		
		/*
		PushDataTask pd = new PushDataTask(
				"http://121.40.239.10:90/ovu_dhs/api/v1/data/");
		Thread thread = new Thread(pd);
		thread.start();
		*/
	}
}
