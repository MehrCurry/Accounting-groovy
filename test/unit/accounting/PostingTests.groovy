package accounting

import de.gzockoll.types.money.Money
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Posting)
@Mock([Posting,Account,Entry])
class PostingTests {

    void testSomething() {
        def account=new Account(this.class.getSimpleName(),Account.EUR)
        def trans=new Posting(memo:"Bla");
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
        def account=new Account(this.class.getSimpleName(),Account.EUR)
        def trans=new Posting(memo: "Hurz");
        trans.credit(Money.euros(10),account)
                .debit(Money.euros(20),account)
        shouldFail { trans.post()}
    }
}
