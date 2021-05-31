package ru.spbstu.gusev.medicinesstorage.data.network.medicinesfirebase

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.asDeferred
import ru.spbstu.gusev.medicinesstorage.data.local.medicines.model.Medicine
import ru.spbstu.gusev.medicinesstorage.extensions.generateUid

class MedicinesRemoteDataSource : RemoteDataSource {
    private val currentUser = { Firebase.auth.currentUser }
    private val remoteDB = Firebase.firestore

    private val usersPath = "users"
    private val medicinesPath = "medicines"

    override fun getAll(): Flow<List<Medicine>>? {
        val currentUser = currentUser.invoke()
        return if (currentUser != null) {
            callbackFlow {
                val eventDocument = remoteDB.collection(usersPath)
                    .document(currentUser.uid)
                    .collection(medicinesPath)

                val subscription = eventDocument.addSnapshotListener { snapshot, _ ->
                    if (snapshot?.isEmpty == false) {
                        val medicines = snapshot.toObjects(Medicine::class.java)
                        offer(medicines)
                    }

                }
                awaitClose { subscription.remove() }
            }
        } else null
    }

    override suspend fun getAllAsync(): List<Medicine>? {
        val currentUser = currentUser.invoke()
        return if (currentUser != null) {
            remoteDB.collection(usersPath)
                .document(currentUser.uid)
                .collection(medicinesPath)
                .get().asDeferred().await()
                .toObjects(Medicine::class.java)
        } else null
    }

    override suspend fun getById(id: Int): Medicine? {
        val currentUser = currentUser.invoke()
        return if (currentUser != null) {
            remoteDB.collection(usersPath)
                .document(currentUser.uid)
                .collection(medicinesPath)
                .whereEqualTo("uid", id)
                .get().asDeferred().await()
                .documents.firstOrNull()?.toObject(Medicine::class.java)
        } else null
    }

    override suspend fun insert(vararg medicines: Medicine) {
        val currentUser = currentUser.invoke()
        if (currentUser != null) {
            medicines.forEach { medicine ->
                remoteDB.collection("$usersPath/${currentUser.uid}/$medicinesPath")
                    .add(if (medicine.uid == 0) medicine.copy(uid = generateUid()) else medicine)
            }
        }
    }

    override suspend fun update(medicine: Medicine) {
        val currentUser = currentUser.invoke()
        if (currentUser != null) {
            remoteDB.collection(usersPath)
                .document(currentUser.uid)
                .collection(medicinesPath)
                .whereEqualTo("uid", medicine.uid)
                .get().addOnSuccessListener { docs ->
                    docs.forEach { doc ->
                        remoteDB.collection(usersPath).document(currentUser.uid)
                            .collection(medicinesPath)
                            .document(doc.id).set(medicine)
                    }
                }
        }
    }

    override suspend fun delete(medicine: Medicine) {
        val currentUser = currentUser.invoke()
        if (currentUser != null) {
            remoteDB.collection(usersPath)
                .document(currentUser.uid)
                .collection(medicinesPath)
                .whereEqualTo("uid", medicine.uid)
                .get().addOnSuccessListener { docs ->
                    docs.forEach { doc ->
                        remoteDB.collection(usersPath).document(currentUser.uid)
                            .collection(medicinesPath)
                            .document(doc.id).delete()
                    }
                }
        }
    }

}