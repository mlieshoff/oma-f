package org.mili.application.ui;

import org.mili.application.dto.Attributeable;

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
public abstract class SimpleTableModel<T extends Attributeable> extends AbstractTableModel {
    private List<T> model = new ArrayList<>();
    private String[] columnNames;

    protected SimpleTableModel(String[] columnNames) {
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

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex) {
            case 0:
                return rowIndex + 1;
        }
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return Integer.class;
        }
        return super.getColumnClass(columnIndex);
    }

    public T get(int row) {
        return model.get(row);
    }

    @Override
    public int getRowCount() {
        return model.size();
    }

    public abstract List<T> load();

    public void update() {
        model.clear();
        model.addAll(load());
        fireTableDataChanged();
    }

    public void filterIntern(String filter) {
        model.clear();
        model.addAll(filter(filter));
        fireTableDataChanged();
    }

    public List<T> filter(String filter) {
        filter = filter.toLowerCase();
        List<T> list = new ArrayList<>();
        for (T t : load()) {
            StringBuilder s = new StringBuilder();
            for (Object o : t.valueMap().values()) {
                if (o != null) {
                    s.append(o.toString());
                }
            }
            if (s.toString().toLowerCase().contains(filter)) {
                list.add(t);
            }
        }
        return list;
    }
}
