package com.example.common;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class SparkAuthUtil {

    public static String getAuthUrl(String apiKey, String apiSecret, String hostUrl) throws Exception {
        URL url = new URL(hostUrl);
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());

        String preStr = "host: " + url.getHost() + "\n" +
                "date: " + date + "\n" +
                "GET " + url.getPath() + " HTTP/1.1";

        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        mac.init(spec);
        String sha = Base64.getEncoder().encodeToString(mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8)));

        String authorization = String.format(
                "api_key=\"%s\", algorithm=\"hmac-sha256\", headers=\"host date request-line\", signature=\"%s\"",
                apiKey, sha);

        return String.format("%s?authorization=%s&date=%s&host=%s",
                hostUrl,
                Base64.getEncoder().encodeToString(authorization.getBytes()),
                URLEncoder.encode(date, "UTF-8"),
                url.getHost());
    }
}