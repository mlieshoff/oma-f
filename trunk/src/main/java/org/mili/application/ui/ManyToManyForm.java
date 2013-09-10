package org.mili.application.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: papa
 * Date: 20.07.13
 * Time: 14:00
 * To change this template use File | Settings | File Templates.
 */
public abstract class ManyToManyForm<N, M> extends Form implements ManyToManyModelChangeListener {
    private JTable table;
    private ManyToManyTableModel<N, M> tableModel;
    protected N formObject;

    protected ManyToManyForm(String label, ManyToManyTableModel<N, M> newTableModel) {
        super(label);
        tableModel = newTableModel;
        tableModel.setManyToManyModelChangeListener(this);
        setLayout(new BorderLayout());

        JPanel search = new JPanel(new BorderLayout());
        search.add(new JLabel("Filter:"), BorderLayout.WEST);
        search.add(new JTextField(), BorderLayout.CENTER);
        add("North", search);

        table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(400, 600));
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setWheelScrollingEnabled(true);
        add("Center", scrollPane);
    }

    public ManyToManyTableModel<N, M> getTableModel() {
        return tableModel;
    }

}
