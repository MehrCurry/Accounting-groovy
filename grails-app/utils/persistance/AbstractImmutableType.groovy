package persistance

import org.hibernate.usertype.UserType

/**
 * Created with IntelliJ IDEA.
 * User: Guido Zockoll
 * Date: 12.08.13
 * Time: 14:40
 * To change this template use File | Settings | File Templates.
 */
abstract class AbstractImmutableType implements UserType {
    def AbstractImmutableType() {
        super();
    }

    public boolean isMutable() {
        return false;
    }

    public Serializable disassemble(Object value) {
        return (Serializable) value;
    }

    public Object assemble(Serializable cached, Object owner) {
        return cached;
    }

    public Object deepCopy(Object value) {
        return value;
    }

    public Object replace(Object original, Object target,
                          Object owner) {
        return original;
    }

    public boolean equals(Object x, Object y) {
        if (x != null && y != null) {
            return x.equals(y);
        }
        // Two nulls are equal as well
        return x == null && y == null;
    }

    public int hashCode(Object x) {
        if (x != null) {
            return x.hashCode();
        }
        return 0;
    }
}
