package com.example.agroecologico

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.toColor

class menuActivityAdmin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_admin)

        val btnMarketStall = findViewById<Button>(R.id.btnMarketStall)

        btnMarketStall.setOnClickListener{
            startActivity(Intent(this, createMarketStallActivity::class.java))
        }

    }




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.overflowmenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return  when(item.itemId){
            R.id.itemSignout-> {
                //startActivity(Intent(this, ))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

