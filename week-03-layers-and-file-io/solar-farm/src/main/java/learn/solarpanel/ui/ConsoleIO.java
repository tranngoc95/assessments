package learn.solarpanel.ui;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ConsoleIO {
    private final Scanner SC = new Scanner(System.in);

    public String readString(String prompt, boolean isRequired){
        String input;

        do{
            System.out.print(prompt);
            input = SC.nextLine().trim();
        }while (input.isEmpty() && isRequired);

        return input;
    }

    public int readInt(String prompt, boolean isRequired){
        int input=0;
        do{
            String stringInput=readString(prompt, isRequired);

            if(!isRequired && stringInput.isBlank()){
                break;
            }

            try{
                input = Integer.parseInt(stringInput);
            } catch (NumberFormatException ex){
                System.out.println("That is not a valid number.");
            }
        }while(input==0);
        return input;
    }

    public int readInt(String prompt, int min, int max, boolean isRequired){
        int input;
        boolean valid;
        do{
            valid=true;
            input=readInt(prompt, isRequired);
            if(!isRequired && input==0){
                break;
            }
            if(input > max || input < min){
                valid = false;
                System.out.printf("Number must be between %s and %s.%n", min, max);
            }
        } while (!valid);
        return input;
    }

    public Boolean readBoolean(String prompt, boolean isRequired){
        Boolean result=null;
        boolean valid=false;
        do {
            String input = readString(prompt, isRequired);
            if(!isRequired && input.isBlank()){
                break;
            }

            if(input.equalsIgnoreCase("y") || input.equalsIgnoreCase("n")) {
                result=input.equalsIgnoreCase("y");
                valid=true;
            }else {
                System.out.println("Your input is not valid. Please enter \"Y\" or \"N\".");
            }
        } while (!valid);
        return result;
    }
}
