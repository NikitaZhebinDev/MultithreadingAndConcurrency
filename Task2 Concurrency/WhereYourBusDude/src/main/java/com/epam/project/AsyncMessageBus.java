package com.epam.project;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public class AsyncMessageBus {

    // data structure for message storage
    private final Map<String, LinkedList<String>> topicMessages = new HashMap<>();
    private final Object lock = new Object();

    public void postMessage(String topic, String message) {
        synchronized (lock) {
            topicMessages.putIfAbsent(topic, new LinkedList<>());
            topicMessages.get(topic).add(message);
            lock.notifyAll(); // notify consumers waiting for messages
        }
    }

    public String consumeMessage(String topic) throws InterruptedException {
        synchronized (lock) {
            while (!topicMessages.containsKey(topic) || topicMessages.get(topic).isEmpty()) {
                lock.wait(); // wait for new messages to arrive
            }
            return topicMessages.get(topic).poll(); // get message
        }
    }


    public static void main(String[] args) {
        AsyncMessageBus messageBus = new AsyncMessageBus();

        // create producers
        Runnable producerTask = () -> {
            Random random = new Random();
            String[] topics = {"Topic1", "Topic2", "Topic3"};
            while (true) {
                String topic = topics[random.nextInt(topics.length)];
                String message = "Message'" + random.nextInt(1000) + "'";
                messageBus.postMessage(topic, message);
                System.out.println("Produced: " + message + " to " + topic);
                try {
                    Thread.sleep(random.nextInt(500)); // simulate delay
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        };

        Thread producer1 = new Thread(producerTask);
        Thread producer2 = new Thread(producerTask);

        Thread consumer1 = new Thread(createConsumerTask(messageBus, "Topic1"));
        Thread consumer2 = new Thread(createConsumerTask(messageBus, "Topic2"));
        Thread consumer3 = new Thread(createConsumerTask(messageBus, "Topic3"));

        producer1.start();
        producer2.start();
        consumer1.start();
        consumer2.start();
        consumer3.start();
    }

    // helper to create consumer tasks
    private static Runnable createConsumerTask(AsyncMessageBus messageBus, String topic) {
        return () -> {
            while (true) {
                try {
                    String message = messageBus.consumeMessage(topic);
                    System.out.println("Consumed: " + message + " from " + topic);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        };
    }
    
}