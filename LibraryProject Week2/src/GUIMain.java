package Week2LibraryProjectV2;



import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUIMain {

    public static void main(String[] args) {
        final String suggestedFile =
                args.length > 0 ? args[0] : "data/books_1.csv";

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFileChooser chooser =
                        new JFileChooser(new File("."));

                chooser.setDialogTitle(
                        "Choose your books CSV file");

                chooser.setFileFilter(
                        new FileNameExtensionFilter(
                                "CSV files", "csv"));

                chooser.setSelectedFile(
                        new File(suggestedFile));

                if (chooser.showOpenDialog(null)
                        != JFileChooser.APPROVE_OPTION) {
                    return;
                }

                final File file = chooser.getSelectedFile();

                
                new SwingWorker<Library, Void>() {

                    @Override
                    protected Library doInBackground()
                            throws IOException {

                        Library library = new Library();

                        library.readCSV(file.getAbsolutePath());

                        return library;
                    }

                    @Override
                    protected void done() {
                        try {
                            Library library = get();

                            LibraryManager manager =
                                    new LibraryManager(
                                            library.getBooksList());

                            LibraryGUI gui =
                                    new LibraryGUI(library, manager);

                            gui.setVisible(true);

                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();

                        } catch (ExecutionException e) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Could not load "
                                            + file.getAbsolutePath()
                                            + "\n"
                                            + e.getCause().getMessage(),
                                    "CSV loading error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }.execute();
            }
        });
    }
}
