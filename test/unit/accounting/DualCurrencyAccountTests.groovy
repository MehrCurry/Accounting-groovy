package accounting

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import com.ibm.icu.util.Currency as Currency
/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(DualCurrencyAccount)
@Mock([DetailAccount,Entry,Posting,Ledger])
class DualCurrencyAccountTests {
    static final EUR=Currency.getInstance("EUR")
    static final USD=Currency.getInstance("USD")

    void testSomething() {
        def ledger=new Ledger()
        def eur=ledger.newAccount("Umsatz:EUR",EUR)
        def usd=ledger.newAccount("Umsatz:USD",USD)

        def dca = new DualCurrencyAccount()

    }
}
