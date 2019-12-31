package ia.notes;

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


}
