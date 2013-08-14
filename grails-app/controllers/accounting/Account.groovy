package accounting

import de.gzockoll.types.money.Money

/**
 * Created with IntelliJ IDEA.
 * User: Guido Zockoll
 * Date: 14.08.13
 * Time: 14:46
 * To change this template use File | Settings | File Templates.
 */
public interface Account {
    void post(Entry entry)
    Money balance()
}