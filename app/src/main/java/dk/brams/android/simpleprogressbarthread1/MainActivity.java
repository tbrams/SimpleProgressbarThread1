package dk.brams.android.simpleprogressbarthread1;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final int DISMISS = 2;
    public static final int UPDATE = 1;
    private ProgressDialog dialog;
    private int increment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startbtn = (Button) findViewById(R.id.startbtn);
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) findViewById(R.id.increment);
                increment = Integer.parseInt(et.getText().toString());

                dialog = new ProgressDialog(MainActivity.this);
                dialog.setTitle("Downloading something...");
                dialog.setMessage("Please have patience!");
                dialog.setCancelable(true);
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog.setProgress(0);

                EditText max = (EditText) findViewById(R.id.maximum);
                int maximum = Integer.parseInt(max.getText().toString());
                dialog.setMax(maximum);

                dialog.show();

                // create a thread for updating the progress bar
                Thread background = new Thread(new Runnable() {
                    public void run() {
                        try {
                            //
                            // This example is just going to increment the progress bar:
                            // So keep running until the progress value reaches maximum value
                            while (dialog.getProgress() <= dialog.getMax()) {
                                Thread.sleep(100);
                                progressHandler.sendMessage(progressHandler.obtainMessage(UPDATE));
                                if (dialog.getProgress()>=dialog.getMax())
                                    progressHandler.sendMessage(progressHandler.obtainMessage(DISMISS));

                            }


                        } catch (java.lang.InterruptedException e) {
                            Log.e(TAG, "Exception triggered:", e);
                        }
                    }
                });

                background.start();
            }
        });

    }

    // handler for the background updating - has access to main thread
    private Handler progressHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE:
                    dialog.incrementProgressBy(increment);
                    break;
                case DISMISS:
                    dialog.dismiss();
            }
        }
    };
}
