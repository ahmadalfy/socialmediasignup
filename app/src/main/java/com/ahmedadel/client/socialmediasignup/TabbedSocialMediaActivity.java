package com.ahmedadel.client.socialmediasignup;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.ahmedadel.socialmediasignup.SocialMediaSignUp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed Adel on 2/16/18.
 */

public class TabbedSocialMediaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_social_media);

        setSupportActionBar(findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.tabbed_social_media);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void initUI() {
        ViewPager viewPager = findViewById(R.id.tabbed_social_media_view_pager);
        TabLayout tabbedBrowserTabLayout = findViewById(R.id.tabbed_social_media_tab_layout);

        List<SocialMediaItem> items = getListOfItems();
        if (items != null && items.size() <= 3)
            tabbedBrowserTabLayout.setTabMode(TabLayout.MODE_FIXED);
        else
            tabbedBrowserTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        setupViewPager(viewPager, items);
        tabbedBrowserTabLayout.setupWithViewPager(viewPager);
    }

    private List<SocialMediaItem> getListOfItems() {
        ArrayList<SocialMediaItem> socialMediaItems = new ArrayList<>();
        socialMediaItems.add(new SocialMediaItem(SocialMediaSignUp.SocialMediaType.FACEBOOK));
        socialMediaItems.add(new SocialMediaItem(SocialMediaSignUp.SocialMediaType.TWITTER));
        socialMediaItems.add(new SocialMediaItem(SocialMediaSignUp.SocialMediaType.INSTAGRAM));
        socialMediaItems.add(new SocialMediaItem(SocialMediaSignUp.SocialMediaType.GOOGLE_PLUS));
        socialMediaItems.add(new SocialMediaItem(SocialMediaSignUp.SocialMediaType.LINKEDIN));
        return socialMediaItems;
    }

    protected void setupViewPager(ViewPager viewPager, List<SocialMediaItem> items) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        for (SocialMediaItem item : items)
            adapter.addFragment(SocialMediaFragment.newInstance(item.getSocialMediaType()), item.getSocialMediaType().getValue());

        viewPager.setOffscreenPageLimit(items.size());
        viewPager.setAdapter(adapter);
    }

    private class SocialMediaItem {
        private SocialMediaSignUp.SocialMediaType socialMediaType;

        SocialMediaItem(SocialMediaSignUp.SocialMediaType socialMediaType) {
            this.socialMediaType = socialMediaType;
        }

        SocialMediaSignUp.SocialMediaType getSocialMediaType() {
            return socialMediaType;
        }
    }

    protected class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }

}
