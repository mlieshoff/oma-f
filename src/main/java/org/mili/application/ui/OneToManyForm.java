package org.mili.application.ui;

import org.mili.application.service.ServiceException;
import org.mili.application.util.Log;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: papa
 * Date: 20.07.13
 * Time: 14:00
 * To change this template use File | Settings | File Templates.
 */
public abstract class OneToManyForm<One, Many> extends Form implements FormChangeListener, ListSelectionListener,
        SelectionChangeListener {
    private JTable table;
    private OneToManyTableModel<One, Many> tableModel;
    protected One oneFormObject;
    SimpleForm<Many> mForm;

    protected OneToManyForm(String label, OneToManyTableModel<One, Many> newTableModel, SimpleForm<Many> mForm) {
        super(label);
        this.mForm = mForm;
        tableModel = newTableModel;
        setLayout(new BorderLayout());

        // select panel
        JPanel select = new JPanel();
        select.setLayout(new BorderLayout());

        JPanel search = new JPanel(new BorderLayout());
        search.add(new JLabel("Filter:"), BorderLayout.WEST);
        search.add(new JTextField(), BorderLayout.CENTER);
        select.add("North", search);

        table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(400, 600));
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener(this);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setWheelScrollingEnabled(true);
        select.add("Center", scrollPane);

        JPanel control = new JPanel(new BorderLayout());

        JButton newButton = new JButton("New");
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                oneSelectionChanged(create(oneFormObject));
            }
        });
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    try {
                        Many many = tableModel.get(row);
                        if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(OneToManyForm.this, "(" + many + ") Wirklich löschen?")) {
                            delete(many);
                            tableModel.update(oneFormObject);
                        }
                    } catch (ServiceException e) {
                        JOptionPane.showMessageDialog(OneToManyForm.this, e.getMessage());
                    }
                }
            }
        });
        control.add(deleteButton, BorderLayout.WEST);
        control.add(newButton, BorderLayout.EAST);
        add("South", control);

        mForm.registerFormDataListener(this);

        add(select, "West");
        add(mForm, "Center");
    }

    public abstract Many create(One oneObject);
    public abstract void delete(Many element) throws ServiceException;

    public OneToManyTableModel<One, Many> getTableModel() {
        return tableModel;
    }

    public void updateOneFormObject(One one) {
        mForm.onSelectionChanged(one);
        oneFormObject = one;
        tableModel.update(one);
    }

    @Override
    public void registerFormDataListener(FormChangeListener formChangeListener) {
        super.registerFormDataListener(formChangeListener);
    }

    public void oneSelectionChanged(Object element) {
        mForm.onSelectionChanged(element);
    }

    @Override
    public void onFormChanged(Form form) {
        Log.log(this, "onFormChanged", "form: %s", form);
        getTableModel().update(oneFormObject);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            int row = table.convertRowIndexToModel(table.getSelectedRow());
            if (row >= 0) {
                oneSelectionChanged(oneFormObject);
                oneSelectionChanged(tableModel.get(row));
            }
        }
    }

}
