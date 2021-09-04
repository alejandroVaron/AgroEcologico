package com.example.agroecologico

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.agroecologico.databinding.ActivityMenuAdminBinding

class menuActivityAdmin : AppCompatActivity() {
    private  lateinit var viewBinding: ActivityMenuAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMenuAdminBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.btnAddSalesUnit.setOnClickListener{
            val intent = Intent(this, addWeightUnit::class.java)
            this.startActivity(intent)
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

