package Week2LibraryProjectV3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/** One window, with a constructor for each student's library. */
public class GUI_V3 extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private static final Color CREAM = new Color(245, 239, 232);
    private static final Color LIGHT_CREAM = new Color(255, 250, 244);
    private static final Color SAGE = new Color(174, 187, 157);
    private static final Color BLUSH = new Color(231, 188, 180);
    private static final Color BROWN = new Color(76, 63, 56);
    private static final Color SOFT_BORDER = new Color(205, 190, 176);
    private static final Font CONTROL_FONT = new Font("SansSerif", Font.PLAIN, 13);
    private LibrarySeyma seymaLibrary;
    private libraryDataController nickolasLibrary;
    private List<String[]> displayedBooks = new ArrayList<String[]>();
    private JTextField searchField = new JTextField(18);
    private JComboBox<String> searchType = new JComboBox<String>(new String[] {"book_id", "isbn"});
    private JComboBox<String> sortType = new JComboBox<String>(new String[] {"Authors", "Original Publication Year"});
    private JComboBox<String> sortOrder = new JComboBox<String>(new String[] {"Ascending", "Descending"});
    private JLabel statusLabel = new JLabel();
    private JLabel imageLabel = new JLabel("Select a book", SwingConstants.CENTER);
    private DefaultTableModel tableModel;
    private JTable table;
    private SwingWorker<Image, Void> imageWorker;

    public GUI_V3(LibrarySeyma library) {
        if (library == null) {
            throw new IllegalArgumentException("A library is required.");
        }
        seymaLibrary = library;
        buildWindow("Seyma - ArrayList / binary search");
    }

    /**
     * @wbp.parser.constructor
     */
    public GUI_V3(libraryDataController library) {
        if (library == null) {
            throw new IllegalArgumentException("A library is required.");
        }
        nickolasLibrary = library;
        buildWindow("Nickolas - LinkedList / linear search");
    }

    private void buildWindow(String version) {
        setTitle("Library Book Viewer | " + version);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1250, 720);
        setMinimumSize(new Dimension(1000, 600));
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout(8, 8));
        getContentPane().setBackground(CREAM);

        JPanel controls = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 6, 6));
        controls.setBackground(CREAM);
        controls.add(sectionLabel("Search:"));
        controls.add(searchField);
        controls.add(searchType);
        controls.add(button("Search"));
        controls.add(button("Show All"));
        controls.add(button("Top 10 Rated"));
        searchField.setActionCommand("Search");
        searchField.addActionListener(this);
        controls.add(Box.createHorizontalStrut(12));
        controls.add(sectionLabel("Sort all books:"));
        controls.add(sortType);
        controls.add(sortOrder);
        controls.add(button("Sort"));
        controls.add(button("Add Book"));
        controls.add(button("Delete Book"));

        searchField.setBackground(LIGHT_CREAM);
        searchField.setForeground(BROWN);
        searchField.setCaretColor(BROWN);
        searchField.setFont(CONTROL_FONT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SOFT_BORDER),
                BorderFactory.createEmptyBorder(5, 7, 5, 7)));
        styleComboBox(searchType);
        styleComboBox(sortType);
        styleComboBox(sortOrder);

        JPanel topArea = new JPanel(new BorderLayout());
        topArea.setBackground(CREAM);
        topArea.add(createBanner(), BorderLayout.NORTH);
        topArea.add(controls, BorderLayout.CENTER);
        getContentPane().add(topArea, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[] {"Book ID", "Title", "Authors", "ISBN"}, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(CONTROL_FONT);
        table.setBackground(LIGHT_CREAM);
        table.setForeground(BROWN);
        table.setGridColor(SOFT_BORDER);
        table.setSelectionBackground(SAGE);
        table.setSelectionForeground(BROWN);
        table.setShowVerticalLines(false);
        table.getTableHeader().setBackground(SAGE);
        table.getTableHeader().setForeground(BROWN);
        table.getTableHeader().setFont(
                CONTROL_FONT.deriveFont(Font.BOLD));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(1).setPreferredWidth(320);
        table.getColumnModel().getColumn(2).setPreferredWidth(210);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    showSelectedBook();
                }
            }
        });

        JPanel sidePanel = new JPanel(new BorderLayout(8, 8));
        sidePanel.setBackground(CREAM);
        imageLabel.setPreferredSize(new Dimension(280, 400));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(LIGHT_CREAM);
        imageLabel.setForeground(BROWN);
        imageLabel.setFont(CONTROL_FONT);
        sidePanel.add(imageLabel, BorderLayout.CENTER);

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.getViewport().setBackground(LIGHT_CREAM);
        tableScrollPane.setBorder(
                BorderFactory.createLineBorder(SOFT_BORDER));

        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                tableScrollPane,
                sidePanel);
        split.setBackground(CREAM);
        split.setBorder(BorderFactory.createEmptyBorder());
        split.setDividerSize(6);
        split.setResizeWeight(0.72);
        split.setDividerLocation(870);
        getContentPane().add(split, BorderLayout.CENTER);
        statusLabel.setOpaque(true);
        statusLabel.setBackground(SAGE);
        statusLabel.setForeground(BROWN);
        statusLabel.setFont(CONTROL_FONT);
        statusLabel.setBorder(
                BorderFactory.createEmptyBorder(7, 10, 7, 10));
        getContentPane().add(statusLabel, BorderLayout.SOUTH);
        showAll();
    }

    private JLabel createBanner() {
        URL bannerUrl = GUI_V3.class.getResource(
                "/Week2LibraryProjectV3/library_banner.png");

        if (bannerUrl == null) {
            return new JLabel(
                    "Library Book Viewer",
                    SwingConstants.CENTER);
        }

        ImageIcon originalIcon = new ImageIcon(bannerUrl);
        int bannerWidth = 1200;
        int bannerHeight = originalIcon.getIconHeight()
                * bannerWidth / originalIcon.getIconWidth();

        Image resizedImage = originalIcon.getImage()
                .getScaledInstance(
                        bannerWidth,
                        bannerHeight,
                        Image.SCALE_SMOOTH);

        return new JLabel(
                new ImageIcon(resizedImage),
                SwingConstants.CENTER);
    }

    private JButton button(String text) {
        JButton button = new JButton(text);
        button.setFont(CONTROL_FONT);
        button.setForeground(BROWN);
        button.setBackground(SAGE);
        if (text.equals("Delete Book")) {
            button.setBackground(BLUSH);
        }
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SOFT_BORDER),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        button.addActionListener(this);
        return button;
    }

    private JLabel sectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(BROWN);
        label.setFont(CONTROL_FONT.deriveFont(Font.BOLD));
        return label;
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setBackground(LIGHT_CREAM);
        comboBox.setForeground(BROWN);
        comboBox.setFont(CONTROL_FONT);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String action = event.getActionCommand();
        try {
            if (action.equals("Search")) {
                search();
            } else if (action.equals("Show All")) {
                showAll();
            } else if (action.equals("Top 10 Rated")) {
                showTopTen();
            } else if (action.equals("Sort")) {
                sortBooks();
            } else if (action.equals("Add Book")) {
                addBook();
            } else if (action.equals("Delete Book")) {
                deleteSelectedBook();
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Check your input", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Both book classes already provide getDataArray(), so the table can use it.
    private List<String[]> seymaRows(List<BookSeyma> books) {
        ArrayList<String[]> rows = new ArrayList<String[]>();
        for (BookSeyma book : books) {
            rows.add(book.getDataArray());
        }
        return rows;
    }

    private List<String[]> nickolasRows(List<Book> books) {
        ArrayList<String[]> rows = new ArrayList<String[]>();
        for (Book book : books) {
            rows.add(book.getDataArray());
        }
        return rows;
    }

    private List<String[]> currentRows() {
        if (seymaLibrary != null) {
            return seymaRows(seymaLibrary.getBooksList());
        }
        return nickolasRows(nickolasLibrary.getBooksList());
    }

    private void updateTable(List<String[]> rows, String view) {
        table.clearSelection();
        displayedBooks = new ArrayList<String[]>(rows);
        tableModel.setRowCount(0);
        for (String[] data : rows) {
            tableModel.addRow(new Object[] {data[0], data[10], data[7], data[5], data[8], data[12]});
        }
        clearDetails();
        statusLabel.setText(view + " | " + rows.size() + " books shown ");
    }

    private void showAll() {
        updateTable(currentRows(), "All books");
    }

    private void search() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            throw new IllegalArgumentException("Enter a book ID or ISBN first.");
        }
        String type = (String) searchType.getSelectedItem();
        if (seymaLibrary != null) {
            updateTable(seymaRows(seymaLibrary.binarySearch(query, type)), "Search results");
        } else {
            updateTable(nickolasRows(nickolasLibrary.LinearSearch(query, type)), "Search results");
        }
    }

    private void sortBooks() {
        String category = (String) sortType.getSelectedItem();
        boolean ascending = sortOrder.getSelectedIndex() == 0;
        if (seymaLibrary != null) {
            seymaLibrary.sortBooks(category, ascending);
        } else {
            nickolasLibrary.sortBooks(category, ascending);
        }
        updateTable(currentRows(), category + " - " + sortOrder.getSelectedItem());
    }

    private void showTopTen() {
        if (seymaLibrary != null) {
            updateTable(seymaRows(seymaLibrary.getTop10ByRating()), "Top 10 by average rating");
            return;
        }
        // Nickolas's controller has no top-ten method. Sort a display copy.
        List<String[]> rows = currentRows();
        Collections.sort(rows, new Comparator<String[]>() {
            @Override
            public int compare(String[] first, String[] second) {
                return Double.compare(LibrarySeyma.numberOrZero(second[12]), LibrarySeyma.numberOrZero(first[12]));
            }
        });
        ArrayList<String[]> topTen = new ArrayList<String[]>();
        for (int i = 0; i < rows.size() && i < 10; i++) {
            topTen.add(rows.get(i));
        }
        updateTable(topTen, "Top 10 by average rating");
    }

    private void clearDetails() {
        if (imageWorker != null) {
            imageWorker.cancel(true);
            imageWorker = null;
        }
        imageLabel.setIcon(null);
        imageLabel.setText("Select a book");
    }

    private void showSelectedBook() {
        clearDetails();
        int row = table.getSelectedRow();
        if (row < 0 || row >= displayedBooks.size()) {
            return;
        }
        String[] data = displayedBooks.get(row);
        loadBookImage(data[21]);
    }

    private void loadBookImage(final String address) {
        if (address == null || address.trim().isEmpty()) {
            imageLabel.setText("No cover available");
            return;
        }
        imageLabel.setText("Loading cover...");
        // A background worker keeps the buttons usable while a cover downloads.
        imageWorker = new SwingWorker<Image, Void>() {
            @Override
            protected Image doInBackground() throws Exception {
                URI uri = new URI(address);
                if (!"https".equalsIgnoreCase(uri.getScheme()) && !"http".equalsIgnoreCase(uri.getScheme())) {
                    throw new IOException("A cover needs an HTTP or HTTPS URL.");
                }
                URLConnection connection = uri.toURL().openConnection();
                connection.setConnectTimeout(3000);
                connection.setReadTimeout(3000);
                try (InputStream input = connection.getInputStream()) {
                    Image image = ImageIO.read(input);
                    if (image == null) {
                        return null;
                    }
                    double scale = Math.min(250.0 / image.getWidth(null), 280.0 / image.getHeight(null));
                    int width = Math.max(1, (int) (image.getWidth(null) * scale));
                    int height = Math.max(1, (int) (image.getHeight(null) * scale));
                    return image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                }
            }
            @Override
            protected void done() {
                // Ignore a previous cover if the user selected another book.
                if (imageWorker != this) {
                    return;
                }
                try {
                    Image image = get();
                    if (image == null) {
                        imageLabel.setText("Cover unavailable");
                    } else {
                        imageLabel.setText("");
                        imageLabel.setIcon(new ImageIcon(image));
                    }
                } catch (Exception e) {
                    imageLabel.setText("Cover unavailable");
                }
            }
        };
        imageWorker.execute();
    }

    private String nextBookId() {
        if (seymaLibrary != null) {
            return seymaLibrary.getNextBookId();
        }
        int highest = 0;
        for (Book book : nickolasLibrary.getBooksList()) {
            try {
                int id = Integer.parseInt(book.getBookId());
                if (id > highest) {
                    highest = id;
                }
            } catch (NumberFormatException e) {
                // Ignore non-numeric IDs when suggesting a numeric ID.
            }
        }
        if (highest == Integer.MAX_VALUE) {
            throw new IllegalArgumentException("No more numeric book IDs are available.");
        }
        return String.valueOf(highest + 1);
    }

    private void addBook() {
        String[] labels = {"Book ID", "Title", "Author", "ISBN", "Publication year", "Average rating (0-5)", "Cover URL"};
        int[] columns = {0, 10, 7, 5, 8, 12, 21};
        JTextField[] inputs = new JTextField[labels.length];
        JPanel form = new JPanel(new GridLayout(labels.length, 2, 8, 8));
        for (int i = 0; i < labels.length; i++) {
            inputs[i] = new JTextField(25);
            form.add(new JLabel(labels[i]));
            form.add(inputs[i]);
        }
        inputs[0].setText(nextBookId());
        while (JOptionPane.showConfirmDialog(this, form, "Add Book", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
            String[] data = new String[23];
            for (int i = 0; i < data.length; i++) {
                data[i] = "";
            }
            for (int i = 0; i < inputs.length; i++) {
                data[columns[i]] = inputs[i].getText().trim();
            }
            try {
                if (data[0].isEmpty() || data[10].isEmpty()) {
                    throw new IllegalArgumentException("Book ID and title are required.");
                }
                validateNumber(data[8], "Publication year", false);
                validateNumber(data[12], "Average rating", true);
                if (seymaLibrary != null) {
                    seymaLibrary.addBook(new BookSeyma(data));
                } else {
                    if (!nickolasLibrary.LinearSearch(data[0], "book_id").isEmpty()) {
                        throw new IllegalArgumentException("That book ID already exists.");
                    }
                    // The controller exposes its list; use it without changing its source.
                    nickolasLibrary.getBooksList().add(new Book(data));
                }
                showAll();
                return;
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Check your input", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void validateNumber(String text, String name, boolean rating) {
        if (text.isEmpty()) {
            return;
        }
        try {
            double number = Double.parseDouble(text);
            if (Double.isNaN(number) || Double.isInfinite(number) || (rating && (number < 0 || number > 5))) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(name + " must be a valid number. Ratings must be between 0 and 5.");
        }
    }

    private void deleteSelectedBook() {
        int row = table.getSelectedRow();
        if (row < 0) {
            throw new IllegalArgumentException("Select a book first.");
        }
        String[] data = displayedBooks.get(row);
        int answer = JOptionPane.showConfirmDialog(this, "Delete " + data[10] + "?", "Delete Book", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION) {
            return;
        }
        if (seymaLibrary != null) {
            seymaLibrary.deleteBook(data[0]);
        } else {
            for (Book book : nickolasLibrary.getBooksList()) {
                if (book.getDataArray() == data) {
                    nickolasLibrary.getBooksList().remove(book);
                    break;
                }
            }
        }
        showAll();
    }

    // Run GUI_V3 with: optional CSV path, then optional "seyma" or "linked".
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    String fileName = "data/books_1.csv";
                    if (args.length > 0) {
                        fileName = args[0];
                    } else if (!new File(fileName).isFile()) {
                        JFileChooser chooser = new JFileChooser();
                        if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
                            return;
                        }
                        fileName = chooser.getSelectedFile().getAbsolutePath();
                    }
                    boolean useSeyma;
                    if (args.length > 1) {
                        if (!args[1].equalsIgnoreCase("seyma") && !args[1].equalsIgnoreCase("linked")) {
                            throw new IllegalArgumentException("Choose seyma or linked as the second argument.");
                        }
                        useSeyma = args[1].equalsIgnoreCase("seyma");
                    } else {
                        int choice = JOptionPane.showOptionDialog(null, "Which library do you want to open?", "Library version",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                                new String[] {"Seyma (ArrayList)", "Nickolas (LinkedList)"}, "Seyma (ArrayList)");
                        if (choice < 0) {
                            return;
                        }
                        useSeyma = choice == 0;
                    }
                    GUI_V3 gui;
                    if (useSeyma) {
                        LibrarySeyma library = new LibrarySeyma();
                        library.readCSV(fileName);
                        gui = new GUI_V3(library);
                    } else {
                        // Validate first: the fixed controller reports errors only to the console.
                        int expectedBooks = LibrarySeyma.readRecords(fileName).size();
                        libraryDataController library = new libraryDataController();
                        library.loadBookData(fileName);
                        if (library.getBooksList().size() != expectedBooks) {
                            throw new IOException("This CSV was not fully loaded by Nickolas's controller.");
                        }
                        gui = new GUI_V3(library);
                    }
                    gui.setVisible(true);
                } catch (IOException | IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Cannot open library", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
