package Api.RoquiGames.RoquiGames.Configuracion;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class ImagenesConfig implements WebMvcConfigurer {

    @Value("${roqui.imagenes.ruta}")
    private String rutaImagenes;

    @PostConstruct
    public void crearCarpeta() throws IOException {
        Files.createDirectories(Paths.get(rutaImagenes));
    }

    // Lo que pidan en /imagenes/** se busca en la carpeta de imágenes
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String ubicacion = Paths.get(rutaImagenes).toAbsolutePath().toUri().toString();

        if (!ubicacion.endsWith("/")) {
            ubicacion = ubicacion + "/";
        }

        registry.addResourceHandler("/imagenes/**")
                .addResourceLocations(ubicacion);
    }
}
