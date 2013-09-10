package org.mili.application.ui;

import javax.swing.*;
import java.awt.*;

/**
 */
public class Portal extends JPanel implements SelectionChangeListener, FormChangeListener {

    private SearchPanel searchPanel;
    private Form[] forms;

    public Portal(SearchPanel searchPanel, Form[] forms) {
        this.searchPanel = searchPanel;
        this.searchPanel.registerSearchSelectionListener(this);
        this.forms = forms;
        GridLayout gridLayout = new GridLayout(2, 1);
        setLayout(gridLayout);
        add(searchPanel);
        JTabbedPane tabbedPane = new JTabbedPane();

        for (Form form : forms) {
            form.registerFormDataListener(this);
            tabbedPane.add(form.getLabel(), form);
        }

        add(tabbedPane);
    }

    public void onFormChanged(Form form) {
        searchPanel.formChanged(form);
    }

    @Override
    public void onSelectionChanged(Object element) {
        for (Form form : forms) {
            form.onSelectionChanged(element);
        }
    }
}
