package xxl;

import xxl.exceptions.InvalidCellRangeExceptionCore;
import java.util.HashMap;
import java.util.ArrayList;

import java.io.Serial;
import java.io.Serializable;
import xxl.contents.*;
import xxl.functions.*;

/**
 * Class representing a spreadsheet.
 */
public class Spreadsheet implements Serializable {

    @Serial
    private static final long serialVersionUID = 202308312359L;

    private int _maxLines;              
    private int _maxColumns;
    private String _filename;
    private Spreadsheet _cutBuffer;
    private Cell[][] _spreadsheet;
    private HashMap<String, User> _spreadsheetUsers = new HashMap<String, User>();
    private User _User = new User("root");

    public Spreadsheet(int MaxLines, int MaxColumns, String filename){
        _maxLines = MaxLines;
        _maxColumns = MaxColumns;
        _filename = filename;
        _spreadsheet = new Cell[_maxLines][_maxColumns];
        
        for (int line = 0; line < _maxLines; line++){
            for (int column = 0; column < _maxColumns; column++){
                _spreadsheet[line][column] = new Cell(null);
            }
        }
    }

    /**
     * Checks if the number of the line and column are within the range of the spreadsheet.
     *
     * @param lines the number of lines.
     * @param columns the number of columns.
     * 
     * @throws InvalidCellRangeExceptionCore
     */
    public void checkSpreadsheetLimits(int line, int column, String rangeSpecification) throws InvalidCellRangeExceptionCore {
        if (line > _maxLines || column > _maxColumns){
            throw new InvalidCellRangeExceptionCore();
        }
        else if (line < 1 || column < 1){
            throw new InvalidCellRangeExceptionCore();
        }
        else{}
    }

    /**Checks if the interval given is in the correct format. 
     * 
     * @param firstLine
     * @param lastLine
     * @param firstColumn
     * @param lastColumn
     * 
     * @throws InvalidCellRangeExceptionCore
    */
    public void rangeIsValid(int firstLine, int lastLine, int firstColumn, int lastColumn) throws InvalidCellRangeExceptionCore{
        if (firstLine != lastLine && firstColumn != lastColumn){
            throw new InvalidCellRangeExceptionCore();
        }
    }

    /**
     * Insert specified content in specified range.
     *
     * @param rangeSpecification
     * @param contentSpecification
     * 
     * @throws InvalidCellRangeExceptionCore
     */
    public void insertContents(String rangeSpecification, String contentSpecification) throws InvalidCellRangeExceptionCore{
        if (rangeSpecification.contains(":")){
            String [] range = rangeSpecification.split(":");
            String [] firstCell = range[0].split(";");
            String [] lastCell = range[1].split(";");

            int firstLine = Integer.parseInt(firstCell[0]);
            int firstColumn = Integer.parseInt(firstCell[1]);
            int lastLine = Integer.parseInt(lastCell[0]);
            int lastColumn = Integer.parseInt(lastCell[1]);

            checkSpreadsheetLimits(firstLine, firstColumn, rangeSpecification);
            checkSpreadsheetLimits(lastLine, lastColumn, rangeSpecification);
            rangeIsValid(firstLine, lastLine, firstColumn, lastColumn);

            for (int i = firstLine; i <= lastLine; i++){
                for (int j = firstColumn; j <= lastColumn; j++){
                    _spreadsheet[i-1][j-1].setContent(contentSpecification);
                }
            }
        }
        else { 
        String[] range = rangeSpecification.split(";");
        int line = Integer.parseInt(range[0]);
        int column = Integer.parseInt(range[1]);
        checkSpreadsheetLimits(line, column, rangeSpecification);
        _spreadsheet[line-1][column-1].setContent(contentSpecification);
        }
    }

    /**
     * Get content of specified range.
     *
     * @param rangeSpecification
     * 
     * @throws InvalidCellRangeExceptionCore
     */
    public Content getContent (String rangeSpecification) throws InvalidCellRangeExceptionCore {
        if (rangeSpecification.contains(";")){
            String[] range = rangeSpecification.split(";");
            int line = Integer.parseInt(range[0]);
            int column = Integer.parseInt(range[1]);
            checkSpreadsheetLimits(line, column, rangeSpecification);
            return _spreadsheet[line][column].getCellContent();
        }
        else{
            throw new InvalidCellRangeExceptionCore();
        }
    }

    // removes the last character of a string
    public String removeLastCharacter(String input) {
        return input.substring(0, input.length() - 1);
    }

    /**
     * Prints the reference recieved in the correct format.
     * 
     * @param reference
     */
    public String printReference(Reference reference){
        String result = "";
        String adress = reference.getAdress();

        if (reference.getReferenceContent(this) != null){
            while(reference.getReferenceContent(this) instanceof Reference){
                reference = (Reference) reference.getReferenceContent(this);
            }
            if (reference.getReferenceContent(this) != null){
                result += reference.getReferenceContent(this).toString() + adress + "\n";
            }
            else{
                result += "#VALUE" + adress + "\n";
            }
        }
        else{
            result += "#VALUE" + adress + "\n";
        }
        return result;
    }

    // checks if the function is a binary function
    public boolean isBinaryFunction(String functionName){
        if (functionName.equals("=ADD") || functionName.equals("=SUB") || functionName.equals("=MUL") || functionName.equals("=DIV")){
            return true;
        }
        else{
            return false;
        }
    }

    // checks if the function is an interval function
    public boolean isIntervalFunction(String functionName){
        if (functionName.equals("=COALESCE") || functionName.equals("=AVERAGE") || functionName.equals("=PRODUCT") 
            || functionName.equals("=CONCAT")){
            return true;
        }
        else{
            return false;
        }
    }

    // checks if the argument of the function is an existing reference, wich means that it has a content not null
    public boolean referenceArgsExists(String[] args){
        Boolean result = true;
        for (int i = 0; i < args.length; i++){
            if (args[i].contains(";")){
                String[] adress = args[i].split(";");
                int line = Integer.parseInt(adress[0]);
                int column = Integer.parseInt(adress[1]);
                if (_spreadsheet[line-1][column-1].getCellContent() == null){
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Prints the function recieved in the correct format.
     * 
     * @param function
     */
    public String printFunction(Function function){
        String result = "";
        if (isBinaryFunction(function.getName())){
            BinaryFunctions binaryFunction = new BinaryFunctions(function.getName(), function.getArgs(), this);
            if (referenceArgsExists(function.getArgs())){
                result += binaryFunction.excuteBinaryFunction(function.getName(), function.getArgs()) + function.toString() + "\n";
            }
            else{
                result += "#VALUE" + function.toString() + "\n";
            }
        }
        else if (isIntervalFunction(function.getName())){ 
            IntervalFunctions intervalFunction = new IntervalFunctions(function.getName(), function.getArgs(), this);
            result += intervalFunction.excuteIntervalFunction(function.getName(), function.getArgs()) + function.toString() + "\n";
        }
        else{}
        return result;
    }

    /**
     * Prints the interval or cell recieved in the correct format.
     *
     * @param rangeSpecification
     * 
     * @throws InvalidCellRangeExceptionCore
     */
    public String print(String rangeSpecification) throws InvalidCellRangeExceptionCore{
        String result = "";
        if (rangeSpecification.contains(":")){
            String [] range = rangeSpecification.split(":");
            String [] firstCell = range[0].split(";");
            String [] lastCell = range[1].split(";");

            int firstLine = Integer.parseInt(firstCell[0]);
            int firstColumn = Integer.parseInt(firstCell[1]);
            int lastLine = Integer.parseInt(lastCell[0]);
            int lastColumn = Integer.parseInt(lastCell[1]);

            checkSpreadsheetLimits(firstLine, firstColumn, rangeSpecification);
            checkSpreadsheetLimits(lastLine, lastColumn, rangeSpecification);
            rangeIsValid(firstLine, lastLine, firstColumn, lastColumn);
            for (int i = firstLine; i <= lastLine; i++){
                for (int j = firstColumn; j <= lastColumn; j++){
                    Content cellContent = _spreadsheet[i-1][j-1].getCellContent();
                    if(cellContent instanceof Reference){
                        Reference reference = (Reference) cellContent;
                        result += (i) + ";" + (j) + "|" + printReference(reference);
                    }
                    else if (cellContent instanceof Function){
                        Function function = (Function) cellContent;
                        result += (i) + ";" + (j) + "|" + printFunction(function);
                    }
                    else{
                    result += (i) + ";" + (j) + "|" + _spreadsheet[i-1][j-1].toString() + "\n";
                    }
                }
            }
            result = removeLastCharacter(result);
        }
        else {
            String [] cell = rangeSpecification.split(";");
            int line = Integer.parseInt(cell[0]);
            int column = Integer.parseInt(cell[1]);
            Content cellContent = _spreadsheet[line-1][column-1].getCellContent();
                if(cellContent instanceof Reference){
                    Reference reference = (Reference) cellContent;
                    result = (line) + ";" + (column) + "|" + printReference(reference);
                    result = removeLastCharacter(result);
                }
                else if (cellContent instanceof Function){
                    Function function = (Function) cellContent;
                    result = (line) + ";" + (column) + "|" + printFunction(function);
                    result = removeLastCharacter(result);
                }
                else{
                result = (line) + ";" + (column) + "|" + _spreadsheet[line-1][column-1].toString();
                }
        }
    return result;
    }

    /**Erases the content of all the cells of the given interval.
     * 
     * @param rangeSpecification
     * 
     * @throws InvalidCellRangeExceptionCore
     */
    public void erase(String rangeSpecification) throws InvalidCellRangeExceptionCore{
        if (rangeSpecification.contains(":")){
            String [] range = rangeSpecification.split(":");
            String [] firstCell = range[0].split(";");
            String [] lastCell = range[1].split(";");

            int firstLine = Integer.parseInt(firstCell[0]);
            int firstColumn = Integer.parseInt(firstCell[1]);
            int lastLine = Integer.parseInt(lastCell[0]);
            int lastColumn = Integer.parseInt(lastCell[1]);

            checkSpreadsheetLimits(firstLine, firstColumn, rangeSpecification);
            checkSpreadsheetLimits(lastLine, lastColumn, rangeSpecification);
            rangeIsValid(firstLine, lastLine, firstColumn, lastColumn);

            for (int i = firstLine; i <= lastLine; i++){
                for (int j = firstColumn; j <= lastColumn; j++){
                    _spreadsheet[i-1][j-1].setContent("");
                }
            }
        }
        else {
            String [] cell = rangeSpecification.split(";");
            int line = Integer.parseInt(cell[0]);
            int column = Integer.parseInt(cell[1]);
            _spreadsheet[line-1][column-1].setContent("");
        }
    }

    /**
     * @return the cell matrix that represents the spreadsheet.
     */
    public Cell[][] getSpreadsheetArray(){
        return _spreadsheet;
    }

    /**
     * Sets the filename associated to the spreadsheet.
     * 
     * @param filename
     */
    public void setFilename(String filename){
        _filename = filename;
    }

    // initializes the cut buffer
    public void initializeCutBuffer(){
        _cutBuffer = new Spreadsheet(_maxLines, _maxColumns, null);
    }

    /**
     * @return the cut buffer of the spreadsheet.
     */
    public Spreadsheet getCutBuffer(){
        return _cutBuffer;
    }


    /**
     * Copies the specified range to the cut buffer.
     * 
     * @param rangeSpecification
     * 
     * @throws InvalidCellRangeExceptionCore
     */
    public void copy(String rangeSpecification) throws InvalidCellRangeExceptionCore{
        if (rangeSpecification.contains(":")){
            String [] range = rangeSpecification.split(":");
            String [] firstCell = range[0].split(";");
            String [] lastCell = range[1].split(";");

            int firstLine = Integer.parseInt(firstCell[0]);
            int firstColumn = Integer.parseInt(firstCell[1]);
            int lastLine = Integer.parseInt(lastCell[0]);
            int lastColumn = Integer.parseInt(lastCell[1]);
            int bufferLine = 0;
            int bufferColumn = 0;

            checkSpreadsheetLimits(firstLine, firstColumn, rangeSpecification);
            checkSpreadsheetLimits(lastLine, lastColumn, rangeSpecification);
            rangeIsValid(firstLine, lastLine, firstColumn, lastColumn);

            for (int i = firstLine; i <= lastLine; i++){
                for (int j = firstColumn; j <= lastColumn; j++){
                    if (_spreadsheet[i-1][j-1].getCellContent() instanceof Reference){
                        Reference reference = (Reference) _spreadsheet[i-1][j-1].getCellContent();
                        _cutBuffer._spreadsheet[bufferLine][bufferColumn].setContent(reference.getAdress());
                    }
                    else{
                    _cutBuffer._spreadsheet[bufferLine][bufferColumn].setContent(_spreadsheet[i-1][j-1].getCellContent().toString());
                    }
                bufferColumn++;
                if (bufferColumn >= _cutBuffer._spreadsheet[0].length) {
                    bufferLine++;
                    bufferColumn = 0;
                }
            }
        }
        }
        else {
            String [] cell = rangeSpecification.split(";");
            int line = Integer.parseInt(cell[0]);
            int column = Integer.parseInt(cell[1]);
            if (_spreadsheet[line-1][column-1].getCellContent() instanceof Reference){
                Reference reference = (Reference) _spreadsheet[line-1][column-1].getCellContent();
                _cutBuffer._spreadsheet[0][0].setContent(reference.getAdress());
            }
            _cutBuffer._spreadsheet[0][0].setContent(_spreadsheet[line-1][column-1].getCellContent().toString());
        }
    }


    // prints the cut buffer acording to the specified format
    public String showCutBuffer(){
        String result = "";
        for (int i = 0; i < _cutBuffer._maxLines; i++){
            for (int j = 0; j < _cutBuffer._maxColumns; j++){
                Content cellContent = _cutBuffer._spreadsheet[i][j].getCellContent();
                if (cellContent != null){
                    if(cellContent instanceof Reference){  
                        Reference reference = (Reference) cellContent;
                        result += (i+1) + ";" + (j+1) + "|" + printReference(reference);
                    }
                    else if (cellContent instanceof Function){
                        Function function = (Function) cellContent;
                        result += (i+1) + ";" + (j+1) + "|" + printFunction(function);
                    }
                    else{
                    result += (i+1) + ";" + (j+1) + "|" + _cutBuffer._spreadsheet[i][j].toString() + "\n";
                    }
                }
            }
        }
        result = removeLastCharacter(result);
        return result;
    }

    /** returns an array with all the cells that contain the function with the same name as the functionName received.
     *  Each element of the array is a string with the format "line;column|content".
     * 
     * @param functionName
     */
    public ArrayList<String> getCellsWithFunction(String functionName) {
        ArrayList<String> functions = new ArrayList<String>();

        if (functionName != ""){
            for (int i = 0; i < _maxLines; i++){
                for (int j = 0; j < _maxColumns; j++){
                    if (_spreadsheet[i][j].getCellContent() instanceof Function){
                        Function function = (Function) _spreadsheet[i][j].getCellContent();
                        if (function.getName().substring(1).equals(functionName)){
                            functions.add((i+1) + ";" + (j+1) + "|" + printFunction(function)); 
                        }
                    }
                }
            }
        } 
        return functions;
    }

    /** returns an array with all the cells that contain the value received in their content.
     *  Each element of the array is a string with the format "line;column|content".
     * 
     * @param referenceName
     */
    public ArrayList<String> getCellsWithValue(int Value) {
        ArrayList<String> values = new ArrayList<String>();

        for (int i = 0; i < _maxLines; i++){
            for (int j = 0; j < _maxColumns; j++){
                Content cellContent = _spreadsheet[i][j].getCellContent();
                    if(cellContent instanceof Reference){
                        Reference reference = (Reference) cellContent;
                        if (reference.getReferenceContent(this) instanceof LiteralInt){
                            LiteralInt literalInt = (LiteralInt) reference.getReferenceContent(this);
                            if (literalInt.getValue() == Value){
                                values.add((i+1) + ";" + (j+1) + "|" + printReference(reference));
                            }
                        }
                    }
                    else if (cellContent instanceof Function){
                        Function function = (Function) cellContent;
                        if (isBinaryFunction(function.getName())){
                            BinaryFunctions binaryFunction = new BinaryFunctions(function.getName(), function.getArgs(), this);
                            if (binaryFunction.excuteBinaryFunction(function.getName(), function.getArgs()) == Value){
                                values.add((i+1) + ";" + (j+1) + "|" + printFunction(function));
                            }
                        }
                        //else if (isIntervalFunction(function.getName())) {
                        //    IntervalFunctions intervalFunction = new IntervalFunctions(function.getName(), function.getArgs(), this);
                        //    if (intervalFunction.excuteIntervalFunction(function.getName(), function.getArgs()) == Value){
                        //        values.add((i+1) + ";" + (j+1) + "|" + printFunction(function));
                        //    }
                        //}
                    }
                    else if (cellContent instanceof LiteralInt){
                        LiteralInt literalInt = (LiteralInt) cellContent;
                        if (literalInt.getValue() == Value){
                            values.add((i+1) + ";" + (j+1) + "|" + _spreadsheet[i][j].toString() + "\n");
                        }
                    }
                    else{}
                }
            }
        return values;
    }

    /**prints an array of Strings.
     * 
     * @param arrayList
     */
    public String printArrayListOfStrings(ArrayList<String> arrayList){
        String result = "";
        for (int i = 0; i < arrayList.size(); i++){
            result += arrayList.get(i);
        }
        result = removeLastCharacter(result);
        return result;
    }

    //returns the range of the cut buffer in the format "firstLine;lastLine:firstColumn;lastColumn".
    public String getCutBufferSize(){
        String result = "";
        String lastLine = Integer.toString(_cutBuffer._maxLines);
        String lastColumn = Integer.toString(_cutBuffer._maxColumns);
        return "1;" + lastLine + ":" + "1;" + lastColumn;
    }

    /**allocates the content of the cutBuffer int the interval received of the spreasheet.
     * If its received only a reference, it allocates the content of the cutBuffer starting on that reference.
     * 
     * @param rangeSpecification
     * 
     * @throws InvalidCellRangeExceptionCore
     */
    public void paste(String rangeSpecification) throws InvalidCellRangeExceptionCore{
        if (rangeSpecification.contains(":")){
            String [] range = rangeSpecification.split(":");
            String [] firstCell = range[0].split(";");
            String [] lastCell = range[1].split(";");

            int firstLine = Integer.parseInt(firstCell[0]);
            int firstColumn = Integer.parseInt(firstCell[1]);
            int lastLine = Integer.parseInt(lastCell[0]);
            int lastColumn = Integer.parseInt(lastCell[1]);
            int bufferLine = 0;
            int bufferColumn = 0;

            checkSpreadsheetLimits(firstLine, firstColumn, rangeSpecification);
            checkSpreadsheetLimits(lastLine, lastColumn, rangeSpecification);
            rangeIsValid(firstLine, lastLine, firstColumn, lastColumn);

            for (int i = firstLine; i <= lastLine; i++){
                for (int j = firstColumn; j <= lastColumn; j++){
                    if (_cutBuffer._spreadsheet[bufferLine][bufferColumn].getCellContent() instanceof Reference){
                        Reference reference = (Reference) _cutBuffer._spreadsheet[bufferLine][bufferColumn].getCellContent();
                        _spreadsheet[i-1][j-1].setContent(reference.getAdress());
                    }
                    else{
                    _spreadsheet[i-1][j-1].setContent(_cutBuffer._spreadsheet[bufferLine][bufferColumn].getCellContent().toString());
                    }
                bufferColumn++;
                if (bufferColumn >= _cutBuffer._spreadsheet[0].length) {
                    bufferLine++;
                    bufferColumn = 0;
                }
            }
        }
        }
        else {
            String [] cell = rangeSpecification.split(";");
            int line = Integer.parseInt(cell[0]);
            int column = Integer.parseInt(cell[1]);
            int[] bufferRange = this.getCutBufferRange();

            for (int i = 0; i < bufferRange[0]; i++){
                for (int j = 0; j < bufferRange[1]; j++){
                    if (_cutBuffer._spreadsheet[i][j].getCellContent() instanceof Reference){
                        Reference reference = (Reference) _cutBuffer._spreadsheet[i][j].getCellContent();
                        _spreadsheet[line-1][column-1].setContent(reference.getAdress());
                    }
                    else{
                    _spreadsheet[line-1][column-1].setContent(_cutBuffer._spreadsheet[i][j].getCellContent().toString());
                    }
                    column++;
                    if (column >= _spreadsheet[0].length) {
                        line++;
                        column = 0;
                    }
                }
            }
        }
    }

    /** returns an array where the elements corresponde to the last line and last column 
     * of the cutBuffer in which the content is not null, respectively.
     *  */
    public int[] getCutBufferRange(){
        int[] result = new int[2];
        int lastLine = 0;
        int lastColumn = 0;

        for (int i = 0; i < _cutBuffer._maxLines; i++){
            for (int j = 0; j < _cutBuffer._maxColumns; j++){
                if (_cutBuffer._spreadsheet[i][j].getCellContent() != null){
                    lastLine = i+1;
                    lastColumn = j+1;
                }
            }
        }
        result[0] = lastLine;
        result[1] = lastColumn;
        return result;
    }
}
