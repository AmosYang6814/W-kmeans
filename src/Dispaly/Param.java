package Dispaly;

public class Param {

    private static int DataChose=0;

    private static boolean DataIsFree=true;
    public static boolean DataHasDone=false;
    public static boolean  start=false;
    public static String FileAbsulotePath;
    public static int Cluster;

    public static void setDataIsFree(boolean dataIsFree) {
        DataIsFree = dataIsFree;
    }


    public static boolean isDataHasDone() {
        return DataHasDone;
    }




    public static boolean SetDataSource(int Source){
        try {
            while (!DataIsFree)Thread.sleep(5);
            DataIsFree=false;
            DataChose=Source;
            DataIsFree=true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static int getDataSource() {
        int a=0;
        try {
            while (!DataIsFree)Thread.sleep(5);
            DataIsFree=false;
            a=DataChose;
            DataIsFree=true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return a;

    }



}
