package com.framework.until.okhttp3.clientconfig;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

public class ClientConfig {

	void new1(){
		int cacheSize = 10 * 1024 * 1024; // 10 MiB
		File cacheDirectory = new File("/path/", "OkHttpCache");
		Cache cache = new Cache(cacheDirectory, cacheSize);

		OkHttpClient client = new OkHttpClient.Builder()
		        .connectTimeout(60, TimeUnit.SECONDS)//连接超时时间
		        .readTimeout(60, TimeUnit.SECONDS)//读的时间
		        .writeTimeout(60, TimeUnit.SECONDS)//写的时间
		        .cache(cache)//配置缓存
		        .build();
	}
	

	void new2(){
		OkHttpClient client = new OkHttpClient();

		//读的时间设置为500ms
		OkHttpClient copy = client.newBuilder()
		                          .readTimeout(500, TimeUnit.MILLISECONDS)
		                          .build();

		//读的时间设置为3000ms
		OkHttpClient copy2 = client.newBuilder()
		                          .readTimeout(3000, TimeUnit.MILLISECONDS)
		                          .build();
	}
	
}
