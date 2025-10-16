package com.thisdayhistory.ui;

import com.thisdayhistory.model.HistoricalFact;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class FactTableModel extends AbstractTableModel {

    private final List<HistoricalFact> facts;
    private final String[] columnNames = {"ID", "Event", "Category"};

    public FactTableModel(List<HistoricalFact> facts) {
        this.facts = facts;
    }

    @Override
    public int getRowCount() {
        return facts.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        HistoricalFact fact = facts.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return fact.getId();
            case 1:
                return fact.getEvent();
            case 2:
                return fact.getCategory();
            default:
                return null;
        }
    }
}
