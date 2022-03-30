package com.binar.grab.junit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TestingLoginController {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void restTemplateLogin() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", "rikialdipari@gmail.com");
        map.add("password", "password");
        map.add("grant_type", "password");
        map.add("client_id", "my-client-web");
        map.add("client_secret", "password");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8082/api/oauth/token", request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println("response  =" + response.getBody());
    }

    @Test
    public void restTemplateLogin2() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "*/*");
        headers.set("Content-Type", "application/json");
        String bodyTesting = "{\n" +
                "    \"username\":\"rikialdipari@gmail.com\",\n" +
                "    \"password\":\"password\"\n" +
                "}";
        HttpEntity<String> entity = new HttpEntity<String>(bodyTesting, headers);

        ResponseEntity<String> exchange = restTemplate.exchange("http://localhost:8082/api/user-login/login", HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.OK, exchange.getStatusCode());
        System.out.println("response  =" + exchange.getBody());
    }
}
