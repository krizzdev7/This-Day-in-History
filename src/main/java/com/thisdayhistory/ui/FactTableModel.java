package com.thisdayhistory.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.thisdayhistory.model.HistoricalFact;

public class FactTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = {"Year", "Description", "Category", "Favorite"};
    private static final Class<?>[] COLUMN_TYPES = {Integer.class, String.class, String.class, Boolean.class};
    
    private List<HistoricalFact> facts;

    public FactTableModel() {
        this.facts = new ArrayList<>();
    }
    
    public void setFacts(List<HistoricalFact> facts) {
        this.facts = new ArrayList<>(facts);
        fireTableDataChanged();
    }
    
    public HistoricalFact getFactAt(int rowIndex) {
        return facts.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return facts.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return COLUMN_TYPES[columnIndex];
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 3; // Only Favorite column is editable
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        HistoricalFact fact = facts.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> fact.getYear();
                case 1 -> fact.getEvent();
            case 2 -> fact.getCategory();
            case 3 -> fact.isFavorite();
            default -> throw new IllegalArgumentException("Invalid column index: " + columnIndex);
        };
    }
    
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (columnIndex == 3 && value instanceof Boolean) {
            HistoricalFact fact = facts.get(rowIndex);
            fact.setFavorite((Boolean) value);
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }
}
