const API = 'http://localhost:8080/apis';

document.getElementById("uploadForm").addEventListener("submit", function(e) {
  e.preventDefault();
  const form = e.target;
  const dados = new FormData(form);

  fetch(API + "/music-upload", {
    method: "POST",
    body: dados
  })
  .then(res => res.text())
  .then(msg => {
    alert(msg);
    form.reset();
  })
  .catch(() => alert("Erro ao enviar música"));
});

function buscarMusicas()
{
  const chave = document.getElementById("searchInput").value;
  if(chave.trim() === "")
    {
    alert("Digite uma palavra-chave");
    return;
  }

  fetch(API + "/find-musics?chave=" + encodeURIComponent(chave))
    .then(res => res.json())
    .then(dados => mostrarMusicas(dados))
    .catch(() => alert("Nenhuma música encontrada"));
}

function listarMusicas()
{
  fetch(API + "/list-musics")
    .then(res => res.json())
    .then(dados => mostrarMusicas(dados))
    .catch(() => alert("Erro ao listar músicas"));
}

function mostrarMusicas(musicas)
{
    const div = document.getElementById("listaMusicas");
    div.innerHTML = "";
  
    musicas.forEach(musica => {
      const bloco = document.createElement("div");
      bloco.className = "music-card";
      bloco.innerHTML = `
        <strong>${musica.titulo}</strong><br>
        Artista: ${musica.artista}<br>
        Estilo: ${musica.estilo}<br>
        <audio controls preload="none">
          <source src="${musica.filename}" type="audio/mpeg">
          Seu navegador não suporta áudio.
        </audio>
      `;
      div.appendChild(bloco);
    });
}
  
  