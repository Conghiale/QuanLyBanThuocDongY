package view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.example.quanlybanthuocdongy.ui.CartFragment;
import com.example.quanlybanthuocdongy.ui.HomeFragment;
import com.example.quanlybanthuocdongy.ui.ProductFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super (fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1:
                return new CartFragment ();
            case 0:
            default:
                return new ProductFragment ();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
