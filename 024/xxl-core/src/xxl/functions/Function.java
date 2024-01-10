package xxl.functions;

import xxl.contents.*;
import java.io.Serializable;
import java.io.Serial;

public class Function extends Content implements Serializable{

    @Serial
    private static final long serialVersionUID = 202308312359L;

    private String _name;
    private String[] _args;
    
    public Function(String name, String[] args){
        _name = name;
        _args = args;
    }
    
    public String getName(){
        return _name;
    }
    
    public String[] getArgs(){
        return _args;
    }
    
    public String toString(){
        String result = _name + "(";
        for(int i = 0; i < _args.length; i++){
            result += _args[i].toString();
            if(i < _args.length - 1){
                result += ",";
            }
        }
        result += ")";
        return result;
    }

}