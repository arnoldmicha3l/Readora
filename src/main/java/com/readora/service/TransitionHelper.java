package com.readora.service;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public final class TransitionHelper {

    private TransitionHelper() {
    }

    public static void fadeIn(Node node) {
        if (node == null) return;

        FadeTransition transition = new FadeTransition(Duration.millis(220), node);
        transition.setFromValue(0.0);
        transition.setToValue(1.0);
        transition.play();
    }

    public static void slideInFromRight(Node node) {
        if (node == null) return;

        TranslateTransition transition = new TranslateTransition(Duration.millis(220), node);
        transition.setFromX(30);
        transition.setToX(0);
        transition.play();
    }

    public static void slideInFromBottom(Node node) {
        if (node == null) return;

        TranslateTransition transition = new TranslateTransition(Duration.millis(220), node);
        transition.setFromY(25);
        transition.setToY(0);
        transition.play();
    }

    public static void pop(Node node) {
        if (node == null) return;

        ScaleTransition transition = new ScaleTransition(Duration.millis(140), node);
        transition.setFromX(0.96);
        transition.setFromY(0.96);
        transition.setToX(1.0);
        transition.setToY(1.0);
        transition.play();
    }

    public static void pulse(Node node) {
        if (node == null) return;

        ScaleTransition transition = new ScaleTransition(Duration.millis(160), node);
        transition.setFromX(1.0);
        transition.setFromY(1.0);
        transition.setToX(1.04);
        transition.setToY(1.04);
        transition.setAutoReverse(true);
        transition.setCycleCount(2);
        transition.play();
    }

    public static void softLoad(Node node) {
        if (node == null) return;

        fadeIn(node);
        slideInFromBottom(node);
    }

    public static void pageLoad(Node node) {
        if (node == null) return;

        fadeIn(node);
        slideInFromRight(node);
    }

    public static void fadeOutThenRun(Node node, Runnable afterFade) {
        if (node == null) {
            if (afterFade != null) afterFade.run();
            return;
        }

        FadeTransition fade = new FadeTransition(Duration.millis(160), node);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        fade.setOnFinished(event -> {
            if (afterFade != null) afterFade.run();
        });

        fade.play();
    }
}