
import Core.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


public class Dispaly extends JFrame implements Runnable {

   public  Dispaly(){
       this.setTitle("基于特征加权的kmeans算法");
       this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       this.setBounds(100,100,750,700);
       JPanel contentPane=new JPanel();
       contentPane.setBorder(new EmptyBorder(5,5,5,5));
       this.setContentPane(contentPane);
       contentPane.setLayout(null);

       //设置属性选择器
       JLabel label=new JLabel("数据属性:");
       Font labelfont=new Font("宋体",Font.PLAIN,20);
       label.setFont(labelfont);
       JComboBox comboBox=new JComboBox();

       for(Core.arribute arribute: DataStore.arribute){
           comboBox.addItem(arribute.arributeName);
       }
       comboBox.setFont(labelfont);


       //属性选择面板
       JPanel repoies=new JPanel();
       repoies.setBackground(Color.BLUE);
       repoies.setBounds(450,20,300,100);
       repoies.add(label);
       repoies.add(comboBox);
       repoies.setLayout(new FlowLayout());

       //绘图面板
       Canvas canvas=new Canvas(comboBox.getSelectedIndex()+1);
       canvas.setBounds(40,200,660,300);
       canvas.setBackground(Color.LIGHT_GRAY);

       contentPane.add(repoies);
       contentPane.add(canvas);
       this.setVisible(true);

    }


    public void run() {

    }

}

class Canvas extends JPanel{
    int arributenumber;


    Canvas(int arributenumber){
        this.arributenumber=arributenumber;
    }

    public void paint(Graphics g) {
        super.paint(g);

        g.drawLine(0,150,660,150);

        for(Data data:DataStore.oprateData){
            g.fillOval((int)(6.6*data.value.get(1)),(int)(3*data.value.get(2)),3,3);
        }


    }


    public void repaint(int arributenumber) {
        this.arributenumber=arributenumber;
        repaint();
    }

}
