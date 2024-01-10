package xxl.app.search;

import pt.tecnico.uilib.menus.Command;
import xxl.Spreadsheet;

/**
 * Command for searching function names.
 */
class DoShowFunctions extends Command<Spreadsheet> {

    DoShowFunctions(Spreadsheet receiver) {
        super(Label.SEARCH_FUNCTIONS, receiver);
        addStringField("function", Prompt.searchFunction());
    }

    @Override
    protected final void execute() {
        _display.popup(_receiver.printArrayListOfStrings(_receiver.getCellsWithFunction(stringField("function"))));
    }

}
