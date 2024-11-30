package zerobase.weather.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@Slf4j
public class ApiService {

    @Value("${openweathermap.key}") // application.properties 값을 가져옴
    private String apiKey;

    public String getWeatherString() {
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather";
        String fullUrl = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("q", "seoul")
                .queryParam("appid", apiKey)
                .toUriString();

        try {
            URL url = new URL(fullUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    connection.getResponseCode() == 200 ?
                            connection.getInputStream() :
                            connection.getErrorStream()
            ))) {
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                return response.toString();
            }
        } catch (Exception e) {
            log.error("Error while calling weather API", e); // 수정된 부분
            return "failed to get response: " + e.getMessage();
        }
    }
}
