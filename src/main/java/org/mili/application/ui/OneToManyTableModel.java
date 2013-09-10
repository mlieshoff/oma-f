package org.mili.application.ui;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: papa
 * Date: 20.07.13
 * Time: 20:41
 * To change this template use File | Settings | File Templates.
 */
public abstract class OneToManyTableModel<N, M> extends AbstractTableModel {
    private List<M> model = new ArrayList<>();
    private String[] columnNames;

    protected OneToManyTableModel(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    @Override
    public String getColumnName(int column) {
        return getColumnNames()[column];
    }

    @Override
    public int getColumnCount() {
        return getColumnNames().length;
    }

    public M get(int row) {
        return model.get(row);
    }

    @Override
    public int getRowCount() {
        return model.size();
    }

    public abstract List<M> load(N n);

    public void update(N n) {
        model.clear();
        model.addAll(load(n));
        fireTableDataChanged();
    }

}
