package com.example.teiyuueki.simplelistview;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener {
    // 要素をArrayListで設定
    private List<String> items = new ArrayList<String>();
    private List<String> imagePaths = new ArrayList<String>();

    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // /asset/image/以下に image0.jpg ～ image7.jpg を入れています
        // それのパスを取り出す method
        getImagePath();

        // ListViewのインスタンスを生成 xmlか引っ張ってくる
        ListView listView = (ListView) findViewById(R.id.listView);

        // BaseAdapter を継承したadapterのインスタンスを生成
        // 子要素のレイアウトファイル list_items.xml を activity_main.xml に inflate するためにadapterに引数として渡す
        adapter = new ListViewAdapter(this.getApplicationContext(), R.layout.list_items, items, imagePaths);

        // ListViewにadapterをセット
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

    }


    class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
    //list viewadapter は、baseadapter を元に、
    class ListViewAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private Bitmap bmp = null;
        //inflaterを設定し、使い回し処理を可能にする。
        public ListViewAdapter(Context context, int layoutId, List<String> itemList, List<String> imgList) {
            super();
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            // 最初だけ View を inflate して、それを再利用する
            if (convertView == null) {
                // activity_main.xml の ＜ListView .../＞ に list_items.xml を inflate して convertView とする
                convertView = inflater.inflate(R.layout.list_items, parent, false);
                // ViewHolder を生成
                holder = new ViewHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.textView);
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                convertView.setTag(holder);
            }
            // holder を使って再利用
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            // 現在の position にある画像リストのパスをデコード
            bmp = BitmapFactory.decodeFile(imagePaths.get(position));
            // holder の imageView にセット
            holder.imageView.setImageBitmap(bmp);
            // 現在の position にあるファイル名リストを holder の textView にセット
            holder.textView.setText(items.get(position));

            return convertView;
        }

        @Override
        public int getCount() {
            // items の全要素数を返す
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }


    private void getImagePath() {
        // asset のデータを取り出す
        AssetManager assetManager = getResources().getAssets();

        String[] fileList = null;
        String destPath = null;

        try {
            // assets/image/image0.jpg があるのでフォルダパスをしていする
            fileList = assetManager.list("image");
            Log.d("*************fileList", String.valueOf(fileList));

        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream input;
        FileOutputStream output;


        for (int i = 0; i < fileList.length; i++) {
            try {
                input = assetManager.open("image/" + fileList[i]);
                //
                destPath = "/data/data/" + this.getPackageName() + "/" + fileList[i];
                System.out.print(destPath);
                output = new FileOutputStream(destPath);

                int DEFAULT_BUFFER_SIZE = 10240 * 4;
                byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
                int n = 0;
                while (-1 != (n = input.read(buffer))) {
                    output.write(buffer, 0, n);
                }
                output.close();
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            items.add(fileList[i]);
            Log.d("==========filelist======", fileList[i]);
            imagePaths.add(destPath);
            Log.d("==========destpath=====", destPath);
            //destpa--->ex) data/data/com.example.teiyuueki.simplelistview/image0.jpg

        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Intent intent = new Intent(this.getApplicationContext(), SubActivity.class);

        Log.d("========position", String.valueOf(position));
        Log.d("========imagePaths",imagePaths.get(position));
        String selectedImagePath = imagePaths.get(position);

        // position を「imagePath」のキーワードでインテントにセット
        intent.putExtra("imagePath", selectedImagePath);
        // Activity をスイッチする
        startActivity(intent);
    }


}
