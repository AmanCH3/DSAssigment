import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import java.util.concurrent.*;

// This is the main class for our file converter application
public class BatchFileConverterGUI extends JFrame {
    // Declare all the GUI components we'll need
    private JButton selectFilesButton, startButton, cancelButton;
    private JComboBox<String> conversionTypeComboBox;
    private JProgressBar overallProgressBar;
    private JTextArea statusTextArea;
    private List<File> selectedFiles;
    private ExecutorService executorService;
    private List<FileConverter> activeConverters;

    // Constructor: sets up the main window
    public BatchFileConverterGUI() {
        setTitle("Batch File Converter");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        // Create a thread pool for running conversions
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        // List to keep track of active conversion tasks
        activeConverters = new CopyOnWriteArrayList<>();
    }

    // This method sets up all the GUI components
    private void initComponents() {
        setLayout(new BorderLayout());

        // Create the top panel with buttons and dropdown
        JPanel topPanel = new JPanel();
        selectFilesButton = new JButton("Select Files");
        conversionTypeComboBox = new JComboBox<>(new String[]{"PDF to Docx", "Image Resize"});
        startButton = new JButton("Start Conversion");
        cancelButton = new JButton("Cancel");
        topPanel.add(selectFilesButton);
        topPanel.add(conversionTypeComboBox);
        topPanel.add(startButton);
        topPanel.add(cancelButton);

        // Create progress bar and status text area
        overallProgressBar = new JProgressBar(0, 100);
        statusTextArea = new JTextArea(10, 50);
        statusTextArea.setEditable(false);

        // Add all components to the main window
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(statusTextArea), BorderLayout.CENTER);
        add(overallProgressBar, BorderLayout.SOUTH);

        // Set up button click handlers
        selectFilesButton.addActionListener(e -> selectFiles());
        startButton.addActionListener(e -> startConversion());
        cancelButton.addActionListener(e -> cancelConversion());
    }

    // This method opens a file chooser dialog to select files
    private void selectFiles() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFiles = List.of(fileChooser.getSelectedFiles());
            statusTextArea.append("Selected " + selectedFiles.size() + " files.\n");
        }
    }

    // This method starts the conversion process for all selected files
    private void startConversion() {
        if (selectedFiles == null || selectedFiles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select files first.");
            return;
        }

        String conversionType = (String) conversionTypeComboBox.getSelectedItem();
        overallProgressBar.setValue(0);
        statusTextArea.setText("");

        // Create and start a FileConverter for each selected file
        for (File file : selectedFiles) {
            FileConverter converter = new FileConverter(file, conversionType);
            activeConverters.add(converter);
            converter.execute();
        }
    }

    // This method cancels all ongoing conversions
    private void cancelConversion() {
        for (FileConverter converter : activeConverters) {
            converter.cancel(true);
        }
        activeConverters.clear();
        statusTextArea.append("Conversion cancelled.\n");
    }

    // This inner class represents a single file conversion task
    private class FileConverter extends SwingWorker<Void, Integer> {
        private final File file;
        private final String conversionType;

        public FileConverter(File file, String conversionType) {
            this.file = file;
            this.conversionType = conversionType;
        }

        // This method does the actual conversion work in the background
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

        // This method updates the GUI with progress information
        @Override
        protected void process(List<Integer> chunks) {
            int latestProgress = chunks.get(chunks.size() - 1);
            statusTextArea.append(String.format("Converting %s (%s): %d%%\n", 
                file.getName(), conversionType, latestProgress));
            updateOverallProgress();
        }

        // This method is called when the conversion is complete
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

    // This method updates the overall progress bar
    private void updateOverallProgress() {
        int totalProgress = activeConverters.stream()
                .mapToInt(SwingWorker::getProgress)
                .sum();
        int overallProgress = totalProgress / Math.max(1, selectedFiles.size());
        overallProgressBar.setValue(overallProgress);
    }

    // This is the main method that starts our application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BatchFileConverterGUI().setVisible(true));
    }
}