package com.example.hirehub;

import android.provider.BaseColumns;

/**
 * Contract class for the database of the Hire Hub app.
 * This class defines the exact schema of the SQLite database for users and posts.
 */
public final class HireHubContract {

    // Prevent the contract class from being instantiated.
    private HireHubContract() {}

    /**
     * Inner class that defines the table contents for the "users" table.
     */
    public static class UsersTable implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME_USER_ID = _ID; // Inherits the _ID field from BaseColumns for unique id
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_CITY = "city";
        public static final String COLUMN_NAME_EMPLOYMENT_STATUS = "employment_status";
    }

    /**
     * Inner class that defines the table contents for the "posts" table.
     */
    public static class PostsTable implements BaseColumns {
        public static final String TABLE_NAME = "posts";
        public static final String COLUMN_NAME_POST_ID = _ID; // Inherits the _ID field from BaseColumns for unique id
        public static final String COLUMN_NAME_USER_ID = "user_id"; // Foreign key to UsersTable _ID
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_SECTOR = "sector";
        public static final String COLUMN_NAME_CONTRACT_TYPE = "contract_type";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_CITY = "city";
    }

}
