package com.blackcows.butakaeyak.ui.navigation

import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.replace
import androidx.viewpager2.widget.ViewPager2
import com.blackcows.butakaeyak.MainActivity
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.ActivityMainBinding
import java.util.Stack

object MainNavigation {
    private val TAG = "MainNavigation"
    private lateinit var fragmentManager: FragmentManager

    private var currentTab = TabTag.Home
    private val fragmentStack = HashMap<TabTag, Stack<FragmentTag>>()
    private val tagToFragment = mutableMapOf<FragmentTag, Fragment?>()

    private lateinit var binding: ActivityMainBinding

    //navigation bar 안 보이게 할 때 쓰는 메소드
    fun hideBottomNavigation(state: Boolean) {
        binding.bottomMenuBar.visibility = if (state) View.GONE
                                            else  View.VISIBLE

        Log.d(TAG, "Hide Bottom Bar: $state")
    }


    fun addFragment(fragment: Fragment, tag: FragmentTag, enterAni: Int? = null, exitAni: Int? = null) {
        val transaction = fragmentManager.beginTransaction()

        if (enterAni != null && exitAni != null) {
            transaction.setCustomAnimations(R.anim.alpha,R.anim.none)
        }

        transaction.replace(R.id.fragment_container_view, fragment).commit()


        Log.d("Navigation", "Push Fragment: ${tag.name} to Stack: ${currentTab.name}")
        tagToFragment[tag] = fragment
        fragmentStack[currentTab]!!.push(tag)
    }

    fun popCurrentFragment(enterAni: Int? = null, exitAni: Int? = null) {
        val curStack = fragmentStack[currentTab]!!
        val transaction = fragmentManager.beginTransaction()

        if(curStack.size == 0) return

        if(enterAni != null && exitAni != null) {
            transaction.setCustomAnimations(enterAni, exitAni)
        }

        if(curStack.size == 1) {
            val tag = curStack[0]!!
            transaction.remove(tagToFragment[tag]!!).commit()
            tagToFragment[tag] = null
        } else {
            val exTag = curStack.lastElement()!!
            val tag = curStack[curStack.lastIndex - 1]
            transaction.replace(R.id.fragment_container_view, tagToFragment[tag]!!).commit()
            tagToFragment[exTag] = null
        }

        Log.d("Navigation", "size: ${curStack.size}")
        curStack.pop()
    }



    fun toOtherTab(tabTag: TabTag) {
        currentTab = tabTag
        binding.viewPager.currentItem = tabTag.index
    }

    //-------------------------------------------------------------------------------------------------------------


    fun initialize(activity: MainActivity, binding: ActivityMainBinding) {
        fragmentManager = activity.supportFragmentManager

        fragmentStack[TabTag.Home] = Stack()
        fragmentStack[TabTag.Search] = Stack()
        fragmentStack[TabTag.Map] = Stack()
        fragmentStack[TabTag.User] = Stack()

        initViewPager(activity, binding)
        initNavigation(binding)
        addBackPressedCallback(activity, binding)
    }


    private fun initViewPager(activity: MainActivity, b: ActivityMainBinding) {
        binding = b
        val viewPager = binding.viewPager
        val viewPagerAdapter = MainViewpager(activity)
        viewPager.adapter = viewPagerAdapter

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.bottomMenuBar.menu.getItem(position).isChecked = true
            }
        })
    }
    private fun initNavigation(binding: ActivityMainBinding) {
        binding.viewPager.isUserInputEnabled = false

        binding.bottomMenuBar.setOnItemSelectedListener { item ->
            val listName = fragmentStack[currentTab]!!.map {
                it.name
            }
            Log.d(TAG, "ex Tab(${currentTab.name}): ${listName.joinToString()}")

            val transaction = fragmentManager.beginTransaction()
            if(fragmentStack[currentTab]!!.size != 0) {

                val exFragment = tagToFragment[fragmentStack[currentTab]!!.lastElement()]!!

                transaction.remove(exFragment)
            }

            currentTab = when(item.itemId) {
                R.id.navigation_take -> {
                    binding.viewPager.currentItem = 0
                    TabTag.Home
                }
                R.id.navigation_home -> {
                    binding.viewPager.currentItem = 1
                    TabTag.Search
                }
                R.id.navigation_map -> {
                    binding.viewPager.currentItem = 2

                    TabTag.Map
                }
                R.id.navigation_user -> {
                    binding.viewPager.currentItem = 3
                    TabTag.User
                }
                else -> return@setOnItemSelectedListener false
            }

            val curListName = fragmentStack[currentTab]!!.map {
                it.name
            }
            Log.d(TAG, "cur Tab(${currentTab.name}): ${curListName.joinToString()}")

            if(fragmentStack[currentTab]!!.size != 0) {
                Log.d(TAG, "last Tag: ${fragmentStack[currentTab]!!.lastElement().name}")
                val curFragment = tagToFragment[fragmentStack[currentTab]!!.lastElement()]!!

                transaction.replace(R.id.fragment_container_view, curFragment)
            }

            transaction.commit()
            true
        }
    }
    private fun addBackPressedCallback(activity: MainActivity, binding: ActivityMainBinding) {
        val callback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d(TAG, "Back Pressed!")
                val curStack = fragmentStack[currentTab]!!
                if(curStack.size == 0) {
                    if(currentTab == TabTag.Home) {
                        activity.finishAffinity()
                    } else {
                        currentTab = TabTag.Home
                        binding.viewPager.currentItem = 0
                    }
                } else popCurrentFragment()
            }
        }
        activity.onBackPressedDispatcher.addCallback(callback)
    }
}