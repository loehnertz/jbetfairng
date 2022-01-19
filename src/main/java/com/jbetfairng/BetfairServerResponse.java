package com.jbetfairng;

import org.joda.time.DateTime;

public class BetfairServerResponse<T> {
    private final T response;
    private final DateTime lastByte;
    private final DateTime requestStart;
    private final long latencyMs;
    private final Boolean hasError;

    public BetfairServerResponse(
            T response,
            DateTime lastByte,
            DateTime requestStart,
            long latencyMs,
            Boolean hasError) {
        this.response = response;
        this.lastByte = lastByte;
        this.requestStart = requestStart;
        this.latencyMs = latencyMs;
        this.hasError = hasError;
    }

    public T getResponse() {
        return this.response;
    }

    public DateTime getLastByte() {
        return this.lastByte;
    }

    public DateTime getRequestStart() {
        return this.requestStart;
    }

    public long getLatencyMs() {
        return this.latencyMs;
    }

    public Boolean getHasError() {
        return this.hasError;
    }
}
