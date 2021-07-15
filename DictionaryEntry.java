package question2;

import java.io.Serializable;

/*The class uses an object with 2 fields, a term and an meaning.
The object is used to represent terms in the dictionary, and their meanings.*/
public class DictionaryEntry implements Comparable<DictionaryEntry>, Serializable {

    private String term;
    private String meaning;

    public DictionaryEntry(String term, String meaning){
        this.term = term;
        this.meaning = meaning;
    }

    public DictionaryEntry(){
    }

    public String getTerm() {
        return term;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    @Override
    public String toString(){
        return "Term - "+getTerm()+"\n"+"Meaning - "+getMeaning();
    }

    @Override
    public int compareTo(DictionaryEntry o) {
        int compression = this.term.compareTo(o.term);
        return compression == 0 ? this.term.compareTo(o.term) : compression ;
    }
}
