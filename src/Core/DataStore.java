package Core;

import java.util.*;

public class DataStore {
    public  static List<arribute> arribute=new ArrayList<>();//  属性值静态存储
    public static List<Data> oprateData=new LinkedList<>();
    public static List<Data> storeData=new LinkedList<>();  //样本数据点静态存储

    public static List<ArrayList<Data>> collection =new LinkedList<>();  //存储划分的集合区域
    /*
    * collection中，LinkList的首元素存储的是聚类中的质心点的属性空间的位置
     */

    public static void AddData(Data data){      //头插法，组成链表，含头节点
       oprateData.add(data);
       storeData.add(data.clone());
    }

    public static void  DisplayData(){
        int num=0;
        for(int m=0;m<oprateData.size();m++){
            System.out.print("数据样本点标号"+num+" ");
            for(int i=0;i<arribute.size();i++){
                System.out.print("属性:"+arribute.get(i).arributeName+" ,value:"+
                        oprateData.get(m).value.get(arribute.get(i).arributenumber)+" ");
            }
            System.out.println();
            num++;
        }
    }

    //数据归一化
    public static void  DataNormalization() {
        int arributenumber = 1;
        for (arribute arri : arribute) {
            arributenumber = arri.arributenumber;

            if(arributenumber==0)continue;

            Double min = DataStore.oprateData.get(0).value.get(arributenumber);
            Double max = min;
            Double temp = 0.0, disvalue = 0.0;

            //找到该属性数据的最大最小值,计算差值
            for (Data data : DataStore.oprateData) {
                if (min > data.value.get(arributenumber)) min = data.value.get(arributenumber);
                if (max < data.value.get(arributenumber)) max = data.value.get(arributenumber);
            }
            disvalue = max - min;
            //归一化更新数据
            for (Data data : DataStore.oprateData) {
                temp = data.value.get(arributenumber);
                temp = 100 * (temp - min) / disvalue;
                data.value.put(arributenumber, temp);
            }
        }
    }

    //单一属性排序
    public static void sort(int arributenumber) {
        Collections.sort(DataStore.oprateData, new Comparator<Data>() {
            public int compare(Data o1, Data o2) {
                return compare(o1, o2, arributenumber);
            }

            public int compare(Data o1, Data o2, int arributenumber) {
                if (o1.value.get(arributenumber) > o2.value.get(arributenumber)) return 1;
                else if (o1.value.get(arributenumber) < o2.value.get(arributenumber)) return -1;
                else return 0;
            }
        });

    }

    public DataStore(){}

}








