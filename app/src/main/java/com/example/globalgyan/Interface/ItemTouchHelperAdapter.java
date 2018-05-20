package com.example.globalgyan.Interface;

/**
 * Created by HP on 5/19/2018.
 */

public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
