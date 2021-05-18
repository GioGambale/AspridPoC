import java.sql.Timestamp

fun main() {
    val myFS = MyFirestore.getInstance()

    myFS.addIcao("LIMC", Icao("Milano Malpensa", Position("45° 37' 28.79 N", "8° 43' 13.79 E")))
    myFS.addCriticalAssetsConfigToIcao("LIMC", "config1", CriticalAssetsConfig())
    myFS.addCriticalAssetsToCriticalAssetsConfigOfIcao(
        "LIMC",
        "config1",
        "CriticalAssetsId1",
        CriticalAssets("CA1", Position("0", "0"), 5, 7, 10)
    )
    val exerciseId = Timestamp(System.currentTimeMillis()).toString()
    myFS.addExerciseToIcao("LIMC", exerciseId, Exercise("config1"))
    myFS.addEventToExerciseOfIcao(
        "LIMC",
        exerciseId,
        "event1",
        Event("ev1", "desc", "CriticalAssetsId1", "DRN01", "drone", Timestamp(System.currentTimeMillis()))
    )
    myFS.addTrafficToExerciseOfIcao(
        "LIMC",
        exerciseId,
        "traffic1",
        Traffic("tr1", "Giovanni", Position("0", "0"), "damaged", "")
    )

    myFS.close()
}