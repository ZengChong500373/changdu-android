package org.linyi.viva.ui.viewholder;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import org.linyi.base.utils.UIUtils;
import org.linyi.ui.recycler.RecyclerViewBaseHolder;
import org.linyi.viva.R;

public class ClassifyTypeHolder extends RecyclerViewBaseHolder {
    public TextView tv_type;
    public ClassifyTypeHolder(View itemView) {
        super(itemView);
        tv_type=itemView.findViewById(R.id.tv_type);
    }
    public void setData(String str,boolean isSeleted){
        tv_type.setText(str);
        if (isSeleted){
            tv_type.setTextColor(Color.parseColor("#FE5E23"));
            tv_type.setBackgroundColor(UIUtils.getColor(R.color.white));
        }else {
            tv_type.setTextColor(UIUtils.getColor(R.color.text_color));
            tv_type.setBackgroundColor(UIUtils.getColor(R.color.line_color2));
        }
    }
}
