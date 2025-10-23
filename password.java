import java.awt.*;
import java.awt.datatransfer.*;
import java.security.SecureRandom;
import javax.swing.*;

public class PasswordGeneratorBeautifulGUI extends JFrame {

    private JTextField lengthField;
    private JCheckBox upperCaseCheck, lowerCaseCheck, digitsCheck, specialCheck, avoidAmbiguousCheck;
    private JTextField passwordField;
    private JButton generateButton, copyButton, suggestButton; // added suggest button
    private final SecureRandom random = new SecureRandom();

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()-_=+[]{};:,.<>?/";
    private static final String AMBIGUOUS = "O0I1l";

    public PasswordGeneratorBeautifulGUI() {
        // ---- Window Setup ----
        setTitle("🔐 Password Generator Pro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 450);
        setLocationRelativeTo(null); // center window
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(30, 30, 30)); // dark background

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ---- Title ----
        JLabel title = new JLabel("💎 Strong Password Generator");
        title.setFont(new Font("Arial Black", Font.BOLD, 22));
        title.setForeground(Color.CYAN);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        // ---- Password Length ----
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lengthLabel = new JLabel("Password Length:");
        lengthLabel.setForeground(Color.WHITE);
        add(lengthLabel, gbc);

        gbc.gridx = 1;
        lengthField = new JTextField("12");
        add(lengthField, gbc);

        // ---- Options Checkboxes ----
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        upperCaseCheck = createStyledCheckBox("Include Uppercase (A-Z)", true);
        add(upperCaseCheck, gbc);

        gbc.gridy++;
        lowerCaseCheck = createStyledCheckBox("Include Lowercase (a-z)", true);
        add(lowerCaseCheck, gbc);

        gbc.gridy++;
        digitsCheck = createStyledCheckBox("Include Digits (0-9)", true);
        add(digitsCheck, gbc);

        gbc.gridy++;
        specialCheck = createStyledCheckBox("Include Special (!@#$%^&*)", true);
        add(specialCheck, gbc);

        gbc.gridy++;
        avoidAmbiguousCheck = createStyledCheckBox("Avoid Ambiguous (O,0,I,1,l)", true);
        add(avoidAmbiguousCheck, gbc);

        // ---- Generate Button ----
        gbc.gridy++;
        generateButton = new JButton("Generate Password");
        styleButton(generateButton, Color.CYAN, Color.BLACK);
        add(generateButton, gbc);

        // ---- Suggest New Password Button ----
        gbc.gridy++;
        suggestButton = new JButton("Suggest New Password");
        styleButton(suggestButton, Color.ORANGE, Color.BLACK);
        add(suggestButton, gbc);

        // ---- Password Field ----
        gbc.gridy++;
        passwordField = new JTextField();
        passwordField.setEditable(false);
        passwordField.setFont(new Font("Monospaced", Font.BOLD, 16));
        passwordField.setForeground(Color.GREEN);
        passwordField.setBackground(Color.DARK_GRAY);
        add(passwordField, gbc);

        // ---- Copy Button ----
        gbc.gridy++;
        copyButton = new JButton("Copy to Clipboard");
        styleButton(copyButton, Color.MAGENTA, Color.WHITE);
        add(copyButton, gbc);

        // ---- Event Listeners ----
        generateButton.addActionListener(e -> generatePassword());
        suggestButton.addActionListener(e -> generatePassword()); // generates a new password same as generate
        copyButton.addActionListener(e -> copyToClipboard());

        setVisible(true);
    }

    private JCheckBox createStyledCheckBox(String text, boolean selected) {
        JCheckBox cb = new JCheckBox(text, selected);
        cb.setForeground(Color.LIGHT_GRAY);
        cb.setBackground(new Color(30, 30, 30));
        cb.setFont(new Font("Arial", Font.PLAIN, 14));
        return cb;
    }

    private void styleButton(JButton button, Color bg, Color fg) {
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(new Font("Arial Black", Font.BOLD, 14));
        button.setFocusPainted(false);
    }

    private void generatePassword() {
        try {
            int length = Integer.parseInt(lengthField.getText().trim());
            boolean useUpper = upperCaseCheck.isSelected();
            boolean useLower = lowerCaseCheck.isSelected();
            boolean useDigits = digitsCheck.isSelected();
            boolean useSpecial = specialCheck.isSelected();
            boolean avoidAmbiguous = avoidAmbiguousCheck.isSelected();

            String password = generatePassword(length, useUpper, useLower, useDigits, useSpecial, avoidAmbiguous);
            passwordField.setText(password);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void copyToClipboard() {
        String text = passwordField.getText();
        if (!text.isEmpty()) {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
            JOptionPane.showMessageDialog(this, "Password copied to clipboard ✅");
        }
    }

    private String generatePassword(int length, boolean useUpper, boolean useLower,
                                    boolean useDigits, boolean useSpecial, boolean avoidAmbiguous) {
        StringBuilder pool = new StringBuilder();
        if (useUpper) pool.append(UPPER);
        if (useLower) pool.append(LOWER);
        if (useDigits) pool.append(DIGITS);
        if (useSpecial) pool.append(SPECIAL);

        if (pool.isEmpty()) throw new IllegalArgumentException("Select at least one character type!");

        String chars = pool.toString();
        if (avoidAmbiguous) {
            for (char c : AMBIGUOUS.toCharArray()) chars = chars.replace(String.valueOf(c), "");
        }

        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PasswordGeneratorBeautifulGUI::new);
    }
}
