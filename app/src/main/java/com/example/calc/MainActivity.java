package com.example.calc;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView tvResultado;
    private String operador;
    private Double valor1;
    private Double valor2;

    private static final String OPERADOR_SOMA = "+";
    private static final String OPERADOR_SUBTRACAO = "-";
    private static final String OPERADOR_MULTIPLICACAO = "*";
    private static final String OPERADOR_DIVISAO = "/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvResultado = findViewById(R.id.tvResultado);
        configurarBotoes();
    }

    private void configurarBotoes() {
        Map<Integer, Runnable> buttonActions = new HashMap<>();
        buttonActions.put(R.id.buttonC, this::limparCampos);
        buttonActions.put(R.id.buttonIgual, this::calcularResultado);
        buttonActions.put(R.id.buttonSoma, () -> prepararOperacao(OPERADOR_SOMA));
        buttonActions.put(R.id.buttonSubtracao, () -> prepararOperacao(OPERADOR_SUBTRACAO));
        buttonActions.put(R.id.buttonMultiplicacao, () -> prepararOperacao(OPERADOR_MULTIPLICACAO));
        buttonActions.put(R.id.buttonDivisao, () -> prepararOperacao(OPERADOR_DIVISAO));
        buttonActions.put(R.id.buttonFatorial, this::calcularFatorial);
        buttonActions.put(R.id.buttonPonto, () -> adicionarNumero("."));
        buttonActions.put(R.id.buttonVirgula, () -> adicionarNumero(","));

        // Configurando os botões numéricos
        for (int i = 0; i <= 9; i++) {
            @SuppressLint("DiscouragedApi") int buttonId = getResources().getIdentifier("button" + i, "id", getPackageName());
            int finalI = i;
            buttonActions.put(buttonId, () -> adicionarNumero(String.valueOf(finalI)));
        }

        // Configurando os listeners
        for (Map.Entry<Integer, Runnable> entry : buttonActions.entrySet()) {
            findViewById(entry.getKey()).setOnClickListener(view -> entry.getValue().run());
        }
    }

    private void adicionarNumero(String s) {
        tvResultado.append(s);
    }

    private void limparCampos() {
        tvResultado.setText("");
        operador = null;
        valor1 = null;
        valor2 = null;
    }

    @SuppressLint("SetTextI18n")
    private void calcularResultado() {
        valor2 = obterValorAtual();

        if (valor1 != null && operador != null && valor2 != null) {
            Double resultado = realizarOperacao();
            if (resultado != null) {
                tvResultado.setText(resultado.toString());
                valor1 = resultado; // Armazena o resultado para continuar a operação
                operador = null; // Reseta o operador para evitar erros
            } else {
                tvResultado.setText("Erro: Divisão por zero");
            }
        } else {
            tvResultado.setText("Erro: Entrada inválida");
        }
    }

    private void prepararOperacao(String operador) {
        valor1 = obterValorAtual();
        this.operador = operador;

        // Adiciona o operador ao texto atual
        if (valor1 != null) {
            tvResultado.append(" " + operador + " ");
        }
    }

    private Double realizarOperacao() {
        switch (operador) {
            case OPERADOR_SOMA:
                return valor1 + valor2;
            case OPERADOR_SUBTRACAO:
                return valor1 - valor2;
            case OPERADOR_MULTIPLICACAO:
                return valor1 * valor2;
            case OPERADOR_DIVISAO:
                if (valor2 != 0) {
                    return valor1 / valor2; // Retorna o resultado da divisão
                } else {
                    return null; // Retorna null em caso de divisão por zero
                }
            default:
                return null; // Operador inválido
        }
    }

    @SuppressLint("SetTextI18n")
    private void calcularFatorial() {
        valor1 = obterValorAtual();
        if (valor1 != null && valor1 >= 0 && valor1 == Math.floor(valor1)) {
            long resultado = fatorial(valor1.intValue());
            tvResultado.setText(String.valueOf(resultado));
            valor1 = null; // Reseta valor1 após calcular o fatorial
        } else {
            tvResultado.setText("Erro: Fatorial só para inteiros");
        }
    }

    private long fatorial(int n) {
        if (n == 0) return 1;
        return n * fatorial(n - 1);
    }

    private Double obterValorAtual() {
        String texto = tvResultado.getText().toString().trim();

        // Verifica se há um espaço em branco antes ou depois do número
        String[] partes = texto.split(" ");
        try {
            return partes.length > 0 ? Double.parseDouble(partes[partes.length - 1].replace(",", ".")) : null;
        } catch (NumberFormatException e) {
            return null; // Retorna null se a conversão falhar
        }
    }
}