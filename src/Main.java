import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import Core.*;
import Dispaly.*;
import Util.register;

public class Main {

    public static void main(String[] args) {
       DisplayOfChoseData displayOfChoseData= new DisplayOfChoseData();

        try {
            while (Param.getDataSource()==0)Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



//        new InitData().start();
//       while (!InitData.IsInit){
//           try {
//               Thread.sleep(10);
//           } catch (InterruptedException e) {
//               e.printStackTrace();
//           }
//       }  //等待数据创建成功
//
//        DataStore.DisplayData();//展示数据
//
//        //加载一次计算算法
//        core c=new core();
//        Thread t=new Thread(c);
//        t.start();

//        System.out.println("Hello World!");
    }






}



class InitDataByCreate extends Thread{
    List<Integer>IndexList=new LinkedList<Integer>();
    HashMap<Integer,Double> value=new HashMap<>();
    public  static  boolean IsInit=false;
    @Override
    public void run() {
        super.run();
        arribute arr1=new arribute(0.3,"属性1",0);
        arribute arr2=new arribute(0.7,"属性2",1);
        DataStore.arribute.add(arr1);
        DataStore.arribute.add(arr2);

        //生成随机数据
        for(int i=0;i<100;i++){
            value.put(0,Math.random()*500);
            value.put(1,Math.random()*500);
            Data data=new Data(value);
            data.Dataweight=1;
            DataStore.AddData(data);
            value.clear();
        }
        Param.DataHasDone=true;
        IsInit=true;

    }


}


