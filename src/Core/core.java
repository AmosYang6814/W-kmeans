package Core;

import Core.Data;
import Core.DataStore;
import Core.arribute;
import Dispaly.DisplayOfResult;
import Dispaly.Param;

import java.util.*;

public class core implements Runnable {
    private static int g = 100;
    private static Double Maxdistance = 0.0;
    private static Double Mindistance = 0.0;
    private static int clariyclass = 0;
    private int merged = 0;
    private int NumberOfCluster = DataStore.oprateData.size();
    private int Cluster=0;      //要求聚类的数据点数目
    /*
    1.计算出每个样本点的受力的值
    2.解析力
    {  3.解析行动速率
       问题：1怎么控制数据是否稳定
            2数据运动越靠后距离越大，怎么控制时间粒度
            3g的大小如何判定
    }{
       3.直接由力经过统计关系得出
       问题：1有怎样的关系，该怎样判定
    }
     */


    //计算每个样本点所受的力
    //其中，距离向量R采用欧式距离计算
    //计算引力变量在各属性空间中的值的大小
    private void CalculatingGravity(int mutiple) {
        Double vaild = Maxdistance / mutiple;  //计算有效引力半径

        HashMap<Integer, Double> Gralation = new HashMap<>();
        Data p = null, q = null;
        for (int m = 0; m < DataStore.oprateData.size(); m++) {
            Caculate(DataStore.oprateData.get(m),Gralation,vaild);
        }
        for(int m=0;m<DataStore.collection.size();m++){
            Caculate(DataStore.collection.get(m).get(0),Gralation,vaild);
        }
    }
    private HashMap<Integer, Double> CaculateGInChild(Data p, Data q, HashMap<Integer, Double> Gralation, double R) {
        Double F = 0.0;
        Double VR = 0.0;
        Double VF = 0.0;
        F = (g * Math.log(q.Dataweight + 1) * Math.log(p.Dataweight + 1)) / R; //计算引力大小

        //计算引力在属性子空间中的投影大小
        for (int i = 0; i <DataStore.arribute.size(); i++) {
            if (i == 0) continue;

            //计算属性空间上的距离：
            VR = q.value.get(DataStore.arribute.get(i).arributenumber)
                    - p.value.get(DataStore.arribute.get(i).arributenumber);

            //属性空间上的引力大小=属性空间上的距离*属性权值*投影值
            VF = F * (VR / R) * DataStore.arribute.get(i).weight;
            //写入属性空间中的引力值
            Gralation.put(DataStore.arribute.get(i).arributenumber,
                    VF + Gralation.get(DataStore.arribute.get(i).arributenumber));
        }

        return Gralation;

    }
    private HashMap<Integer,Double> Caculate(Data p,HashMap<Integer,Double> Gralation,double vaild){
        double R;
        Gralation = p.Gravitation;
        Data q=null;
        //检查属性空间是否初始化
        if (Gralation.size() == 0 || Gralation == null) {
            if (Gralation == null) Gralation = new HashMap<>();
            for (int i = 0; i < DataStore.arribute.size(); i++) {
                Gralation.put(DataStore.arribute.get(i).arributenumber, 0.0);
            }
        }
        /**
         *该for 循环计算引力值大小并解析到各个属性子空间中
         */

        for (int n = 0; n < DataStore.oprateData.size(); n++) {
            q = DataStore.oprateData.get(n);
            if (p.equals(q)) continue;    //跳过自身
            R = CaculateDistance(p, q);
            if (R > vaild) continue;    //大于引力半径则不计算

            Gralation = CaculateGInChild(p, q, Gralation, R);

        }
        for(int n=0;n<DataStore.collection.size();n++){
            q=DataStore.collection.get(n).get(0);
            if(p.equals(q))continue;
            R=CaculateDistance(p,q);
            if(R>vaild)continue;
            Gralation=CaculateGInChild(p,q,Gralation,R);
        }

        p.setGravitation(Gralation);    //将运算后的引力变量存入
        return Gralation;
    }

    //两个孤立点之间的聚合

    /**
     * @param p
     * @param q this Method only can do merge this for two point,but can't delete in original collection
     */
    private ArrayList<Data> mergeData(Data p, Data q) {
        ArrayList<Data> data = new ArrayList<>();
        data.add(new Data(new HashMap<Integer, Double>()));
        data.add(p);
        data.add(q);
        DataStore.oprateData.remove(p);
        DataStore.oprateData.remove(q);
        data = CaculateQualityPoint(data);
        DataStore.collection.add(data);
        return data;
    }

    //点集之间的聚合

    /**
     * @param p
     * @param collection this method also can't delete data in original collection
     */
    private ArrayList<Data> mergeData(Data p, ArrayList<Data> collection) {
        collection.add(p);
        collection = CaculateQualityPoint(collection);
        return collection;
    }

    //两个集合之间的合并

    /**
     * @param collection1
     * @param collection2 this method can merge two collection
     */
    private void mergeData(ArrayList<Data> collection1, ArrayList<Data> collection2) {
        //合并
        System.out.println("collection1.size="+collection1.size());
        System.out.println("collection2.size="+collection2.size());
        for (int i = 1; i < collection1.size(); i++) {
            System.out.println("i="+i);
            collection2.add(collection1.get(i));
        }

        //清除
        collection1.clear();
        DataStore.collection.remove(collection1);

        //重新计算质心点
        collection2 = CaculateQualityPoint(collection2);
    }


    //展示各个属性子空间的中计算之后的引力参数值
    public void Display_prorites(int arrinumber) {
        System.out.println("属性代码" + arrinumber + "加权引力显示");
        Data d = null;
        for (int i = 0; i < DataStore.oprateData.size(); i++) {
            d = DataStore.oprateData.get(i);
            System.out.println("属性值为" + d.value.get(arrinumber) + "引力值为" + d.Gravitation.get(arrinumber));
        }
    }

    //计算欧式距离
    private Double CaculateDistance(Data p, Data q) {
        Double R = 0.0;
        Double sum = 0.0;
        String arributename = null;
        for (int i = 0; i < DataStore.arribute.size(); i++) {
            //System.out.println("p的值"+p.value.get(DataStore.arribute.get(i).arributenumber)+";q的值"
            // +q.value.get(DataStore.arribute.get(i).arributenumber));

            //ID属性不参与计算
            arributename = DataStore.arribute.get(i).arributeName;
            if (arributename.equals("ID") || arributename.equals("Id") ||
                    arributename.equals("id") || arributename.equals("iD")) continue;

            sum = sum + Math.pow((p.value.get(DataStore.arribute.get(i).arributenumber) -
                    q.value.get(DataStore.arribute.get(i).arributenumber)), 2);
        }

        R = Math.sqrt(sum);
        //System.out.println("p="+p.value+",q="+q.value+"距离为："+R);
        return R;
    }


    //计算欧氏空间中的距离最大值最小值并存储
    private void Caculate_MaxAndMix_Distance() {

        double i[] = {0.0,500.0};
        double temp = 0.0;
        for (Data data1 : DataStore.oprateData) i=Max_Min(i, data1);
        for(int m=0;m<DataStore.collection.size();m++) i=Max_Min(i,DataStore.collection.get(m).get(0));
        Maxdistance = i[0];
        Mindistance = i[1];
    }
    private double[] Max_Min(double[] i, Data data1) {
        Double temp;
        for (Data data2 : DataStore.oprateData) {
            temp = CaculateDistance(data1, data2);
            if(temp==0.0)continue;
            if (i[0]< temp) i[0] = temp;
            if (i[1]> temp) i[1]= temp;
        }
        for(int m=0;m<DataStore.collection.size();m++){
            temp=CaculateDistance(data1,DataStore.collection.get(m).get(0));
            if(temp==0.0)continue;
            if (i[0]< temp) i[0] = temp;
            if (i[1]> temp) i[1]= temp;
        }
        return i;
    }


    //是否为ID属性，是则不计算
    private boolean IsID(String S) {
        if (S.equals("ID") ||S.equals("Id") ||
                S.equals("id") || S.equals("iD")) return true;
        else return false;
    }


    //集合中初始质心计算
    private ArrayList<Data> CaculateQualityPoint(ArrayList<Data> collection) {
        Double temp1 = 1.0, temp2 = 1.0;
        for (int i = 1; i < DataStore.arribute.size(); i++) {
            for (int n = 1; n < collection.size(); n++) {
                temp1 = 0.0;
                temp2 = 0.0;
                temp1 = temp1 + collection.get(n).Dataweight * collection.get(n).value.get(i);
                temp2 = temp2 + collection.get(n).Dataweight;
            }

            //计算质心点在属性空间上的值
            collection.get(0).value.put(i, (temp1 / temp2));
        }

        collection.get(0).Dataweight=collection.size();
        return collection;
    }

    //点移动
    private void Move() {
        for (Data data : DataStore.oprateData) {
            for (arribute arribute : DataStore.arribute) {
                if(IsID(arribute.arributeName))continue;//ID属性不参与移动
                data.value.put(arribute.arributenumber, data.value.get(arribute.arributenumber) +
                        data.Gravitation.get(arribute.arributenumber));
            }
        }

        //坐标质心点移动
        for (int i = 0; i < DataStore.collection.size(); i++) {
            for (arribute arribute : DataStore.arribute) {
                if(IsID(arribute.arributeName))continue;//ID属性不参与移动
                DataStore.collection.get(i).get(0).value.put(arribute.arributenumber,
                        DataStore.collection.get(i).get(0).value.get(arribute.arributenumber) +
                                 DataStore.collection.get(i).get(0).Gravitation.get(arribute.arributenumber));
            }
        }
    }

    //碰撞合并检查
    /**
     *
     *
     *
     * this Method rely on the refrsh Distance Param.
     */
    private void DetectMergeInRoute(){

    }
    private boolean DetectCollection(Data p, Data q) {
        Double temp;
        if(p==q)return false;
        temp=CaculateDistance(p,q);
        if(temp<=1.01*Mindistance)return true;
        else return false;
    }


    @Override
    public void run() {
          DataStore.DataNormalization();   //数据归一化处理
        System.out.println("DataStore.data大小"+DataStore.oprateData.size());


        //显示数据
//        for(Data data:DataStore.oprateData) {
//            System.out.println(data.value);
//        }
        System.out.println("进入算法核心");
        Caculate_MaxAndMix_Distance();
//        this.CalculatingGravity(2);
//        System.out.println("最大欧式距离为：" + Maxdistance);
//        this.Display_prorites(2);
//
//        DataStore.sort(2);
//        Display_prorites(100);
//
//        new Dispaly();

       int count = 0;
//

        Cluster= Param.Cluster;                   //读取到最终需要聚类的聚类数
//
//
        //进行引力聚类
        while ( clariyclass <= Cluster ) {

            //延时
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count++;
            this.CalculatingGravity(2 * count); //计算引力值
            System.out.println("计算引力完成");
            Move();
            Caculate_MaxAndMix_Distance();//更新最大最小数据
            System.out.println("Max=" + Maxdistance + ",Min=" + Mindistance);

            //检测点与点之间是否碰撞并合并数据点
            for (int i = 0; i < DataStore.oprateData.size(); i++) {
                for (int m = 0; m < DataStore.oprateData.size(); m++)
                    if (DetectCollection(DataStore.oprateData.get(i), DataStore.oprateData.get(m))) {
                        mergeData(DataStore.oprateData.get(i), DataStore.oprateData.get(m));
                    }
                for (int m = 0; m < DataStore.collection.size(); m++) {
                    if (DetectCollection(DataStore.oprateData.get(i), DataStore.collection.get(m).get(0))) {
                        mergeData(DataStore.oprateData.get(i), DataStore.collection.get(m));
                    }
                }
            }
            for (int i = 0; i < DataStore.collection.size(); i++) {
                for (int m = 0; m < DataStore.oprateData.size(); m++)
                    if (DetectCollection(DataStore.collection.get(i).get(0), DataStore.oprateData.get(m))) {
                        mergeData(DataStore.oprateData.get(m), DataStore.collection.get(i));
                    }
                for (int m = 0; m < DataStore.collection.size(); m++) {
                    System.out.println("L351:DataStore.collection.size:"+DataStore.collection.size());
                    System.out.println("L352:DataStore.collection.get("+i+").get(0):"+DataStore.collection.get(i).get(0));
                    System.out.println("L353:DataStore.collection.get("+m+").get(0):"+DataStore.collection.get(m).get(0));
                    if (DetectCollection(DataStore.collection.get(i).get(0), DataStore.collection.get(m).get(0))) {
                        mergeData(DataStore.collection.get(i), DataStore.collection.get(m));
                    }
                }
            }

            //更新数据的部分描述
            clariyclass = DataStore.collection.size();//计算已经聚类的个数
            System.out.println("当前已经聚类的数目为：" + clariyclass);

        }


        /*
        * 从storeData中还原已经已经移动的数据点
        * */
        int IDnumber=0;
        boolean HasRecover=false;
        for(arribute arribute:DataStore.arribute){
            if(arribute.arributeName.equals("ID") ||arribute.arributeName.equals("id")||
                    arribute.arributeName.equals("Id")||arribute.arributeName.equals("iD")){
                IDnumber=arribute.arributenumber;
                break;
            }
        }
        for(Data data1:DataStore.storeData){
            HasRecover=false;
            for(Data data2:DataStore.oprateData){
                if(data1.value.get(IDnumber).equals(data2.value.get(IDnumber))){
                    data2.value=(HashMap<Integer, Double>) data1.value.clone();
                    HasRecover=true;
                    break;
                }
            }
            if(HasRecover)continue;

            //集合中数据点恢复
            for(ArrayList<Data> list:DataStore.collection){
                for(int m=1;m<list.size();m++){
                    if(data1.value.get(IDnumber).equals(list.get(m).value.get(IDnumber))){
                        list.get(m).value=(HashMap<Integer, Double>) data1.value.clone();
                        break;
                    }
                }
            }

        }

        //重新计算质心点
        for(ArrayList<Data> list:DataStore.collection)CaculateQualityPoint(list);


        /**
         * 使用传统Kmeans算法
         */
        double min=10000.0;
        double temp;
        int flag=0;
        for(Data data:DataStore.oprateData){
            min=10000.0;
            for(int i=0;i<DataStore.collection.size();i++){
                temp=CaculateDistance(data,DataStore.collection.get(i).get(0));
                if(temp<min){
                    min=temp;flag=i;
                    System.out.println(data.value.get(0)+"集合"+i+"距离为："+temp);
                }
            }
            mergeData(data,DataStore.collection.get(flag));
        }

        /**
         * 检查集合点的数量，若集合点的数量大于cluster则继续聚合
         */
        int flag1=0,flag2=1;
        while(DataStore.collection.size()>Cluster){
            min=10000.0;
            for(int i=0;i<DataStore.collection.size();i++){
                for(int m=0;m<DataStore.collection.size();m++){
                    if(DataStore.collection.get(i).equals(DataStore.collection.get(m)))continue;
                    else{
                        if(min>CaculateDistance(DataStore.collection.get(i).get(0),DataStore.collection.get(i).get(0))){
                            min=CaculateDistance(DataStore.collection.get(i).get(0),DataStore.collection.get(i).get(0));
                            flag1=i;flag2=m;
                        }

                    }
                }
            }
            mergeData(DataStore.collection.get(flag1),DataStore.collection.get(flag2));
        }

        /**
         *输出数据检测
         */
        System.out.println("DataStore.data大小"+DataStore.oprateData.size());
        for(int i=0;i<DataStore.collection.size();i++){
            System.out.println("集合"+i+"大小:"+DataStore.collection.get(i).size());
            for(Data data:DataStore.collection.get(i)){
                System.out.println("数据点为："+data.toString());
            }
        }


        new DisplayOfResult();






    }
}
