import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class NumberGuessingGame {
    public static void main(String[] args) {
        // Frame instance
        Frame frame = new Frame("Number Guessing Game");

        // Load background image (make sure the path is correct)
        Image backgroundImage = Toolkit.getDefaultToolkit().getImage("C:\\Users\\Deekshitha\\Desktop\\III sem JAVA Project\\java project code\\img_java.jpg");
        // Create a custom panel with background image
        Panel backgroundPanel = new Panel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                if (backgroundImage != null) {
                    // Draw the image to cover the entire panel
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        // Set layout and size for the frame
        frame.setSize(500, 400);
        frame.setLayout(null);

        // Add background panel to the frame
        backgroundPanel.setBounds(0, 0, 500, 400); // Match the size of the frame
        frame.add(backgroundPanel);

        // Labels
        Label setRangeLabel = new Label("Set range (e.g., 1-100):");
        Label guessLabel = new Label("Enter your guess:");
        Label hintLabel = new Label("Hint: ");
        Label scoreLabel = new Label("Score: 0");
        Label answerLabel = new Label("Answer: ");

        // Text fields
        TextField rangeField = new TextField();
        TextField guessField = new TextField();

        // Buttons
        Button setButton = new Button("Set");
        Button checkButton = new Button("Check");
        Button retryButton = new Button("Retry");
        Button nextButton = new Button("Next");
        Button viewAnswerButton = new Button("View Answer");

        // Setting bounds for components
        setRangeLabel.setBounds(50, 80, 150, 30);
        rangeField.setBounds(220, 80, 200, 30);
        guessLabel.setBounds(50, 140, 150, 30);
        guessField.setBounds(220, 140, 200, 30);
        hintLabel.setBounds(50, 200, 370, 30);
        hintLabel.setBackground(Color.YELLOW);
        scoreLabel.setBounds(50, 250, 370, 30);
        answerLabel.setBounds(310, 340, 120, 30);
        answerLabel.setBackground(Color.PINK);
        setButton.setBounds(50, 300, 80, 30);
        checkButton.setBounds(180, 300, 80, 30);
        retryButton.setBounds(50, 340, 80, 30);
        nextButton.setBounds(180, 340, 80, 30);
        viewAnswerButton.setBounds(310, 300, 120, 30);

        // Add components to the background panel
        backgroundPanel.setLayout(null); // Use null layout for custom positioning
        backgroundPanel.add(setRangeLabel);
        backgroundPanel.add(rangeField);
        backgroundPanel.add(guessLabel);
        backgroundPanel.add(guessField);
        backgroundPanel.add(hintLabel);
        backgroundPanel.add(scoreLabel);
        backgroundPanel.add(answerLabel);
        backgroundPanel.add(setButton);
        backgroundPanel.add(checkButton);
        backgroundPanel.add(retryButton);
        backgroundPanel.add(nextButton);
        backgroundPanel.add(viewAnswerButton);

        // Game variables
        final int[] secretNumber = {0}; // To store the secret number
        final int[] chances = {3};
        final int[] fromRange = {0};
        final int[] toRange = {0};
        final int[] score = {0}; // To store the score
        final boolean[] chancesOver = {false}; // To track if chances are over

        // Button actions
        setButton.addActionListener(e -> {
            String rangeText = rangeField.getText();
            try {
                String[] bounds = rangeText.split("-");
                fromRange[0] = Integer.parseInt(bounds[0].trim());
                toRange[0] = Integer.parseInt(bounds[1].trim());

                if (toRange[0] - fromRange[0] < 10) {
                    hintLabel.setText("Hint: Range difference must be > 10.");
                    hintLabel.setForeground(Color.RED);
                } else {
                    secretNumber[0] = generateRandomNumber(fromRange[0], toRange[0]);
                    hintLabel.setText("Hint: Range set! You have 3 chances.");
                    hintLabel.setForeground(Color.BLUE);
                    chancesOver[0] = false;
                    answerLabel.setText("Answer: "); // Clear previous answer
                }
            } catch (Exception ex) {
                hintLabel.setText("Hint: Enter a valid range (e.g., 1-100).");
                hintLabel.setForeground(Color.RED);
            }
        });

        checkButton.addActionListener(e -> {
            if (secretNumber[0] == 0) {
                hintLabel.setText("Hint: Set the range first!");
                return;
            }
            try {
                int guess = Integer.parseInt(guessField.getText().trim());
                if (guess < fromRange[0] || guess > toRange[0]) {
                    hintLabel.setText("Hint: Guess out of range!");
                    hintLabel.setForeground(Color.RED);
                    guessField.setText("");
                    return;
                }

                if (chances[0] <= 0) {
                    hintLabel.setText("Hint: No chances left! You lost.");
                    hintLabel.setForeground(Color.RED);
                    retryButton.setEnabled(true);
                    chancesOver[0] = true; // Mark chances as over
                    return;
                }

                if (guess == secretNumber[0]) {
                    hintLabel.setText("Hint: Congratulations! You won!");
                    hintLabel.setForeground(Color.BLUE);
                    score[0] = calculateScore(3 - chances[0]);
                    scoreLabel.setText("Score: " + score[0]);
                    retryButton.setEnabled(true);
                    nextButton.setEnabled(true);
                    chancesOver[0] = true; // Mark chances as over
                } else {
                    chances[0]--;
                    guessField.setText("");
                    if (chances[0] == 0) {
                        hintLabel.setText("Hint: No chances left! You lost.");
                        hintLabel.setForeground(Color.RED);
                        retryButton.setEnabled(true);
                        chancesOver[0] = true; // Mark chances as over
                    } else {
                        int diff = Math.abs(secretNumber[0] - guess);
                        if (diff <= 5) {
                            hintLabel.setText("Hint: Very close! " + chances[0] + " chances left.");
                            hintLabel.setForeground(Color.BLUE);
                        } else {
                            hintLabel.setText("Hint: Far away! " + chances[0] + " chances left.");
                            hintLabel.setForeground(Color.RED);
                        }
                    }
                }
            } catch (Exception ex) {
                hintLabel.setText("Hint: Enter a valid number.");
                hintLabel.setForeground(Color.RED);
            }
        });

        viewAnswerButton.addActionListener(e -> {
            if (chancesOver[0]) {
                answerLabel.setText("Answer: " + secretNumber[0]);
                answerLabel.setForeground(Color.BLACK);
            } else {
                hintLabel.setText("Hint: You are chances are not yet over!");
                hintLabel.setForeground(Color.RED);
            }
        });

        retryButton.addActionListener(e -> {
            rangeField.setText("");
            guessField.setText("");
            hintLabel.setText("Hint: ");
            hintLabel.setForeground(Color.BLACK);
            scoreLabel.setText("Score: 0");
            answerLabel.setText("Answer: "); // Clear answer
            chances[0] = 3;
            secretNumber[0] = 0;
            chancesOver[0] = false;
            retryButton.setEnabled(false);
            nextButton.setEnabled(false);
        });

        nextButton.addActionListener(e -> {
            retryButton.setEnabled(false);
            nextButton.setEnabled(false);
            rangeField.setText("");
            guessField.setText("");
            hintLabel.setText("Hint: Set a new range.");
            hintLabel.setForeground(Color.BLACK);
            scoreLabel.setText("Score: 0");
            answerLabel.setText("Answer: "); // Clear answer
            chances[0] = 3;
            secretNumber[0] = 0;
            chancesOver[0] = false;
        });

        // Close the frame when clicking the close button
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // Frame visibility
        frame.setVisible(true);
    }

    // Backend method to generate random number
    public static int generateRandomNumber(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    // Calculate score based on the number of guesses
    public static int calculateScore(int guessesUsed) {
        switch (guessesUsed) {
            case 0: return 0;
            case 1: return 70;
            case 2: return 35;
            default: return 0;
        }
    }
}
