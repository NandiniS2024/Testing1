package com.cgi.portfoliometricsreport.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import com.google.gson.Gson;

/**
 *
 * @author Sangita Roy
 * @date Nov 26,2019
 *
 */

public class HttpConnector {

	private static final String USER_AGENT = "Mozilla/5.0";
	private final String ENCODING = Base64.getEncoder().encodeToString(("admin:sqcoe").getBytes());

	private static final String UserAgent = "User-Agent";
	private static final String AUTHORIZATION = "Authorization";
	private static final String CHARSET = "UTF-8";

	// HTTP GET request
	public String sendGet(String url) throws IOException {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();


		con.setRequestMethod("GET");
		con.setRequestProperty(UserAgent, USER_AGENT);
		con.setRequestProperty(AUTHORIZATION, "Basic " + ENCODING);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuilder response = new StringBuilder();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}

		in.close();

		return response.toString();

	}
	
	public String sendGet(String url, String password) throws IOException {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		String auth = "admin:" + password;

		String encoding = Base64.getEncoder().encodeToString((auth).getBytes());

		con.setRequestMethod("GET");
		con.setRequestProperty(UserAgent, USER_AGENT);
		con.setRequestProperty(AUTHORIZATION, "Basic " + encoding);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuilder response = new StringBuilder();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}

		in.close();

		return response.toString();

	}

	public Object sendPost(String url, String urlParameters, boolean returnResponse) throws IOException {
		Response res = new Response();
		res.setStatus("Failure");
		res.setMessage("There was some error");

		String result = "Error";
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("POST");
			con.setRequestProperty(UserAgent, USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setRequestProperty("Accept-Charset", CHARSET);
			con.setRequestProperty  (AUTHORIZATION, "Basic " + ENCODING);

			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			byte[] data = urlParameters.getBytes(CHARSET);
			wr.write(data);
			wr.flush();
			wr.close();

			InputStream is = null;

			if(con.getResponseCode() == HttpURLConnection.HTTP_OK) { //Success
				is = con.getInputStream();
				BufferedReader in = new BufferedReader(new InputStreamReader(is, CHARSET));
				String inputLine;
				StringBuilder response = new StringBuilder();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				result = response.toString();
				res = new Gson().fromJson(result, Response.class);

			} else if(con.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT) { //Failure
				res.setMessage("There is no Content");
			} else if(con.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) { //Failure
				res.setMessage("It's bad request.");
			} else if(con.getResponseCode() == HttpURLConnection.HTTP_FORBIDDEN) { //Failure
				res.setMessage("The connection is forbidden.");
			} else if(con.getResponseCode() == HttpURLConnection.HTTP_INTERNAL_ERROR) { //Failure
				res.setMessage("There is some internal error.");
			} else if(con.getResponseCode() == HttpURLConnection.HTTP_NOT_ACCEPTABLE) { //Failure
				res.setMessage("The request is not acceptable.");
			} else if(con.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) { //Failure
				res.setMessage("The connection is unauthorized.");
			} else if(con.getResponseCode() == HttpURLConnection.HTTP_PARTIAL) { //Failure
				res.setMessage("The content is partial.");
			}

		} catch (Exception e) {
			result = "Error";
			res.setMessage("");
		}

		if(returnResponse) {
			return res;
		} else {
			return result;
		}
	}

	// HTTP POST request
	public String sendPost(String url, String urlParameters) throws IOException {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty(UserAgent, USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Accept-Charset", CHARSET);
		con.setRequestProperty  (AUTHORIZATION, "Basic " + ENCODING);

		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		byte[] data = urlParameters.getBytes(CHARSET);
		wr.write(data);
		wr.flush();
		wr.close();

		InputStream is = null;

		if(con.getResponseCode() <= 400) {
			is = con.getInputStream();
		} else {
			is = con.getErrorStream();
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(is, CHARSET));
		String inputLine;
		StringBuilder response = new StringBuilder();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();
	}
}
