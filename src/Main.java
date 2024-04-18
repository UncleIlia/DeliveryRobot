import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {
    public static Map<Integer, Long> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {
        List<Integer> listForCountR = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                synchronized (listForCountR) {
                    String randomString = generateRoute("RLRFR", 100);
                    int count = randomString.length() - randomString.replace(String.valueOf('R'), "").length();
                    listForCountR.add(count);
                    sizeToFreq = listForCountR.stream()
                            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
                }
            }).start();
        }

        Map<Integer, Long> sortedMap = sortMapByValueDescending(sizeToFreq);
        List<Integer> listOfKeys = new ArrayList<>(sortedMap.keySet());
        System.out.println("Самое частое количество повторений " + listOfKeys.get(0) + " (встретилось " + sortedMap.get(listOfKeys.get(0)) + " раз)");
        System.out.println("Другие размеры:");
        for (int i = 1; i < listOfKeys.size(); i++) {
            System.out.println("- " + listOfKeys.get(i) + " (" + sortedMap.get(listOfKeys.get(i)) + " раз)");
        }
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortMapByValueDescending(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.<K, V>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
