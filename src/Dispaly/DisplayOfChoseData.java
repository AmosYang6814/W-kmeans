package Dispaly;

import Core.Data;
import Core.DataStore;
import Core.arribute;
import Core.core;
import Util.register;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class DisplayOfChoseData extends JFrame {
    JButton chose, start;
    JLabel cluster,weight,wei,weititle;
    JTextField path,clusterInput,weightInput;
    JComboBox arribute;


    public DisplayOfChoseData() {

        chose = new JButton("选择数据");
        chose.setBounds(71,24,182,27);
        chose.setFont(new Font("微软雅黑",Font.PLAIN,18));
        this.getContentPane().add(chose);

        path=new JTextField();
        path.setBounds(71,69,315,27);
        path.setFont(new Font("微软雅黑",Font.PLAIN,18));
        this.getContentPane().add(path);

        cluster=new JLabel("设置聚类数");
        cluster.setBounds(466,24,96,27);
        cluster.setFont(new Font("微软雅黑",Font.PLAIN,18));
        this.getContentPane().add(cluster);

        clusterInput=new JTextField("5");
        clusterInput.setBounds(606,24,75,27);
        clusterInput.setFont(new Font("微软雅黑",Font.PLAIN,18));
        this.getContentPane().add(clusterInput);

        weight=new JLabel("属性");
        weight.setBounds(71,150,96,27);
        weight.setFont(new Font("微软雅黑",Font.PLAIN,18));
        this.getContentPane().add(weight);

        arribute=new JComboBox();
        arribute.setBounds(200,150,200,27);
        arribute.setFont(new Font("微软雅黑",Font.PLAIN,18));
        this.getContentPane().add(arribute);

        wei=new JLabel("特征权值");
        wei.setBounds(71,200,96,27);
        wei.setFont(new Font("微软雅黑",Font.PLAIN,18));
        this.getContentPane().add(wei);

        weightInput=new JTextField();
        weightInput.setBounds(200,200,200,27);
        weightInput.setFont(new Font("微软雅黑",Font.PLAIN,15));
        weightInput.setEnabled(false);
        this.getContentPane().add(weightInput);

        weititle=new JLabel("特征权值默认为属性的平均分配");
        weititle.setBounds(90,250,400,27);
        weititle.setFont(new Font("微软雅黑",Font.PLAIN,18));
        this.getContentPane().add(weititle);



        start = new JButton();
        start.setBounds(350,370,80,80);
        start.setContentAreaFilled(false);
        ImageIcon ic=new ImageIcon("img/start.png");
        Image temp = ic.getImage().getScaledInstance(start.getWidth(), start.getHeight(), ic.getImage().SCALE_DEFAULT);
        ic = new ImageIcon(temp);
        start.setIcon(ic);
        start.setBorder(null);//除去边框
        this.getContentPane().add(start);


        this.getContentPane().setLayout(null);


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(540,170,800,610);
        this.setVisible(true);





        //属性输入框事件
        weightInput.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {




                String s=(String)arribute.getSelectedItem();
                for(Core.arribute arri:DataStore.arribute){
                    if(arri.arributeName.equals(s))arri.weight=Double.valueOf(weightInput.getText());
                }
            }
        });

        //文件选择按钮事件
        chose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFont(new Font("微软雅黑",Font.PLAIN,20));
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                chooser.showDialog(new JLabel(), "选择");
                File file = chooser.getSelectedFile();
                Param.FileAbsulotePath=file.getAbsolutePath();
                path.setText(Param.FileAbsulotePath);
                new InitData(DisplayOfChoseData.this).start();
            }
        });

        //下拉列表事件
        arribute.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                for(Core.arribute arri:DataStore.arribute){
                    if(arri.arributeName.equals(arribute.getSelectedItem())){
                        if(IsID(arri.arributeName))weightInput.setEnabled(false);
                        else {
                            weightInput.setEnabled(true);
                            weightInput.setText(""+arri.weight);
                        }
                    }
                }
            }
        });


        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double sum=0.0;
                for(Core.arribute arri:DataStore.arribute){
                    sum=sum+arri.weight;
                }

                if(path.getText().equals("")) {
                    JOptionPane.showMessageDialog(new JPanel(),
                            "请选择文件", "错误提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if(sum!=1){
                    JOptionPane.showMessageDialog(new JPanel(),
                            "特征权值之和不为1，请重新输入", "错误提示",JOptionPane.WARNING_MESSAGE);
                    return;
                }



                if (!Param.isDataHasDone()) {
                    return;
                } else {
                    Param.Cluster=Integer.valueOf(clusterInput.getText());
                    /*加载进入算法界面*/
                    new Thread(new core()).start();

                }

            }
        });


    }

    public void SetCombox(){

        for(arribute arri: DataStore.arribute){
            System.out.println(arri.arributeName);
            arribute.addItem(arri.arributeName);
        }
        if(IsID((String)arribute.getSelectedItem()))weightInput.setEnabled(false);
    }


    public boolean IsID(String s){

            if(s.equals("ID")||s.equals("id")||
                    s.equals("iD")||s.equals("Id"))return true;
            else return false;
    }


}

class InitData extends Thread{
    FileInputStream file;
    Reader read;
    BufferedReader bufferedReader;
    String line;
    List<Integer> IndexList=new LinkedList<Integer>();
    HashMap<Integer,Double> value=new HashMap<>();
    public  static  boolean IsInit=false;
    private DisplayOfChoseData displayOfChoseData;
    InitData(DisplayOfChoseData displayOfChoseData){
        this.displayOfChoseData=displayOfChoseData;
    }

    public void run() {
        super.run();

        try {
            file = new FileInputStream(Param.FileAbsulotePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        read =new InputStreamReader(file);
        bufferedReader=new BufferedReader(read);

        try {
            while((line=bufferedReader.readLine())!=null){

                //字符串处理
                String[] s=line.split(",");
                Double temp=0.0;
                value.clear();
                for(int i=0;i<s.length;i++){

                    //划分数据及属性
                    try {
                        temp=Double.valueOf(s[i]);
                    } catch (NumberFormatException e) {
                        //添加属性
                        arribute arribute=new arribute(0,s[i],i);
                        if(arribute.arributeName.equals("ID")||arribute.arributeName.equals("id")||
                                arribute.arributeName.equals("iD")||arribute.arributeName.equals("Id"))arribute.weight=0;
                        DataStore.arribute.add(arribute);
                        continue;
                    }
                    value.put(i,temp);
                }

                //清空ID属性的权值


                //存入数据点
                if(value.size()!=0){
                    Data d=new Data(value);
                    DataStore.AddData(d);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        IsInit=true;
        Param.DataHasDone=true;
        displayOfChoseData.SetCombox();
    }
}


