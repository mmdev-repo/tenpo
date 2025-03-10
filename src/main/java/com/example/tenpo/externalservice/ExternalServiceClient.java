package com.example.tenpo.externalservice;


import com.example.tenpo.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ExternalServiceClient {

    @Value("${retry.maxAttempts}")
    private String attempts;
    @Autowired
    private RedisRepository redisRepository;

    @Retryable(maxAttemptsExpression = "${retry.maxAttempts}")
    public int getValueFromExternalService() {
        System.out.print("----- Getting value from External Service -----");
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Integer> response = restTemplate.getForEntity("http://localhost:8081/mock-value", Integer.class);
        System.out.print(response);
        redisRepository.saveIntoRedis("externalValue", response.getBody().intValue());
        return response.getBody().intValue();
    }

    @Recover
    public int recoverFromFailure(Exception e) {
        System.out.println("error after " + attempts + " attempts. Looking into Redis...");
        return redisRepository.getIntFromRedis("externalValue");
    }
}