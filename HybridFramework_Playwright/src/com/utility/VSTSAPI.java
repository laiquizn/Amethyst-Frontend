package com.utility;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class VSTSAPI {
	
	static ConfigReader config = new ConfigReader();	
	public static int updateTestCase(String plan_id, String suit_id, String point_id, String outcome ) {
String url="https://dev.azure.com/accentureciostg/TestingIndia_2791/_apis/test/Plans/#plan_id/Suites/#suit_id/points/#point_id?api-version=5.1";
//String url="https://dev.azure.com/accenturecio07/TestingIndia_2791/_apis/test/Plans/312445/Suites/312446/points/60083?api-version=5.1";
String token ="ZGVtbzpsNXc2anp0dnlhcDNzc3lpdWw2cDRoYmJuZjd6aDJhcG92NnRscHBkdTRkbGYybDVuZG1h";
		int result = 0;
		
		try {

		
			
			url = url.replace("#plan_id", plan_id);
			url = url.replace("#suit_id", suit_id);
			url = url.replace("#point_id", point_id);
			URL uri = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
			conn.setDoOutput(true);
			conn.setRequestProperty("X-HTTP-Method-Override", "PATCH");
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("authorization", "Basic " + token);

			String body = "{\"outcome\": \""+ outcome +"\"}";

			OutputStream os = conn.getOutputStream();
			os.write(body.getBytes());
			os.flush();

			//if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
			result = conn.getResponseCode();
				//throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			//}

		  } catch (IOException e) {
			e.printStackTrace();
			
		 }
		
		return result;
		
	}
}
	