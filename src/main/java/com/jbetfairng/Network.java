package com.jbetfairng;

import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.jbetfairng.enums.Endpoint;
import com.jbetfairng.enums.Exchange;
import com.jbetfairng.util.Helpers;
import java.lang.reflect.Type;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;

public class Network {

    public static final HttpClient HTTP_CLIENT = buildHttpClient();
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

    private static HttpClient buildHttpClient() {
        return HttpClient.newBuilder()
                .proxy(ProxySelector.getDefault())
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
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
                "application/json",
                this.appKey,
                this.sessionToken);

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
                "application/x-www-form-urlencoded",
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
            String contentType,
            String appKey,
            String sessionToken) {
        try {
            URI uri;
            try {
                uri = new URI(url);
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException(e);
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("X-Application", appKey)
                    .header("X-Authentication", sessionToken)
                    .header("Cache-Control", "no-cache")
                    .header("Pragma", "no-cache")
                    .header("Accept", "application/json")
                    .header("Content-Type", contentType)
                    .POST(HttpRequest.BodyPublishers.ofString(requestPostData))
                    .timeout(Duration.ofMinutes(1))
                    .build();

            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() / 100 != 2) {
                throw new IllegalStateException(String.format("Request '%s' did not succeed with response '%s'", request, response));
            }

            return response.body();
        } catch (Exception e) {
            tracer.error(String.format("An error occurred when performing HTTP request to '%s'", url), e);
            return null;
        }
    }
}
