package accounting
import de.gzockoll.types.money.Money
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.joda.time.DateTime

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Posting)
@Mock([Posting,DetailAccount,Entry,Ledger,PostingService,SummaryAccount,DetailAccount])
class PostingTests {

    void testSomething() {
        def ledger=new Ledger()
        ledger.postingService=new PostingService()
        def account=ledger.newAccount(this.class.getSimpleName(),"EUR")
        def trans=ledger.posting("Bla");
        assert trans.canPost() == false
        assert trans.credit(Money.euros(10),account).canPost() == false
        assert trans.credit(Money.euros(20),account).canPost() == false
        assert trans.debit(Money.euros(30),account).canPost() == true

        assert trans.isPosted() == false
        trans.post()
        assert trans.isPosted() == true

        assert account.balance() == Money.euros(0)
        assert account.entries.size() == 3
    }

    void testUnbalancedPostingShouldFail() {
        def ledger=new Ledger()
        ledger.postingService=new PostingService()
        def account=ledger.newAccount(this.class.getSimpleName(),"EUR")
        def trans=ledger.posting("Bla");
        trans.credit(Money.euros(10),account)
                .debit(Money.euros(20),account)
        shouldFail { trans.post()}
    }

    void testStorno() {
        def ledger=new Ledger()
        ledger.postingService=new PostingService()
        def account1=ledger.newAccount(this.class.getSimpleName()+"-1","EUR")
        def account2=ledger.newAccount(this.class.getSimpleName()+"-2","EUR")

        def ten = Money.euros(10)
        def trans=ledger.posting("Bla")
                .credit(ten,account1)
                .debit(ten,account2)

        def zero = Money.euros(0)
        assert account1.balance() == zero
        assert account2.balance() == zero
        trans.post()
        assert account1.balance() == ten
        assert account2.balance() == ten.negate()
        trans.inverse(DateTime.now()).post()
        assert account1.balance() == zero
        assert account2.balance() == zero
    }
}
