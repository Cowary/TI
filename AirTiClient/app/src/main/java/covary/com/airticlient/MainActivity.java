package covary.com.airticlient;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

private ClientAsk cl;

TextView[] textViews;
TextView[] errorViews;
Button button;

private String ipAdress;
private boolean startStop = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final EditText ipInput = (EditText)findViewById(R.id.ipInput);

        textViews = new TextView[3];
        errorViews = new TextView[3];
        textViews[0] = findViewById(R.id.valueTemp);
        textViews[1] = findViewById(R.id.valueSpeed);
        textViews[2] = findViewById(R.id.valueRec);
        errorViews[0] = findViewById(R.id.error1);
        errorViews[1] = findViewById(R.id.error2);
        errorViews[2] = findViewById(R.id.error3);
        button = findViewById(R.id.mainButton);

        //Получение IP-адреса
        //Обработка поля ввода текста
        ipInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if(event.getAction() ==  KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    ipAdress = ipInput.getText().toString();
                    System.out.println("ipAdress = " + ipAdress);
                    return true;
                }
                return false;
            }
        });
    }
    //Запуск и остановка сетевой части приложения
    //Обработка нажатия кнопки
    public void onButtonClick(View view){
        if(startStop) {
            cl = new ClientAsk(textViews, errorViews, ipAdress);
            cl.execute();
            startStop = false;
            button.setText("Стоп");
        }
        else{
            button.setText("Старт");
            cl.cancel(true);
            cl = null;
            startStop = true;
        }

    }






}
