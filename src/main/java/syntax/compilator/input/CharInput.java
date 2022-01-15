package syntax.compilator.input;

import java.io.BufferedReader;
import java.io.CharArrayReader;

public class CharInput extends BufferedInput implements InputInterface {
    protected CharArrayReader charReader;

    public CharInput(char[] array){
        charReader = new CharArrayReader(array);
        setBufferedReader(new BufferedReader(charReader));
    }
}
