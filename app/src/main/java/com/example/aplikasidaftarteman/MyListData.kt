package com.example.aplikasidaftarteman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyListData : AppCompatActivity() {
    private var RecyclerView:RecyclerView? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    val database = FirebaseDatabase.getInstance()
    private var dataTeman = ArrayList<data_teman>()
    private var auth:FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_list_data)
        RecyclerView = findViewById(R.id.datalist)
        supportActionBar!!.title = "DataTeman"
        auth = FirebaseAuth.getInstance()
        MyRecyclerView()
        GetData()
    }

    private fun GetData() {
        Toast.makeText(applicationContext, "Mohon Tunggu Sebentar...",
            Toast.LENGTH_LONG).show()
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        val getReference = database.getReference()
        getReference.child("Admin").child(getUserID).child("DataTeman")
            .addValueEventListener(object :ValueEventListener {
            override fun onDataChange(datasnapshot: DataSnapshot) {
                if (datasnapshot.exists()) {
                    for (snapshot in datasnapshot.children) {
                        val teman = snapshot.getValue(data_teman::class.java)
                        teman?.key = snapshot.key
                        dataTeman.add(teman!!)
                    }
                    adapter = RecyclerViewAdapter(dataTeman, this@MyListData)
                    RecyclerView?.adapter = adapter
                    (adapter as RecyclerViewAdapter).notifyDataSetChanged()
                    Toast.makeText(applicationContext, "Data Berhasil dimuat",
                    Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(applicationContext, "Data Gagal Dimuat",
                Toast.LENGTH_LONG).show()
                Log.e("MyListActivity", databaseError.details + " " +
                        databaseError.message)
            }
            })
    }

    private fun MyRecyclerView(){
        layoutManager = LinearLayoutManager(this)
        RecyclerView?.layoutManager = layoutManager
        RecyclerView?.setHasFixedSize(true)

        val itemDecoration = DividerItemDecoration(applicationContext,
        DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.line)!!)
        RecyclerView?.addItemDecoration(itemDecoration)
    }
}