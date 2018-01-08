package com.test.navigationdrawer1.REST.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by osvaldo on 1/7/18.
 */

public class HueUserData {
    @SerializedName("latitud")
    public String latitud = "";

    @SerializedName("longitud")
    public String longitud = "";

    @SerializedName("id_usr_registro_hue")
    public int idUsrRegistroHue = 0;

    @SerializedName("id_evento_hue")
    public int idEventoHue;

    @SerializedName("id_edo_usr_hue")
    public int idEdoUsrHue;

    @SerializedName("id")
    public int id;

    @SerializedName("nombre")
    public String nombre;

    @SerializedName("correo_electronico")
    public String email;
}