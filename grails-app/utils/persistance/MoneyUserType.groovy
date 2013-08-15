package persistance

import com.ibm.icu.util.Currency
import de.gzockoll.types.money.Money
import org.hibernate.HibernateException
import org.hibernate.engine.SessionImplementor
import org.hibernate.type.BigDecimalType
import org.hibernate.type.StringType
import org.hibernate.type.Type
import org.hibernate.usertype.CompositeUserType

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
class MoneyUserType implements CompositeUserType {
    @Override
    String[] getPropertyNames() {
        ["amount","currency"]  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    Type[] getPropertyTypes() {
        [BigDecimalType.INSTANCE,StringType.INSTANCE]  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    Object getPropertyValue(Object o, int i) throws HibernateException {
        // assert i == 2
        def money=(Money) o
        switch(i) {
            case 0:
                return money.value
            case 1:
                return money.currency
            default:
                return null
        }
    }

    @Override
    void setPropertyValue(Object o, int i, Object data) throws HibernateException {
        def money=(Money) o
        switch(i) {
            case 0:
                money.value=data
            case 1:
                money.currency=data
        }
    }

    @Override
    Class returnedClass() {
        return Money.class
    }

    @Override
    boolean equals(Object o, Object o1) throws HibernateException {
        if (o == o1)
            return true
        if (o == null || o1 == null)
            return false
        return o.equals(o1)
    }

    @Override
    int hashCode(Object o) throws HibernateException {
        o.hashCode()
    }

    @Override
    Object nullSafeGet(ResultSet resultSet, String[] strings, SessionImplementor sessionImplementor, Object o) throws HibernateException, SQLException {
        assert strings.length == 2;
        BigDecimal amount = BigDecimalType.INSTANCE.get(names[0]); // already handles null check
        def iso = StringType.INSTANCE.get(names[1])
        Currency currency = Currency.getInstance(iso); // already handles null check
        return amount == null && currency == null ? null : Money.fromMinor(amount, currency);
    }

    @Override
    void nullSafeSet(PreparedStatement preparedStatement, Object o, int index, SessionImplementor sessionImplementor) throws HibernateException, SQLException {
        if (o == null) {
            BigDecimalType.INSTANCE.set(preparedStatement, null, index,sessionImplementor);
            StringType.INSTANCE.set(preparedStatement, null, index + 1,sessionImplementor);
        } else {
            final Money money = (Money) o;
            BigDecimalType.INSTANCE.set(preparedStatement, money.scaled().value, index,sessionImplementor);
            StringType.INSTANCE.set(preparedStatement, money.currency.getCurrencyCode(), index + 1,sessionImplementor);
        }
    }

    @Override
    Object deepCopy(Object o) throws HibernateException {
        Money.fromMinor(o.value,o.currency)
    }

    @Override
    boolean isMutable() {
        return false
    }

    @Override
    Serializable disassemble(Object o, SessionImplementor sessionImplementor) throws HibernateException {
        o
    }

    @Override
    Object assemble(Serializable serializable, SessionImplementor sessionImplementor, Object o) throws HibernateException {
        serializable
    }

    @Override
    Object replace(Object o, Object o1, SessionImplementor sessionImplementor, Object o2) throws HibernateException {
        this.deepCopy(o)
    }
}
