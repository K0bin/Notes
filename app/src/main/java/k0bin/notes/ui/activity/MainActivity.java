package k0bin.notes.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
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

    @Override
    public void onBackPressed() {
        NavDestination currentTop = Navigation.findNavController(this, R.id.navHostFragment).getCurrentDestination();
        if (currentTop instanceof FragmentNavigator.Destination && getSupportFragmentManager() != null) {
            Class<? extends Fragment> fragmentClass = ((FragmentNavigator.Destination) currentTop).getFragmentClass();
            Fragment hostFragment = getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
            if (hostFragment != null) {
                for (Fragment fragment : hostFragment.getChildFragmentManager().getFragments()) {
                    if (fragment.getClass().equals(fragmentClass) && fragment instanceof BackFragment) {
                        if (((BackFragment) fragment).goBack()) {
                            return;
                        }
                    }
                }
            }
        }

        super.onBackPressed();
    }

    public interface BackFragment {
        boolean goBack();
    }
}
