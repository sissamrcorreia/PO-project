package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;
import xxl.exceptions.InvalidCellRangeExceptionCore;

/**
 * Copy command.
 */
class DoCopy extends Command<Spreadsheet> {

    DoCopy(Spreadsheet receiver) {
        super(Label.COPY, receiver);
        addStringField("gama", Prompt.address());
    }

    @Override
    protected final void execute() throws CommandException {
        try{
        _receiver.copy(stringField("gama"));
        } catch (InvalidCellRangeExceptionCore e) {
            throw new InvalidCellRangeException(stringField("gama"));
        }
    }
}

