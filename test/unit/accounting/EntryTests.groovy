package accounting
import de.gzockoll.types.money.Money
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Entry)
@Mock([DetailAccount])
class EntryTests {

    public static final Currency EUR = Currency.getInstance("EUR")

    void testSomething() {
        def account=new DetailAccount(null,"JUnit",EUR)
        assert account.balance() == Money.euros(0)

        def entry=new Entry(account, Money.euros(10),new Posting())

        entry.post();
        assert account.balance() == Money.euros(10)
    }

    void testNegateMode() {
        assert Entry.Mode.CREDIT.negate() == Entry.Mode.DEBIT
        assert Entry.Mode.DEBIT.negate() == Entry.Mode.CREDIT
        assert Entry.Mode.CREDIT.negate().negate() == Entry.Mode.CREDIT
        assert Entry.Mode.DEBIT.negate().negate() == Entry.Mode.DEBIT
    }
}
