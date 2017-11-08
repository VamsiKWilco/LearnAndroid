package com.prisam.customcalendar.common;

import android.view.View;

/**
 * Created by vamsi on 10/12/2017.
 */

public interface OnItemClickListener {

    void onItemClick(View view, int position);

    void onItemScroll(int position);
}
