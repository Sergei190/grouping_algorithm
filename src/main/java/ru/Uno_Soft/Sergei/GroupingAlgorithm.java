package ru.Uno_Soft.Sergei;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

public class GroupingAlgorithm {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        Map<String, Set<String>> groups = new HashMap<>();

        try (Scanner scanner = new Scanner(new GZIPInputStream(new URL("https://github.com/PeacockTeam/new-job/releases/download/v1.0/lng-4.txt.gz").openStream()))){
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (Pattern.matches("^[^;]*(;[^;]*)*$", line)) {
                    String[] elements = line.split(";");
                    for (Map.Entry<String, Set<String>> entry : new HashMap<>(groups).entrySet()) {
                        for (String value : elements) {
                            if (entry.getKey().contains(value)) {
                                entry.getValue().add(line);
                                groups.put(line, entry.getValue());
                                groups.remove(entry.getKey());
                                break;
                            }
                        }
                    }
                    if (!groups.containsKey(line)) {
                        groups.put(line, new HashSet<>(Set.of(line)));
                    }
                }
            }
        }

        List<Set<String>> groupList = new ArrayList<>(groups.values());

        groupList.sort((g1, g2) -> Integer.compare(g2.size(), g1.size()));

        int groupCount = 0;

        for (Set<String> group : groupList) {
            if (group.size() > 1) {
                groupCount++;
                System.out.println("Group " + groupCount);
                for (String str : group) {
                    System.out.println(str);
                }
                System.out.println();
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Execution time: " + (endTime - startTime) + " ms");
    }
}
