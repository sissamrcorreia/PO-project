package xxl.app.edit;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import xxl.Spreadsheet;
import xxl.exceptions.InvalidCellRangeExceptionCore;

/**
 * Class for inserting data.
 */
class DoInsert extends Command<Spreadsheet> {

    DoInsert(Spreadsheet receiver) {
        super(Label.INSERT, receiver);
        addStringField("gama", Prompt.address());
        addStringField("conteudo", Prompt.content());
    }

    @Override
    protected final void execute() throws CommandException {
        try{
        _receiver.insertContents(stringField("gama"), stringField("conteudo"));
    } catch (InvalidCellRangeExceptionCore e) {
        throw new InvalidCellRangeException(stringField("gama"));
    }
    }

}
