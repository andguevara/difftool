package com.aguevara.difftool.integration;

import com.aguevara.difftool.entities.DiffEntity;
import com.aguevara.difftool.model.Result;
import com.aguevara.difftool.model.StringEncoded;
import com.aguevara.difftool.services.DiffResult;
import com.aguevara.difftool.services.DiffService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DiffControllerTests {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    DiffService diffService;

    final Result expectedResultEqual = new Result("Strings are equal", DiffResult.EQUAL);
    final Result expectedResultDifferentSize = new Result("Strings are not of equal size",
            DiffResult.SIZE_NOT_EQUAL);
    final Result expectedResultDifferentContent = new Result("The Strings differ and the first difference is at offset 2" +
                                " and the different characters are: mp", DiffResult.DIFFERENT);
    final Result resultNotReady = new Result("you have not submitted all the necessary strings", DiffResult.INCOMPLETE);

    final RestTemplate restTemplate = new RestTemplate();

    @Test
    public void testDiffLeftOk() throws URISyntaxException {
        testDiff("left", 1L);
    }

    @Test
    public void testDiffRightOk() throws URISyntaxException {
        testDiff("right", 2L);
    }

    @Test(expected = HttpClientErrorException.class)
    public void testGetEndpointBadRequest() throws URISyntaxException {
        final String diffUrl = "http://localhost:" + randomServerPort + "/v1/diff/3/";
        URI uri = new URI(diffUrl);

        ResponseEntity<Result> result = restTemplate.getForEntity(uri, Result.class);

        assertEquals("Returned status should be 400", 400, result.getStatusCodeValue());
    }

    @Test
    public void testGetEndpointResultEqual() {
        final String diffLeftUrl = "http://localhost:" + randomServerPort + "/v1/diff/4/left";
        final String diffRightUrl = "http://localhost:" + randomServerPort + "/v1/diff/4/right";
        final String diffGet = "http://localhost:" + randomServerPort + "/v1/diff/4/";

        String base64EncodedString = "dGVzdA==";
        StringEncoded requestBody = new StringEncoded();
        requestBody.setBase64encoded(base64EncodedString);

        restTemplate.postForEntity(diffLeftUrl, requestBody, DiffEntity.class);
        restTemplate.postForEntity(diffRightUrl, requestBody, DiffEntity.class);

        ResponseEntity<Result> result = restTemplate.getForEntity(diffGet, Result.class);

        assertEquals("Returned status should be 200", 200, result.getStatusCodeValue());
        assertEquals("Returned result should be equals", expectedResultEqual, result.getBody());
    }

    @Test
    public void testGetEndpointResultDifferentSize() {
        final String diffLeftUrl = "http://localhost:" + randomServerPort + "/v1/diff/5/left";
        final String diffRightUrl = "http://localhost:" + randomServerPort + "/v1/diff/5/right";
        final String diffGet = "http://localhost:" + randomServerPort + "/v1/diff/5/";

        String base64EncodedString1 = "dGVzdA==";
        String base64EncodedString2 = "dGVzdDI=";
        StringEncoded requestBody1 = new StringEncoded();
        requestBody1.setBase64encoded(base64EncodedString1);
        StringEncoded requestBody2 = new StringEncoded();
        requestBody2.setBase64encoded(base64EncodedString2);

        restTemplate.postForEntity(diffLeftUrl, requestBody1, DiffEntity.class);
        restTemplate.postForEntity(diffRightUrl, requestBody2, DiffEntity.class);

        ResponseEntity<Result> result = restTemplate.getForEntity(diffGet, Result.class);

        assertEquals("Returned status should be 200", 200, result.getStatusCodeValue());
        assertEquals("Returned result should be equals", expectedResultDifferentSize, result.getBody());
    }

    @Test
    public void testGetEndpointResultDifferentContent() {
        final String diffLeftUrl = "http://localhost:" + randomServerPort + "/v1/diff/6/left";
        final String diffRightUrl = "http://localhost:" + randomServerPort + "/v1/diff/6/right";
        final String diffGet = "http://localhost:" + randomServerPort + "/v1/diff/6/";

        String base64EncodedString1 = "dGVzdA==";
        String base64EncodedString2 = "dGVtcA==";
        StringEncoded requestBody1 = new StringEncoded();
        requestBody1.setBase64encoded(base64EncodedString1);
        StringEncoded requestBody2 = new StringEncoded();
        requestBody2.setBase64encoded(base64EncodedString2);

        restTemplate.postForEntity(diffLeftUrl, requestBody1, DiffEntity.class);
        restTemplate.postForEntity(diffRightUrl, requestBody2, DiffEntity.class);

        ResponseEntity<Result> result = restTemplate.getForEntity(diffGet, Result.class);

        assertEquals("Returned status should be 200", 200, result.getStatusCodeValue());
        assertEquals("Returned result should be equals", expectedResultDifferentContent, result.getBody());
    }

    @Test(expected = HttpClientErrorException.class)
    public void testGetEndpointIncompleteInfo() {
        final String diffGetNotReady = "http://localhost:" + randomServerPort + "/v1/diff/7/";

        ResponseEntity<Result> result = restTemplate.getForEntity(diffGetNotReady, Result.class);
        assertEquals("Returned status should be 400", 400, result.getStatusCodeValue());
    }

    @Test(expected = HttpClientErrorException.class)
    public void testGetEndpointIncompleteInfoJustOneString() {
        final String diffGetNotReady = "http://localhost:" + randomServerPort + "/v1/diff/8/";
        final String diffLeftUrl = "http://localhost:" + randomServerPort + "/v1/diff/8/left";
        String base64EncodedString = "dGVzdA==";
        StringEncoded requestBody = new StringEncoded();
        requestBody.setBase64encoded(base64EncodedString);

        restTemplate.postForEntity(diffLeftUrl, requestBody, DiffEntity.class);

        ResponseEntity<Result> result = restTemplate.getForEntity(diffGetNotReady, Result.class);
        assertEquals("Returned status should be 400", 400, result.getStatusCodeValue());
    }

    private void testDiff(String direction, Long id) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();

        final String diffUrl = "http://localhost:" + randomServerPort + "/v1/diff/" + id + "/" + direction;
        URI uri = new URI(diffUrl);

        String base64EncodedString = "dGVzdA==";
        StringEncoded requestBody = new StringEncoded();
        requestBody.setBase64encoded(base64EncodedString);

        ResponseEntity<DiffEntity> result = restTemplate.postForEntity(uri, requestBody, DiffEntity.class);

        DiffEntity expectedResponse = new DiffEntity();
        expectedResponse.setDiffid(id);
        if (direction.equals("left")) {
            expectedResponse.setDiffleft(base64EncodedString);
        }
        else {
            expectedResponse.setDiffright(base64EncodedString);
        }

        assertEquals("Returned status code should be 200", 200, result.getStatusCodeValue());
        assertEquals("Response body should be a DiffEntity with an id and an argument",
                expectedResponse, result.getBody());
    }
}
