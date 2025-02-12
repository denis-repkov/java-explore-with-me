package ru.practicum.stat.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.stat.dto.CreateEndpointHitDto;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.ViewStatsDto;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsClient {

    private final RestTemplate restTemplate;

    @Autowired
    public StatisticsClient(
            @Value("${stats-server.url}") String serverUrl,
            RestTemplateBuilder builder) {
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build();
    }

    public List<ViewStatsDto> getStats(String start, String end, List<String> uris, boolean unique) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start);
        parameters.put("end", end);
        parameters.put("uris", uris == null ? "" : String.join(",", uris));
        parameters.put("unique", unique);

        ResponseEntity<String> response = restTemplate.getForEntity("/stats?start={start}&end={end}&uris={uris}&unique={unique}", String.class, parameters);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return Arrays.asList(objectMapper.readValue(response.getBody(), ViewStatsDto[].class));
        } catch (JsonProcessingException exception) {
            throw new ClientException("Client error: " + exception.getMessage());
        }
    }

    public void createRecord(CreateEndpointHitDto dto) {
        HttpEntity<CreateEndpointHitDto> requestEntity = new HttpEntity<>(dto, defaultHeaders());
        restTemplate.exchange("/hit", HttpMethod.POST, requestEntity, EndpointHitDto.class);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}