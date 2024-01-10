package xxl.contents;

import java.io.Serializable;
import java.io.Serial;

public class LiteralInt extends Content implements Serializable {

    private int _value;

    @Serial
    private static final long serialVersionUID = 202308312359L;

    public LiteralInt(int value) {
        _value = value;
    }

    public int getValue() {
        return _value;
    }

    public String toString() {
        return Integer.toString(_value);
    }
}