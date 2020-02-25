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
        System.out.println(matchResult);
        return matchResult.group();
    }


    /**
     * @precondition    oldValue and newValue differ by exactly 1 character
     */

    // hhh
    // hhhh

    // aaab
    // aab

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

        if (oldValue.length() < newValue.length()){
            int last = newValue.length() - 1;
            return new Insertion(newValue.charAt(last), last, timestamp);
        } else {
            int last = oldValue.length() - 1;
            return new Deletion(last, timestamp);
        }
    }


}
