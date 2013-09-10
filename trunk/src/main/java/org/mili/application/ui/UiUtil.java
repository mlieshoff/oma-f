package org.mili.application.ui;

import org.mili.application.util.Lambda;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: papa
 * Date: 17.07.13
 * Time: 23:37
 * To change this template use File | Settings | File Templates.
 */
public class UiUtil {

    public static void createFromField(JPanel p, String label, JComponent component) {
        JLabel l = new JLabel(label, JLabel.TRAILING);
        p.add(l);
        l.setLabelFor(component);
        p.add(component);
    }

    public static JButton createButton(String label, final Lambda action) {
        JButton button = new JButton(label);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    action.exec();
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }
        });
        return button;
    }

}
