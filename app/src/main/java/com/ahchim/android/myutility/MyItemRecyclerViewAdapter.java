package com.ahchim.android.myutility;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.ahchim.android.myutility.FiveFragment.OnListFragmentInteractionListener;
import com.ahchim.android.myutility.dummy.DummyContent.DummyItem;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    // Image 경로가 있는 컬럼명을 담는 String List
    private final List<String> datas;
    private final OnListFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapter(Context context, OnListFragmentInteractionListener mListener) {
        this.context = context;

        // 폰에서 이미지를 가져온 후 datas에 세팅한다.
        this.datas = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        // 1. 데이터 Uri 정의
        Uri target = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        // 2. projections 정의
        String[] projection = {MediaStore.Images.Media.DATA}; // DATA : image 경로가 있는 컬럼명
        // 3. 데이터 가져오기
        Cursor cursor = resolver.query(target, projection, null, null, null);
        if(cursor!=null) {
            while (cursor.moveToNext()) {
                //int idx = cursor.getColumnIndex(projection[0]);
                String uriString = cursor.getString(0);
                datas.add(uriString);

            }
            cursor.close();
        }
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.imagePath = datas.get(position);
        // 이미지 사이즈로 인해 터짐
        //holder.imageView.setImageURI(holder.imageUri);
        Glide.with(context).load(holder.imagePath).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //public final View mView;
        public final ImageView imageView;
        public String imagePath;

        public ViewHolder(View view) {
            super(view);
            //mView = view;
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) context.getApplicationContext()
                                                                 .getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metrics);

            imageView = (ImageView) view.findViewById(R.id.id);
            LayoutParams params = (LayoutParams) imageView.getLayoutParams();
            params.width = (int) (metrics.widthPixels / 4);
            params.height = (int) (metrics.widthPixels / 4);  // 가로세로 길이 동일하게 하려고 여기도 widthPixels 넣어줌

            imageView.setLayoutParams(params);



            imagePath = null;


            // 홀더에 클릭리스너 넣으면 좋다! (원본에서도 뷰홀더에 구현되어 있었다.)
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 클릭 시 큰이미지 보여주기
                    // View로 띄우기..
                    Intent intent = new Intent(context, DetailActivity.class);
                    context.startActivity(intent);
                }
            });
        }
//
//        @Override
//        public String toString() {
//            return super.toString() + " '" + mContentView.getText() + "'";
//        }
    }
}
