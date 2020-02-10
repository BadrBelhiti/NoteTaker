package ia.notes.modifications;

public class Insertion extends Modification {

    private char character;

    public Insertion(char character, int pos, long time) {
        super(pos, time);
        this.character = character;
    }

    public char getCharacter() {
        return character;
    }

    @Override
    public String toString() {
        return String.format("{type:insertion, char:%s, pos:%d, time:%d}", character, pos, time);
    }
}
