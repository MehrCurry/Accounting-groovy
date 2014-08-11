import de.gzockoll.types.money.Money
import persistance.MoneyUserType

/**
 * Created with IntelliJ IDEA.
 * User: Guido Zockoll
 * Date: 12.08.13
 * Time: 14:52
 * To change this template use File | Settings | File Templates.
 */
class MoneyUserTypeTest extends GroovyTestCase {
    static final EUR=Currency.getInstance("EUR")
    MoneyUserType cut

    void setUp() {
        this.cut=new MoneyUserType()
    }

    void testSqlTypes() {
        assert cut.getPropertyTypes().size() == 2
    }

    void testReturnedClass() {
        assert cut.returnedClass() == Money.class
    }
}
