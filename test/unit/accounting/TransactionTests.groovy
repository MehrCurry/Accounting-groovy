package accounting

import de.gzockoll.types.money.Money
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Transaction)
@Mock([Account,Entry])
class TransactionTests {

    void testSomething() {
        def account=new Account(this.class.getSimpleName(),Account.EUR)
        def trans=new Transaction();
        assert trans.canPost() == false
        trans.add(Money.euros(10),account,"Buchung 1")
        assert trans.canPost() == false
        trans.add(Money.euros(20),account,"Buchung 2")
        assert trans.canPost() == false
        trans.add(Money.euros(-30),account,"Buchung 3")
        assert trans.canPost() == true

        assert trans.isPosted() == false
        trans.post()
        assert trans.isPosted() == true

        assert account.balance() == Money.euros(0)
        assert account.entries.size() == 3
    }
}
