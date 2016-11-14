package jp.ac.kumamoto_u.abco.wifi_direct_file_transfer;

/**
 * Created by AbeKodai on 2016/11/14.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditActivity extends Activity {
    // 保存ファイル名
    String mFileName = "";
    // 保存なしフラグ
    boolean mNotSave = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setActionBar((Toolbar) findViewById(R.id.toolbar));

        // タイトルと内容入力用の EditText を取得
        EditText eTxtTitle = (EditText)findViewById(R.id.eTxtTitle);
        EditText eTxtContent = (EditText)findViewById(R.id.eTxtContent);

        // メイン画面からの情報受け取り、EditTextに設定
        // （情報がない場合（新規作成の場合）は、設定しない）
        Intent intent = getIntent();
        String name = intent.getStringExtra("NAME");
        if (name != null) {
            mFileName = name;
            eTxtTitle.setText(intent.getStringExtra("TITLE"));
            eTxtContent.setText(intent.getStringExtra("CONTENT"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        // [削除] で画面を閉じるときは、保存しない
        if (mNotSave) {
            return;
        }

        // タイトル、内容を取得
        EditText eTxtTitle = (EditText)findViewById(R.id.eTxtTitle);
        EditText eTxtContent = (EditText)findViewById(R.id.eTxtContent);
        String title = eTxtTitle.getText().toString();
        String content = eTxtContent.getText().toString();

        // タイトル、内容が空白の場合、保存しない
        if (title.isEmpty() && content.isEmpty()) {
            Toast.makeText(this, R.string.msg_destruction, Toast.LENGTH_SHORT).show();
            return;
        }

        // ファイル名を生成  ファイル名 ： yyyyMMdd_HHmmssSSS.txt
        // （既に保存されているファイルは、そのままのファイル名とする）
        if (mFileName.isEmpty()) {
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.JAPAN);
            mFileName = sdf.format(date) + ".txt";
        }

        // 保存
        OutputStream out = null;
        PrintWriter writer = null;
        try{
            out = this.openFileOutput(mFileName, Context.MODE_PRIVATE);
            writer = new PrintWriter(new OutputStreamWriter(out,"UTF-8"));
            // タイトル書き込み
            writer.println(title);
            // 内容書き込み
            writer.print(content);
            writer.close();
            out.close();
        }catch(Exception e){
            Toast.makeText(this, "File save error!", Toast.LENGTH_LONG).show();
        }
    }

    // メニュー選択時の処理
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_del:
                // [削除] 選択処理
                // ファイル削除
                if (!mFileName.isEmpty()) {
                    this.deleteFile(mFileName);
                }
                // 保存せずに、画面を閉じる
                mNotSave = true;
                this.finish();
                break;
            default:
                break;
        }

        return super.onMenuItemSelected(featureId, item);
    }

}
