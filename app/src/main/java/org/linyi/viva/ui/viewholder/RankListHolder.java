package org.linyi.viva.ui.viewholder;


import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.api.changdu.proto.BookApi;


import org.linyi.base.utils.ImageLoader.ImageLoaderUtil;
import org.linyi.base.utils.UIUtils;
import org.linyi.ui.recycler.RecyclerViewBaseHolder;
import org.linyi.viva.R;
import org.linyi.viva.ui.activity.BookDetailActivity;

public class RankListHolder extends RecyclerViewBaseHolder {
    private RelativeLayout rl;
    private ImageView img_cover;
    private TextView tv_title, tv_type, tv_author, tv_description, tv_read_total, tv_rank;

    public RankListHolder(View itemView) {
        super(itemView);
        rl=itemView.findViewById(R.id.rl);
        img_cover = itemView.findViewById(R.id.img_cover);
        tv_title = itemView.findViewById(R.id.tv_title);
        tv_type = itemView.findViewById(R.id.tv_type);
        tv_author = itemView.findViewById(R.id.tv_author);
        tv_description = itemView.findViewById(R.id.tv_description);
        tv_read_total = itemView.findViewById(R.id.tv_read_total);
        tv_rank = itemView.findViewById(R.id.tv_rank);

    }

    public void setData(final BookApi.BookOverView data, int position) {
        ViewGroup.LayoutParams params = img_cover.getLayoutParams();
        params.width = UIUtils.dp2px(57);
        params.height = UIUtils.dp2px(76);

        img_cover.setLayoutParams(params);
        ImageLoaderUtil.getInstance().loadImage(img_cover, data.getCoverImage());

        tv_title.setText(data.getTitle());
        tv_type.setText(data.getCategoryName());
        tv_author.setText(data.getAuthorName());
        tv_description.setText(data.getDescription());
        String read_total = data.getReadTotal() + "浏览";
        tv_read_total.setText(read_total);
        if (position == 0) {
            tv_rank.setText("TOP1");
            tv_rank.setTextColor(Color.parseColor("#F52929"));
        } else if (position == 1) {
            tv_rank.setText("TOP2");
            tv_rank.setTextColor(Color.parseColor("#FE5E23"));
        } else if (position == 2) {
            tv_rank.setText("TOP3");
            tv_rank.setTextColor(Color.parseColor("#FEA423"));
        } else {
            tv_rank.setText(position+1 + "");
            tv_rank.setTextColor(Color.parseColor("#CCCCCC"));
        }
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(rl.getContext(), BookDetailActivity.class);
                intent.putExtra("id", data.getBookID());
                rl.getContext().startActivity(intent);
            }
        });
        RecyclerView.LayoutParams params1= (RecyclerView.LayoutParams) rl.getLayoutParams();
       if (position==0){
           params1.setMargins(0,0,0,0);
       }else {
           params1.setMargins(0,UIUtils.dp2px(1),0,0);
       }
        rl.setLayoutParams(params1);
    }
}
