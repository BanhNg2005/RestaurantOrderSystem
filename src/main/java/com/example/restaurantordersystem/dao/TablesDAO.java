package com.example.restaurantordersystem.dao;
import com.example.restaurantordersystem.model.Table;

import java.util.List;

public interface TablesDAO {
    Table findTableByID(long tableId);

    List<Table> findAllTables();

    boolean saveTable(Table table);

    boolean deleteTable(int id);
}
