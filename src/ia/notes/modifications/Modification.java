package ia.notes.modifications;

import ia.notes.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Modification implements Comparable<Modification> {

    protected int pos;
    protected long time;

    protected Modification(int pos, long time){
        this.pos = pos;
        this.time = time;
    }

    @Override
    public int compareTo(Modification o) {
        return o.time - time > 0 ? -1 : 1;
    }

    public int getPos(){
        return pos;
    }

    public long getTime() {
        return time;
    }

    public abstract String toString();

    // {type:deletion, pos:0, time:1582582140032}
    // {type:insertion, char:a, pos:0, time:1582582139284}

    public static Modification fromData(String data){

        Modification modification;

        // Verifies format of data being retrieved from string
        if (!data.matches("\\{type:(insertion|deletion), (char:., )?pos:\\d+, time:\\d+}")){
            return null;
        }

        // Parse data using regex
        int pos = Integer.parseInt(Utils.findSingle("(pos:\\d+)", data).replace("pos:", ""));
        long time = Long.parseLong(Utils.findSingle("(time:\\d+)", data).replace("time:", ""));
        String type = Utils.findSingle("(insertion|deletion)", data);


        if (type.equals("insertion")){
            // Parse character data for insertion modification
            char character = Utils.findSingle("(char:.)", data).charAt(5);

            modification = new Insertion(character, pos, time);
        } else {
            modification = new Deletion(pos, time);
        }

        return modification;
    }


}
