package com.denada.travpholer.Doc;


import com.denada.travpholer.R;

/**
 * Created by hgc on 4/16/2016.
 */
public class Constants {
    public static final String defaultCountryID = "ZZ1";
//    public static String url = "http://192.168.1.108/adminuser/";
        public static String url = "http://139.162.42.92/adminuser/";

    public static final String BOTTOM_TAB_EXPLORE = "tab_a_identifier";
    public static final String BOTTOM_TAB_MINE = "tab_b_identifier";
    public static final String BOTTOM_TAB_UPLOAD = "tab_d_identifier";
    public static final String BOTTOM_TAB_PROFILE = "tab_e_identifier";

    public static final String  APISERVICE_IP_URL = "http://ip-api.com/";
    public static final String  APISERVICE_MAP_URL = "http://maps.googleapis.com/maps/";


    public static final String  ACTION_LOGIN = "assets/rest/apns/login.php";
    public static final String  ACTION_HOT = "assets/rest/apns/loadhot.php";
    public static final String ACTION_FRESH = "assets/rest/apns/loadfresh.php";
    public static final String ACTION_TOGO = "assets/rest/apns/loadtogo.php";
    public static final String ACTION_CONQUERED = "assets/rest/apns/loadconquered1.php";
    public static final String ACTION_TOGO_COUNTRY = "assets/rest/apns/loadtogo_country.php";
    public static final String ACTION_CONQUERED_COUNTRY = "assets/rest/apns/loadconquered_country.php";

    public static final String  ACTION_UPLOAD = "assets/rest/apns/fileuploadmm.php";
    public static final String ACTION_LIKEPIC = "assets/rest/apns/actionlike.php";
    public static final String ACTION_REPORT = "assets/rest/apns/actionreport.php";
    public static final String ACTION_MAKEPOST = "assets/rest/apns/makepost.php";
    public static final String ACTION_COMMENT = "assets/rest/apns/actioncomment.php";
    public static final String  ACTION_USERINFO = "assets/rest/apns/userinfo.php";
    public static final String  ACTION_UPDATEPROFILE = "assets/rest/apns/updateprofile.php";
    public static final String  ACTION_LOADNOTI = "assets/rest/apns/load_noti.php";
    public static final String  ACTION_TOGO_IDS = "assets/rest/apns/loadtogo_ids.php";

    public static final String  ACTION_TOGO_DATA = "assets/rest/apns/loadtogo_data.php";
    public static final String  ACTION_CONQUERED_DATA = "assets/rest/apns/loadconquered_data.php";

    public static final String  ACTION_DEFAULTPROFILE = "assets/uploads/user1.png";
    public static final String ACTION_LOADCOMMENT = "assets/rest/apns/load_comment.php";


    public static final String  KEY_HASLOGIN = "hasLogin";
    public static final String  KEY_INTROVIEWED = "introViewed";
    public static String[] listGender = {"Male","Female"};
    public static String[] listEthnicity = {"White","Hispanic or Latino","Black or African","Native","Asian/Pacific Islander","Mixed race"};
    public static String[] listLookingfor = {"Man","Woman", "Man & Woman"};
    public static  String[] mPlanetTitles =
            {
                    "Browse",
                    "Search",
                    "Profile",
                    "My Chat",
                    "Create Bid",
                    "Bid Feeds",
                    "Buy Coins",
                    "Invite friends",
                    "Help",
                    "Sign Out",

            };
    public static  int[] mMenuIconResource =
            {
                    R.mipmap.ico_menu_browse,
                    R.drawable.ico_menu_search,
                    R.drawable.ico_menu_profile,
                    R.drawable.ico_menu_chat,
                    R.drawable.ico_menu_createbid,
                    R.drawable.ico_menu_bidfeeds,
                    R.drawable.ico_menu_buycoin,
                    R.drawable.ico_menu_invitefriend,
                    R.drawable.ico_menu_help,
                    R.drawable.ico_menu_signout,

            };
    public static final int TAB_BROWSE      = 0 ;
    public static final int TAB_SEARCH      = 1 ;
    public static final int TAB_PROFILE       = 2 ;
    public static final int TAB_MYCHAT          = 3 ;
    public static final int TAB_CREATEBID           = 4 ;
    public static final int TAB_BIDFEEDS           = 5 ;
    public static final int TAB_BUYCOINS           = 6 ;
    public static final int TAB_INVITEFRIENDS           = 7 ;
    public static final int TAB_HELP           = 8 ;
    public static final int TAB_SIGNOUT           = 9 ;

    public static final int REQUESTCODE_UPDATEPROFILE = 10;

    //broadcast
    public static final String  BROADCAST_MAP_PICKLOCATION = "com.denada.travpholer.Doc.BROADCAST_MAP_PICKLOCATION";

    public static final String  BROADCAST_CONTENTCHANGED_HOT = "com.denada.travpholer.Doc.BROADCAST_CONTENTCHANGED_HOT";
    public static final String  BROADCAST_CONTENTCHANGED_FRESH = "com.denada.travpholer.Doc.BROADCAST_CONTENTCHANGED_FRESH";
    public static final String  BROADCAST_CONTENTCHANGED_OTHERS = "com.denada.travpholer.Doc.BROADCAST_CONTENTCHANGED_OTHERS";

    public static final String  BROADCAST_PHOTOCHANGED = "com.denada.travpholer.Doc.BROADCAST_CONTENTCHANGED_OTHERS";

    public static final int REQUEST_CAMERA     =   10;
    public static final int SELECT_FILE         =   11;

    final public static String google_browserkey = "AIzaSyCmvC_H5S08MvkO-ixoQTpJQGXdu5qyVWg";

    public static final float MAP_ZOOM         =   4;
}
