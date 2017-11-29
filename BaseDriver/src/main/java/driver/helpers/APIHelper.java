package driver.helpers;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.simple.JSONObject;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class APIHelper {
    private static Map<String, String> HEADERS = new HashMap<>();
    protected String baseURL;

    /**
     * Set baseURL
     *
     * @param baseURL
     */
    public APIHelper(String baseURL) {
        this.baseURL = baseURL;
    }

    public static void setHeaders(Map<String, String> headers) {
        HEADERS.putAll(headers);
    }

    public static void clearHeaders() {
        HEADERS.clear();
        Assert.assertTrue(HEADERS.isEmpty());
    }

    /**
     * Rest Method: GET
     *
     * @param endpoint - String
     * @return JSON Object
     */
    public HttpResponse<JsonNode> GET(String endpoint) {
        return this.sendRequest("GET", endpoint, null, false);
    }

    /**
     * @param endpoint        - String
     * @param data            - Can be Nullable
     * @param postQueryString - True for String, of False For JSON
     * @return
     */
    public HttpResponse<JsonNode> POST(String endpoint, Map<String, Object> data, boolean postQueryString) {
        return this.sendRequest("POST", endpoint, data, postQueryString);
    }

    /**
     * Rest Method: PUT
     *
     * @param endpoint        - String
     * @param data            - Can be Nullable
     * @param postQueryString - True for String, of False For JSON
     * @return JSON Object
     */
    public HttpResponse<JsonNode> PUT(String endpoint, Map<String, Object> data, boolean postQueryString) {
        return this.sendRequest("PUT", endpoint, data, postQueryString);
    }

    /**
     * Rest Method: DELETE
     *
     * @param endpoint        - String
     * @param data            - Can be Nullable
     * @param postQueryString - boolean
     * @return JSON Object
     */
    public HttpResponse<JsonNode> DELETE(String endpoint, Map<String, Object> data, boolean postQueryString) {
        return this.sendRequest("DELETE", endpoint, data, postQueryString);
    }

    /**
     * @param method          - GET, POST, PUT, DELETE
     * @param uri             - string
     * @param data            - Can be Nullable
     * @param postQueryString - boolean
     * @return - JsonNode
     */
    protected HttpResponse<JsonNode> sendRequest(String method, String uri, Map<String, Object> data, boolean postQueryString) {
        try {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                NavigableMap<String, Object> map = new TreeMap<String, Object>();
                map.putAll(data);
                for (Map.Entry<String, Object> item : map.entrySet()) {
                    if (map.lastEntry() == item) {
                        stringBuilder.append(item.getKey()).append("=").append(item.getValue());
                    } else {
                        stringBuilder.append(item.getKey()).append("=").append(item.getValue()).append("&");
                    }
                }
                JSONObject json = new JSONObject();
                json.putAll(data);

                Unirest.clearDefaultHeaders();
                for (Map.Entry<String, String> item : HEADERS.entrySet()) {
                    Unirest.setDefaultHeader(item.getKey(), item.getValue());
                }
                switch (method) {
                    case "GET":
                        System.out.println("Calling endpoint: " + baseURL + uri);
                        return Unirest.get(baseURL + uri).asJson();
                    case "POST":
                        System.out.println("Calling endpoint: " + baseURL + uri);
                        if (postQueryString) {
                            return Unirest.post(baseURL + uri).body(stringBuilder.toString()).asJson();
                        } else {
                            return Unirest.put(baseURL + uri).body(json.toJSONString()).asJson();
                        }
                    case "PUT":
                        System.out.println("Calling endpoint: " + baseURL + uri);
                        if (postQueryString) {
                            return Unirest.put(baseURL + uri).body(stringBuilder.toString()).asJson();
                        } else {
                            return Unirest.put(baseURL + uri).body(json.toJSONString()).asJson();
                        }
                    case "DELETE":
                        System.out.println("Calling endpoint: " + baseURL + uri);
                        return Unirest.delete(baseURL + uri).body(json.toJSONString()).asJson();
                    default:
                        org.testng.Assert.fail("Method was not chosen for the API call");
                        break;
                }
                return null;
            } catch (NullPointerException e) {
                Unirest.clearDefaultHeaders();
                for (Map.Entry<String, String> item : HEADERS.entrySet()) {
                    System.out.println("Header: " + item.getKey() + " Value: " + item.getValue());
                    Unirest.setDefaultHeader(item.getKey(), item.getValue());
                }
                switch (method) {
                    case "GET":
                        System.out.println("Calling endpoint: " + baseURL + uri);
                        return Unirest.get(baseURL + uri).asJson();
                    case "POST":
                        System.out.println("Calling endpoint: " + baseURL + uri);
                        return Unirest.put(baseURL + uri).asJson();
                    case "PUT":
                        System.out.println("Calling endpoint: " + baseURL + uri);
                        return Unirest.put(baseURL + uri).asJson();
                    case "DELETE":
                        System.out.println("Calling endpoint: " + baseURL + uri);
                        return Unirest.delete(baseURL + uri).asJson();
                    default:
                        org.testng.Assert.fail("Method was not chosen for the API call");
                        break;
                }
            }

        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

}
