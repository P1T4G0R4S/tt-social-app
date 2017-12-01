package com.test.navigationdrawer1.REST.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by osvaldo on 10/25/17.
 */

public class ReporteIncidente {
    @SerializedName("fecha")
    public String fecha;

    @SerializedName("hora")
    public String hora;

    @SerializedName("descripcion")
    public String descripcion;

    @SerializedName("imagen_reporte")
    public String imagenReporte = "";

    @SerializedName("latitud")
    public String latitud;

    @SerializedName("longitud")
    public String longitud;

    @SerializedName("id_usuario_ri")
    public int idUsuarioRI;

    @SerializedName("id_edo_reporte_ri")
    public int idEdoReporteRI;

    @SerializedName("id_sismo_registrado_ri")
    public int idSismoRegistradoRI;

    @SerializedName("id")
    public int id;
}
