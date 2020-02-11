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
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.time.Instant;
import java.util.Base64;

public class ShpyliteCancelOrder {

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

        System.out.println(authtoken);




        JSONArray jsonArray = new JSONArray();
        jsonArray.put("123456");

        JSONObject jsonData = new JSONObject();
        jsonData.put("orders", jsonArray);

        String jsonStringData = jsonData.toString();

        System.out.println(jsonStringData);

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost request = new HttpPost("https://api.shyplite.com/ordercancel");
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
