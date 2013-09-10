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
public abstract class ManyToManyTableModel<N, NM> extends AbstractTableModel {
    private List<NM> model = new ArrayList<>();
    private String[] columnNames;
    private ManyToManyModelChangeListener _manyToManyModelChangeListener;

    protected ManyToManyTableModel(String[] columnNames) {
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
    public boolean isCellEditable(int row, int column) {
        switch (column) {
            case 1:
                return true;
            default:
                return super.isCellEditable(row, column);
        }
    }

    @Override
    public Class getColumnClass(int column) {
        switch (column) {
            case 0:
                return Integer.class;
            case 1:
                return Boolean.class;
            default:
                return super.getColumnClass(column);
        }
    }

    public NM get(int row) {
        return model.get(row);
    }

    @Override
    public int getRowCount() {
        return model.size();
    }

    public abstract List<NM> load(N n);

    public void update(N n) {
        model.clear();
        model.addAll(load(n));
        fireTableDataChanged();
    }

    public void fireModelChange(N n, NM nm) {
        _manyToManyModelChangeListener.onModelChange(n, nm);
    }

    public void setManyToManyModelChangeListener(ManyToManyModelChangeListener manyToManyModelChangeListener) {
        this._manyToManyModelChangeListener = manyToManyModelChangeListener;
    }
}
