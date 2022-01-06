package syntax.scanner;

import syntax.lang.letter.Terminal;
import syntax.scanner.exceptions.GeneralScannerException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class ScannerBuffer {

    protected List<Terminal> buffer = new LinkedList<>();

    protected long currentMark = 0;

    protected Stack<Long> savedMarks = new Stack<>();

    protected Iterator<Terminal> bufferIterator;

    public void saveMark(){
        savedMarks.push(currentMark);
    }

    public void restoreSavedMark() throws GeneralScannerException {
        setMark(savedMarks.pop());
    }

    protected void setMark(long bufferMark) throws GeneralScannerException {
        if(0 > bufferMark || bufferMark > buffer.size()){
            throw new GeneralScannerException("Buffer mark is out of bound");
        }

        if(currentMark == bufferMark && null != bufferIterator){
            return;
        }

        currentMark = bufferMark;
        bufferIterator = buffer.iterator();

        for (int i = 0; i < bufferMark; i++){
            bufferIterator.next();
        }
    }

    protected void resetIterator(){
        bufferIterator = null;
    }

    public boolean hasNext() {
        return null != bufferIterator && bufferIterator.hasNext();
    }

    public Terminal next() {
        currentMark++;
        return bufferIterator.next();
    }

    public void save(Terminal terminal) {
        currentMark++;
        resetIterator();
        buffer.add(terminal);
    }

    public void clear() {
        currentMark = 0;
        savedMarks.clear();
        buffer.clear();
    }
}
