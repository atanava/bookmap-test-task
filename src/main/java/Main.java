package main.java;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        String inputFile = "input.txt";
        String outFile = "output.txt";

        Broker broker = new Broker();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {

            String input;
            StringBuilder sb = new StringBuilder();

            while ((input = reader.readLine()) != null) {
                String[] line = input.split(",");

                switch (line[0]) {
                    case "u" -> broker.updateBook(line);
                    case "o" -> broker.orderAndUpdateBook(line);
                    case "q" -> broker.respondToQuery(line, sb);
                }
            }
            writer.write(sb.toString().trim());
            writer.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
