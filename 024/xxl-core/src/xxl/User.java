package xxl;

import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;
import java.io.Serial;

public class User implements Serializable{

    @Serial
    private static final long serialVersionUID = 202308312359L;

    private String _username;
    private Map<String, Spreadsheet> _spreadsheets = new HashMap<String, Spreadsheet>();

    public User(String username){
        _username = username;
    }

}