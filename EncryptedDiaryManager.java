import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;


public class EncryptedDiaryManager {

    private final int key;

    EncryptedDiaryManager() {
        key = 3;
    }

    public String encrypt(String text){

        StringBuilder encrypted = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                encrypted.append((char) ((c - base + key) % 26 + base));
            } else {
                encrypted.append(c);
            }
        }
        return encrypted.toString();
    }

    public String decrypt(String text){

        StringBuilder decrypted = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                decrypted.append((char) ((c - base - key + 26) % 26 + base));
            } else {
                decrypted.append(c);
            }
        }
        return decrypted.toString();
    }

    public static void main(String[] args) {


        EncryptedDiaryManager diaryManager = new EncryptedDiaryManager();
        Scanner scanner = new Scanner(System.in);

        int choice;

        File diaryDir = new File("diary_entries");
        if (!diaryDir.exists()) {
            diaryDir.mkdir();
        }

        while (true){

            System.out.println("1. Create a new diary entry");
            System.out.println("2. Read an existing diary entry");
            System.out.println("3. List all diary entries");
            System.out.println("4. Delete a diary entry");
            System.out.println("5. Search for keyword in diary entries");
            System.out.println("6. Exit");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }
            
            switch (choice){

                case 1:

                    while(true){

                        System.out.println("Type your diary entry (press Enter to finish):");
                        String entry = scanner.nextLine();

                        if (entry.trim().isEmpty()) {
                            System.out.println("Diary entry cannot be empty. Please try again.");
                            continue;
                        }
                        
                        String encryptedEntry = diaryManager.encrypt(entry);
                        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        String fileName = "entry_" + datePart + ".txt";

                        File file = new File(diaryDir, fileName);
                        try (FileWriter writer = new FileWriter(file)) {
                            writer.write(encryptedEntry);
                            System.out.println("Diary entry saved successfully.");
                        } catch (IOException e) {
                            System.out.println("An error occurred while saving the diary entry: " + e.getMessage());
                        }

                        break;
                    }

                    break;

                case 2:

                    while (true){

                        System.out.println("Enter the date of the diary entry to read (yyyy-MM-dd):");
                        String dateInput = scanner.nextLine();
                        if (dateInput.trim().isEmpty()) {
                            System.out.println("Date cannot be empty. Please try again.");
                            continue;
                        }
                        if (!dateInput.matches("\\d{4}-\\d{2}-\\d{2}")) {
                            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
                            continue;
                        }

                        String fileName = "entry_" + dateInput + ".txt";
                        File file = new File(diaryDir, fileName);

                        try (Scanner reader = new Scanner(file)){
                            String content = "";
                            while (reader.hasNextLine()) {
                                content += reader.nextLine() + "\n";
                            }
                            String decryptedContent = diaryManager.decrypt(content);    
                            System.out.println("Diary entry for " + dateInput + ":");
                            System.out.println(decryptedContent);

                        } catch (IOException e) {
                            System.out.println("Diary entry not found for the given date. Please try again.");
                        }
                        

                        break;
                    }

                    break;

                case 3:

                    String[] files = diaryDir.list();
                    if (files != null) {
                        System.out.println("Available diary entries:");
                        for (String fileName : files) {
                            System.out.println("- " + fileName);
                            try {
                                File file = new File(diaryDir, fileName);
                                System.out.println("Size: " + file.length() + " bytes");
                                System.out.println("Last Modified: " + LocalDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneId.systemDefault()));
                                
                            } catch (Exception e) {
                                System.out.println("An error occurred while listing diary entries: " + e.getMessage());
                            }
                        }
                    } else {
                        System.out.println("No diary entries found.");
                    }

                    break;

                case 4:

                    while (true){

                        System.out.println("Enter the date of the diary entry to delete (yyyy-MM-dd):");

                        String[] fileList = diaryDir.list();
                        if (fileList != null) {
                            System.out.println("Available diary entries:");
                            for (String fileName : fileList) {
                                System.out.println("- " + fileName);
                            }
                        } 
                        else {
                            System.out.println("No diary entries found to delete.");
                        }

                        String dateInput = scanner.nextLine();
                        if (dateInput.trim().isEmpty()) {
                            System.out.println("Date cannot be empty. Please try again.");
                            continue;
                        }
                        if (!dateInput.matches("\\d{4}-\\d{2}-\\d{2}")) {
                            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
                            continue;
                        }

                        File fileToDelete = new File(diaryDir, "entry_" + dateInput + ".txt");
                        if (!fileToDelete.exists()) {
                            System.out.println("Diary entry for " + dateInput + " not found.");
                            break;
                        }
                        else{
                            while (true){
                                System.out.println("Are you sure you want to delete the diary entry for " + dateInput + "? (yes/no)");
                                String confirmation = scanner.nextLine().trim().toLowerCase();
                                if (confirmation.equals("yes")) {
                                    if (fileToDelete.delete()) {
                                        System.out.println("Diary entry deleted successfully.");
                                    } else {
                                        System.out.println("Failed to delete the diary entry.");
                                    }
                                    break;
                                } else if (confirmation.equals("no")) {
                                    System.out.println("Deletion cancelled.");
                                    break;
                                } else {
                                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                                }
                            }
                        }

                        break;
                    }
                    break;

                case 5:

                    while (true){

                        System.out.println("Enter the keyword to search for in diary entries:");
                        String keyword = scanner.nextLine().trim();
                        if (keyword.isEmpty()) {
                            System.out.println("Keyword cannot be empty. Please try again.");
                            continue;
                        }
                        String[] entryFiles = diaryDir.list();
                        if (entryFiles != null) {
                            boolean found = false;
                            for (String fileName : entryFiles) {
                                File file = new File(diaryDir, fileName);
                                try (Scanner reader = new Scanner(file)) {
                                    String content = "";
                                    while (reader.hasNextLine()) {
                                        content += reader.nextLine() + "\n";
                                    }
                                    String decryptedContent = diaryManager.decrypt(content);
                                    if (decryptedContent.contains(keyword)) {
                                        System.out.println("Keyword found in " + fileName + ":");
                                        System.out.println(decryptedContent);
                                        found = true;
                                    }
                                } catch (IOException e) {
                                    System.out.println("An error occurred while searching diary entries: " + e.getMessage());
                                }
                            }
                            if (!found) {
                                System.out.println("No diary entries found containing the keyword.");
                            }
                        } else {
                            System.out.println("No diary entries found.");
                        }


                        break;
                    }
                    break;

                case 6:
                    System.out.println("Exiting...");
                    System.exit(0);
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}