package xxl.functions;

import java.io.Serializable;
import java.io.Serial;
import xxl.Spreadsheet;
import xxl.contents.*;
import xxl.Cell;

public class BinaryFunctions extends Function implements Serializable {

    @Serial
    private static final long serialVersionUID = 202308312359L;

    private Spreadsheet _spreadsheet;

    public BinaryFunctions(String functionName, String[] arguments, Spreadsheet spreadsheet) {
        super(functionName, arguments);
        _spreadsheet = spreadsheet;
    }

    public boolean checkReference(String contentSpecification){
        if (contentSpecification.contains(";")){
            return true;
        }
        else{
            return false;
        }
    }

    public  Reference getReference(String contentSpecification){
        String[] adress = contentSpecification.split(";");
        int line = Integer.parseInt(adress[0]);
        int column = Integer.parseInt(adress[1]);
        return new Reference(contentSpecification, line, column);
    }

    public int[] getIntContent(String[] args){
        int[] result = new int[args.length];
        Cell cell = new Cell(null);

        for(int i = 0; i < args.length; i++){
            if (checkReference(args[i]) == true){  
                Reference reference = getReference(args[i]);
                Content refContent = reference.getReferenceContent(_spreadsheet);
                if (refContent instanceof LiteralInt){
                    result[i] = ((LiteralInt) refContent).getValue();
                }
            }
            else if (cell.checkInteger(args[i]) == true){
                result[i] = Integer.parseInt(args[i]);
            }
            else{}
        }
        return result;
    }

    public int add(int a, int b){
        return a + b;
    }

    public int sub(int a, int b){
        return a - b;
    }

    public int mul(int a, int b){
        return a * b;
    }

    public int div(int a, int b){
        return a / b;
    }

    public int excuteBinaryFunction(String functionName, String[] arguments){
        switch (functionName) {
        case "=ADD" : return add(getIntContent(arguments)[0], getIntContent(arguments)[1]);
        case "=SUB" : return sub(getIntContent(arguments)[0], getIntContent(arguments)[1]);
        case "=MUL" : return mul(getIntContent(arguments)[0], getIntContent(arguments)[1]);
        case "=DIV" : return div(getIntContent(arguments)[0], getIntContent(arguments)[1]);
        }
        return 0;
    }
}

