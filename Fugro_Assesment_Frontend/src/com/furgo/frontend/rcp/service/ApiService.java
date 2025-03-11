package com.furgo.frontend.rcp.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

import org.eclipse.e4.core.di.annotations.Creatable;

import com.furgo.frontend.rcp.model.Location;
import com.furgo.frontend.rcp.model.Sample;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;


import ogsfrontend.LocalDateAdapter;

@Creatable
public class ApiService {
	private static final String BASE_URL = "http://localhost:8080/samples";
	private static final String USERNAME = "admin";  // Replace with actual credentials
    private static final String PASSWORD = "secret";

    private static String encodeCredentials() {
        String credentials = USERNAME + ":" + PASSWORD;
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
    }
    
	private final Gson gson = new GsonBuilder()
		    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())  // Register LocalDate Adapter
		    .create();

	public List<Sample> fetchSamples() throws IOException {
		URL url = new URL(BASE_URL + "/list");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		
		// Set Authorization Header
        conn.setRequestProperty("Authorization", encodeCredentials());

		InputStreamReader reader = new InputStreamReader(conn.getInputStream());
		
		List<Sample> data = gson.fromJson(reader, new TypeToken<List<Sample>>() {
		}.getType());
		
		
		return data;
	}
	
	public List<Location> fetchLocations() throws IOException{
		URL url = new URL(BASE_URL + "/allLocations");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		
		// Set Authorization Header
        conn.setRequestProperty("Authorization", encodeCredentials());

		InputStreamReader reader = new InputStreamReader(conn.getInputStream());
		List<Location> data = gson.fromJson(reader, new TypeToken<List<Location>>() {
		}.getType());
		return data;
		
	}

	public void addSample(Sample sample) throws IOException {
		URL url = new URL(BASE_URL+ "/add");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoOutput(true);
		// Set Authorization Header
        conn.setRequestProperty("Authorization", encodeCredentials());
		String jsonInput = gson.toJson(sample);
		try (OutputStream os = conn.getOutputStream()) {
			byte[] input = jsonInput.getBytes("utf-8");
			os.write(input, 0, input.length);
		}

		conn.getResponseCode(); // Execute request
	}
	
	public int editSample(Sample sample) throws IOException {		
		URL url = new URL(BASE_URL + "/update/" + sample.getId());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// Set Authorization Header
        conn.setRequestProperty("Authorization", encodeCredentials());
		conn.setRequestMethod("PUT");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoOutput(true);
		String jsonInput = gson.toJson(sample);
		try (OutputStream os = conn.getOutputStream()) {
			byte[] input = jsonInput.getBytes("utf-8");
			os.write(input, 0, input.length);
		}
		return conn.getResponseCode();
	}

	public void deleteSample(int id) throws IOException {
		URL url = new URL(BASE_URL + "/delete/" + id);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("DELETE");
		// Set Authorization Header
        conn.setRequestProperty("Authorization", encodeCredentials());
		conn.getResponseCode();
	}
	
	public double getAverageWaterContent() throws IOException {   
	    URL url = new URL(BASE_URL + "/avgwatercontent");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		
		// Set Authorization Header
        conn.setRequestProperty("Authorization", encodeCredentials());

        int responseCode = conn.getResponseCode();
        
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                // Parse JSON response
                //JSONObject json = new JSONObject(response.toString());
                JsonElement jsonElement = JsonParser.parseString(response.toString());
                return jsonElement.getAsDouble();
            }
        } else {
            throw new IOException("Failed to fetch average water content. HTTP response code: " + responseCode);
        }
	}

	public List<Sample> fetchThresholdSamples() throws IOException {
		URL url = new URL(BASE_URL + "/thresholdvalues");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		
		// Set Authorization Header
        conn.setRequestProperty("Authorization", encodeCredentials());

		InputStreamReader reader = new InputStreamReader(conn.getInputStream());
		
		List<Sample> data = gson.fromJson(reader, new TypeToken<List<Sample>>() {
		}.getType());
		
		
		return data;
	}
}
