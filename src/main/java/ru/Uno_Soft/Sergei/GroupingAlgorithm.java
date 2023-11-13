package ru.Uno_Soft.Sergei;

import java.io.*;
import java.net.URL;
import java.util.*;

public class GroupingAlgorithm {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();

        if (args.length == 0) {
            System.out.println("Необходимо указать URL файла в аргументах командной строки");
            return;
        }

        String fileUrl = args[0];

        try {
            List<Set<String>> groups = findGroups(fileUrl);
            saveGroupsToFile(groups);
        } catch (IOException e) {
            System.out.println("Произошла ошибка при обработке файла: " + e.getMessage());
        }

        long executionTime = System.currentTimeMillis() - startTime;
        System.out.println("Количество групп с более чем одним элементом: " + getGroupsWithMoreThanOneElement(findGroups(fileUrl)));
        System.out.println("Время выполнения программы: " + executionTime + " мс");
    }

    private static List<Set<String>> findGroups(String fileUrl) throws IOException {
        List<Set<String>> groups = new ArrayList<>();
        Map<String, Set<String>> groupMap = new HashMap<>();

        URL url = new URL(fileUrl);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (isValidLine(line)) {
                    Set<String> existingGroup = null;
                    for (String value : line.split(";")) {
                        existingGroup = groupMap.get(value);
                        if (existingGroup != null) {
                            break;
                        }
                    }

                    if (existingGroup != null) {
                        existingGroup.add(line);
                    } else {
                        Set<String> newGroup = new HashSet<>();
                        newGroup.add(line);
                        groups.add(newGroup);
                        for (String value : line.split(";")) {
                            groupMap.put(value, newGroup);
                        }
                    }
                }
            }
        }

        return groups;
    }

    private static boolean isValidLine(String line) {
        return line.matches("[^;\n]+(;[^;\n]+)*");
    }

    private static void saveGroupsToFile(List<Set<String>> groups) throws IOException {
        groups.sort((g1, g2) -> Integer.compare(g2.size(), g1.size()));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
            writer.write("Количество групп с более чем одним элементом: " + getGroupsWithMoreThanOneElement(groups) + "\n");
            writer.newLine();

            for (int i = 0; i < groups.size(); i++) {
                Set<String> group = groups.get(i);
                writer.write("Группа " + (i + 1) + "\n");
                for (String line : group) {
                    writer.write(line + "\n");
                }
                writer.newLine();
            }
        }
    }

    private static int getGroupsWithMoreThanOneElement(List<Set<String>> groups) {
        int count = 0;
        for (Set<String> group : groups) {
            if (group.size() > 1) {
                count++;
            }
        }
        return count;
    }
}
