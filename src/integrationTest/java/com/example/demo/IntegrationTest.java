package com.example.demo;

import org.junit.After;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class IntegrationTest {

    private static GenericContainer simpleWebServer = new GenericContainer("demo:0.0.1-SNAPSHOT")
            .withExposedPorts(8888);

    @Test
    void test() throws Exception {
        simpleWebServer.start();
        String address = "http://"
                + simpleWebServer.getContainerIpAddress()
                + ":" + simpleWebServer.getMappedPort(8888) + "/test";
        String response = simpleGetRequest(address);
        assertEquals(response, "test1");
    }

    @After
    public void after() {
        simpleWebServer.stop();
    }

    private String simpleGetRequest(String address) throws Exception {
        URL url = new URL(address);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        return content.toString();
    }

}
