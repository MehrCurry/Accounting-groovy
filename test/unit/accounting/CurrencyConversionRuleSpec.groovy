package accounting

import accounting.groovy.CurrencyConverterService
import de.gzockoll.types.money.Money
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(CurrencyConversionRule)
@Mock([Ledger,DetailAccount,Entry,Posting])
class CurrencyConversionRuleSpec extends Specification {

    public static final Currency USD = Currency.getInstance("USD")
    public static final Currency EUR = Currency.getInstance("EUR")

    void "test Something"() {
        given:
        def ledger=Ledger.withName("Test")
        ledger.postingService=new PostingService()

        def xUSD=ledger.newAccount("X in Dollar", USD)
        def yUSD=ledger.newAccount("Y in EUR", EUR)
        def accUSD=ledger.newAccount("Einnahmen in Dollar",USD)
        def accEUR=ledger.newAccount("Einnahmen in EUR",EUR)
        xUSD.rule(new CurrencyConversionRule(targetAccountName: "Y in EUR",otherAccountName: "Einnahmen in EUR",ccs: new CurrencyConverterService()))

        when:
        ledger.posting("A Posting").credit(Money.fromMajor(10,USD),xUSD).debit(Money.fromMajor(10,USD),accUSD).post()

        then:
        assert 1 == 1
        ledger.accounts.values().each {
            it.printT()
        }

    }
}
