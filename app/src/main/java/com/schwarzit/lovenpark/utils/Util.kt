package com.schwarzit.lovenpark.utils

import com.google.android.gms.maps.model.LatLng

class Util {
    companion object {

        const val URL_POI_PROD = "https://lovenpark-backend.apps.01.cf.eu01.stackit.cloud/api/v1/poi"

        const val URL_POI_DEBUG = "https://lovenpark-backend-dev.apps.01.cf.eu01.stackit.cloud/api/v1/poi"

        const val SCHEMA_VERSION = 2L

        const val SCHEMA_MAIN_NAME = "main.data.base"

        const val SCHEMA_FAVOURITE_NAME = "favourite.data.base"

        const val SCHEMA_FAVOURITE_VERSION = 1L

        const val EMAIL_PATTERN = "[a-zA-Z]{1}[a-zA-Z0-9+._%\\-!#$&'*=?^`{|]{1,63}" +
                "@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,253}" +
                "(\\.[a-zA-Z][a-zA-Z\\-]{1,25})+"

        const val NAME_PATTERN = "^[a-zA-ZА-Яа-я.'\\-\\s]{2,60}+$"

        const val PASSWORD_MINIMUM_LENGTH: Int = 8

        val LOVEN_PARK_LOCATION = LatLng(42.664993, 23.333792)

        const val LOVEN_PARK_SW_LAT = 42.6562

        const val LOVEN_PARK_SW_LON = 23.3257

        const val LOVEN_PARK_NE_LAT = 42.6755

        const val LOVEN_PARK_NE_LON = 23.3441

        const val ZOOM_PREFERENCE = 14.85F

        const val EMPTY_STRING = ""

        const val ZERO_DOUBLE = 0.0

        const val CHOOSE_CATEGORY_STR = "Choose category"

        const val SIGNAL_TEXT_MIN_L = 3

        const val SIGNAL_TEXT_MAX_L = 255

        const val CONNECTION_AVAILABLE = "connected"

        const val CONNECTION_LOST = "connection lost"

        const val SELECTED_UPLOAD_OPTION = "selectedUploadOption"

        const val UPLOAD_OPTION_REQUEST_KEY = "uploadOptionRequestKey"

        const val SIGNAL_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss"

        const val SIGNAL_DATE_PATTERN = "dd.MM.yyyy"

        const val TIME_INTERVAL_PIN = 24

        const val OPEN_GALLERY_INPUT = "image/*"

        const val SPLASH_SCREEN_DELAY = 2000L

        const val ASTERISK_PASSWORD = '*'

        const val DEFAULT_TAB_POSITION = 0

        const val PASSWORD_DIALOG_TAG = "password dialog"

        const val NETWORK_DIALOG_TAG = "network dialog"

        const val ABOUT_IMAGE_DIALOG_TAG = "about detail image dialog"

        const val RATE_APP_MARKET_STRING = "market://details?id="

        const val RATE_APP_PACKAGE_STRING = "http://play.google.com/store/apps/details?id="

        const val SIGNAL_DESC_TEXT_MIN_L = 3

        const val SIGNAL_DESC_TEXT_MAX_L = 500

        const val SIGNAL_DESCRIPTION = "description"

        const val SIGNAL_DATE = "date"

        const val SIGNAL_TIME = "time"

        const val SIGNAL_YEAR = "year"

        const val REQUEST_DESCRIPTION = "requestDescription"

        const val REQUEST_DATE_AND_TIME = "requestDateAndTime"

        const val COLOR_PREFIX = "#%06x"

        const val ZOOM_PREFERENCE_CLUSTER = 18.0f

        const val BEARER_TOKEN = "Bearer "

        const val SERVER_CLIENT_ID =
            "1081915378073-hglr0hghqlfe9jr601vnufpkg89log07.apps.googleusercontent.com"

        const val ENCRYPT_STANDARD_SYMBOLS = "\u0020"

        const val SIGNAL_NUMBER_RANGE1_START = 2

        const val SIGNAL_NUMBER_RANGE1_END = 8

        const val SIGNAL_NUMBER_RANGE2_START = 14

        const val USER_FIRST_NAME_DELIMITER = " "

        const val ANDROID_WEB_KIT = "android.webkit."

        val PARK_BOUNDS = listOf(
            LatLng(42.65693891853256, 23.328039208999378),
            LatLng(42.65896414749519, 23.3278626788998),
            LatLng(42.659197823515626, 23.327262476561216),
            LatLng(42.6611206457469, 23.32800844338529),
            LatLng(42.66158325470392, 23.32595274919171),
            LatLng(42.66195572767861, 23.32508141210821),
            LatLng(42.66247735737935, 23.324499254878212),
            LatLng(42.662924745734884, 23.324256432677238),
            LatLng(42.66328677015993, 23.324012490778294),
            LatLng(42.665114011645926, 23.32980491067283),
            LatLng(42.66789083137478, 23.327618172001195),
            LatLng(42.66958089104671, 23.332509875071132),
            LatLng(42.67246016600302, 23.336125516125495),
            LatLng(42.67098983682555, 23.337483824805293),
            LatLng(42.67052540537641, 23.337765020458068),
            LatLng(42.66657918567513, 23.337412215430938),
            LatLng(42.66665972796245, 23.341100286900968),
            LatLng(42.66639784084568, 23.341812603851704),
            LatLng(42.66476413916192, 23.34316939804359),
            LatLng(42.664321199576456, 23.342702640726756),
            LatLng(42.66387965946266, 23.342887888285865),
            LatLng(42.66343811621294, 23.342795264506307),
            LatLng(42.66332762291043, 23.342823346724334),
            LatLng(42.66208285300805, 23.342196229749028),
            LatLng(42.6613071772373, 23.342081102629557),
            LatLng(42.66115615959526, 23.341779282897523),
            LatLng(42.658889621036714, 23.339904240234027),
            LatLng(42.658638930124944, 23.33981859182931),
            LatLng(42.6586818321393, 23.339691857456128),
            LatLng(42.65864139575877, 23.339618096709827),
            LatLng(42.65856298855569, 23.339663694262086),
            LatLng(42.65849810207505, 23.33946600622135),
            LatLng(42.65814792843927, 23.339120007948274),
            LatLng(42.65776020388703, 23.338983297309674),
            LatLng(42.65769673742452, 23.338908132590422),
            LatLng(42.65760665588263, 23.33886359053457),
            LatLng(42.657562638717785, 23.338553188066427),
            LatLng(42.65754216560738, 23.337972749401096),
            LatLng(42.65766495661525, 23.336534422513928),
            LatLng(42.659141897443355, 23.336731238998766),
            LatLng(42.65922555145656, 23.334835347971275),
            LatLng(42.657708607802945, 23.33435758337686),
            LatLng(42.65659876166203, 23.333796399632725),
            LatLng(42.6561860500044, 23.33382673388917),
            LatLng(42.655862571385015, 23.333447555683666),
            LatLng(42.655678522453975, 23.33296979114474),
            LatLng(42.655711985936485, 23.332120431964423),
            LatLng(42.65607450584296, 23.330952563091486),
            LatLng(42.656855310771384, 23.32996669975719),
            LatLng(42.65659318449572, 23.329337263936065),
            LatLng(42.656721459194216, 23.329215926910305),
            LatLng(42.6567604992673, 23.328055641601484),
            LatLng(42.6667704,23.3384108),
            LatLng(42.6668126,23.3404548),
            LatLng(42.6666548,23.3412138),
            LatLng(42.6666548,23.3412138),
            LatLng(42.6666548,23.3412138),
            LatLng(42.6666548,23.3412138),
            LatLng(42.6668362,23.3413969),
            LatLng(42.6669191,23.3416443),
            LatLng(42.6669083,23.3417449),
            LatLng(42.6669083,23.3417449),
            LatLng(42.6670315,23.3425844),
            LatLng(42.6670315,23.3425844),
            LatLng(42.6670315,23.3425844),
            LatLng(42.6673003,23.3424538),
            LatLng(42.6673003,23.3424538),
            LatLng(42.6678139,23.3427652),
            LatLng(42.6682604,23.3429387),
            LatLng(42.669021,23.3431856),
            LatLng(42.669613,23.3433547),
            LatLng(42.6703376,23.3436416),
            LatLng(42.6707628,23.3438084),
            LatLng(42.6711831,23.3440153),
            LatLng(42.6715658,23.3442466),
            LatLng(42.6720009,23.3445247),
            LatLng(42.672606,23.345281),
            LatLng(42.672606,23.345281),
            LatLng(42.6735006,23.3460751),
            LatLng(42.6749892,23.3467825),
            LatLng(42.6760546,23.3443475),
            LatLng(42.6763258,23.3439045),
            LatLng(42.6763258,23.3439045),
            LatLng(42.6763258,23.3439045),
            LatLng(42.6763258,23.3439045),
            LatLng(42.6763258,23.3439045),
            LatLng(42.676329,23.3431529),
            LatLng(42.676329,23.3431529),
            LatLng(42.676329,23.3431529),
            LatLng(42.6758279,23.3425149),
            LatLng(42.6758279,23.3425149),
            LatLng(42.6758279,23.3425149),
            LatLng(42.6758279,23.3425149),
            LatLng(42.6749347,23.3408151),
            LatLng(42.6749347,23.3408151),
            LatLng(42.6746648,23.3407233),
            LatLng(42.6746648,23.3407233),
            LatLng(42.6746648,23.3407233),
            LatLng(42.6735468,23.3396264),
            LatLng(42.6735468,23.3396264),
            LatLng(42.6735468,23.3396264),
            LatLng(42.6726143,23.3399639),
            LatLng(42.6716305,23.3388317),
            LatLng(42.6706515,23.3389549),
            LatLng(42.6696833,23.3390886),
            LatLng(42.6696833,23.3390886),
            LatLng(42.6676157,23.3387569),
            LatLng(42.6676157,23.3387569),
            LatLng(42.6676157,23.3387569),

        )

    }
}