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

                        "### REGRAS ESTRITAS ###\n" +
                        "1. **REVIRAVOLTA (\"piloto\")**:\n" +
                        "   - Deve ser um evento **transformador, irreversível e dramático** (ex: invasão, traição, milagre, colapso).\n" +
                        "   - Parte 1: introduz o evento com suspense.\n" +
                        "   - Parte 2: mostra sua escalada ou revelação chocante.\n" +
                        "   - Parte 3: conclui com **consequências permanentes** para o reino.\n" +
                        "   - NUNCA repita temas anteriores. Seja criativo: use magia, pragas, rebeliões, alianças proibidas, fenômenos cósmicos, etc.\n\n" +

                        "2. **CARTAS**:\n" +
                        "   - Título: máximo 30 caracteres.\n" +
                        "   - Descrição: 1–2 frases, tom imersivo e medieval.\n" +
                        "   - **Respostas \"Sim\"/\"Não\": EXATAMENTE 2 ou 3 palavras** (ex: \"Aceitar aliança\", \"Recusar tributo\").\n" +
                        "   - **Efeitos**: inteiros entre -20 e +20. **Ao menos uma folha deve ter risco de terminar o jogo** (ex: levar atributo a 0 ou 100).\n\n" +

                        "3. **FINALIZAÇÃO**:\n" +
                        "   - Se **QUALQUER atributo (riqueza, povo, exército, fé) atingir ≤0 ou ≥100 após uma decisão**, o jogo **DEVE TERMINAR**.\n" +
                        "   - A carta que causa isso deve refletir **consequências finais** (ex: \"Seu povo se revolta e o enforca\").\n\n" +

                        "4. **CONTINUIDADE**:\n" +
                        "%s\n\n" +

                        "### CONTEXTO ATUAL ###\n" +
                        "Missão inicial do jogador:\n%s\n\n" +
                        "Estado do reino (escala 0–100):\n%s\n\n" +

                        "### SAÍDA EXIGIDA ###\n" +
                        "Responda APENAS com JSON válido, **sem qualquer texto adicional**, **sem blocos de código**, **sem explicações**.\n" +
                        "Formato exato:\n" +
                        "{\n" +
                        "  \"root\": { \"title\": \"...\", \"description\": \"...\", \"yesResponse\": \"...\", \"noResponse\": \"...\", \"riqueza\": 0, \"povo\": 0, \"exercito\": 0, \"fe\": 0 },\n" +
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
                        "Valores numéricos SEM aspas. Respostas de Sim/Não com 2–3 palavras. NADA além do JSON.",
                narrativeBase,
                context.getUserContext().trim(),
                context.getKingdomState()
        );
    }
}