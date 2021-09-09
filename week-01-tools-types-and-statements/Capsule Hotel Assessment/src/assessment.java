import java.util.Scanner;

public class assessment {
    public static void main(String[] args) {
        //Start up
        System.out.println("Welcome to Capsule-Capsule.\n" + "=".repeat(27));
        int capsuleCapacity = getIntInfo("Enter the number of capsules available:");
        String[] capsuleArray = new String[capsuleCapacity];
        System.out.printf("There are %s unoccupied capsules ready to be booked.\n\n", capsuleCapacity);

        String guestName;
        int capsuleNum;
        boolean exit=false;
        String exitAnswer;
        int numberOfGuests=0;

        while (!exit) {
            //Main menu
            System.out.println("Guest Menu\n==========\n1. Check In\n2. Check Out\n3. View Guests\n4. Exit");
            int option = getIntInfo("Choose one option [1-4]: ", 4);

            switch (option) {
                //Check in
                case 1:
                    System.out.println("Guest Check In\n" + "==============");
                    if(numberOfGuests==capsuleCapacity){
                        System.out.println("The hotel is full. Sorry we can't check you in. Please try again later.\n");
                        continue;
                    }
                    guestName = getInfo("Guest Name: ");
                    capsuleNum = getIntInfo("Capsule #[1-"+capsuleCapacity+"]:", capsuleCapacity);
                    while (!capsuleAvailable(capsuleArray, capsuleNum)) {
                        System.out.printf("Error :(\nCapsule #%s is occupied.\n", capsuleNum);
                        capsuleNum = getIntInfo("Capsule #[1-"+capsuleCapacity+"]:", capsuleCapacity);
                    }
                    capsuleArray[capsuleNum-1] = guestName;
                    numberOfGuests++;
                    System.out.printf("Success :)\n%s is booked in capsule #%s.\n", guestName, capsuleNum);
                    break;

                //Check out
                case 2:
                    System.out.println("Guest Check Out\n===============");
                    if(numberOfGuests==0){
                        System.out.println("Error :(\nThere is no guest in the hotel to check out.\n");
                        continue;
                    }
                    capsuleNum = getIntInfo("Capsule #[1-"+capsuleCapacity+"]:", capsuleCapacity);
                    while (capsuleAvailable(capsuleArray, capsuleNum)) {
                        System.out.printf("Error :(\nCapsule #%s is unoccupied.\n", capsuleNum);
                        capsuleNum = getIntInfo("Capsule #[1-"+capsuleCapacity+"]:", capsuleCapacity);
                    }
                    guestName = capsuleArray[capsuleNum-1];
                    capsuleArray[capsuleNum-1] = null;
                    System.out.printf("%s checked out from capsule #%s.\n", guestName, capsuleNum);
                    numberOfGuests--;
                    break;

                //View Guests
                case 3:
                    System.out.println("View Guests\n===========");
                    capsuleNum = getIntInfo("Capsule #[1-"+capsuleCapacity+"]:", capsuleCapacity);
                    System.out.println("Capsule: Guest");
                    int indexMin=capsuleNum-1-5;
                    int indexMax=capsuleNum-1+5;
                    if(indexMin<0){
                        indexMin=0;
                    }
                    if(indexMax>capsuleCapacity-1){
                        indexMax=capsuleCapacity-1;
                    }
                    for(int i=indexMin; i <=indexMax ; i++){
                        if (capsuleAvailable(capsuleArray, i+1)){
                            System.out.println( i+1 +": [unoccupied]");
                        }
                        else {
                            System.out.println( i+1 + ": " + capsuleArray[i]);
                        }
                    }
                    break;

                //Exit
                case 4:
                default:
                    System.out.println("Exit\n====");
                    System.out.println("Are you sure you want to exit?\nAll data will be lost.");
                    do {
                        exitAnswer = getInfo("Exit [y/n]:");
                    }while(!exitAnswer.equalsIgnoreCase("y") && !exitAnswer.equalsIgnoreCase("n"));
                    if (exitAnswer.equalsIgnoreCase("y")) {
                        exit = true;
                    }
                    System.out.println("Goodbye!");
            }
            System.out.println();
        }
    }

    public static String getInfo(String prompt) {
        Scanner sc = new Scanner(System.in);
        String input;
        do{
            System.out.println(prompt);
            input = sc.nextLine().trim();
        }while(input.isEmpty());
        return input;
    }

    public static int getIntInfo(String prompt) {
        int number=-1;
        do{
            boolean isNumber=true;
            String input=getInfo(prompt);
            for(int i=0; i<input.length(); i++){
                if(!Character.isDigit(input.charAt(i))){
                    System.out.println("What you entered is not a positive number!");
                    isNumber=false;
                    break;
                }
            }
            if(isNumber) {
                number = Integer.parseInt(input);
            }
        }while(number<=0);
        return number;
    }

    public static int getIntInfo(String prompt, int max) {
        int input=getIntInfo(prompt);
        while (input > max){
            System.out.println("The number you entered is out of range!");
            input=getIntInfo(prompt);
        }
        return input;
    }

    public static boolean capsuleAvailable(String[] capsuleArray, int capsuleNum) {
        return capsuleArray[capsuleNum-1]==null;
    }

//    public static int getCapsuleNum(String[] capsuleArray, boolean checkIn) {
//        int capsuleCapacity= capsuleArray.length;
//        String occupied = checkIn ? "occupied" : "unoccupied";
//        int capsuleNum;
//        boolean success;
//        do {
//            capsuleNum = getIntInfo("Capsule #[1-" + capsuleCapacity + "]:", capsuleCapacity);
//            success=capsuleAvailable(capsuleArray, capsuleNum)==checkIn;
//            if(!success){
//                System.out.printf("Error :(\nCapsule #%s is %s.\n", capsuleNum, occupied);
//            }
//        }while(!success);
//        return capsuleNum;
//    }
}
