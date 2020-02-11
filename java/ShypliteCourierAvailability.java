import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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

public class ShypliteCourierAvailability {

    private static final String HASH_ALGORITHM = "HmacSHA256";

    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, IOException, SignatureException {
        Instant instant = Instant.now();
        long timeStampMillis = instant.toEpochMilli() / 1000;

        System.out.println(timeStampMillis);

        int appID = 123;
        String key = "abcd=";
        String secret = "abcd==";

        String sign = "key:" + key + "id:" + String.valueOf(appID) + ":timestamp:" + String.valueOf(timeStampMillis);

        String authtoken = URLEncoder.encode(hashMac(sign, secret));

        

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet request = new HttpGet("https://api.shyplite.com/getserviceability/508252/500035");
        StringEntity params = new StringEntity(jsonStringData);
        request.addHeader("x-appid", String.valueOf(appID));
        request.addHeader("x-sellerid", "123");
        request.addHeader("x-timestamp", String.valueOf(timeStampMillis));
        request.addHeader("Authorization", authtoken);
        request.addHeader("x-version","3");
  

        HttpResponse response = httpClient.execute(request);


        System.out.println(response.getStatusLine().toString());

        HttpEntity entity = response.getEntity();
        Header headers = entity.getContentType();
        System.out.println(headers);

        if (entity != null) {
            // return it as a String
            String result = EntityUtils.toString(entity);
            System.out.println(result);
        }


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
        } catch (Exception e){
            System.out.println("Error");
            return "dummy";
        }

    }


}
