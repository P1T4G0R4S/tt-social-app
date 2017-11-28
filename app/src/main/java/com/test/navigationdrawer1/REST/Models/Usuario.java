package com.test.navigationdrawer1.REST.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by osvaldo on 10/25/17.
 */

public class Usuario {
    @SerializedName("rol_app")
    public String rolApp;
    
    @SerializedName("profesion")
    public String profesion;
    
    @SerializedName("genero")
    public String genero;
    
    @SerializedName("contrasena")
    public String contrasena;
    
    @SerializedName("correo_electronico")
    public String correoElectronico;
    
    @SerializedName("celular")
    public String celular;

    @SerializedName("nombre")
    public String nombre;

    @SerializedName("id")
    public String id;
}