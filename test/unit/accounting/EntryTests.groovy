package accounting
import de.gzockoll.types.money.Money
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Entry)
@Mock([Account])
class EntryTests {

    public static final com.ibm.icu.util.Currency EUR = com.ibm.icu.util.Currency.getInstance("EUR")

    void testSomething() {
        def account=new Account("JUnit",EUR)
        assert account.balance() == Money.euros(0)

        def entry=new Entry(account, Money.euros(10),new Posting())

        entry.post();
        assert account.balance() == Money.euros(10)
    }
}
