# 👑 Throne: AI Kingdom

> Um jogo de estratégia medieval baseado em cartas, onde suas decisões moldam o destino do reino. Desenvolvido com **Java**, **Firebase** e narrativa gerada por **Inteligência Artificial (Gemini)**.

![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=flat&logo=android)
![Java](https://img.shields.io/badge/Language-Java-ED8B00?style=flat&logo=java)
![Firebase](https://img.shields.io/badge/Backend-Firebase-FFCA28?style=flat&logo=firebase)
![Gemini AI](https://img.shields.io/badge/AI-Google%20Gemini-8E75B2?style=flat&logo=google)

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

## 📱 Funcionalidades

* **Mecânica de Swipe:** Deslize para a direita (Sim) ou esquerda (Não) para tomar decisões.
* **Ranking em Tempo Real:** Integração com **Firebase Firestore** para listar os reinados mais longos globalmente.
* **Autenticação na Nuvem:** Sistema de Login e Registro de usuários via Firebase Auth.
* **IA Generativa:** O contexto do jogo (ex: "Reino Futurista", "Idade Média", "Apocalipse Zumbi") pode ser definido pelo jogador e a IA cria a história.
* **Design Imersivo:** Interface temática medieval, com efeitos sonoros e músicas de fundo.

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java (Android Nativo)
* **IDE:** Android Studio
* **Interface:** XML Layouts & CardStackView
* **Backend:**
    * **Firebase Authentication:** Gestão de usuários.
    * **Cloud Firestore:** Banco de dados NoSQL para ranking e persistência.
* **IA:** Integração via API REST com Google Gemini.

## 🚀 Como Rodar o Projeto

### Pré-requisitos
* Android Studio Ladybug ou superior.
* JDK 11 ou superior.
* Conta no Firebase.

### Passo a Passo

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/GabrielBarbosaAfo/AndroidReings
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

* **Gabriel Afonso Barbosa** - https://github.com/GabrielBarbosaAfo
* **Sua Dupla** - https://github.com/michelleGomes85

---
