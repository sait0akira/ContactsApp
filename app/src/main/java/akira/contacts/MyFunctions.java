package akira.contacts;

public class MyFunctions {

    public String phoneNumCorrector(String phn) {
        char[] temp_number = null;
        temp_number = phn.toCharArray();
        if ((temp_number[0] == '+') && (temp_number[1] == '7')) {
            temp_number[0] = '-';
            temp_number[1] = '8';
        }
        for (int i = 0; i < temp_number.length - 1; i++) {
            if ((temp_number[i] == ' ') || (temp_number[i] == '(') || (temp_number[i] == ')'))
                temp_number[i] = '-';


        }
        String finalNum = "";
        for (int i = 0; i < temp_number.length; i++) {
            if (temp_number[i] != '-') finalNum += String.valueOf(temp_number[i]);
        }
        return finalNum;
    }
}
