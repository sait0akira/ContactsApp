package akira.contacts;

public class MyFunctions {

    static public String phoneNumCorrector(String phn) {

        String[] toReplace = {" ", "-", "(", ")", "."};
        String tempString = phn.replace("+7", "8");
        for (int i = 0; i < toReplace.length; i++) {
            if (phn.contains(toReplace[i])) tempString = tempString.replace(toReplace[i], "");
        }
        return tempString;
    }


}
