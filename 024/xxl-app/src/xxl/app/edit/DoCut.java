package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;
import xxl.exceptions.InvalidCellRangeExceptionCore;

/**
 * Cut command.
 */
class DoCut extends Command<Spreadsheet> {

    DoCut(Spreadsheet receiver) {
        super(Label.CUT, receiver);
        addStringField("gama", Prompt.address());
    }

    @Override
    protected final void execute() throws CommandException {
        try{
        _receiver.copy(stringField("gama"));
        _receiver.erase(stringField("gama"));
        } catch (InvalidCellRangeExceptionCore e) {
            throw new InvalidCellRangeException(stringField("gama"));
        }
    }

}
