import java.util.*;

public class Simulador {

    public static void main(String[] args) {
        int[][] matrizRAM = new int[10][6];
        int[][] matrizSWAP = new int[100][6];

        preencherMatrizSwap(matrizSWAP);
        preencherMatrizRAM(matrizRAM, matrizSWAP);
        imprimirMatriz("Matriz RAM no início:", matrizRAM);
        imprimirMatriz("Matriz SWAP no início:", matrizSWAP);

        // Executar 1000 instruções
        for (int i = 0; i < 1000; i++) {
            int instrucao = (int) (Math.random() * 100) + 1;
            executarInstrucao(instrucao, matrizRAM, matrizSWAP);
            if (i % 10 == 0) {
                zerarBitR(matrizRAM);
            }
        }

        imprimirMatriz("Matriz RAM no final:", matrizRAM);
        imprimirMatriz("Matriz SWAP no final:", matrizSWAP);
    }

    private static void preencherMatrizSwap(int[][] matrizSWAP) {
        for (int i = 0; i < matrizSWAP.length; i++) {
            matrizSWAP[i][0] = i;  // Número de Página (N)
            matrizSWAP[i][1] = i + 1;  // Instrução (I)
            matrizSWAP[i][2] = (int) (Math.random() * 50) + 1;  // Dado (D)
            matrizSWAP[i][3] = 0;  // Bit de Acesso R
            matrizSWAP[i][4] = 0;  // Bit de Modificação M
            matrizSWAP[i][5] = (int) (Math.random() * 9900) + 100;  // Tempo de Envelhecimento (T)
        }
    }

    private static void preencherMatrizRAM(int[][] matrizRAM, int[][] matrizSWAP) {
        for (int i = 0; i < matrizRAM.length; i++) {
            int indiceSorteado = (int) (Math.random() * 100);
            for (int j = 0; j < matrizRAM[i].length; j++) {
                matrizRAM[i][j] = matrizSWAP[indiceSorteado][j];
            }
        }
    }

    private static void executarInstrucao(int instrucao, int[][] matrizRAM, int[][] matrizSWAP) {
        boolean instrucaoEncontrada = false;

        for (int i = 0; i < matrizRAM.length; i++) {
            if (matrizRAM[i][1] == instrucao) {
                instrucaoEncontrada = true;
                matrizRAM[i][3] = 1;  // Setar o Bit de Acesso R para 1
                if (Math.random() < 0.3) {
                    matrizRAM[i][2]++;  // Atualizar o Dado (D)
                    matrizRAM[i][4] = 1;  // Setar o Bit de Modificação M para 1
                }
                break;
            }
        }

        if (!instrucaoEncontrada) {
            // Escolher o algoritmo de substituição desejado
            // Exemplo: FIFO
            substituicaoFIFO(matrizRAM, matrizSWAP);
            // Exemplo: WS-CLOCK
            // substituicaoWSClock(matrizRAM, matrizSWAP);
        }

        for (int i = 0; i < matrizRAM.length; i++) {
            if (matrizRAM[i][4] == 1) {
                salvarPaginaEmSWAP(matrizRAM[i], matrizSWAP);
                matrizRAM[i][4] = 0;  // Resetar o Bit de Modificação M para 0
            }
        }
    }

    private static void substituicaoFIFO(int[][] matrizRAM, int[][] matrizSWAP) {
        int indiceSubstituir = 0;

        for (int i = 1; i < matrizRAM.length; i++) {
            if (matrizRAM[i][5] < matrizRAM[indiceSubstituir][5]) {
                indiceSubstituir = i;
            }
        }

        int numeroPaginaSubstituir = matrizRAM[indiceSubstituir][0];
        for (int i = 0; i < matrizRAM.length; i++) {
            if (matrizRAM[i][0] == numeroPaginaSubstituir) {
                copiarPaginaDaSWAP(matrizRAM[i], matrizSWAP);
                break;
            }
        }
    }

    private static void salvarPaginaEmSWAP(int[] paginaRAM, int[][] matrizSWAP) {
        int numeroPagina = paginaRAM[0];
        for (int i = 0; i < matrizSWAP.length; i++) {
            if (matrizSWAP[i][0] == numeroPagina) {
                for (int j = 1; j < paginaRAM.length; j++) {
                    matrizSWAP[i][j] = paginaRAM[j];
                }
                break;
            }
        }
    }

    private static void copiarPaginaDaSWAP(int[] paginaRAM, int[][] matrizSWAP) {
        int numeroPagina = paginaRAM[0];
        for (int i = 0; i < matrizSWAP.length; i++) {
            if (matrizSWAP[i][0] == numeroPagina) {
                for (int j = 1; j < paginaRAM.length; j++) {
                    paginaRAM[j] = matrizSWAP[i][j];
                }
                break;
            }
        }
    }

    private static void zerarBitR(int[][] matrizRAM) {
        for (int i = 0; i < matrizRAM.length; i++) {
            matrizRAM[i][3] = 0;
        }
    }

    private static void imprimirMatriz(String mensagem, int[][] matriz) {
        System.out.println(mensagem);
        for (int[] linha : matriz) {
            for (int valor : linha) {
                System.out.print(valor + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }
}
