package com.jbetfairng;

import static java.lang.Integer.parseInt;

import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.jbetfairng.enums.Endpoint;
import com.jbetfairng.enums.Exchange;
import com.jbetfairng.util.Helpers;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;

public class Network {

    @Nullable
    private final HttpHost proxy = getJvmProxy();
    private final String appKey;
    private final String sessionToken;
    private final Logger tracer;

    public Network(
            String appKey,
            String sessionToken) {
        this.appKey = appKey;
        this.sessionToken = sessionToken;
        this.tracer = LogManager.getFormatterLogger("Network");
    }

    public <T> BetfairServerResponse<T> Invoke(
            TypeToken<T> elementClass,
            Exchange exchange,
            Endpoint endpoint,
            String method,
            Map<String, Object> args) {
        if (Helpers.isNullOrWhitespace(method)) throw new IllegalArgumentException(method);
        tracer.debug("%s, %s", formatEndpoint(endpoint), method);

        DateTime requestStart = DateTime.now();
        long requestStartMillis = System.currentTimeMillis();

        String url;
        if (exchange == Exchange.AUS)
            url = "https://api-au.betfair.com/exchange";
        else
            url = "https://api.betfair.com/exchange";

        if (endpoint == Endpoint.Betting)
            url += "/betting/json-rpc/v1";
        else
            url += "/account/json-rpc/v1";

        JsonRequest call = new JsonRequest();
        call.method = method;
        call.params = args;
        call.id = 1;

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
        String requestData = gson.toJson(call);
        String result = requestSync(
                url,
                requestData,
                ContentType.APPLICATION_JSON,
                appKey,
                sessionToken);

        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

        Type underlyingType = new TypeToken<JsonResponse<T>>() {
        }.where(new TypeParameter<T>() {
        }, elementClass).getType();

        JsonResponse<T> entity = deserializeResponseBody(result, gson, underlyingType);
        // should be returning Observable<Betfair...> here.
        if (entity != null) {
            BetfairServerResponse<T> response = new BetfairServerResponse<T>(
                    entity.result,
                    DateTime.now(),
                    requestStart,
                    (System.currentTimeMillis() - requestStartMillis) / 1000,
                    entity.hasError);
            return response;
        } else
            return new BetfairServerResponse<>(
                    null,
                    DateTime.now(),
                    requestStart,
                    (System.currentTimeMillis() - requestStartMillis) / 1000,
                    true);
    }

    public BetfairServerResponse<KeepAliveResponse> keepAliveSynchronous() {
        DateTime requestStart = DateTime.now();
        long requestStartMillis = System.currentTimeMillis();

        String keepAliveResponse = this.requestSync(
                "https://identitysso.betfair.com/api/keepAlive",
                "",
                ContentType.APPLICATION_FORM_URLENCODED,
                this.appKey,
                this.sessionToken);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
        Type typeToken = new TypeToken<KeepAliveResponse>() {
        }.getType();
        KeepAliveResponse entity = deserializeResponseBody(keepAliveResponse, gson, typeToken);
        if (entity != null) {
            BetfairServerResponse<KeepAliveResponse> response = new BetfairServerResponse<>(
                    entity,
                    DateTime.now(),
                    requestStart,
                    (System.currentTimeMillis() - requestStartMillis) / 1000,
                    Boolean.parseBoolean(entity.error));
            return response;
        } else {
            KeepAliveResponse response = new KeepAliveResponse();
            response.error = "Keep Alive request failed.";
            return new BetfairServerResponse<>(
                    response,
                    DateTime.now(),
                    requestStart,
                    (System.currentTimeMillis() - requestStartMillis) / 1000,
                    true);
        }
    }

    @Nullable
    private static HttpHost getJvmProxy() {
        @Nullable String proxyHost = System.getProperty("https.proxyHost");
        if (proxyHost != null) {
            return new HttpHost(proxyHost, parseInt(System.getProperty("https.proxyPort")), "https");
        } else {
            return null;
        }
    }

    @Nullable
    private <T> T deserializeResponseBody(String responseBody, Gson gson, Type typeToken) {
        try {
            return gson.fromJson(responseBody, typeToken);
        } catch (JsonSyntaxException e) {
            tracer.error(String.format("Could not deserialize alleged JSON object %s as %s", responseBody, typeToken.getTypeName()), e);
            return null;
        }
    }

    private String formatEndpoint(Endpoint endpoint) {
        return endpoint == Endpoint.Betting ? "betting" : "account";
    }

    @Nullable
    private String requestSync(
            String url,
            String requestPostData,
            ContentType contentType,
            String appKey,
            String sessionToken) {
        Header[] headers = {
                new BasicHeader("X-Application", appKey),
                new BasicHeader("X-Authentication", sessionToken),
                new BasicHeader("Cache-Control", "no-cache"),
                new BasicHeader("Pragma", "no-cache"),
                new BasicHeader("Accept", "application/json")
        };

        HttpClientBuilder clientBuilder = HttpClientBuilder.create()
                .setDefaultHeaders(new ArrayList<>(Arrays.asList(headers)));
        if (proxy != null) clientBuilder.setProxy(proxy);
        HttpClient client = clientBuilder.build();

        try {
            StringEntity entity = new StringEntity(requestPostData);
            entity.setContentType(contentType.toString());
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            HttpResponse response = client.execute(post);

            return EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            tracer.error(String.format("An error occurred when performing HTTP request to '%s'", url), e);
            return null;
        }
    }
}
