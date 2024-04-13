import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {
    public static Map<Integer, Integer> sizeToFreq = new HashMap<>();
    public static List<Integer> listOfKeys = new ArrayList<>();
    public static int maxFreq = 0;

    public static void main(String[] args) {
        List<String> listOfRandomStrings = new ArrayList<>();
        List<Integer> listForCountR = new ArrayList<>();

        new Thread(() -> {
            synchronized (listOfRandomStrings) {
                for (int i = 0; i < 1000; i++) {
                    listOfRandomStrings.add(generateRoute("RLRFR", 100));
                    int count = listOfRandomStrings.get(i).length() - listOfRandomStrings.get(i).replace(String.valueOf('R'), "").length();
                    listForCountR.add(count);
                }
            }
        }).start();

        new Thread(() -> {
            synchronized (listOfRandomStrings) {
                for (Integer element : listForCountR) {
                        sizeToFreq.put(element, sizeToFreq.getOrDefault(element, 0) + 1);
                        listOfRandomStrings.notify();
                    }
                listOfKeys.addAll(sizeToFreq.keySet());
                for (Integer key : listOfKeys) {
                    System.out.println("- " + key + " (" + sizeToFreq.get(key) + " раз)");
                }
            }
        }).start();

        new Thread(() -> {
            while (!Thread.interrupted()) {
                synchronized (listOfRandomStrings) {
                try {
                    listOfRandomStrings.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < sizeToFreq.size(); i++) {
                        maxFreq = maxUsingIteration(sizeToFreq);
                        System.out.println("Максимальная частота - " + maxFreq + " (" + sizeToFreq.get(maxFreq) + " раз)");
                    }
                Thread.interrupted();
                }
            }
        }).start();
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static <K, V extends Comparable<V>> V maxUsingIteration(Map<K, V> map) {
        Map.Entry<K, V> maxEntry = null;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (maxEntry == null || entry.getValue()
                    .compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }
        return maxEntry.getValue();
    }
}
