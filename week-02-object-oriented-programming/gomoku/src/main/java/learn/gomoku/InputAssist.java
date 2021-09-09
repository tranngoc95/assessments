package learn.gomoku;

import java.util.Scanner;

public class InputAssist {
    private final Scanner SC = new Scanner(System.in);

    public String readString(String prompt){
        String input;
        do{
            System.out.print(prompt);
            input = SC.nextLine().trim();
        }while (input.isEmpty());
        return input;
    }

    public int readInt(String prompt){
        boolean isNumber;
        String input;
        do{
            input=readString(prompt);
            isNumber=true;
            if(!Character.isDigit(input.charAt(0)) && input.charAt(0)!='-'){
                continue;
            }
            for(int i=1; i<input.length(); i++){
                if(!Character.isDigit(input.charAt(i))){
                    System.out.println("What you entered is not a number!");
                    isNumber=false;
                    break;
                }
            }
        }while(!isNumber);
        return Integer.parseInt(input);
    }

    public int readInt(String prompt, int min, int max){
        int input=readInt(prompt);
        while (input > max || input < min){
            System.out.println("The number you entered is out of range!\n");
            input=readInt(prompt);
        }
        return input;
    }

    //    private String readString(String prompt, String[] validInputs){
//        String input;
//        boolean valid=false;
//        do{
//        input=readString(prompt);
//        for(String validInput : validInputs){
//            if(input.equals(validInput)){
//                valid=true;
//                break;
//            }
//        }
//        }while (!valid);
//        return input;
//    }
}
