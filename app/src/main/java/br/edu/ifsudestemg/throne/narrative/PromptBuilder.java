package br.edu.ifsudestemg.throne.narrative;

import androidx.annotation.NonNull;

public class PromptBuilder {

    @NonNull
    public static String buildPrompt(@NonNull NarrativeGenerationContext context) {

        String narrativeBase = context.getPreviousTwist() != null && !context.getPreviousTwist().trim().isEmpty()
                ? "EVENTO ANTERIOR (ponto de partida obrigatório):\n" + context.getPreviousTwist().trim()
                : "Este é o primeiro ciclo narrativo do jogo.";

        return String.format(
                "Você é o mestre de narrativas de um jogo medieval no estilo Reigns, com tom épico, sombrio e cheio de consequências reais. " +
                        "Sua tarefa é gerar **exatamente 7 cartas em árvore** (1 raiz + 2 ramos + 4 folhas) e **uma reviravolta final em 3 partes**.\n\n" +

                        "### PERSONAGENS DO REINO (escolha UM por CARTA) ###\n" +
                        "Cada carta deve ser apresentada por **um dos 5 personagens abaixo**, escolhido com propósito. " +
                        "Use **apenas a palavra-chave** (uma única palavra em maiúsculas) para identificar o personagem no campo \"character\". " +
                        "A **descrição da carta deve soar como se esse personagem estivesse falando diretamente ao rei**, **MAS NUNCA inclua o nome do personagem na descrição** (ex: proibido \"ISOLDE: ...\" ou \"Diz Malachi: ...\"). " +
                        "A fala deve ser imersiva, direta e contextual — como um apelo, ameaça ou aviso feito à coroa.\n\n" +

                        "1. **ISOLDE** – Capitã da Guarda. Honrada, direta. Fala com firmeza militar. Temas: exército, defesa, traição, lealdade.\n" +
                        "2. **MALACHI** – Alto Sacerdote. Fanático, dramático. Fala com linguagem sagrada. Temas: fé, profecias, sacrifício, blasfêmia.\n" +
                        "3. **ELOWEN** – Curandeira Sábia. Empática, realista. Fala com metáforas naturais. Temas: povo, pragas, colheitas, justiça social.\n" +
                        "4. **BORIN** – Chanceler do Tesouro. Astuto, calculista. Fala com precisão fria. Temas: riqueza, impostos, comércio, corrupção.\n" +
                        "5. **NYRA** – Vidente do Observatório. Enigmática, visionária. Fala com presságios e urgência cósmica. Temas: destino, tempo, magia, colapso.\n\n" +

                        "### REGRAS ESTRITAS ###\n" +
                        "1. **REVIRAVOLTA (\"piloto\")**:\n" +
                        "   - Deve ser um evento **transformador, irreversível e dramático**.\n" +
                        "   - Parte 1: suspense.\n" +
                        "   - Parte 2: revelação chocante.\n" +
                        "   - Parte 3: consequências permanentes.\n" +
                        "   - NUNCA repita temas anteriores. Use criatividade: pragas, rebeliões, fenômenos celestes, pactos sobrenaturais, etc.\n\n" +

                        "2. **CARTAS**:\n" +
                        "   - Título: máximo 30 caracteres.\n" +
                        "   - Descrição: 1–2 frases, **como se o personagem escolhido estivesse falando diretamente ao rei**, **SEM NUNCA mencionar seu nome ou título**.\n" +
                        "   - **Respostas \"Sim\"/\"Não\": EXATAMENTE 2 ou 3 palavras**.\n" +
                        "   - **Efeitos**: inteiros **fora do intervalo [-9, +9]** → use **≤ -10 ou ≥ +10**.\n" +
                        "   - **Cada efeito deve ter lógica narrativa clara** (ex: \"Perdoar desertores → exército -20, povo +15\").\n" +
                        "   - **Evite afetar todos os 4 atributos na mesma carta.** A maioria das cartas deve alterar **apenas 1 ou 2 atributos**.\n" +
                        "   - **Apenas cartas de alta dramaticidade** (ex: colapso econômico, guerra santa, traição real) podem afetar **3 ou 4 atributos** — e mesmo assim, **nunca use valores pequenos em todos** (ex: riqueza +10, povo +10, exército -10, fé -10 é FRACO e genérico).\n" +
                        "   - Ao menos uma folha deve conter risco real de fim de jogo (atributo ≤0 ou ≥100).\n\n" +

                        "3. **PERSONAGEM POR CARTA**:\n" +
                        "   - Cada um dos 7 nós (**root**, **yesBranch**, **noBranch**, **yesYesLeaf**, **yesNoLeaf**, **noYesLeaf**, **noNoLeaf**) deve incluir:\n" +
                        "     → **\"character\": \"PALAVRA-CHAVE\"**\n" +
                        "   - Use **exatamente uma das 5 palavras-chave acima**, em **MAIÚSCULAS**.\n\n" +

                        "4. **FINALIZAÇÃO**:\n" +
                        "   - Se **QUALQUER atributo (riqueza, povo, exército, fé) atingir ≤0 ou ≥100 após uma decisão**, o jogo **DEVE TERMINAR**.\n" +
                        "   - A carta que causa isso deve refletir **consequências narrativas finais**.\n\n" +

                        "5. **CONTINUIDADE**:\n" +
                        "%s\n\n" +

                        "### CONTEXTO ATUAL ###\n" +
                        "Missão inicial do jogador:\n%s\n\n" +
                        "Estado do reino (escala 0–100):\n%s\n\n" +

                        "### SAÍDA EXIGIDA ###\n" +
                        "Responda APENAS com JSON válido, **sem qualquer texto adicional**, **sem blocos de código**, **sem explicações**.\n" +
                        "Formato exato:\n" +
                        "{\n" +
                        "  \"root\": { \"title\": \"...\", \"description\": \"...\", \"yesResponse\": \"...\", \"noResponse\": \"...\", \"riqueza\": X, \"povo\": Y, \"exercito\": Z, \"fe\": W, \"character\": \"...\" },\n" +
                        "  \"yesBranch\": { ... },\n" +
                        "  \"noBranch\": { ... },\n" +
                        "  \"yesYesLeaf\": { ... },\n" +
                        "  \"yesNoLeaf\": { ... },\n" +
                        "  \"noYesLeaf\": { ... },\n" +
                        "  \"noNoLeaf\": { ... },\n" +
                        "  \"twist\": {\n" +
                        "    \"part1\": \"...\",\n" +
                        "    \"part2\": \"...\",\n" +
                        "    \"part3\": \"...\"\n" +
                        "  }\n" +
                        "}\n" +
                        "Valores numéricos SEM aspas. \"character\" deve ser uma das 5 palavras-chave em MAIÚSCULAS. NADA além do JSON.",
                narrativeBase,
                context.getUserContext().trim(),
                context.getKingdomState()
        );
    }
}