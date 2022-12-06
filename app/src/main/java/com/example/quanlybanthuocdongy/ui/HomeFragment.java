package com.example.quanlybanthuocdongy.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.example.quanlybanthuocdongy.R;
import com.example.quanlybanthuocdongy.databinding.FragmentHomeBinding;

import model.MedicineViewModel;
import view.ViewPagerAdapter;

public class HomeFragment extends Fragment{

    public AHBottomNavigation bottomNavigation;
    private AHBottomNavigationViewPager navigationViewPager;
    private View viewEndAnimation;
    private ImageView viewAnimation;
    private int countMedicine;

    private FragmentHomeBinding homeBinding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeBinding = FragmentHomeBinding.inflate (inflater, container, false);
        return homeBinding.getRoot ();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);
        bottomNavigation = homeBinding.AHBottomNavigation;
        navigationViewPager = homeBinding.AHBottomNavigationViewpager;
        viewEndAnimation = homeBinding.viewEndAnimation;
        viewAnimation = homeBinding.viewAnimation;

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter (getChildFragmentManager (), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        navigationViewPager.setAdapter (pagerAdapter);
        navigationViewPager.setPagingEnabled (true);
        initOjects();
    }

    private void initOjects() {
//        Create items
        AHBottomNavigationItem product = new AHBottomNavigationItem(R.string.home_fragment, R.drawable.ic_navigation_bottom_product, R.color.color_tab_product);
        AHBottomNavigationItem cart = new AHBottomNavigationItem(R.string.cart_fragment, R.drawable.ic_navigation_bottom_cart, R.color.color_tab_cart);
//        Add items
        bottomNavigation.addItem(product);
        bottomNavigation.addItem(cart);

        bottomNavigation.setOnTabSelectedListener ((position, wasSelected) -> {
            navigationViewPager.setCurrentItem (position);
            return true;
        });

        navigationViewPager.addOnPageChangeListener (new ViewPager.OnPageChangeListener () {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigation.setCurrentItem (position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setCountMedicineInCart(int count){
        countMedicine = count;
        AHNotification notification = new AHNotification.Builder()
                .setText(String.valueOf (count))
                .setBackgroundColor(ContextCompat.getColor(getContext (), R.color.green))
                .setTextColor(ContextCompat.getColor(getContext (), R.color.white))
                .build();
        bottomNavigation.setNotification(notification, 1);
    }

    public int getCountMedicine() {
        return countMedicine;
    }

    public View getViewEndAnimation() {
        return viewEndAnimation;
    }

    public ImageView getViewAnimation() {
        return viewAnimation;
    }
}