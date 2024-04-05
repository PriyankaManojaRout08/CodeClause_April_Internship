import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Simple_Calculator extends JFrame implements ActionListener {
private JTextField textField;
private JButton[] numberButtons;
private JButton[] operatorButtons;
private JButton addButton, subButton, mulButton, divButton, eqButton, clrButton, dotButton,
delButton;
private JPanel panel;
private double num1 = 0, num2 = 0, result = 0;
private char operator;
public Simple_Calculator() {
setTitle("Simple Calculator");
setLayout(new BorderLayout());
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setSize(400, 500);
setResizable(false);
setLocationRelativeTo(null);
initializeComponents();
addActionListeners();
setVisible(true);}
private void initializeComponents() {
textField = new JTextField();
textField.setFont(new Font("Arial", Font.PLAIN, 24));
textField.setHorizontalAlignment(JTextField.RIGHT);
textField.setEditable(false);
panel = new JPanel();
panel.setLayout(new GridLayout(4, 4));
numberButtons = new JButton[10];
for (int i = 0; i < 10; i++) {
numberButtons[i] = new JButton(String.valueOf(i));
numberButtons[i].setFont(new Font("Arial", Font.PLAIN, 18));}
operatorButtons = new JButton[4];
addButton = new JButton("+");
subButton = new JButton("-");
mulButton = new JButton("*");
divButton = new JButton("/");
operatorButtons[0] = addButton;
operatorButtons[1] = subButton;
operatorButtons[2] = mulButton;
operatorButtons[3] = divButton;
eqButton = new JButton("=");
clrButton = new JButton("C");
dotButton = new JButton(".");
delButton = new JButton("DEL");
panel.add(numberButtons[7]);
panel.add(numberButtons[8]);
panel.add(numberButtons[9]);
panel.add(divButton);
panel.add(numberButtons[4]);
panel.add(numberButtons[5]);
panel.add(numberButtons[6]);
panel.add(mulButton);
panel.add(numberButtons[1]);
panel.add(numberButtons[2]);
panel.add(numberButtons[3]);
panel.add(subButton);
panel.add(numberButtons[0]);
panel.add(dotButton);
panel.add(eqButton);
panel.add(addButton);
add(textField, BorderLayout.NORTH);
add(panel, BorderLayout.CENTER);
add(delButton, BorderLayout.SOUTH);
add(clrButton, BorderLayout.EAST);}
private void addActionListeners() {
for (int i = 0; i < 10; i++) {
numberButtons[i].addActionListener(this); }
for (JButton operatorButton : operatorButtons) {
operatorButton.addActionListener(this); }
eqButton.addActionListener(this);
clrButton.addActionListener(this);
dotButton.addActionListener(this);
delButton.addActionListener(this); }
public void actionPerformed(ActionEvent e) {
for (int i = 0; i < 10; i++) {
if (e.getSource() == numberButtons[i]) {
textField.setText(textField.getText() + i);}}
if (e.getSource() == dotButton) {
if (!textField.getText().contains(".")) {
textField.setText(textField.getText() + "."); }}
if (e.getSource() == clrButton) {
textField.setText("");
num1 = num2 = result = 0;
operator = '\0';}
if (e.getSource() == delButton) {
String currentText = textField.getText();
if (!currentText.isEmpty()) {
textField.setText(currentText.substring(0, currentText.length() - 1));}}
for (JButton operatorButton : operatorButtons) {
if (e.getSource() == operatorButton) {
num1 = Double.parseDouble(textField.getText());
operator = e.getActionCommand().charAt(0);
textField.setText("");}}
if (e.getSource() == eqButton) {
num2 = Double.parseDouble(textField.getText());
switch (operator) {
case '+':
result = num1 + num2;
break;
case '-':
result = num1 - num2;
break;
case '*':
result = num1 * num2;
break;
case '/':
if (num2 != 0) {
result = num1 / num2;
} else {
textField.setText("Error");
return;}
break;}
textField.setText(String.valueOf(result));
num1 = result;
operator = '\0';}}
public static void main(String[] args) {
Simple_Calculator c = new Simple_Calculator();
}}
