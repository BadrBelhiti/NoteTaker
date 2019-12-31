package ia.notes.modifications;

public class Deletion extends Modification {

    public Deletion(int pos, long time) {
        super(pos, time);
    }

    @Override
    public String toString() {
        return String.format("{type:deletion, pos:%d, time:%d}", pos, time);
    }
}
