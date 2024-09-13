package utils;

public class StringUtils {

    public static boolean checkNullOrEmpty(String s){
        return s != null && !s.trim().isEmpty();
    }
}
