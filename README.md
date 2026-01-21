# 👑 Throne: AI Kingdom

> Um jogo de estratégia medieval baseado em cartas, onde suas decisões moldam o destino do reino. Desenvolvido com **Java**, **Firebase** e narrativa gerada por **Inteligência Artificial (Gemini)**.

[![Website](https://img.shields.io/badge/Website-Acesse%20o%20Site-181717?style=flat&logo=github&logoColor=white)](https://gabrielbarbosaafo.github.io/AndroidReings/)
<br>

![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=flat&logo=android)
![Java](https://img.shields.io/badge/Language-Java-ED8B00?style=flat&logo=java)
![Firebase](https://img.shields.io/badge/Backend-Firebase-FFCA28?style=flat&logo=firebase)
![Gemini AI](https://img.shields.io/badge/AI-Google%20Gemini-8E75B2?style=flat&logo=google)

## 📸 Screenshots

<div align="center">
  <table>
    <tr>
      <td align="center">
        <img src="https://github.com/user-attachments/assets/55c348bd-e5c4-4c62-bc3d-1feb87dcbb89" width="200px;" alt="Login"/><br>
        <sub><b>1. Início e Login</b></sub>
      </td>
      <td align="center">
        <img src="https://github.com/user-attachments/assets/5210d462-e8be-4402-a236-8a4ec089b0d5" width="200px;" alt="Configuração API"/><br>
        <sub><b>2. Configuração (API)</b></sub>
      </td>
      <td align="center">
        <img src="https://github.com/user-attachments/assets/48b85312-4807-4444-b793-ca99f851ac64" width="200px;" alt="Contexto"/><br>
        <sub><b>3. Contexto da História</b></sub>
      </td>
    </tr>
  </table>


  <img src="https://github.com/user-attachments/assets/75d3038d-1d64-4922-991c-6c3d3241c2fb" width="400px;" alt="Gameplay"/><br>
    <sub><b>4. Jogo (Gameplay)</b></sub>
</div>

## 📖 Sobre o Projeto

**Throne** é um jogo inspirado na mecânica clássica de "swipe" (deslizar cartas). O jogador assume o papel de um monarca e deve tomar decisões difíceis para manter o equilíbrio de quatro pilares essenciais do reino:
1.  ⛪ **Religião**
2.  👥 **Povo**
3.  ⚔️ **Exército**
4.  💰 **Tesouro**

Se qualquer um desses recursos chegar a 0 ou 100, o rei morre e o reinado acaba.

### ✨ Diferencial: Narrativa Infinita com IA
Diferente de jogos tradicionais com histórias fixas, o **Throne** utiliza a API do **Google Gemini** para gerar cenários, personagens e consequências em tempo real. Cada reinado é único!

---

## 🎨 Design & Atmosfera (IA)

Este projeto explora o potencial da IA Generativa em todas as frentes. Além da história, todos os ativos visuais e sonoros foram sintetizados digitalmente:

* 🖼️ **Imagens das Cartas:** Geradas por IA, criando um estilo medieval consistente para personagens e cenários.
* 🎵 **Trilha Sonora & Efeitos:** Músicas de fundo e efeitos sonoros compostos por Inteligência Artificial para garantir imersão total.
* 🤖 **Game Master:** O Google Gemini atua como o "Mestre de RPG", criando os dilemas e calculando as consequências.

---

## 📱 Funcionalidades

* **Mecânica de Swipe:** Deslize para a direita (Sim) ou esquerda (Não) para tomar decisões.
* **Ranking em Tempo Real:** Integração com **Firebase Firestore** para listar os reinados mais longos globalmente.
* **Autenticação na Nuvem:** Sistema de Login e Registro de usuários via Firebase Auth.
* **Contexto Personalizável:** O jogador pode definir o cenário (ex: "Reino Futurista", "Idade Média", "Apocalipse Zumbi") e a IA adapta a história.
* **Design Imersivo:** Interface temática com feedbacks visuais e sonoros.

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java (Android Nativo)
* **IDE:** Android Studio
* **Interface:** XML Layouts & CardStackView
* **Backend:**
    * **Firebase Authentication:** Gestão de usuários e segurança.
    * **Cloud Firestore:** Banco de dados NoSQL para ranking e persistência de dados.
* **IA:** Integração via API REST com Google Gemini 1.5.

---

## 🚀 Como Rodar o Projeto

### Pré-requisitos
* Android Studio Ladybug ou superior.
* JDK 11 ou superior.
* Conta no Firebase.

### Passo a Passo

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/GabrielBarbosaAfo/AndroidReigns](https://github.com/GabrielBarbosaAfo/AndroidReigns)
    ```
2.  **Configuração do Firebase:**
    * Crie um projeto no Console do Firebase.
    * Baixe o arquivo `google-services.json`.
    * Coloque o arquivo na pasta `app/` do projeto.
3.  **Configuração da API Key (Gemini):**
    * Gere uma chave de API no Google AI Studio.
    * Ao abrir o app, vá em Configurações e insira sua chave para habilitar a geração de histórias.
4.  **Build & Run:**
    * Conecte um emulador ou dispositivo físico e execute o projeto pelo Android Studio.

---

## 🤝 Autores

Projeto desenvolvido como parte da disciplina de Desenvolvimento Android no **IF Sudeste MG**.

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/GabrielBarbosaAfo">
        <img src="https://github.com/GabrielBarbosaAfo.png" width="100px;" alt=""/><br>
        <sub><b>Gabriel Afonso Barbosa</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/michelleGomes85">
        <img src="https://github.com/michelleGomes85.png" width="100px;" alt=""/><br>
        <sub><b>Michelle Cristina Gomes</b></sub>
      </a>
    </td>
  </tr>
</table>

---

## 📄 Licença

Este projeto está sob a licença MIT.
