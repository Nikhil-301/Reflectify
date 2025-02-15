package com.example.reflectify

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.reflectify.databinding.ActivityAddJournalBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.Date

class AddJournalActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddJournalBinding

    //Credentials
    var currentUserId: String = " "
    var currentUserName: String = " "

    //Firebase
    lateinit var auth: FirebaseAuth
    lateinit var user: FirebaseUser
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // yaha pe change kiya
    lateinit var storageReference: StorageReference
    var collectionReference: CollectionReference = db.collection("Journal")
    lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_journal)

        storageReference = FirebaseStorage.getInstance().getReference()
        auth = FirebaseAuth.getInstance()
        binding.apply {
            postProgressBar.visibility = View.INVISIBLE
            if (JournalUser.instance != null) {
                currentUserId = JournalUser.instance!!.userId.toString()
                currentUserName = JournalUser.instance!!.username.toString()

                postUsernameTextView.text = currentUserName

            }
            postSaveJournalButton.setOnClickListener {
                SaveJournal()
            }
        }


    }

    private fun SaveJournal() {
        var title: String = binding.postTitleEt.text.toString().trim()
        var thoughts: String = binding.postDescriptionEt.text.toString().trim()

        binding.postProgressBar.visibility = View.VISIBLE
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(thoughts) && imageUri != null) {
            //saving the path in images in storage
            val filePath = storageReference.child("Journal_Images")
                .child("my_image_" + Timestamp.now().seconds)

            // uploading images
            filePath.putFile(imageUri).addOnSuccessListener() {
                filePath.downloadUrl.addOnSuccessListener {
                    var imageUri: String = it.toString()
                    var timestamp: Timestamp = Timestamp(Date())

                    //Creating the object of journal
                    var journal: Journal = Journal(
                        title,
                        thoughts,
                        imageUri,
                        currentUserId,
                        timestamp,
                        currentUserName

                    )

                    collectionReference.add(journal).addOnSuccessListener {
                        binding.postProgressBar.visibility = View.INVISIBLE
                        var i: Intent = Intent(this, JournalList::class.java)
                        startActivity(i)
                        finish()

                    }
                }


            }.addOnFailureListener() {
                binding.postProgressBar.visibility = View.INVISIBLE
            }
        } else {
            binding.postProgressBar.visibility = View.INVISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if(data!=null){
                imageUri = data.data!!
                binding.postImageView.setImageURI(imageUri)
            }
        }

    }

    override fun onStart() {
        super.onStart()
        user = auth.currentUser!!
    }

    override fun onStop() {
        super.onStop()
        if(auth!=null){

        }
    }
}