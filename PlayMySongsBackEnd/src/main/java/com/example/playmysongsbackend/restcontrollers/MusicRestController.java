package com.example.playmysongsbackend.restcontrollers;

import com.example.playmysongsbackend.entities.Erro;
import com.example.playmysongsbackend.entities.Musica;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "apis")
public class MusicRestController {
    private final String UPLOAD_FOLDER = "src/main/resources/static/uploads/";

    @Autowired
    private HttpServletRequest request;

    @PostMapping(value="music-upload")
    public ResponseEntity<Object> musicUpload(@RequestParam("titulo") String titulo,
                                              @RequestParam("artista") String artista,
                                              @RequestParam("estilo") String estilo,
                                              @RequestParam("file") MultipartFile file){
        String fileName="";
        try {
            //cria uma pasta na área estática para acomodar os arquivos recebidos, caso não exista
            File uploadFolder = new File(UPLOAD_FOLDER);
            if (!uploadFolder.exists()) uploadFolder.mkdir();
            //montar o nome do arquivo
            fileName=file.getOriginalFilename();
            file.transferTo(new File(uploadFolder.getAbsolutePath() + "\\"+file.getOriginalFilename()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro ao armazenar o arquivo." + e.getMessage()));
        }
        Musica musica=new Musica(titulo,artista,estilo,fileName);
        return ResponseEntity.ok( musica);
    }
    @GetMapping(value="find-musics")
    public ResponseEntity<Object> findMusics(@RequestParam("chave") String chave){
        File files=new File(UPLOAD_FOLDER);
        List<Musica> musicaList=new ArrayList<>();
        String[] arquivos = files.list();
        for(String arq : arquivos)
        {
            if (arq.toUpperCase().contains(chave.toUpperCase())) {
                musicaList.add(new Musica("titulo","artista","estilo",getHostStatic()+"/"+arq));
            }
        }
        if (musicaList.isEmpty())
            return ResponseEntity.badRequest().body(new Erro("Nenuma música encontrada"));
        else
            return ResponseEntity.ok(musicaList);
    }
    /* retorna a url da pasta static/uploads*/
    public String getHostStatic() {
        //request deve ser declarado do tipo HttpServletRequest como atributo @Autowired
        return "http://"+request.getServerName().toString()+":"+request.getServerPort()+"/uploads/";
    }
}
