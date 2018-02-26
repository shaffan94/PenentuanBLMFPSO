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
public class DataKelayakan {
    int id;
    String nama;
    String jenis_kelamin;
    String alamat;
    String pekerjaan;
    double usia;
    String tunjangan;
    
    public DataKelayakan(){}
    
    public void setId(int id){
        this.id = id;
    }
    
    public void setNama(String nama){
        this.nama = nama;
    }
    
    public void setJenisKelamin(String jenis_kelamin){
        this.jenis_kelamin = jenis_kelamin;
    }
    
    public void setAlamat(String alamat){
        this.alamat = alamat;
    }
    
    public void setPekerjaan(String pekerjaan){
        this.pekerjaan = pekerjaan;
    }
    
    public void setUsia(double usia){
        this.usia = usia;
    }
    
    public void setTunjangan(String tunjangan){
        this.tunjangan = tunjangan;
    }
    
    public int getId(){
        return id;
    }
    
    public String getNama(){
        return nama;
    }
    
    public String getJenisKelamin(){
        return jenis_kelamin;
    }
    
    public String getAlamat(){
        return alamat;
    }
    
    public String getPekerjaan(){
        return pekerjaan;
    }
    
    public double getUsia(){
        return usia;
    }
    
    public String getTunjangan(){
        return tunjangan;
    }
    
    
}
