import javax.swing.*;
import java.awt.*;

import java.io.File;
import java.util.List;
import java.util.concurrent.*;

public class BatchFileConverterGUI extends JFrame {
    private JButton selectFilesButton, startButton, cancelButton;
    private JComboBox<String> conversionTypeComboBox;
    private JProgressBar overallProgressBar;
    private JTextArea statusTextArea;
    private List<File> selectedFiles;
    private ExecutorService executorService;
    private List<FileConverter> activeConverters;

    public BatchFileConverterGUI() {
        setTitle("Batch File Converter");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        activeConverters = new CopyOnWriteArrayList<>();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        selectFilesButton = new JButton("Select Files");
        conversionTypeComboBox = new JComboBox<>(new String[]{"PDF to Docx", "Image Resize"});
        startButton = new JButton("Start Conversion");
        cancelButton = new JButton("Cancel");
        topPanel.add(selectFilesButton);
        topPanel.add(conversionTypeComboBox);
        topPanel.add(startButton);
        topPanel.add(cancelButton);

        overallProgressBar = new JProgressBar(0, 100);
        statusTextArea = new JTextArea(10, 50);
        statusTextArea.setEditable(false);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(statusTextArea), BorderLayout.CENTER);
        add(overallProgressBar, BorderLayout.SOUTH);

        selectFilesButton.addActionListener(e -> selectFiles());
        startButton.addActionListener(e -> startConversion());
        cancelButton.addActionListener(e -> cancelConversion());
    }

    private void selectFiles() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFiles = List.of(fileChooser.getSelectedFiles());
            statusTextArea.append("Selected " + selectedFiles.size() + " files.\n");
        }
    }

    private void startConversion() {
        if (selectedFiles == null || selectedFiles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select files first.");
            return;
        }

        String conversionType = (String) conversionTypeComboBox.getSelectedItem();
        overallProgressBar.setValue(0);
        statusTextArea.setText("");

        for (File file : selectedFiles) {
            FileConverter converter = new FileConverter(file, conversionType);
            activeConverters.add(converter);
            converter.execute();
        }
    }

    private void cancelConversion() {
        for (FileConverter converter : activeConverters) {
            converter.cancel(true);
        }
        activeConverters.clear();
        statusTextArea.append("Conversion cancelled.\n");
    }

    private class FileConverter extends SwingWorker<Void, Integer> {
        private final File file;
        private final String conversionType;

        public FileConverter(File file, String conversionType) {
            this.file = file;
            this.conversionType = conversionType;
        }

        @Override
        protected Void doInBackground() throws Exception {
            for (int i = 0; i <= 100; i += 10) {
                if (isCancelled()) {
                    break;
                }
                Thread.sleep(500); // Simulate conversion work
                publish(i);
            }
            return null;
        }

        @Override
        protected void process(List<Integer> chunks) {
            int latestProgress = chunks.get(chunks.size() - 1);
            statusTextArea.append(String.format("Converting %s (%s): %d%%\n", 
                file.getName(), conversionType, latestProgress));
            updateOverallProgress();
        }

        @Override
        protected void done() {
            try {
                get(); // This will throw an exception if the task was cancelled
                statusTextArea.append(String.format("Converted %s successfully.\n", file.getName()));
            } catch (InterruptedException | ExecutionException e) {
                statusTextArea.append(String.format("Error converting %s: %s\n", 
                    file.getName(), e.getMessage()));
            } catch (CancellationException e) {
                statusTextArea.append(String.format("Conversion of %s was cancelled.\n", file.getName()));
            }
            activeConverters.remove(this);
            updateOverallProgress();
            if (activeConverters.isEmpty()) {
                JOptionPane.showMessageDialog(BatchFileConverterGUI.this, "All conversions completed!");
            }
        }
    }

    private void updateOverallProgress() {
        int totalProgress = activeConverters.stream()
                .mapToInt(SwingWorker::getProgress)
                .sum();
        int overallProgress = totalProgress / Math.max(1, selectedFiles.size());
        overallProgressBar.setValue(overallProgress);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BatchFileConverterGUI().setVisible(true));
    }
}