package gitlet;

import java.util.*;

public class test {
    public static void main(String[] args) {
        Date d = new Date(0);
        StringTokenizer st = new StringTokenizer(d.toString());
        while(st.countTokens() != 2) {
            System.out.printf("%s ", st.nextToken());
        }
        st.nextToken();
        System.out.printf("%s ", st.nextToken());
        System.out.printf("%tz%n", new Date(0));
    }
}
