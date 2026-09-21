package Week2LibraryProjectV2;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class LibraryGUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private final Library arrayLibrary;
    private final LibraryManager linkedLibrary;

    private final JComboBox<String> implementationCombo =
            new JComboBox<String>(new String[] {
                "ArrayList / Binary search",
                "LinkedList / Linear search"
            });

    private final JTextField searchField = new JTextField(16);

    private final JComboBox<String> searchCombo =
            new JComboBox<String>(new String[] {
                "book_id", "isbn"
            });

    private final JComboBox<String> sortCombo =
            new JComboBox<String>(new String[] {
                "Authors", "Original Publication Year"
            });

    private final JComboBox<String> orderCombo =
            new JComboBox<String>(new String[] {
                "Ascending", "Descending"
            });

    private final JLabel status = new JLabel(" ");

    private final DefaultTableModel tableModel =
            new DefaultTableModel(Book.COLUMN_NAMES, 0) {

                private static final long serialVersionUID = 1L;

                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

    public LibraryGUI(
            Library arrayLibrary,
            LibraryManager linkedLibrary) {

        super("SENG300 Library Project");

        this.arrayLibrary = arrayLibrary;
        this.linkedLibrary = linkedLibrary;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1150, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        add(buildControls(), BorderLayout.NORTH);

        JTable table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(145);
        }

        table.getColumnModel().getColumn(7).setPreferredWidth(250);
        table.getColumnModel().getColumn(10).setPreferredWidth(320);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);

        showTopTen();
    }

    private JPanel buildControls() {
        JPanel controls = new JPanel(new GridLayout(2, 1));

        JPanel searchPanel =
                new JPanel(new FlowLayout(FlowLayout.LEFT));

        searchPanel.add(new JLabel("Implementation:"));
        searchPanel.add(implementationCombo);

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchCombo);

        JButton searchButton = new JButton("Search");
        searchPanel.add(searchButton);

        controls.add(searchPanel);

        JPanel sortPanel =
                new JPanel(new FlowLayout(FlowLayout.LEFT));

        sortPanel.add(new JLabel("Sort by:"));
        sortPanel.add(sortCombo);
        sortPanel.add(orderCombo);

        JButton sortButton = new JButton("Sort");
        JButton topTenButton = new JButton("Show first 10");
        JButton addButton = new JButton("Add Book");
        JButton deleteButton = new JButton("Delete Book");

        sortPanel.add(sortButton);
        sortPanel.add(topTenButton);
        sortPanel.add(addButton);
        sortPanel.add(deleteButton);

        controls.add(sortPanel);

        // Clicking Search or pressing Enter performs the search.
        ActionListener searchAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                searchBooks();
            }
        };

        searchButton.addActionListener(searchAction);
        searchField.addActionListener(searchAction);

        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String criteria =
                        (String) sortCombo.getSelectedItem();

                boolean ascending =
                        orderCombo.getSelectedIndex() == 0;

                if (useArrayList()) {
                    arrayLibrary.sortBooks(criteria, ascending);
                } else {
                    linkedLibrary.sortBooks(criteria, ascending);
                }

                showTopTen();
            }
        });

        topTenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                searchField.setText("");
                showTopTen();
            }
        });

        implementationCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                searchField.setText("");
                showTopTen();
            }
        });
        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {

                if (!useArrayList()) {
                    JOptionPane.showMessageDialog(
                            LibraryGUI.this,
                            "Add Book is available in the ArrayList version.");
                    return;
                }

                String bookId =
                        JOptionPane.showInputDialog(
                                LibraryGUI.this, "Book ID:");

                if (bookId == null || bookId.trim().isEmpty()) {
                    return;
                }

                String isbn =
                        JOptionPane.showInputDialog(
                                LibraryGUI.this, "ISBN:");

                String authors =
                        JOptionPane.showInputDialog(
                                LibraryGUI.this, "Authors:");

                String year =
                        JOptionPane.showInputDialog(
                                LibraryGUI.this, "Publication Year:");

                String title =
                        JOptionPane.showInputDialog(
                                LibraryGUI.this, "Title:");

                String[] fields = new String[23];

                for (int i = 0; i < fields.length; i++) {
                    fields[i] = "";
                }

                fields[0] = bookId;
                fields[5] = isbn;
                fields[7] = authors;
                fields[8] = year;
                fields[10] = title;

                Book book = new Book(fields);

                arrayLibrary.addBook(book);

                showTopTen();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {

                if (!useArrayList()) {
                    JOptionPane.showMessageDialog(
                            LibraryGUI.this,
                            "Delete Book is available in the ArrayList version.");
                    return;
                }

                String bookId =
                        JOptionPane.showInputDialog(
                                LibraryGUI.this,
                                "Enter Book ID to delete:");

                if (bookId == null || bookId.trim().isEmpty()) {
                    return;
                }

                boolean deleted =
                        arrayLibrary.deleteBook(bookId.trim());

                if (deleted) {
                    JOptionPane.showMessageDialog(
                            LibraryGUI.this,
                            "Book deleted.");

                    showTopTen();
                }
                else {
                    JOptionPane.showMessageDialog(
                            LibraryGUI.this,
                            "Book not found.");
                }
            }
        });
        return controls;
    }

    private boolean useArrayList() {
        return implementationCombo.getSelectedIndex() == 0;
    }

    private List<Book> currentBooks() {
        return useArrayList()
                ? arrayLibrary.getBooksList()
                : linkedLibrary.getBooksList();
    }

    private void searchBooks() {
        String query = searchField.getText().trim();

        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Enter a book ID or ISBN first.");
            return;
        }

        String type = (String) searchCombo.getSelectedItem();
        List<Book> results;

        if (useArrayList()) {
            results = arrayLibrary.binarySearch(query, type);
        } else {
            results = linkedLibrary.linearSearch(query, type);
        }

        updateTable(results, results.size());

        status.setText(
                implementationCombo.getSelectedItem()
                        + " | Matches: " + results.size());
    }

    private void showTopTen() {
        List<Book> source = currentBooks();

        updateTable(source, 10);

        status.setText(
                implementationCombo.getSelectedItem()
                        + " | Showing "
                        + Math.min(10, source.size())
                        + " of "
                        + source.size()
                        + " books");
    }

    private void updateTable(List<Book> source, int limit) {
        tableModel.setRowCount(0);

        int count = 0;

        for (Book book : source) {
            if (count >= limit) {
                break;
            }

            tableModel.addRow(book.getDataArray());
            count++;
        }
    }
}