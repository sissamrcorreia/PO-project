package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;
import xxl.exceptions.InvalidCellRangeExceptionCore;


/**
 * Class for searching functions.
 */
class DoShow extends Command<Spreadsheet> {

    DoShow(Spreadsheet receiver) {
        super(Label.SHOW, receiver);
        addStringField("gama", Prompt.address());
    }

    @Override
    protected final void execute() throws CommandException, InvalidCellRangeException {
        try{
           _display.popup(_receiver.print(stringField("gama")));
        } catch (InvalidCellRangeExceptionCore e) {
            throw new InvalidCellRangeException(stringField("gama"));
        }
    }
}


