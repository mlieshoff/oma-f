package org.mili.application.ui;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: papa
 * Date: 20.07.13
 * Time: 14:00
 * To change this template use File | Settings | File Templates.
 */
public abstract class Form<T> extends JPanel implements SelectionChangeListener {
    private String label;
    private FormChangeListener formChangeListener;

    protected Form(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void registerFormDataListener(FormChangeListener formChangeListener) {
        this.formChangeListener = formChangeListener;
    }

    public void fireDataChanged(Object selected) {
        formChangeListener.onFormChanged(this);
    }

}
