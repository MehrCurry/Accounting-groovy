package accounting

import com.ibm.icu.util.Currency as Currency
import de.gzockoll.types.money.Money
/**
 * Created with IntelliJ IDEA.
 * User: Guido Zockoll
 * Date: 14.08.13
 * Time: 14:46
 * To change this template use File | Settings | File Templates.
 */
abstract class Account {
    static belongsTo = [parent:SummaryAccount]

    static constraints = {
        parent nullable: true
    }

    abstract void post(Entry entry)
    abstract Money balance()
    abstract Currency getCurrency()
    String getName() {
        parent!=null ? "$parent.name:$name" : name
    }
}