package com.example.manager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private IntentIntegrator qrScan;

    private final String BASE_URL = "https://emoclew.pythonanywhere.com";
    private MyAPI mMyAPI;

    private String division,name,major,phoneNum,studentNum,email, nowTime;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMyAPI(BASE_URL);

        qrScan = new IntentIntegrator(this);
        Button buttonScan_In = (Button) findViewById(R.id.qrscanner);
        buttonScan_In.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan.initiateScan();
                qrScan.setOrientationLocked(false);
                qrScan.setPrompt("스캐너에 QR코드를 위치시켜주세요");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {

            if (result.getContents() == null) {
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();

            } else {
                //qrcode 결과가 있으면
                try {
                    String resultInformation = URLDecoder.decode(result.getContents(),"UTF-8");
                    JSONObject obj = new JSONObject(resultInformation);

                    division = obj.getString("구분");
                    name = obj.getString("이름");
                    major = obj.getString("학과");
                    phoneNum = obj.getString("핸드폰번호");
                    studentNum = obj.getString("학번");
                    email = obj.getString("이메일");

                    if(division.equals("입장"))
                    {
                        inputSurveys();
                    }
                    else if (division.equals("퇴장"))
                    {
                       outputSurvey();
                    }
                } catch (UnsupportedEncodingException | JSONException e) {
                    Toast.makeText(MainActivity.this, "정보전달 실패", Toast.LENGTH_SHORT).show();
                    Log.d("TAG","에러메시지: "  );
                    e.printStackTrace();
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initMyAPI(String baseUrl){

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(loggingInterceptor);

        Log.d("TAG","initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder.build())
                .build();

        mMyAPI = retrofit.create(MyAPI.class);
    }

    private void inputSurveys(){
        //시간구하기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        nowTime = sdfNow.format(date);

        PostLiveData post = new PostLiveData(phoneNum,name,major,studentNum,nowTime);

        Call<PostLiveData> getCall = mMyAPI.post_posts(post);

        getCall.enqueue(new Callback<PostLiveData>() {
            @Override
            public void onResponse(Call<PostLiveData> call, Response<PostLiveData> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "입장에 실패했습니다. 에러메시지 : " + response.message(), Toast.LENGTH_LONG).show();
                }
                Toast.makeText(MainActivity.this, "입장에 성공했습니다. ", Toast.LENGTH_LONG).show();
                inputCovid();
            }

            @Override
            public void onFailure(Call<PostLiveData> call, Throwable t) {
                Toast.makeText(MainActivity.this, "서버연결에 실패했습니다. :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void outputSurvey(){

        Call<PostLiveData> deleteInformation = mMyAPI.delete_posts(phoneNum);
        deleteInformation.enqueue(new Callback<PostLiveData>() {
            @Override
            public void onResponse(Call<PostLiveData> call, Response<PostLiveData> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "퇴장에 실패했습니다. 에러메시지: " + response.message(), Toast.LENGTH_LONG).show();
                }
                Toast.makeText(MainActivity.this, "성공적으로 삭제했습니다. ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<PostLiveData> call, Throwable t) {
                Toast.makeText(MainActivity.this, "서버연결에 실패했습니다. :" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void inputCovid(){

        PostLiveData post = new PostLiveData(phoneNum,name,major,studentNum,nowTime);

        Call<PostLiveData> getCall = mMyAPI.post_covid(post);

        getCall.enqueue(new Callback<PostLiveData>() {
            @Override
            public void onResponse(Call<PostLiveData> call, Response<PostLiveData> response) {
                if(!response.isSuccessful()){
                    Log.d("TAG","covid : " + response.code());
                  }
                Log.d("TAG","covidsuccess " + response.body());
           }

            @Override
            public void onFailure(Call<PostLiveData> call, Throwable t) {
                Log.d("TAG","covid : " + t.getMessage());

            }
        });
    }
}