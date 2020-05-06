
import java.text.DecimalFormat;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    Stage myStage;

    Pane root = new Pane();
    Button b0 = new Button("0");
    Button b1 = new Button("1");
    Button b2 = new Button("2");
    Button b3 = new Button("3");
    Button b4 = new Button("4");
    Button b5 = new Button("5");
    Button b6 = new Button("6");
    Button b7 = new Button("7");
    Button b8 = new Button("8");
    Button b9 = new Button("9");
    Button comma = new Button(".");
    Button plus = new Button("+");
    Button minus = new Button("-");
    Button multiple = new Button("×");
    Button divide = new Button("÷");
    Button cos = new Button("cos");
    Button sin = new Button("sin");
    Button tan = new Button("tan");
    Button sqrt = new Button("√");
    Button power = new Button("^");
    Button modulo = new Button("%");
    Button exponential = new Button("e");
    Button pi = new Button("π");
    Button parentesesLeft = new Button("(");
    Button parentesesRight = new Button(")");
    Button equal = new Button("=");
    Button clear = new Button("C");
    Button back = new Button("←");
    TextField textField = new TextField("");
    TextArea historyText = new TextArea();

    MenuBar menuBar = new MenuBar();
    Menu view = new Menu(" View ");
    Menu edit = new Menu(" Edit ");
    Menu help = new Menu(" Help ");
    CheckMenuItem history = new CheckMenuItem("History");
    MenuItem copy = new MenuItem("Copy");
    MenuItem paste = new MenuItem("Paste");
    MenuItem copyHistory = new MenuItem("Copy History");
    MenuItem clearHistory = new MenuItem("Clear History");
    MenuItem keyboardShortcuts = new MenuItem("Keyboard Shortcuts");
    MenuItem about = new MenuItem("About");

    DecimalFormat format = new DecimalFormat("0.###############");

    Alert alert = new Alert(AlertType.INFORMATION);

    private void autoAddOrRemove(String button) {

        if (!textField.getText().isEmpty()) {
            Character lastCharacter = textField.getText().charAt(textField.getText().length() - 1);

            switch (button) {

                case "symbol":
                    switch (lastCharacter) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                        case 'e':
                        case 'π':
                            textField.setText(textField.getText() + "×");
                            break;
                        case '.':
                            textField.setText(textField.getText() + "0×");
                            break;
                    }
                    break;

                case "number":
                    switch (lastCharacter) {
                        case 'e':
                        case 'π':
                            textField.setText(textField.getText() + "×");
                            break;
                        case '0':
                            switch (textField.getText()) {
                                case "0":
                                case "+0":
                                case "-0":
                                case "×0":
                                case "÷0":
                                case "%0":
                                case "^0":
                                case "√0":
                                case "(0":
                                case "cos0":
                                case "sin0":
                                case "tan0":
                                    textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
                            }
                            break;
                    }
                    break;
                case "operand":
                    switch (lastCharacter) {
                        case '+':
                        case '-':
                        case '×':
                        case '÷':
                        case '%':
                        case '.':
                            textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
                            break;
                    }
                    break;

                case "point":
                    switch (lastCharacter) {
                        case '+':
                        case '-':
                        case '×':
                        case '÷':
                        case '%':
                        case '(':
                        case '√':
                        case 'π':
                        case 's':
                        case 'n':
                        case '^':
                            textField.setText(textField.getText() + "0");
                            break;
                        case ')':
                            textField.setText(textField.getText() + "×0");
                            break;
                        case '.':
                            textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
                            break;
                    }
                    break;

            }
        }
    }

    private double calculate(String str) {

        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') {
                    nextChar();
                }
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) {
                    throw new RuntimeException("Unexpected" + (char) ch);
                }
                return x;
            }

            double parseExpression() {
                double x = parseterm();
                for (;;) {
                    if (eat('+')) {
                        x += parseterm();
                    } else if (eat('-')) {
                        x -= parseterm();
                    } else {
                        return x;
                    }
                }
            }

            double parseterm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('×')) {
                        x *= parseFactor();
                    } else if (eat('÷')) {
                        x /= parseFactor();
                    } else if (eat('%')) {
                        x %= parseFactor();
                    } else {
                        return x;
                    }
                }
            }

            double parseFactor() {
                if (eat('+')) {
                    return parseFactor();
                }
                if (eat('-')) {
                    return -parseFactor();
                }
                double x;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if (eat('e')) {
                    x = Math.E;
                } else if (eat('π')) {
                    x = Math.PI;
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') {
                        nextChar();
                    }
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if ((ch >= 'a' && ch <= 'z') || ch == '√') {
                    while ((ch >= 'a' && ch <= 'z') || ch == '√') {
                        nextChar();
                    }
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    switch (func) {
                        case "√":
                            x = Math.sqrt(x);
                            break;
                        case "sin":
                            x = Math.sin(Math.toRadians(x));
                            break;
                        case "cos":
                            x = Math.cos(Math.toRadians(x));
                            break;
                        case "tag":
                            x = Math.tan(Math.toRadians(x));
                            break;
                        default:
                            throw new RuntimeException("Unknown funktion: " + func);
                    }
                } else {
                    throw new RuntimeException("Unexpected" + (char) ch);
                }
                if (eat('^')) {
                    x = Math.pow(x, parseFactor());
                }
                return x;

            }
        }.parse();
    }

    private void b0_isClicked() {
        autoAddOrRemove("number");
        textField.setText(textField.getText() + "0");
    }

    private void b1_isClicked() {
        autoAddOrRemove("number");
        textField.setText(textField.getText() + "1");
    }

    private void b2_isClicked() {
        autoAddOrRemove("number");
        textField.setText(textField.getText() + "2");
    }

    private void b3_isClicked() {
        autoAddOrRemove("number");
        textField.setText(textField.getText() + "3");
    }

    private void b4_isClicked() {
        autoAddOrRemove("number");
        textField.setText(textField.getText() + "4");
    }

    private void b5_isClicked() {
        autoAddOrRemove("number");
        textField.setText(textField.getText() + "5");
    }

    private void b6_isClicked() {
        autoAddOrRemove("number");
        textField.setText(textField.getText() + "6");
    }

    private void b7_isClicked() {
        autoAddOrRemove("number");
        textField.setText(textField.getText() + "7");
    }

    private void b8_isClicked() {
        autoAddOrRemove("number");
        textField.setText(textField.getText() + "8");
    }

    private void b9_isClicked() {
        autoAddOrRemove("number");
        textField.setText(textField.getText() + "9");
    }

    private void sin_isClicked() {
        autoAddOrRemove("symbol");
        textField.setText(textField.getText() + "sin");
    }

    private void cos_isClicked() {
        autoAddOrRemove("symbol");
        textField.setText(textField.getText() + "cos");
    }

    private void tan_isClicked() {
        autoAddOrRemove("symbol");
        textField.setText(textField.getText() + "tan");
    }

    private void sqrt_isClicked() {
        autoAddOrRemove("symbol");
        textField.setText(textField.getText() + "√");
    }

    private void exponential_isClicked() {
        autoAddOrRemove("symbol");
        textField.setText(textField.getText() + "e");
    }

    private void pi_isClicked() {
        autoAddOrRemove("symbol");
        textField.setText(textField.getText() + "π");
    }

    private void plus_isClicked() {
        autoAddOrRemove("operand");
        textField.setText(textField.getText() + "+");
    }

    private void minus_isClicked() {
        autoAddOrRemove("operand");
        textField.setText(textField.getText() + "-");
    }

    private void multiple_isClicked() {
        if (!textField.getText().isEmpty()) {
            autoAddOrRemove("operand");
            textField.setText(textField.getText() + "×");
        }
    }

    private void divide_isClicked() {
        if (!textField.getText().isEmpty()) {
            autoAddOrRemove("operand");
            textField.setText(textField.getText() + "÷");
        }
    }

    private void modulo_isClicked() {
        if (!textField.getText().isEmpty()) {
            autoAddOrRemove("operand");
            textField.setText(textField.getText() + "%");
        }
    }

    private void power_isClicked() {
        if (textField.getText().matches(".*[0-9eπ]$")) {
            textField.setText(textField.getText() + "^");
        }
    }

    private void parentesesRight_isClicked() {
        if (textField.getText().matches(".*[^ns√(]$")) {
            int leftParentesesCounter = 0, rightParentesesCounter = 0;
            for (char c : textField.getText().toCharArray()) {
                if (c == '(') {
                    leftParentesesCounter++;
                } else if (c == ')') {
                    rightParentesesCounter++;
                }
            }
            if (leftParentesesCounter > rightParentesesCounter) {
                textField.setText(textField.getText() + ")");
            }
        }
    }

    private void parentesesLeft_isClicked() {
        autoAddOrRemove("symbol");
        textField.setText(textField.getText() + "(");
    }

    private void comma_isClicked() {
        String str = textField.getText();
        if (textField.getText().isEmpty()) {
            textField.setText("0.");
        } else {
            int lastPointIndex = str.lastIndexOf(".");
            int lastPlusIndex = str.lastIndexOf("+");
            int lastMinustIndex = str.lastIndexOf("-");
            int lastMultipleIndex = str.lastIndexOf("×");
            int lastDivideIndex = str.lastIndexOf("÷");
            int lastModuloIndex = str.lastIndexOf("%");

            if (lastPointIndex <= lastPlusIndex
                    || lastPointIndex <= lastMinustIndex
                    || lastPointIndex <= lastMultipleIndex
                    || lastPointIndex <= lastDivideIndex
                    || lastPointIndex <= lastModuloIndex) {
                autoAddOrRemove("point");
                textField.setText(textField.getText() + ".");
            }
        }
    }

    private void equal_isClicked() {
        if (!textField.getText().isEmpty()) {
            String historyNewText = historyText.getText() + textField.getText() + "\n";
            try {
                Double answer = calculate(textField.getText());
                if (answer.isInfinite()) {
                    textField.setText("cannot divide by 0");
                    historyNewText += "cannot divide by 0";
                } else if (answer.isNaN()) {
                    textField.setText("Error");
                    historyNewText += "Error";
                } else {
                    textField.setText(format.format(answer));
                    historyNewText += format.format(answer);
                }
            } catch (Exception ex) {
                textField.setText("Error");
                historyNewText += "Error";
            }
            historyText.setText(historyNewText + "\n\n");
        }
    }

    private void back_isClicked() {
        String temp = textField.getText();
        if (temp.equals("Error") || temp.equals("cannot divide by 0")) {
            textField.setText("");
        } // ***
        else if (!temp.isEmpty()) {
            temp = textField.getText().substring(0, textField.getText().length() - 1);
            if (temp.length() >= 2) {
                switch (temp.substring(temp.length() - 2)) {
                    case "co":
                    case "si":
                    case "ta":
                        temp = temp.substring(0, textField.getText().length() - 2);
                        break;
                }
            }
            textField.setText(temp);
        }
    }

    private void clear_isClicked() {
        textField.setText("");
    }

    @Override
    public void start(Stage stage) {

        menuBar.getMenus().add(view);
        menuBar.getMenus().add(edit);
        menuBar.getMenus().add(help);
        view.getItems().add(history);
        edit.getItems().add(copy);
        edit.getItems().add(paste);
        edit.getItems().add(new SeparatorMenuItem());
        edit.getItems().add(copyHistory);
        edit.getItems().add(clearHistory);
        help.getItems().add(keyboardShortcuts);
        help.getItems().add(about);

        historyText.setPrefSize(256, 311);
        historyText.setTranslateX(260);
        historyText.setTranslateY(33);
        textField.setPrefSize(234, 60);
        textField.setTranslateX(11);
        textField.setTranslateY(33);
        cos.setPrefSize(45, 38);
        cos.setTranslateX(10);
        cos.setTranslateY(101);
        sin.setPrefSize(45, 38);
        sin.setTranslateX(58);
        sin.setTranslateY(101);
        tan.setPrefSize(45, 38);
        tan.setTranslateX(106);
        tan.setTranslateY(101);
        back.setPrefSize(45, 38);
        back.setTranslateX(154);
        back.setTranslateY(101);
        clear.setPrefSize(45, 38);
        clear.setTranslateX(202);
        clear.setTranslateY(101);
        pi.setPrefSize(45, 38);
        pi.setTranslateX(10);
        pi.setTranslateY(142);
        exponential.setPrefSize(45, 38);
        exponential.setTranslateX(58);
        exponential.setTranslateY(142);
        modulo.setPrefSize(45, 38);
        modulo.setTranslateX(106);
        modulo.setTranslateY(142);
        parentesesLeft.setPrefSize(45, 38);
        parentesesLeft.setTranslateX(154);
        parentesesLeft.setTranslateY(142);
        parentesesRight.setPrefSize(45, 38);
        parentesesRight.setTranslateX(202);
        parentesesRight.setTranslateY(142);
        b7.setPrefSize(45, 38);
        b7.setTranslateX(10);
        b7.setTranslateY(183);
        b8.setPrefSize(45, 38);
        b8.setTranslateX(58);
        b8.setTranslateY(183);
        b9.setPrefSize(45, 38);
        b9.setTranslateX(106);
        b9.setTranslateY(183);
        plus.setPrefSize(45, 38);
        plus.setTranslateX(154);
        plus.setTranslateY(183);
        power.setPrefSize(45, 38);
        power.setTranslateX(202);
        power.setTranslateY(183);
        b4.setPrefSize(45, 38);
        b4.setTranslateX(10);
        b4.setTranslateY(224);
        b5.setPrefSize(45, 38);
        b5.setTranslateX(58);
        b5.setTranslateY(224);
        b6.setPrefSize(45, 38);
        b6.setTranslateX(106);
        b6.setTranslateY(224);
        minus.setPrefSize(45, 38);
        minus.setTranslateX(154);
        minus.setTranslateY(224);
        sqrt.setPrefSize(45, 38);
        sqrt.setTranslateX(202);
        sqrt.setTranslateY(224);
        b1.setPrefSize(45, 38);
        b1.setTranslateX(10);
        b1.setTranslateY(265);
        b2.setPrefSize(45, 38);
        b2.setTranslateX(58);
        b2.setTranslateY(265);
        b3.setPrefSize(45, 38);
        b3.setTranslateX(106);
        b3.setTranslateY(265);
        multiple.setPrefSize(45, 38);
        multiple.setTranslateX(154);
        multiple.setTranslateY(265);
        equal.setPrefSize(45, 79);
        equal.setTranslateX(202);
        equal.setTranslateY(265);
        b0.setPrefSize(93, 38);
        b0.setTranslateX(10);
        b0.setTranslateY(306);
        comma.setPrefSize(45, 38);
        comma.setTranslateX(106);
        comma.setTranslateY(306);
        divide.setPrefSize(45, 38);
        divide.setTranslateX(154);
        divide.setTranslateY(306);
        menuBar.setPrefSize(5000, 20);

        textField.setAlignment(Pos.CENTER_RIGHT);

        b0.setId("b0");
        b1.setId("b1");
        b2.setId("b2");
        b3.setId("b3");
        b4.setId("b4");
        b5.setId("b5");
        b6.setId("b6");
        b7.setId("b7");
        b8.setId("b8");
        b9.setId("b9");
        comma.setId("comma");
        equal.setId("equal");
        plus.setId("plus");
        minus.setId("minus");
        multiple.setId("multiple");
        divide.setId("divide");
        cos.setId("cos");
        sin.setId("sin");
        tan.setId("tan");
        sqrt.setId("sqrt");
        power.setId("power");
        modulo.setId("modulo");
        exponential.setId("exponential");
        pi.setId("pi");
        parentesesLeft.setId("parentesesLeft");
        parentesesRight.setId("parentesesRight");
        clear.setId("clear");
        back.setId("back");
        textField.setId("textField");
        historyText.setId("historyText");
        menuBar.setId("menuBar");
        root.setId("root");

        textField.setFocusTraversable(false);
        textField.setEditable(false);
        b0.setFocusTraversable(false);
        b1.setFocusTraversable(false);
        b2.setFocusTraversable(false);
        b3.setFocusTraversable(false);
        b4.setFocusTraversable(false);
        b5.setFocusTraversable(false);
        b6.setFocusTraversable(false);
        b7.setFocusTraversable(false);
        b8.setFocusTraversable(false);
        b9.setFocusTraversable(false);
        comma.setFocusTraversable(false);
        equal.setFocusTraversable(false);
        plus.setFocusTraversable(false);
        minus.setFocusTraversable(false);
        multiple.setFocusTraversable(false);
        divide.setFocusTraversable(false);
        cos.setFocusTraversable(false);
        sin.setFocusTraversable(false);
        tan.setFocusTraversable(false);
        sqrt.setFocusTraversable(false);
        power.setFocusTraversable(false);
        modulo.setFocusTraversable(false);
        exponential.setFocusTraversable(false);
        pi.setFocusTraversable(false);
        parentesesLeft.setFocusTraversable(false);
        parentesesRight.setFocusTraversable(false);
        clear.setFocusTraversable(false);
        back.setFocusTraversable(false);
        historyText.setFocusTraversable(false);
        historyText.setEditable(false);

        EventHandler<ActionEvent> eventHandler = (ActionEvent e) -> {
            actionPerformed(e);
        };

        history.addEventHandler(ActionEvent.ACTION, eventHandler);
        copy.addEventHandler(ActionEvent.ACTION, eventHandler);
        paste.addEventHandler(ActionEvent.ACTION, eventHandler);
        copyHistory.addEventHandler(ActionEvent.ACTION, eventHandler);
        clearHistory.addEventHandler(ActionEvent.ACTION, eventHandler);
        about.addEventHandler(ActionEvent.ACTION, eventHandler);
        keyboardShortcuts.addEventHandler(ActionEvent.ACTION, eventHandler);
        b0.addEventHandler(ActionEvent.ACTION, eventHandler);
        b1.addEventHandler(ActionEvent.ACTION, eventHandler);
        b2.addEventHandler(ActionEvent.ACTION, eventHandler);
        b3.addEventHandler(ActionEvent.ACTION, eventHandler);
        b4.addEventHandler(ActionEvent.ACTION, eventHandler);
        b5.addEventHandler(ActionEvent.ACTION, eventHandler);
        b6.addEventHandler(ActionEvent.ACTION, eventHandler);
        b7.addEventHandler(ActionEvent.ACTION, eventHandler);
        b8.addEventHandler(ActionEvent.ACTION, eventHandler);
        b9.addEventHandler(ActionEvent.ACTION, eventHandler);
        comma.addEventHandler(ActionEvent.ACTION, eventHandler);
        equal.addEventHandler(ActionEvent.ACTION, eventHandler);
        plus.addEventHandler(ActionEvent.ACTION, eventHandler);
        multiple.addEventHandler(ActionEvent.ACTION, eventHandler);
        minus.addEventHandler(ActionEvent.ACTION, eventHandler);
        divide.addEventHandler(ActionEvent.ACTION, eventHandler);
        cos.addEventHandler(ActionEvent.ACTION, eventHandler);
        sin.addEventHandler(ActionEvent.ACTION, eventHandler);
        tan.addEventHandler(ActionEvent.ACTION, eventHandler);
        sqrt.addEventHandler(ActionEvent.ACTION, eventHandler);
        power.addEventHandler(ActionEvent.ACTION, eventHandler);
        modulo.addEventHandler(ActionEvent.ACTION, eventHandler);
        exponential.addEventHandler(ActionEvent.ACTION, eventHandler);
        pi.addEventHandler(ActionEvent.ACTION, eventHandler);
        parentesesLeft.addEventHandler(ActionEvent.ACTION, eventHandler);
        parentesesRight.addEventHandler(ActionEvent.ACTION, eventHandler);
        clear.addEventHandler(ActionEvent.ACTION, eventHandler);
        back.addEventHandler(ActionEvent.ACTION, eventHandler);

        root.getChildren().add(menuBar);
        root.getChildren().add(b0);
        root.getChildren().add(b1);
        root.getChildren().add(b2);
        root.getChildren().add(b3);
        root.getChildren().add(b4);
        root.getChildren().add(b5);
        root.getChildren().add(b6);
        root.getChildren().add(b7);
        root.getChildren().add(b8);
        root.getChildren().add(b9);
        root.getChildren().add(comma);
        root.getChildren().add(equal);
        root.getChildren().add(plus);
        root.getChildren().add(multiple);
        root.getChildren().add(minus);
        root.getChildren().add(divide);
        root.getChildren().add(cos);
        root.getChildren().add(sin);
        root.getChildren().add(tan);
        root.getChildren().add(sqrt);
        root.getChildren().add(power);
        root.getChildren().add(modulo);
        root.getChildren().add(exponential);
        root.getChildren().add(pi);
        root.getChildren().add(parentesesRight);
        root.getChildren().add(parentesesLeft);
        root.getChildren().add(clear);
        root.getChildren().add(back);
        root.getChildren().add(textField);
        root.getChildren().add(historyText);

        Scene scene = new Scene(root, 247, 343);

        scene.getStylesheets().add("css/style.css");

        myStage = stage;

        myStage.setTitle("Scientific Calculator");

        myStage.setScene(scene);
        myStage.setResizable(false);

        myStage.show();

        scene.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent e) -> {

            if (null != e.getCode()) {

                switch (e.getCode()) {
                    case NUMPAD0:
                        b0_isClicked();
                        break;

                    case NUMPAD1:
                        b1_isClicked();
                        break;

                    case NUMPAD2:
                        b2_isClicked();
                        break;

                    case NUMPAD3:
                        b3_isClicked();
                        break;

                    case NUMPAD4:
                        b4_isClicked();
                        break;

                    case NUMPAD5:
                        b5_isClicked();
                        break;

                    case NUMPAD6:
                        b6_isClicked();
                        break;

                    case NUMPAD7:
                        b7_isClicked();
                        break;

                    case NUMPAD8:
                        b8_isClicked();
                        break;

                    case NUMPAD9:
                        b9_isClicked();
                        break;

                    case S:
                        sin_isClicked();
                        break;

                    case C:
                        cos_isClicked();
                        break;

                    case T:
                        tan_isClicked();
                        break;

                    case V:
                        sqrt_isClicked();
                        break;

                    case E:
                        exponential_isClicked();
                        break;

                    case P:
                        pi_isClicked();
                        break;

                    case PLUS:
                        plus_isClicked();
                        break;

                    case MINUS:
                        minus_isClicked();
                        break;

                    case MULTIPLY:
                        multiple_isClicked();
                        break;

                    case DIVIDE:
                        divide_isClicked();
                        break;

                    case DIGIT5:
                        modulo_isClicked();
                        break;

                    case DIGIT6:
                        power_isClicked();
                        break;

                    case DIGIT0:
                        parentesesRight_isClicked();
                        break;

                    case DIGIT9:
                        parentesesLeft_isClicked();
                        break;

                    case COMMA:
                        comma_isClicked();
                        break;

                    case ENTER:
                        equal_isClicked();
                        break;

                    case BACK_SPACE:
                        back_isClicked();
                        break;

                    case DELETE:
                        clear_isClicked();
                        break;
                }
            }
        });
    }

    private void actionPerformed(ActionEvent e) {

        if (e.getSource() == b0) {
            b0_isClicked();

        } else if (e.getSource() == b1) {
            b1_isClicked();

        } else if (e.getSource() == b2) {
            b2_isClicked();

        } else if (e.getSource() == b3) {
            b3_isClicked();

        } else if (e.getSource() == b4) {
            b4_isClicked();

        } else if (e.getSource() == b5) {
            b5_isClicked();

        } else if (e.getSource() == b6) {
            b6_isClicked();

        } else if (e.getSource() == b7) {
            b7_isClicked();

        } else if (e.getSource() == b8) {
            b8_isClicked();

        } else if (e.getSource() == b9) {
            b9_isClicked();

        } else if (e.getSource() == sin) {
            sin_isClicked();

        } else if (e.getSource() == cos) {
            cos_isClicked();

        } else if (e.getSource() == tan) {
            tan_isClicked();

        } else if (e.getSource() == sqrt) {
            sqrt_isClicked();

        } else if (e.getSource() == exponential) {
            exponential_isClicked();

        } else if (e.getSource() == pi) {
            pi_isClicked();

        } else if (e.getSource() == plus) {
            plus_isClicked();

        } else if (e.getSource() == minus) {
            minus_isClicked();

        } else if (e.getSource() == multiple) {
            multiple_isClicked();

        } else if (e.getSource() == divide) {
            divide_isClicked();

        } else if (e.getSource() == modulo) {
            modulo_isClicked();

        } else if (e.getSource() == power) {
            power_isClicked();

        } else if (e.getSource() == parentesesRight) {
            parentesesRight_isClicked();

        } else if (e.getSource() == parentesesLeft) {
            parentesesLeft_isClicked();

        } else if (e.getSource() == comma) {
            comma_isClicked();

        } else if (e.getSource() == equal) {
            equal_isClicked();

        } else if (e.getSource() == back) {
            back_isClicked();

        } else if (e.getSource() == clear) {
            clear_isClicked();
        } else if (e.getSource() == history) {
            if (history.isSelected()) {
                myStage.setWidth(532);
            } else {
                myStage.setWidth(263);
            }
        } else if (e.getSource() == copy) {
            textField.selectAll();
            textField.copy();
            textField.positionCaret(textField.getText().length());
        } else if (e.getSource() == paste) {
            textField.paste();
        } else if (e.getSource() == copyHistory) {
            historyText.selectAll();
            historyText.copy();
            textField.positionCaret(textField.getText().length());

        } else if (e.getSource() == clearHistory) {
            historyText.setText("");
        } else if (e.getSource() == keyboardShortcuts) {
            String str
                    = "Press V to add √.\n"
                    + "Press P to add π.\n"
                    + "Press C to add cos.\n"
                    + "Press S to add sin.\n"
                    + "Press T to add tan.\n"
                    + "Press = Or Enter to get the result.\n"
                    + "Press BackSpace to clear last character enterd.\n"
                    + "Press Delete to clear all character enterd.";

            alert.setTitle("Keyboard Shortcuts");
            alert.setHeaderText("Keyboard Shortcuts");
            alert.setContentText(str);
            alert.showAndWait();
        } else if (e.getSource() == about) {
            String str
                    = "Scientific Calculator\n\n"
                    + "Prepared by Majd Talji\n\n"
                    + "If you have any comments, ideas.. just let know\n\n"
                    + "E-Mail:  en.majd.talji@gmail.com\n"
                    + "twitter & facebook: @... \n\n"
                    + "Note\n"
                    + "I used JDK 1.8 to compile the source code.\n\n"
                    + "© Copyright 2019 majd.com - All Rights Reserved";

            alert.setTitle("About");
            alert.setHeaderText("About");
            alert.setContentText(str);
            alert.showAndWait();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
