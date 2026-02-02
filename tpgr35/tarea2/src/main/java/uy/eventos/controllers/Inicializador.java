package uy.eventos.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class Inicializador implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// ejecuta al levantar el servidor
		ServletContext context = sce.getServletContext();
		
		// creación o carga de archivo webapp.properties
		Properties propiedades = new Properties();
		String home = System.getProperty("user.home");
		File carpeta = new File(home, ".eventosuy");
		File archivo = new File(carpeta, "webapp.properties");
		if (carpeta.exists() && archivo.exists()) {
			try (FileInputStream input = new FileInputStream(archivo)) {
				propiedades.load(input);
			} catch (IOException e) {
				System.out.println("Error al leer webapp.properties: \n");
				e.printStackTrace();
			}
		} else if (!carpeta.exists()) carpeta.mkdirs();
		
		
		if (propiedades.getProperty("url_ws_eventos") == null) propiedades.setProperty("url_ws_eventos", "http://localhost:9128/evtuy/eventos");
		if (propiedades.getProperty("url_ws_usuarios") == null) propiedades.setProperty("url_ws_usuarios", "http://localhost:9128/evtuy/usuarios");
		
		try (FileOutputStream output = new FileOutputStream(archivo)) {
			propiedades.store(output, "Configuración de web services a consumir");
		} catch (FileNotFoundException e) {
			System.out.println("Archivo " + archivo.getName() + " no encontrado");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error al crear webapp.properties");
			e.printStackTrace();
		}
		
		context.setAttribute("propiedades", propiedades);
		
	}
}
