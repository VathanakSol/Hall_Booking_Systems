import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.time.LocalDate;
public class Main {
    static Scanner input = new Scanner(System.in);
    static int row;
    static int col;
    static char[] alphabet = new char[26];
    static int index = 0;
    static int n = 0;
    static int rowIndex = 0;
    static int colIndex = 0;
    static String op;
    static String[] values;
    static String id = null;
    static boolean confirmBooking = false;
    static String[] bookingHistory = new String[50];
    static String[][] hallMorning = new String[row][col];
    static String[][] hallAfternoon = new String[row][col];
    static String[][] hallNight = new String[row][col];

    public static void main(String[] args) {
        System.out.println("\n" +
                "██╗  ██╗ █████╗ ██╗     ██╗         ██████╗  ██████╗  ██████╗ ██╗  ██╗██╗███╗   ██╗ ██████╗     ███████╗██╗   ██╗███████╗████████╗███████╗███╗   ███╗\n" +
                "██║  ██║██╔══██╗██║     ██║         ██╔══██╗██╔═══██╗██╔═══██╗██║ ██╔╝██║████╗  ██║██╔════╝     ██╔════╝╚██╗ ██╔╝██╔════╝╚══██╔══╝██╔════╝████╗ ████║\n" +
                "███████║███████║██║     ██║         ██████╔╝██║   ██║██║   ██║█████╔╝ ██║██╔██╗ ██║██║  ███╗    ███████╗ ╚████╔╝ ███████╗   ██║   █████╗  ██╔████╔██║\n" +
                "██╔══██║██╔══██║██║     ██║         ██╔══██╗██║   ██║██║   ██║██╔═██╗ ██║██║╚██╗██║██║   ██║    ╚════██║  ╚██╔╝  ╚════██║   ██║   ██╔══╝  ██║╚██╔╝██║\n" +
                "██║  ██║██║  ██║███████╗███████╗    ██████╔╝╚██████╔╝╚██████╔╝██║  ██╗██║██║ ╚████║╚██████╔╝    ███████║   ██║   ███████║   ██║   ███████╗██║ ╚═╝ ██║\n" +
                "╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝    ╚═════╝  ╚═════╝  ╚═════╝ ╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝ ╚═════╝     ╚══════╝   ╚═╝   ╚══════╝   ╚═╝   ╚══════╝╚═╝     ╚═╝\n" +
                "                                                                                                                                                     \n");
        System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
        System.out.println("CSTAD HALL BOOKING SYSTEM");
        System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
        String Row = validate("> Config total rows in hall: ", input, "[1-9]+");
        String Col = validate("> Config total seats per row in hall: ", input, "[1-9]+");
        int row = Integer.parseInt(Row);
        int col = Integer.parseInt(Col);
        System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
        System.out.println("> Total seats are: " + row * col);
        for(char ch='A'; ch<='Z'; ch++)
        {
            alphabet[index] = ch;
            index++;
        }
        do{
            System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
            System.out.println("[[ Application Menu ]]");
            System.out.println("<A> Booking");
            System.out.println("<B> Hall");
            System.out.println("<C> Showtime");
            System.out.println("<D> Reboot Showtime");
            System.out.println("<E> History");
            System.out.println("<F> Exit");
            System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
            op = validate(
                    "Please Choose Option: ",input,
                    "[a-zA-Z]");
            switch(op){
                case "a","A" -> {
                    showTime();
                    String message = validate("> Please select show time (A | B | C): ",input,
                            "[a-cA-C]");
                    System.out.println("-+".repeat(45));
                    String nameHall = check(message);
                    System.out.println("-+".repeat(45));
                    System.out.println(nameHall);
                    String[][] value = switch(message)
                    {
                        case "A","a" -> mainHall(hallMorning);
                        case "B","b" -> mainHall(hallAfternoon);
                        case "C","c" -> mainHall(hallNight);
                        default -> throw new IllegalStateException("Unexpected value: " + message);
                    };
                    showHall(value, alphabet);
                    role();
                    String option = validate("Please enter value: ",input,
                            "[A-Za-z]-[1-9](?:\\s*,\\s*[A-Za-z]-[1-9])*");
                    String options = option.toUpperCase();
                    values = options.split(",");
                    for(String value1 : values){
                        String trimValue = value1.trim();
                        String[] parts = trimValue.split("-");
                        if(parts.length != 2) {
                            System.out.println("Invalid input format: " + trimValue);
                            continue;
                        }
                        rowIndex = parts[0].charAt(0) - 'A';
                        colIndex = Integer.parseInt(parts[1]) - 1;
                        if(rowIndex < 0 || rowIndex >= value.length || colIndex < 0 || colIndex >= value[0].length){
                            System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                            System.out.println("Invalid seat selection: " + trimValue + ". Please select a valid seat");
                            System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                            continue;
                        }
                        if(value[rowIndex][colIndex] == null) {
                            confirmBooking = true;
                        } else {
                            System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                            System.out.println("Seat " + trimValue + " is already booked by someone else.");
                            System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                        }
                    }
                    if(confirmBooking) {
                        String trimValue;
                        System.out.print("Please Enter Your ID: ");
                        id = input.nextLine();

                        String info = validate(
                                "Please enter <Yes> to confirm booking or <No> to cancel: ", input,
                                "^(?:Yes|No)$");
                        if(info.equalsIgnoreCase("Yes")) {
                            for(String value1 : values) {
                                trimValue = value1.trim();
                                String[] parts = trimValue.split("-");
                                rowIndex = parts[0].charAt(0) - 'A';
                                colIndex = Integer.parseInt(parts[1]) - 1;
                                value[rowIndex][colIndex] = "BO";
                            }
                            System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                            System.out.println("Seat " + option + " successfully booked.");
                            System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                        }
                        LocalDate localDate = LocalDate.now();
                        String bookingDetails =nameHall + "   User ID: " + id + ", Time " + localDate + " Seat " + Arrays.toString(values);
                        bookingHistory[n++] = bookingDetails;
                    }
                    else{
                        for(String value1 : values) {
                            String trimValue = value1.trim();
                            System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                            System.out.println("Booking for seat " + trimValue + " cancelled.");
                            System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                        }
                    }
                }
                case "b","B" -> {
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                    System.out.println("# Hall Information");
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                    System.out.println("# Hall - Morning ");
                    showHall(hallMorning, alphabet);
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                    System.out.println("# Hall - Afternoon ");
                    showHall(hallAfternoon, alphabet);
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                    System.out.println("# Hall - Night ");
                    showHall(hallNight, alphabet);
                }
                case "c","C" -> showTime();
                case "d","D" -> {
                    clearArray(hallMorning);
                    clearArray(hallAfternoon);
                    clearArray(hallNight);
                    clearArray(new String[][]
                            {bookingHistory}
                    );
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                    System.out.println("\t\tAlready reboot\t\t");
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                }
                case "E","e" -> {
                    System.out.println("Booking History: ");
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                    history(bookingHistory, n);
                    System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                }
            }
        } while(!op.equalsIgnoreCase("F"));

    }
    public static String validate(String message, Scanner scanner,String regex)
    {
        while(true){
            System.out.print(message);
            String userInput = scanner.nextLine();
            Pattern pattern = Pattern.compile(regex);
            if(pattern.matcher(userInput).matches()){
                return userInput;
            }
            else{
                System.out.println("Invalid formats Input");
            }
        }
    }
    public static void showTime()
    {
        System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
        System.out.println("# Daily Showtime of CSTAD Hall: ");
        System.out.println("# A) Morning (10:00AM - 12:30PM");
        System.out.println("# B) Afternoon (03:00PM - 05:30PM");
        System.out.println("# C) Night (07:00PM - 09:30PM");
        System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
    }
    public static String[][] mainHall(String[][] hall)
    {
        return hall;
    }
    public static void history(String[] bookingHistory, int n)
    {
        for(int i = 0; i < n; i++){
            if(bookingHistory[i] == null)
            {
                System.out.println("\tAll Array is Reboot\t");
            }
            else{
                System.out.println("# User : " + (i + 1));
                System.out.println(bookingHistory[i]);
            }
        }
    }
    public static String check(String message)
    {
        if(message.equalsIgnoreCase("a")){
            return "Morning Hall";
        } else if(message.equalsIgnoreCase("b"))
        {
            return "Afternoon Hall";
        } else{
            return "Night";
        }
    }
    public static void role()
    {
        System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
        System.out.println("# INSTRUCTION");
        System.out.println("# Single: C-1");
        System.out.println("# Multiple (separate by comma): C-1,C-2");
    }
    public static void showHall(String[][] arr, char[] alphabet)
    {
        System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr[i].length; j++) {
                if(arr[i][j] == null) {
                    System.out.print("|" + alphabet[i] + "-" + (j + 1) + "::" + "AV" + "|   ");
                } else{
                    System.out.print("|" + alphabet[i] + "-" + (j + 1) + "::" + arr[i][j] + "|   ");
                }
            }
            System.out.println();
        }
        System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
    }
    public static void clearArray(String[][] array) {
        for (String[] strings : array) {
            Arrays.fill(strings, null);
        }
    }

}
