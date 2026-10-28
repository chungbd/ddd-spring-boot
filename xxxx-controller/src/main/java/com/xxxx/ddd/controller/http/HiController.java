package com.xxxx.ddd.controller.http;

import com.xxxx.ddd.application.service.event.EventAppService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;

@RestController
@RequestMapping("/hello")
public class HiController {
    @Autowired
    private EventAppService eventAppService;

    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/hi")
    @RateLimiter(name = "backendA", fallbackMethod = "fallbackHelloWorld")
    public String helloWorld() {
        return eventAppService.sayHi("Alice");
    }

    public String fallbackHelloWorld(Throwable t) {
        return "Fallback: Hello, World!";
    }

    @GetMapping("/hi/v1")
    @RateLimiter(name = "backendB", fallbackMethod = "fallbackHelloWorld")
    public String helloWorldV1() {
        return eventAppService.sayHi("Bob");
    }

    private static final SecureRandom RANDOM = new SecureRandom();
    @GetMapping("circuit/breaker")
    @CircuitBreaker(name = "default", fallbackMethod = "fallbackCircuitBreaker")
    public String circuitBreaker() {
        int productId = RANDOM.nextInt(20) + 1; // Example product ID
        String url = "https://fakestoreapi.com/products/" + productId;
        return restTemplate.getForObject(url, String.class);
    }

    public String fallbackCircuitBreaker(Throwable t) {
        return t.getMessage();
    }
}
