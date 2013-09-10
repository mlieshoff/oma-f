package org.mili.application.ui;

import org.mili.application.dto.Field;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: papa
 * Date: 20.07.13
 * Time: 23:46
 * To change this template use File | Settings | File Templates.
 */
public class FormElement<T extends JComponent> {

    private T component;
    private Field field;

    public FormElement(Field field, T component) {
        this.component = component;
        this.field = field;
    }

    public T getComponent() {
        return component;
    }

    public Field getField() {
        return field;
    }
}
