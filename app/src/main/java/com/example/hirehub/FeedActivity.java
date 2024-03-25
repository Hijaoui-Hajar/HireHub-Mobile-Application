package com.example.hirehub;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import java.util.ArrayList;
import android.widget.Button;

public class FeedActivity extends Activity {
    private RecyclerView postsRecyclerView;
    private HireHubDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        dbHelper = new HireHubDbHelper(this);
        postsRecyclerView = findViewById(R.id.postsRecyclerView);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Récupérer la ville sélectionnée depuis l'intent
        String selectedCity = getIntent().getStringExtra("selectedCity");

        // Interroger la base de données pour obtenir le nombre de postes dans la ville sélectionnée
        int postCount = dbHelper.getPostCountByCity(selectedCity);

        // Afficher le nombre de postes à l'utilisateur
        TextView postCountTextView = findViewById(R.id.numberOfAdsTextView);
        postCountTextView.setText("Nombre de postes dans " + selectedCity + ": " + postCount);
        // Fetch user city from SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("YourAppPreferences", MODE_PRIVATE);
        String userCity = sharedPref.getString("userCity", "DefaultCity"); // Use a sensible default if not set
        // Charger les annonces pour la ville sélectionnée
        loadPostsByCity(userCity);
        // Créer une instance de AdManager en lui passant la référence à la base de données
        AdManager adManager = new AdManager(dbHelper.getWritableDatabase());
        // Récupérer la valeur de la ville passée par l'activité précédente
        Intent intent = getIntent();
        // Utiliser la valeur de la ville pour récupérer le nombre d'annonces correspondant
        // Supposons que vous ayez une méthode pour récupérer le nombre d'annonces pour une ville donnée
        int numberOfAds = getNumberOfAdsForCity(selectedCity);

        // Afficher le nombre d'annonces dans votre interface utilisateur (par exemple, dans un TextView)
        TextView numberOfAdsTextView = findViewById(R.id.numberOfAdsTextView);
        numberOfAdsTextView.setText("Number of ads in " + selectedCity + ": " + numberOfAds);

            Button backButton = findViewById(R.id.backToPostButton);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Retourner à la page de publication
                    Intent intent = new Intent(FeedActivity.this, PostingActivity.class);
                    startActivity(intent);
                }
            });

    }

    private int getNumberOfAdsForCity(String selectedCity) {
        return 1;
    }

    public class AdManager {
        public AdManager(SQLiteDatabase writableDatabase) {
        }
        public AdManager() {
        }
        public int getNumberOfAdsForCity(String city) {
            // Logique pour obtenir le nombre d'annonces pour une ville donnée
            int numberOfAds = 0;
            return numberOfAds; // Nombre d'annonces pour la ville donnée
        }
    }
    AdManager adManager = new AdManager();
    String selectedCity = "Agadir"; // La ville sélectionnée à partir de votre interface utilisateur
    int numberOfAds = adManager.getNumberOfAdsForCity(selectedCity);


    private void loadPostsByCity(String city) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Post> posts = new ArrayList<>();

        String query = "SELECT p." + HireHubContract.PostsTable.COLUMN_NAME_TITLE + ", " +
                "p." + HireHubContract.PostsTable.COLUMN_NAME_DESCRIPTION + ", " +
                "u." + HireHubContract.UsersTable.COLUMN_NAME_EMAIL +
                " FROM " + HireHubContract.PostsTable.TABLE_NAME + " p" +
                " JOIN " + HireHubContract.UsersTable.TABLE_NAME + " u ON p." +
                HireHubContract.PostsTable.COLUMN_NAME_USER_ID + " = u." +
                HireHubContract.UsersTable._ID +
                " WHERE p." + HireHubContract.PostsTable.COLUMN_NAME_CITY + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{city});

        while (cursor.moveToNext()) {
            String email = cursor.getString(cursor.getColumnIndexOrThrow(HireHubContract.UsersTable.COLUMN_NAME_EMAIL));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(HireHubContract.PostsTable.COLUMN_NAME_TITLE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(HireHubContract.PostsTable.COLUMN_NAME_DESCRIPTION));
            posts.add(new Post(email, title, description));
        }
        cursor.close();

        PostsAdapter adapter = new PostsAdapter(this, posts);
        postsRecyclerView.setAdapter(adapter);

        // Check if posts list is empty and show a toast message if it is
        if (posts.isEmpty()) {
            Toast.makeText(this, "No posts found in your city.", Toast.LENGTH_LONG).show();
        }
    }
}
