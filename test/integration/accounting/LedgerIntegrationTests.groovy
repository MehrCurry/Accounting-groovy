package accounting
import de.gzockoll.types.money.Money
/**
 * Created with IntelliJ IDEA.
 * User: Guido Zockoll
 * Date: 30.08.13
 * Time: 09:33
 * To change this template use File | Settings | File Templates.
 */
class LedgerIntegrationTests extends GroovyTestCase {

    void testMultiCurrency() {
        def ledger=new Ledger(name: 'Test')
        def eur1 = ledger.newAccount("EUR:DetailAccount 1", "EUR")
        def eur2 = ledger.newAccount("EUR:DetailAccount 2", "EUR")
        def usd1 = ledger.newAccount("USD:DetailAccount 3", "USD")
        def usd2 = ledger.newAccount("USD:DetailAccount 4", "USD")
        ledger.save()

        ledger.posting("Post1").credit(Money.fromMajor(10,"USD"),usd1).debit(Money.fromMajor(10,"USD"),usd2).post()
        ledger.posting("Post 2").credit(Money.fromMajor(11,"EUR"),eur1).debit(Money.fromMajor(11,"EUR"),eur2).post()
        ledger.save()

        assert ledger.isBalanced()


    }

}
