package ibf2022.ssf.nasaapi.services;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import ibf2022.ssf.nasaapi.models.Rover;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class RoverService {

    @Value("${rover.url}")
    private String URL;

    @Value("${potd.key}")
    private String API_KEY;

    public Optional<List<Rover>> getRoverPhoto() {
        String url = UriComponentsBuilder
                .fromUriString(URL)
                .queryParam("sol", 1000)
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
        JsonArray photosArray = json.getJsonArray("photos");

        String name;
        String fullName;
        String img;
        String earthDate;
        String roverName;
        String launchDate;
        String landingDate;
        String status;

        JsonObject camera;
        JsonObject rover;
        List<Rover> roverList = new LinkedList<>();

        for (int i = 0; i < photosArray.size(); i++) {
            Rover roverClass = new Rover();
            JsonObject data = photosArray.getJsonObject(i);
            img = data.getString("img_src");
            earthDate = data.getString("earth_date");
            camera = data.getJsonObject("camera");
            name = camera.getString("name");
            fullName = camera.getString("full_name");
            rover = data.getJsonObject("rover");
            roverName = rover.getString("name");
            launchDate = rover.getString("launch_date");
            landingDate = rover.getString("landing_date");
            status = rover.getString("status");

            roverClass.setName(name);
            roverClass.setFullName(fullName);
            roverClass.setEarthDate(earthDate);
            roverClass.setImgUrl(img);
            roverClass.setRoverName(roverName);
            roverClass.setLaunchDate(launchDate);
            roverClass.setLandingDate(landingDate);
            roverClass.setRoverName(roverName);
            roverClass.setStatus(status.substring(0, 1).toUpperCase() + status.substring(1, status.length()));

            roverList.add(roverClass);
        }

        return Optional.of(roverList);
    }
}
