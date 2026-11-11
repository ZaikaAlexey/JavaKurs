
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Locale;

public class MainForm {

    private JPanel mainPanel;
    private JLabel titleLabel;
    private JTable costTable;
    private JButton calculateButton;
    private JButton saveButton;
    private JButton resetButton;
    private JLabel resultLabel;
    private JTextArea detailsArea;
    private JScrollPane ResultPanel;

    private final boolean isAdmin;

    private final int[][] originalCosts = {
            {7, 12, 8, 10, 5},
            {9, 10, 7, 8, 9},
            {6, 8, 15, 9, 7},
            {9, 10, 8, 11, 7}
    };

    private final String[] alternatives = {
            "A₁", "A₂", "A₃", "A₄"
    };

    private String lastResult = "";

    public MainForm(boolean isAdmin) {
        this.isAdmin = isAdmin;
        detailsArea.setEnabled(true);
        detailsArea.setVisible(true);
        detailsArea.setRows(8);
        detailsArea.setColumns(30);
        titleLabel.setText("Критерий Лапласа");

        resultLabel.setText("(Нажмите «Рассчитать» для получения ответа)");

        // Настраиваем права администратора
        resetButton.setVisible(isAdmin);
        costTable.setEnabled(true);

        // Заполняем таблицу
        String[] columns = {
                "Альтернатива", "S₁", "S₂", "S₃", "S₄", "S₅"
        };

        Object[][] data = new Object[4][6];

        for (int i = 0; i < 4; i++) {
            data[i][0] = alternatives[i];

            for (int j = 0; j < 5; j++) {
                data[i][j + 1] = originalCosts[i][j];
            }
        }

        DefaultTableModel model =
                new DefaultTableModel(data, columns) {

                    @Override
                    public boolean isCellEditable(
                            int row, int column) {

                        return isAdmin && column > 0;
                    }
                };

        costTable.setModel(model);
        costTable.setRowHeight(28);
        ResultPanel.setVisible(true);
        detailsArea.setEditable(false);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);

        // Кнопки
        calculateButton.addActionListener(e -> calculate());

        saveButton.addActionListener(e -> saveResult());

        resetButton.addActionListener(e -> resetData());

        // Подсказка о текущей роли
        titleLabel.setText(
                "Критерий Лапласа — "
                        + (isAdmin ? "Администратор" : "Пользователь")
        );
    }

    // Получение актуальных данных из таблицы
    private int[][] getCurrentCosts() {

        int[][] current = new int[4][5];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {

                Object value = costTable.getValueAt(i, j + 1);

                try {
                    current[i][j] =
                            Integer.parseInt(value.toString().trim());

                } catch (Exception ex) {
                    throw new IllegalArgumentException(
                            "В строке " + (i + 1)
                                    + ", столбце " + (j + 1)
                                    + " должно находиться целое число."
                    );
                }
            }
        }

        return current;
    }

    // Расчёт по критерию Лапласа
    private void calculate() {

        try {
            int[][] costs = getCurrentCosts();

            double minCost = Double.MAX_VALUE;
            int bestAlternative = -1;

            StringBuilder result = new StringBuilder();
            result.append("Расчёт по критерию Лапласа\n\n");
            result.append("Формула: L(Ai) = сумма затрат / 5\n\n");

            for (int i = 0; i < costs.length; i++) {

                int sum = 0;

                for (int value : costs[i]) {
                    sum += value;
                }

                double average = (double) sum / 5;

                result.append(alternatives[i])
                        .append(": ")
                        .append(sum)
                        .append(" / 5 = ")
                        .append(String.format(
                                Locale.forLanguageTag("ru-RU"),
                                "%.1f", average
                        ))
                        .append("\n");

                if (average < minCost) {
                    minCost = average;
                    bestAlternative = i;
                }
            }

            result.append("\nПравило выбора: минимальные затраты.");

            resultLabel.setText(
                    "Оптимальная альтернатива: "
                            + alternatives[bestAlternative]
                            + " | Средние затраты: "
                            + String.format(
                            Locale.forLanguageTag("ru-RU"),
                            "%.1f", minCost
                    )
            );
            detailsArea.setText(result.toString());
            detailsArea.setCaretPosition(0);

            lastResult = resultLabel.getText()
                    + "\n\n"
                    + result;

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    ex.getMessage(),
                    "Ошибка данных",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Сохранение результата в текстовый файл
    private void saveResult() {

        if (lastResult.isEmpty()) {
            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Сначала выполните расчёт!",
                    "Внимание",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JFileChooser chooser = new JFileChooser();

        chooser.setDialogTitle("Сохранение результата");
        chooser.setSelectedFile(
                new File("Результат_Лапласа.txt")
        );

        if (chooser.showSaveDialog(mainPanel)
                == JFileChooser.APPROVE_OPTION) {

            File file = chooser.getSelectedFile();

            try {
                Files.writeString(
                        file.toPath(),
                        lastResult,
                        StandardCharsets.UTF_8
                );

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Результат успешно сохранён!",
                        "Сохранение",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } catch (IOException ex) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Ошибка сохранения: " + ex.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    // Сброс матрицы к исходным значениям
    private void resetData() {

        if (!isAdmin) {
            return;
        }
        int answer = JOptionPane.showConfirmDialog(
                mainPanel,
                "Вернуть исходные значения матрицы?",
                "Сброс данных",
                JOptionPane.YES_NO_OPTION
        );

        if (answer == JOptionPane.YES_OPTION) {

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 5; j++) {

                    costTable.setValueAt(
                            originalCosts[i][j],
                            i,
                            j + 1
                    );
                }
            }

            resultLabel.setText("Данные восстановлены");
            detailsArea.setText("");
            lastResult = "";
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
