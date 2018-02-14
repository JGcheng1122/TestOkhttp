package tw.okhttp.hwcheng.com.testokhttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Call;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private Button sendBtnInBg;
    private Button sendBtn;
    private ExecutorService service;
    private OkHttpClient client;
    String[] l = { "http://rate.bot.com.tw/xrt/quote/ltm/", "http://rate.bot.com.tw/xrt/quote/l6m/" };
    String m = "http://rate.bot.com.tw/xrt/quote/ltm/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initObject();
        initView();
    }

    private void initObject() {
        client = new OkHttpClient();
        service = Executors.newSingleThreadExecutor();
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.context);
        sendBtnInBg = (Button) findViewById(R.id.testHttp);
        sendBtnInBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRequestInBackground();
            }
        });
        sendBtn = (Button) findViewById(R.id.send_request);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRequest();
            }
        });

    }

    private void handleRequest(){
        Request request = new Request.Builder().url("http://www.google.com.tw").build();
        client.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, final IOException e)
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //
                    }
                });
            }
            public void onResponse(Call call, Response response) throws IOException
            {
                final String resStr = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(resStr);
                    }
                });
            }
        });
    }

    private void handleRequestInBackground(){
        service.submit(new Runnable() {
            public void run() {
                //set query parameter
                HttpUrl.Builder builder = HttpUrl.parse("https://www.google.com.tw/search?").newBuilder();
                builder.addQueryParameter("q","wheator");
                builder.addQueryParameter("oq","test");

                //put in task to run
                Request request = new Request.Builder()
                        .url(builder.toString())
                        .build();
                try {
                    final Response response = client.newCall(request).execute();
                    final String resStr = response.body().string();
                    if (response.isSuccessful()) {
                        Log.e("hwcheng","okHttp is request success");
                    } else {
                        Log.e("hwcheng", "okHttp is request error");
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(resStr);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
