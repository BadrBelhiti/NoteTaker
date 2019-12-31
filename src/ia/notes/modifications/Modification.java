package ia.notes.modifications;

import ia.notes.Utils;

public abstract class Modification implements Comparable<Modification> {

    protected int pos;
    protected long time;

    protected Modification(int pos, long time){
        this.pos = pos;
        this.time = time;
    }

    @Override
    public int compareTo(Modification o) {
        return o.time - time > 0 ? 1 : -1;
    }

    public int getPos(){
        return pos;
    }

    public abstract String toString();

    public static Modification fromData(String data){

        Modification modification;

        // Verifies format of data being retrieved from string
        if (!data.matches("\\{type:(insertion|deletion), (char:%s, )?pos:%d, time:%d}")){
            return null;
        }

        // Parse data using regex
        String type = Utils.findSingle("(insertion|deletion)", data);
        int pos = Integer.parseInt(Utils.findSingle("pos:%d", data).replace("pos:", ""));
        long time = Long.parseLong(Utils.findSingle("time:%d", data).replace("time:", ""));

        if (type.equals("insertion")){
            // Parse character data for insertion modification
            char character = Utils.findSingle("char:%s", data).charAt(5);

            modification = new Insertion(character, pos, time);
        } else {
            modification = new Deletion(pos, time);
        }

        return modification;
    }


}
