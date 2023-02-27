package ibf2022.ssf.nasaapi.services;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import ibf2022.ssf.nasaapi.models.POTD;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class POTDService {

    @Value("${potd.url}")
    private String URL;

    @Value("${potd.key}")
    private String API_KEY;

    public Optional<POTD> getPOTD() throws ParseException {
        String url = UriComponentsBuilder
                .fromUriString(URL)
                .queryParam("api_key", API_KEY)
                .toUriString();

        RequestEntity<Void> req = RequestEntity.get(url)
                .accept(MediaType.APPLICATION_JSON)
                // .header("X-Api-Key", API_KEY)
                .build();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = null;

        String payload = "";
        int statusCode = 0;

        try {
            resp = restTemplate.exchange(req, String.class);
            payload = resp.getBody();
            statusCode = resp.getStatusCode().value();
        } catch (HttpClientErrorException ex) {
            payload = ex.getResponseBodyAsString();
            statusCode = ex.getStatusCode().value();
            return Optional.empty();
        } finally {
            System.out.printf("URL: %s\n", url);
            System.out.printf("Payload: %s\n", payload);
            System.out.printf("Status Code: %s\n", statusCode);
        }

        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject json = reader.readObject();

        Date date = new SimpleDateFormat("YYYY-MM-DD").parse(json.getString("date"));
        String title = json.getString("title");
        String explanation = json.getString("explanation");
        String hdUrl = json.getString("hdurl");

        POTD potd = new POTD();
        potd.setDate(date);
        potd.setTitle(title);
        potd.setExplanation(explanation);
        potd.setHdUrl(hdUrl);

        return Optional.of(potd);
    }
}
