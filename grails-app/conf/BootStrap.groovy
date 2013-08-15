import accounting.DetailAccount
import accounting.Posting
import accounting.PostingService
import accounting.SummaryAccount
import com.ibm.icu.util.ULocale
import de.gzockoll.types.money.Money
import com.ibm.icu.util.Currency as Currency

class BootStrap {
    static final EUR = Currency.getInstance("EUR")
    def postingService
    def currencyConverterService

    def init = { servletContext ->
        Currency omanRial = Currency.getInstance(new ULocale.Builder().setRegion("OM").setLanguage("ar").build());

        def a1=new DetailAccount("0000",DetailAccount.EUR)
        def a2=new DetailAccount("0001",DetailAccount.EUR)
        def a3=new DetailAccount("0002",DetailAccount.EUR)
        def a4=new DetailAccount("Oman Rial", omanRial)
        [a1,a2,a3,a4].each { it.save() }

        def posting = new Posting(memo:"Hurz");
        def euros10 = Money.euros(10)
        def p1=posting.credit(euros10,a1).credit(Money.euros(20),a2).debit(Money.euros(30),a3)
        postingService.post(p1)
        // [a1,a2,a3,posting].each { it.save() }

        def s=new SummaryAccount("4000",DetailAccount.EUR)
        s.save()
        [a1,a2,a3].each { s.addAccount(it)}
        s.save()
        [a1,a2,a3].each { println it.canonicalName() }

        def p2=posting.inverse()
        postingService.post(p2)

        def betrag=Money.euros(10)
        (1..400).each { betrag = betrag * 1.03 }
        def p3 = new Posting(memo: "Zinsen").credit(betrag, a1).debit(betrag, a2)
        assert p3.canPost()
        postingService.post(p3)

        Money rial = Money.fromMinor(12345,omanRial)
        postingService.post(new Posting(memo: "Rials").credit(rial,a4).debit(rial,a4))

        Money rialAsEuro = currencyConverterService.convert(rial,EUR)
        postingService.post(new Posting(memo: "Rial in Euro").credit(rialAsEuro,a1).debit(rialAsEuro,a2))
    }

    def destroy = {
    }
}
