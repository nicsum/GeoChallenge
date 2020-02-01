package ru.geochallengegame.app.utils

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil


class CalculateUtils {

    companion object{

        fun calculateDistance(from : Pair<Double, Double>, to : Pair<Double, Double>) : Double{
            return  SphericalUtil.computeDistanceBetween(
                    LatLng(from.first, from.second),
                    LatLng(to.first, to.second) )
        }
    }
}
