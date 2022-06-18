package org.freesoft.rudenko;

import java.io.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class Main {
    public static final String ADMIN_PASSWORD = "admin";
    public static final String CLIENT_PASSWORD = "4226";
    public static final String FILE_NAME = "time.data";

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nEnter passwowrd: \n" );
        Scanner scanner = new Scanner(System.in);
        String enterPass = scanner.nextLine();

        //admin
        if (enterPass.equalsIgnoreCase(ADMIN_PASSWORD)){
            System.out.println("Main.main");
            //client
        }else  if (enterPass.equalsIgnoreCase(CLIENT_PASSWORD)){
            if (!new File(FILE_NAME).exists()) {
                createAndSave();
            } else {
                String read = read();
                int dalay = 60000;
                boolean life = true;
                if (!(Instant.parse(read).atZone(ZoneId.systemDefault()).getDayOfWeek()
                        .equals(Instant.now().atZone(ZoneId.systemDefault()).getDayOfWeek())
                )){
                    createAndSave();
                }
                while (life){
                    if (Instant.parse(read).isBefore(Instant.now())){
                        Thread.sleep(dalay);
                    }else {
                        life = false;
                        Runtime.getRuntime().exec(new String[]{"shutdown", "-s" });
                    }
                }
            }
            //OFF
        }else {
            Runtime.getRuntime().exec(new String[]{"shutdown", "-s" });
        }
    }

    private static String read() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
       return reader.readLine();
    }

    private static void createAndSave() throws IOException {
        FileWriter writer = new FileWriter(FILE_NAME);
        writer.write(String.valueOf(Instant.now().plus(6, ChronoUnit.HOURS)));
        writer.close();
    }

}
