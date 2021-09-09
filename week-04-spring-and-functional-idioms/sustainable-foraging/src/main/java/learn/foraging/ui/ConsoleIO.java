package learn.foraging.ui;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

@Component
public class ConsoleIO {

    private static final String INVALID_NUMBER
            = "[INVALID] Enter a valid number.";
    private static final String NUMBER_OUT_OF_RANGE
            = "[INVALID] Enter a number between %s and %s.";
    private static final String REQUIRED
            = "[INVALID] Value is required.";
    private static final String INVALID_DATE
            = "[INVALID] Enter a date in MM/dd/yyyy format.";
    private static final String INVALID_FUTURE_DATE
            = "[INVALID] Enter a date of present or past.";

    private final Scanner scanner = new Scanner(System.in);
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public void print(String message) {
        System.out.print(message);
    }

    public void println(String message) {
        System.out.println(message);
    }

    public void printf(String format, Object... values) {
        System.out.printf(format, values);
    }

    public String readString(String prompt) {
        print(prompt);
        return scanner.nextLine();
    }

    public String readRequiredString(String prompt) {
        while (true) {
            String result = readString(prompt);
            if (!result.isBlank()) {
                return result;
            }
            println(REQUIRED);
        }
    }

    public double readDouble(String prompt) {
        while (true) {
            try {
                return Double.parseDouble(readRequiredString(prompt));
            } catch (NumberFormatException ex) {
                println(INVALID_NUMBER);
            }
        }
    }

    public double readDouble(String prompt, double min, double max) {
        while (true) {
            double result = readDouble(prompt);
            if (result >= min && result <= max) {
                return result;
            }
            println(String.format(NUMBER_OUT_OF_RANGE, min, max));
        }
    }

    public Integer readInt(String prompt){
        return readInt(prompt, true);
    }

    public Integer readInt(String prompt, boolean required) {
        while (true) {
            String input = readString(prompt);
            if(!required && input.isBlank()){
                return null;
            }
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                println(INVALID_NUMBER);
            }
        }
    }

    public Integer readInt(String prompt, int min, int max) {
        return readInt(prompt,min,max,true);
    }

    public Integer readInt(String prompt, int min, int max, boolean required) {
        while (true) {
            Integer result = readInt(prompt, required);
            if(result==null){
                return null;
            }
            if (result >= min && result <= max) {
                return result;
            }
            println(String.format(NUMBER_OUT_OF_RANGE, min, max));
        }
    }

    public boolean readBoolean(String prompt) {
        while (true) {
            String input = readRequiredString(prompt).toLowerCase();
            if (input.equals("y")) {
                return true;
            } else if (input.equals("n")) {
                return false;
            }
            println("[INVALID] Please enter 'y' or 'n'.");
        }
    }

    public LocalDate readLocalDate(String prompt) {
        while (true) {
            String input = readRequiredString(prompt);
            try {
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException ex) {
                println(INVALID_DATE);
            }
        }
    }

    public LocalDate readLocalDate(String prompt, LocalDate date){
        while (true) {
            LocalDate input = readLocalDate(prompt);
            if (!input.isAfter(date)){
                return input;
            }
            println(INVALID_FUTURE_DATE);
        }
    };

    public BigDecimal readBigDecimal(String prompt){
        return readBigDecimal(prompt, true);
    }

    public BigDecimal readBigDecimal(String prompt, boolean required) {
        while (true) {
            String input = readString(prompt);
            if(!required && input.isBlank()){
                return null;
            }
            try {
                return new BigDecimal(input);
            } catch (NumberFormatException ex) {
                println(INVALID_NUMBER);
            }
        }
    }

    public BigDecimal readBigDecimal(String prompt, BigDecimal min, BigDecimal max){
        return readBigDecimal(prompt, min, max, true);
    }

    public BigDecimal readBigDecimal(String prompt, BigDecimal min, BigDecimal max, boolean required) {
        while (true) {
            BigDecimal result = readBigDecimal(prompt, required);
            if(!required && result==null){
                return null;
            }
            if (result.compareTo(min) >= 0 && result.compareTo(max) <= 0) {
                return result;
            }
            println(String.format(NUMBER_OUT_OF_RANGE, min, max));
        }
    }
}
