package com.example.luyentapbuoi9;

import android.content.Context;
import android.view.Choreographer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    List<Book> list;
    Context context;
    OnClickItemBook onClickItemBook;

    public OnClickItemBook getOnClickItemBook() {
        return onClickItemBook;
    }

    public void setOnClickItemBook(OnClickItemBook onClickItemBook) {
        this.onClickItemBook = onClickItemBook;
    }

    public BookAdapter(List<Book> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.custum_item_list, parent, false);
        ViewHolder viewHoldernew = new ViewHolder(view);
        return viewHoldernew;
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, int position) {
        Book book = list.get(position);

        Glide.with(context).load(book.getImageLink()).into(holder.img);
        holder.tvtt.setText(book.getTitle());
        holder.tvPr.setText(book.getPrice()+" vnđ");
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItemBook.clickItem(book);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView  img;
        TextView tvtt, tvPr;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.img);
            tvtt=itemView.findViewById(R.id.tvtt);
            tvPr=itemView.findViewById(R.id.tvPr);
        }
    }
}
