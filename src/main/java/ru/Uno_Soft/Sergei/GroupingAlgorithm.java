package ru.Uno_Soft.Sergei;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class GroupingAlgorithm {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите путь к файлу (например, https://github.com/PeacockTeam/new-job/releases/download/v1.0/lng-4.txt.gz): ");
        String filePath = scanner.nextLine();

        try {
            List<String> lines = readFile(filePath);
            List<String> validLines = filterValidLines(lines);
            Map<String, List<String>> groupedLines = groupLines(validLines);
            saveGroupsToFile(groupedLines, scanner);

            int groupCount = countGroupsWithMultipleElements(groupedLines);
            long executionTime = System.currentTimeMillis();

            System.out.println("Количество групп с более чем одним элементом: " + groupCount);
            System.out.println("Время выполнения программы (мс): " + executionTime);
        } catch (IOException e) {
            System.out.println("Произошла ошибка при чтении файла: " + e.getMessage());
        }
    }

    private static List<String> readFile(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        URL url = new URL(filePath);
        try (InputStream inputStream = url.openStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(inputStream)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    private static List<String> filterValidLines(List<String> lines) {
        List<String> validLines = new ArrayList<>();
        for (String line : lines) {
            String[] values = line.split(";");
            if (values.length > 1) {
                validLines.add(line);
            }
        }
        return validLines;
    }

    private static Map<String, List<String>> groupLines(List<String> lines) {
        Map<String, List<String>> groups = new HashMap<>();
        for (String line : lines) {
            boolean isNewGroup = true;

            for (Map.Entry<String, List<String>> entry : groups.entrySet()) {
                String groupMember = entry.getKey();

                if (hasSharedValues(line, groupMember)) {
                    entry.getValue().add(line);
                    isNewGroup = false;
                    break;
                }
            }

            if (isNewGroup) {
                List<String> newGroup = new ArrayList<>();
                newGroup.add(line);
                groups.put(line, newGroup);
            }
        }
        return groups;
    }

    private static boolean hasSharedValues(String line1, String line2) {
        String[] values1 = line1.split(";");
        String[] values2 = line2.split(";");

        for (int i = 0; i < values1.length; i++) {
            if (!values1[i].isEmpty() && !values2[i].isEmpty() && values1[i].equals(values2[i])) {
                return true;
            }
        }

        return false;
    }

    private static void saveGroupsToFile(Map<String, List<String>> groups, Scanner scanner) throws IOException {
        List<String> outputLines = new ArrayList<>();
        int groupCount = 0;

        for (Map.Entry<String, List<String>> entry : groups.entrySet()) {
            List<String> group = entry.getValue();
            if (group.size() > 1) {
                groupCount++;
                outputLines.add("Группа " + groupCount);
                for (String line : group) {
                    outputLines.add(line);
                }
                outputLines.add("");
            }
        }

        outputLines.add(0, "Количество групп с более чем одним элементом: " + groupCount);

        System.out.print("Введите путь для сохранения результата (например, output.txt): ");
        String outputPath = scanner.nextLine();
        Path outputFilePath = Path.of(outputPath);
        Files.write(outputFilePath, outputLines);
        System.out.println("Результат сохранен в файле: " + outputFilePath);
    }

    private static int countGroupsWithMultipleElements(Map<String, List<String>> groups) {
        int count = 0;
        for (Map.Entry<String, List<String>> entry : groups.entrySet()) {
            if (entry.getValue().size() > 1) {
                count++;
            }
        }
        return count;
    }
}
