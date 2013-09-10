package org.mili.application.ui;

import org.mili.application.dto.Attributeable;
import org.mili.application.service.ServiceException;

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
public abstract class SearchPanel<T extends Attributeable> extends JPanel implements ListSelectionListener {
    private JTable table;
    private SimpleTableModel<T> tableModel;
    private SelectionChangeListener _selectionChangeListener;

    protected SearchPanel(SimpleTableModel<T> newTableModel) {
        tableModel = newTableModel;
        setLayout(new BorderLayout());

        JPanel search = new JPanel(new BorderLayout());
        search.add(new JLabel("Filter:"), BorderLayout.WEST);
        final JTextField filter = new JTextField();
        search.add(filter, BorderLayout.CENTER);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                tableModel.filterIntern(filter.getText());
            }
        });
        search.add(searchButton, BorderLayout.EAST);
        add("North", search);

        table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(400, 600));
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener(this);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setWheelScrollingEnabled(true);
        add("Center", scrollPane);

        JPanel control = new JPanel(new BorderLayout());

        JButton newButton = new JButton("New");
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                _selectionChangeListener.onSelectionChanged(create());
            }
        });
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    T object = tableModel.get(row);
                    try {
                        if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(SearchPanel.this, "(" + object + ") Wirklich löschen?")) {
                            delete(object);
                            _selectionChangeListener.onSelectionChanged(create());
                            tableModel.update();
                        }
                    } catch (ServiceException e) {
                        JOptionPane.showMessageDialog(SearchPanel.this, e.getMessage());
                    }
                }
            }
        });
        control.add(deleteButton, BorderLayout.WEST);
        control.add(newButton, BorderLayout.EAST);
        add("South", control);
    }

    public abstract void formChanged(Form form);
    public abstract T create();
    public abstract void delete(T element) throws ServiceException;

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            int row = table.convertRowIndexToModel(table.getSelectedRow());
            if (row >= 0) {
                _selectionChangeListener.onSelectionChanged(tableModel.get(row));
            }
        }
    }

    public void registerSearchSelectionListener(SelectionChangeListener selectionChangeListener) {
        this._selectionChangeListener = selectionChangeListener;
    }

    public SimpleTableModel<T> getTableModel() {
        return tableModel;
    }
}
