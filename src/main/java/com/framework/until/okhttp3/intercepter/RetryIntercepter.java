package com.framework.until.okhttp3.intercepter;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 假如设置为3次重试的话，则最大共请求4次（默认1次+3次重试）
 * 
 * @author chenwentao
 *
 */
public class RetryIntercepter implements Interceptor {

	// 最大重试次数
	public final int maxRetry;

	public RetryIntercepter(int maxRetry) {
		this.maxRetry = maxRetry;
	}

	@SuppressWarnings("resource")
	@Override
	public Response intercept(Chain chain) throws IOException {
		int retryNum = 0;
		// 第一次请求
		Request request = chain.request();
		Response response = chain.proceed(request);
		// 重试N次请求
		while (!response.isSuccessful() && retryNum < maxRetry) {
			retryNum++;
			response = chain.proceed(request);
		}

		return response;
	}
}
