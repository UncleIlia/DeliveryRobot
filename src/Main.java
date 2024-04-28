import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 1000; i++) {

            Thread thread1 = new Thread(() -> {
                String randomString = generateRoute("RLRFR", 100);
                int count = randomString.length() - randomString.replace(String.valueOf('R'), "").length();
                synchronized (sizeToFreq) {
                    sizeToFreq.put(count, sizeToFreq.getOrDefault(count, 0) + 1);
                    sizeToFreq.notify();
                }
            });

            Thread thread2 = new Thread(() -> {
                while (!Thread.interrupted()) {
                    try {
                        sizeToFreq.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                synchronized (sizeToFreq) {
                    Map.Entry<Integer, Integer> maxValue = sizeToFreq
                            .entrySet()
                            .stream()
                            .max(Map.Entry.comparingByValue())
                            .get();

                    System.out.println("Самое частое количество повторений - " + maxValue.getKey() + " (" + maxValue.getValue() + " раз)");

                }
            });

            thread1.start();
            thread1.join();
            thread2.start();
            thread2.interrupt();
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
}
