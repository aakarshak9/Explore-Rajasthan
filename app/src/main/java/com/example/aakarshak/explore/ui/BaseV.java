package com.example.aakarshak.explore.ui;

public interface BaseV<T extends PresenterB> {

    /**
     * Method that registers the PresenterBase {@code presenter} with the V implementing {@link BaseV}
     *
     * @param presenter PresenterBase instance implementing the {@link PresenterB}
     */
    void setPresenter(T presenter);
}
