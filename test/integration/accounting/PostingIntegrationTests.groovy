package accounting
import com.ibm.icu.util.Currency as Currency
import de.gzockoll.types.money.Money
import org.junit.After
import org.junit.Before
import org.junit.Test

class PostingIntegrationTests extends GroovyTestCase {
    private static final EUR = Currency.getInstance("EUR")
    private static final USD = Currency.getInstance("USD")

    def currencyConverterService
    def postingService

    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testSomething() {
        def ledger=new Ledger()
        def a1 = ledger.newAccount("DetailAccount 1", DetailAccount.EUR)
        def a2 = ledger.newAccount("DetailAccount 2", DetailAccount.EUR)
        def a3 = ledger.newAccount("DetailAccount 3", DetailAccount.EUR)
        ledger.save()

        def posting = ledger.posting("JUNIT").credit(Money.euros(10), a1)
                .credit(Money.euros(20), a2)
                .debit(Money.euros(30), a3).post()
        [a1, a2, a3, posting].each { it.save() }

        assert Posting.findAll().size() >= 1
    }

    @Test
    void testDualCurrencyPosting() {
        def ledger=new Ledger()
        def eur1 = ledger.newAccount("DetailAccount 1", EUR)
        def eur2 = ledger.newAccount("DetailAccount 2", EUR)
        def usd1 = ledger.newAccount("DetailAccount 3", USD)
        def usd2 = ledger.newAccount("DetailAccount 4", USD)
        [eur1,eur2,usd1,usd2].each { it.save() }

        def values = [10, 30, 2, 34, 1020]
        def postEur = new Posting(memo: "TX #1 [EUR]")
        def postUsd = new Posting(memo: "TX #1 [USD]")

        values.each {
            def euros = Money.euros(it)
            postEur.credit(euros, eur1)
            postUsd.credit(currencyConverterService.convert(euros, USD),usd1)
        }
        def sum = Money.euros(values.sum())
        postEur.debit(sum, eur2)
        postUsd.debit(currencyConverterService.convert(sum, USD), usd2)

        postingService.post(postEur)
        postingService.post(postUsd)
        assert postEur.isPosted()
        assert postUsd.isPosted()
    }

    @Test
    void testUnbalancedDualCurrencyPosting() {
        def ledger=new Ledger()
        def eur1 = ledger.newAccount("DetailAccount 1", EUR)
        def eur2 = ledger.newAccount("DetailAccount 2", EUR)
        def usd1 = ledger.newAccount("DetailAccount 3", USD)
        def usd2 = ledger.newAccount("DetailAccount 4", USD)
        [ledger].each { it.save() }

        def values = [10, 30, 2, 34, 1020]
        def postEur = new Posting(memo: "TX #1 [EUR]")
        def postUsd = new Posting(memo: "TX #1 [USD]")

        values.each {
            def euros = Money.euros(it)
            postEur.credit(euros, eur1)
            postUsd.credit(currencyConverterService.convert(euros, USD),usd1)
        }
        def sum = Money.euros(values.sum())
        postEur.debit(sum, eur2)
        postUsd.debit(currencyConverterService.convert(sum, USD), usd2)

        postUsd.credit(Money.fromMinor(1,USD),usd2)
        assert !postUsd.isBalanced()
        assert !postUsd.canPost()

        shouldFail { postingService.post([postEur, postUsd]) }

        assert !postEur.isPosted()
        assert !postUsd.isPosted()
    }

    @Test
    public void testMulti(){
        def ledger=new Ledger()
        def eur1 = ledger.newAccount("multi:DetailAccount 1", EUR)
        def eur2 = ledger.newAccount("multi:DetailAccount 2", EUR)
        ledger.save()

        def multi=ledger.accountByname("multi");
        assert multi != null
        assert multi.balance() == Money.euros(0)

        // def duration= StopWatch.time {1000.times { ledger.posting(it.toString()).credit(Money.euros(it),eur1).debit(Money.euros(it),eur2).post()}}
        def duration=postingService.multipost(ledger,eur1,eur2)
        println duration
        assert eur1.balance().amount() > 0G
        assert multi.balance() == Money.euros(0)

    }
}
