package org.exemple.legacy.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

@RestController
public class InfoController {

    @Value("${server.port}")
    private int port;

    @GetMapping("/port")
    public int getPort() {
        return port;
    }

    @GetMapping("/sum")
    public long getSum() {
        return Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .parallel()          // ключевая модификация для ускорения
                .reduce(0, Integer::sum);
    }
}
