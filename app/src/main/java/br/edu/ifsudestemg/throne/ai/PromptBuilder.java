package br.edu.ifsudestemg.throne.ai;

import br.edu.ifsudestemg.throne.model.GameContext;
import br.edu.ifsudestemg.throne.model.GameState;
import java.util.Locale;

public class PromptBuilder {

    private static final String PROMPT_TEMPLATE =
            "Você é um sistema de narrativa procedural medieval para um jogo de decisões.\n" +
                    "\n" +
                    "Gere um novo evento no **formato JSON válido**, seguindo estritamente este formato:\n" +
                    "\n" +
                    "{\n" +
                    "  \"title\": \"string\",\n" +
                    "  \"description\": \"string\",\n" +
                    "  \"effects\": {\n" +
                    "    \"yes\": {\n" +
                    "      \"people\": int,\n" +
                    "      \"army\": int,\n" +
                    "      \"wealth\": int,\n" +
                    "      \"faith\": int\n" +
                    "    },\n" +
                    "    \"no\": {\n" +
                    "      \"people\": int,\n" +
                    "      \"army\": int,\n" +
                    "      \"wealth\": int,\n" +
                    "      \"faith\": int\n" +
                    "    }\n" +
                    "  }\n" +
                    "}\n" +
                    "\n" +
                    "Regras importantes:\n" +
                    "- NÃO escreva nada fora do JSON.\n" +
                    "- NÃO inclua comentários.\n" +
                    "- Efeitos devem estar entre -15 e +15.\n" +
                    "- A descrição deve ter 1–2 frases, sem opções explícitas.\n" +
                    "\n" +
                    "CONTEXTO DO JOGADOR:\n" +
                    "\"%s\"\n" +
                    "\n" +
                    "ÚLTIMA ESCOLHA DO JOGADOR:\n" +
                    "\"%s\"\n" +
                    "\n" +
                    "ESTADO ATUAL DO REINO:\n" +
                    "Povo = %d\n" +
                    "Exército = %d\n" +
                    "Riqueza = %d\n" +
                    "Fé = %d\n" +
                    "\n" +
                    "Gere um novo evento coerente com isso.";

    private PromptBuilder() {}

    public static String buildCardPrompt(GameContext context, GameState state, String lastUserChoice) {
        String userCtx = (context != null) ? context.getUserContext() : "nenhum";
        String lastChoice = (lastUserChoice != null) ? lastUserChoice : "nenhuma";

        return String.format(Locale.US, PROMPT_TEMPLATE,
                userCtx,
                lastChoice,
                state.getPeople(),
                state.getArmy(),
                state.getWealth(),
                state.getFaith());
    }
}
