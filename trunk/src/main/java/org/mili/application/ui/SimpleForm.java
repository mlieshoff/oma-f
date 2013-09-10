package org.mili.application.ui;

import org.freixas.jcalendar.JCalendarCombo;
import org.mili.application.dto.Field;
import org.mili.application.service.ServiceException;
import org.mili.application.util.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: papa
 * Date: 20.07.13
 * Time: 14:00
 * To change this template use File | Settings | File Templates.
 */
public abstract class SimpleForm<T> extends Form<T> {

    public enum FormMode {
        NORMAL,
        WIDE;
    }

    private JButton cancelButton;
    private JButton resetButton;
    private JButton saveButton;
    private Map<Field, FormElement> elements = new HashMap<>();

    protected T formObject;

    protected SimpleForm(FormMode formMode, String label, FormElement[] formElements) {
        super(label);

        Log.log(this, "construct", "init callbacks.");

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                cancel();
            }
        });
        resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                reset();
            }
        });
        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (validation()) {
                    try {
                        save();
                    } catch (ServiceException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(SimpleForm.this, e.getMessage());
                    }
                    fireDataChanged(formObject);
                } else {
                    JOptionPane.showMessageDialog(SimpleForm.this, "Not valid!");
                }
            }
        });

        setLayout(new BorderLayout());

        JPanel p = new JPanel(new SpringLayout());
        for (FormElement formElement : formElements) {
            elements.put(formElement.getField(), formElement);
            UiUtil.createFromField(p, formElement.getField().name(), formElement.getComponent());
        }

        int length = formElements.length;
        int columns = 2;

        if (formMode == FormMode.WIDE) {
            length /= 2;
            columns = 4;
        }

        SpringUtilities.makeCompactGrid(p,
                length, columns,
                6, 6,
                6, 6);

        add(p, BorderLayout.NORTH);

        JPanel buttons = new JPanel();
        buttons.setLayout(new BorderLayout());

        JPanel buttonsLeft = new JPanel();
        buttonsLeft.add(cancelButton);
        buttonsLeft.add(resetButton);

        JPanel buttonsRight = new JPanel();
        buttonsRight.add(saveButton);

        buttons.add(buttonsLeft, BorderLayout.WEST);
        buttons.add(buttonsRight, BorderLayout.EAST);

        add(buttons, BorderLayout.SOUTH);

        Log.log(this, "construct", "finished.");

    }

    public <X extends JComponent> FormElement<X> getFormElement(Field field) {
        return elements.get(field);
    }

    protected void updateTextField(Field field, String value) {
        JTextField c = (JTextField) getFormElement(field).getComponent();
        c.setText(value);
    }

    protected void updateComboBox(Field field, Object value) {
        JComboBox c = (JComboBox) getFormElement(field).getComponent();
        c.setSelectedItem(value);
    }

    protected void updateCalendarCombo(Field field, Object value) {
        Timestamp timestamp = (Timestamp) value;
        JCalendarCombo c = (JCalendarCombo) getFormElement(field).getComponent();
        if (timestamp != null) {
            c.setDate(new Date(timestamp.getTime()));
        } else {
            c.setDate(null);
        }
        c.repaint();
    }

    protected void updateCheckBox(Field field, boolean value) {
        JCheckBox c = (JCheckBox) getFormElement(field).getComponent();
        c.setSelected(value);
    }

    protected JTextField getTextField(Field field) {
        return (JTextField) getFormElement(field).getComponent();
    }

    protected JComboBox getComboBox(Field field) {
        return (JComboBox) getFormElement(field).getComponent();
    }

    protected JCalendarCombo getCalendarCombo(Field field) {
        return (JCalendarCombo) getFormElement(field).getComponent();
    }

    protected JCheckBox getCheckBox(Field field) {
        return (JCheckBox) getFormElement(field).getComponent();
    }

    protected String getString(Field field) {
        return getTextField(field).getText();
    }

    protected int getInt(Field field) {
        return Integer.valueOf(getTextField(field).getText());
    }

    protected boolean getBoolean(Field field) {
        return getCheckBox(field).isSelected();
    }

    protected Object getObject(Field field) {
        return getComboBox(field).getSelectedItem();
    }

    protected Timestamp getDate(Field field) {
        java.util.Date date = getCalendarCombo(field).getDate();
        return date != null ? new Timestamp(date.getTime()) : null;
    }

    public abstract void save() throws ServiceException;
    public abstract void reset();
    public abstract void cancel();
    public abstract boolean validation();
}
