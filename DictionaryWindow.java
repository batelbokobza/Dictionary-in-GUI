package question2;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/*The class builds the dictionary panel displayed to the user.*/
public class DictionaryWindow extends JFrame implements ActionListener {

    private interface InformationForAction {
        JLabel labelInfo = new JLabel();
        JTextField inputField = new JTextField(20);
        JButton actionButtons = new JButton();
        JPanel controlPanel = new JPanel(new GridLayout(2, 1));
    }

    private interface Constants {
        int TABLE_TOP = 0;
        int WIDTH_PANEL = 950;
        int HEIGHT_PANEL = 700;
        int CANCEL = 1;
        int NOT_SELECTED_ROW = -1;
        int PARTS_OF_INPUT = 2; //One part of a term and a second part of a meaning.
        Font FONT_TEXT = new Font("Ariel", Font.BOLD, 20);
        Color COLOR_TEXT = new Color(145, 57, 238);
    }

    private interface SearchOptions {
        JTextField searchField = new JTextField();
        JPanel searchPanel = new JPanel(new BorderLayout());
        JLabel searchLabel = new JLabel("Search - ");
    }

    private Dictionary dic = new Dictionary();
    JPanel keyPanel = new JPanel(new GridLayout(2, 1));

    private final DictionaryModel model;
    private final JTable dictionaryTable;

    public DictionaryWindow() {
        super("Dictionary");
        setLayout(new BorderLayout());

        //Initialize the table
        model = new DictionaryModel(dic.getListDictionary());
        dictionaryTable = new JTable(model);
        dictionaryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dictionaryTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);


        updateDictionaryTable();
        // Adding it to scroll panel
        JScrollPane sp = new JScrollPane(dictionaryTable);
        add(sp);

        createMenuBar();
        createControlPanel();
        panelDesign();

        //Add panel of search
        createSearchFilter();
        add(SearchOptions.searchPanel, BorderLayout.SOUTH);

        createDictionaryPanel();

    }

    private void createSearchFilter() {
        SearchOptions.searchField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                searchTerm();
            }
        });
    }

    /*The function contains a string variable whose value is equal to the input that the user enters in the search field.
     * Using the tableRowSorter function which provides filtering and sorting by TableModel,
     * a filter is defined that determines which rows will not be in the table, depending on the search value entered by the user.
     * If there are entries in the table that contain the search word at the beginning of the string,
     * they will appear in the dictionary table.*/
    private void searchTerm() {
        String searchValue = SearchOptions.searchField.getText().toLowerCase(); //Gets user input in search box.
        TableRowSorter<DictionaryModel> tableRowSorter = new TableRowSorter<>((DictionaryModel) dictionaryTable.getModel());
        dictionaryTable.setRowSorter(tableRowSorter);
        tableRowSorter.setRowFilter(RowFilter.regexFilter(searchValue, Constants.TABLE_TOP));
        if (dic.getRowNumberByKey(searchValue) >= 0) {//If greater than zero, current search results were obtained.
            dictionaryTable.addRowSelectionInterval(Constants.TABLE_TOP, Constants.TABLE_TOP);
        }
    }

    private void panelDesign() {
        InformationForAction.labelInfo.setFont(Constants.FONT_TEXT);
        InformationForAction.inputField.setFont(Constants.FONT_TEXT);
        InformationForAction.actionButtons.setFont(Constants.FONT_TEXT);
        InformationForAction.actionButtons.setBackground(Constants.COLOR_TEXT);
        InformationForAction.labelInfo.setForeground(Constants.COLOR_TEXT);
        SearchOptions.searchLabel.setForeground(Constants.COLOR_TEXT);
        SearchOptions.searchLabel.setFont(Constants.FONT_TEXT);
        SearchOptions.searchField.setFont(Constants.FONT_TEXT);
        SearchOptions.searchPanel.add(SearchOptions.searchLabel, BorderLayout.NORTH);
        SearchOptions.searchPanel.add(SearchOptions.searchField, BorderLayout.SOUTH);
        dictionaryTable.setBackground(Color.white);
        dictionaryTable.setForeground(new Color(145, 57, 238));
        dictionaryTable.setFont(new Font("Ariel", Font.BOLD, 15));
    }


    /*Create an information panel, which will be used to display information according to the action selected by the user.*/
    private void createControlPanel() {
        JPanel buttonPanel = new JPanel();
        keyPanel.add(InformationForAction.labelInfo);
        keyPanel.add(InformationForAction.inputField);
        buttonPanel.add(InformationForAction.actionButtons);
        InformationForAction.actionButtons.addActionListener(this);
        InformationForAction.controlPanel.add(keyPanel);
        InformationForAction.controlPanel.add(buttonPanel);
        add(InformationForAction.controlPanel, BorderLayout.NORTH);
        InformationForAction.controlPanel.setVisible(false);
        dictionaryTable.getTableHeader().setFont(Constants.FONT_TEXT);
        dictionaryTable.getTableHeader().setForeground(Constants.COLOR_TEXT);
    }

    public void createDictionaryPanel() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Constants.WIDTH_PANEL, Constants.HEIGHT_PANEL);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /*Create a menu bar.*/
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menuBarFileOptions());
        menuBar.add(menuBarEditOptions());
        this.setJMenuBar(menuBar);
    }

    /*Create a menu for file operations*/
    private JMenu menuBarFileOptions() {
        String[] fileOptions = {"Open file", "Save to file"};
        JMenu fileMenu = new JMenu("File");
        fileMenu.setForeground(Constants.COLOR_TEXT);
        fileMenu.setFont(Constants.FONT_TEXT);
        for (String option : fileOptions) {
            fileMenu.add(new JMenuItem(option)).addActionListener(this);
        }
        return fileMenu;
    }

    /*Create a menu for editing actions*/
    private JMenu menuBarEditOptions() {
        String[] editOptions = {"New", "Add", "Delete", "Update"};
        JMenu editMenu = new JMenu("Edit");
        editMenu.setForeground(Constants.COLOR_TEXT);
        editMenu.setFont(Constants.FONT_TEXT);
        for (String option : editOptions) {
            editMenu.add(new JMenuItem(option)).addActionListener(this);
        }
        return editMenu;
    }

    /*Listener interface for action events.
     * In this function, calls are made to functions that will operate according to the user's choice.*/
    @Override
    public void actionPerformed(ActionEvent e) {
        String selectedAction = e.getActionCommand();
        InformationForAction.actionButtons.setText(selectedAction);
        InformationForAction.controlPanel.setVisible(!selectedAction.equals("Open file") &&
                !selectedAction.equals("Save to file") && !selectedAction.equals("New"));
        InformationForAction.inputField.setVisible(selectedAction.equals("Add") || selectedAction.equals("Update"));
        InformationForAction.labelInfo.setText(messageToUser(selectedAction));
        final JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home") + "\\" + "Desktop");
        if (selectedAction.equals("New")) {
            deleteAll();
        } else if (selectedAction.equals("Open file")) {
           if(fileChooser.showOpenDialog(null) != Constants.CANCEL) {
               try {
                   openFile(fileChooser.getSelectedFile());
               } catch (InvalidFile | FileNotFoundException invalidFile) {
                   invalidFile.printStackTrace();
               }
           }
        } else if (selectedAction.equals("Save to file")) {
            if(fileChooser.showSaveDialog(null) != Constants.CANCEL) {
                saveFile(fileChooser.getSelectedFile());
            }
        } else if (e.getSource().getClass().getSimpleName().equals("JButton")) {
            buttonActionPerformed(selectedAction);
        }
    }

    /*Clearing all their terms and interpretations in the dictionary, and updating the table.
     * At the end of the method, the table will be empty.*/
    private void deleteAll() {
        dic.clear();
        updateDictionaryTable();
    }

    /*The method is used to open a window for the user, to select a file that he wants to upload to the dictionary.
     * If the user entered a file with invalid rows, he will receive a warning message,
     * and the table will be updated according to the user's choice.
     * If there is an IO error, he will get an error message.*/
    private void openFile(File selectedFile) throws InvalidFile, FileNotFoundException {
        if (isValidFile(selectedFile)) {
            dic = dic.createDictionaryFromFile(selectedFile);
            updateDictionaryTable();
        }
        else{
            JOptionPane.showMessageDialog(this,
                    "You have selected an unsupported file.\n" +
                            "Please select a file with a txt extension.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidFile(File file) {
        String extension = "";
        int i = file.getAbsolutePath().lastIndexOf('.');
        if (i >= 0) {
            extension = file.getAbsolutePath().substring(i + 1);
        }
        return extension.equals("txt");
    }

    private void saveFile(File fileChooser) {
        try {
            dic.LoadDictionaryIntoFile(fileChooser);
        } catch (IOException ignored) {
        }
    }

    /*The method receives the selected action event, and performs the required action.
     * The actions performed - add, update, and call to a delete function.
     * In the search and update operation, if the user enters a string that is not part of the dictionary terms,
     * he will receive an error message.*/
    private void buttonActionPerformed(String selectedAction) {
        if (selectedAction.equals("Delete")) {
            deleteTerm();
        }
        else {
                if (selectedAction.equals("Add")) {
                    addTerm();
                } else if (selectedAction.equals("Update")){
                    updateTerm();
                }
            }
        InformationForAction.inputField.setText("");
    }

    private String messageToUser(String selectedOption) {
        String msg = selectedOption.equals("Add") ?
                "Enter the term and meaning you want to add according to the format: 'term - meaning':" :
                "Select the line you want to update, and enter the updated meaning:";
        if (selectedOption.equals("Delete")) {
            msg = "Select the line you want to delete, then press delete.";
        }
        return msg;
    }

    private void deleteTerm() {
        String termDeletion = getSelectedTerm();
        if (termDeletion == null) {
            return;
        }
        dic.delete(termDeletion);
        updateDictionaryTable();
        InformationForAction.controlPanel.setVisible(false);
    }

    private void addTerm() {
        String[] tm = InformationForAction.inputField.getText().split("-", Constants.PARTS_OF_INPUT);
        DictionaryEntry termAndMeaning = new DictionaryEntry(tm[0].trim(), tm[1].trim());
        if (tm.length == Constants.PARTS_OF_INPUT) {
            if (!dic.containsKey(termAndMeaning.getTerm())) {
                dic.add(termAndMeaning);
                updateDictionaryTable();
                InformationForAction.controlPanel.setVisible(false);
            } else {
                InformationForAction.labelInfo.setText("Error: This term already exist in the dictionary.");
            }
        } else
            InformationForAction.labelInfo.setText("Error: The input format is incorrect. " +
                    "Please enter the input in the following format: term - meaning");
    }

    private void updateTerm() {
        DictionaryEntry termAndMeaning = new DictionaryEntry();
        String termByRow = getSelectedTerm();
        if(termByRow != null){
            termAndMeaning.setTerm(termByRow);
            String newMeaning = InformationForAction.inputField.getText();
            if(!newMeaning.isEmpty()){
                termAndMeaning.setMeaning(newMeaning);
                dic.update(termAndMeaning);
                updateDictionaryTable();
                InformationForAction.controlPanel.setVisible(false);
            }
            else{
                InformationForAction.labelInfo.setText("The meaning of the term cannot be empty. Please try again:");
            }
        }
        else{
            InformationForAction.labelInfo.setText("You need to select a row to perform an update, and enter the updated meaning:");
        }
    }

    /*Returns the key value of the selected row in the table.*/
    private String getSelectedTerm() {
        int selectedRow = dictionaryTable.getSelectedRow();
        if (selectedRow == Constants.NOT_SELECTED_ROW) {
            return null;
        }
        return (String) model.getValueAt(selectedRow, 0);
    }

    /*Updating the model of the dictionary table.
     * Each addition, update and deletion updates the dictionary table model.*/
    private void updateDictionaryTable() {
        model.setData(dic.getListDictionary());
    }
}