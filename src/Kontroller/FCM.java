/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Kontroller;

import Model.DataPengelompokan;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 *
 * @author USER
 */
public class FCM {
    final int maxiter;
    private int posisi;
    private final double error;
    private double[][] matriksPartisi;
    private double[][] matriksClusterFinal;
    private double[][] DataWarga;
    private double[][] pusatClusterFCM;
    private int data;
    private boolean isSendedPartisi;
    private final int clustering;
    private int attribut;
    private String[] dataKelayakan;;
    
    public FCM(int maxiter, double error, int clustering){
        this.maxiter = maxiter;
        this.error = error;
        this.clustering = clustering;
    }
    
    public void inisialisasiData(double[][] dataWarga){
        int i,j;
        isSendedPartisi = false;
        DataWarga = new double[dataWarga.length][dataWarga[0].length];
        
        for(i=0; i<dataWarga.length; i++){
            for(j=0; j<dataWarga[0].length; j++){
                DataWarga[i][j] = dataWarga[i][j];
            }
        }
        
        data = DataWarga.length;
        attribut = DataWarga[0].length;
        
        //System.out.println("data : "+data);
        System.out.println("Inisialisasi Data is Done");
    }
    
    public double[][] getMatriksPatrisi(){
        int i,j;
        double[][] partisi;
        partisi = new double[data][clustering];
        
        for(i=0; i<data; i++){
            for(j=0; j<clustering; j++){
                partisi[i][j] = Math.random();
            }
        }
        //CekMatriksPartisi(partisi);
        
        return partisi;
    }
    
    public void setMatriksPatrisi(double[][] mPartisi){
        int i,j;
        matriksPartisi = new double[data][clustering];
        
        for(i=0; i<data; i++){
            for(j=0; j<clustering; j++){
                matriksPartisi[i][j] = mPartisi[i][j];
            }
        }
        
        isSendedPartisi = true;
        //CekMatriksPartisi(partisi);
    }
    
    public double[][] getMatriksPatrisiKuadrat(double[][] partisi_temp){
        int i,j;
        double[][] partisiP2;
        double[][] partisi;
        
        partisi = new double[data][clustering];
        
        for(i=0; i<data; i++){
            for(j=0; j<clustering; j++){
                partisi[i][j] = partisi_temp[i][j];
            }
        }
        
        partisiP2 = new double[data][clustering];
        for(i=0; i<data; i++){
            for(j=0; j<clustering; j++){
                partisiP2[i][j] = Math.pow(partisi[i][j], 2);
            }
        }
        
        return partisiP2;
    }
    
    public void setPusatClusterFCM(double[][] partisiP2_temp){
        double[][] cross;
        double[][] partisiP2;
        double[][] dataWargatemp;
        double[] sum;
        double sumCluster;
        int i,j,k;
        
        cross = new double[data][attribut];
        sum = new double[clustering];
        partisiP2 = new double[data][clustering];
        dataWargatemp = new double[data][attribut];
        
        for(i=0; i<clustering; i++){
            sum[i] = 0.0;
        }
        
        for(i=0; i<data; i++){
            for(j=0; j<clustering; j++){
                partisiP2[i][j] = partisiP2_temp[i][j];
            }
        }
        
        for(i=0; i<data; i++){
            for(j=0; j<attribut; j++){
                dataWargatemp[i][j] = DataWarga[i][j];
            }
        }
        
        for(i=0; i<clustering; i++){
            for(j=0; j<data; j++){
                sum[i] = sum[i]+ partisiP2[j][i];
            }
        }
        
        for(i=0; i<clustering; i++){
            for(j=0; j<attribut; j++){
                sumCluster = 0.0;
                for(k=0; k<data; k++){
                    //melakukan perkalian antara datawarga dan partikel yang dikuadratkan
                    cross[k][j] = dataWargatemp[k][j] * partisiP2[k][i];
                    sumCluster = sumCluster + cross[k][j];
                }   
                pusatClusterFCM[i][j] =  sumCluster/sum[i];
            }                
        }
    }
    
    public double getFungsiObjektifFCM(int kondisi){
        int i,j,k;
        int index_terbesar;
        double[][] partisiP2;
        double[][] dataWargatemp;
        double[] L;
        double[] jumlahL;
        double fitnessTerbesar;
        double jumlah;
        double selisih;
        double kuadratselisih;
        double f_objective;
        
        pusatClusterFCM = new double[clustering][attribut];
        dataWargatemp = new double[data][attribut];
        
        for(i=0; i<data; i++){
            for(j=0; j<attribut; j++){
                dataWargatemp[i][j] = DataWarga[i][j];
            }
        }
        
        if(kondisi == 0 && isSendedPartisi == false){
            matriksPartisi = getMatriksPatrisi();
        }
        
        partisiP2 = getMatriksPatrisiKuadrat(matriksPartisi);
        
        setPusatClusterFCM(partisiP2);
        
        //CekMatriksPusatCluster();
        
        L = new double[clustering];
        jumlahL = new double[data];
        
        for(i=0; i<data; i++){
            jumlahL[i] = 0.0;
        }

        for(i=0; i<data; i++){
            for(j=0; j<clustering; j++){
                jumlah = 0.0;

                for(k=0; k<attribut; k++){
                    selisih = dataWargatemp[i][k] - pusatClusterFCM[j][k];
                    kuadratselisih = Math.pow(selisih, 2);

                    jumlah = jumlah + kuadratselisih;
                }

                L[j] = jumlah * partisiP2[i][j];
                jumlahL[i] = jumlahL[i] + L[j];
            }                 
        }

        jumlah = 0.0;
        for(i=0; i<data; i++){
            jumlah = jumlah + jumlahL[i];
        }

        f_objective = 1.0/jumlah;
        
        System.out.println("Hitung Fungsi Objektif is Done");
        
        return f_objective;
    }
    
    public void setUpdateMatriksPartisi(){
        int i,j,k;
        
        double[][] L;
        double jumlah;
        double selisih;
        double kuadratselisih;
        double[] jumlahL;
        double[][] dataWargatemp;
        
        L = new double[data][clustering];
        jumlahL = new double[data];
        
        dataWargatemp = new double[data][attribut];
        
        for(i=0; i<data; i++){
            for(j=0; j<attribut; j++){
                dataWargatemp[i][j] = DataWarga[i][j];
            }
        }
        
        for(i=0; i<data; i++){
            jumlahL[i] = 0.0;
        }

        for(i=0; i<data; i++){
            for(j=0; j<clustering; j++){
                jumlah = 0.0;

                for(k=0; k<attribut; k++){
                    selisih = dataWargatemp[i][k] - pusatClusterFCM[j][k];
                    kuadratselisih = Math.pow(selisih, 2);

                    jumlah = jumlah + kuadratselisih;
                }

                L[i][j] = (1.0/jumlah)*100000000000000.00;
                jumlahL[i] = jumlahL[i] + L[i][j];
            }                 
        }

        double[][] partisiBaru;
        
        partisiBaru = new double[data][clustering];
        
        for(i=0; i<data; i++){
            for(j=0; j<clustering; j++){
                partisiBaru[i][j] = L[i][j]/jumlahL[i];
            }                 
        }
        
        for(i=0; i<data; i++){
            for(j=0; j<clustering; j++){
                matriksPartisi[i][j] = partisiBaru[i][j];
            }                 
        }
        
        //CekMatriksPartisi(matriksPartisi);
        System.out.println("Update Matriks Partisi is Done");
    }
    
    public void setPengelompokan(){
        int i,j;
        double[][] matriksCluster;
        double[][] partisi;
        double[] dataCluster;
        double maks;
        
        matriksCluster = new double[data][clustering];
        partisi = new double[data][clustering];
        dataCluster = new double[data];
        
        for(i=0; i<data; i++){
            for(j=0; j<clustering; j++){
                partisi[i][j] = matriksPartisi[i][j];
            }                 
        }
        
        for(i=0; i<data; i++){
            maks = 0;
            for(j=0; j<clustering; j++){
                if(maks < partisi[i][j]){
                    maks = partisi[i][j];
                    dataCluster[i] = j;
                }
            }                 
        }
        
        for(i=0; i<data; i++){
            for(j=0; j<clustering; j++){
                if(dataCluster[i] == j){
                    matriksCluster[i][j] = 1;
                }
                else{
                    matriksCluster[i][j] = 0;
                }
            }                 
        }
        
        matriksClusterFinal = new double[data][clustering];
        
        for(i=0; i<data; i++){
            for(j=0; j<clustering; j++){
                matriksClusterFinal[i][j] = matriksCluster[i][j];
            }                 
        }
        
        System.out.println("Pengelompokan is Done");
    }
    
    public ArrayList<DataPengelompokan> getDataPengelompokan(){
        int i, j;
        ArrayList<DataPengelompokan> dataPengelompokans;
        DataPengelompokan dataPengelompokan;
        
        dataPengelompokans = new ArrayList<>();
        
        for(i=0; i<data; i++){
            for(j=0; j<clustering; j++){
                if(matriksClusterFinal[i][j] == 1){
                    dataPengelompokan = new DataPengelompokan();
                    dataPengelompokan.setIdKelommpok(i);
                    dataPengelompokan.setKeterangan("C"+String.valueOf(j));
                    dataPengelompokans.add(dataPengelompokan);
                }
            }
        }
        
        return dataPengelompokans;
    }
    
    public boolean isKondisiTerpenuhi(ArrayList<Double> fObjective, int posisi){
        double kondisiTerpenuhi;
        int indexLast = fObjective.size()-1;
        boolean terpenuhi = false;
        
        //System.out.println(posisi);
        if(posisi == 0){
            kondisiTerpenuhi = fObjective.get(indexLast) - 0.0;
        }
        else{
            kondisiTerpenuhi = fObjective.get(indexLast) - fObjective.get(indexLast-1);
        }
       
        if(kondisiTerpenuhi < error){ 
            terpenuhi = true;
        }
        
        return terpenuhi;
    }
    
    /*public void CekMatriksPusatCluster(){
        int i,j;
        
        NumberFormat nf_out = NumberFormat.getNumberInstance(Locale.UK);
        nf_out.setMaximumFractionDigits(4);
        
        System.out.println("Cek Pusat Cluster: ");
        
        for(i=0; i<clustering; i++){
           for(j=0; j<attribut; j++){  
               pusatClusterFCM[i][j] = Double.parseDouble(nf_out.format(pusatClusterFCM[i][j]));
               System.out.print(pusatClusterFCM[i][j]+"  ");
           }            
            System.out.println();
        }
    }*/
    
    /*public void CekMatriksClusterData(){
        int i,j;
        System.out.println("Tampil Data Cluster: ");
        
         for(i=0; i<data; i++){
            for(j=0; j<clustering; j++){
                System.out.print(matriksClusterFinal[i][j]+" ");
            }                 
             System.out.println();
        }
    }8?
    
    /*public void CekMatriksPartisi(double[][] matriksPartisi){
        int i,j;
        System.out.println("Cek Matriks Partisi: ");
        NumberFormat nf_out = NumberFormat.getNumberInstance(Locale.UK);
        nf_out.setMaximumFractionDigits(4);
        
        for(i=0; i<data; i++){
           for(j=0; j<clustering; j++){
               matriksPartisi[i][j] = Double.parseDouble(nf_out.format(matriksPartisi[i][j]));
               System.out.print(matriksPartisi[i][j]+"  ");
           }                 
           System.out.println();
        }
    }*/
    
    /*public void CekKondisiFungsiObjektif(ArrayList<Double> fObjective, int posisi){
        int indexLast = fObjective.size()-1;
        
        System.out.println("Data 2 fungsi Objektif terakhir: ");
        if(posisi == 0){
            System.out.println("FObjektif pertama : "+fObjective.get(indexLast));
            System.out.println("FObjektif kedua : 0");
        }
        else{
            System.out.println("FObjektif pertama : "+fObjective.get(indexLast));
            System.out.println("FObjektif kedua : "+fObjective.get(indexLast-1));
        }
    }*/
    
    public double[] getCompactnessCluster(){
        int i,j,k;
        double[][] matriksPartisiKuadrat;
        double[][] matriksCompCluster;
        double[][] dataWargatemp;
        
        matriksCompCluster = new double[data][attribut];        
        matriksPartisiKuadrat = getMatriksPatrisiKuadrat(matriksPartisi); 
        dataWargatemp = new double[data][attribut];
        
        for(i=0; i<data; i++){
            for(j=0; j<attribut; j++){
                dataWargatemp[i][j] = DataWarga[i][j];
            }
        }
        
        double[] sum = new double[clustering];
        
        for(i=0; i<clustering; i++){
            sum[i] = 0.0;
            for(j=0; j<data; j++){
                for(k=0; k<attribut; k++){
                    matriksCompCluster[j][k] = matriksPartisiKuadrat[j][i]*(Math.pow(Math.abs(pusatClusterFCM[i][k] - dataWargatemp[j][k]), 2));
                }
            }
            
            double[] sumComCluster = new double[attribut];
            
            for(j=0; j<attribut; j++){
                sumComCluster[j] = 0.0;
                for(k=0; k<data; k++){
                    sumComCluster[j] = sumComCluster[j] + matriksCompCluster[k][j];
                }
                sum[i] = sum[i] + sumComCluster[j];
            }
        }
        
        return sum;
    }
    
    public double[][] getPusatClusterTranspose(){
        int i,j;
        
        double[][] pusatClusterTemp;
        double[][] pusatClusterTranspose;
        
        pusatClusterTranspose = new double[attribut][clustering];
        pusatClusterTemp = new double[clustering][attribut];
        
        for(i=0; i<clustering; i++){
            for(j=0; j<attribut; j++){
                pusatClusterTemp[i][j] = pusatClusterFCM[i][j]; 
            }
        }
        
        for(i=0; i<clustering; i++){
            for(j=0; j<attribut; j++){
                pusatClusterTranspose[j][i] = pusatClusterTemp[i][j];
            }
        }
        
        return pusatClusterTranspose; 
    }
    
    public double[] getSeparationCluster(){
        int i,j,k;
        
        double[][] matriksSepartCluster;
        double[][] pusatClusterTranspose;
        ArrayList<double[][]> separationClusters;
        
        pusatClusterTranspose = getPusatClusterTranspose();
        matriksSepartCluster = new double[attribut][attribut];
        
        separationClusters = new ArrayList<>();
        double[] min_value = new double[clustering];
        
        for(i=0; i<clustering; i++){
            for(j=0; j<attribut; j++){
                for(k=0; k<attribut; k++){
                    matriksSepartCluster[j][k] = Math.pow(Math.abs(pusatClusterTranspose[k][i] - pusatClusterFCM[i][k]), 2); 
                }
            }
            
            double min = 1000000000000000000.00;
            
            for(j=0; j<attribut; j++){
                for(k=0; k<attribut; k++){
                    if(min > matriksSepartCluster[j][k]){
                        if(matriksSepartCluster[j][k] != 0){
                            min = matriksSepartCluster[j][k];
                        }
                    }
                }
            }
            
            min_value[i] = min;
        }
        
        return min_value;
    }
    
    public double[] XB(double[] compactnessClusters, double[] separationClusters){
        int i;
        double XB[] = new double[clustering];
        
        for(i=0; i<clustering; i++){
            XB[i] = compactnessClusters[i] / ((double)data*separationClusters[i]);
        }
        
        return XB;
    }
    
    public int[] UrutBesarkeKecil(double[] dataCluster){
        int i, j, k, indexTerbesar;
        double max;
        int[] dataClusterBaru;
        double[] dataClusterTemp;
        ArrayList<Integer> dataClusterBarus;
        
        dataClusterTemp = new double[clustering];
        dataClusterBaru = new int[clustering];
        indexTerbesar = 0;
        
        for(i=0; i<clustering; i++){
            dataClusterTemp[i] = dataCluster[i];
            dataClusterTemp[i] = dataClusterTemp[i]*1000000000000000000000.00;
        }
        
        dataClusterBarus = new ArrayList<>();
        for(i=0; i<clustering; i++){
            max = 0;
            if(dataClusterBarus.isEmpty()){
                for(j=0; j<clustering; j++){
                    if(max < dataClusterTemp[j]){
                        max = dataClusterTemp[j];
                        indexTerbesar = j;
                    }
                }
                
                dataClusterBaru[i] = indexTerbesar;
                dataClusterBarus.add(indexTerbesar);
            }
            else{
                for(j=0; j<clustering; j++){
                    int isAda = 0;
                    for(k=0; k<dataClusterBarus.size(); k++){
                        if(dataClusterBarus.get(k) == j){
                            isAda = 1;
                        }
                    }
                    
                    if(isAda == 0){
                        if(max < dataClusterTemp[j]){
                            max = dataClusterTemp[j];
                            indexTerbesar = j;
                        }
                    }
                }
                
                dataClusterBarus.add(indexTerbesar);
                dataClusterBaru[i] = indexTerbesar;
            }
        }
        
        return dataClusterBaru;
    }
    
    public String[] Pelabelan_FCM(){
        int i,j;
        
        double[] compactnessClusters;
        double[] separationClusters;
        double[] dataLabelCluster;
        int[] indexUrut;
        String[] dataKelayakan;
        
        compactnessClusters = getCompactnessCluster();
        separationClusters = getSeparationCluster();
        dataLabelCluster = XB(compactnessClusters, separationClusters);
        indexUrut = UrutBesarkeKecil(dataLabelCluster);
        dataKelayakan = new String[data];
        
        for(i=0; i<data; i++){
            for(j=0; j<clustering; j++){
                if(matriksClusterFinal[i][j] == 1){
                    if(j == indexUrut[0]){
                        dataKelayakan[i] = "Layak";
                    }
                    else if(j == indexUrut[1]){
                        dataKelayakan[i] = "Kurang Layak";
                    }
                    else if(j == indexUrut[2]){
                        dataKelayakan[i] = "Tidak Layak";
                    }
                } 
            }
        }
        
        return dataKelayakan;    
    }
    
    public void setFCM(){
        int kondisi,i;
        ArrayList<Double> fObjective;
        
        fObjective = new ArrayList<>();
        
        kondisi = 0;
        posisi = 0;
        
        for(i=0; i<maxiter; i++){
            switch (kondisi) {
                case 0:
                    fObjective.add(getFungsiObjektifFCM(kondisi));
                    setUpdateMatriksPartisi();
                    
                    if(isKondisiTerpenuhi(fObjective, i)){
                        kondisi = 2;
                        posisi = i;
                    }
                    else{
                        kondisi = 1; 
                    }
                    
                    //System.out.println(kondisi);
                    break;
                case 1:
                    isSendedPartisi = true;
                    fObjective.add(getFungsiObjektifFCM(kondisi));
                    setUpdateMatriksPartisi();
                    
                    if(isKondisiTerpenuhi(fObjective, i)){ 
                        kondisi = 2;
                        posisi = i;
                    }
                    //System.out.println(kondisi);
                    
                    break;
                default:
                    i = maxiter;
                    break;
            }
        }
        //System.out.println("FCM berhenti pada iterasi ke-"+posisi);
        
        setPengelompokan();
        //CekKondisiFungsiObjektif(fObjective, posisi); 
        //CekMatriksClusterData();
        dataKelayakan = Pelabelan_FCM();
    }
    
    public String[] getFCM(){
        return dataKelayakan;
    }
    
    public int getMaxIterFCM(){
        return posisi;
    }
}
