package br.com.ownard.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import br.com.ownard.fragments.BlockCallsFragment;


public class CallPagerAdapter extends FragmentPagerAdapter{

    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> tabNames = new ArrayList<>();
    ArrayList<Integer> tabIcons = new ArrayList<>();

    public void addFragmentTab(Fragment fragment, String tabName, Integer icon){
        this.fragments.add(fragment);
        this.tabNames.add(tabName);
        this.tabIcons.add(icon);
    }

    public CallPagerAdapter(FragmentManager fm){
            super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


    @Override
    public int getItemPosition(Object object) {

        if(object instanceof BlockCallsFragment){
            BlockCallsFragment f = (BlockCallsFragment)object;
            f.update();
        }
        return super.getItemPosition(object);
    }



   /* @Override
    public CharSequence getPageTitle(int position) {
        return tabNames.get(position);
    } */

}
