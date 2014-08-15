package accounting

import de.gzockoll.types.money.Money
import grails.test.mixin.TestFor
import org.junit.Test
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(CurrencyConversionRule)
class CurrencyConversionRuleSpec extends Specification {

    public static final Currency USD = Currency.getInstance("USD")
    public static final Currency EUR = Currency.getInstance("EUR")
    PostingService service

    def setup() {
    }

    def cleanup() {
    }

    @Test
    void testSomething() {
        def ledger=Ledger.withName("Test")
        def xUSD=ledger.newAccount("X in Dollar", USD)
        def yUSD=ledger.newAccount("Y in EUR", EUR)
        def accUSD=ledger.newAccount("Einnahmen in Dollar",USD)
        def accEUR=ledger.newAccount("Einnahmen in EUR",EUR)
        accUSD.addToRules(new CurrencyConversionRule(target: accEUR))
        ledger.posting("A Posting").credit(Money.fromMajor(10,USD),xUSD).debit(Money.fromMajor(10,USD),accUSD).post()


    }
}
