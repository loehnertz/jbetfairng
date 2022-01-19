package com.jbetfairng;


import com.google.common.base.Optional;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.jbetfairng.entities.AccountDetailsResponse;
import com.jbetfairng.entities.AccountFundsResponse;
import com.jbetfairng.entities.AccountStatementReport;
import com.jbetfairng.entities.CancelExecutionReport;
import com.jbetfairng.entities.CancelInstruction;
import com.jbetfairng.entities.ClearedOrderSummaryReport;
import com.jbetfairng.entities.CompetitionResult;
import com.jbetfairng.entities.CountryCodeResult;
import com.jbetfairng.entities.CurrencyRate;
import com.jbetfairng.entities.CurrentOrderSummaryReport;
import com.jbetfairng.entities.EventResult;
import com.jbetfairng.entities.EventTypeResult;
import com.jbetfairng.entities.MarketBook;
import com.jbetfairng.entities.MarketCatalogue;
import com.jbetfairng.entities.MarketFilter;
import com.jbetfairng.entities.MarketProfitAndLoss;
import com.jbetfairng.entities.MarketTypeResult;
import com.jbetfairng.entities.MarketVersion;
import com.jbetfairng.entities.PlaceExecutionReport;
import com.jbetfairng.entities.PlaceInstruction;
import com.jbetfairng.entities.PriceProjection;
import com.jbetfairng.entities.ReplaceExecutionReport;
import com.jbetfairng.entities.ReplaceInstruction;
import com.jbetfairng.entities.TimeRange;
import com.jbetfairng.entities.TimeRangeResult;
import com.jbetfairng.entities.TransferResponse;
import com.jbetfairng.entities.UpdateExecutionReport;
import com.jbetfairng.entities.UpdateInstruction;
import com.jbetfairng.entities.VenueResult;
import com.jbetfairng.enums.BetStatus;
import com.jbetfairng.enums.Endpoint;
import com.jbetfairng.enums.Exchange;
import com.jbetfairng.enums.GroupBy;
import com.jbetfairng.enums.IncludeItem;
import com.jbetfairng.enums.MarketProjection;
import com.jbetfairng.enums.MarketSort;
import com.jbetfairng.enums.MatchProjection;
import com.jbetfairng.enums.OrderBy;
import com.jbetfairng.enums.OrderProjection;
import com.jbetfairng.enums.Side;
import com.jbetfairng.enums.SortDir;
import com.jbetfairng.enums.TimeGranularity;
import com.jbetfairng.enums.Wallet;
import com.jbetfairng.exceptions.LoginException;
import com.jbetfairng.util.Constants;
import com.jbetfairng.util.Helpers;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class BetfairClient {

    private static final String LIST_COMPETITIONS_METHOD = "SportsAPING/v1.0/listCompetitions";
    private static final String LIST_COUNTRIES_METHOD = "SportsAPING/v1.0/listCountries";
    private static final String LIST_CURRENT_ORDERS_METHOD = "SportsAPING/v1.0/listCurrentOrders";
    private static final String LIST_CLEARED_ORDERS_METHOD = "SportsAPING/v1.0/listClearedOrders";
    private static final String LIST_EVENT_TYPES_METHOD = "SportsAPING/v1.0/listEventTypes";
    private static final String LIST_EVENTS_METHOD = "SportsAPING/v1.0/listEvents";
    private static final String LIST_MARKET_CATALOGUE_METHOD = "SportsAPING/v1.0/listMarketCatalogue";
    private static final String LIST_MARKET_BOOK_METHOD = "SportsAPING/v1.0/listMarketBook";
    private static final String LIST_MARKET_PROFIT_AND_LOSS = "SportsAPING/v1.0/listMarketProfitAndLoss";
    private static final String LIST_MARKET_TYPES = "SportsAPING/v1.0/listMarketTypes";
    private static final String LIST_TIME_RANGES = "SportsAPING/v1.0/listTimeRanges";
    private static final String LIST_VENUES = "SportsAPING/v1.0/listVenues";
    private static final String PLACE_ORDERS_METHOD = "SportsAPING/v1.0/placeOrders";
    private static final String CANCEL_ORDERS_METHOD = "SportsAPING/v1.0/cancelOrders";
    private static final String REPLACE_ORDERS_METHOD = "SportsAPING/v1.0/replaceOrders";
    private static final String UPDATE_ORDERS_METHOD = "SportsAPING/v1.0/updateOrders";
    private static final String GET_ACCOUNT_DETAILS = "AccountAPING/v1.0/getAccountDetails";
    private static final String GET_ACCOUNT_FUNDS = "AccountAPING/v1.0/getAccountFunds";
    private static final String GET_ACCOUNT_STATEMENT = "AccountAPING/v1.0/getAccountStatement";
    private static final String LIST_CURRENCY_RATES = "AccountAPING/v1.0/listCurrencyRates";
    private static final String TRANSFER_FUNDS = "AccountAPING/v1.0/transferFunds";
    private static final String FILTER = "filter";
    private static final String BET_IDS = "betIds";
    private static final String RUNNER_IDS = "runnerIds";
    private static final String SIDE = "side";
    private static final String SETTLED_DATE_RANGE = "settledDateRange";
    private static final String EVENT_TYPE_IDS = "eventTypeIds";
    private static final String EVENT_IDS = "eventIds";
    private static final String BET_STATUS = "betStatus";
    private static final String PLACED_DATE_RANGE = "placedDateRange";
    private static final String DATE_RANGE = "dateRange";
    private static final String ORDER_BY = "orderBy";
    private static final String GROUP_BY = "groupBy";
    private static final String SORT_DIR = "sortDir";
    private static final String FROM_RECORD = "fromRecord";
    private static final String RECORD_COUNT = "recordCount";
    private static final String GRANULARITY = "granularity";
    private static final String MARKET_PROJECTION = "marketProjection";
    private static final String MATCH_PROJECTION = "matchProjection";
    private static final String ORDER_PROJECTION = "orderProjection";
    private static final String PRICE_PROJECTION = "priceProjection";
    private static final String SORT = "sort";
    private static final String MAX_RESULTS = "maxResults";
    private static final String MARKET_IDS = "marketIds";
    private static final String MARKET_ID = "marketId";
    private static final String INSTRUCTIONS = "instructions";
    private static final String CUSTOMER_REFERENCE = "customerRef";
    private static final String MARKET_VERSION = "marketVersion";
    private static final String INCLUDE_SETTLED_BETS = "includeSettledBets";
    private static final String INCLUDE_BSP_BETS = "includeBspBets";
    private static final String INCLUDE_ITEM_DESCRIPTION = "includeItemDescription";
    private static final String NET_OF_COMMISSION = "netOfCommission";
    private static final String FROM_CURRENCY = "fromCurrency";
    private static final String FROM = "from";
    private static final String TO = "to";
    private static final String AMOUNT = "amount";
    private static final String WALLET = "wallet";
    private static final String ITEM_DATE_RANGE = "itemDateRange";
    private static final String INCLUDE_ITEM = "includeItem";
    /**
     * Static defined identity endpoints
     */
    private static final HashMap<Exchange, String> identityEndpoints = new HashMap<>();

    static {
        identityEndpoints.put(Exchange.RO, "https://identitysso-cert.betfair.ro/api/certlogin");
        identityEndpoints.put(Exchange.UK, "https://identitysso-cert.betfair.com/api/certlogin");
        identityEndpoints.put(Exchange.AUS, "https://identitysso-cert.betfair.com/api/certlogin");
        identityEndpoints.put(Exchange.IT, "https://identitysso-cert.betfair.it/api/certlogin");
        identityEndpoints.put(Exchange.ES, "https://identitysso-cert.betfair.es/api/certlogin");
    }

    private final Exchange exchange;
    private final String appKey;
    private final Logger LOGGER = LogManager.getLogger(BetfairClient.class);
    private Network networkClient;

    public BetfairClient(Exchange exchange, String appKey) {
        this.exchange = exchange;
        this.appKey = appKey;
    }

    public Boolean login(
            String p12CertificateLocation,
            String p12CertificatePassword,
            String username,
            String password) throws LoginException {
        if (Helpers.isNullOrWhitespace(p12CertificateLocation))
            throw new IllegalArgumentException(p12CertificateLocation);
        if (Helpers.isNullOrWhitespace(p12CertificatePassword))
            throw new IllegalArgumentException(username);
        if (Helpers.isNullOrWhitespace(username))
            throw new IllegalArgumentException(username);
        if (Helpers.isNullOrWhitespace(password))
            throw new IllegalArgumentException(password);

        FileInputStream keyStream = null;
        try {
            KeyStore clientStore = KeyStore.getInstance("PKCS12");
            keyStream = new FileInputStream(p12CertificateLocation);
            clientStore.load(keyStream, p12CertificatePassword.toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(clientStore, p12CertificatePassword.toCharArray());
            KeyManager[] kms = kmf.getKeyManagers();

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kms, null, new SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            URL url = new URL(identityEndpoints.get(exchange));

            String postData = String.format("username=%s&password=%s", username, password);

            HttpsURLConnection request = (HttpsURLConnection) url.openConnection();
            request.setRequestMethod("POST");
            request.setRequestProperty("X-Application", this.appKey);
            request.setDoOutput(true);
            DataOutputStream writer = new DataOutputStream(request.getOutputStream());
            writer.writeBytes(postData);
            writer.flush();
            writer.close();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(request.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(response.toString());
            }
            Gson gson = new Gson();
            LoginResponse loginResult = gson.fromJson(response.toString(), LoginResponse.class);
            if (loginResult.loginStatus.equals(Constants.SUCCESS)) {
                this.networkClient = new Network(this.appKey, loginResult.sessionToken, false);
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            throw new LoginException(ex);
        } finally {
            try {
                keyStream.close();
            } catch (Exception ignore) {
            }
        }
    }

    public BetfairServerResponse<KeepAliveResponse> keepAlive() {
        return networkClient.keepAliveSynchronous();
    }

    public BetfairServerResponse<List<CompetitionResult>> listCompetitions(MarketFilter marketFilter) {
        HashMap<String, Object> args = new HashMap<>();
        args.put(FILTER, marketFilter);
        return networkClient.Invoke(new TypeToken<List<CompetitionResult>>() {
                                    }, this.exchange,
                Endpoint.Betting, LIST_COMPETITIONS_METHOD, args);
    }

    public BetfairServerResponse<List<CountryCodeResult>> listCountries(MarketFilter marketFilter) {
        HashMap<String, Object> args = new HashMap<>();
        args.put(FILTER, marketFilter);
        return networkClient.Invoke(
                new TypeToken<List<CountryCodeResult>>() {
                },
                this.exchange,
                Endpoint.Betting,
                LIST_COUNTRIES_METHOD,
                args);
    }

    public BetfairServerResponse<List<EventResult>> listEvents(MarketFilter marketFilter) {
        HashMap<String, Object> args = new HashMap<>();
        args.put(FILTER, marketFilter);
        return networkClient.Invoke(
                new TypeToken<List<EventResult>>() {
                },
                this.exchange,
                Endpoint.Betting,
                LIST_EVENTS_METHOD,
                args);
    }

    public BetfairServerResponse<List<EventTypeResult>> listEventTypes(MarketFilter marketFilter) {
        HashMap<String, Object> args = new HashMap<>();
        args.put(FILTER, marketFilter);
        return networkClient.Invoke(
                new TypeToken<List<EventTypeResult>>() {
                },
                this.exchange,
                Endpoint.Betting,
                LIST_EVENT_TYPES_METHOD,
                args);
    }

    public BetfairServerResponse<List<MarketBook>> listMarketBook(
            List<String> marketIds,
            PriceProjection priceProjection,
            OrderProjection orderProjection,
            MatchProjection matchProjection) {
        HashMap<String, Object> args = new HashMap<>();
        args.put(MARKET_IDS, marketIds);
        args.put(PRICE_PROJECTION, priceProjection);
        args.put(ORDER_PROJECTION, orderProjection);
        args.put(MATCH_PROJECTION, matchProjection);
        return networkClient.Invoke(
                new TypeToken<List<MarketBook>>() {
                },
                this.exchange,
                Endpoint.Betting,
                LIST_MARKET_BOOK_METHOD,
                args);
    }

    public BetfairServerResponse<List<MarketCatalogue>> listMarketCatalogue(
            MarketFilter marketFilter,
            Set<MarketProjection> marketProjections,
            MarketSort sort,
            int maxResult) {
        HashMap<String, Object> args = new HashMap<>();
        args.put(FILTER, marketFilter);
        args.put(MARKET_PROJECTION, marketProjections);
        args.put(SORT, sort);
        args.put(MAX_RESULTS, maxResult);
        return networkClient.Invoke(
                new TypeToken<List<MarketCatalogue>>() {
                },
                this.exchange,
                Endpoint.Betting,
                LIST_MARKET_CATALOGUE_METHOD,
                args);
    }

    public BetfairServerResponse<List<MarketTypeResult>> listMarketTypes(MarketFilter marketFilter) {
        HashMap<String, Object> args = new HashMap<>();
        args.put(FILTER, marketFilter);
        return networkClient.Invoke(
                new TypeToken<List<MarketTypeResult>>() {
                },
                this.exchange,
                Endpoint.Betting,
                LIST_MARKET_TYPES,
                args);
    }

    public BetfairServerResponse<List<MarketProfitAndLoss>> listMarketProfitAndLoss(
            List<String> marketIds,
            Boolean includeSettledBets,
            Boolean includeBsbBets,
            Boolean netOfComission) {
        HashMap<String, Object> args = new HashMap<>();
        args.put(MARKET_IDS, marketIds);
        args.put(INCLUDE_SETTLED_BETS, includeSettledBets);
        args.put(INCLUDE_BSP_BETS, includeBsbBets);
        args.put(NET_OF_COMMISSION, netOfComission);
        return networkClient.Invoke(
                new TypeToken<List<MarketProfitAndLoss>>() {
                },
                this.exchange,
                Endpoint.Betting,
                LIST_MARKET_PROFIT_AND_LOSS,
                args);
    }

    public BetfairServerResponse<List<TimeRangeResult>> listTimeRanges(
            MarketFilter marketFilter,
            TimeGranularity timeGranularity) {
        HashMap<String, Object> args = new HashMap<>();
        args.put(FILTER, marketFilter);
        args.put(GRANULARITY, timeGranularity);
        return networkClient.Invoke(
                new TypeToken<List<TimeRangeResult>>() {
                },
                this.exchange,
                Endpoint.Betting,
                LIST_TIME_RANGES,
                args);
    }

    public BetfairServerResponse<List<VenueResult>> listVenues(MarketFilter marketFilter) {
        HashMap<String, Object> args = new HashMap<>();
        args.put(FILTER, marketFilter);
        return networkClient.Invoke(
                new TypeToken<List<VenueResult>>() {
                },
                this.exchange,
                Endpoint.Betting,
                LIST_VENUES,
                args);
    }

    public BetfairServerResponse<CurrentOrderSummaryReport> listCurrentOrders(
            Set<String> betIds,
            Set<String> marketIds,
            OrderProjection orderProjection,
            TimeRange placedDateRange,
            TimeRange dateRange,
            OrderBy orderBy,
            SortDir sortDir,
            Optional<Integer> fromRecord,
            Optional<Integer> recordCount) {
        HashMap<String, Object> args = new HashMap<>();
        args.put(BET_IDS, betIds);
        args.put(MARKET_IDS, marketIds);
        args.put(ORDER_PROJECTION, orderProjection);
        args.put(PLACED_DATE_RANGE, placedDateRange);
        args.put(DATE_RANGE, dateRange);
        args.put(ORDER_BY, orderBy);
        args.put(SORT_DIR, sortDir);
        args.put(FROM_RECORD, fromRecord);
        args.put(RECORD_COUNT, recordCount);
        return networkClient.Invoke(
                new TypeToken<CurrentOrderSummaryReport>() {
                },
                this.exchange,
                Endpoint.Betting,
                LIST_CURRENT_ORDERS_METHOD,
                args);
    }

    public BetfairServerResponse<ClearedOrderSummaryReport> listClearedOrders(
            BetStatus betStatus,
            Set<String> eventTypeIds,
            Set<String> eventIds,
            Set<String> marketIds,
            Set<String> runnerIds,
            Set<String> betIds,
            Side side,
            TimeRange settledDateRange,
            GroupBy groupBy,
            Boolean includeItemDescription,
            Integer fromRecord,
            Integer recordCount) {
        HashMap<String, Object> args = new HashMap<>();
        args.put(BET_STATUS, betStatus);
        args.put(EVENT_TYPE_IDS, eventTypeIds);
        args.put(EVENT_IDS, eventIds);
        args.put(MARKET_IDS, marketIds);
        args.put(BET_IDS, betIds);
        args.put(SIDE, side);
        args.put(DATE_RANGE, settledDateRange);
        args.put(GROUP_BY, groupBy);
        args.put(INCLUDE_ITEM_DESCRIPTION, includeItemDescription);
        args.put(FROM_RECORD, fromRecord);
        args.put(RECORD_COUNT, recordCount);

        return networkClient.Invoke(
                new TypeToken<ClearedOrderSummaryReport>() {
                },
                this.exchange,
                Endpoint.Betting,
                LIST_CLEARED_ORDERS_METHOD,
                args);
    }

    public BetfairServerResponse<PlaceExecutionReport> placeOrders(
            String marketId,
            List<PlaceInstruction> placeInstructions,
            String customerRef, long marketVersion) {
        HashMap<String, Object> args = new HashMap<>();
        args.put(MARKET_ID, marketId);
        args.put(INSTRUCTIONS, placeInstructions);
        args.put(CUSTOMER_REFERENCE, customerRef);
        args.put(MARKET_VERSION, new MarketVersion(marketVersion));

        return networkClient.Invoke(
                new TypeToken<PlaceExecutionReport>() {
                },
                this.exchange,
                Endpoint.Betting,
                PLACE_ORDERS_METHOD,
                args);
    }

    public BetfairServerResponse<CancelExecutionReport> cancelOrders(
            String marketId,
            List<CancelInstruction> instructions,
            String customerRef) {
        HashMap<String, Object> args = new HashMap<>();
        args.put(MARKET_ID, marketId);
        args.put(INSTRUCTIONS, instructions);
        args.put(CUSTOMER_REFERENCE, customerRef);

        return networkClient.Invoke(
                new TypeToken<CancelExecutionReport>() {
                },
                this.exchange,
                Endpoint.Betting,
                CANCEL_ORDERS_METHOD,
                args);
    }

    public BetfairServerResponse<ReplaceExecutionReport> replaceOrders(
            String marketId,
            List<ReplaceInstruction> instructions,
            String customerRef) {
        HashMap<String, Object> args = new HashMap<>();
        args.put(MARKET_ID, marketId);
        args.put(INSTRUCTIONS, instructions);
        args.put(CUSTOMER_REFERENCE, customerRef);

        return networkClient.Invoke(
                new TypeToken<ReplaceExecutionReport>() {
                },
                this.exchange,
                Endpoint.Betting,
                REPLACE_ORDERS_METHOD,
                args);
    }

    public BetfairServerResponse<UpdateExecutionReport> updateOrders(
            String marketId,
            List<UpdateInstruction> instructions,
            String customerRef) {
        HashMap<String, Object> args = new HashMap<>();
        args.put(MARKET_ID, marketId);
        args.put(INSTRUCTIONS, instructions);
        args.put(CUSTOMER_REFERENCE, customerRef);

        return networkClient.Invoke(
                new TypeToken<UpdateExecutionReport>() {
                },
                this.exchange,
                Endpoint.Betting,
                UPDATE_ORDERS_METHOD,
                args);
    }

    // Account API's
    public BetfairServerResponse<AccountDetailsResponse> getAccountDetails() {
        HashMap<String, Object> args = new HashMap<>();
        return networkClient.Invoke(
                new TypeToken<AccountDetailsResponse>() {
                },
                this.exchange,
                Endpoint.Account,
                GET_ACCOUNT_DETAILS,
                args);
    }

    public BetfairServerResponse<AccountFundsResponse> getAccountFunds(Wallet wallet) {
        HashMap<String, Object> args = new HashMap<>();
        args.put(WALLET, wallet);
        return networkClient.Invoke(
                new TypeToken<AccountFundsResponse>() {
                },
                this.exchange,
                Endpoint.Account,
                GET_ACCOUNT_FUNDS,
                args);
    }

    public BetfairServerResponse<AccountStatementReport> getAccountStatement(
            Integer fromRecord,
            Integer recordCount,
            TimeRange itemDateRange,
            IncludeItem includeItem,
            Wallet wallet) {
        HashMap<String, Object> args = new HashMap<>();
        args.put(FROM_RECORD, fromRecord);
        args.put(RECORD_COUNT, recordCount);
        args.put(ITEM_DATE_RANGE, itemDateRange);
        args.put(INCLUDE_ITEM, includeItem);
        args.put(WALLET, wallet);
        return networkClient.Invoke(
                new TypeToken<AccountStatementReport>() {
                },
                this.exchange,
                Endpoint.Account,
                GET_ACCOUNT_STATEMENT,
                args);
    }

    public BetfairServerResponse<List<CurrencyRate>> listCurrencyRates(String fromCurrency) {
        HashMap<String, Object> args = new HashMap<>();
        args.put(FROM_CURRENCY, fromCurrency);
        return networkClient.Invoke(
                new TypeToken<List<CurrencyRate>>() {
                },
                this.exchange,
                Endpoint.Account,
                LIST_CURRENCY_RATES,
                args);
    }

    public BetfairServerResponse<TransferResponse> transferFunds(Wallet from, Wallet to, double amount) {
        HashMap<String, Object> args = new HashMap<>();
        args.put(FROM, from);
        args.put(TO, to);
        args.put(AMOUNT, amount);
        return networkClient.Invoke(
                new TypeToken<TransferResponse>() {
                },
                this.exchange,
                Endpoint.Account,
                TRANSFER_FUNDS,
                args);
    }

}
