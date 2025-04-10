package com.example.playmysongsbackend.restcontrollers;

import com.example.playmysongsbackend.entities.Erro;
import com.example.playmysongsbackend.entities.Musica;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "apis")
public class MusicRestController {

    final String UPLOAD_FOLDER = "src/main/resources/static/uploads/";

    @Autowired
    private HttpServletRequest request;

    @CrossOrigin(origins = "*")
    @PostMapping(value = "music-upload", consumes = "multipart/form-data")
    public ResponseEntity<Object> musicUpload(@RequestParam("titulo") String titulo,
                                              @RequestParam("artista") String artista,
                                              @RequestParam("estilo") String estilo,
                                              @RequestParam("file") MultipartFile file) {

        try {
            File uploadFolder = new File(UPLOAD_FOLDER);
            if (!uploadFolder.exists()) uploadFolder.mkdirs(); // cria todos os diretórios se não existirem

            // Sanitiza os dados para o nome do arquivo
            String sanitizedTitulo = titulo.replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll(" ", "-");
            String sanitizedArtista = artista.replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll(" ", "-");
            String sanitizedEstilo = estilo.replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll(" ", "-");

            String fileName = sanitizedTitulo + "_" + sanitizedArtista + "_" + sanitizedEstilo + ".mp3";

            file.transferTo(Paths.get(uploadFolder.getAbsolutePath(), fileName));
            System.out.println("Título: " + titulo + "\tArtista: " + artista + "\tEstilo: " + estilo);

            String fileUrl = "http://localhost:8080/uploads/" + fileName;
            Musica musica = new Musica(titulo, artista, estilo, fileUrl);

            return ResponseEntity.ok("Arquivo recebido com sucesso");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro ao armazenar o arquivo. " + e.getMessage()));
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "find-musics")
    public ResponseEntity<Object> findMusics(@RequestParam("chave") String chave) {
        File files = new File(UPLOAD_FOLDER);
        List<Musica> musicaList = new ArrayList<>();
        String[] arquivos = files.list();

        if (arquivos == null) {
            return ResponseEntity.badRequest().body(new Erro("Nenhum arquivo encontrado"));
        }

        for (String arq : arquivos) {
            if (arq.endsWith(".mp3") && arq.toUpperCase().contains(chave.toUpperCase())) {
                String[] separarString = arq.replace(".mp3", "").split("_");
                if (separarString.length == 3) {
                    String fileName = arq;
                    musicaList.add(new Musica(separarString[0], separarString[1], separarString[2], getHostStatic() + fileName));
                }
            }
        }

        if (musicaList.isEmpty()) {
            return ResponseEntity.badRequest().body(new Erro("Nenhuma música encontrada"));
        } else {
            return ResponseEntity.ok(musicaList);
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("list-musics")
    public ResponseEntity<Object> listMusics() {
        File files = new File(UPLOAD_FOLDER);
        List<Musica> musicaList = new ArrayList<>();
        String[] arquivos = files.list();

        if (arquivos == null) {
            return ResponseEntity.badRequest().body(new Erro("Nenhum arquivo encontrado"));
        }

        for (String arq : arquivos) {
            if (arq.toLowerCase().endsWith(".mp3")) {
                String[] separarString = arq.replace(".mp3", "").split("_");
                if (separarString.length == 3) {
                    String fileName = arq;
                    musicaList.add(new Musica(separarString[0], separarString[1], separarString[2], getHostStatic() + fileName));
                }
            }
        }

        if (musicaList.isEmpty()) {
            return ResponseEntity.badRequest().body(new Erro("Nenhuma música encontrada"));
        } else {
            return ResponseEntity.ok(musicaList);
        }
    }

    /* retorna a url da pasta static/uploads */
    public String getHostStatic() {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + "/uploads/";
    }
}