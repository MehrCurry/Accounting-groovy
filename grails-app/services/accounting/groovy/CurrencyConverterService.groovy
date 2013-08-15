package accounting.groovy
import com.google.gson.Gson
import com.ibm.icu.util.Currency as Currency
import de.gzockoll.types.money.Money
import grails.plugin.cache.Cacheable
import org.apache.http.client.HttpClient
import org.apache.http.client.ResponseHandler
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.BasicResponseHandler
import org.apache.http.impl.client.DefaultHttpClient
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat

class CurrencyConverterService {
    private static final String google = "http://www.google.com/ig/calculator?hl=en&q="
    private static final String CHARSET = "UTF-8"
    private static final dateFormatter = ISODateTimeFormat.date()
    public static final List<String> CURRENCY_CODES = java.util.Currency.availableCurrencies.collect { it.currencyCode }.sort()

    Money convert(Money source, Currency currency) {
        def factor = getExchangeRate(source.currency.currencyCode, currency.currencyCode)
        Money.fromMajor(source.value * factor, currency)
    }

    BigDecimal getExchangeRateFromYahoo(String currencyFrom, String currencyTo) throws IOException {
        assert CURRENCY_CODES.contains(currencyFrom), "Unsupported Currency: $currencyFrom"
        assert CURRENCY_CODES.contains(currencyTo), "Unsupported Currency: $currencyTo"

        HttpClient httpclient = new DefaultHttpClient()
        HttpGet httpGet = new HttpGet("http://quote.yahoo.com/d/quotes.csv?s=" + currencyFrom + currencyTo + "=X&f=l1&e=.csv")
        ResponseHandler<String> responseHandler = new BasicResponseHandler()
        String responseBody = httpclient.execute(httpGet, responseHandler)
        httpclient.getConnectionManager().shutdown()
        return new BigDecimal(responseBody.trim())
    }

    BigDecimal getExchangeRate(String currencyFrom, String currencyTo) throws IOException {
        assert CURRENCY_CODES.contains(currencyFrom), "Unsupported Currency: $currencyFrom"
        assert CURRENCY_CODES.contains(currencyTo), "Unsupported Currency: $currencyTo"

        def rates = getRates()
        rates.get(currencyTo) / rates.get(currencyFrom)
    }

    @Cacheable(value='exchangerates',key="now")
    def getRates() {
        def url = "http://openexchangerates.org/api/latest.json?app_id=5e9f69fd45494652aff0fc7796f0d618"
        executeRequest(url)
    }

    @Cacheable(value='exchangerates',key="#aDate")
    def getHistoricalRates(DateTime aDate) {
        def dateString = aDate.toString(dateFormatter)
        def url = "http://openexchangerates.org/api/historical/${dateString}.json?app_id=5e9f69fd45494652aff0fc7796f0d618"
        executeRequest(url)
    }

    def executeRequest(String url) {
        HttpClient httpclient = new DefaultHttpClient()
        HttpGet httpGet = new HttpGet(url)
        ResponseHandler<String> responseHandler = new BasicResponseHandler()
        String responseBody = httpclient.execute(httpGet, responseHandler)
        httpclient.getConnectionManager().shutdown()
        def rates=new Gson().fromJson(responseBody, Data.class).rates
        rates
    }

    static class Data {
        String disclaimer
        String license
        String timestamp
        String base
        Map<String,BigDecimal> rates
    }
}

