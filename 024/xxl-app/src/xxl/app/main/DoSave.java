package xxl.app.main;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import xxl.Calculator;
import java.io.IOException;
import xxl.exceptions.MissingFileAssociationException;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Save to file under current name (if unnamed, query for name).
 */
class DoSave extends Command<Calculator> {

    DoSave(Calculator receiver) {
        super(Label.SAVE, receiver, xxl -> xxl.getSpreadsheet() != null);
    }

    @Override
    protected final void execute() throws CommandException {
        try {
      try {
        _receiver.save();
      } catch (MissingFileAssociationException ex) {
        saveAs();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void saveAs() throws IOException {
    try {
      _receiver.saveAs(Form.requestString(Prompt.newSaveAs()));
    } catch (MissingFileAssociationException e) {
      saveAs();
    }
  }
}

