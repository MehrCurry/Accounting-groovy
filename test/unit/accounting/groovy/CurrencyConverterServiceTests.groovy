package accounting.groovy

import de.gzockoll.types.money.Money
import grails.test.mixin.TestFor
import net.sf.ehcache.CacheManager
import org.apache.commons.lang3.time.StopWatch

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(CurrencyConverterService)
class CurrencyConverterServiceTests {
    public static final Currency USD = Currency.getInstance("USD")
    CurrencyConverterService service
    void setUp() {
        service=new CurrencyConverterService()
    }
    void testConversion() {
        def result=service.convert(Money.euros(10),USD);
        assert result.currency == USD
        assert result.value > 10.0
    }

    void testCache() {
        def mgr=getCacheManager("exchangerates")
        def sw=new StopWatch()
        def run = {
            sw.start()
            def rate=service.getExchangeRate("USD","EUR");
            sw.stop()
        }
        run.call()
        def deltaT = sw.getTime()
        sw.reset()
        run.call()
        assert sw.getTime() < deltaT
    }

    def getCacheManager(name) {
        CacheManager.ALL_CACHE_MANAGERS.find { it.name.equalsIgnoreCase name}
    }
}
