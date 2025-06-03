package com.tenpo.externalservice;


import com.tenpo.repositoryredis.RedisRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Component
public class ExternalServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(ExternalServiceClient.class);

    @Value("${retry.maxAttempts}")
    private String attempts;

    @Value("${externalServiceUrl}")
    private String externalServiceUrl;

    @Autowired
    private RedisRepository redisRepository;

    @Retryable(maxAttemptsExpression = "${retry.maxAttempts}")
    public double getValueFromExternalService() {
        logger.info("----- Getting value from External Service -----");
        RestTemplate restTemplate = new RestTemplate();
        Random ran = new Random();
        int nxt = ran.nextInt(99);
        ResponseEntity<String> response = restTemplate.getForEntity(externalServiceUrl+nxt, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        double valueReturned = Double.parseDouble(jsonNode.get("id").asText());
        redisRepository.saveIntoRedis("externalValue", valueReturned);
        return valueReturned;
    }

    @Recover
    public double recoverFromFailure(Exception e) {
        logger.info("error after {} attempts. Looking into Redis...", attempts);
        return redisRepository.getExternalValueFromRedis("externalValue");
    }

}