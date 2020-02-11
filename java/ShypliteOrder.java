

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.time.Instant;
import java.util.Base64;

public class ShypliteOrder {


    private static final String HASH_ALGORITHM = "HmacSHA256";

    public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, IOException, SignatureException {
        Instant instant = Instant.now();
        long timeStampMillis = instant.toEpochMilli() / 1000;

        System.out.println(timeStampMillis);

        int appID = 123;
        String key = "abcd=";
        String secret = "abcd==";

        String sign = "key:" + key + "id:" + String.valueOf(appID) + ":timestamp:" + String.valueOf(timeStampMillis);

        String authtoken = URLEncoder.encode(hashMac(sign, secret));

        JSONObject json = new JSONObject();

        json.put("orderId", "987654321");
        json.put("customerName", "Pushpendra Kumar");
        json.put("customerAddress", "B-56, M-Block Lane No. 56 Kakadeo Near City Model School");
        json.put("customerCity", "Hitech city");
        json.put("customerPinCode", "500035");
        json.put("customerContact", "1111111111");
        json.put("orderDate", "2017-07-25");
        json.put("modeType", "Lite-1kg");
        json.put("orderType", "postpaid");
        json.put("totalValue", "1708.50");
        json.put("categoryName", "Cameras Audio and Video");
        json.put("packageName", "Sony Extra-bass Headphone.");
        json.put("quantity", "1");
        json.put("packageLength", "45");
        json.put("packageWidth", "45");
        json.put("packageHeight", "10");
        json.put("packageWeight", "0.5");
        json.put("sellerAddressId", "16973");

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(json);

        JSONObject jsonData = new JSONObject();
        jsonData.put("orders", jsonArray);

        String jsonStringData = jsonData.toString();

        System.out.println(jsonStringData);

        HttpClient httpClient = new DefaultHttpClient();
        HttpPut request = new HttpPut("https://api.shyplite.com/order");
        StringEntity params = new StringEntity(jsonStringData);
        request.setHeader("x-appid", String.valueOf(appID));
        request.setHeader("x-sellerid", "123");
        request.setHeader("x-version", "3");
        request.setHeader("x-timestamp", String.valueOf(timeStampMillis));
        request.setHeader("Authorization", authtoken);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(params);


        HttpResponse response = httpClient.execute(request);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder builder = new StringBuilder();
        String str = "";
        while ((str = rd.readLine()) != null) {
            builder.append(str);
        }


        System.out.println(builder.toString());

    }

    public static String hashMac(String text, String secretKey) {
        try {
            String secret = secretKey;
            String message = text;

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            Base64.Encoder encoder = Base64.getEncoder();
            String hash = encoder.encodeToString(sha256_HMAC.doFinal(message.getBytes()));
            System.out.println(hash);
            return hash;
        } catch (Exception e) {
            System.out.println("Error");
            return "dummy";
        }

    }

}
