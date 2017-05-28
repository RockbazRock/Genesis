package com.aisoft.akarshan.genesis;

import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StrictMode;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.aisoft.akarshan.genesis.brain.Bot;
import com.aisoft.akarshan.genesis.brain.Chat;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.os.StrictMode.setThreadPolicy;

public class MainActivity extends AppCompatActivity {

    public Context context;
    TextView ip;
    WebView op;
    String botname = "genesis";
    Chat chatSession;
    Bot bot;
    String baseDir, r, nohtmlstr;
    TextToSpeech ts;
    protected static final int RESULT_SPEECH = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ip=(TextView)findViewById(R.id.textView4);
        op=(WebView)findViewById(R.id.webview);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        setThreadPolicy(policy);
        final String googleTtsPackage = "com.google.android.tts";
        ts= new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if(status != TextToSpeech.ERROR) {

                    ts.setEngineByPackageName(googleTtsPackage);
                }
                else
                    Toast.makeText(getApplicationContext(),"Please install Google text to speech available in different languages from Google play store or else i cannot talk", Toast.LENGTH_LONG).show();

            }
        });
        String state = Environment.getExternalStorageState();

        if(Environment.MEDIA_MOUNTED.equals(state)) {
            File baseDirFile = getExternalFilesDir(null);
            if(baseDirFile == null) {
                baseDir = getFilesDir().getAbsolutePath();
            } else {
                baseDir = baseDirFile.getAbsolutePath();
            }
        } else {
            baseDir = getFilesDir().getAbsolutePath();
        }
        File fileExt = new File(baseDir+ "/bots");
        if (!fileExt.exists()) {
            ZipFileExtraction extract = new ZipFileExtraction();
            try {
                extract.unZipIt(getAssets().open("bots.zip"),baseDir + "/");
                Log.i("InitSMARTBOT", "Extracted bots folder");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        //path = baseDir;
        bot = new Bot(botname, baseDir);
        chatSession = new Chat(bot);
        op.getSettings().setJavaScriptEnabled(true);
        op.setBackgroundColor(Color.TRANSPARENT);
        op.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        op.loadData("<html><head>"
                + "<style type=\"text/css\">body{color: #fff; }"
                + "</style></head>"
                + "<body>"
                + "Hi! How can i help?"
                + "</body></html>","html/text", "utf-8");
    }

    public void onInputResponse(View view) {
        Intent intent = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

        try {
            startActivityForResult(intent, RESULT_SPEECH);
            op.loadData("","","");
        } catch (ActivityNotFoundException a) {
            Toast t = Toast.makeText(getApplicationContext(),
                    "Opps! Your device doesn't support Speech to Text",
                    Toast.LENGTH_SHORT);
            t.show();
        }


        ip.setText("");


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    ip.setText(text.get(0));
                    String i = ip.getText().toString();
                    if(i.toLowerCase().contains("wifi") && (i.toLowerCase().contains("on")|| i.toLowerCase().contains("enable"))) {
                        WifiManager wifiManager = (WifiManager)this.context.getSystemService(Context.WIFI_SERVICE);
                        wifiManager.setWifiEnabled(true);
                    r="<html><head>"
                            + "<style type=\"text/css\">body{color: #fff; background-color: #000;}"
                            + "</style></head>"
                            + "<body>"
                            + "Wifi Enabled!"
                            + "</body></html>";
                        ts.speak("Wifi enabled",TextToSpeech.QUEUE_FLUSH,null,null);
                    }
                    else if(i.toLowerCase().contains("wifi") && (i.toLowerCase().contains("off")|| i.toLowerCase().contains("disable"))){
                        WifiManager wifiManager = (WifiManager)this.context.getSystemService(Context.WIFI_SERVICE);
                        wifiManager.setWifiEnabled(false);
                        r="<html><head>"
                                + "<style type=\"text/css\">body{color: #fff; }"
                                + "</style></head>"
                                + "<body>"
                                + "Wifi disabled!"
                                + "</body></html>";
                        ts.speak("Wifi disabled",TextToSpeech.QUEUE_FLUSH,null,null);
                    }
                    else if(i.toLowerCase().contains("bluetooth") && (i.toLowerCase().contains("on")|| i.toLowerCase().contains("enable"))) {
                        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        if(!mBluetoothAdapter.isEnabled()) mBluetoothAdapter.enable();
                        r= "<html><head>"
                                + "<style type=\"text/css\">body{color: #fff; }"
                                + "</style></head>"
                                + "<body>"
                                + "Bluetooth enabled"
                                + "</body></html>";
                        ts.speak("Bluetooth enabled",TextToSpeech.QUEUE_FLUSH,null,null);
                    }
                    else if(i.toLowerCase().contains("bluetooth") && (i.toLowerCase().contains("off")|| i.toLowerCase().contains("disable"))) {
                        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        if(mBluetoothAdapter.isEnabled()) mBluetoothAdapter.disable();
                        r="<html><head>"
                                + "<style type=\"text/css\">body{color: #fff;}"
                                + "</style></head>"
                                + "<body>"
                                + "Bluetooth Disabled"
                                + "</body></html>";
                        ts.speak("Bluetooth disabled",TextToSpeech.QUEUE_FLUSH,null,null);
                    }
                    else

                     nohtmlstr = chatSession.multisentenceRespond(i);
                    r="<html><head>"
                            + "<style type=\"text/css\">body{color: #fff; }"
                            + "</style></head>"
                            + "<body>"
                            + nohtmlstr
                            + "</body></html>";





                    //Todo : find substring in ip in a string r

                    op.loadDataWithBaseURL(null, r, "text/html", "utf-8", null);
                    ts.speak(stripHtml(nohtmlstr), TextToSpeech.QUEUE_FLUSH, null, null);
                    if (!(pullLinks(stripHtml(r)).equals(""))) {

                        op.setWebViewClient(new WebViewClient());
                        op.setWebChromeClient(new WebChromeClient() {
                        });
                        op.loadUrl(pullLinks(r));
                        ip.setText("");
                    }
                }
                break;
            }
        }
    }
    public String stripHtml(String html) {
        return Html.fromHtml(html).toString();
    }
    private String pullLinks(String text) {
        String links="";

        String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        while(m.find()) {
            String urlStr = m.group();
            if (urlStr.startsWith("(") && urlStr.endsWith(")"))
            {
                urlStr = urlStr.substring(1, urlStr.length() - 1);
            }
            links=urlStr;
        }
        return links;
    }

    @Override
    protected void onPause() {
        super.onPause();
        ts.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ts.shutdown();
    }
}
