package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import xxl.Spreadsheet;
import xxl.exceptions.InvalidCellRangeExceptionCore;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show cut buffer command.
 */
class DoShowCutBuffer extends Command<Spreadsheet> {

    DoShowCutBuffer(Spreadsheet receiver) {
        super(Label.SHOW_CUT_BUFFER, receiver);
    }

    @Override
    protected final void execute() throws CommandException {
        _display.popup(_receiver.showCutBuffer());
    }

}
