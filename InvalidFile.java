package question2;

public class InvalidFile extends Exception{
    public InvalidFile() {
        super("The file contains lines in an invalid format.");
    }
}
