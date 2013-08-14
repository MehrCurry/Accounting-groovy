import com.ibm.icu.util.Currency
import de.gzockoll.types.money.Money
import org.hibernate.type.BigDecimalType
import org.hibernate.type.CurrencyType

import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
/**
 * Created with IntelliJ IDEA.
 * User: Guido Zockoll
 * Date: 12.08.13
 * Time: 14:34
 * To change this template use File | Settings | File Templates.
 */
class MoneyUserType extends AbstractImmutableType {
    @Override
    int[] sqlTypes() {
        [BigDecimalType.INSTANCE.sqlType(), CurrencyType.INSTANCE.sqlType()]
    }

    @Override
    Class returnedClass() {
        return Money.class
    }

    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws SQLException {
        assert names.length == 2;
        BigDecimal amount = BigDecimalType.INSTANCE.get(names[0]); // already handles null check
        Currency currency = CurrencyType.INSTANCE.get(names[1]); // already handles null check
        return amount == null && currency == null ? null : Money.fromMinor(amount, currency);
    }

    public void nullSafeSet(PreparedStatement st, Object value, int index) throws SQLException {
        if (value == null) {
            BigDecimalType.INSTANCE.set(st, null, index);
            CurrencyType.INSTANCE.set(st, null, index + 1);
        } else {
            final Money money = (Money) value;
            BigDecimalType.INSTANCE.set(st, money.value, index);
            CurrencyType.INSTANCE.set(st, money.currency, index + 1);
        }
    }
}
