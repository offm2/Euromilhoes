import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class Euromilhoes extends JFrame {

    // ---- Dados historicos (aproximados 2004-2024) ----
    private static final int[] FRIOS = {22, 33, 41, 46, 47, 48, 49, 50, 13, 5};
    private static final int[] ESTRELAS_FRIAS = {4, 8, 11, 12};

    private static final int[] QUENTES = {44, 50, 38, 42, 23, 19, 27, 25, 17, 39, 20, 37};
    private static final int[] ESTRELAS_QUENTES = {2, 3, 8, 6, 9};

    private static final int MAX_1_31_NA_CHAVE4 = 2;

    private JLabel lblAleatoria, lblMenosPopular, lblFria, lblLimitada, lblQuente;
    private final Random random = new Random();

    // Guarda o texto da ultima geracao (para exportar)
    private String ultimoTextoExport = "";

    public Euromilhoes() {
        super("Euromilhoes - 5 Chaves");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(640, 640);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Topo: botoes ---
        JPanel topo = new JPanel(new GridLayout(1, 3, 10, 10));

        JButton btnGerar = new JButton("🎲 Gerar 5 Chaves");
        btnGerar.setFont(new Font("Arial", Font.BOLD, 14));
        btnGerar.setBackground(new Color(230, 240, 255));
        btnGerar.addActionListener(e -> gerarChaves());

        JButton btnExplicar = new JButton("📖 Explicar Estrategias");
        btnExplicar.setFont(new Font("Arial", Font.BOLD, 14));
        btnExplicar.addActionListener(e -> mostrarExplicacoes());

        JButton btnExportar = new JButton("💾 Exportar para .txt");
        btnExportar.setFont(new Font("Arial", Font.BOLD, 14));
        btnExportar.addActionListener(e -> exportarParaTxt());

        topo.add(btnGerar);
        topo.add(btnExplicar);
        topo.add(btnExportar);
        painel.add(topo, BorderLayout.NORTH);

        // --- Centro: chaves ---
        JPanel centro = new JPanel(new GridLayout(5, 1, 10, 10));
        lblAleatoria    = criarLabel();
        lblMenosPopular = criarLabel();
        lblFria         = criarLabel();
        lblLimitada     = criarLabel();
        lblQuente       = criarLabel();
        centro.add(lblAleatoria);
        centro.add(lblMenosPopular);
        centro.add(lblFria);
        centro.add(lblLimitada);
        centro.add(lblQuente);
        painel.add(centro, BorderLayout.CENTER);

        add(painel);
    }

    private JLabel criarLabel() {
        JLabel l = new JLabel("", SwingConstants.CENTER);
        l.setFont(new Font("Arial", Font.PLAIN, 14));
        l.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        l.setOpaque(true);
        l.setBackground(Color.WHITE);
        return l;
    }

    // ==================== GERACAO ====================

    private void gerarChaves() {
        List<Integer> n1 = numerosAleatorios(5, 1, 50);
        List<Integer> e1 = numerosAleatorios(2, 1, 12);

        List<Integer> n2 = numerosMenosPopulares();
        List<Integer> e2 = numerosAleatorios(2, 1, 12);

        List<Integer> n3 = pegarAleatoriosDe(FRIOS, 5);
        List<Integer> e3 = pegarAleatoriosDe(ESTRELAS_FRIAS, 2);

        List<Integer> n4 = numerosAleatoriosComLimite1a31();
        List<Integer> e4 = numerosAleatorios(2, 1, 12);

        List<Integer> n5 = pegarAleatoriosDe(QUENTES, 5);
        List<Integer> e5 = pegarAleatoriosDe(ESTRELAS_QUENTES, 2);

        lblAleatoria.setText(formatar("Chave 1 - Aleatoria (normal)", n1, e1));
        lblMenosPopular.setText(formatar("Chave 2 - Menos Populares (32-50)", n2, e2));
        lblFria.setText(formatar("Chave 3 - Frios (menos frequentes)", n3, e3));
        lblLimitada.setText(formatar("Chave 4 - Aleatoria c/ max 2 nrs 1-31", n4, e4));
        lblQuente.setText(formatar("Chave 5 - Quentes (mais frequentes)", n5, e5));

        // Prepara texto para exportar
        StringBuilder sb = new StringBuilder();
        sb.append("EUROMILHOES - 5 CHAVES GERADAS\n");
        sb.append("Data/hora: ").append(LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        sb.append("========================================\n\n");
        sb.append("Chave 1 - Aleatoria (normal)\n");
        sb.append("   Numeros : ").append(n1).append("\n");
        sb.append("   Estrelas: ").append(e1).append("\n\n");
        sb.append("Chave 2 - Menos Populares (32-50)\n");
        sb.append("   Numeros : ").append(n2).append("\n");
        sb.append("   Estrelas: ").append(e2).append("\n\n");
        sb.append("Chave 3 - Frios (menos frequentes)\n");
        sb.append("   Numeros : ").append(n3).append("\n");
        sb.append("   Estrelas: ").append(e3).append("\n\n");
        sb.append("Chave 4 - Aleatoria c/ max ").append(MAX_1_31_NA_CHAVE4)
          .append(" nrs entre 1-31\n");
        sb.append("   Numeros : ").append(n4).append("\n");
        sb.append("   Estrelas: ").append(e4).append("\n\n");
        sb.append("Chave 5 - Quentes (mais frequentes)\n");
        sb.append("   Numeros : ").append(n5).append("\n");
        sb.append("   Estrelas: ").append(e5).append("\n\n");
        sb.append("========================================\n");
        sb.append("AVISO: Todas as chaves tem a MESMA probabilidade\n");
        sb.append("de acertar (1 em 139.838.160). As chaves 3 e 5 sao\n");
        sb.append("apenas curiosidade estatistica. Joga com responsabilidade.\n");

        ultimoTextoExport = sb.toString();
    }

    private List<Integer> numerosAleatorios(int qtd, int min, int max) {
        Set<Integer> set = new TreeSet<>();
        while (set.size() < qtd) {
            set.add(random.nextInt(max - min + 1) + min);
        }
        return new ArrayList<>(set);
    }

    private List<Integer> numerosMenosPopulares() {
        Set<Integer> set = new TreeSet<>();
        while (set.size() < 5) {
            set.add(random.nextInt(19) + 32); // 32 a 50
        }
        return new ArrayList<>(set);
    }

    private List<Integer> numerosAleatoriosComLimite1a31() {
        int qtd1a31 = random.nextInt(MAX_1_31_NA_CHAVE4 + 1); // 0, 1 ou 2
        int qtd32a50 = 5 - qtd1a31;

        Set<Integer> escolhidos = new TreeSet<>();

        while (countRange(escolhidos, 1, 31) < qtd1a31) {
            escolhidos.add(random.nextInt(31) + 1);
        }
        while (countRange(escolhidos, 32, 50) < qtd32a50) {
            escolhidos.add(random.nextInt(19) + 32);
        }
        return new ArrayList<>(escolhidos);
    }

    private int countRange(Set<Integer> set, int min, int max) {
        int c = 0;
        for (int n : set) if (n >= min && n <= max) c++;
        return c;
    }

    private List<Integer> pegarAleatoriosDe(int[] fonte, int qtd) {
        List<Integer> copia = new ArrayList<>();
        for (int n : fonte) copia.add(n);
        Collections.shuffle(copia);
        List<Integer> res = new ArrayList<>(copia.subList(0, Math.min(qtd, copia.size())));
        Collections.sort(res);
        return res;
    }

    private String formatar(String titulo, List<Integer> nums, List<Integer> ests) {
        return "<html><div style='text-align:center;'>"
             + "<b>" + titulo + "</b><br>"
             + "Numeros: " + nums + " &nbsp;|&nbsp; Estrelas: " + ests
             + "</div></html>";
    }

    // ==================== EXPLICACOES ====================

    private void mostrarExplicacoes() {
        String texto =
            "EXPLICACAO DAS 5 ESTRATEGIAS\n" +
            "==========================================\n\n" +

            "🔵 CHAVE 1 - ALEATORIA (NORMAL)\n" +
            "------------------------------------------\n" +
            "Como funciona: sorteia 5 numeros entre 1-50 e 2\n" +
            "estrelas entre 1-12, todos com igual probabilidade.\n\n" +
            "Porque: e a UNICA estrategia matematicamente correta.\n" +
            "Cada sorteio e independente e todas as bolas tem a\n" +
            "mesma chance. Qualquer combinacao tem 1 em 139.838.160\n" +
            "de probabilidade. Escolher 1-2-3-4-5 ou 3-17-24-39-46\n" +
            "da exatamente a mesma chance.\n\n" +

            "🟢 CHAVE 2 - MENOS POPULAR (32-50)\n" +
            "------------------------------------------\n" +
            "Como funciona: sorteia os 5 numeros no intervalo 32-50.\n\n" +
            "Porque: a maioria dos apostadores escolhe numeros\n" +
            "baseados em datas de aniversario (1-31). Resultado:\n" +
            "os numeros 32-50 sao escolhidos por pouca gente.\n\n" +
            "Vantagem: NAO aumenta a chance de ganhar, mas se\n" +
            "ganhares, divides o jackpot com MENOS pessoas -\n" +
            "ou seja, recebes mais dinheiro.\n\n" +

            "🟡 CHAVE 3 - FRIA (MENOS FREQUENTES)\n" +
            "------------------------------------------\n" +
            "Como funciona: usa numeros/estrelas que sairam MENOS\n" +
            "vezes no historico completo do Euromilhoes.\n\n" +
            "Porque: e a 'falacia do jogador' - a ideia errada\n" +
            "de que numeros atrasados vao compensar. Nao tem\n" +
            "qualquer vantagem real.\n\n" +
            "Utilidade: curiosidade estatistica. Serve para\n" +
            "aprender o que sao frequencias, mas nao da vantagem.\n\n" +

            "🟠 CHAVE 4 - ALEATORIA COM LIMITE 1-31\n" +
            "------------------------------------------\n" +
            "Como funciona: aleatoria como a Chave 1, mas com no\n" +
            "maximo 2 numeros entre 1-31. O resto sai de 32-50.\n\n" +
            "Porque: e uma versao flexivel da Chave 2. Em vez de\n" +
            "proibir todos os 1-31, permite alguns, mas mantem\n" +
            "a tendencia para numeros altos.\n\n" +
            "Vantagem: tal como a Chave 2, o premio em caso de\n" +
            "acerto tende a ser maior (menos gente escolhe estes).\n\n" +

            "🔴 CHAVE 5 - QUENTE (MAIS FREQUENTES)\n" +
            "------------------------------------------\n" +
            "Como funciona: usa numeros/estrelas que sairam MAIS\n" +
            "vezes no historico completo do Euromilhoes.\n\n" +
            "Porque: e a 'falacia da mao quente' - a ideia errada\n" +
            "de que numeros em forma vao continuar a sair.\n" +
            "Nao tem qualquer vantagem real.\n\n" +
            "Utilidade: curiosidade estatistica. Ver quentes e\n" +
            "frios lado a lado mostra que os desvios sao naturais\n" +
            "do acaso.\n\n" +

            "==========================================\n" +
            "RESUMO:\n" +
            "Chaves 1, 3 e 5 -> nenhuma vantagem real\n" +
            "Chaves 2 e 4   -> melhor premio se ganhares\n" +
            "TODAS tem a MESMA probabilidade de acertar:\n" +
            "1 em 139.838.160\n\n" +
            "Joga com responsabilidade. 🍀";

        JTextArea area = new JTextArea(texto);
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setCaretPosition(0);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(620, 550));

        JOptionPane.showMessageDialog(this, scroll,
                "Explicacao das Estrategias", JOptionPane.INFORMATION_MESSAGE);
    }

    // ==================== EXPORTAR ====================

    private void exportarParaTxt() {
        if (ultimoTextoExport.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Gera primeiro uma chave antes de exportar.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar chaves como...");
        chooser.setSelectedFile(new File("chaves_euromilhoes.txt"));

        int escolha = chooser.showSaveDialog(this);
        if (escolha != JFileChooser.APPROVE_OPTION) return;

        File ficheiro = chooser.getSelectedFile();
        // Garante extensao .txt
        if (!ficheiro.getName().toLowerCase().endsWith(".txt")) {
            ficheiro = new File(ficheiro.getAbsolutePath() + ".txt");
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ficheiro))) {
            bw.write(ultimoTextoExport);
            JOptionPane.showMessageDialog(this,
                    "Chaves exportadas com sucesso para:\n" + ficheiro.getAbsolutePath(),
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao gravar o ficheiro:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Euromilhoes().setVisible(true));
    }
}
