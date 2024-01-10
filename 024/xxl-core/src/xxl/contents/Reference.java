package xxl.contents;

import java.io.Serializable;
import java.io.Serial;
import xxl.Spreadsheet;

public class Reference extends Content implements Serializable{

    private String _adress = null;
    private int _line;
    private int _column;

    @Serial
    private static final long serialVersionUID = 202308312359L;

    public Reference(String adress, int line, int column){
        _adress = adress;
        _line = line;
        _column = column;
    }

    public String getAdress(){
        return _adress;
    }
    
    public Content getReferenceContent(Spreadsheet spreadsheet){
        return spreadsheet.getSpreadsheetArray()[_line-1][_column-1].getCellContent();
    }

}