package accounting

import de.gzockoll.types.money.Money
import org.apache.commons.logging.LogFactory
import org.junit.After
import org.junit.Before
import org.junit.Test

class AccountTests {
    static log = LogFactory.getLog(this)
    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    public void testAccount() {
        def account=new Account(name:"JUNIT",currency: Account.EUR)
        def entry=new Entry(account: account, amount: Money.euros(10));
        entry.post()
        assert account.balance() == Money.euros(10)
        account.save()
        log.debug "Balance:" + account.balance()
        // Account.findAll().each { println "$it.name($it.id): $it.balance()" }
        assert Entry.findAll().size() == 1
    }
}
