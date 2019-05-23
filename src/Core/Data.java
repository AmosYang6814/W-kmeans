package Core;

import java.util.HashMap;

//数据点属性量
public class Data{

    public HashMap<Integer,Double> value;          //属性值
    public HashMap<Integer,Double> Gravitation;    //解析到属性引力
    public int Dataweight=1;
    public double ContourCoefficient=0.0;
    public Data(HashMap<Integer,Double> value){   //实构造
        this.value=(HashMap<Integer, Double>) value.clone();
        Gravitation=new HashMap<>();
    }
    public Data(){             //空构造
        this.value=null;
    }

    public void setGravitation(HashMap<Integer, Double> gravitation) {
        this.Gravitation =(HashMap<Integer, Double>) gravitation.clone();
    }

    public Data clone(){
        Data d=new Data((HashMap<Integer, Double>)this.value.clone());
        return  d;
    }

}
