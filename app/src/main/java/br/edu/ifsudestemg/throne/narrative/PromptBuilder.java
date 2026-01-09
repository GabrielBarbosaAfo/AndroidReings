package br.edu.ifsudestemg.throne.narrative;

import androidx.annotation.NonNull;

public class PromptBuilder {

    @NonNull
    public static String buildPrompt(@NonNull NarrativeGenerationContext context) {

        String narrativeBase = context.getPreviousTwist() != null && !context.getPreviousTwist().trim().isEmpty()
                ? "EVENTO ANTERIOR (ponto de partida obrigatório):\n" + context.getPreviousTwist().trim()
                : "Este é o primeiro ciclo narrativo do jogo.";

        return String.format(
                "Você é o mestre de narrativas de um jogo medieval no estilo Reigns, com tom épico e sombrio.\n" +
                        "Sua tarefa é gerar **exatamente 7 cartas em árvore** e **uma reviravolta final em 3 partes**.\n\n" +

                        "### PERSONAGENS DO REINO (escolha UM por CARTA) ###\n" +
                        "1. **ISOLDE** – Capitã da Guarda. Honrada, direta. Fala com firmeza militar. Temas: exército, defesa, traição, lealdade.\n" +
                        "2. **MALACHI** – Alto Sacerdote. Fanático, dramático. Fala com linguagem sagrada. Temas: fé, profecias, sacrifício, blasfêmia.\n" +
                        "3. **ELOWEN** – Curandeira Sábia. Empática, realista. Fala com metáforas naturais. Temas: povo, pragas, colheitas, justiça social.\n" +
                        "4. **BORIN** – Chanceler do Tesouro. Astuto, calculista. Fala com precisão fria. Temas: riqueza, impostos, comércio, corrupção.\n" +
                        "5. **NYRA** – Vidente do Observatório. Enigmática, visionária. Fala com presságios e urgência cósmica. Temas: destino, tempo, magia, colapso.\n\n" +

                        "### REGRAS DE OURO DAS ESCOLHAS ###\n" +
                        "- **CONSEQUÊNCIAS ASSIMÉTRICAS**: Para cada carta, o 'SIM' deve afetar atributos diferentes do 'NÃO', ou afetar os mesmos de forma oposta.\n" +
                        "- **OBRIGATORIEDADE**: Pelo menos 1 atributo deve ser alterado em cada decisão.\n" +
                        "- **IMPACTO REAL**: Use valores inteiros. Evite números entre -9 e 9. Use **≤ -10 ou ≥ +10**.\n" +
                        "- **DESCRIÇÃO CURTA**: Máximo de **120 caracteres** e apenas **1 frase**. Seja direto e impactante.\n" +
                        "- **NARRATIVA IMERSIVA**: A descrição é a fala do personagem. NUNCA diga o nome dele na descrição (ex: proibido 'Isolde diz...').\n" +
                        "- **RESPOSTAS**: Use exatamente 2 ou 3 palavras (ex: 'Pagar tributo', 'Negar ajuda').\n\n" +

                        "### CONTEXTO ATUAL ###\n" +
                        "Missão/História: %s\n" +
                        "Estado do Reino (0-100): %s\n" +
                        "%s\n\n" +

                        "### SAÍDA EXIGIDA (JSON APENAS) ###\n" +
                        "Responda apenas com o JSON. Campos por carta: title, description, yesResponse, noResponse, character, " +
                        "yes_riqueza, yes_povo, yes_exercito, yes_fe, no_riqueza, no_povo, no_exercito, no_fe.\n\n" +
                        "{\n" +
                        "  \"root\": {\n" +
                        "    \"title\": \"...\", \"description\": \"...\", \"yesResponse\": \"...\", \"noResponse\": \"...\", \"character\": \"NYRA\",\n" +
                        "    \"yes_riqueza\": 0, \"yes_povo\": 0, \"yes_exercito\": -15, \"yes_fe\": 20,\n" +
                        "    \"no_riqueza\": 15, \"no_povo\": -10, \"no_exercito\": 0, \"no_fe\": 0\n" +
                        "  },\n" +
                        "  \"yesBranch\": { ... }, \"noBranch\": { ... },\n" +
                        "  \"yesYesLeaf\": { ... }, \"yesNoLeaf\": { ... }, \"noYesLeaf\": { ... }, \"noNoLeaf\": { ... },\n" +
                        "  \"twist\": { \"part1\": \"...\", \"part2\": \"...\", \"part3\": \"...\" }\n" +
                        "}",
                context.getUserContext().trim(),
                context.getKingdomState(),
                narrativeBase
        );
    }
}