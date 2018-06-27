package k0bin.notes.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import androidx.navigation.Navigation;
import k0bin.notes.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.navHostFragment).navigateUp();
    }
}
