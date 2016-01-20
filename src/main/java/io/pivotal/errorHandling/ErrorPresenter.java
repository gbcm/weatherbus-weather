package io.pivotal.errorHandling;

import io.pivotal.view.JsonPresenter;

/**
 * Created by pivotal on 1/12/16.
 */
public class ErrorPresenter extends JsonPresenter {
    private final String message;

    public ErrorPresenter(String message) {
        this.message = message;
    }
}
