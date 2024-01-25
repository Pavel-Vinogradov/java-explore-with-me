package ru.practicum.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.RequestStatsDto;
import ru.practicum.dto.ResponseStatsDto;

import java.util.List;
import java.util.Map;

@Component
public class Client extends BaseClient {

    public Client(@Value("${explore-with-me-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }

    public List<ResponseStatsDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );
        return (List<ResponseStatsDto>) get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters).getBody();
    }

    public RequestStatsDto postStat(RequestStatsDto statsDto) {
        return (RequestStatsDto) post("/hit", statsDto).getBody();
    }
}
