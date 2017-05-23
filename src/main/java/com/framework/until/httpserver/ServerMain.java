package com.framework.until.httpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ServerMain {

	final static String response = "HTTP/1.0 200 OK"
			+ "Content-type: text/plain" + "Hello World";
	
	// 限制
	public static ExecutorService newBoundedFixedThreadPool(int nThreads, int capacity) {
	    return new ThreadPoolExecutor(nThreads, nThreads,
	        0L, TimeUnit.MILLISECONDS,
	        new LinkedBlockingQueue<Runnable>(capacity),
	        new ThreadPoolExecutor.AbortPolicy());
	}


	public static void main(String[] args) throws IOException {
		ServerSocket listener = new ServerSocket(8080);
		ExecutorService executor = newBoundedFixedThreadPool(2,1);
		try {
		    while (true) {
		        Socket socket = listener.accept();
		        executor.submit( new HandleRequestRunnable(socket) );
		    }
		} finally {
		    listener.close();
		}
	}

	public static void handleRequest(Socket socket) throws IOException {
		// Read the input stream, and return "200 OK"
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			System.out.println((in.readLine()));

			OutputStream out = socket.getOutputStream();
			out.write(response.getBytes(StandardCharsets.UTF_8));
		} finally {
			socket.close();
		}
	}

	public static class HandleRequestRunnable implements Runnable {
		final Socket socket;

		public HandleRequestRunnable(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				handleRequest(socket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}