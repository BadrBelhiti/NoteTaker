package ia.notes;

import ia.notes.modifications.Deletion;
import ia.notes.modifications.Insertion;
import ia.notes.modifications.Modification;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static String findSingle(String regex, String s){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        MatchResult matchResult = matcher.toMatchResult();
        return matchResult.group();
    }


    /**
     * @precondition    oldValue and newValue differ by exactly 1 character
     */
    public static Modification getChange(String oldValue, String newValue, long timestamp){

        for (int i = 0; i < Math.min(oldValue.length(), newValue.length()); i++){
            char old = oldValue.charAt(i);
            char nnew = newValue.charAt(i);

            if (old != nnew){
                if (oldValue.length() < newValue.length()){
                    return new Insertion(nnew, i, timestamp);
                } else {
                    return new Deletion(i, timestamp);
                }
            }
        }

        return null;
    }


}
