package com.test.navigationdrawer1.REST;

import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;
import com.test.navigationdrawer1.REST.Models.CatEdoReporte;
import com.test.navigationdrawer1.REST.Models.HistorialEstadoUsuario;
import com.test.navigationdrawer1.REST.Models.ReporteIncidente;
import com.test.navigationdrawer1.REST.Models.Usuario;

import org.json.JSONObject;

/**
 * Created by osvaldo on 10/25/17.
 */

public class WebApi {
    private final String BaseUrl = "https://floating-sands-67659.herokuapp.com/";//"http://192.168.1.75:3000/";
    private final Activity parent;

    public IHttpResponseMethods responseMethods;

    public WebApi(Activity parent) {
        this.parent = parent;
    }

    //region cat_edo_reportes
    public void CreateCatEdoReporte(CatEdoReporte catEdoReporte) {
        String url = BaseUrl + "api/cat_edo_reportes";

        JSONObject postDataParams = new JSONObject();
        try {
            postDataParams.put("nombre_estado_reporte", catEdoReporte.edoReporte);
            new HttpPostRequest(parent).execute(url, responseMethods, postDataParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void QueryCatEdoReportes(String query) {
        String url = BaseUrl + "api/cat_edo_reportes?filter=%7B%22where%22%3A%22"+ query + "%22%7D";

        new HttpGetRequest(parent).execute(url, responseMethods);
    }
    //endregion

    //region reporte_incidentes
    public void QueryAllReporteIncidente() {
        String url = BaseUrl + "api/reporte_incidentes";

        new HttpGetRequest(parent).execute(url, responseMethods);
    }

    public void CreateReporteIncidente(ReporteIncidente reporteIncidente) {
        String url = BaseUrl + "api/reporte_incidentes";

        JSONObject postDataParams = new JSONObject();
        try {
            postDataParams.put("fecha", reporteIncidente.fecha);
            postDataParams.put("hora", reporteIncidente.hora);
            postDataParams.put("descripcion", reporteIncidente.descripcion);
            postDataParams.put("latitud", reporteIncidente.latitud);
            postDataParams.put("longitud", reporteIncidente.longitud);
            postDataParams.put("id_usuario_ri", reporteIncidente.idUsuarioRI);
            postDataParams.put("id_edo_reporte_ri", reporteIncidente.idEdoReporteRI);

            if (!reporteIncidente.imagenReporte.equals("")) {
                postDataParams.put("imagen_reporte", reporteIncidente.imagenReporte);
            }
            //if (reporteIncidente.idSismoRegistradoRI != 0) {
                postDataParams.put("id_sismo_registrado_ri", reporteIncidente.idSismoRegistradoRI);
            //}

            new HttpPostRequest(parent).execute(url, responseMethods, postDataParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UploadImageReporteIncidente(String imagenReporte) {
        String url = BaseUrl + "api/usuarios/uploadImage";

        JSONObject postDataParams = new JSONObject();
        try {
            postDataParams.put("image", imagenReporte);

            new HttpPostRequest(parent).execute(url, responseMethods, postDataParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region historial_estado_usuarios
    public void CreateHistorialEstadoUsuarios(HistorialEstadoUsuario estadoUsuario) {
        String url = BaseUrl + "api/historial_estado_usuarios";

        JSONObject postDataParams = new JSONObject();
        try {
            postDataParams.put("id", estadoUsuario.id);
            postDataParams.put("id_evento_hue", estadoUsuario.idEventoHue);
            postDataParams.put("id_edo_usr_hue", estadoUsuario.idEdoUsrHue);

            if (!estadoUsuario.latitud.equals("")) {
                postDataParams.put("latitud", estadoUsuario.latitud);
            }
            if (!estadoUsuario.longitud.equals("")) {
                postDataParams.put("longitud", estadoUsuario.longitud);
            }
            if (estadoUsuario.idUsrRegistroHue != 0) {
                postDataParams.put("id_usr_registro_hue", estadoUsuario.idUsrRegistroHue);
            }

            new HttpPostRequest(parent).execute(url, responseMethods, postDataParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void QueryHistorialEstadoUsuariosByLocation(String lat, String lon) {
        String location = "?lat=" + lat + "&lng=" + lon;
        String url = BaseUrl + "api/getUserDataWithLocation";

        new HttpGetRequest(parent).execute(url, responseMethods);
    }

    public void QueryAllHistorialEstadoUsuarios() {
        String url = BaseUrl + "api/historial_estado_usuarios";

        new HttpGetRequest(parent).execute(url, responseMethods);
    }
    //endregion

    //region cat_edo_usr
    public void QueryCatEdoUsuarios(String query) {
        String url = BaseUrl + "api/cat_edo_usr?filter=%7B%22where%22%3A%22"+ query + "%22%7D";

        new HttpGetRequest(parent).execute(url, responseMethods);
    }
    //endregion

    //region usuario
    public void CreateUsuario(Usuario usuario) {
        String url = BaseUrl + "api/usuarios";

        JSONObject postDataParams = new JSONObject();
        try {
            postDataParams.put("nombre", usuario.nombre);
            postDataParams.put("correo_electronico", usuario.correoElectronico);
            postDataParams.put("contrasena", usuario.contrasena);

            new HttpPostRequest(parent).execute(url, responseMethods, postDataParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void QueryUsuario(Usuario usuario) {
        String url = BaseUrl + "api/usuarios?filter=%7B%22where%22%3A%7B%22correo_electronico%22%3A%22" 
            + usuario.correoElectronico + "%22%2C%22contrasena%22%3A%22" 
            + usuario.contrasena + "%22%7D%7D";

        new HttpGetRequest(parent).execute(url, responseMethods);
    }
    //endregion
}