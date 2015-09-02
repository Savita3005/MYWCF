package wcfd.savita.mywcf;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    TextView output;
    Button b;
    ProgressBar pb;
    List<MyTask> tasks;

    List<Flower> flowerList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output=(TextView)findViewById(R.id.textView);
        output.setMovementMethod(new ScrollingMovementMethod());

        b = (Button) findViewById(R.id.button1);
        pb=(ProgressBar)findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);

        tasks=new ArrayList<>();

        b.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {
                if(isOnline())
                {
                //    requestData("http://192.168.1.42/PersonJSON/PersonJSONService.svc/GetPeople");
                //    requestData("http://services.hanselandpetal.com/feeds/flowers.json");
                  //  requestData("http://10.0.2.2:8080/PersonJSON/PersonJSONService.svc/GetPeople");
                  requestData("https://api.myjson.com/bins/4uvvi");

                }
                else
                {
                    Toast t=Toast.makeText(MainActivity.this, "Network nt available",Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });
    }

    private void requestData(String uri) {
        MyTask task=new MyTask();
        //   task.execute("Param1","Param2","Param3");//Single thread
     //   task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"Param1","Param2","Param3");
        task.execute(uri);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

protected void updateDisplay()
{
    if(flowerList!=null) {
        for (Flower flower:flowerList)
        {
          //  output.append(flower.getName()+ "\n");
            output.append(flower.getFirstName()+"\n");
        }
    }

}

    protected boolean isOnline()
    {
        ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo=cm.getActiveNetworkInfo();
        if(netinfo!=null && netinfo.isConnectedOrConnecting())
        {
            return true;
        }
        else
        {
            return  false;
        }
    }
    private class MyTask extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPreExecute() {
         //  updateDisplay("Starting task");

            if(tasks.size()==0)
            {
                pb.setVisibility(View.VISIBLE);
            }
tasks.add(this);
        }

        @Override
        protected String doInBackground(String... params) {
         /*   for (int i=0;i<params.length;i++)
            {
                publishProgress("Working with "+params[i]);
                try
                {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

            return "Task complete";*/

            //////////////////
            try {
                String content = HttpManager.getData(params[0]);
                return content;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {

            flowerList=FlowerJSONParser.parseFeed(result);


            updateDisplay();

            tasks.remove(this);
            if(tasks.size()==0)
            {
                pb.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        protected void onProgressUpdate(String... values) {
         //   updateDisplay(values[0]);
        }
    }
}
