package com.test.navigationdrawer1.REST.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by osvaldo on 10/25/17.
 */

public class CatEdoReporte {
    @SerializedName("nombre_estado_reporte")
    public String edoReporte;

    @SerializedName("id")
    public int id;

    @Override
    public String toString() {
        return edoReporte;
    }
}
