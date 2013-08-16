import accounting.DetailAccount
import accounting.Ledger
import com.ibm.icu.util.Currency as Currency
import com.ibm.icu.util.ULocale
import de.gzockoll.types.money.Money

class BootStrap {
    static final EUR = Currency.getInstance("EUR")

    def currencyConverterService
    def postingService

    def init = { servletContext ->
        assert postingService != null
        def ledger = new Ledger("general ledger").save(failOnError: true)
        ledger.postingService=postingService

        def b0 = ledger.newAccount("a:b:c",EUR)
        def b1 = ledger.newAccount("a:b:d",EUR)

        def p=ledger.posting("First post").credit(Money.euros(10),b0).debit(Money.euros(10),b1).post()

        Currency omanRial = Currency.getInstance(new ULocale.Builder().setRegion("OM").setLanguage("ar").build());

        def a1=ledger.newAccount("4000:0000",DetailAccount.EUR)
        def a2=ledger.newAccount("4000:0001",DetailAccount.EUR)
        def a3=ledger.newAccount("4000:0002",DetailAccount.EUR)
        def a4=ledger.newAccount("Oman Rial", omanRial)

        def p1=ledger.posting("Some money").credit(Money.euros(10),a1).credit(Money.euros(20),a2).debit(Money.euros(30),a3).post()
        [a1,a2,a3].each { println it.canonicalName() }

        def p2=p1.inverse()
        ledger.post(p2)

        def betrag=Money.euros(10)
        (1..400).each { betrag = betrag * 1.03 }
        def p3 = ledger.posting("Zinsen").credit(betrag, a1).debit(betrag, a2).post()

        Money rial = Money.fromMinor(12345,omanRial)
        def pRial = ledger.posting("Rials").credit(rial, a4).debit(rial, a4)
        ledger.post(pRial)

        Money rialAsEuro = currencyConverterService.convert(rial,EUR)
        currencyConverterService.convert(rial + rial,EUR)
        def pRialInEuro = ledger.posting("Rial in Euro").credit(rialAsEuro, a1).debit(rialAsEuro, a2).post()
    }

    def destroy = {
    }
}
