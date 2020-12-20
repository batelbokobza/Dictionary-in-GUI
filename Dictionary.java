package question2;

import java.io.*;
import java.util.*;

/*Dictionary class - the class contains basic methods on the data dictionary.
 * The dictionary contains termAndMeaning objects which contain 2 fields - term and meaning.
 * The class contains necessary methods -
 * adding a term, deleting, updating, searching, uploading a file dictionary, saving a file dictionary.
 * There is a function for returning the number of terms that contain at the beginning of the string,
 * a sub-string obtained as a parameter.*/
public class Dictionary implements Serializable {

    int PARTS_OF_INPUT = 2; //One part of a term and a second part of a meaning.


    private final TreeMap<String, String> dictionary;


    /*Constructor - Instantiates a new Dictionary.*/
    public Dictionary() {
        dictionary = new TreeMap<>();
    }

    /*Adding entries to a dictionary - If the entry does not exist in the dictionary, the entry is added.*/
    public void add(DictionaryEntry input) {
            dictionary.put(input.getTerm(), input.getMeaning());
        }

    /*The function deletes the word and its meaning from the dictionary, according to the key value obtained as a parameter.*/
    public void delete(String term) {
        dictionary.remove(term);
    }

    /*The function updates the term in a dictionary.
     * It introduces the term and its reinterpretation, it overrides the old interpretation of the key value,
     * and replaces it with the updated interpretation.*/
    public void update(DictionaryEntry tm) {
        dictionary.put(tm.getTerm(), tm.getMeaning());
    }

    /*The function searches for the term in the list by key value, and returns the meaning.*/
    public String search(DictionaryEntry tm) {
        return dictionary.get(tm.getTerm());
    }

    /*The function returns a list of dictionary entries - a term and its meaning.
     * If the dictionary is updated, the list will need to be re-posted.*/
    public ArrayList<Map.Entry<String, String>> getListDictionary() {
        return new ArrayList<>(dictionary.entrySet());
    }

    /*The function returns true if the value of the key is in the list. Otherwise, returns a false.*/
    public boolean containsKey(String key) {
        return dictionary.containsKey(key);
    }

    /*The function returns the number of lines in the dictionary,
     * which contain at the beginning of the string of the term, the string obtained in the parameter*/
    public int getRowNumberByKey(String key) {
        if (dictionary.containsKey(key))
            return dictionary.headMap(key).size();
        return -1;
    }

    /*Delete all their terms and interpretations from the dictionary.
     * At the end of this method operation, the dictionary will be empty.*/
    public void clear(){
        dictionary.clear();
    }

    /*The user selects a file from his computer,
     * which contains terms and interpretations in the "term - meaning" format,
     * and when the input is correct, the dictionary will contain this input.*/
    public Dictionary createDictionaryFromFile(File file) throws InvalidFile, FileNotFoundException{
        Dictionary dictionaryFromFile = new Dictionary();
        Scanner scanner = new Scanner(file);
        DictionaryEntry termAndMeaning = new DictionaryEntry();

        while (scanner.hasNextLine()) {
            String[] tm = scanner.nextLine().split("-", PARTS_OF_INPUT);
            if (tm.length == PARTS_OF_INPUT) {
                termAndMeaning.setTerm(tm[0].trim());
                termAndMeaning.setMeaning(tm[1].trim());
                dictionaryFromFile.add(termAndMeaning);
            } else if (!tm[0].trim().isEmpty()) {
                throw new InvalidFile();
            }

        }
        scanner.close();
        return dictionaryFromFile;
    }

    /*Saving the dictionary into a user's file.
     * The user can select a specific file to save, or insert a new file that we will create,
     * and there we will save the dictionary.*/
    public void LoadDictionaryIntoFile(File file) throws IOException {
        try (BufferedWriter buffWriter = new BufferedWriter(new FileWriter(file + ".txt"))) {
            for (Map.Entry<String, String> entry : dictionary.entrySet()) {
                buffWriter.write(entry.getKey() + " - " + entry.getValue());
                buffWriter.newLine();
            }
            buffWriter.flush();
        }
    }
}


