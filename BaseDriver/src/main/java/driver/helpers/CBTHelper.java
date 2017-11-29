package driver.helpers;

/**
 * Created by akruglyanskaya on 6/29/17.
 */

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import driver.BaseWebTest;

public class CBTHelper extends BaseWebTest {
    String testScore = "unset";

    public static JsonNode setScore(String seleniumTestId, String score) throws UnirestException {
// Mark a Selenium test as Pass/Fail
        HttpResponse response = Unirest.put("http://crossbrowsertesting.com/api/v3/selenium/{seleniumTestId}")
                .basicAuth(cbtUsername, cbtAuthKey)
                .routeParam("seleniumTestId", seleniumTestId)
                .field("action", "set_score")
                .field("score", score)
                .asJson();
        return (JsonNode) response.getBody();
    }

    public static String takeSnapshot(String seleniumTestId) throws UnirestException {
/*
* Takes a snapshot of the screen for the specified test.
* The output of this function can be used as a parameter for setDescription()
*/
        HttpResponse response = Unirest.post("http://crossbrowsertesting.com/api/v3/selenium/{seleniumTestId}/snapshots")
                .basicAuth(cbtUsername, cbtAuthKey)
                .routeParam("seleniumTestId", seleniumTestId)
                .asJson();
// grab out the snapshot "hash" from the response
        JsonNode jsonNode = (JsonNode) response.getBody();
        String snapshotHash = jsonNode.getObject().getString("hash");
        System.out.println(snapshotHash);
        return snapshotHash;
    }

    public static JsonNode setDescription(String seleniumTestId, String snapshotHash, String description) throws UnirestException {
/*
* sets the description for the given seleniemTestId and snapshotHash
*/
        HttpResponse response = Unirest.put("http://crossbrowsertesting.com/api/v3/selenium/{seleniumTestId}/snapshots/{snapshotHash}")
                .basicAuth(cbtUsername, cbtAuthKey)
                .routeParam("seleniumTestId", seleniumTestId)
                .routeParam("snapshotHash", snapshotHash)
                .field("description", description)
                .asJson();
        return (JsonNode) response.getBody();
    }

}
