package Objects;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import utils.Utils;

public class ServerConnection {

    private Context context;

    private JSONArray jsonArrayResponse;//handle returned JSONArray data. define it at the beginning of the methods with new
    private JSONObject jsonObjectResponse; //handle returned JSONObject data. define it at the beginning of the methods with new
    private String stringResponse; //handle returned String data. define it at the beginning of the methods with ""

    private final String SERVER_CONNECTION_TAG = "Server Connection";
    private final String SERVER_IP = "10.0.0.8";
    private final String SERVER_PORT = "9000";
    private final String HTTP_REQUEST_ADDRESS = "http://"+SERVER_IP+":"+SERVER_PORT;

    public interface JSONObjectResponseListener{
        void onError(String message);
        void onResponse(JSONObject response);
    }
    public interface JSONArrayResponseListener{
        void onError(String message);
        void onResponse(JSONArray response);
    }
    public interface StringResponseListener{
        void onError(String message);
        void onResponse(String response);
    }

    public ServerConnection(Context context) {
        this.context = context;
    }
    public Context getContext() {
        return context;
    }
    public void setContext(Context context) {
        this.context = context;
    }

    //teacher methods
    public JSONArray getAllTeachers(JSONArrayResponseListener jsonArrayResponseListener){
        jsonArrayResponse = new JSONArray();
        String url = HTTP_REQUEST_ADDRESS+"/get-all-teachers";
        JsonArrayRequest request  = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //need to do something
                        jsonArrayResponse = response;
                        jsonArrayResponseListener.onResponse(jsonArrayResponse);
                        Log.i(SERVER_CONNECTION_TAG, "Get All Teachers");
                        //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()  {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        jsonArrayResponseListener.onError("Error at connection");
                        Log.e(SERVER_CONNECTION_TAG,"Error at connection to server");
                        error.printStackTrace();
                    }
                }
        );
        MySingleton.getInstance(context).addToRequestQueue(request);
        return this.jsonArrayResponse;
    }
    public String checkTeacherExist(String username,String token, StringResponseListener stringResponseListener){
        stringResponse = "";
        String params = "?username="+username+"&token="+token;
        String url = HTTP_REQUEST_ADDRESS+"/check-teacher-exist"+params;
        StringRequest request  = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //need to do something
                        //activate responseListener onResponse;
                        stringResponse = response;
                        stringResponseListener.onResponse(response);
                        //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()  {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        stringResponseListener.onError("Error at connection");
                        Log.e(SERVER_CONNECTION_TAG,"Error at connection to server");
                        error.printStackTrace();
                    }
                }
        );
        MySingleton.getInstance(context).addToRequestQueue(request);
        return stringResponse;
    }
    public String createTeacher(Teacher teacher, StringResponseListener stringResponseListener){
        stringResponse = "";
        String url = HTTP_REQUEST_ADDRESS+"/create-new-teacher";
        StringRequest request  = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //need to do something
                        //activate responseListener onResponse;
                        stringResponse = response;
                        stringResponseListener.onResponse(response);
                    }
                },
                new Response.ErrorListener()  {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        stringResponseListener.onError("Error at connection");
                        Log.e(SERVER_CONNECTION_TAG,"Error at connection to server");
                        error.printStackTrace();
                    }
                }
        )
        {
            //Override getParams that pass on the params to the server
            @Override
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", teacher.getUsername());
                params.put("token", teacher.getToken());
                params.put("fullName", teacher.getFullName());
                params.put("phoneNumber", teacher.getPhoneNumber());
                params.put("email", teacher.getEmail());
                params.put("price", String.valueOf(teacher.getPrice())); //need to work with string - check at the server that that came is int/number
                params.put("subject",teacher.getSubject());
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
        return stringResponse;

    }
    public JSONObject getTeacherByToken(String token, JSONObjectResponseListener jsonObjectResponseListener){
        jsonObjectResponse = new JSONObject();
        String params = "?token="+token;
        String url = HTTP_REQUEST_ADDRESS+"/get-teacher-by-token"+params;
        JsonObjectRequest request  = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //need to do something
                        jsonObjectResponse = response;
                        jsonObjectResponseListener.onResponse(jsonObjectResponse);
                        Log.i(SERVER_CONNECTION_TAG, "Get Teacher By Token= "+token+". Got=" +response.toString());
                        //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()  {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        jsonObjectResponseListener.onError("Error at connection");
                        Log.e(SERVER_CONNECTION_TAG,"Error at connection to server");
                        error.printStackTrace();
                    }
                }
        );
        MySingleton.getInstance(context).addToRequestQueue(request);
        return this.jsonObjectResponse;
    }
    public JSONArray getFilterTeacher(String username, String subject, String price, JSONArrayResponseListener jsonArrayResponseListener){
        jsonArrayResponse = new JSONArray();
        String params = "?username="+username+"&subject="+subject+"&price="+price;
        String url = HTTP_REQUEST_ADDRESS+"/get-filter-teacher"+params;
        JsonArrayRequest request  = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //need to do something
                        jsonArrayResponse = response;
                        jsonArrayResponseListener.onResponse(jsonArrayResponse);
                        Log.i(SERVER_CONNECTION_TAG, "Get Filter Teachers");
                        //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()  {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        jsonArrayResponseListener.onError("Error at connection");
                        Log.e(SERVER_CONNECTION_TAG,"Error at connection to server");
                        error.printStackTrace();
                    }
                }
        );
        MySingleton.getInstance(context).addToRequestQueue(request);
        return this.jsonArrayResponse;
    }

    //student methods
    public String createStudent(Student student, StringResponseListener stringResponseListener){
        stringResponse = "";
        String url = HTTP_REQUEST_ADDRESS+"/create-new-student";
        StringRequest request  = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //need to do something
                        //activate responseListener onResponse;
                        stringResponse = response;
                        stringResponseListener.onResponse(response);
                    }
                },
                new Response.ErrorListener()  {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        stringResponseListener.onError("Error at connection");
                        Log.e(SERVER_CONNECTION_TAG,"Error at connection to server");
                        error.printStackTrace();
                    }
                }
        )
        {
            //Override getParams that pass on the params to the server
            @Override
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", student.getUsername());
                params.put("token", student.getToken());
                params.put("fullName", student.getFullName());
                params.put("phoneNumber", student.getPhoneNumber());
                params.put("email", student.getEmail());
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
        return stringResponse;
    }
    public String checkStudentExist(String username,String token, StringResponseListener stringResponseListener){
        stringResponse = "";
        String params = "?username="+username+"&token="+token;
        String url = HTTP_REQUEST_ADDRESS+"/check-student-exist"+params;
        StringRequest request  = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //need to do something
                        //activate responseListener onResponse;
                        stringResponse = response;
                        stringResponseListener.onResponse(response);
                        //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()  {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        stringResponseListener.onError("Error at connection");
                        Log.e(SERVER_CONNECTION_TAG,"Error at connection to server");
                        error.printStackTrace();
                    }
                }
        );
        MySingleton.getInstance(context).addToRequestQueue(request);
        return stringResponse;
    }
    public JSONObject getStudentByToken(String studentToken, JSONObjectResponseListener jsonObjectResponseListener){
        jsonObjectResponse = new JSONObject();
        String params = "?token="+studentToken;
        String url = HTTP_REQUEST_ADDRESS+"/get-student-by-token"+params;
        JsonObjectRequest request  = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //need to do something
                        jsonObjectResponse = response;
                        jsonObjectResponseListener.onResponse(jsonObjectResponse);
                        Log.i(SERVER_CONNECTION_TAG, "Get Student By Token= "+studentToken+". Got=" +response.toString());
                        //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()  {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        jsonObjectResponseListener.onError("Error at connection");
                        Log.e(SERVER_CONNECTION_TAG,"Error at connection to server");
                        error.printStackTrace();
                    }
                }
        );
        MySingleton.getInstance(context).addToRequestQueue(request);
        return this.jsonObjectResponse;
    }
    public String changeStudentDetails(String studentToken, String fullName, String phone, String email, StringResponseListener stringResponseListener){
        stringResponse = "";
        String url = HTTP_REQUEST_ADDRESS+"/change-student-details";
        StringRequest request  = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //need to do something
                        //activate responseListener onResponse;
                        stringResponse = response;
                        stringResponseListener.onResponse(response);
                    }
                },
                new Response.ErrorListener()  {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        stringResponseListener.onError("Error at connection");
                        Log.e(SERVER_CONNECTION_TAG,"Error at connection to server");
                        error.printStackTrace();
                    }
                }
        )
        {
            //Override getParams that pass on the params to the server
            @Override
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", studentToken);
                params.put("fullName", fullName);
                params.put("phone", phone);
                params.put("email", email);
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
        return stringResponse;
    }

    //lessons methods
    public String addLesson(Date startDate, Date endDate, String teacherToken, String studentToken, StringResponseListener stringResponseListener){
        stringResponse = "";
        String url = HTTP_REQUEST_ADDRESS+"/add-new-lesson";
        StringRequest request  = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //need to do something
                        //activate responseListener onResponse;
                        //response - the value that came from the server
                        stringResponse = response;
                        stringResponseListener.onResponse(response);
                    }
                },
                new Response.ErrorListener()  {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        stringResponseListener.onError("Error at connection");
                        Log.e(SERVER_CONNECTION_TAG,"Error at connection to server");
                        error.printStackTrace();
                    }
                }
        )
        {
            //Override getParams that pass on the params to the server
            @Override
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("startDate",new SimpleDateFormat("yyyy-MM-dd HH:mm").format(startDate));
                params.put("endDate",new SimpleDateFormat("yyyy-MM-dd HH:mm").format(endDate));
                params.put("teacherToken", teacherToken);
                params.put("studentToken",studentToken);
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
        return stringResponse;
    }
    public JSONArray getTeacherFutureLessons(String teacherToken, JSONArrayResponseListener jsonArrayResponseListener){
        jsonArrayResponse = new JSONArray();
        String params = "?teacherToken="+teacherToken;
        String url = HTTP_REQUEST_ADDRESS+"/get-teacher-future-lessons"+params;
        JsonArrayRequest request  = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //need to do something
                        jsonArrayResponse = response;
                        jsonArrayResponseListener.onResponse(jsonArrayResponse);
                        Log.i(SERVER_CONNECTION_TAG, "Got All Teacher Future Lessons, "+response);
                        //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()  {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        jsonArrayResponseListener.onError("Error at connection");
                        Log.e(SERVER_CONNECTION_TAG,"Error at connection to server");
                        error.printStackTrace();
                    }
                }
        );
        MySingleton.getInstance(context).addToRequestQueue(request);
        return this.jsonArrayResponse;
    }
    public JSONArray getTeacherPastLessons(String teacherToken, JSONArrayResponseListener jsonArrayResponseListener){
        jsonArrayResponse = new JSONArray();
        String params = "?teacherToken="+teacherToken;
        String url = HTTP_REQUEST_ADDRESS+"/get-teacher-past-lessons"+params;
        JsonArrayRequest request  = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //need to do something
                        jsonArrayResponse = response;
                        jsonArrayResponseListener.onResponse(jsonArrayResponse);
                        Log.i(SERVER_CONNECTION_TAG, "Got All Teacher Past Lessons, "+response);
                        //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()  {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        jsonArrayResponseListener.onError("Error at connection");
                        Log.e(SERVER_CONNECTION_TAG,"Error at connection to server");
                        error.printStackTrace();
                    }
                }
        );
        MySingleton.getInstance(context).addToRequestQueue(request);
        return this.jsonArrayResponse;
    }
    public String getHighestPrice(StringResponseListener stringResponseListener){
        stringResponse = "";
        String url = HTTP_REQUEST_ADDRESS+"/get-highest-price";
        StringRequest request  = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //need to do something
                        //activate responseListener onResponse;
                        stringResponse = response;
                        stringResponseListener.onResponse(response);
                        //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()  {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        stringResponseListener.onError("Error at connection");
                        Log.e(SERVER_CONNECTION_TAG,"Error at connection to server");
                        error.printStackTrace();
                    }
                }
        );
        MySingleton.getInstance(context).addToRequestQueue(request);
        return stringResponse;
    }
    public String getLowestPrice(StringResponseListener stringResponseListener){
        stringResponse = "";
        String url = HTTP_REQUEST_ADDRESS+"/get-lowest-price";
        StringRequest request  = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //need to do something
                        //activate responseListener onResponse;
                        stringResponse = response;
                        stringResponseListener.onResponse(response);
                        //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()  {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        stringResponseListener.onError("Error at connection");
                        Log.e(SERVER_CONNECTION_TAG,"Error at connection to server");
                        error.printStackTrace();
                    }
                }
        );
        MySingleton.getInstance(context).addToRequestQueue(request);
        return stringResponse;
    }
    public JSONArray getFilteredLesson(String subject, String maxPrice, JSONArrayResponseListener jsonArrayResponseListener){
        this.jsonArrayResponse = new JSONArray();
        String url;
        if (subject.equals("All") && maxPrice.equals("")){
            url = HTTP_REQUEST_ADDRESS+"/get-all-available-lessons";
        }
        else if (!subject.equals("All") && maxPrice.equals("")){
            String params = "?subject="+subject;
            url = HTTP_REQUEST_ADDRESS+"/get-subject-filtered-available-lessons"+params;
        }
        else if (subject.equals("All") && !maxPrice.equals("")){
            String params = "?price="+maxPrice;
            url = HTTP_REQUEST_ADDRESS+"/get-price-filtered-available-lessons"+params;
        }
        else{
            String params = "?subject="+subject+"&price="+maxPrice;
            url = HTTP_REQUEST_ADDRESS+"/get-filtered-available-lessons"+params;
        }
        JsonArrayRequest request  = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //need to do something
                        jsonArrayResponse = response;
                        jsonArrayResponseListener.onResponse(jsonArrayResponse);
                        Log.i(SERVER_CONNECTION_TAG, "Get Filtered Lessons");
                        //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()  {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        jsonArrayResponseListener.onError("Error at connection");
                        Log.e(SERVER_CONNECTION_TAG,"Error at connection to server");
                        error.printStackTrace();
                    }
                }
        );
        MySingleton.getInstance(context).addToRequestQueue(request);
        return this.jsonArrayResponse;


    }
    public JSONObject getLessonById(int id, JSONObjectResponseListener jsonObjectResponseListener){
        jsonObjectResponse = new JSONObject();
        String params = "?id="+id;
        String url = HTTP_REQUEST_ADDRESS+"/get-lesson-by-id"+params;
        JsonObjectRequest request  = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //need to do something
                        jsonObjectResponse = response;
                        jsonObjectResponseListener.onResponse(response);
                        Log.i(SERVER_CONNECTION_TAG, "Get Filtered Lessons");
                        //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()  {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        jsonObjectResponseListener.onError("Error at connection");
                        Log.e(SERVER_CONNECTION_TAG,"Error at connection to server");
                        error.printStackTrace();
                    }
                }
        );
        MySingleton.getInstance(context).addToRequestQueue(request);
        return this.jsonObjectResponse;

    }
    public String signIntoLesson(String studentToken, int lessonId, StringResponseListener stringResponseListener){
        stringResponse = "";
        String url = HTTP_REQUEST_ADDRESS+"/sign-into-lesson";
        StringRequest request  = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //need to do something
                        //activate responseListener onResponse;
                        stringResponse = response;
                        stringResponseListener.onResponse(response);
                        //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()  {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        stringResponseListener.onError("Error at connection");
                        Log.e(SERVER_CONNECTION_TAG,"Error at connection to server");
                        error.printStackTrace();
                    }
                }
        )
        {
            //Override getParams that pass on the params to the server
            @Override
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("studentToken", studentToken);
                params.put("lessonId", String.valueOf(lessonId));//need to work with string - check at the server that that came is int/number
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
        return stringResponse;
    }
    public JSONArray getStudentFutureLessons(String studentToken, JSONArrayResponseListener jsonArrayResponseListener){
        jsonArrayResponse = new JSONArray();
        String params = "?studentToken="+studentToken;
        String url = HTTP_REQUEST_ADDRESS+"/get-student-signed-future-lessons"+params;
        JsonArrayRequest request  = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //need to do something
                        jsonArrayResponse = response;
                        jsonArrayResponseListener.onResponse(jsonArrayResponse);
                        Log.i(SERVER_CONNECTION_TAG, "Got All Student Signed Future Lessons, "+response);
                        //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()  {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        jsonArrayResponseListener.onError("Error at connection");
                        Log.e(SERVER_CONNECTION_TAG,"Error at connection to server");
                        error.printStackTrace();
                    }
                }
        );
        MySingleton.getInstance(context).addToRequestQueue(request);
        return this.jsonArrayResponse;
    }
    public JSONArray getStudentPastLessons(String studentToken, JSONArrayResponseListener jsonArrayResponseListener){
        jsonArrayResponse = new JSONArray();
        String params = "?studentToken="+studentToken;
        String url = HTTP_REQUEST_ADDRESS+"/get-student-signed-past-lessons"+params;
        JsonArrayRequest request  = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //need to do something
                        jsonArrayResponse = response;
                        jsonArrayResponseListener.onResponse(jsonArrayResponse);
                        Log.i(SERVER_CONNECTION_TAG, "Got All Student Signed Past Lessons, "+response);
                        //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()  {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        jsonArrayResponseListener.onError("Error at connection");
                        Log.e(SERVER_CONNECTION_TAG,"Error at connection to server");
                        error.printStackTrace();
                    }
                }
        );
        MySingleton.getInstance(context).addToRequestQueue(request);
        return this.jsonArrayResponse;
    }
}
