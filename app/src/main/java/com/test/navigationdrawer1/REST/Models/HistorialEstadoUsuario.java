package com.test.navigationdrawer1.REST.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by osvaldo on 10/25/17.
 */

public class HistorialEstadoUsuario {
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
}
