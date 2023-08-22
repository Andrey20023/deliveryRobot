package ru.netology;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Hello world!");
        for (int i = 0; i < 1000; i++) {
            AtomicInteger countR = new AtomicInteger();
            Runnable runnable = () -> {
                String route = generateRoute("RLRFR", 100);
                for (char element : route.toCharArray()) {
                    if (element == 'R') countR.getAndIncrement();
                }
                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(countR.get())) {
                        sizeToFreq.put(countR.get(), sizeToFreq.get(countR.get()) + 1);
                    } else {
                        sizeToFreq.put(countR.get(), 1);
                    }
                }
            };
            Thread tread = new Thread(runnable);
            tread.start();
            try {
                tread.join();
            } catch (InterruptedException e) {
                System.out.printf("%s ,было прервано", tread.getName());
            }
        }

        sizeToFreq
                .entrySet()
                .stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .limit(1)
                .forEach((e) -> System.out.println("Самое частое количество повторений: " + e.getKey() + ", встретилось " + e.getValue() + " раз."));
        ;
        System.out.println("Другие размеры: ");
        sizeToFreq
                .entrySet()
                .stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .skip(1)
                .forEach((e) -> System.out.println("- " + e.getKey() + " ( " + e.getValue() + " раз)"));
        ;
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