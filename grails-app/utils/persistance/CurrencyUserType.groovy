package persistance

import org.hibernate.type.StringType

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
class CurrencyUserType extends AbstractImmutableType {
    @Override
    int[] sqlTypes() {
        [StringType.INSTANCE.sqlType()]
    }

    @Override
    Class returnedClass() {
        return Currency.class
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws SQLException {
        assert names.length == 1;
        def iso = StringType.INSTANCE.get(rs,names[0])
        iso ? Currency.getInstance(iso) : null
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index) throws SQLException {
        if (value == null) {
            StringType.INSTANCE.set(st, null, index);
        } else {
            StringType.INSTANCE.set(st, value.getCurrencyCode(), index);
        }
    }
}
