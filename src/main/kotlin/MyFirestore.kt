import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.SetOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import java.io.FileInputStream
import java.io.InputStream

class MyFirestore private constructor() {
    private val firestoreDb: Firestore = init()
    private val mainCollection = firestoreDb.collection(mainCollectionName)

    companion object {
        const val projectId = "java-poc-99510"
        const val mainCollectionName = "ASPRID-PoC v2"
        const val criticalAssetsConfigCollectionName = "Critical Assets Config"
        const val exercisesCollectionName = "Exercises"
        const val criticalAssetsCollectionName = "Critical Assets"
        const val eventsCollectionName = "Events"
        const val trafficCollectionName = "Traffic"

        @Volatile
        private var myFirestoreInstance: MyFirestore? = null

        @Synchronized
        fun getInstance(): MyFirestore =
            myFirestoreInstance ?: MyFirestore().also { myFirestoreInstance = it }
    }

    private fun init(): Firestore {
        val serviceAccount: InputStream = FileInputStream("src/main/resources/serviceAccountKey.json")
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setProjectId(projectId)
            .build()
        FirebaseApp.initializeApp(options)
        return FirestoreClient.getFirestore()
    }

    fun close() {
        firestoreDb.close()
    }

    fun addIcao(id: String, icao: Icao) {
        mainCollection.document(id).set(icao, SetOptions.merge()).get()
    }

    fun getIcao(id: String): Icao? = mainCollection.document(id).get().get().toObject(Icao::class.java)

    fun addCriticalAssetsConfigToIcao(
        icaoId: String,
        criticalAssetsConfigId: String,
        criticalAssetsConfig: CriticalAssetsConfig
    ) {
        mainCollection.document(icaoId).collection(criticalAssetsConfigCollectionName)
            .document(criticalAssetsConfigId).set(criticalAssetsConfig, SetOptions.merge()).get()
    }

    fun getCriticalAssetsConfigOfIcao(
        icaoId: String,
        criticalAssetsConfigId: String
    ): CriticalAssetsConfig? = mainCollection.document(icaoId).collection(criticalAssetsConfigCollectionName)
        .document(criticalAssetsConfigId).get().get().toObject(CriticalAssetsConfig::class.java)

    fun addCriticalAssetsToCriticalAssetsConfigOfIcao(
        icaoId: String,
        criticalAssetsConfigId: String,
        criticalAssetsId: String,
        criticalAssets: CriticalAssets
    ) {
        mainCollection.document(icaoId).collection(criticalAssetsConfigCollectionName)
            .document(criticalAssetsConfigId).collection(criticalAssetsCollectionName).document(criticalAssetsId)
            .set(criticalAssets, SetOptions.merge()).get()
    }

    fun getCriticalAssetsOfCriticalAssetsConfigOfIcao(
        icaoId: String,
        criticalAssetsConfigId: String,
        criticalAssetsId: String
    ): CriticalAssets? = mainCollection.document(icaoId).collection(criticalAssetsConfigCollectionName)
        .document(criticalAssetsConfigId).collection(criticalAssetsCollectionName).document(criticalAssetsId).get()
        .get().toObject(CriticalAssets::class.java)

    fun addExerciseToIcao(
        icaoId: String,
        exerciseId: String,
        exercise: Exercise
    ) {
        mainCollection.document(icaoId).collection(exercisesCollectionName)
            .document(exerciseId).set(exercise, SetOptions.merge()).get()
    }

    fun getExerciseOfIcao(
        icaoId: String,
        exerciseId: String
    ): Exercise? = mainCollection.document(icaoId).collection(exercisesCollectionName)
        .document(exerciseId).get().get().toObject(Exercise::class.java)

    fun addEventToExerciseOfIcao(
        icaoId: String,
        exerciseId: String,
        eventId: String,
        event: Event
    ) {
        mainCollection.document(icaoId).collection(exercisesCollectionName)
            .document(exerciseId).collection(eventsCollectionName).document(eventId).set(event, SetOptions.merge())
            .get()
    }

    fun getEventOfExerciseOfIcao(
        icaoId: String,
        exerciseId: String,
        eventId: String
    ): Event? = mainCollection.document(icaoId).collection(exercisesCollectionName)
        .document(exerciseId).collection(eventsCollectionName).document(eventId).get().get().toObject(Event::class.java)

    fun addTrafficToExerciseOfIcao(
        icaoId: String,
        exerciseId: String,
        trafficId: String,
        traffic: Traffic
    ) {
        mainCollection.document(icaoId).collection(exercisesCollectionName)
            .document(exerciseId).collection(trafficCollectionName).document(trafficId).set(traffic, SetOptions.merge())
            .get()
    }

    fun getTrafficOfExerciseOfIcao(
        icaoId: String,
        exerciseId: String,
        trafficId: String
    ): Traffic? = mainCollection.document(icaoId).collection(exercisesCollectionName)
        .document(exerciseId).collection(trafficCollectionName).document(trafficId).get().get()
        .toObject(Traffic::class.java)
}