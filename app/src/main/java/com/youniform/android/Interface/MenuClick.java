package com.youniform.android.Interface;

import com.youniform.android.Fragment.MenuSubFragment;

public interface MenuClick {
    void HeaderClick(int headerId, int position, MenuSubFragment.TreeModel treeModel);

    void SectionClick(int sectionId, int position, MenuSubFragment.TreeModel treeModel);
}
