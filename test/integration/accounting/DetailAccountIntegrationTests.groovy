package accounting

import org.apache.commons.logging.LogFactory
import org.junit.After
import org.junit.Before
import org.junit.Test

class DetailAccountIntegrationTests {
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
        def account=new DetailAccount(name:"JUNIT",currency: DetailAccount.EUR)
        account.save()
        assert DetailAccount.findAll().size() == 1
    }
}
