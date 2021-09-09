# Assessment M01 Capsule Hotel Plan
### Requirements
- Be able to get input from user
- Be able to perform requests of user: Check in, Check out, View guests, Exit
- Keep track of capsule situation

### State
capsuleCapacity

capsuleArray

guestName

capsuleNum

exit

### Steps
- Get capsuleCapacity
- Set capsuleArray to store capsule availability
- do while(!exit):
    - Get menu options
    - switch case:
        - Check in:
            - prompt user to enter guestName and capsuleNum
            - if capsule is occupied: prompt user to enter a different capsule
            - else: change capsule to guestName in capsuleArray and print out confirmation
        - Check out:
            - if all capsules are unoccupied then print out that there is no guest to check out then continue next loop
            - prompt user to enter capsuleNum
            - if capsule is unoccupied: prompt user to enter a different capsule
            - else: change capsule to null in capsuleArray and print out confirmation
        - View guests
            - prompt user to enter capsuleNum
            - for loop from capsuleNum-5 to capsuleNum+5: print out capsule guest
        - Exit
            - prompt user to confirm they want to exit
            - if answer is yes then set exit=true
    

### Methods
public static String getInfo(String prompt)

public static int getIntInfo(String prompt)

public static int getIntInfo(String prompt, int max)

public static boolean capsuleAvailable(String[] capsuleArray, int capsuleNum)

