package Dispaly;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class extro extends JPanel {
    public List<String>result=new LinkedList<>();
    public  boolean resultIsfree=true;
    public  boolean Ischange=false;

    extro(){
        super();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        try {
            while(!resultIsfree)Thread.sleep(5);
            resultIsfree=false;
            for(int i=0;i<result.size();i++){
                g.drawString(result.get(i),5,20+15*i);
            }
            resultIsfree=true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public  boolean Log(String s){


        try {
            while(!resultIsfree)Thread.sleep(5);
            resultIsfree=false;
            for(String s1:s.split("\n"))result.add(s1);
            resultIsfree=true;
            this.repaint();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        return true;
    }
}