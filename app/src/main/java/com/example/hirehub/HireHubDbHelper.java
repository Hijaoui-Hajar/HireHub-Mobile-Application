package com.example.hirehub;

import static android.app.DownloadManager.COLUMN_TITLE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HireHubDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1; // Version de la base de données
    public static final String DATABASE_NAME = "HireHub.db"; // Nom de la base de données

    // Nom de la table des postes
    private static final String TABLE_POSTS = "posts";

    // Noms des colonnes de la table des postes
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_SECTOR = "sector";
    private static final String COLUMN_CONTRACT_TYPE = "contract_type";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_CITY = "city";
    // Requête de création de la table des utilisateurs
    private static final String SQL_CREATE_USERS =
            "CREATE TABLE " + HireHubContract.UsersTable.TABLE_NAME + " (" +
                    HireHubContract.UsersTable.COLUMN_NAME_USER_ID + " INTEGER PRIMARY KEY," +
                    HireHubContract.UsersTable.COLUMN_NAME_EMAIL + " TEXT," +
                    HireHubContract.UsersTable.COLUMN_NAME_PASSWORD + " TEXT," +
                    HireHubContract.UsersTable.COLUMN_NAME_CITY + " TEXT," +
                    HireHubContract.UsersTable.COLUMN_NAME_EMPLOYMENT_STATUS + " TEXT)";

    // Requête de création de la table des postes
    private static final String SQL_CREATE_POSTS =
            "CREATE TABLE " + HireHubContract.PostsTable.TABLE_NAME + " (" +
                    HireHubContract.PostsTable.COLUMN_NAME_POST_ID + " INTEGER PRIMARY KEY," +
                    HireHubContract.PostsTable.COLUMN_NAME_USER_ID + " INTEGER," +
                    HireHubContract.PostsTable.COLUMN_NAME_TITLE + " TEXT," +
                    HireHubContract.PostsTable.COLUMN_NAME_CATEGORY + " TEXT," +
                    HireHubContract.PostsTable.COLUMN_NAME_SECTOR + " TEXT," +
                    HireHubContract.PostsTable.COLUMN_NAME_CONTRACT_TYPE + " TEXT," +
                    HireHubContract.PostsTable.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    HireHubContract.PostsTable.COLUMN_NAME_CITY + " TEXT," +
                    "FOREIGN KEY (" + HireHubContract.PostsTable.COLUMN_NAME_USER_ID + ") REFERENCES " +
                    HireHubContract.UsersTable.TABLE_NAME + "(" + HireHubContract.UsersTable.COLUMN_NAME_USER_ID + "))";

    // Requête de suppression de la table des utilisateurs
    private static final String SQL_DELETE_USERS =
            "DROP TABLE IF EXISTS " + HireHubContract.UsersTable.TABLE_NAME;
    // Requête de suppression de la table des postes
    private static final String SQL_DELETE_POSTS =
            "DROP TABLE IF EXISTS " + HireHubContract.PostsTable.TABLE_NAME;

    public HireHubDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Créer les table lors de la création de la base de données
        db.execSQL(SQL_CREATE_USERS);
        db.execSQL(SQL_CREATE_POSTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Supprimer et recréer les table lors d'une mise à jour de la base de données
        db.execSQL(SQL_DELETE_USERS);
        db.execSQL(SQL_DELETE_POSTS);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    //méthode pour insérer un poste dans la base de données
    public long insertPost(String title, String category, String sector, String contractType, String description, String city) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_SECTOR, sector);
        values.put(COLUMN_CONTRACT_TYPE, contractType);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_CITY, city);
        long newRowId = db.insert(TABLE_POSTS, null, values);
        db.close();
        return newRowId;
    }

        // Méthode pour obtenir le nombre de postes pour une ville donnée
        public int getPostCountByCity(String city) {
            SQLiteDatabase db = this.getReadableDatabase();
            int count = 0;

            // Construire la requête SQL pour compter le nombre de postes dans la ville spécifiée
            String query = "SELECT COUNT(*) FROM " + HireHubContract.PostsTable.TABLE_NAME +
                    " WHERE " + HireHubContract.PostsTable.COLUMN_NAME_CITY + " = ?";
            Cursor cursor = db.rawQuery(query, new String[]{city});

            // Vérifier si le curseur est valide et déplacer le curseur au premier élément
            if (cursor != null) {
                if (((Cursor) cursor).moveToFirst()) {
                    count = cursor.getInt(0); // Le nombre est dans la première colonne
                }
                cursor.close();
            }

            // Fermer la connexion à la base de données
            db.close();

            return count;
        }

}
