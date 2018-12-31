package com.vuvucall.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aapbd.appbajarlib.storage.PersistData;
import com.google.gson.Gson;
import com.vuvucall.R;
import com.vuvucall.data.model.CommonData;
import com.vuvucall.data.model.UserResponse;
import com.vuvucall.data.model.ContactList;
import com.vuvucall.data.model.CountryModel;
import com.vuvucall.data.model.GPlace;
import com.vuvucall.data.model.MainFeature;
import com.vuvucall.data.model.QRCode;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by Mahmudul Hasan Swapon on 1/22/2018.
 * Company: AAPBD
 * Email: swaponsust@gmail.com
 */

public class AllMethods {




    public static void comingSoon(final Context context)
    {
        Toast.makeText(context,"Coming soon...",Toast.LENGTH_LONG).show();
    }


    public static String getFormattedDate(Context context, long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "h:mm aa, E";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE) ) {
            return "Today " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1  ){
            return "Yesterday " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return DateFormat.format("h:mm aa, E", smsTime).toString();
        }
    }

    public static String getFormattedAMPM(Context context, long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "h:mm aa, E";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE) ) {
            return ""+DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1  ){
            return "Yesterday " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return DateFormat.format("h:mm aa, E", smsTime).toString();
        }
    }


    // Convert a view to bitmap
    public static Bitmap createDrawableFromView(Context context, Bitmap bitmap, GPlace gPlace) {


        View customMarkerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.placeIcon);
        TextView distanceText = (TextView) customMarkerView.findViewById(R.id.distanceText);
        Location loc1 = new Location("");
        loc1.setLatitude(gPlace.getGeometry().getLocation().getLat());
        loc1.setLongitude(gPlace.getGeometry().getLocation().getLng());
        Location loc2 = new Location("");
        loc2.setLatitude(AppConstant.my_lat);
        loc2.setLongitude(AppConstant.my_lng);
        distanceText.setText(AllMethods.getFormattedDistance(loc1, loc2));
        markerImageView.setImageBitmap(bitmap);

        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        //  customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        // canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);

        return  returnedBitmap;
    }

    // Convert a view to bitmap
    public static Bitmap createDrawableFromMyView(Context context, Bitmap bitmap) {


        View customMarkerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_my_marker, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.placeIcon);
        markerImageView.setImageBitmap(getCircularBitmap(bitmap));

        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        //  customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        // canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);

        return  returnedBitmap;
    }

    public static Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        float r = 0;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2;
        } else {
            r = bitmap.getWidth() / 2;
        }

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }


    public static String getFormattedDistance( Location l1,Location l2)
    {
        String distance;


        float distanceInMeters = l1.distanceTo(l2);


        if(distanceInMeters<1000)
        {
            int temDist=(int)distanceInMeters;
            distance=temDist+" M";

        }else
        {
            distanceInMeters=distanceInMeters/1000;
            String newValue=String.format("%.1f", distanceInMeters);
            distance=newValue+" KM";
        }

        return distance;
    }

    public static String getCountryId(final Context context)
    {
        String countryId="17";
        Gson commonGSON = new Gson();
        CommonData commonData = commonGSON.fromJson(PersistData.getStringData(context, AppConstant.LOCAL_COMMON_DATA), CommonData.class);

        String countryCode="+"+AppStaticMethods.getCountryDialCode(context);
        for (CountryModel tempCountry:commonData.getCountry_list())
        {
            if(tempCountry.getCountry_code().equalsIgnoreCase(countryCode))
            {
                countryId=tempCountry.getId();
                break;

            }
        }


        return countryId;
    }


    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public static String getUserInfo(Context context, String name) {

        Gson gson=new Gson();
        UserResponse userResponse=gson.fromJson(PersistData.getStringData(context, AppConstant.USER_RESPONSE),UserResponse.class);
        return userResponse.getUserinfo().getProfile_picture();
    }

    public static void hideKeyBoard(Context con, EditText view)
    {
        InputMethodManager imm = (InputMethodManager)con.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static int getSmallerDimension(final Context context)
    {
        WindowManager manager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
       int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;
        return smallerDimension;
    }

    public static boolean isPackageExisted(Context context, String packageName) {
        PackageInfo packageInfo;

        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if(packageInfo == null){
            return false;
        }else{
            return true;
        }
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(5);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }


    public static int getAge(String dobString){

        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.parse(dobString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date == null) return 0;

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(date);

        int year = dob.get(Calendar.YEAR);
        int month = dob.get(Calendar.MONTH);
        int day = dob.get(Calendar.DAY_OF_MONTH);

        dob.set(year, month+1, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }



        return age;
    }


    public static Bitmap getQRCode(final Context context,final String user_id, final String name, final String profile_url)
    {
        Bitmap bitmap=null;
       // QRCodeEncoder qrCodeEncoder;

        QRCode qrCode=new QRCode();
        qrCode.setId(user_id);
        qrCode.setN(name);
        qrCode.setU(profile_url);
        Gson gson=new Gson();

//        qrCodeEncoder = new QRCodeEncoder(gson.toJson(qrCode), null,
//                Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(),
//                AllMethods.getSmallerDimension(context));
//        try {
//            bitmap = qrCodeEncoder.encodeAsBitmap();
//        } catch (WriterException e) {
//            bitmap=null;
//        }
//

        return  bitmap;
    }

    public static String getLastSeenTime(final long seconds) {

           // final long seconds = difference;
            final long minutes = seconds / 60;
            final long hours = minutes / 60;
            final long days = hours / 24;
            final long weeks = days / 7;
            final long months = days / 31;
            final long years = days / 365;

            if (seconds < 60) {
                return "active now";
            } else if (seconds < (60*60)) // 45 * 60
            {
                return minutes + " min ago";
            } else if (seconds < (24*60*60)) // 24  60  60
            {
                return hours + "h ago";

            } else if (seconds < 172800) // 48  60  60
            {
                return "yesterday";
            } else if (seconds < 604800) // 7  24  60 * 60
            {
                return days + " days ago";
            } else if (seconds < 2592000) // 30  24  60 * 60
            {
                return weeks <= 1 ? "1 week ago" : weeks + " weeks ago";
            } else if (seconds < 31104000) // 12  30  24  60  60
            {

                return months <= 1 ? "1 month ago" : months + " months ago";
            } else {

                return years <= 1 ? "one year ago" : years + " years ago";
            }


    }


    public static String getDisplayableTime(final Context con,String delta) {
        long difference = 0;
        long deltaa1;
        Long mDate = System.currentTimeMillis();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {


            Date date = dateFormat.parse(delta);

            deltaa1 = date.getTime();

        } catch (ParseException e) {
           return "";
        }


        if (mDate > deltaa1) {
            difference = mDate - deltaa1;
            final long seconds = difference / 1000;
            final long minutes = seconds / 60;
            final long hours = minutes / 60;
            final long days = hours / 24;
            final long weeks = days / 7;
            final long months = days / 31;
            final long years = days / 365;

            if (seconds < 0) {
                return "not yet";
            }  else if (seconds < 120) {
                return con.getResources().getString(R.string.just_now);
            } else if (seconds < (60*60)) // 45 * 60
            {
                return minutes + " min ago";
            } else if (seconds < (24*60*60)) // 24  60  60
            {
                return hours + "h ago";

            } else if (seconds < 172800) // 48  60  60
            {
                return "yesterday";
            } else if (seconds < 604800) // 7  24  60 * 60
            {
                return days + " days ago";
            } else if (seconds < 2592000) // 30  24  60 * 60
            {
                return weeks <= 1 ? "1 week ago" : weeks + " weeks ago";
            } else if (seconds < 31104000) // 12  30  24  60  60
            {

                return months <= 1 ? "1 month ago" : months + " months ago";
            } else {

                return years <= 1 ? "one year ago" : years + " years ago";
            }
        }
        return null;
    }



    public static String getDisplayableTimeWithoutAgo(final Context con,String delta) {
        long difference = 0;
        long deltaa1;
        Long mDate = System.currentTimeMillis();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {


            Date date = dateFormat.parse(delta);

            deltaa1 = date.getTime();

        } catch (ParseException e) {
            return "";
        }


        if (mDate > deltaa1) {
            difference = mDate - deltaa1;
            final long seconds = difference / 1000;
            final long minutes = seconds / 60;
            final long hours = minutes / 60;
            final long days = hours / 24;
            final long weeks = days / 7;
            final long months = days / 31;
            final long years = days / 365;

            if (seconds < 0) {
                return "not yet";
            }  else if (seconds < 120) {
                return con.getResources().getString(R.string.just_now);
            } else if (seconds < (60*60)) // 45 * 60
            {
                return minutes + " min";
            } else if (seconds < (24*60*60)) // 24  60  60
            {
                return hours + "h";

            } else if (seconds < 172800) // 48  60  60
            {
                return "yesterday";
            } else if (seconds < 604800) // 7  24  60 * 60
            {
                return days + " days";
            } else if (seconds < 2592000) // 30  24  60 * 60
            {
                return weeks <= 1 ? "1 week" : weeks + " weeks";
            } else if (seconds < 31104000) // 12  30  24  60  60
            {

                return months <= 1 ? "1 month" : months + " months";
            } else {

                return years <= 1 ? "one year" : years + " years";
            }
        }
        return null;
    }


    public static String getDisplayableTimeFromLongTIme(final Context con,long delta) {
        long difference = 0;
        //long deltaa1;
        Long mDate = System.currentTimeMillis();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (mDate > delta) {
            difference = mDate - delta;
            final long seconds = difference / 1000;
            final long minutes = seconds / 60;
            final long hours = minutes / 60;
            final long days = hours / 24;
            final long weeks = days / 7;
            final long months = days / 31;
            final long years = days / 365;

            if (seconds < 0) {
                return "not yet";
            }  else if (seconds < 120) {
                return con.getResources().getString(R.string.just_now);
            } else if (seconds < (60*60)) // 45 * 60
            {
                return minutes + " min ago";
            } else if (seconds < (24*60*60)) // 24  60  60
            {
                return hours + "h ago";

            } else if (seconds < 172800) // 48  60  60
            {
                return "yesterday";
            } else if (seconds < 604800) // 7  24  60 * 60
            {
                return days + " days ago";
            } else if (seconds < 2592000) // 30  24  60 * 60
            {
                return weeks <= 1 ? "1 week ago" : weeks + " weeks ago";
            } else if (seconds < 31104000) // 12  30  24  60  60
            {

                return months <= 1 ? "1 month ago" : months + " months ago";
            } else {

                return years <= 1 ? "1 year ago" : years + " years ago";
            }
        }
        return null;
    }


    // Method for converting pixels value to dps
    public static int getDPsFromPixels(Context context, int pixels){
        /*
            public abstract Resources getResources ()
                Return a Resources instance for your application's package.
        */
        Resources r = context.getResources();
        int  dps = Math.round(pixels/(r.getDisplayMetrics().densityDpi/160f));
        return dps;
    }

    // Method for converting DP/DIP value to pixels
    public static int getPixelsFromDPs(Context activity, int dps){
        /*
            public abstract Resources getResources ()

                Return a Resources instance for your application's package.
        */
        Resources r = activity.getResources();

        /*
            TypedValue

                Container for a dynamically typed data value. Primarily
                used with Resources for holding resource values.
        */

        /*
            applyDimension(int unit, float value, DisplayMetrics metrics)

                Converts an unpacked complex data value holding
                a dimension to its final floating point value.
        */

        /*
            Density-independent pixel (dp)

                A virtual pixel unit that you should use when defining UI layout,
                to express layout dimensions or position in a density-independent way.

                The density-independent pixel is equivalent to one physical pixel on
                a 160 dpi screen, which is the baseline density assumed by the system
                for a "medium" density screen. At runtime, the system transparently handles
                any scaling of the dp units, as necessary, based on the actual density
                of the screen in use. The conversion of dp units to screen pixels
                is simple: px = dp * (dpi / 160). For example, on a 240 dpi screen,
                1 dp equals 1.5 physical pixels. You should always use dp
                units when defining your application's UI, to ensure proper
                display of your UI on screens with different densities.
        */

        /*
            public static final int COMPLEX_UNIT_DIP

                TYPE_DIMENSION complex unit: Value is Device Independent Pixels.
                Constant Value: 1 (0x00000001)
        */
        int  px = (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps, r.getDisplayMetrics()));
        return px;
    }

    // big number format to show small [need to test]

    public static String getSmallFormatNumber(final String number) {
        String formatedString;
        if (number == "" || number == null)
        {
            formatedString= "0";
        }else
        {
            if(number.length()<=3)
            {
                formatedString=number;
            }else {

                int intValue=Integer.parseInt(number);
                if(intValue<100000)
                {
                    formatedString = String.format("%.02f", (intValue/1000))+"K";
                }else if(intValue>=100000 && intValue<100000000)
                {
                    formatedString = String.format("%.02f", (intValue/1000000))+"M";
                }else {

                    formatedString = String.format("%.02f", (intValue/1000000000))+"B";
                }

            }
        }

            return formatedString;
    }

    // return Bangla day by day number
    public static  String getBanglaDay(String day) {

        String symbol = null;
        switch (day.toLowerCase()) {
            case "saturday":
                symbol = "শনিবার";
                break;
            case "sunday":
                symbol = "রবিবার";
//				symbol="₽";
                break;
            case "monday":
                symbol = "সোমবার";
                break;
            case "tuesday":
                symbol = "মঙ্গলবার";
                break;
            case "wednesday":
                symbol = "বুধবার";
                break;
            case "thursday":
                symbol = "বৃহস্পতিবার";
                break;
            case "friday":
                symbol = "শুক্রবার";
                break;
        }
        return symbol;
    }

    public void uploadFile(ArrayList<String> imgPaths) {

        String charset = "UTF-8";
        //File uploadFile1 = new File("e:/Test/PIC1.JPG");
        //File uploadFile2 = new File("e:/Test/PIC2.JPG");

        File sourceFile[] = new File[imgPaths.size()];
        for (int i=0;i<imgPaths.size();i++){
            sourceFile[i] = new File(imgPaths.get(i));
            // Toast.makeText(getApplicationContext(),imgPaths.get(i),Toast.LENGTH_SHORT).show();
        }

        String requestURL = "your API";

        try {
            FileUploader multipart = new FileUploader(requestURL, charset);

            multipart.addHeaderField("User-Agent", "CodeJava");
            multipart.addHeaderField("Test-Header", "Header-Value");

            multipart.addFormField("description", "Cool Pictures");
            multipart.addFormField("keywords", "Java,upload,Spring");

            for (int i=0;i<imgPaths.size();i++){
                multipart.addFilePart("uploaded_file[]", sourceFile[i]);
            }

            /*multipart.addFilePart("fileUpload", uploadFile1);
            multipart.addFilePart("fileUpload", uploadFile2);*/

            List<String> response = multipart.finish();

            System.out.println("SERVER REPLIED:");

            for (String line : response) {
                System.out.println(line);
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }


    public static List<MainFeature> getMainFeatureList(final Context context){

        List<MainFeature> allItems = new ArrayList<>();
        allItems.add(new MainFeature(context.getString(R.string.vuvu_cash), R.drawable.home_vuvucash,false));
        allItems.add(new MainFeature(context.getString(R.string.trade_care), R.drawable.home_tradecare,false));
        allItems.add(new MainFeature(context.getString(R.string.my_home), R.drawable.home_my_home,false));
        allItems.add(new MainFeature(context.getString(R.string.medicare), R.drawable.home_healthcare,false));
        allItems.add(new MainFeature(context.getString(R.string.topup), R.drawable.home_topup,false));
        allItems.add(new MainFeature(context.getString(R.string.big_mall), R.drawable.home_bigmall,false));
        allItems.add(new MainFeature(context.getString(R.string.vuvu_car), R.drawable.home_vuvucar,false));
        allItems.add(new MainFeature(context.getString(R.string.more), R.drawable.home_more,false));


        return allItems;
    }

    public static List<MainFeature> getAllMore(){

        List<MainFeature> allItems = new ArrayList<>();
        allItems.add(new MainFeature("Stock", R.drawable.more_stock,false));
        allItems.add(new MainFeature("Discount", R.drawable.more_discount,false));
        allItems.add(new MainFeature("Event", R.drawable.more_event,false));
        allItems.add(new MainFeature("Hot Sale", R.drawable.more_hot_sale,false));
        allItems.add(new MainFeature("Top View ", R.drawable.more_top_view,false));
        allItems.add(new MainFeature("vLive", R.drawable.more_vlive,false));
        allItems.add(new MainFeature("Utility", R.drawable.home_more,true));
        allItems.add(new MainFeature("Electric Bill", R.drawable.more_electricity_bill,false));
        allItems.add(new MainFeature("Gas", R.drawable.more_gas,false));
        allItems.add(new MainFeature("Water", R.drawable.more_water,false));
        allItems.add(new MainFeature("Internet Bill", R.drawable.more_internet,false));

        allItems.add(new MainFeature("Delivery Service", R.drawable.home_more,true));
        allItems.add(new MainFeature("Courier", R.drawable.more_currier,false));
        allItems.add(new MainFeature("Food Delivery", R.drawable.more_food_delivery,false));
        allItems.add(new MainFeature("Group Buy", R.drawable.more_group_by,false));

        allItems.add(new MainFeature("Ticket", R.drawable.home_more,true));
        allItems.add(new MainFeature("Bus", R.drawable.more_bus,false));
        allItems.add(new MainFeature("Train", R.drawable.more_train,false));
        allItems.add(new MainFeature("Metro Rail", R.drawable.more_metro,false));
        allItems.add(new MainFeature("Movie", R.drawable.more_movie,false));
        allItems.add(new MainFeature("Entertainment", R.drawable.home_more,true));
        allItems.add(new MainFeature("Music", R.drawable.more_music,false));
        allItems.add(new MainFeature("Video", R.drawable.more_video,false));
        allItems.add(new MainFeature("Games", R.drawable.more_games,false));
        allItems.add(new MainFeature("Life Style", R.drawable.more_life_style,false));
        allItems.add(new MainFeature("Women's Corner", R.drawable.more_womens_corner,false));
        allItems.add(new MainFeature("FitMe", R.drawable.more_fitme,false));
        allItems.add(new MainFeature("Social", R.drawable.home_more,true));
        allItems.add(new MainFeature("News", R.drawable.more_news,false));
        allItems.add(new MainFeature("Jobs", R.drawable.more_jobs,false));
        allItems.add(new MainFeature("Voice Republic", R.drawable.more_voice_republic,false));
        allItems.add(new MainFeature("Team", R.drawable.more_team,false));


        return allItems;
    }

    // return Bangla month by month number

    public static String getBanglaMonth(String englishMonth) {

        String symbol = null;
        switch (englishMonth.toLowerCase()) {
            case "0":
                symbol = "জানুয়ারি ";
                break;
            case "1":
                symbol = "ফেব্রূয়ারি ";
                break;
            case "2":
                symbol = "মার্চ ";
                break;
            case "3":
                symbol = "এপ্রিল ";
                break;
            case "4":
                symbol = "মে ";
                break;
            case "5":
                symbol = "জুন ";
                break;
            case "6":
                symbol = "জুলাই ";
                break;
            case "7":
                symbol = "আগস্ট ";
                break;
            case "8":
                symbol = "সেপ্টেম্বর";
                break;
            case "9":
                symbol = "অক্টোবর ";
                break;
            case "10":
                symbol = "নভেম্বর";
                break;
            case "11":
                symbol = "ডিসেম্বর";
                break;
        }
        return symbol;
    }

    // get all contact (need customization)
    public static void getContact(Context context)
    {
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);
        String id = null, name = null, email = null, phone = null, note = null, orgName =     null, title = null;
        String Phone1 = "unknown", Phone2 = "unknown", Phone3 = "unknown", type1 = "unknown",        type2 = "unknown", type3 = "unknown";
        int size = cur.getCount();
        Log.e("size :: ",">>"+size);
        if (cur.getCount() > 0) {
            int cnt = 1;
            while (cur.moveToNext()) {
                email = "";
                name = "";
                cnt++;
                id = cur.getString(cur
                        .getColumnIndex(ContactsContract.Contacts._ID));
                name = cur
                        .getString(cur
                                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                if (name != null && name != "") {
//                    if (!checkEmail(name)) {
//                        email = "";
//
//                    } else {
//                        email = name;
//                        name = "";
//                    }

                    Log.e("name :: ",">>"+name);
                }
                if (Integer
                        .parseInt(cur.getString(cur
                                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)

                {
                    System.out.println("name : " + name);
                    Cursor pCur = cr
                            .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                            + " = ?", new String[]{id},
                                    null);

                    Phone1 = " ";
                    Phone2 = " ";
                    Phone3 = " ";
                    while (pCur.moveToNext()) {
                        String phonetype = pCur
                                .getString(pCur
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        String MainNumber = pCur
                                .getString(pCur
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        if (phonetype.equalsIgnoreCase("1")) {
                            Phone1 = MainNumber;
                            type1 = "home";
                        } else if (phonetype.equalsIgnoreCase("2")) {
                            Phone2 = MainNumber;
                            type2 = "mobile";
                        } else {
                            Phone3 = MainNumber;
                            type3 = "work";
                        }
                    }
                    pCur.close();

                }
                Cursor addrCur = cr
                        .query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID
                                        + " = ?", new String[]{id},
                                null);
                if (addrCur.getCount() == 0) {
                    //  addbuffer.append("unknown");
                } else {
                    int cntr = 0;
                    while (addrCur.moveToNext()) {

                        cntr++;
                        String poBox = addrCur
                                .getString(addrCur
                                        .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
                        if (poBox == null) {
                            poBox = " ";
                        }
                        String street = addrCur
                                .getString(addrCur
                                        .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                        if (street == null) {
                            street = " ";
                        }
                        String neb = addrCur
                                .getString(addrCur
                                        .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.NEIGHBORHOOD));
                        if (neb == null) {
                            neb = " ";
                        }
                        String city = addrCur
                                .getString(addrCur
                                        .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                        if (city == null) {
                            city = " ";
                        }
                        String state = addrCur
                                .getString(addrCur
                                        .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
                        if (state == null) {
                            state = " ";
                        }
                        String postalCode = addrCur
                                .getString(addrCur
                                        .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
                        if (postalCode == null) {
                            postalCode = " ";
                        }
                        String country = addrCur
                                .getString(addrCur
                                        .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
                        if (country == null) {
                            country = " ";
                        }

                        String type = addrCur
                                .getString(addrCur
                                        .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));
                        if (type == null) {
                            type = " ";
                        }
                    }

                }

                addrCur.close();

                String noteWhere = ContactsContract.Data.CONTACT_ID
                        + " = ? AND " + ContactsContract.Data.MIMETYPE
                        + " = ?";
                String[] noteWhereParams = new String[]{
                        id,
                        ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};
                Cursor noteCur = cr.query(
                        ContactsContract.Data.CONTENT_URI, null,
                        noteWhere, noteWhereParams, null);

                note = " ";

                if (noteCur.moveToFirst()) {
                    note = noteCur
                            .getString(noteCur
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));

                    if (note == null) {
                        note = " ";
                    }
                }
                noteCur.close();
                String orgWhere = ContactsContract.Data.CONTACT_ID
                        + " = ? AND " + ContactsContract.Data.MIMETYPE
                        + " = ?";
                String[] orgWhereParams = new String[]{
                        id,
                        ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
                Cursor orgCur = cr.query(
                        ContactsContract.Data.CONTENT_URI, null,
                        orgWhere, orgWhereParams, null);
                orgName = " ";
                if (orgCur.moveToFirst()) {
                    orgName = orgCur
                            .getString(orgCur
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY));

                }
                if (orgName == null) {
                    orgName = " ";
                }
                orgCur.close();

                Cursor emailCur = cr
                        .query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Email.CONTACT_ID
                                        + " = ?", new String[]{id},
                                null);
                email = "unknown";
                while (emailCur.moveToNext()) {

                    email = emailCur
                            .getString(emailCur
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    String emailType = emailCur
                            .getString(emailCur
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

                    if (email == null) {
                        email = "unknown";
                    }
                    if (emailType.equalsIgnoreCase("1")) {
                    } else {
                    }
                }

                // add
                emailCur.close();
            }
        }
    }

    public static boolean isEligablePhone(final String phoneNumber)
    {


        if((phoneNumber.startsWith("*"))|| (phoneNumber==null) ||(phoneNumber==""))
        {
            return false;
        }else if(phoneNumber.length()<10)
        {
            return false;
        }else
        {
            return true;
        }

    }

    public static String getFormatedPhoneNumber(final String phoneNumber, final Context con)
    {

        String tempPhone=phoneNumber.replaceAll(" ","");
        tempPhone = tempPhone.replaceAll("-", "");


        String formattedPhoneNumber;
        String countryCode="+"+AppStaticMethods.getCountryDialCode(con);
        if(tempPhone.startsWith(countryCode))
        {
            tempPhone = tempPhone.replaceAll("\\+", "");
        }else if(tempPhone.startsWith(AppStaticMethods.getCountryDialCode(con)))
        {
            tempPhone=tempPhone;
        }else {

            if(tempPhone.startsWith("+") && (!(tempPhone.startsWith(countryCode))))
            {

                tempPhone=tempPhone.replaceAll("\\+", "");
            }else {
                if ((AppStaticMethods.getCountryDialCode(con).endsWith("0")) && (tempPhone.startsWith("0"))) {
                    tempPhone = AppStaticMethods.getCountryDialCode(con).substring(0, AppStaticMethods.getCountryDialCode(con).length()-1) + tempPhone;
                } else {
                    tempPhone = AppStaticMethods.getCountryDialCode(con) + tempPhone;
                }
            }



        }

        formattedPhoneNumber=tempPhone.replaceAll("\\+", "");



        return formattedPhoneNumber;
    }

    public static List<ContactList> getLocalContactList(Context context,boolean isRecent) {

        List<ContactList> contactLists=new ArrayList<>();


        ContentResolver cr = context.getContentResolver();

        String[] projection = new String[] { ContactsContract.Contacts._ID }; // you can add more fields you need here
        int oneDay = (1000 * 60 * 60 * 24);
        long last24h = (System.currentTimeMillis() - oneDay);

        Cursor cur;

        if(isRecent)
        {
            cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, ContactsContract.Contacts.LAST_TIME_CONTACTED + ">" + last24h, null, null);

        }else {
            cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                    null, null, null);
        }



        String id = null, name = null, phone = null;
        String Phone1 = "unknown", Phone2 = "unknown", Phone3 = "unknown", type1 = "unknown", type2 = "unknown", type3 = "unknown";
        int size = cur.getCount();
        Log.e("size :: ", ">>" + size);
        if (cur.getCount() > 0) {
            int cnt = 1;
            while (cur.moveToNext()) {

                ContactList contactList = new ContactList();

                name = "";
                cnt++;
                id = cur.getString(cur
                        .getColumnIndex(ContactsContract.Contacts._ID));
                name = cur
                        .getString(cur
                                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                contactList.setUser_fullname(name);


                if (Integer
                        .parseInt(cur.getString(cur
                                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)

                {
                    //System.out.println("name : " + name);
                    //Log.e("name :: ", "Name with phone >>" + name);
                    Cursor pCur = cr
                            .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                            + " = ?", new String[]{id},
                                    null);

                    Phone1 = " ";
                    Phone2 = " ";
                    Phone3 = " ";
                    while (pCur.moveToNext()) {


                        try {
                            String phonetype = pCur
                                    .getString(pCur
                                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                            String MainNumber = pCur
                                    .getString(pCur
                                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            if (phonetype.equalsIgnoreCase("1")) {
                                Phone1 = MainNumber;
                                type1 = "home";
                            } else if (phonetype.equalsIgnoreCase("2")) {
                                Phone2 = MainNumber;
                                type2 = "mobile";
                            } else {
                                Phone3 = MainNumber;
                                type3 = "work";
                            }
                        }catch (Exception e)
                        {

                        }
                    }


                    String validPhone="";
                    if(!(TextUtils.isEmpty(Phone2)))
                    {
                        validPhone=Phone2;
                    }else
                    {
                        if(!(TextUtils.isEmpty(Phone1)))
                        {
                            validPhone=Phone1;
                        }else
                        {
                            validPhone=Phone3;
                        }


                    }

                    if(AllMethods.isEligablePhone(validPhone))
                    {
                        contactList.setPhone(AllMethods.getFormatedPhoneNumber(validPhone,context));
                        contactList.setIsFavorite("NO");
                        contactList.setVuvu_call_user("NO");
                        contactList.setSynced_vuvuCall("NO");
                        contactLists.add(contactList);
                    }


                  //  Log.e("Phone :: ", Phone1 + " : " + Phone2 + " : " + Phone3);


                    pCur.close();

                }


            }
        }
        Log.e("contactLists size :: ", contactLists.size() +">>");


        return contactLists;
    }
}
