package com.showu.common.http;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HttpClient {
	private IResponse response;
	private List<HttpEngine> list = new ArrayList<HttpEngine>();

	public HttpClient(IResponse res) {
		// TODO Auto-generated constructor stub
		this.response = res;
	}

	public void get(String url) {
		HttpEngine task = new HttpEngine(response);
		list.add(task);
		task.executeOnExecutor(AsyncTask.DUAL_THREAD_EXECUTOR, 0, url);
	}

	public void post(String url, String urlParam) {
		HttpEngine task = new HttpEngine(response);
		list.add(task);
		task.executeOnExecutor(AsyncTask.DUAL_THREAD_EXECUTOR, 1, url,
				urlParam);
	}

	public void uploadFile(String url,File file){
		HttpEngine task = new HttpEngine(response);
		list.add(task);
		task.executeOnExecutor(AsyncTask.DUAL_THREAD_EXECUTOR, 2, url,
				file);
	}
	
	public void cancel() {
		for (HttpEngine task : list) {
			task.cancel(true);
		}
	}
}
