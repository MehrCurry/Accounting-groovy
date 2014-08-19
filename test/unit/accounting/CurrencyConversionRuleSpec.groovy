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
    public static final Money TEN_DOLLAR = Money.fromMajor(10, USD)
    public static final Money EUR_750 = Money.fromMinor(750, EUR)

    void "test Something"() {
        given:
        def ledger=Ledger.withName("Test")
        ledger.postingService=new PostingService()

        def xUSD=ledger.newAccount("X in Dollar", USD)
        def yUSD=ledger.newAccount("Y in EUR", EUR)
        def accUSD=ledger.newAccount("Einnahmen in Dollar",USD)
        def accEUR=ledger.newAccount("Einnahmen in EUR",EUR)

        def ccs = Mock(CurrencyConverterService)
        ccs.convert(*_) >>> [Money.fromMinor(750,EUR), Money.fromMinor(749,EUR)]

        def rule = new CurrencyConversionRule(targetAccountName: "Y in EUR", otherAccountName: "Einnahmen in EUR", ccs: ccs)
        xUSD.rule(rule)

        when:
        ledger.posting("A Posting").credit(TEN_DOLLAR,xUSD).debit(TEN_DOLLAR,accUSD).post()

        then:
        assert xUSD.balance() == TEN_DOLLAR
        assert accUSD.balance() == TEN_DOLLAR.negate()
        assert yUSD.balance() == EUR_750
        assert accEUR.balance() == EUR_750.negate()

        ledger.posting("Another one").debit(TEN_DOLLAR,xUSD).credit(TEN_DOLLAR,accUSD).post()

        ledger.accounts.values().each {
            it.printT()
        }
    }
}
