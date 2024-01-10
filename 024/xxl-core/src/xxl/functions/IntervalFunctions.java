package xxl.functions;

import java.io.Serial;
import java.io.Serializable;
import xxl.Spreadsheet;
import xxl.Cell;
import xxl.contents.*;
import xxl.exceptions.InvalidCellRangeExceptionCore;
import xxl.exceptions.ContentOfIntervalNotAllInteger;
import java.util.ArrayList;
import java.util.List;

public class IntervalFunctions extends Function implements Serializable {

    @Serial
    private static final long serialVersionUID = 202308312359L;
    
    private Spreadsheet _spreadsheet;

    public IntervalFunctions(String functionName, String[] arguments, Spreadsheet spreadsheet) {
        super(functionName, arguments);
        _spreadsheet = spreadsheet;
    }


    /**returns true if the content of all the cells of the interval are integer
     * 
     * @param args
    */
    public boolean isAllInteger(int firstLine, int lastLine, int firstColumn, int lastColumn){
        Cell cell = new Cell(null);
        for (int i = 0; i < firstLine; i++){
                for (int j = 0; j < firstColumn; j++){
                    Reference reference = cell.createReference("=" + i + ";" + j);
                if (reference.getReferenceContent(_spreadsheet) instanceof LiteralInt){
                    return true;
                }
                else{
                    return false;
                }
            }
        }
        return false;
    }

    /**returns an array with the content of the cell only if that content is instance of LiteralInt 
     * 
     * @param args
    */
    public List<Integer> getArgIntContent(int firstLine, int lastLine, int firstColumn, int lastColumn){
        System.out.println("entrou em ArgsIntContent");
        Cell cell = new Cell(null);
        List<Integer> result = new ArrayList<>();

        if (isAllInteger(firstLine, lastLine, firstColumn, lastColumn)){
            for (int i = 0; i < firstLine; i++){
                for (int j = 0; j < firstColumn; j++){
                    Reference reference = cell.createReference("=" + i + ";" + j);
                    if (reference.getReferenceContent(_spreadsheet) instanceof LiteralInt){
                        result.add(((LiteralInt) reference.getReferenceContent(_spreadsheet)).getValue());
                    }
                }
            }
        }
        return result;
    }

    /**returns the product of all the int contents of the given interval
     * 
     * @param args
     * @param spreadsheet
     * 
     * @throws InvalidCellRangeExceptionCore
    */
    public int product(String[] args, Spreadsheet spreadsheet) /**throws InvalidCellRangeExceptionCore*/{
        String[] firstCell = args[0].split(";");
        String[] lastCell  = args[1].split(";");
        Cell cell = new Cell(null);
        int result = 1;
        System.out.println(args[0] + " " + args[1]);
        System.out.println(firstCell[0] +" " + firstCell[1]);
        System.out.println(lastCell[0] +" " + lastCell[1]);
        int firstLine = Integer.parseInt(firstCell[0]);
        int lastLine  = Integer.parseInt(lastCell[0]);
        int firstColumn = Integer.parseInt(firstCell[1]);
        int lastColumn  = Integer.parseInt(lastCell[1]);
        List<Integer> argIntContent = getArgIntContent(firstLine, lastLine, firstColumn, lastColumn);
        
        //_spreadsheet.rangeIsValid(firstLine, lastLine, firstColumn, lastColumn);  
        System.out.println(argIntContent);
        for (int contentInt : argIntContent){
                    result *= contentInt;
                }
        return result;
        }

    /**return the result of all the operations possible for interval functions
     * 
     * @param functionName
     * @param arguments
     */
    public int excuteIntervalFunction(String functionName, String[] arguments){
        switch (functionName) {
        //case "=CONCAT"   : return concat();
        //case "=COALESCE" : return coalesce();
        case "=PRODUCT"  : return product(arguments, _spreadsheet);
        //try{
        //    return product(arguments, _spreadsheet);
        //} catch (InvalidCellRangeExceptionCore e) {
        //}
        //case "=AVERAGE"  : return average();
        }
        return 0;
    }
}