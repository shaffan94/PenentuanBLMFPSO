/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author USER
 */
public class DataPengelompokan {
    int id_kelompok;
    String nama;
    String keterangan;
    
    public DataPengelompokan(){}
    
    public void setIdKelommpok(int id_kelompok){
        this.id_kelompok = id_kelompok;
    }
    
    public void setNama(String nama){
        this.nama = nama;
    }
    
    public void setKeterangan(String keterangan){
        this.keterangan = keterangan;
    }
    
    public int getIdKelommpok(){
        return id_kelompok;
    }
    
    public String getNama(){
        return nama;
    }
    
    public String getKeterangan(){
        return keterangan;
    }
}
