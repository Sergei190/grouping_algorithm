package ru.Uno_Soft.Sergei;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

public class GroupingAlgorithm {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите путь к файлу (например, https://github.com/PeacockTeam/new-job/releases/download/v1.0/lng-4.txt.gz): ");
        String filePath = scanner.nextLine();

        try {
            List<String> lines = readFile(filePath);
            List<String> validLines = filterValidLines(lines);
            Map<String, Set<String>> groupedLines = groupLines(validLines);
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
        return lines.parallelStream()
                .filter(line -> {
                    String[] values = line.split(";");
                    return values.length > 1;
                })
                .collect(Collectors.toList());
    }

    private static Map<String, Set<String>> groupLines(List<String> lines) {
        Map<String, Set<String>> groups = new HashMap<>();
        for (String line : lines) {
            Set<String> groupRef = null;

            for (Map.Entry<String, Set<String>> entry : groups.entrySet()) {
                String groupMember = entry.getKey();
                if (hasSharedValues(line, groupMember)) {
                    groupRef = entry.getValue();
                    break;
                }
            }

            if (groupRef != null) {
                groupRef.add(line);
            } else {
                Set<String> newGroup = new HashSet<>();
                newGroup.add(line);
                groups.put(line, newGroup);
            }
        }
        return groups;
    }

    private static boolean hasSharedValues(String line1, String line2) {
        String[] values1 = line1.split(";");
        String[] values2 = line2.split(";");

        if (values1.length != values2.length) {
            return false;
        }

        for (int i = 0; i < values1.length; i++) {
            if (!values1[i].isEmpty() && !values2[i].isEmpty() && values1[i].equals(values2[i])) {
                return true;
            }
        }

        return false;
    }

    private static void saveGroupsToFile(Map<String, Set<String>> groups, Scanner scanner) throws IOException {
        System.out.print("Введите путь для сохранения результата (например, output.txt): ");
        String outputPath = scanner.nextLine();
        Path outputFilePath = Path.of(outputPath);

        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            int groupCount = 0;

            for (Map.Entry<String, Set<String>> entry : groups.entrySet()) {
                Set<String> group = entry.getValue();
                if (group.size() > 1) {
                    groupCount++;
                    writer.write("Группа " + groupCount);
                    writer.newLine();
                    for (String line : group) {
                        writer.write(line);
                        writer.newLine();
                    }
                    writer.newLine();
                }
            }

            writer.write("Количество групп с более чем одним элементом: " + groupCount);
        }

        System.out.println("Результат сохранен в файле: " + outputFilePath);
    }

    private static int countGroupsWithMultipleElements(Map<String, Set<String>> groups) {
        int count = 0;
        for (Map.Entry<String, Set<String>> entry : groups.entrySet()) {
            if (entry.getValue().size() > 1) {
                count++;
            }
        }
        return count;
    }
}
