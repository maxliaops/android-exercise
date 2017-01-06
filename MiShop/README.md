###简介
现在大多数App都会用到底部导航栏，比如QQ、微信和购物App等等。有了底部导航栏，用户可以随时切换界面，查看不同的内容。Android底部导航栏的实现方式特别多，例如TabHost，TabLayout，或者TextView等，都可以实现底部导航栏的效果，但是却没有Google官方统一的导航栏样式，今天讲的就是Google最近添加到Material Design中的底部导航栏BottomNavigationBar。

###使用场景
（1）底部导航栏需要有3-5个标签（tab）,并且每个tab选择的视图重要性要相似，对于少于3个tab的情况，是不推荐使用Bottom navigation的。

（2）如果标签很多，比如有超过了5个这种情况呢？Google也是不提倡使用Bottom navigation的，可以用Drawer navigation替换。

###风格样式
（1）标签Icons和文字的颜色选择是很重要的，一亮一暗才能有对比，用户才很快知道你选择了哪个，如果五颜六色，你是很难分清选择了哪个的。

（2）标签的文字说明要简短而有意义，避免太长的，也不提倡太长了换行和省略的方式。

###基本使用

1.  在Android Studio下添加依赖：
  ```groovy
compile 'com.ashokvarma.android:bottom-navigation-bar:1.3.0'
  ```

2. 在布局文件中，添加布局
  ```xml
<com.ashokvarma.bottomnavigation.BottomNavigationBar
        android:id="@+id/bottom_navigation_bar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom"/>
  ```

3. 在代码中为BottomNavigationBar添加Item选项

  ```
BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_home_white_24dp, "Home"))
                .addItem(new BottomNavigationItem(R.drawable.ic_book_white_24dp, "Books"))
                .addItem(new BottomNavigationItem(R.drawable.ic_music_note_white_24dp, "Music"))
                .addItem(new BottomNavigationItem(R.drawable.ic_tv_white_24dp, "Movies & TV"))
                .addItem(new BottomNavigationItem(R.drawable.ic_videogame_asset_white_24dp, "Games"))
                .initialise();
  ```

4. 添加选项卡切换事件监听
  ```java
bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
         @Override
         public void onTabSelected(int position) {//未选中 -> 选中
         }

         @Override
         public void onTabUnselected(int position) {//选中 -> 未选中
         }

         @Override
         public void onTabReselected(int position) {//选中 -> 选中
         }
     });
  ```

###设置BottomNavigationBar

1. **Mode**
xml：bnbMode
方法：setMode()
包含3种Mode:
  * MODE_DEFAULT
如果Item的个数<=3就会使用MODE_FIXED模式，否则使用MODE_SHIFTING模式
  * MODE_FIXED
填充模式，未选中的Item会显示文字，没有换挡动画。
  * MODE_SHIFTING
换挡模式，未选中的Item不会显示文字，选中的会显示文字。在切换的时候会有一个像换挡的动画

2. **Background Style**
xml: bnbBackgroundStyle
方法：setBackgroundStyles()
包含3种Style:

  * BACKGROUND_STYLE_DEFAULT
如果设置的Mode为MODE_FIXED，将使用BACKGROUND_STYLE_STATIC 。如果Mode为MODE_SHIFTING将使用BACKGROUND_STYLE_RIPPLE。

  * BACKGROUND_STYLE_STATIC点击的时候没有水波纹效果

  * BACKGROUND_STYLE_RIPPLE点击的时候有水波纹效果

3. **设置默认颜色**
xml：bnbActiveColor, bnbInactiveColor, bnbBackgroundColor
方法：setActiveColor, setInActiveColor, setBarBackgroundColor

 -  **in-active color**表示未选中Item中的图标和文本颜色。默认为 Color.LTGRAY

  - **active color :**
在**BACKGROUND_STYLE_STATIC**下，表示选中Item的图标和文本颜色。而
在**BACKGROUND_STYLE_RIPPLE**下，表示整个容器的背景色。默认Theme's Primary Color

  - **background color :**
在**BACKGROUND_STYLE_STATIC**下，表示整个容器的背景色。而
在**BACKGROUND_STYLE_RIPPLE**下，表示选中Item的图标和文本颜色。默认 Color.WHITE

4. **定制Item的选中未选中颜色**
我们可以为每个Item设置选中未选中的颜色，如果没有设置，将继承BottomNavigationBar设置的选中未选中颜色。
方法：
**BottomNavigationItem.setInActiveColor()** 设置Item未选中颜色方法
**BottomNavigationItem.setActiveColor()** 设置Item选中颜色方法

5. **Icon的定制**
如果使用颜色的变化不足以展示一个选项Item的选中与非选中状态，可以使用**BottomNavigationItem.setInActiveIcon()**方法来设置。

6. **设置自动隐藏与显示**
如果BottomNavigationBar是在CoordinatorLayout布局里，默认设置当向下滑动时会自动隐藏它，当向上滑动时会自动显示它。我们可以通过setAutoHideEnabled(boolean)设置是否允许这种行为,好像这个方法木有啦。

7. **手动隐藏与显示**
我们可以在任何时间，通过代码隐藏或显示BottomNavigationBar
方法：
  ```
bottomNavigationBar.hide();//隐藏
bottomNavigationBar.hide(true);//隐藏是否启动动画，这里并不能自定义动画
bottomNavigationBar.unHide();//显示
bottomNavigationBar.hide(true);//隐藏是否启动动画，这里并不能自定义动画
  ```

8. **为Item添加Badge**

  创建一个BadgeItem
  ```
badge=new BadgeItem()
//                .setBorderWidth(2)//Badge的Border(边界)宽度
//                .setBorderColor("#FF0000")//Badge的Border颜色
//                .setBackgroundColor("#9ACD32")//Badge背景颜色
//                .setGravity(Gravity.RIGHT| Gravity.TOP)//位置，默认右上角
          .setText("2")//显示的文本
//                .setTextColor("#F0F8FF")//文本颜色
//                .setAnimationDuration(2000)
//                .setHideOnSelect(true)//当选中状态时消失，非选中状态显示
  ```
  为BottomNavigationItem设置BadgeItem
  ```
new BottomNavigationItem(R.mipmap.ic_directions_bike_white_24dp, "骑行").setBadgeItem(badge)
  ```

###案例实现
GitHub地址：[https://github.com/Ashok-Varma/BottomNavigation](https://github.com/Ashok-Varma/BottomNavigation)

Demo：[https://github.com/maxliaops/android-exercise/tree/master/MiShop](https://github.com/maxliaops/android-exercise/tree/master/MiShop)

**布局文件activity_main.xml**

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    android:id="@+id/activity_main"
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context="com.rainsong.mishop.MainActivity">

    <android.support.v4.view.ViewPager
        android:id="@+id/view_pager"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"/>

    <com.ashokvarma.bottomnavigation.BottomNavigationBar
        android:id="@+id/bottom_navigation_bar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom"/>

</LinearLayout>
```

**源码 MainActivity.java**

```
package com.rainsong.mishop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar
        .OnTabSelectedListener {
    BottomNavigationBar bottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setTabSelectedListener(this);
        bottomNavigationBar.clearAll();
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.icon_main_home_selected, R.string.home)
                        .setInactiveIconResource(R.drawable.icon_main_home_normal)
                        .setActiveColorResource(R.color.orange))
                .addItem(new BottomNavigationItem(R.drawable.icon_main_category_selected, R.string.category)
                        .setInactiveIconResource(R.drawable.icon_main_category_normal)
                        .setActiveColorResource(R.color.orange))
                .addItem(new BottomNavigationItem(R.drawable.icon_main_discover_selected, R.string.discover)
                        .setInactiveIconResource(R.drawable.icon_main_discover_normal)
                        .setActiveColorResource(R.color.orange))
                .addItem(new BottomNavigationItem(R.drawable.icon_main_mine_selected, R.string.mine)
                        .setInactiveIconResource(R.drawable.icon_main_mine_normal)
                        .setActiveColorResource(R.color.orange))
                .initialise();
    }

    @Override
    public void onTabSelected(int position) {

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
```

###效果对比

![小米商城](https://github.com/maxliaops/android-exercise/blob/master/MiShop/MiShop_offical_demo.png)
