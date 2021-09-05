package com.example.agroecologico

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.agroecologico.databinding.ActivityMenuAdminBinding
class MenuActivityAdmin : AppCompatActivity() {

    private  lateinit var viewBinding: ActivityMenuAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMenuAdminBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.btnAddSalesUnit.setOnClickListener{
            startActivity(Intent(this, AddWeightUnit::class.java))
        }

        viewBinding.btnMarketStall.setOnClickListener{
            startActivity(Intent(this, CreateMarketStallActivity::class.java))
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.overflowmenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return  when(item.itemId){
            R.id.itemSignout-> {
                startActivity(Intent(this, MainActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

