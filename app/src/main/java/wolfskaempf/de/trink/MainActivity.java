package wolfskaempf.de.trink;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private SQLiteDatabase store = null;

    private TextView progressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressTextView = findViewById(R.id.progess_text_view);


        store = this.openOrCreateDatabase("store", MODE_PRIVATE, null);

        store.execSQL("CREATE TABLE IF NOT EXISTS drinks (amount integer);");

        updateProgress();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.reset_menu_item:
                store.execSQL("DELETE FROM drinks");
                updateProgress();
                return true;
            case R.id.remove_50_ml_menu_item:
                addDrink(-50);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addDrink(int amount) {

        try {
            store.execSQL("INSERT INTO drinks VALUES (" + String.valueOf(amount) + ");");

            updateProgress();

        } catch (SQLException | CursorIndexOutOfBoundsException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void updateProgress() {
        try {

            int progress;

            Cursor cursor = store.rawQuery("SELECT sum(amount) FROM drinks;", null);

            if (cursor.moveToFirst()) {
                progress = cursor.getInt(0);
            } else {
                progress = -1;
            }

            cursor.close();

            progressTextView.setText(getString(R.string.text_view_progress_dynamic, progress));

        } catch (SQLException | CursorIndexOutOfBoundsException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void on100mlButtonClick(View view) {
        addDrink(100);
    }

    public void on250mlButtonClick(View view) {
        addDrink(250);
    }

    public void on500mlButtonClick(View view) {
        addDrink(500);
    }

    public void on1lButtonClick(View view) {
        addDrink(1000);
    }
}
