package com.cn.graduationclient;

import java.util.LinkedList;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class MyApplication extends Application {
    private static int activityCounter=0;
    private static MyApplication mApplicationInstance;
    private static LinkedList<Activity> mActivityLinkedList;
    private ActivityLifecycleCallbacksImpl mActivityLifecycleCallbacksImpl;
    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationInstance=new MyApplication();
        mActivityLinkedList=new LinkedList<Activity>();
        mActivityLifecycleCallbacksImpl=new ActivityLifecycleCallbacksImpl();
        this.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacksImpl);
    }

    public static MyApplication getInstance() {
        if (null==mApplicationInstance) {
            mApplicationInstance=new MyApplication();
        }
        return mApplicationInstance;
    }

    //判断App是否在后台运行
    public boolean isAppRunningBackground(){
        boolean flag=false;
        if(activityCounter==0){
            flag=true;
        }
        return flag;
    }

    //退出应用
    public void finishAllActivity(){
        //unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacksImpl);
        System.out.println("--> mActivityLinkedList.size()="+mActivityLinkedList.size());
        if(null!=mActivityLinkedList){
            for(Activity activity:mActivityLinkedList){
                if(null!=activity){
                    activity.finish();
                }
            }
        }
    }


    private class ActivityLifecycleCallbacksImpl implements ActivityLifecycleCallbacks{
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            System.out.println("onActivityCreated --> "+activity.getClass().getName());
            if (null!=mActivityLinkedList&&null!=activity) {
                mActivityLinkedList.addFirst(activity);
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {
            activityCounter++;
            System.out.println("onActivityStarted --> "+activity.getClass().getName()+",activityCounter="+activityCounter);
        }

        @Override
        public void onActivityResumed(Activity activity) {
            System.out.println("onActivityResumed --> "+activity.getClass().getName());
        }

        @Override
        public void onActivityPaused(Activity activity) {
            System.out.println("onActivityPaused --> "+activity.getClass().getName());
        }

        @Override
        public void onActivityStopped(Activity activity) {
            activityCounter--;
            System.out.println("onActivityStopped --> "+activity.getClass().getName()+",activityCounter="+activityCounter);
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            System.out.println("onActivitySaveInstanceState --> "+activity.getClass().getName());
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            System.out.println("onActivityDestroyed --> "+activity.getClass().getName());
            if (null!=mActivityLinkedList&&null!=activity) {
                if (mActivityLinkedList.contains(activity)) {
                    mActivityLinkedList.remove(activity);
                }
            }
        }

    }

}


