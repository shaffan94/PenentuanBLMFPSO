/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Kontroller;

import Model.DataPengelompokan;
import java.util.ArrayList;

/**
 *
 * @author USER
 */
public class FPSO_FCM {
    private final double r1;
    private final double r2;
    private final int c1;
    private final int c2;
    private final int w;
    private int posisi;
    private final int maxiter;
    private final int nPartikel;
    private double fitnessGbest;
    private double[][] V;
    private double[][] X;
    private double[][] DataWarga;
    private double[][] PBest;
    private double[][] matriksPartisi;
    private double[] fitness;
    double[][] pusatClusterFCM;
    private int data;
    private final int clustering;
    private int attribut;
    private ArrayList<double[][]> Partikel;
    private double[][] GBest;
    private ArrayList<ArrayList> Partikels;
    private FCM fcm;
    private String[] dataKelayakan;
    
    public FPSO_FCM(double r1, double r2, int c1, int c2, int w, int maxiter, int nPartikel){
        this.r1 = r1;
        this.r2 = r2;
        this.c1 = c1;
        this.c2 = c2;
        this.w = w;
        this.maxiter = maxiter;
        clustering = 3;
        this.nPartikel = nPartikel;
    }
    
    public void inisialisasiData(double[][] dataWarga){
        int i,j;
        DataWarga = new double[dataWarga.length][dataWarga[0].length];
        
        for(i=0; i<dataWarga.length; i++){
            for(j=0; j<dataWarga[0].length; j++){
                DataWarga[i][j] = dataWarga[i][j];
            }
        }
        
        data = DataWarga.length;
        attribut = DataWarga[0].length;
        
        System.out.println("data : "+data);
        System.out.println("Inisialisasi Data is Done");
    }
    
    public ArrayList<double[][]> bentukPartikel(){
        int i,j;
    
        V = new double[data][clustering];
        X = new double[data][clustering];
        PBest = new double[data][clustering];
        
        for(i=0; i<data; i++){
            for(j=0; j<clustering; j++){
                V[i][j] = Math.random();
            }
        }
        
        for(i=0; i<data; i++){
            for(j=0; j<clustering; j++){
                X[i][j] = Math.random();
            }
        }
        
        PBest = X;
        Partikel = new ArrayList<>();
        
        Partikel.add(V);
        Partikel.add(X);
        Partikel.add(PBest);
        
        return Partikel;
    }
    
    public void insialisasiP(){
        int i;
        
        Partikels = new ArrayList<>();
        
        for(i=0; i<nPartikel; i++){
            Partikels.add(bentukPartikel());
        }
        
        System.out.println("Inisialisasi Partikel is Done");
    }
    
    public void normalisasiP(){
        int i,j,k;
        double sum;
        double[][] posisiPartikel;
        
        for(k=0; k<Partikels.size(); k++){
            posisiPartikel = (double[][])Partikels.get(k).get(1);
            for(i=0; i<data; i++){
                sum = 0.0;
                for(j=0; j<clustering; j++){
                    sum = sum + posisiPartikel[i][j]; 
                }

                for(j=0; j<clustering; j++){
                    posisiPartikel[i][j] = posisiPartikel[i][j]/sum;
                    //System.out.println(posisiPartikel[i][j]);
                }
                
                //System.out.println();
            }
            Partikels.get(k).remove(1);
            Partikels.get(k).add(1, posisiPartikel);
        }
        
        System.out.println("Normalisasi Posisi Partikel is Done");
    }    
    
    public void hitungFitness(){
        int i,j,k,l;
        double[][] posisiPartikel;
        double[][] pusatCluster;
        double fitnessTerbesar;
        int index_terbesar;
        
        ArrayList<double[][]> posisiPartikelP2;
        
        posisiPartikelP2 = new ArrayList<>();
        for(k=0; k<Partikels.size(); k++){
            posisiPartikel = (double[][])Partikels.get(k).get(1);
            for(i=0; i<data; i++){
                for(j=0; j<clustering; j++){
                    posisiPartikel[i][j] = Math.pow(posisiPartikel[i][j], 2);
                }
            }
           
            posisiPartikelP2.add(posisiPartikel);
        }
        
        double[][] sum = new double[Partikels.size()][clustering];
        
        for(i=0; i<Partikels.size(); i++){
            for(j=0; j<clustering; j++){
                sum[i][j] = 0.0;
            }
        }
        
        for(i=0; i<Partikels.size(); i++){
            posisiPartikel = (double[][])posisiPartikelP2.get(i);
            for(j=0; j<clustering; j++){
                for(k=0; k<data; k++){
                    sum[i][j] = sum[i][j]+ posisiPartikel[k][j];
                }
            }
        }
        
        double[][] dataWargatemp;
        ArrayList<double[][]> pusatClusterPartikels;
        
        double[][] cross = new double[data][attribut];
        pusatCluster = new double[clustering][attribut];
        double[] sumCluster = new double[attribut];
        dataWargatemp = DataWarga;
        
        pusatClusterPartikels = new ArrayList<>();
        
        for(i=0; i<attribut; i++){
            sumCluster[i] = 0.0;
        }
        
        for(i=0; i<Partikels.size(); i++){
            posisiPartikel = (double[][])posisiPartikelP2.get(i);
            for(j=0; j<clustering; j++){
                for(k=0; k<attribut; k++){
                    for(l=0; l<data; l++){
                        //melakukan perkalian antara datawarga dan partikel yang dikuadratkan
                        cross[l][k] = dataWargatemp[l][k] * posisiPartikel[l][j];
                        sumCluster[k] = sumCluster[k] + cross[l][k];
                    }   
                    pusatCluster[j][k] =  sumCluster[k]/sum[i][j];
                }                
            }            
            pusatClusterPartikels.add(pusatCluster);
        }
        
        double[] L;
        double jumlah = 0;
        double selisih;
        double kuadratselisih;
        double[] jumlahL;
        
        L = new double[clustering];
        fitness = new double[Partikels.size()];
        jumlahL = new double[data];
        
        for(i=0; i<data; i++){
            jumlahL[i] = 0.0;
        }
        
        for(l=0; l<Partikels.size(); l++){
            posisiPartikel = (double[][])posisiPartikelP2.get(l);
            
            for(i=0; i<data; i++){
                for(j=0; j<clustering; j++){
                    jumlah = 0;
                    
                    for(k=0; k<attribut; k++){
                        selisih = dataWargatemp[i][k] - pusatCluster[j][k];
                        kuadratselisih = Math.pow(selisih, 2);

                        jumlah = jumlah + kuadratselisih;
                    }
                    
                    L[j] = jumlah * posisiPartikel[i][j];
                    jumlahL[i] = jumlahL[i] + L[j];
                }                 
            }
            
            jumlah = 0.0;
            for(i=0; i<data; i++){
                jumlah = jumlah + jumlahL[i];
            }
            
            fitness[l] = 1.0/jumlah;
        }
        
        fitnessTerbesar = 0;
        index_terbesar = 0;
        for(i=0; i<Partikels.size(); i++){
            if(fitnessTerbesar < fitness[i]){
                fitnessTerbesar = fitness[i];
                index_terbesar = i;
                fitnessGbest = fitnessTerbesar;
            }
        }
        GBest = new double[data][clustering];
        double[][] GbestTemp;
        
        GbestTemp = (double[][])Partikels.get(index_terbesar).get(1);
        for(i=0; i<data; i++){
            for(j=0; j<clustering; j++){
                GBest[i][j] = GbestTemp[i][j];
            }
        }
        //
        System.out.println("Menghitung ftness dan menentukan Gbest is Done");
    }
    
    public void updatePartikel(){
        int i,j,k,l;
        double kali_cr1, kali_cr2;
        ArrayList<double[][]> partikel;
        double[][] PartikelPBest;
        double[][] PartikelX;
        double[][] PartikelV;
        double[][] PartikeltempPX;
        double[][] PartikeltempGX;
        double[][] PartikeltempWT;
        double[][] UpdateV;
        double[][] UpdateX;
        double[] jumlah;
        
        kali_cr1 = c1*r1;
        kali_cr2 = c2*r2;
        
        jumlah = new double[data];
        
        for(i=0; i<data; i++){
            jumlah[i] = 0.0;
        }
        
        for(i=0; i<Partikels.size() ;i++){
            partikel = Partikels.get(i);
            
            PartikelPBest = (double[][])partikel.get(2);
            PartikelX = (double[][])partikel.get(1);
            PartikelV = (double[][])partikel.get(0);
            PartikeltempPX = new double[data][clustering];
            PartikeltempGX = new double[data][clustering];
            PartikeltempWT = new double[data][clustering];
            UpdateV = new double[data][clustering];
            UpdateX = new double[data][clustering];

            for(k=0; k<data; k++){
                for(l=0; l<clustering; l++){
                    PartikeltempPX[k][l]  = PartikelPBest[k][l]-PartikelX[k][l];
                    PartikeltempPX[k][l] = PartikeltempPX[k][l]*kali_cr1;

                    PartikeltempGX[k][l]  = GBest[k][l]-PartikelX[k][l];
                    PartikeltempGX[k][l]  = PartikeltempGX[k][l]*kali_cr2;

                    PartikeltempWT[k][l] = PartikelV[k][l]*w;

                    UpdateV[k][l] = PartikeltempPX[k][l] + PartikeltempGX[k][l] + PartikeltempWT[k][l];
                    UpdateX[k][l] = UpdateV[k][l] + PartikelX[k][l];

                    jumlah[k] = jumlah[k]+UpdateX[k][l];
                }
                Partikels.get(i).remove(0);
                Partikels.get(i).add(0, UpdateV);
            }

            for(k=0; k<data; k++){
                for(l=0; l<clustering; l++){
                    UpdateX[k][l] = UpdateX[k][l]/jumlah[k];
                }
            }
            
            Partikels.get(i).remove(1);
            Partikels.get(i).add(1, UpdateX);
        }
        System.out.println("Update Partikels is Done");
    }
    
    public void HitungOptimasiGBest(){
        int i,j,k;
        double[] fitnessLama;
        double[] fitnessbaru;
        double[][] Pbestbaru;
        double[][] Pbesttemp;
        
        fitnessLama = new double[2];
        for(i=0; i<Partikels.size(); i++){
            fitnessLama[i] = fitness[i];
        }
        
        hitungFitness();
        
        Pbestbaru = new double[data][clustering];
        
        for(i=0; i<Partikels.size(); i++){
            if(fitness[i] > fitnessLama[i]){
                Partikels.get(i).remove(2);
                Pbesttemp = (double[][])Partikels.get(i).get(1);
                
                for(j=0; j<data; j++){
                    for(k=0; k<clustering; k++){
                        Pbestbaru[j][k] = Pbesttemp[j][k];
                    }
                }
                
                Partikels.get(i).add(2, Pbestbaru);
            }
        }
        
        updatePartikel();
    }
    
    public double[][] getMatriksPatrisi(){
        int i,j;
        double[][] partisi;
        partisi = new double[data][clustering];
        
        for(i=0; i<data; i++){
            for(j=0; j<clustering; j++){
                partisi[i][j] = GBest[i][j];
            }
        }
        
        return partisi;
    }
    
    public ArrayList<DataPengelompokan> getDataPengelompokan(){
        return fcm.getDataPengelompokan();
    }
    
    public void setFPSO_FCM(){
        int i;
        
        fcm = new FCM(100, 0.0000005, 3);
        fcm.inisialisasiData(DataWarga);
        
        insialisasiP();
        normalisasiP();
        
        for(i=0; i<maxiter; i++){
            if(i==0){
                hitungFitness();
                updatePartikel();
                
                //System.out.println("fitness Gbest itrasi ke-"+i+": "+ fitnessGbest);
                
                if(fitnessGbest > 0.00000001){
                    System.out.println("Kondisi terpenuh pada iterasi ke-"+i);
                    i=maxiter;
                }
            }
            else{
                HitungOptimasiGBest();
                
                //System.out.println("fitness Gbest itrasi ke-"+i+": "+ fitnessGbest);
                
                if(fitnessGbest > 0.00000001){
                    System.out.println("Kondisi terpenuh pada iterasi ke-"+i);
                    i=maxiter;
                }
            }
        }
        
        System.out.println();
        
        fcm.setMatriksPatrisi(GBest);
        fcm.setFCM();
        posisi  = fcm.getMaxIterFCM();
        dataKelayakan = fcm.getFCM();
    }
    
    public String[] getFPSO_FCM(){
        return dataKelayakan;
    }
    
    public int getMaxIterasi(){
        return posisi;
    }
}
