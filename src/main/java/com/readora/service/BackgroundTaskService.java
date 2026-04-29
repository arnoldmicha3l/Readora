package com.readora.service;

import javafx.concurrent.Task;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class BackgroundTaskService {

    private BackgroundTaskService() {}

    public static <T> void runTask(Supplier<T> work, Consumer<T> onSuccess, Consumer<Throwable> onError) {
        Task<T> task = new Task<>() {
            @Override
            protected T call() {
                return work.get();
            }
        };

        task.setOnSucceeded(event -> {
            if (onSuccess != null) {
                onSuccess.accept(task.getValue());
            }
        });

        task.setOnFailed(event -> {
            if (onError != null) {
                onError.accept(task.getException());
            }
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}