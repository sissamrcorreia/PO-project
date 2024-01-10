package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;
import xxl.exceptions.InvalidCellRangeExceptionCore;

/**
 * Paste command.
 */
class DoPaste extends Command<Spreadsheet> {

    DoPaste(Spreadsheet receiver) {
        super(Label.PASTE, receiver);
        addStringField("gama", Prompt.address());
    }

    @Override
    protected final void execute() throws CommandException {
        try{
            if(_receiver.getCutBuffer() != null){
            _receiver.paste(stringField("gama"));
            }
        } catch (InvalidCellRangeExceptionCore e) {
            throw new InvalidCellRangeException(stringField("gama"));
        }
    }
}
