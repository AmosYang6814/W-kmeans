package Dispaly;

import Core.Data;
import Core.DataStore;
import Core.arribute;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class DisplayOfResult extends JFrame {
   private JLabel arribute,data,collection,result;
   private JList<String> JTarribute,JTdata,JTcollection;
   private int height,width;
   private  JComboBox JC;

    public DisplayOfResult(){
        //设置截面大小
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        height=(int)(screenSize.getHeight()*(3/4));
        width=(int)(screenSize.getWidth()*(1/2));

        //初始化控件
        arribute=new JLabel("属性及其权值");
        arribute.setBounds(24,24,116,30);
        arribute.setFont(new Font("微软雅黑",Font.PLAIN,17));
        this.getContentPane().add(arribute);
        data=new JLabel("数据点");
        data.setBounds(24,291,116,30);
        data.setFont(new Font("微软雅黑",Font.PLAIN,17));
        this.getContentPane().add(data);
        collection=new JLabel("聚集的簇");
        collection.setBounds(498,24,147,30);
        collection.setFont(new Font("微软雅黑",Font.PLAIN,17));
        this.getContentPane().add(collection);
//        result=new JLabel("轮廓系数");
//        result.setBounds(498,664,116,24);
//        this.getContentPane().add(result);

        //数据显示区域
        JTarribute=SetArributeData(new JList());
        JTarribute.setBounds(24,69,372,177);
        JScrollPane scrollarribute = new JScrollPane(JTarribute);
        scrollarribute.setBounds(JTarribute.getBounds());
        this.getContentPane().add(scrollarribute);


        JTdata=SetData(new JList());
        JTdata.setBounds(24,329,372,376);
        JScrollPane scrolldata = new JScrollPane(JTdata);
        scrolldata.setBounds(JTdata.getBounds());
        this.getContentPane().add(scrolldata);


        JTcollection=new JList();
        JTcollection.setBounds(498,118,372,495);
        JTcollection.setFont(new Font("微软雅黑",Font.PLAIN,18));
        JScrollPane scrollcollection = new JScrollPane(JTcollection);
        SetCillection(0,JTcollection);
        scrollcollection.setBounds(JTcollection.getBounds());
        this.getContentPane().add(scrollcollection);


//        String []s;
//        s=new String[DataStore.collection.size()];
//        for(int i=0;i<s.length;i++)s[i]="集合标号："+i;
       JC=new JComboBox();
        JC.setBounds(498,63,372,30);
        JC.setFont(new Font("微软雅黑",Font.PLAIN,17));
        this.getContentPane().add(JC);
       for(int i=0;i<DataStore.collection.size();i++){
           JC.addItem("簇"+(i+1));
       }
       JC.addItemListener(new Item());

        System.out.println(screenSize.getHeight()+","+screenSize.getWidth());
        this.setBounds(460,10-0,960,810);
        this.getContentPane().setLayout(null);
        this.getContentPane().setBounds(460,100,960,810);
        this.setVisible(true);



    }

    public JList SetArributeData(JList JTarribute){
        ArrayList<String> arri=new ArrayList<>();

        for(arribute arribute:DataStore.arribute){

           String s="属性名称:"+String.format("%10s",arribute.arributeName)+",属性权值:"+arribute.weight+",属性编号:"+arribute.arributenumber;
           arri.add(s);
        }
        JTarribute.setListData(arri.toArray());
        JTarribute.setFont(new Font("微软雅黑",Font.PLAIN,20));
        return JTarribute;
    }

    public JList SetData(JList JTdata){
        ArrayList<String> data=new ArrayList<>();

        for(Data d:DataStore.oprateData){
            data.add(d.value.toString());
        }
        JTdata.setListData(data.toArray());
        JTdata.setFont(new Font("微软雅黑",Font.PLAIN,17));
        return JTdata;
    }

    public void SetCillection(int i,JList JTcollection){
        ArrayList<String> data=new ArrayList<>();
        data.add("质心点属性值: "+DataStore.collection.get(i).get(0).value.toString());
        data.add("——————————————————————————————————————————————————");
        for(int m=1;m<DataStore.collection.get(i).size();m++){
            data.add(DataStore.collection.get(i).get(m).value.toString());
        }
        JTcollection.setListData(data.toArray());
        JTcollection.repaint();
        JTcollection.setFont(new Font("微软雅黑",Font.PLAIN,17));
    }

    class Item implements ItemListener{
        public void itemStateChanged(ItemEvent event){
            String s=(String)event.getItem();
            s=s.substring(1,2);
            SetCillection((Integer.valueOf(s)-1),JTcollection);
        }
    }


}
