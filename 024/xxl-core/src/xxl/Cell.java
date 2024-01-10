package xxl;

import xxl.contents.*;
import xxl.functions.*;
import java.io.Serializable;
import java.io.Serial;

public class Cell implements Serializable{

    @Serial
    private static final long serialVersionUID = 202308312359L;

    private Content _content = null;

    public Cell(Content content){
        _content = content;
    }

    // checks if the content is an integer
    public boolean checkInteger(String contentSpecification) {
        try {
            int number = Integer.parseInt(contentSpecification); 
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // checks if the content is a reference
    public boolean isReference(String contentSpecification){
        if (contentSpecification.startsWith("=") && contentSpecification.contains(";")){
            return true;
        }
        else{
            return false;
        }
    }

    public Reference createReference(String contentSpecification){
        String[] adress = contentSpecification.substring(1).split(";");
        int line = Integer.parseInt(adress[0]);
        int column = Integer.parseInt(adress[1]);
        return new Reference(contentSpecification, line, column);
    }

    // gets the name of the function
    public String getFunctionName(String contentSpecification){
        int i = 0;
        while (contentSpecification.charAt(i) != '('){
            i++;
        }
        String name = contentSpecification.substring(0,i);
        return name;
    }

    // sets the content of the cell
    public void setContent(String contentSpecification){
        if (contentSpecification.startsWith("=") && contentSpecification.contains("(")){
            String name = getFunctionName(contentSpecification);
            if (contentSpecification.contains(":")){
                String[] args = contentSpecification.substring(name.length()+1, contentSpecification.length()-1).split(":");
                Function function = new Function(name, args);
                _content = function;
            }
            else{
                String[] args = contentSpecification.substring(name.length()+1, contentSpecification.length()-1).split(",");
                Function function = new Function(name, args);
                _content = function;
            }
        }
        else if (isReference(contentSpecification) == true){
            Reference reference = createReference(contentSpecification);
            _content = reference;
        }
        else if (checkInteger(contentSpecification) == true){
            int number = Integer.parseInt(contentSpecification);
            LiteralInt intContent = new LiteralInt(number);
            _content = intContent;
        }
        else{
            LiteralString strContent = new LiteralString(contentSpecification);
            _content = strContent;
        }
    }
    
    public Content getCellContent(){
        return _content;
    } 

    public String toString(){
        if (_content != null) {
        return _content.toString();
        }
        else {
            return "";
        }
    }
}