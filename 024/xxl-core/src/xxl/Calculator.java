package xxl;

import java.io.IOException;
import java.io.FileNotFoundException;

import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import xxl.exceptions.ImportFileException;
import xxl.exceptions.MissingFileAssociationException;
import xxl.exceptions.UnavailableFileException;
import xxl.exceptions.InvalidCellRangeExceptionCore;
import java.io.Serial;


import java.util.HashMap;

/**
 * Class representing a spreadsheet application.
 */
public class Calculator implements Serializable{

    @Serial
    private static final long serialVersionUID = 202308312359L;

    /** The current spreadsheet. */
    private Spreadsheet _spreadsheet = null;

    /** The current file name. */
    private String _filename;

    /** Map of users */
    private HashMap<String, User> _allUsers = new HashMap<String, User>();

    /**the current User */
    private User _currentUser;

    /**
     * Creates a new spreadsheet with the specified number of lines and columns.
     *
     * @param maxLines the number of lines of the new spreadsheet.
     * @param maxColumns the number of columns of the new spreadsheet.
    */
    public void newSpreadsheet(int maxLines, int maxColumns) {
        if (_spreadsheet != null){
            _spreadsheet = null;
            _filename = null;
        }
        _spreadsheet = new Spreadsheet(maxLines, maxColumns,null);
        _spreadsheet.initializeCutBuffer();
    }


    /**
     * Saves the serialized application's state into the file associated to the current network.
     *
     * @throws FileNotFoundException if for some reason the file cannot be created or opened. 
     * @throws MissingFileAssociationException if the current network does not have a file.
     * @throws IOException if there is some error while serializing the state of the network to disk.
     */
    public void save() throws FileNotFoundException, MissingFileAssociationException, IOException {
        if (_filename == null || _filename.equals(""))
            throw new MissingFileAssociationException();
        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(_filename)))) {
            oos.writeObject(this._spreadsheet);  
        }
    }

    /**
     * Saves the serialized application's state into the specified file. The current network is
     * associated to this file.
     *
     * @param filename the name of the file.
     * @throws FileNotFoundException if for some reason the file cannot be created or opened.
     * @throws MissingFileAssociationException if the current network does not have a file.
     * @throws IOException if there is some error while serializing the state of the network to disk.
     */
    public void saveAs(String filename) throws FileNotFoundException, MissingFileAssociationException, IOException {
        _filename = filename;
        _spreadsheet.setFilename(filename);
        save();
    }

    /**
     * @param filename name of the file containing the serialized application's state
     *        to load.
     * @throws UnavailableFileException if the specified file does not exist or there is
     *         an error while processing this file.
     */
    public void load(String filename) throws UnavailableFileException {
        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
            _spreadsheet = (Spreadsheet) in.readObject();
            this._filename = filename;
        } catch (IOException | ClassNotFoundException e) {
        throw new UnavailableFileException(filename);
        }
    }


    /**
     * Read text input file and create domain entities..
     *
     * @param filename name of the text input file
     * @throws ImportFileException
     */
    public void importFile(String filename) throws ImportFileException {
       try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int lines = 0;
            int columns = 0;

        // reads the first two lines to get the number of columns and lines
        for (int i = 0; i < 2; i++) {
            if ((line = reader.readLine()) != null) {
                if (line.startsWith("linhas=")) {
                    lines = Integer.parseInt(line.substring(7).trim());   
                } else if (line.startsWith("colunas=")) {
                    columns = Integer.parseInt(line.substring(8).trim());  
                }
            }
        }

        // initializes the current spreadsheet
        newSpreadsheet(lines, columns);
        
        // read the input lines and separates the reference from the content
            while ((line = reader.readLine()) != null && !line.equals("")) {
                String[] fields = line.split("\\|");
                try {
                _spreadsheet.insertContents(fields[0],fields[1]);
                } catch (InvalidCellRangeExceptionCore e) {
                e.printStackTrace();
                }
            }
        } catch (IOException e1) {
        throw new ImportFileException(filename, e1);
        }
    }
    
        /**
         * @return the current spreadsheet.
         */
    public Spreadsheet getSpreadsheet() {
            return _spreadsheet;
        }

}


