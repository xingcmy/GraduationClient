package com.cn.graduationclient.music;

public class Random {
    private int[] sum=new int[]{2,1,2,3,4,5,6,8,9,11,15,20,14,13,16,7,4,8,7,5,6,11,12,45,7,1,2,5,6,7,8,9,10,11,12,15,26,15,2,4,6,8,7,6,1,8,9
    ,11,5,6,4,3,2,11,45,26,11,5,9,10,12,16,1,8,10,11,12,6,8,9};
    public int Random(int idex,int song,int i){
        idex=idex+sum[i];
        while (true){
            if (idex >= song){
                idex=idex-song;
            }
            else break;
        }

        return idex;
    }
}
