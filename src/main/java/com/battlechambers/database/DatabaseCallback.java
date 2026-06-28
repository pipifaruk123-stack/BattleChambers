package com.battlechambers.database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interface for handling asynchronous database operation callbacks
 * Used for async database queries and updates
 * 
 * @author BattleChambers Team
 * @version 1.0.0
 */
public interface DatabaseCallback {

    /**
     * Called when a query result is returned
     * @param result The ResultSet from the query
     */
    default void onResult(ResultSet result) {
    }

    /**
     * Called when an update is completed
     * @param affectedRows The number of rows affected
     */
    default void onUpdate(int affectedRows) {
    }

    /**
     * Called when an error occurs
     * @param error The SQLException that occurred
     */
    default void onError(SQLException error) {
    }
}
