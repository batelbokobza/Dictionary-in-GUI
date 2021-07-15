package question2;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Map;

/*DictionaryModel interface,
 * which specifies the methods for investigation and further actions on the data model of the dictionary.
 * The interface implements a list of Map.Entry.
 * The class inherits from the AbstractTableModel abstract class, which implements applications for table model interfaces.
 * A number of methods belonging to this department have been implemented, for the purpose of satisfying applications.*/
public class DictionaryModel extends AbstractTableModel {
    private static final int NUMBER_COLS = 2; // Term and meaning
    private static final String TERM_COLUMN = "Term";
    private static final String MEANING_COLUMN = "Meaning";

    private List<Map.Entry<String, String>> dictionaryList;


    public DictionaryModel(List<Map.Entry<String, String>> list) {
        this.dictionaryList = list;
    }

    //Returns a list of terms that are in the dictionary with their meaning.
    public List<Map.Entry<String, String>> getData() {
        return dictionaryList;
    }

    /*Updates the model of the list of terms that are in the dictionary with their interpretation.
     * An update is made after adding, deleting and updating a term in a dictionary.*/
    public void setData(List<Map.Entry<String, String>> list) {
        this.dictionaryList = list;
        this.fireTableDataChanged(); //Informs all listeners that all cell values are in the table.
    }

    /*Returns the number of rows in the table model of the dictionary.*/
    @Override
    public int getRowCount() {
        return dictionaryList.size();
    }

    /*Returns the number of cols in the table model of the dictionary.*/
    @Override
    public int getColumnCount() {
        return NUMBER_COLS;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return dictionaryList.get(rowIndex).getKey();
            case 1:
                return dictionaryList.get(rowIndex).getValue();
            default:
                return null; // Can't reach here
        }
    }


    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return TERM_COLUMN;
            case 1:
                return MEANING_COLUMN;
            default:
                return null; // Can't reach here
        }
    }
}
