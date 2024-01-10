package xxl.contents;

import java.io.Serial;
import java.io.Serializable;

public class LiteralString extends Content implements Serializable {

    private String _value;

    @Serial
    private static final long serialVersionUID = 202308312359L;

    public LiteralString(String value) {
        _value = value;
    }

    public String getValue() {
        return _value;
    }

    public String toString() {
        return _value;
    }
}