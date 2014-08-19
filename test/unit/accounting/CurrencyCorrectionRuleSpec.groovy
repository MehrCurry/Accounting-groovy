package accounting

import accounting.groovy.CurrencyConverterService
import de.gzockoll.types.money.Money
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * Created by guido on 17.08.14.
 */
@TestFor(CurrencyConversionRule)
@Mock([Ledger,DetailAccount,Entry,Posting])
class CurrencyCorrectionRuleSpec extends Specification {

    public static final Currency USD = Currency.getInstance("USD")
    public static final Currency EUR = Currency.getInstance("EUR")
    public static final Money TEN_DOLLAR = Money.fromMajor(10, USD)
    public static final Money EUR_750 = Money.fromMinor(750, EUR)

    void "test Something"() {
        given:
        def ledger = Ledger.withName("Test")
        ledger.postingService = new PostingService()

        def xUSD = ledger.newAccount("X in Dollar", USD)
        def yUSD = ledger.newAccount("Y in EUR", EUR)
        def accUSD = ledger.newAccount("Einnahmen in Dollar", USD)
        def accEUR = ledger.newAccount("Einnahmen in EUR", EUR)
        def diff = ledger.newAccount("Währungsdifferenzen", EUR)

        def ccs = Mock(CurrencyConverterService)
        ccs.convert(*_) >>> [Money.fromMinor(750, EUR), Money.fromMinor(749, EUR),Money.fromMinor(0, EUR)]

        def rule = new CurrencyConversionRule(targetAccountName: "Y in EUR", otherAccountName: "Einnahmen in EUR")
        rule.currencyConverterService=ccs
        xUSD.rule(rule)
        ledger.posting("A Posting").credit(TEN_DOLLAR, xUSD).debit(TEN_DOLLAR, accUSD).post()
        ledger.posting("Another one").debit(TEN_DOLLAR, xUSD).credit(TEN_DOLLAR, accUSD).post()

        when:
        def correctionRule = new CurrencyCorrectionRule(otherAccount: "Y in EUR", correctionAccountName: "Währungsdifferenzen")
        correctionRule.currencyConverterService = ccs

        def rule2=new CurrencyCorrectionRule()
        xUSD.fireRule(correctionRule)

        then:
        ledger.accounts.values().each {
            it.printT()
        }
    }
}