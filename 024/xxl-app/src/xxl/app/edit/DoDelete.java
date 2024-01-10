package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;
import xxl.exceptions.InvalidCellRangeExceptionCore;

/**
 * Delete command.
 */
class DoDelete extends Command<Spreadsheet> {

    DoDelete(Spreadsheet receiver) {
        super(Label.DELETE, receiver);
        addStringField("gama", Prompt.address());
    }

    @Override
    protected final void execute() throws CommandException {
        try{
            _receiver.erase(stringField("gama"));
        } catch (InvalidCellRangeExceptionCore e) {
            throw new InvalidCellRangeException(stringField("gama"));
        }
    }
}
