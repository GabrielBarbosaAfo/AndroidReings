package br.edu.ifsudestemg.throne.ai;

import android.annotation.SuppressLint;

import br.edu.ifsudestemg.throne.model.GameContext;
import br.edu.ifsudestemg.throne.model.GameState;

public class AiPromptBuilder {

    private static final String PROMPT_TEMPLATE =

            "Gere exatamente 10 eventos para um jogo no estilo REIGNS.\n" +
                    "\n" +
                    "O jogador definiu o seguinte contexto:\n" +
                    "\"%s\"\n" +
                    "\n" +
                    "Estado atual do reino:\n" +
                    "- Pessoas: %d\n" +
                    "- Exército: %d\n" +
                    "- Riqueza: %d\n" +
                    "- Fé: %d\n" +
                    "\n" +
                    "Últimos eventos relevantes:\n" +
                    "%s\n" +
                    "\n" +
                    "Cada evento deve conter:\n" +
                    "- título\n" +
                    "- descrição curta (1 a 2 linhas)\n" +
                    "- efeitos caso o jogador escolha SIM (people, army, wealth, faith)\n" +
                    "- efeitos caso o jogador escolha NÃO (people, army, wealth, faith)\n" +
                    "\n" +
                    "Formato JSON EXATO (uma lista com exatamente 10 objetos):\n" +
                    "[\n" +
                    "  {\n" +
                    "    \"title\": \"string\",\n" +
                    "    \"description\": \"string\",\n" +
                    "    \"yes\": { \"people\": 0, \"army\": 0, \"wealth\": 0, \"faith\": 0 },\n" +
                    "    \"no\":  { \"people\": 0, \"army\": 0, \"wealth\": 0, \"faith\": 0 }\n" +
                    "  },\n" +
                    "  ... (9 more eventos)\n" +
                    "]\n" +
                    "\n" +
                    "Use apenas números inteiros (positivos, negativos ou zero). " +
                    "Não use o sinal '+', apenas o número com sinal implícito (ex: -3, 5, 0). " +
                    "Não inclua comentários, explicações ou texto adicional — apenas o JSON.";

    @SuppressLint("DefaultLocale")
    public static String build(GameContext context, GameState state) {
        String userContext = sanitize(context.getUserContext());
        String lastEvents = formatLastEvents(state.getLastEvents());

        return String.format(
                PROMPT_TEMPLATE,
                userContext,
                state.getPeople(),
                state.getArmy(),
                state.getWealth(),
                state.getFaith(),
                lastEvents
        );
    }

    private static String formatLastEvents(java.util.List<String> lastEvents) {
        if (lastEvents == null || lastEvents.isEmpty()) {
            return "Nenhum evento recente.";
        }
        StringBuilder sb = new StringBuilder();
        int limit = Math.min(5, lastEvents.size());
        for (int i = 0; i < limit; i++) {
            sb.append("- ").append(sanitize(lastEvents.get(i))).append("\n");
        }
        return sb.toString().trim();
    }

    private static String sanitize(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("\n", " ")
                .replace("\r", " ")
                .replace("\"", "'");
    }
}