package learn.dontwreckmyhouse.ui;

import learn.dontwreckmyhouse.models.State;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.Scanner;

public class ConsoleIO {
    private final Scanner SC = new Scanner(System.in);

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    // Print Methods
    public void println(String message) {
        System.out.println(message);
    }

    public void printf(String format, Object... values) {
        System.out.printf(format, values);
    }

    // Read Methods
    public String readString(String prompt, boolean isRequired){
        String input;

        do{
            System.out.print(prompt);
            input = SC.nextLine().trim();
        }while (input.isEmpty() && isRequired);

        return input;
    }

    public Integer readInt(String prompt, boolean isRequired){
        Integer input = null;
        do{
            String stringInput=readString(prompt, isRequired);

            if(!isRequired && stringInput.isBlank()){
                break;
            }

            try{
                input = Integer.parseInt(stringInput);
            } catch (NumberFormatException ex){
                println("That is not a valid number.");
            }
        }while(input==null);
        return input;
    }

    public Integer readInt(String prompt, int min, int max, boolean isRequired){
        Integer input = null;
        do{
            input=readInt(prompt, isRequired);
            if(!isRequired && input==null){
                break;
            }
            if(input > max || input < min){
                printf("Number must be between %s and %s.%n", min, max);
            }
        } while (input > max || input < min);
        return input;
    }

    public Double readDouble(String prompt, boolean isRequired){
        Double input = null;
        do{
            String stringInput=readString(prompt, isRequired);

            if(!isRequired && stringInput.isBlank()){
                break;
            }

            try{
                input = Double.parseDouble(stringInput);
            } catch (NumberFormatException ex){
                println("That is not a valid number.");
            }
        }while(input==null);
        return input;
    }

    public Double readDouble(String prompt, double min, double max, boolean isRequired){
        Double input = null;
        do{
            input=readDouble(prompt, isRequired);
            if(!isRequired && input==null){
                break;
            }
            if(input > max || input < min){
                printf("Number must be between %s and %s.%n", min, max);
            }
        } while (input > max || input < min);
        return input;
    }

    public Boolean readBoolean(String prompt){
        Boolean result=null;
        boolean valid=false;
        do {
            String input = readString(prompt, true);

            if(input.equalsIgnoreCase("y") || input.equalsIgnoreCase("n")) {
                result=input.equalsIgnoreCase("y");
                valid=true;
            }else {
                println("Your input is not valid. Please enter \"Y\" or \"N\".");
            }
        } while (!valid);
        return result;
    }

    public BigDecimal readBigDecimal(String prompt, boolean isRequired){
        BigDecimal input = null;
        do{
            String stringInput=readString(prompt, isRequired);

            if(!isRequired && stringInput.isBlank()){
                break;
            }

            try{
                input = new BigDecimal(stringInput);
            } catch (NumberFormatException ex){
                println("That is not a valid number.");
            }
        }while(input==null);
        return input;
    }

    public LocalDate readDate(String prompt, boolean isRequired){
        LocalDate input=null;
        do{
            String stringInput = readString(prompt, isRequired);

            if(!isRequired && stringInput.isBlank()){
                break;
            }

            try {
                input = LocalDate.parse(stringInput, formatter);
            } catch (Exception ex){
                println("That is not a valid date.");
            }

        }while (input==null);
        return input;
    }

    public LocalDate readDate(String prompt, LocalDate date, boolean isRequired){
        LocalDate input;
        do{
            input = readDate(prompt, isRequired);

            if(input==null&&!isRequired){
                break;
            }

            if(!input.isAfter(date)){
                printf("Date should be after %s.%n", date.format(formatter));
            }

        }while (!input.isAfter(date));
        return input;
    }

    public LocalDate readFutureDate(String prompt){
        LocalDate date;
        do{
            date = readDate(prompt, true);

            if(date.isBefore(LocalDate.now())){
                println("Date should be in future.");
            }

        }while (date.isBefore(LocalDate.now()));
        return date;
    }

    public State readState(String prompt, boolean isRequired){
        State[] states = State.values();
        println("State List:");
        for(int i=0; i<17; i++){
            int s1 = i+1;
            int s2 = (i+1)+17;
            int s3 = (i+1)+17*2;
            printf("%2s. %-20s        %2s. %-20s        %2s. %-20s%n", s1, states[s1-1].getName(),
                    s2, states[s2-1].getName(), s3, states[s3-1].getName());
        }
        Integer input = readInt(prompt,1, states.length, isRequired);
        if(!isRequired && input==null){
            return null;
        }
        return states[input-1];
    }
}
