package br.edu.ifsudestemg.throne.data;

import java.util.Arrays;
import java.util.List;

import br.edu.ifsudestemg.throne.R;
import br.edu.ifsudestemg.throne.model.CardData;

public class CardLibrary {

    public static List<CardData> getGenericCards() {
        return Arrays.asList(

                new CardData(
                        "Conselho Real",
                        "Os nobres exigem mais poder. Delegar autoridade?",
                        R.drawable.bg_back_card,
                        "Conceder poderes",
                        "Negar autoridade"
                ),

                new CardData(
                        "Guerra Fronteiriça",
                        "Inimigos atacam nossas vilas. Retaliar com exército?",
                        R.drawable.bg_back_card,
                        "Atacar com força total",
                        "Buscar negociação"
                ),

                new CardData(
                        "Colheita Fracassada",
                        "A seca destruiu as plantações. Distribuir grãos do celeiro real?",
                        R.drawable.bg_back_card,
                        "Distribuir reservas",
                        "Esperar ajuda divina"
                ),

                new CardData(
                        "Profeta Místico",
                        "Um vidente prevê traição na corte. Prender os suspeitos?",
                        R.drawable.bg_back_card,
                        "Prender à noite",
                        "Ignorar visões"
                ),

                new CardData(
                        "Feira Anual",
                        "Mercadores pedem permissão para a grande feira. Permitir?",
                        R.drawable.bg_back_card,
                        "Autorizar feira",
                        "Temer aglomeração"
                ),

                new CardData(
                        "Igreja Reformista",
                        "O clero quer reformar os dogmas. Apoiar mudança?",
                        R.drawable.bg_back_card,
                        "Apoiar reforma",
                        "Manter tradição"
                ),

                new CardData(
                        "Peste Negra",
                        "Doença se espalha pelas ruas. Isolar os doentes?",
                        R.drawable.bg_back_card,
                        "Isolar bairros",
                        "Confiar em curandeiros"
                ),

                new CardData(
                        "Tesouro Escondido",
                        "Mapa revela ouro enterrado. Enviar escavadores?",
                        R.drawable.bg_back_card,
                        "Escavar imediatamente",
                        "Ignorar lenda"
                ),

                new CardData(
                        "Casamento Real",
                        "Princesa de nação vizinha propõe aliança. Aceitar?",
                        R.drawable.bg_back_card,
                        "Aceitar aliança",
                        "Recusar proposta"
                ),

                new CardData(
                        "Revolta Camponesa",
                        "Camponeses queimam campos em protesto. Mandar guardas?",
                        R.drawable.bg_back_card,
                        "Suprimir revolta",
                        "Ouvir reivindicações"
                ),

                new CardData(
                        "Navio Estrangeiro",
                        "Embarcação traz tecnologia proibida. Permitir desembarque?",
                        R.drawable.bg_back_card,
                        "Permitir acesso",
                        "Afundar o navio"
                ),

                new CardData(
                        "Festival da Lua",
                        "Povo pede celebração noturna. Autorizar?",
                        R.drawable.bg_back_card,
                        "Celebrar com todos",
                        "Proibir por segurança"
                ),

                new CardData(
                        "Espião Infiltrado",
                        "Carta anônima revela espião na guarda real. Investigar?",
                        R.drawable.bg_back_card,
                        "Investigar todos",
                        "Destruir a carta"
                ),

                new CardData(
                        "Rios Secos",
                        "As águas sumiram. Construir aquedutos?",
                        R.drawable.bg_back_card,
                        "Investir em obras",
                        "Rezar por chuva"
                ),

                new CardData(
                        "Dragão Avistado",
                        "Besta alada sobrevoa o castelo. Oferecer tributo?",
                        R.drawable.bg_back_card,
                        "Oferecer ouro",
                        "Armar arqueiros"
                ),

                new CardData(
                        "Impostos Altos",
                        "Nobreza reclama da carga tributária. Reduzir impostos?",
                        R.drawable.bg_back_card,
                        "Reduzir impostos",
                        "Manter arrecadação"
                ),

                new CardData(
                        "Muralha Rachada",
                        "Defesas do reino estão fragilizadas. Reparar muralha?",
                        R.drawable.bg_back_card,
                        "Reparar imediatamente",
                        "Usar recursos em exército"
                ),

                new CardData(
                        "Culto Secreto",
                        "Rituais estranhos ocorrem nas catacumbas. Proibir?",
                        R.drawable.bg_back_card,
                        "Banir o culto",
                        "Investigar primeiro"
                ),

                new CardData(
                        "Invenção Proibida",
                        "Alquimista cria máquina que pensa. Destruir?",
                        R.drawable.bg_back_card,
                        "Destruir a máquina",
                        "Estudar a invenção"
                ),

                new CardData(
                        "Trono Vazio",
                        "Sussurros dizem que você não é legítimo. Convocar conselho?",
                        R.drawable.bg_back_card,
                        "Provar legitimidade",
                        "Ignorar rumores"
                )
        );
    }
}