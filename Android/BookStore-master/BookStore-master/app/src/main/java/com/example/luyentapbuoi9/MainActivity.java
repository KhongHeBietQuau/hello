package com.example.luyentapbuoi9;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.luyentapbuoi9.Database.Account;
import com.example.luyentapbuoi9.Database.SQLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String arrayBook;
    List<Book> listBook = new ArrayList<>();
    BookAdapter adapter;
    RecyclerView recyclerView;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar=getSupportActionBar();
        recyclerView = findViewById(R.id.rcv);
        img=findViewById(R.id.img);
        String url = "https://bookshopb.herokuapp.com/api/books";
        Intent intent1=getIntent();
        Account account= intent1.getParcelableExtra("account");
        if(account==null){
            account=intent1.getParcelableExtra("acc");
        }
        actionBar.setTitle("Welcome to Book Shop");


        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        Account finalAccount = account;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                arrayBook = response;
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(arrayBook);
                    int size = jsonArray.length();
                    for (int i = 0; i < size; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String imageLink = jsonObject.getString("imageLink");
                        String title = jsonObject.getString("title");
                        String author = jsonObject.getString("author");
                        String des = jsonObject.getString("description");
                        String category = jsonObject.getString("categoty");
                        int page = jsonObject.getInt("numOfPage");
                        int rateStar = jsonObject.getInt("rateStar");
                        int soDanhgia = jsonObject.getInt("numOfReview");
                        long price = jsonObject.getLong("price");
                        Book book=new Book(imageLink, title, author, page, des, rateStar, soDanhgia, price, category);
                        listBook.add(book);

                        adapter = new BookAdapter(listBook, MainActivity.this);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(layoutManager);
                        adapter.setOnClickItemBook(new OnClickItemBook() {
                            @Override
                            public void clickItem(Book book1) {
                                Intent  intent1=new Intent(MainActivity.this, MainActivity2.class);
                                intent1.putExtra("book", book1);
                                intent1.putExtra("account", finalAccount);
                                startActivity(intent1);
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                arrayBook = "Không lấy được API sách đâu kkaka";
            }
        });
        requestQueue.add(stringRequest);

    }

}