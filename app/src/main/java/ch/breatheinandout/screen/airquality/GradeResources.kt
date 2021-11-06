package ch.breatheinandout.screen.airquality

import android.graphics.Color
import ch.breatheinandout.R

object GradeResources {
    private val faceGrade = arrayOf(R.drawable.face_fail, R.drawable.face_best, R.drawable.face_good, R.drawable.face_bad, R.drawable.face_verybad)
    private val pmGrade = arrayOf(R.drawable.state_fail, R.drawable.state_best, R.drawable.state_good, R.drawable.state_bad, R.drawable.state_verybad)
    private val smallGrade = arrayOf(R.drawable.small_face_fail, R.drawable.small_best, R.drawable.small_good, R.drawable.small_bad, R.drawable.small_verybad)
    private val smallFaceGrade = arrayOf(R.drawable.small_face_fail, R.drawable.small_face_best, R.drawable.small_face_good, R.drawable.small_face_bad, R.drawable.small_face_verybad)

    val resourceArray = listOf(faceGrade, pmGrade, smallGrade, smallFaceGrade)
}

//enum class ImgType {
//    KHAI,
//    PM,
//    SMALL,
//    SMALL_FACE
//}
sealed class ImgType(val order: Int)
object KHAI: ImgType(0)
object PM: ImgType(1)
object SMALL: ImgType(2)
object SMALL_FACE: ImgType(3)

object TextColor {
    val textColor = arrayOf(
        Color.rgb(0,0,0),           // FAIL
        Color.rgb(0, 150, 250),     // BEST
        Color.rgb(120, 190, 80),    // GOOD
        Color.rgb(255, 220, 80),    // BAD
        Color.rgb(255, 80, 80)      // VERY BAD
    )
}