package accounting

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
        account.save()
        assert Account.findAll().size() == 1
    }
}
