import java.util.Scanner;
public class Avon {
    public static void main(String[] args) {
        String separator = "____________________________________________________________";
        String avonPrefix = "Avon:\t";
        String banner = """
                ___                    
               /   |_   ______  ____  
              / /| | | / / __ \\/ __ \\ 
             / ___ | |/ / /_/ / / / / 
            /_/  |_|___/\\____/_/ /_/ """;
        System.out.println(separator);
        System.out.println(banner);
        System.out.println(avonPrefix + "Hark! I am Avon who stands before thee.");
        System.out.println(avonPrefix + "How may my hand or wit now serve thy need?");
        System.out.println(separator);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(separator);
            if (command.equals("bye")) {
                System.out.println(avonPrefix + "Fare thee well! Pray heavens our paths cross anon.");
                System.out.println(separator);
                break;
            }
            System.out.println(avonPrefix + command);
            System.out.println(separator);
        }
        scanner.close();
    }
}
