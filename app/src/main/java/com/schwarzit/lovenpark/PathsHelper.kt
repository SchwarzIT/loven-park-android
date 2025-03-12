package com.schwarzit.lovenpark

import android.content.Context
import android.graphics.Color
import androidx.core.graphics.ColorUtils.setAlphaComponent
import androidx.core.graphics.alpha
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions

object PathsHelper {

    private const val width = 3F
    private const val isVisible = true
    fun addPaths(gMap: GoogleMap?, color: Int = R.color.paths_color, context: Context): GoogleMap? {
        val aColor = setAlphaComponent(context.getColor(color), 230)
        gMap?.apply {
            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.672132, 23.336430),
                        LatLng(42.671804, 23.335792),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.670279, 23.335357),
                        LatLng(42.669403, 23.332890),
                        LatLng(42.669368, 23.332509),
                        LatLng(42.669340, 23.332310),
                        LatLng(42.669387, 23.332182),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.669404, 23.332793),
                        LatLng(42.668123, 23.335805),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.667315, 23.337500),
                        LatLng(42.668613, 23.335992),
                        LatLng(42.669401, 23.335386),
                        LatLng(42.670761, 23.334150),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.666481, 23.330283),
                        LatLng(42.666686, 23.330819),
                        LatLng(42.666888, 23.332579),
                        LatLng(42.667729, 23.332862),
                        LatLng(42.666943, 23.333078),
                        LatLng(42.667073, 23.333378),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.667906, 23.333232),
                        LatLng(42.667729, 23.332862),
                        LatLng(42.667441, 23.332755),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.665769, 23.336547),
                        LatLng(42.666057, 23.336150),
                        LatLng(42.666479, 23.335732),
                        LatLng(42.666569, 23.335496),
                        LatLng(42.666577, 23.335308),
                        LatLng(42.666514, 23.334927),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.666419, 23.337406),
                        LatLng(42.666403, 23.336810),
                        LatLng(42.666057, 23.336150),
                        LatLng(42.666289, 23.335893),
                        LatLng(42.666620, 23.335328),
                        LatLng(42.666577, 23.335308),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.666788, 23.329800),
                        LatLng(42.666481, 23.330283),
                        LatLng(42.664706, 23.331329),
                        LatLng(42.665014, 23.331136),
                        LatLng(42.664691, 23.331329),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.665014, 23.331136),
                        LatLng(42.664904, 23.330814),
                        LatLng(42.664643, 23.330943),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.664311, 23.329136),
                        LatLng(42.663841, 23.329233),
                        LatLng(42.663372, 23.329466),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.663912, 23.325489),
                        LatLng(42.663392, 23.325816),
                    ).visible(isVisible).width(width).color(color)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.663419, 23.328643),
                        LatLng(42.662871, 23.328683),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.663442, 23.331611),
                        LatLng(42.663343, 23.331439),
                        LatLng(42.663307, 23.331187),
                        LatLng(42.663430, 23.330914),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.662921, 23.329068),
                        LatLng(42.662759, 23.328162),
                        LatLng(42.662463, 23.327958),
                        LatLng(42.661264, 23.328038),
                        LatLng(42.661153, 23.328006),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.662215, 23.330940),
                        LatLng(42.662254, 23.330447),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.662989, 23.332776),
                        LatLng(42.663322, 23.332862),
                        LatLng(42.663312, 23.333136),
                        LatLng(42.662977, 23.333356),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.663022, 23.331661),
                        LatLng(42.662989, 23.332776),
                        LatLng(42.663322, 23.332862),
                        LatLng(42.662882, 23.335225),
                        LatLng(42.662681, 23.335689),
                        LatLng(42.662963, 23.336520),
                        LatLng(42.663393, 23.337068),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.661872, 23.330499),
                        LatLng(42.661706, 23.334029),
                        LatLng(42.661746, 23.335483),
                        LatLng(42.661896, 23.335944),
                        LatLng(42.661686, 23.336255),
                        LatLng(42.661193, 23.336797),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.661896, 23.335944),
                        LatLng(42.662681, 23.335689),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.663777, 23.337617),
                        LatLng(42.665063, 23.337778),
                        LatLng(42.666199, 23.338390),
                        LatLng(42.666633, 23.339548),
                        LatLng(42.665821, 23.341126),
                        LatLng(42.665182, 23.341598),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.666199, 23.338390),
                        LatLng(42.666578, 23.338304),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.665821, 23.341126),
                        LatLng(42.665513, 23.342445),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.663619, 23.337252),
                        LatLng(42.663738, 23.337875),
                        LatLng(42.663959, 23.338229),
                        LatLng(42.664306, 23.338368),
                        LatLng(42.664471, 23.338626),
                        LatLng(42.664574, 23.339849),
                        LatLng(42.664618, 23.339950),
                        LatLng(42.664910, 23.341275),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.663738, 23.337875),
                        LatLng(42.663209, 23.337928),
                        LatLng(42.662933, 23.337682),
                        LatLng(42.662562, 23.337778),
                        LatLng(42.662326, 23.337564),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.663959, 23.338229),
                        LatLng(42.663564, 23.338422),
                        LatLng(42.662933, 23.338508),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.664314, 23.340294),
                        LatLng(42.663533, 23.339779),
                        LatLng(42.662807, 23.339900),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.662898, 23.337655),
                        LatLng(42.662326, 23.337564),
                        LatLng(42.661225, 23.337848),
                        LatLng(42.660404, 23.337945),
                        LatLng(42.659243, 23.338966),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.660718, 23.340125),
                        LatLng(42.660687, 23.339889),
                        LatLng(42.660404, 23.337945),
                        LatLng(42.660053, 23.337135),
                        LatLng(42.659694, 23.336781),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.659694, 23.336781),
                        LatLng(42.659536, 23.336958),
                        LatLng(42.658968, 23.337789),
                        LatLng(42.658944, 23.339098),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.659243, 23.338992),
                        LatLng(42.659536, 23.336958),
                        LatLng(42.659528, 23.336749),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.660536, 23.339995),
                        LatLng(42.660687, 23.339889),
                        LatLng(42.661562, 23.339653),
                        LatLng(42.662233, 23.340318),
                        LatLng(42.662201, 23.341155),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.670661, 23.337863),
                        LatLng(42.672223, 23.338335),
                        LatLng(42.672710, 23.338322),
                        LatLng(42.673160, 23.338440),
                        LatLng(42.674066, 23.338043),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.669547, 23.343858),
                        LatLng(42.669435, 23.342841),
                        LatLng(42.669016, 23.341801),
                        LatLng(42.668598, 23.340256),
                        LatLng(42.668424, 23.339902),
                        LatLng(42.667592, 23.338346),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.668424, 23.339902),
                        LatLng(42.670661, 23.337863),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.673102, 23.340266),
                        LatLng(42.673126, 23.339478),
                        LatLng(42.672700, 23.338346),
                        LatLng(42.672631, 23.337538),
                        LatLng(42.672534, 23.336865),
                        LatLng(42.672503, 23.336238),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.673160, 23.338440),
                        LatLng(42.673192, 23.337507),
                        LatLng(42.672939, 23.337346),
                        LatLng(42.672963, 23.336734),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.673073, 23.340242),
                        LatLng(42.673041, 23.339647),
                        LatLng(42.672876, 23.339153),
                        LatLng(42.672710, 23.338322),
                        LatLng(42.672777, 23.337098),
                        LatLng(42.672489, 23.336251),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.672223, 23.338335),
                        LatLng(42.672777, 23.337098),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.671839, 23.340802),
                        LatLng(42.672689, 23.339748),
                        LatLng(42.673041, 23.339647),
                        LatLng(42.673396, 23.339122),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.675230, 23.343030),
                        LatLng(42.675796, 23.342397),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.672535, 23.344657),
                        LatLng(42.672724, 23.345311),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.673051, 23.346706),
                        LatLng(42.673373, 23.344619),
                        LatLng(42.673263, 23.343780),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.671196, 23.341457),
                        LatLng(42.670616, 23.342332),
                        LatLng(42.670778, 23.342139),
                        LatLng(42.669496, 23.343866),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.670951, 23.342825),
                        LatLng(42.669823, 23.343946),
                        LatLng(42.671338, 23.342358),
                        LatLng(42.671586, 23.342095),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.670951, 23.342825),
                        LatLng(42.669823, 23.343946),
                        LatLng(42.671586, 23.342095),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.670951, 23.342825),
                        LatLng(42.669823, 23.343946),
                        LatLng(42.671338, 23.342358),
                        LatLng(42.671586, 23.342095),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.671709, 23.342107),
                        LatLng(42.670778, 23.342139),
                        LatLng(42.671338, 23.342358),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.671873, 23.344993),
                        LatLng(42.672764, 23.344548),
                        LatLng(42.673128, 23.342777),
                        LatLng(42.674177, 23.341715),
                        LatLng(42.674571, 23.341195),
                        LatLng(42.674958, 23.340723),
                        LatLng(42.675392, 23.340197),
                        LatLng(42.675597, 23.339848),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.674560, 23.342166),
                        LatLng(42.674828, 23.341828),
                        LatLng(42.675100, 23.341442),
                        LatLng(42.675601, 23.340691),
                        LatLng(42.675889, 23.340235),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.674571, 23.341195),
                        LatLng(42.674828, 23.341828),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.675100, 23.341442),
                        LatLng(42.674958, 23.340723),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.674770, 23.342452),
                        LatLng(42.675145, 23.341921),
                        LatLng(42.675100, 23.341442),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.675912, 23.342472),
                        LatLng(42.676504, 23.340932),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.671274, 23.339505),
                        LatLng(42.671532, 23.340031),
                        LatLng(42.671840, 23.340820),
                        LatLng(42.672041, 23.341147),
                        LatLng(42.672408, 23.341796),
                        LatLng(42.672984, 23.343405),
                        LatLng(42.673100, 23.343716),
                        LatLng(42.673526, 23.344349),
                        LatLng(42.673889, 23.345057),
                        LatLng(42.673747, 23.345647),
                        LatLng(42.673834, 23.346194),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.671505, 23.340830),
                        LatLng(42.672041, 23.341147),
                        LatLng(42.672502, 23.341689),
                        LatLng(42.673128, 23.342777),
                        LatLng(42.672984, 23.343405),
                        LatLng(42.672684, 23.343410),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.673816, 23.344687),
                        LatLng(42.673820, 23.344526),
                        LatLng(42.673621, 23.344177),
                        LatLng(42.673100, 23.343716),
                        LatLng(42.673889, 23.345057),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.675766, 23.343196),
                        LatLng(42.676011, 23.343582),
                    ).visible(isVisible).width(width).color(aColor)
            )

            addPolyline(
                PolylineOptions()
                    .add(
                        LatLng(42.673794, 23.343888),
                        LatLng(42.674347, 23.343882),
                        LatLng(42.674701, 23.343603),
                    ).visible(isVisible).width(width).color(aColor)
            )
        }
        return gMap
    }
}