package com.rohit.permissionactivity

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.rohit.permissionactivity.databinding.ActivityMainBinding
import java.security.Permission

class MainActivity : AppCompatActivity() {
    var storageRef=FirebaseStorage.getInstance()
    lateinit var binding: ActivityMainBinding
    var imagePermission=registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if (it){
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this,"Permission not granted",Toast.LENGTH_LONG).show()
        }

    }
    var pickImage=registerForActivityResult(ActivityResultContracts.GetContent()){
        System.out.println("in PickImage $it")
        binding.ivpic.setImageURI(it)
        it?.let { it1->
            storageRef.getReference("Gallery").putFile(it1).addOnSuccessListener {
                    UploadTask->System.out.println("UploadTask ${UploadTask.storage.downloadUrl}")
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btn1.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                pickImage.launch("image/*")
            }else{
                imagePermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }
}

