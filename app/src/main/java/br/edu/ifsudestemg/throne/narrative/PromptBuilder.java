package br.edu.ifsudestemg.throne.narrative;

import androidx.annotation.NonNull;

public class PromptBuilder {

    @NonNull
    public static String buildPrompt(@NonNull NarrativeGenerationContext context) {
        String expandedContext;
        if (context.getPreviousTwist() != null && !context.getPreviousTwist().trim().isEmpty()) {
            expandedContext = context.getUserContext() + " " + context.getPreviousTwist();
        } else {
            expandedContext = context.getUserContext();
        }

        return String.format(
                "Você é o narrador de um jogo de decisões no estilo Reigns. " +
                        "Com base no contexto abaixo, gere **sete cartas interativas em árvore** (1 raiz + 2 filhas + 4 netas) " +
                        "e **uma reviravolta narrativa épica em três partes**.\n\n" +

                        "REGRAS:\n" +
                        "- Cada carta deve ter: título curto, descrição (1-2 frases), respostas \"Sim\"/\"Não\" concretas.\n" +
                        "- Efeitos: inteiros entre -10 e +10 para riqueza, povo, exército, fé. Evite todos positivos ou todos negativos.\n" +
                        "- A **reviravolta NÃO é uma decisão**. É uma narração imersiva em 3 partes:\n" +
                        "   • Parte 1: introduz um evento misterioso.\n" +
                        "   • Parte 2: revela sua escalada.\n" +
                        "   • Parte 3: mostra o clímax que altera o reino permanentemente.\n" +
                        "- Mantenha coerência com o mundo e o histórico do jogador.\n\n" +

                        "CONTEXTO DO MUNDO (inclui eventos anteriores):\n%s\n\n" +
                        "ESTADO ATUAL DO REINO:\n%s\n\n" +
                        "%s\n\n" +

                        "FORMATO DE SAÍDA:\n" +
                        "Responda APENAS com JSON válido no formato exato abaixo (os valores numéricos devem estar SEM aspas):\n" +
                        "{\n" +
                        "  \"root\": { \"title\": \"...\", \"description\": \"...\", \"yesResponse\": \"...\", \"noResponse\": \"...\", \"riqueza\": 0, \"povo\": 0, \"exercito\": 0, \"fe\": 0 },\n" +
                        "  \"yesBranch\": { \"title\": \"...\", \"description\": \"...\", \"yesResponse\": \"...\", \"noResponse\": \"...\", \"riqueza\": 0, \"povo\": 0, \"exercito\": 0, \"fe\": 0 },\n" +
                        "  \"noBranch\": { \"title\": \"...\", \"description\": \"...\", \"yesResponse\": \"...\", \"noResponse\": \"...\", \"riqueza\": 0, \"povo\": 0, \"exercito\": 0, \"fe\": 0 },\n" +
                        "  \"yesYesLeaf\": { \"title\": \"...\", \"description\": \"...\", \"yesResponse\": \"...\", \"noResponse\": \"...\", \"riqueza\": 0, \"povo\": 0, \"exercito\": 0, \"fe\": 0 },\n" +
                        "  \"yesNoLeaf\": { \"title\": \"...\", \"description\": \"...\", \"yesResponse\": \"...\", \"noResponse\": \"...\", \"riqueza\": 0, \"povo\": 0, \"exercito\": 0, \"fe\": 0 },\n" +
                        "  \"noYesLeaf\": { \"title\": \"...\", \"description\": \"...\", \"yesResponse\": \"...\", \"noResponse\": \"...\", \"riqueza\": 0, \"povo\": 0, \"exercito\": 0, \"fe\": 0 },\n" +
                        "  \"noNoLeaf\": { \"title\": \"...\", \"description\": \"...\", \"yesResponse\": \"...\", \"noResponse\": \"...\", \"riqueza\": 0, \"povo\": 0, \"exercito\": 0, \"fe\": 0 },\n" +
                        "  \"twist\": {\n" +
                        "    \"part1\": \"Primeira parte da reviravolta...\",\n" +
                        "    \"part2\": \"Segunda parte...\",\n" +
                        "    \"part3\": \"Terceira parte (clímax)...\"\n" +
                        "  }\n" +
                        "}\n" +
                        "NÃO envolva a resposta em blocos de código. NÃO use ```json ou ```. \n" +
                        "NÃO adicione explicações, comentários ou texto antes/depois do JSON.\n" +
                        "Retorne APENAS o objeto JSON, do caractere '{' ao '}', sem nenhum texto adicional.",
                expandedContext,
                context.getKingdomState().toString(),
                context.getDecisionsSummary()
        );
    }
}