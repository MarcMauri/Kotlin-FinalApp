package es.marcmauri.finalapp.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import es.marcmauri.finalapp.R
import es.marcmauri.finalapp.adapters.PagerAdapter
import es.marcmauri.finalapp.fragments.ChatFragment
import es.marcmauri.finalapp.fragments.InfoFragment
import es.marcmauri.finalapp.fragments.RatesFragment
import es.marcmauri.finalapp.utils.goToActivity
import es.marcmauri.finalapp.utils.toast
import es.marcmauri.mylibrary.ToolbarActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : ToolbarActivity() {

    private var prevBottomSelected: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbarToLoad(toolbarView as Toolbar)

        setUpViewPager(getPagerAdapter())
        setUpBottomNavigationBar()
    }

    private fun getPagerAdapter(): PagerAdapter {
        val adapter = PagerAdapter(supportFragmentManager)
        adapter.addFragment(InfoFragment())
        adapter.addFragment(RatesFragment())
        adapter.addFragment(ChatFragment())
        return adapter
    }

    private fun setUpViewPager(adapter: PagerAdapter) {
        viewPager.adapter = adapter
        // Esta linea es para que todos los fragments del adapter queden en memoria y no se destruyan sus vistas
        viewPager.offscreenPageLimit = adapter.count
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                if (prevBottomSelected == null) {
                    bottomNavigation.menu.getItem(0).isChecked = false
                } else {
                    prevBottomSelected!!.isChecked = false
                }
                bottomNavigation.menu.getItem(position).isChecked = true
                prevBottomSelected = bottomNavigation.menu.getItem(position)
            }
        })
    }

    private fun setUpBottomNavigationBar() {
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_nav_info -> {
                    viewPager.currentItem = 0; true
                }
                R.id.bottom_nav_rates -> {
                    viewPager.currentItem = 1; true
                }
                R.id.bottom_nav_chat -> {
                    viewPager.currentItem = 2; true
                }
                else -> false
            }
        }
    }


    /* Events */

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_log_out -> {
                FirebaseAuth.getInstance().signOut()
                goToActivity<LoginActivity> {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
