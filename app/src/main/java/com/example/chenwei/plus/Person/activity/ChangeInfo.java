 package com.example.chenwei.plus.Person.activity;

 import android.animation.ValueAnimator;
 import android.app.TaskStackBuilder;
 import android.content.Intent;
 import android.content.res.Resources;
 import android.graphics.Bitmap;
 import android.graphics.BitmapFactory;
 import android.os.Build;
 import android.os.Bundle;
 import android.support.annotation.RequiresApi;
 import android.support.v4.app.NavUtils;
 import android.support.v4.content.LocalBroadcastManager;
 import android.support.v7.app.AppCompatActivity;
 import android.view.Gravity;
 import android.view.MenuItem;
 import android.view.View;
 import android.view.animation.Animation;
 import android.view.animation.CycleInterpolator;
 import android.view.animation.TranslateAnimation;
 import android.widget.Button;
 import android.widget.ImageView;
 import android.widget.LinearLayout;
 import android.widget.RelativeLayout;
 import android.widget.TextView;

 import com.example.chenwei.plus.Person.control.FastBlurUtil;
 import com.example.chenwei.plus.Person.control.TXWaveViewByBezier;
 import com.example.chenwei.plus.Person.view.CircleView;
 import com.example.chenwei.plus.Person.view.GuideView;
 import com.example.chenwei.plus.R;

 import java.io.FileInputStream;
 import java.io.FileNotFoundException;
 import java.io.IOException;

 import cn.bmob.v3.BmobUser;
 import de.hdodenhof.circleimageview.CircleImageView;

 public class ChangeInfo extends AppCompatActivity {

     private int REQUEST_PATH = 0;
     private String picPath;
     private String name;

     private RelativeLayout backLayout;

     private TextView nameView;
     private Bitmap scaledBitmap;
     private ImageView background;
//     private GuideView guideView;


     private TXWaveViewByBezier waveViewByBezier1, waveViewByBezier2;

     private CircleImageView img_1;
     private LinearLayout layout;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_change_info);

         picPath = getIntent().getStringExtra("path");
         name = getIntent().getStringExtra("name");
         findViews();
         setListener();
         startAimation();
     }


     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         switch (item.getItemId()) {
             case android.R.id.home:
                 Intent upIntent = NavUtils.getParentActivityIntent(this);
                 if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                     TaskStackBuilder.create(this)
                             .addNextIntentWithParentStack(upIntent)
                             .startActivities();
                 } else {
                     upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                     NavUtils.navigateUpTo(this, upIntent);
                 }

                 return true;

             default:
                 break;
         }
         return super.onOptionsItemSelected(item);
     }

     private void findViews() {
         backLayout=(RelativeLayout) findViewById(R.id.evaluate_down_back_btn);
         background = (ImageView) findViewById(R.id.background);
         nameView = (TextView) findViewById(R.id.name);
//         circleView=(CircleView)findViewById(R.id.drag_circle_view);
         waveViewByBezier1 = findViewById(R.id.wave_bezier1);
         waveViewByBezier2 = findViewById(R.id.wave_bezier2);

         img_1=findViewById(R.id.img_1);
         nameView=findViewById(R.id.name);
         layout=findViewById(R.id.float_layout);
     }

     private void setListener() {

         backLayout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                 intent.putExtra("info_refresh","refresh");
                 LocalBroadcastManager.getInstance(ChangeInfo.this).sendBroadcast(intent);
                 sendBroadcast(intent);

                 finish();
             }
         });

         //        获取需要被模糊的原图bitmap
         if(picPath!=null){
             FileInputStream fis = null;
             try {
                 fis = new FileInputStream(picPath);
                 scaledBitmap  = BitmapFactory.decodeStream(fis);
                 fis.close();
             } catch (FileNotFoundException e) {
                 e.printStackTrace();
             } catch (IOException e) {
                 e.printStackTrace();
             }

         }else{
             Resources res = getResources();
             scaledBitmap = BitmapFactory.decodeResource(res, R.drawable.resource_background);
         }
         nameView.setOnClickListener(nameChange);
         if(name!=null){
             nameView.setText(name);
         }

         //        scaledBitmap为目标图像，10是缩放的倍数（越大模糊效果越高）
         Bitmap blurBitmap = FastBlurUtil.toBlur(scaledBitmap, 5);
         background.setScaleType(ImageView.ScaleType.CENTER_CROP);
         background.setImageBitmap(blurBitmap);


         img_1.setOnClickListener(iconChange);
         if(picPath!=null){
             Bitmap bm = BitmapFactory.decodeFile(picPath);
             img_1.setImageBitmap(bm);
         }

         waveViewByBezier1.setWaveType(TXWaveViewByBezier.SIN);
         waveViewByBezier1.startAnimation();

         waveViewByBezier2.setWaveType(TXWaveViewByBezier.COS);
         waveViewByBezier2.startAnimation();
     }


     private ImageView.OnClickListener iconChange = new Button.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent = new Intent(ChangeInfo.this, ChangeIcon.class);
             intent.putExtra("path", picPath);
             startActivityForResult(intent, REQUEST_PATH);


         }
     };
     private TextView.OnClickListener nameChange = new Button.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent = new Intent(ChangeInfo.this, ChangeName.class);
             intent.putExtra("name", name);
             startActivityForResult(intent, 99);
         }
     };


     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
         super.onActivityResult(requestCode, resultCode, intent);
         if (requestCode == REQUEST_PATH && resultCode == 111) {
             picPath = intent.getStringExtra("path");
             Bitmap bm = BitmapFactory.decodeFile(picPath);
             img_1.setImageBitmap(bm);

 //设置背景
             FileInputStream fis = null;
             try {
                 fis = new FileInputStream(picPath);
                 scaledBitmap  = BitmapFactory.decodeStream(fis);
                 fis.close();
             } catch (FileNotFoundException e) {
                 e.printStackTrace();
             } catch (IOException e) {
                 e.printStackTrace();
             }

             ImageView background = (ImageView) findViewById(R.id.background);
             //        scaledBitmap为目标图像，10是缩放的倍数（越大模糊效果越高）
             Bitmap blurBitmap = FastBlurUtil.toBlur(scaledBitmap, 5);
             background.setScaleType(ImageView.ScaleType.CENTER_CROP);
             background.setImageBitmap(blurBitmap);

             Intent intent2 = new Intent();
             intent2.putExtra("path", picPath);
             setResult(77, intent);

         }
         if (requestCode == 99 && resultCode == 88) {
             name = intent.getStringExtra("name");
             nameView.setText(name);

             Intent intent2 = new Intent();
             intent2.putExtra("name", name);
             setResult(66, intent2);
         }
     }
//     private void setGuideView() {
//
//
//         // 使用文字
//         TextView tv = new TextView(this);
//         tv.setText("拖动至头像"+"\n"+"以添加兴趣爱好");
//         tv.setTextColor(getResources().getColor(R.color.bg_white));
//         tv.setTextSize(30);
//         tv.setGravity(Gravity.CENTER);
//
//
//         guideView = GuideView.Builder
//                 .newInstance(this)
//                 .setTargetView(img_1)
//                 .setCustomGuideView(tv)
//                 .setDirction(GuideView.Direction.LEFT_BOTTOM)
//                 .setShape(GuideView.MyShape.ELLIPSE)   // 设置椭圆形显示区域，
//                 .setBgColor(getResources().getColor(R.color.shadow))
//                 .setOnclickListener(new GuideView.OnClickCallback() {
//                     @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//                     @Override
//                     public void onClickedGuideView() {
//                         guideView.hide();
//
//                     }
//                 })
//                 .build();
//
//
//         guideView.show();
//     }

     @Override
     protected void onResume() {
         super.onResume();
//         setGuideView();
     }
     private void startAimation() {
//        AnimationSet aset=new AnimationSet(true);
//
//        int dy=100;
//
//        TranslateAnimation translateAnimation1=new TranslateAnimation(0,0,0,dy);
//
//        TranslateAnimation translateAnimation2=new TranslateAnimation(0,0,0,-dy);
//
//        aset.addAnimation(translateAnimation1);
//        aset.addAnimation(translateAnimation2);
//
//
//
//        //设置插值器
//        aset.setInterpolator(new LinearInterpolator());
//        //设置动画持续时长
//        aset.setDuration(3000);
//        //设置动画结束之后是否保持动画的目标状态
//        aset.setFillAfter(true);
//        //设置动画结束之后是否保持动画开始时的状态
//        aset.setFillBefore(true);
//        //设置重复模式
//        aset.setRepeatMode(AnimationSet.REVERSE);
//        //aset
//        aset.setRepeatCount(AnimationSet.INFINITE);
//        //设置动画延时时间
//        aset.setStartOffset(2000);
//        //取消动画
////        aset.cancel();
//        //释放资源
////        aset.reset();
//
//
//        img_1.startAnimation(aset);


//        ObjectAnimator animator1=ObjectAnimator.ofFloat(img_1, "rotation", 0,360f);
//        ObjectAnimator animator2=ObjectAnimator.ofFloat(img_1, "translationX", 0,200f);
//        ObjectAnimator animator3=ObjectAnimator.ofFloat(img_1, "translationY", 0,200f);


//        float dy=100l;
//
//        ObjectAnimator animator1=ObjectAnimator.ofFloat(img_1, "translationY", 0,200f);
//        ObjectAnimator animator2=ObjectAnimator.ofFloat(img_1, "translationY", 0,0f);
//        ObjectAnimator animator3=ObjectAnimator.ofFloat(img_1, "translationY", 0,-200f);
//        ObjectAnimator animator4=ObjectAnimator.ofFloat(img_1, "translationY", 0,0f);
//
//        AnimatorSet set=new AnimatorSet();
//        //set.playTogether(animator1,animator2,animator3);
//        set.playSequentially(animator1,animator2,animator3,animator4);
//        set.setDuration(4000);
//        set.start();


//        ObjectAnimator animator1=ObjectAnimator.ofFloat(img_1, "translationY", -100f,100f);
//        ObjectAnimator animator2=ObjectAnimator.ofFloat(img_1, "translationY", 100f,-100f);
//
//        animator1.setRepeatCount(ValueAnimator.INFINITE);
//        animator1.setDuration(2000);
//
//        animator2.setRepeatCount(ValueAnimator.INFINITE);
//        animator2.setDuration(2000);
//
//        AnimatorSet handAnimSet = new AnimatorSet();
//        handAnimSet.setDuration(4000);
//
//
//        handAnimSet.play(animator1).after(animator2);
//
//        handAnimSet.start();

         Animation animation = shakeAnimation(5);

         layout.startAnimation(animation);
     }


     public static Animation shakeAnimation(int counts) {
         Animation translateAnimation = new TranslateAnimation(0, 0, 15, -15);
         translateAnimation.setInterpolator(new CycleInterpolator(counts));
         translateAnimation.setDuration(10000);
         translateAnimation.setRepeatCount(ValueAnimator.INFINITE);
         return translateAnimation;
     }

 }

