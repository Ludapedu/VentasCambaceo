package com.upiicsa.cambaceo.ImageSaver;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by luis.perez on 21/10/2016.
 */

public class ImageSaver {
    public String Guardar(Bitmap bitmap, String Nombre)
    {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/imagenes");
        if(!myDir.exists()) {
            myDir.mkdirs();
        }
        String fname = "Imagen-"+ Nombre +".jpg";
        String rutaarchivo = root + "/imagenes/" + fname;
        File file = new File (myDir, fname);
        if (file.exists ())
        {
            file.delete ();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rutaarchivo;
    }

    public Bitmap ObtenerImagen (String rutaimagen)
    {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(rutaimagen);
        }catch (Exception e)
        {
            Log.d("Error", e.toString());
            return null;
        }
        return bitmap;
    }
}
