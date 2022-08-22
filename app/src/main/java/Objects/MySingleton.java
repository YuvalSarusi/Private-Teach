package Objects;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
/*this class need to handle some request queue and make sure that the network requests will
be limit, and go through exactly one request queue
it make sure that the request will be added&will be done at the order they created*/
public class MySingleton {

    private static MySingleton instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private MySingleton(Context context){
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized MySingleton getInstance(Context context){
        if (instance == null){
            instance = new MySingleton(context);
        }
        return instance;
    }

    private RequestQueue getRequestQueue() {
        if (this.requestQueue == null){
            //getApplicationContext() is key, it keep you from leaking
            //the Activity or BroadcastReceiver if something pass one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }

    public static MySingleton getInstance() {
        return instance;
    }

    public static void setInstance(MySingleton instance) {
        MySingleton.instance = instance;
    }

    public void setRequestQueue(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public static Context getCtx() {
        return ctx;
    }

    public static void setCtx(Context ctx) {
        MySingleton.ctx = ctx;
    }
}
